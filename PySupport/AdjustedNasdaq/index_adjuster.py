"""Create an inflation-adjusted NASDAQ Composite index CSV file.

The module combines daily NASDAQ Composite closing values with daily estimates
of the Chained Consumer Price Index for All Urban Consumers (C-CPI-U), series
SUUR0000SA0.  The most recent CPI observation is the base-period CPI.  A
historical index value is expressed in base-date dollars as::

    adjusted index = historical index * base CPI / historical CPI

The default input and output paths follow the project specification.  Input
files use the Excel CSV dialect.  Invalid file structure or ordering raises an
exception; a NASDAQ date without a corresponding CPI observation is reported
and skipped so processing can continue.
"""

from __future__ import annotations

import csv
from collections import defaultdict
from datetime import date, datetime, timedelta
from pathlib import Path
from statistics import fmean
from typing import Iterable


_NASDAQ_FILE = Path("data/NASDAQCOM.csv")
_CPI_FILE = Path("data/daily_cpi.csv")
_OUTPUT_FILE = Path("data/NASDAQCOM_ADJUSTED.csv")


def _parse_date(value: str, *, source: str, line_number: int) -> date:
    """Parse an ISO date and add file context to validation errors."""
    try:
        parsed = datetime.strptime(value.strip(), "%Y-%m-%d").date()
    except (AttributeError, ValueError) as exc:
        raise ValueError(
            f"{source}, line {line_number}: invalid date {value!r}; "
            "expected yyyy-mm-dd"
        ) from exc
    return parsed


def _parse_number(
    value: str, *, field: str, source: str, line_number: int, positive: bool
) -> float:
    """Parse and validate a finite, non-negative or positive number."""
    try:
        number = float(value)
    except (TypeError, ValueError) as exc:
        raise ValueError(
            f"{source}, line {line_number}: {field} must be numeric"
        ) from exc

    lower_bound_ok = number > 0 if positive else number >= 0
    if not lower_bound_ok or number in (float("inf"), float("-inf")) or number != number:
        requirement = "greater than zero" if positive else "non-negative"
        raise ValueError(
            f"{source}, line {line_number}: {field} must be finite and {requirement}"
        )
    return number


def _read_csv(
    filename: str | Path,
    *,
    date_column: str,
    value_column: str,
    positive_value: bool,
) -> list[tuple[date, float]]:
    """Read and validate one ordered, unique, two-column input CSV file."""
    path = Path(filename)
    records: list[tuple[date, float]] = []

    with path.open("r", encoding="utf-8-sig", newline="") as input_file:
        reader = csv.DictReader(input_file, dialect="excel")
        expected = [date_column, value_column]
        if reader.fieldnames != expected:
            raise ValueError(
                f"{path}: expected columns {expected!r}, found {reader.fieldnames!r}"
            )

        previous_date: date | None = None
        for line_number, row in enumerate(reader, start=2):
            effective_date = _parse_date(
                row[date_column], source=str(path), line_number=line_number
            )
            value = _parse_number(
                row[value_column],
                field=value_column,
                source=str(path),
                line_number=line_number,
                positive=positive_value,
            )

            if previous_date is not None and effective_date <= previous_date:
                issue = "duplicate" if effective_date == previous_date else "out-of-order"
                raise ValueError(
                    f"{path}, line {line_number}: {issue} date "
                    f"{effective_date.isoformat()}"
                )
            if (
                previous_date is not None
                and effective_date != previous_date + timedelta(days=1)
            ):
                first_missing = previous_date + timedelta(days=1)
                raise ValueError(
                    f"{path}, line {line_number}: missing date "
                    f"{first_missing.isoformat()} before {effective_date.isoformat()}"
                )
            records.append((effective_date, value))
            previous_date = effective_date

    if not records:
        raise ValueError(f"{path}: input file contains no data records")
    return records


def _adjust_index(
    effective_date: date, base_cpi: float, effective_cpi: float, index_value: float
) -> float:
    """Return an index value restated in base-date dollars."""
    context = effective_date.isoformat() if isinstance(effective_date, date) else str(effective_date)
    if not isinstance(effective_date, date):
        raise ValueError(f"{context}: effective date must be a date")

    for name, value, allow_zero in (
        ("base CPI", base_cpi, False),
        ("effective CPI", effective_cpi, False),
        ("index value", index_value, True),
    ):
        if not isinstance(value, (int, float)) or isinstance(value, bool):
            raise ValueError(f"{context}: {name} must be numeric")
        if value != value or value in (float("inf"), float("-inf")):
            raise ValueError(f"{context}: {name} must be finite")
        if value < 0 or (not allow_zero and value == 0):
            qualifier = "non-negative" if allow_zero else "greater than zero"
            raise ValueError(f"{context}: {name} must be {qualifier}")

    adjusted = index_value * base_cpi / effective_cpi
    if adjusted < 0 or adjusted != adjusted or adjusted == float("inf"):
        raise ValueError(f"{context}: computed adjusted index value is invalid")
    return adjusted


def _print_file_summary(label: str, records: list[tuple[date, float]]) -> None:
    """Display record count and date bounds for an input file."""
    print(f"{label} records read: {len(records)}")
    print(f"{label} date range: {records[0][0].isoformat()} through {records[-1][0].isoformat()}")


def _monthly_averages(
    records: Iterable[tuple[date, float]],
) -> dict[str, float]:
    """Return average values grouped by calendar month."""
    groups: dict[str, list[float]] = defaultdict(list)
    for effective_date, value in records:
        groups[effective_date.strftime("%Y-%m")].append(value)
    return {month: fmean(values) for month, values in groups.items()}


def _print_monthly_comparison(
    original: list[tuple[date, float]], adjusted: list[tuple[date, float]]
) -> None:
    """Display monthly averages and the inflation adjustment percentage."""
    original_averages = _monthly_averages(original)
    adjusted_averages = _monthly_averages(adjusted)
    print("Monthly average NASDAQ Composite values:")
    print("month,uncorrected,corrected,percent_difference")
    for month in original_averages:
        uncorrected = original_averages[month]
        corrected = adjusted_averages.get(month)
        corrected_text = f"{corrected:.4f}" if corrected is not None else "N/A"
        if corrected is None or uncorrected == 0:
            percent_difference_text = "N/A"
        else:
            percent_difference = (corrected - uncorrected) / uncorrected * 100
            percent_difference_text = f"{percent_difference:.4f}%"
        print(
            f"{month},{uncorrected:.4f},{corrected_text},"
            f"{percent_difference_text}"
        )


def _write_output(
    filename: str | Path, records: list[tuple[date, float]]
) -> None:
    """Write inflation-adjusted records using the Excel CSV dialect."""
    path = Path(filename)
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8", newline="") as output_file:
        writer = csv.writer(output_file, dialect="excel")
        writer.writerow(["observation_date", "NASDAQCOM"])
        for effective_date, value in records:
            writer.writerow([effective_date.isoformat(), f"{value:.4f}"])


def _create_adjusted_file(
    nasdaq_file: str | Path, cpi_file: str | Path, output_file: str | Path
) -> None:
    """Sequence input validation, adjustment, reporting, and output creation."""
    print("Creating an inflation-adjusted NASDAQ Composite index file.")
    print(f"NASDAQ input: {nasdaq_file}")
    print(f"CPI input: {cpi_file}")
    print(f"Adjusted output: {output_file}")

    nasdaq_records = _read_csv(
        nasdaq_file,
        date_column="observation_date",
        value_column="NASDAQCOM",
        positive_value=False,
    )
    cpi_records = _read_csv(
        cpi_file,
        date_column="date",
        value_column="cpi",
        positive_value=True,
    )
    _print_file_summary("NASDAQ", nasdaq_records)
    _print_file_summary("CPI", cpi_records)

    cpi_by_date = dict(cpi_records)
    nasdaq_dates = {effective_date for effective_date, _ in nasdaq_records}
    base_date, base_cpi = cpi_records[-1]
    print(f"Inflation base date: {base_date.isoformat()} (CPI {base_cpi:.4f})")

    adjusted_records: list[tuple[date, float]] = []
    matched_original: list[tuple[date, float]] = []
    skipped_nasdaq_count = 0
    for effective_date, index_value in nasdaq_records:
        effective_cpi = cpi_by_date.get(effective_date)
        if effective_cpi is None:
            print(
                "ERROR: NASDAQ date has no matching CPI record; skipped: "
                f"{effective_date.isoformat()}"
            )
            skipped_nasdaq_count += 1
            continue
        matched_original.append((effective_date, index_value))
        adjusted_records.append(
            (
                effective_date,
                _adjust_index(effective_date, base_cpi, effective_cpi, index_value),
            )
        )

    for effective_date, _ in cpi_records:
        if effective_date not in nasdaq_dates:
            print(f"Unused CPI date: {effective_date.isoformat()}")

    _write_output(output_file, adjusted_records)
    print(f"Adjusted records written: {len(adjusted_records)}")
    _print_monthly_comparison(matched_original, adjusted_records)
    print(f"NASDAQ records skipped without matching CPI values: {skipped_nasdaq_count}")
    print("Done.")


def main() -> None:
    """Create the adjusted file using the project-standard file locations."""
    _create_adjusted_file(_NASDAQ_FILE, _CPI_FILE, _OUTPUT_FILE)


if __name__ == "__main__":
    main()
