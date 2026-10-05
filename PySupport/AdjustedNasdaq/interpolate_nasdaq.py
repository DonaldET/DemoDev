"""Fill missing NASDAQ calendar dates and values by time-based interpolation.

The public function validates a two-column NASDAQ Composite DataFrame, expands
it to an inclusive daily calendar, and linearly interpolates only interior
missing values according to the distance between dates. Synthetic values are
rounded to four decimal places; valid input values retain their full precision.
"""

from __future__ import annotations

import numpy as np
import pandas as pd


_DATE_COLUMN = "observation_date"
_VALUE_COLUMN = "NASDAQCOM"
_EXPECTED_COLUMNS = [_DATE_COLUMN, _VALUE_COLUMN]
_DATETIME_NS_DTYPE = np.dtype("datetime64[ns]")
_FLOAT64_DTYPE = np.dtype("float64")
_MINIMUM_ROWS = 9


def fill_in_missing_days(
    nasdaq_composite_df: pd.DataFrame,
) -> tuple[pd.DataFrame, int, int]:
    """Insert missing calendar dates and interpolate missing NASDAQ values.

    The input is not modified. The returned DataFrame covers every calendar day
    from the minimum through maximum input date. Interpolation is linear in
    elapsed time and is limited to interior gaps bounded by valid values.

    Args:
        nasdaq_composite_df: Nonempty DataFrame containing exactly
            ``observation_date`` (``datetime64[ns]``) followed by ``NASDAQCOM``
            (``float64``), with at least nine rows.

    Returns:
        A tuple containing the augmented DataFrame, the number of inserted
        calendar-date rows, and the number of values interpolated. The returned
        DataFrame retains ``observation_date`` as a column and also uses it as
        its ``DatetimeIndex``.

    Raises:
        ValueError: If any input or output constraint is violated.
    """

    _validate_input(nasdaq_composite_df)

    # Select nanosecond resolution explicitly for the working date values.
    input_dates_ns = nasdaq_composite_df[_DATE_COLUMN].dt.as_unit("ns")
    working = nasdaq_composite_df.copy(deep=True)
    working[_DATE_COLUMN] = pd.Series(
        input_dates_ns.array,
        index=working.index,
        dtype="datetime64[ns]",
    )
    working.set_index(_DATE_COLUMN, drop=True, inplace=True)
    working.index = working.index.as_unit("ns")

    daily_index_ns = pd.date_range(
        start=working.index.min(),
        end=working.index.max(),
        freq="D",
    ).as_unit("ns")

    count_added_rows = len(daily_index_ns) - len(working)
    augmented_df = working.reindex(daily_index_ns)
    augmented_df.index = augmented_df.index.as_unit("ns")

    missing_before_interpolation = augmented_df[_VALUE_COLUMN].isna()
    count_interpolations = int(missing_before_interpolation.sum())

    interpolated_values = augmented_df[_VALUE_COLUMN].interpolate(
        method="time",
        limit_area="inside",
    )
    augmented_df.loc[missing_before_interpolation, _VALUE_COLUMN] = (
        interpolated_values.loc[missing_before_interpolation].round(4)
    )

    if augmented_df[_VALUE_COLUMN].isna().any():
        raise ValueError(
            "NASDAQCOM contains a missing value that cannot be interpolated."
        )

    observation_dates_ns = pd.Series(
        augmented_df.index.as_unit("ns"),
        index=augmented_df.index,
        dtype="datetime64[ns]",
        name=_DATE_COLUMN,
    )
    augmented_df.insert(0, _DATE_COLUMN, observation_dates_ns)
    augmented_df = augmented_df.loc[:, _EXPECTED_COLUMNS]
    augmented_df.set_index(_DATE_COLUMN, drop=False, inplace=True)
    augmented_df.index = augmented_df.index.as_unit("ns")
    augmented_df.index.name = _DATE_COLUMN

    _validate_output(augmented_df)
    return augmented_df, count_added_rows, count_interpolations


def _validate_input(nasdaq_composite_df: object) -> None:
    """Raise ``ValueError`` unless the input satisfies every requirement."""

    if not isinstance(nasdaq_composite_df, pd.DataFrame):
        raise ValueError("nasdaq_composite_df must be a pandas DataFrame.")
    if nasdaq_composite_df.empty:
        raise ValueError("nasdaq_composite_df must not be empty.")
    if list(nasdaq_composite_df.columns) != _EXPECTED_COLUMNS:
        raise ValueError(
            "The columns must be exactly observation_date and NASDAQCOM, "
            "in that order."
        )
    if len(nasdaq_composite_df) < _MINIMUM_ROWS:
        raise ValueError(
            f"nasdaq_composite_df must contain at least {_MINIMUM_ROWS} rows."
        )

    dates = nasdaq_composite_df[_DATE_COLUMN]
    values = nasdaq_composite_df[_VALUE_COLUMN]

    if dates.dtype != _DATETIME_NS_DTYPE:
        raise ValueError("observation_date must have dtype datetime64[ns].")
    if values.dtype != _FLOAT64_DTYPE:
        raise ValueError("NASDAQCOM must have dtype float64.")
    if dates.isna().any():
        raise ValueError("observation_date must not contain NaT.")

    dates_ns = dates.dt.as_unit("ns")
    normalized_dates_ns = dates_ns.dt.normalize().dt.as_unit("ns")
    if not dates_ns.equals(normalized_dates_ns):
        raise ValueError("Every observation_date must be midnight 00:00:00.")
    if not dates_ns.is_monotonic_increasing:
        raise ValueError("observation_date must be strictly increasing.")
    if not dates_ns.is_unique:
        raise ValueError("observation_date values must be unique.")

    missing_values = values.isna()
    if missing_values.all():
        raise ValueError("NASDAQCOM must not be entirely missing.")
    if bool(missing_values.iloc[0]) or bool(missing_values.iloc[-1]):
        raise ValueError("The first and last NASDAQCOM values must not be missing.")

    present_values = values.loc[~missing_values].to_numpy(
        dtype=np.float64,
        copy=False,
    )
    if not np.isfinite(present_values).all():
        raise ValueError("NASDAQCOM values must be finite or NaN.")
    if (present_values <= 0.0).any():
        raise ValueError("NASDAQCOM values must be greater than zero.")


def _validate_output(augmented_df: pd.DataFrame) -> None:
    """Raise ``ValueError`` if an internal result violates the output contract."""

    if list(augmented_df.columns) != _EXPECTED_COLUMNS:
        raise ValueError("The output columns do not match the required schema.")
    if augmented_df[_DATE_COLUMN].dtype != _DATETIME_NS_DTYPE:
        raise ValueError("The output observation_date dtype is not datetime64[ns].")
    if augmented_df[_VALUE_COLUMN].dtype != _FLOAT64_DTYPE:
        raise ValueError("The output NASDAQCOM dtype is not float64.")
    if augmented_df.index.dtype != _DATETIME_NS_DTYPE:
        raise ValueError("The output index dtype is not datetime64[ns].")
    if augmented_df.index.name != _DATE_COLUMN:
        raise ValueError("The output index name is not observation_date.")
    if not augmented_df.index.is_monotonic_increasing:
        raise ValueError("The output dates are not in ascending order.")


__all__ = ["fill_in_missing_days"]
