"""Utilities for filling missing monthly CPI observations.

This module provides :func:`fill_in_missing`, which expands a monthly CPI
DataFrame to include every first-of-month date between its earliest and latest
observations. Missing CPI values are filled using linear interpolation by
monthly position.
"""

from __future__ import annotations

import pandas as pd

_REQUIRED_COLUMNS = {"date", "cpi"}


def fill_in_missing(df: pd.DataFrame) -> pd.DataFrame:
    """Return a copy of *df* with missing monthly CPI rows interpolated.

    The input DataFrame must contain ``date`` and ``cpi`` columns. Dates must
    represent first-of-month observations in ascending order. The returned
    DataFrame contains a complete monthly sequence from the first input date
    through the last input date.

    Missing CPI values are linearly interpolated according to monthly sequence,
    rather than according to the differing number of days in each month. The
    returned DataFrame retains the updated ``date`` column and also uses those
    same dates as its index.

    Args:
        df: DataFrame containing ``date`` and ``cpi`` columns.

    Returns:
        A new DataFrame with missing monthly rows inserted and CPI values
        interpolated. The ``date`` column is retained and is also the index.

    Raises:
        TypeError: If ``df`` is not a pandas DataFrame.
        ValueError: If required columns are missing, the DataFrame is empty,
            dates or CPI values are invalid, dates are duplicated or unsorted,
            or dates are not first-of-month values.
    """
    if not isinstance(df, pd.DataFrame):
        raise TypeError("df must be a pandas DataFrame")

    missing_columns = _REQUIRED_COLUMNS.difference(df.columns)
    if missing_columns:
        missing = ", ".join(sorted(missing_columns))
        raise ValueError(f"df is missing required column(s): {missing}")

    if df.empty:
        raise ValueError("df must contain at least one row")

    working: pd.Dataframe = df.loc[:, ["date", "cpi"]].copy()
    working.set_index(working["date"], inplace=True)
    try:
        working["date"] = pd.to_datetime(working["date"], errors="raise")
    except (TypeError, ValueError) as exc:
        raise ValueError("date column contains invalid date values") from exc

    if working["date"].isna().any():
        raise ValueError("date column must not contain missing values")
    if not working["date"].is_monotonic_increasing:
        raise ValueError("date column must be in ascending order")
    if working["date"].duplicated().any():
        raise ValueError("date column must not contain duplicate dates")
    if not (working["date"].dt.day == 1).all():
        raise ValueError("every date must be the first day of its month")

    working["cpi"] = pd.to_numeric(working["cpi"], errors="coerce")
    if working["cpi"].isna().any():
        raise ValueError("cpi column must contain numeric, non-missing values")
    if (working["cpi"] <= 0).any():
        raise ValueError("cpi values must be positive and non-zero")

    complete_dates = pd.date_range(
        start=working["date"].iloc[0],
        end=working["date"].iloc[-1],
        freq="MS",
    )

    augmented = working.set_index("date").reindex(complete_dates)
    augmented.index.name = "date"
    augmented["cpi"] = augmented["cpi"].interpolate(method="linear")
    augmented["date"] = augmented.index
    augmented = augmented.loc[:, ["date", "cpi"]]

    # Keep the date column while also using the same date values as the index.
    augmented.set_index(augmented["date"], inplace=True)
    augmented.index.name = "date"

    return augmented
