"""Clean, validate, interpolate, and format NASDAQ Composite index data.

The module reads the raw two-column NASDAQ CSV file, validates its dates and
index values, inserts rows for missing calendar dates, interpolates missing
NASDAQCOM values linearly by date, and writes an Excel-dialect CSV file.
"""

from __future__ import annotations

import csv
from pathlib import Path

import pandas as pd


INPUT_FILE = Path("data") / "NASDAQCOM_RAW.csv"
OUTPUT_FILE = Path("data") / "NASDAQCOM.csv"

DATE_COLUMN = "observation_date"
VALUE_COLUMN = "NASDAQCOM"
EXPECTED_COLUMNS = [DATE_COLUMN, VALUE_COLUMN]

MIN_DATE = pd.Timestamp("2023-01-01")
MAX_DATE = pd.Timestamp("2026-07-02")
MIN_INDEX_VALUE = 10_000.0
MAX_INDEX_VALUE = 30_000.0


def _describe_csv_errors(values: pd.Series, mask: pd.Series) -> str:
    """Return offending values paired with their one-based CSV row numbers."""
    problems = []
    for index in values.index[mask]:
        value = values.loc[index]
        display_value = "<missing>" if str(value).strip() == "" else repr(value)
        problems.append(f"row {index + 2}: {display_value}")
    return "; ".join(problems)


def _count_non_increasing_dates(dates: pd.Series) -> int:
    """Return the number of dates not strictly greater than their predecessor."""
    return int(dates.diff().iloc[1:].le(pd.Timedelta(0)).sum())


def _read_and_validate_input(input_file: Path) -> tuple[pd.DataFrame, int]:
    """Read the raw CSV and validate its columns, dates, and supplied values.

    Missing NASDAQCOM values are retained for later interpolation. All supplied
    values must be numeric and within the permitted range.
    """
    try:
        frame = pd.read_csv(
            input_file,
            dtype=str,
            keep_default_na=False,
            skipinitialspace=True,
        )
    except (OSError, pd.errors.ParserError) as exc:
        raise ValueError(f"Unable to read input CSV '{input_file}': {exc}") from exc

    if list(frame.columns) != EXPECTED_COLUMNS:
        raise ValueError(
            f"Input columns must be exactly {EXPECTED_COLUMNS}; "
            f"received {list(frame.columns)}."
        )

    if frame.empty:
        raise ValueError("The input CSV contains no data records.")

    raw_dates = frame[DATE_COLUMN].astype(str).str.strip()
    if raw_dates.eq("").any():
        details = _describe_csv_errors(raw_dates, raw_dates.eq(""))
        raise ValueError(f"Missing observation_date ({details}).")

    parsed_dates = pd.to_datetime(raw_dates, errors="coerce", format="mixed")
    if parsed_dates.isna().any():
        details = _describe_csv_errors(raw_dates, parsed_dates.isna())
        raise ValueError(f"Invalid observation_date ({details}).")
    parsed_dates = parsed_dates.dt.normalize()

    outside_date_range = ~parsed_dates.between(MIN_DATE, MAX_DATE, inclusive="both")
    if outside_date_range.any():
        details = _describe_csv_errors(raw_dates, outside_date_range)
        raise ValueError(
            f"observation_date must be between {MIN_DATE.date()} and "
            f"{MAX_DATE.date()} ({details})."
        )

    if parsed_dates.duplicated().any():
        duplicate_mask = parsed_dates.duplicated(keep=False)
        details = _describe_csv_errors(raw_dates, duplicate_mask)
        raise ValueError(f"observation_date values must be unique ({details}).")

    non_increasing_count = _count_non_increasing_dates(parsed_dates)

    raw_values = frame[VALUE_COLUMN].astype(str).str.strip()
    missing_values = raw_values.eq("")
    numeric_values = pd.to_numeric(raw_values.mask(missing_values), errors="coerce")

    invalid_numeric = ~missing_values & numeric_values.isna()
    if invalid_numeric.any():
        details = _describe_csv_errors(raw_values, invalid_numeric)
        raise ValueError(f"NASDAQCOM must be numeric ({details}).")

    outside_value_range = numeric_values.notna() & ~numeric_values.between(
        MIN_INDEX_VALUE, MAX_INDEX_VALUE, inclusive="both"
    )
    if outside_value_range.any():
        details = _describe_csv_errors(raw_values, outside_value_range)
        raise ValueError(
            f"NASDAQCOM must be between {MIN_INDEX_VALUE:g} and "
            f"{MAX_INDEX_VALUE:g} ({details})."
        )

    validated = pd.DataFrame(
        {DATE_COLUMN: parsed_dates, VALUE_COLUMN: numeric_values.astype(float)}
    )
    return validated, non_increasing_count


def _interpolate_data(frame: pd.DataFrame) -> tuple[pd.DataFrame, int, int]:
    """Insert missing daily rows and interpolate all absent index values.

    Returns the formatted frame, the number of values interpolated, and the
    number of missing date rows inserted.
    """
    ordered = frame.sort_values(DATE_COLUMN).set_index(DATE_COLUMN)
    complete_dates = pd.date_range(ordered.index.min(), ordered.index.max(), freq="D")
    missing_rows = int(len(complete_dates) - len(ordered))
    completed = ordered.reindex(complete_dates)
    interpolations = int(completed[VALUE_COLUMN].isna().sum())

    completed[VALUE_COLUMN] = completed[VALUE_COLUMN].interpolate(
        method="time", limit_area="inside"
    )
    if completed[VALUE_COLUMN].isna().any():
        missing_dates = completed.index[completed[VALUE_COLUMN].isna()]
        rendered = "; ".join(
            f"output row {position + 2}: date {date.strftime('%Y-%m-%d')}, "
            "NASDAQCOM <missing>"
            for position, date in enumerate(completed.index)
            if date in missing_dates
        )
        raise ValueError(
            "NASDAQCOM values at the beginning or end of the date range cannot "
            f"be interpolated ({rendered})."
        )

    invalid_result = ~completed[VALUE_COLUMN].between(
        MIN_INDEX_VALUE, MAX_INDEX_VALUE, inclusive="both"
    )
    if invalid_result.any():
        rendered = "; ".join(
            f"output row {position + 2}: {value!r} on {date.strftime('%Y-%m-%d')}"
            for position, (date, value) in enumerate(completed[VALUE_COLUMN].items())
            if invalid_result.loc[date]
        )
        raise ValueError(f"Interpolated NASDAQCOM value is out of range ({rendered}).")

    completed.index.name = DATE_COLUMN
    completed = completed.reset_index()
    completed[DATE_COLUMN] = completed[DATE_COLUMN].dt.strftime("%Y-%m-%d")
    return completed, interpolations, missing_rows


def _write_output(frame: pd.DataFrame, output_file: Path) -> None:
    """Write the corrected frame using the standard Excel CSV dialect."""
    output_file.parent.mkdir(parents=True, exist_ok=True)
    try:
        with output_file.open("w", newline="", encoding="utf-8") as csv_file:
            writer = csv.writer(csv_file, dialect="excel")
            writer.writerow(EXPECTED_COLUMNS)
            writer.writerows(frame.itertuples(index=False, name=None))
    except OSError as exc:
        raise ValueError(f"Unable to write output CSV '{output_file}': {exc}") from exc


def _format_nasdaq(input_file: Path, output_file: Path) -> None:
    """Orchestrate NASDAQ validation, interpolation, output, and reporting."""
    print("Clean and Format NASDAQ Composite Index Data")
    print(f"Input file:  {input_file}")
    print(f"Output file: {output_file}")

    source, non_increasing = _read_and_validate_input(input_file)
    formatted, interpolations, missing_rows = _interpolate_data(source)
    _write_output(formatted, output_file)

    input_beginning = source[DATE_COLUMN].min().strftime("%Y-%m-%d")
    input_ending = source[DATE_COLUMN].max().strftime("%Y-%m-%d")
    output_beginning = formatted[DATE_COLUMN].iloc[0]
    output_ending = formatted[DATE_COLUMN].iloc[-1]

    print(f"Input beginning date: {input_beginning}")
    print(f"Input ending date: {input_ending}")
    print(f"Output beginning date: {output_beginning}")
    print(f"Output ending date: {output_ending}")
    print(f"Records processed: {len(source)}")
    print(f"Interpolations performed: {interpolations}")
    print(f"Missing rows added: {missing_rows}")
    print(f"Records written: {len(formatted)}")
    print(f"Dates not monotonically increasing: {non_increasing}")
    print("Done.")


def main() -> None:
    """Run the formatter with the module's input and output file constants."""
    _format_nasdaq(INPUT_FILE, OUTPUT_FILE)


if __name__ == "__main__":
    main()
