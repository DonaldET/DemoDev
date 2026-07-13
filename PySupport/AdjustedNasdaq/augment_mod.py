"""Clean Chained Urban CPI data and append forecasted monthly CPI values.

This module reads the BLS Chained Urban CPI input file, selects the desired
series and date range, restructures the data into ``date`` and ``cpi`` columns,
calls ``forecast.forecast_cpi`` to generate future values, and writes the
augmented data to a CSV file.
"""

from __future__ import annotations

from collections.abc import Sequence
from pathlib import Path

import pandas as pd

from forecast import forecast_cpi

_SERIES_ID = "SUUR0000SA0"
_MIN_YEAR = 2023
_MAX_YEAR = 2026
_REQUIRED_INPUT_COLUMNS = {"series", "year", "month", "value", "footnote"}


def _read_and_clean_cpi(input_file: str | Path) -> pd.DataFrame:
    """Read, filter, and restructure the source CPI CSV file.

    Args:
        input_file: Path to the source CPI CSV file.

    Returns:
        A dataframe containing only ``date`` and ``cpi`` columns, sorted in
        ascending monthly order.

    Raises:
        FileNotFoundError: If the input file does not exist.
        ValueError: If required columns are missing or selected data is invalid.
    """
    input_path = Path(input_file)

    if not input_path.is_file():
        raise FileNotFoundError(f"CPI input file not found: {input_path}")

    input_df = pd.read_csv(
        input_path,
        dtype={
            "series": "string",
            "year": "Int64",
            "month": "Int64",
            "value": "float64",
            "footnote": "string",
        },
        skipinitialspace=True,
    )

    # Normalize header whitespace because some source files may contain padded
    # column names.
    input_df.columns = input_df.columns.str.strip()

    missing_columns = _REQUIRED_INPUT_COLUMNS.difference(input_df.columns)
    if missing_columns:
        missing = ", ".join(sorted(missing_columns))
        raise ValueError(f"Input file is missing required columns: {missing}")

    input_df["series"] = input_df["series"].str.strip()

    selected = input_df.loc[
        (input_df["series"] == _SERIES_ID)
        & input_df["year"].between(_MIN_YEAR, _MAX_YEAR),
        ["year", "month", "value"],
    ].copy()

    if selected.empty:
        raise ValueError(
            f"No CPI rows found for series {_SERIES_ID} "
            f"between {_MIN_YEAR} and {_MAX_YEAR}."
        )

    if selected[["year", "month", "value"]].isna().any().any():
        raise ValueError("Selected CPI data contains missing year, month, or value fields.")

    if not selected["month"].between(1, 12).all():
        raise ValueError("Selected CPI data contains a month outside the range 1 through 12.")

    if not (selected["value"] > 0).all():
        raise ValueError("Selected CPI data contains a non-positive CPI value.")

    selected["date"] = pd.to_datetime(
        {
            "year": selected["year"].astype(int),
            "month": selected["month"].astype(int),
            "day": 1,
        },
        errors="raise",
    )

    df = (
        selected.rename(columns={"value": "cpi"})
        .loc[:, ["date", "cpi"]]
        .sort_values("date")
        .reset_index(drop=True)
    )

    if df["date"].duplicated().any():
        duplicate_dates = (
            df.loc[df["date"].duplicated(keep=False), "date"]
            .dt.strftime("%Y-%m-%d")
            .unique()
        )
        raise ValueError(
            "Selected CPI data contains duplicate dates: "
            + ", ".join(duplicate_dates)
        )

    return df


def _forecast_future_cpi(
        df: pd.DataFrame,
        future_dates: list[str],
) -> pd.DataFrame:
    """Append forecasted CPI values for the requested future dates.

    Args:
        df: Historical CPI dataframe with ``date`` and ``cpi`` columns.
        future_dates: Monthly dates for which CPI values should be forecast.

    Returns:
        The dataframe returned by ``forecast_cpi``.

    Raises:
        ValueError: If no future dates are supplied, dates are invalid, or a
            requested date does not occur after the final historical date.
    """
    if not future_dates:
        raise ValueError("At least one future forecast date is required.")

    normalized_dates: pd.Series = pd.to_datetime(pd.Series(future_dates), errors="raise")

    if normalized_dates.duplicated().any():
        raise ValueError("Forecast dates must not contain duplicates.")

    if not normalized_dates.dt.is_month_start.all():
        raise ValueError("Every forecast date must be the first day of a month.")

    normalized_dates = normalized_dates.sort_values()
    print(f"  --Future dates ordered:\n{normalized_dates}")

    last_historical_date = pd.Timestamp(df["date"].max())
    if (normalized_dates <= last_historical_date).any():
        raise ValueError(
            "Every forecast date must occur after the final historical CPI date "
            f"({last_historical_date:%Y-%m-%d})."
        )

    date_strings = normalized_dates.to_timestamp()
    print(f"  --Future dates as strings:\n{date_strings}")
    exit(0)
    augmented_df = forecast_cpi(df.copy(), date_strings)

    expected_columns = ["date", "cpi"]
    if list(augmented_df.columns) != expected_columns:
        raise ValueError(
            "forecast_cpi must return a dataframe with columns "
            f"{expected_columns}; received {list(augmented_df.columns)}."
        )

    augmented_df = augmented_df.copy()
    augmented_df["date"] = pd.to_datetime(augmented_df["date"], errors="raise")
    augmented_df["cpi"] = pd.to_numeric(augmented_df["cpi"], errors="raise")

    if augmented_df[expected_columns].isna().any().any():
        raise ValueError("The augmented CPI dataframe contains missing values.")

    return augmented_df.sort_values("date").reset_index(drop=True)


def _write_augmented_cpi(
        augmented_df: pd.DataFrame,
        output_file: str | Path,
) -> None:
    """Write the augmented CPI dataframe to a CSV file.

    Args:
        augmented_df: Dataframe containing ``date`` and ``cpi`` columns.
        output_file: Destination CSV path.
    """
    output_path = Path(output_file)
    output_path.parent.mkdir(parents=True, exist_ok=True)

    output_df = augmented_df.loc[:, ["date", "cpi"]].copy()
    output_df["date"] = pd.to_datetime(output_df["date"]).dt.strftime("%Y-%m-%d")
    output_df.to_csv(output_path, index=False)


def _run_augmentation(
        input_file: str | Path,
        future_dates: Sequence[str | pd.Timestamp],
        output_file: str | Path,
) -> pd.DataFrame:
    """Execute the complete CPI cleaning, forecasting, and output workflow."""
    df = _read_and_clean_cpi(input_file)
    augmented_df = _forecast_future_cpi(df, future_dates)
    _write_augmented_cpi(augmented_df, output_file)
    return augmented_df


def augment_cpi(
        input_file: str | Path,
        future_dates: list[str],
        output_file: str) -> pd.DataFrame:
    """Clean historical CPI data, forecast future values, and write the result.

    Args:
        input_file: Path to the source CPI CSV file.
        future_dates: Monthly dates to forecast, each occurring after the final
            historical observation.
        output_file: Destination CSV path. Defaults to
            ``data/nasdaq_chained_projected.csv``.

    Returns:
        A dataframe containing historical and forecasted CPI values.
    """
    return _run_augmentation(input_file, future_dates, output_file)


def main() -> None:
    """Run the CPI augmentation workflow with the project default settings."""
    print("Running CPI augmentation workflow.")
    input_file: str = Path("data") / "su.data.1.AllItems.csv"
    future_dates: list[str] = ["2026-06-01", "2026-07-01"]
    output_file: str = Path("data") / "nasdaq_chained_projected.csv"

    print(f"Input file  : {input_file}")
    print(f"Future dates: {future_dates}")
    print(f"Output file : {output_file}")

    augmented_df: pd.DataFrame = augment_cpi(input_file, future_dates, output_file)
    print(f"Wrote {len(augmented_df)} CPI rows to {output_file}")


if __name__ == "__main__":
    main()
