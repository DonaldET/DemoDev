"""Clean and augment Chained Urban CPI data with forecasted monthly values.

This module reads the Bureau of Labor Statistics Chained Urban CPI source file,
selects the requested series and year range, restructures the data into
``date`` and ``cpi`` columns, calls ``forecast_cpi`` to append future values,
and writes the augmented data to a CSV file.
"""

from __future__ import annotations

from pathlib import Path

import pandas as pd

from forecast import forecast_cpi
from interpolate import fill_in_missing

DEFAULT_INPUT_FILE = r"data\su.data.1.AllItems.csv"
DEFAULT_OUTPUT_FILE = r"data\nasdaq_chained_projected.csv"
DEFAULT_SERIES = "SUUR0000SA0"
DEFAULT_START_YEAR = 2023
DEFAULT_END_YEAR = 2026
DEFAULT_FUTURE_DATES = ["2026-06-01", "2026-07-01"]


def _read_and_clean_cpi(
        input_file: str,
        series: str,
        start_year: int,
        end_year: int,
) -> pd.DataFrame:
    """Read, filter, and restructure the source CPI data.

    Args:
        input_file: Path to the source CSV file.
        series: CPI series identifier to retain.
        start_year: First year to retain, inclusive.
        end_year: Last year to retain, inclusive.

    Returns:
        A dataframe containing only ``date`` and ``cpi`` columns, sorted by
        date in ascending order.

    Raises:
        FileNotFoundError: If the source CSV file does not exist.
        ValueError: If required columns are missing or retained values are
            invalid.
    """
    print("Entering _read_and_clean_cpi. . .")
    print(f"-- Reading CPI input data from CSV file: {input_file!r}")
    input_path = Path(input_file)
    if not input_path.is_file():
        raise FileNotFoundError(f"CPI input file not found: {input_path}")

    input_df = pd.read_csv(input_path)
    print(f"-- Raw data from {input_file!r}; shape is {input_df.shape}; index.df={input_df.index}")

    required_columns = {"series", "year", "month", "value", "footnote"}
    missing_columns = required_columns.difference(input_df.columns)
    if missing_columns:
        missing = ", ".join(sorted(missing_columns))
        raise ValueError(f"Input CSV is missing required columns: {missing}")

    input_df["year"] = pd.to_numeric(input_df["year"], errors="coerce")
    input_df["month"] = pd.to_numeric(input_df["month"], errors="coerce")
    input_df["value"] = pd.to_numeric(input_df["value"], errors="coerce")

    selected_rows = (
            input_df["series"].astype(str).str.strip().eq(series)
            & input_df["year"].between(start_year, end_year, inclusive="both")
    )
    df = input_df.loc[selected_rows].copy()
    if df.empty:
        raise ValueError(
            f"No CPI records found for series {series!r} "
            f"between {start_year} and {end_year}."
        )
    print(f"-- Filtered {df.shape} from {input_df.shape}")

    invalid_months = ~df["month"].between(1, 12, inclusive="both")
    if invalid_months.any():
        raise ValueError("The filtered CPI data contains invalid month values.")

    if df[["year", "month", "value"]].isna().any().any():
        raise ValueError(
            "The filtered CPI data contains non-numeric or missing year, "
            "month, or value entries."
        )

    df["date"] = pd.to_datetime(
        {
            "year": df["year"].astype(int),
            "month": df["month"].astype(int),
            "day": 1
        }
    )
    if df["date"].duplicated().any():
        raise ValueError("The filtered CPI data contains duplicate monthly dates.")
    if not df["date"].is_monotonic_increasing and df["date"].is_unique:
        raise ValueError("The filtered CPI data contains duplicate or non-monotonic dates.")

    dates: [pd.Series] = df["date"]
    print(f"-- Extracted and formatted dates [{len(dates)}].")

    df = pd.DataFrame(
        {
            "date": list(dates),
            "cpi": df["value"]
        }
    )
    df.set_index(df["date"], inplace=True)
    df.sort_index(inplace=True)

    print(".................................................")
    print(f"-- Read CSV and corrected filtered input for series {series!r}, returning shape: {df.shape}")
    df.info()
    print(".................................................")
    return df


def _forecast_future_cpi(
        df: pd.DataFrame,
        future_dates: list[str],
) -> pd.DataFrame:
    """Append forecasted CPI values for the supplied future dates.

    Args:
        df: Historical CPI dataframe with ``date`` and ``cpi`` columns.
        future_dates: Future monthly dates formatted as ``yyyy-mm-01`` strings.

    Returns:
        The dataframe returned by ``forecast_cpi``.

    Raises:
        TypeError: If a future date is not supplied as a string.
        ValueError: If future dates are invalid, duplicated, not monthly first
            dates, or not later than the last historical date.
    """
    print("Entering _forecast_future_cpi . . .")
    print(f"-- Forecast and cleaned source CPI Data: shape={df.shape};")
    df.info()

    future_date_strings: list[str] = future_dates.copy()
    print(f"-- Future dates: {future_date_strings}.")
    if not future_date_strings:
        raise ValueError("future dates must be provided as a non-empty list of strings.")
    if any(not isinstance(date_value, str) for date_value in future_date_strings):
        raise TypeError("Every future date must be provided as a string.")

    parsed_dates = pd.to_datetime(
        pd.Series(future_date_strings, dtype="string"),
        format="%Y-%m-%d",
        errors="coerce",
    )

    if parsed_dates.isna().any():
        raise ValueError("Future dates must use the format yyyy-mm-01.")

    if (parsed_dates.dt.day != 1).any():
        raise ValueError("Every future date must be the first day of a month.")

    if parsed_dates.duplicated().any():
        raise ValueError("Future dates must not contain duplicates.")

    if not parsed_dates.is_monotonic_increasing:
        raise ValueError("Future dates must be in ascending order.")

    last_historical_date = pd.Timestamp(df["date"].max())
    print(f"-- Future dates must follow this timestamp: {last_historical_date}.")
    if (parsed_dates <= last_historical_date).any():
        raise ValueError(
            "Every future date must be later than the last historical CPI date."
        )

    safe_input = df.copy(deep=True)
    safe_input.set_index(df["date"], inplace=True)
    print(f"-- Input to augmentation:")
    safe_input.info()
    augmented_df = forecast_cpi(safe_input, list(future_date_strings))

    required_columns = {"date", "cpi"}
    if not isinstance(augmented_df, pd.DataFrame):
        raise TypeError("forecast_cpi must return a pandas DataFrame.")
    if not required_columns.issubset(augmented_df.columns):
        raise ValueError("forecast_cpi must return date and cpi columns.")

    return augmented_df.loc[:, ["date", "cpi"]].copy()


def _write_augmented_cpi(
        augmented_df: pd.DataFrame,
        output_file: str | Path,
) -> None:
    """Write the augmented CPI dataframe to a CSV file.

    Args:
        augmented_df: Dataframe containing ``date`` and ``cpi`` columns.
        output_file: Destination CSV path.
    """
    print("Entering _write_augmented_cpi . . .")
    print(f"-- Writing augmented CPI dataframe to {output_file}, shape={augmented_df.shape}")

    output_path = Path(output_file)
    output_path.parent.mkdir(parents=True, exist_ok=True)

    output_df = augmented_df.loc[:, ["date", "cpi"]].copy()
    output_df["date"] = pd.to_datetime(output_df["date"], errors="raise").dt.strftime(
        "%Y-%m-01"
    )
    output_df.to_csv(output_path, index=False)
    print("Done.")


def _run_augmentation(
        input_file: str,
        future_dates: list[str],
        output_file: str,
        series: str,
        start_year: int,
        end_year: int
) -> None:
    """Run the CPI cleaning, forecasting, and output workflow.

    Args:
        input_file: Source CPI CSV path.
        future_dates: Future dates formatted as ``yyyy-mm-01`` strings.
        output_file: Destination CSV path.
        start_year: First year of range of interest.
        end_year: Last year of range of interest.

    Returns:
        The cleaned and forecast-augmented CPI dataframe.
    """
    print("Entering _run_augmentation. . .")
    print(f"==== Running CPI cleaning, forecasting, and output workflow.")
    df = _read_and_clean_cpi(input_file, series, start_year, end_year)
    df = fill_in_missing(df)
    print(f"==== Augmenting with future dates {future_dates}.")
    augmented_df = _forecast_future_cpi(df, future_dates)
    print(f"-- Augmented DataFrame:")
    augmented_df.info()
    print(f"Write out augmented CPI dataframe to {output_file}.")
    _write_augmented_cpi(augmented_df, output_file)


def main():
    """Create the cleaned and forecast-augmented Chained Urban CPI file.

    This public function is the program's command-line entry point. Its default
    arguments read ``data/su.data.1.AllItems.csv``, forecast June and July 2026,
    and write ``data/nasdaq_chained_projected.csv``.
    Returns: None
    """
    input_file: str = DEFAULT_INPUT_FILE
    future_dates: list[str] = list(DEFAULT_FUTURE_DATES)
    output_file: str = DEFAULT_OUTPUT_FILE
    series: str = DEFAULT_SERIES
    start_year: int = DEFAULT_START_YEAR
    end_year: int = DEFAULT_END_YEAR
    _run_augmentation(input_file, future_dates, output_file, series, start_year, end_year)


if __name__ == "__main__":
    main()
