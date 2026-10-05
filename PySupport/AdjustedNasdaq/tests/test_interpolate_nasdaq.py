"""Unit tests for nanosecond-resolution NASDAQ date interpolation.

The tests validate schema handling, calendar-date generation, time-weighted
interpolation, four-decimal rounding, count semantics, and preservation of the
input DataFrame. Every constructed timestamp, date sequence, and expected
DatetimeIndex explicitly selects ``datetime64[ns]`` resolution.
"""

from __future__ import annotations

import numpy as np
import pandas as pd
import pytest
from pandas.testing import assert_frame_equal, assert_index_equal

from interpolate_nasdaq import fill_in_missing_days


NS_DTYPE = np.dtype("datetime64[ns]")
FLOAT64_DTYPE = np.dtype("float64")
EXPECTED_COLUMNS = ["observation_date", "NASDAQCOM"]


def _ns_index(values: list[str] | tuple[str, ...]) -> pd.DatetimeIndex:
    """Create a ``DatetimeIndex`` with explicitly selected ns resolution."""

    return pd.to_datetime(values).as_unit("ns")


def _ns_series(values: list[str] | tuple[str, ...]) -> pd.Series:
    """Create a datetime Series with explicitly selected ns resolution."""

    dates_ns = pd.to_datetime(values).as_unit("ns")
    return pd.Series(dates_ns, dtype="datetime64[ns]", name="observation_date")


def _ns_range(start: str, periods: int) -> pd.DatetimeIndex:
    """Create a daily ``DatetimeIndex`` with explicitly selected ns resolution."""

    return pd.date_range(start=start, periods=periods, freq="D").as_unit("ns")


def _ns_timestamp(value: str) -> pd.Timestamp:
    """Create a scalar timestamp with explicitly selected ns resolution."""

    return pd.to_datetime(value).as_unit("ns")


def _frame(
    *,
    dates: pd.DatetimeIndex | None = None,
    values: list[float] | np.ndarray | None = None,
) -> pd.DataFrame:
    """Build a valid test DataFrame with exact required datatypes."""

    dates_ns = (
        _ns_range("2023-01-07", 9) if dates is None else dates.as_unit("ns")
    )
    if values is None:
        values = [
            10386.980012345,
            10458.76,
            10305.24,
            10569.29,
            10635.65,
            10742.63,
            10931.67,
            11001.10,
            11079.16,
        ]
    return pd.DataFrame(
        {
            "observation_date": pd.Series(
                dates_ns,
                dtype="datetime64[ns]",
            ),
            "NASDAQCOM": pd.Series(values, dtype="float64"),
        }
    )


def test_non_dataframe_argument_is_invalid() -> None:
    """Reject an argument that is not a DataFrame."""

    with pytest.raises(ValueError):
        fill_in_missing_days(EXPECTED_COLUMNS)  # type: ignore[arg-type]


def test_empty_dataframe_is_invalid() -> None:
    """Reject an empty DataFrame even if it has the required schema."""

    empty = pd.DataFrame(
        {
            "observation_date": pd.Series([], dtype="datetime64[ns]"),
            "NASDAQCOM": pd.Series([], dtype="float64"),
        }
    )
    assert empty["observation_date"].dtype == NS_DTYPE
    with pytest.raises(ValueError):
        fill_in_missing_days(empty)


def test_fewer_than_nine_rows_is_invalid() -> None:
    """Reject an otherwise valid DataFrame containing only eight rows."""

    short = _frame(dates=_ns_range("2023-01-07", 8), values=np.arange(1.0, 9.0))
    with pytest.raises(ValueError):
        fill_in_missing_days(short)


def test_nine_rows_is_valid() -> None:
    """Accept the minimum permitted row count."""

    source = _frame()
    result, added, interpolated = fill_in_missing_days(source)
    assert len(result) == 9
    assert added == 0
    assert interpolated == 0


def test_one_interior_missing_calendar_day() -> None:
    """Insert and interpolate one omitted interior calendar date."""

    full_dates = _ns_range("2023-01-07", 10)
    missing_date = _ns_timestamp("2023-01-12")
    input_dates = full_dates[full_dates != missing_date].as_unit("ns")
    source = _frame(dates=input_dates, values=np.arange(100.0, 109.0))

    result, added, interpolated = fill_in_missing_days(source)

    assert added == 1
    assert interpolated == 1
    assert result.loc[missing_date, "NASDAQCOM"] == pytest.approx(104.5)


def test_two_nonadjacent_interior_missing_days() -> None:
    """Insert two omitted, nonadjacent interior calendar dates."""

    full_dates = _ns_range("2023-01-07", 12)
    first_missing = _ns_timestamp("2023-01-10")
    second_missing = _ns_timestamp("2023-01-15")
    input_dates = full_dates[
        ~full_dates.isin(_ns_index(("2023-01-10", "2023-01-15")))
    ].as_unit("ns")
    source = _frame(dates=input_dates, values=np.arange(200.0, 210.0))

    result, added, interpolated = fill_in_missing_days(source)

    assert added == 2
    assert interpolated == 2
    assert result.loc[first_missing, "NASDAQCOM"] == pytest.approx(202.5)
    assert result.loc[second_missing, "NASDAQCOM"] == pytest.approx(206.5)


def test_multiple_consecutive_interior_missing_days() -> None:
    """Interpolate consecutive omitted dates using elapsed-day distance."""

    full_dates = _ns_range("2023-01-07", 12)
    omitted_ns = _ns_index(("2023-01-15", "2023-01-16"))
    input_dates = full_dates[~full_dates.isin(omitted_ns)].as_unit("ns")
    values = np.array(
        [10386.98, 10458.76, 10305.24, 10569.29, 10635.65,
         10742.63, 10931.67, 11001.10, 11095.11, 11100.0],
        dtype=np.float64,
    )
    source = _frame(dates=input_dates, values=values)

    result, added, interpolated = fill_in_missing_days(source)

    assert added == 2
    assert interpolated == 2
    assert result.loc[_ns_timestamp("2023-01-15"), "NASDAQCOM"] == pytest.approx(
        11032.4367
    )
    assert result.loc[_ns_timestamp("2023-01-16"), "NASDAQCOM"] == pytest.approx(
        11063.7733
    )


def test_input_with_no_missing_days_is_unchanged_in_output_values() -> None:
    """Return the same valid values when the calendar is already complete."""

    source = _frame()
    result, added, interpolated = fill_in_missing_days(source)

    assert added == 0
    assert interpolated == 0
    assert np.array_equal(
        result["NASDAQCOM"].to_numpy(),
        source["NASDAQCOM"].to_numpy(),
    )


def test_input_dataframe_remains_unchanged() -> None:
    """Preserve the source DataFrame's index, values, schema, and dtypes."""

    source = _frame()
    source.loc[4, "NASDAQCOM"] = np.nan
    source.index = pd.Index(
        [f"row-{number}" for number in range(len(source))],
        name="source_index",
        dtype="object",
    )
    original = source.copy(deep=True)

    fill_in_missing_days(source)

    assert_frame_equal(source, original, check_exact=True)


def test_output_schema_dtypes_order_and_index() -> None:
    """Return the exact required columns, dtypes, order, and DatetimeIndex."""

    source = _frame()
    result, _, _ = fill_in_missing_days(source)
    expected_index_ns = _ns_range("2023-01-07", 9)
    expected_index_ns.name = "observation_date"

    assert list(result.columns) == EXPECTED_COLUMNS
    assert result["observation_date"].dtype == NS_DTYPE
    assert result["NASDAQCOM"].dtype == FLOAT64_DTYPE
    assert isinstance(result.index, pd.DatetimeIndex)
    assert result.index.dtype == NS_DTYPE
    assert result.index.name == "observation_date"
    assert result.index.is_monotonic_increasing
    assert_index_equal(result.index, expected_index_ns, exact=True)
    assert_index_equal(
        pd.DatetimeIndex(result["observation_date"]).as_unit("ns"),
        expected_index_ns,
        exact=True,
    )


@pytest.mark.parametrize(
    "columns",
    [
        ["NASDAQCOM", "observation_date"],
        ["date", "NASDAQCOM"],
        ["observation_date", "value"],
    ],
)
def test_incorrect_or_reordered_columns_are_invalid(columns: list[str]) -> None:
    """Reject incorrect names and the correct names in the wrong order."""

    source = _frame()
    source.columns = columns
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


def test_extra_column_is_invalid() -> None:
    """Reject a DataFrame containing an additional column."""

    source = _frame()
    source["extra"] = np.arange(len(source), dtype=np.int64)
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


def test_invalid_date_strings_are_invalid() -> None:
    """Reject invalid date text rather than coercing it inside the function."""

    source = _frame()
    source["observation_date"] = pd.Series(
        ["not-a-date"] * len(source),
        dtype="object",
    )
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


def test_object_date_dtype_is_invalid() -> None:
    """Reject date values stored with object dtype."""

    source = _frame()
    source["observation_date"] = source["observation_date"].astype("object")
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


def test_non_nanosecond_datetime_dtype_is_invalid() -> None:
    """Reject a datetime column whose unit is explicitly microseconds."""

    source = _frame()
    dates_us = source["observation_date"].dt.as_unit("us")
    source["observation_date"] = dates_us
    assert source["observation_date"].dtype == np.dtype("datetime64[us]")
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


def test_non_float64_value_dtype_is_invalid() -> None:
    """Reject NASDAQCOM values stored with an integer dtype."""

    source = _frame(values=np.arange(1.0, 10.0))
    source["NASDAQCOM"] = source["NASDAQCOM"].astype("int64")
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


def test_nat_is_invalid() -> None:
    """Reject NaT while retaining exact datetime64[ns] test dtype."""

    source = _frame()
    dates_ns = _ns_series(
        [
            "2023-01-07",
            "2023-01-08",
            "2023-01-09",
            "2023-01-10",
            "2023-01-11",
            "2023-01-12",
            "2023-01-13",
            "2023-01-14",
            "2023-01-15",
        ]
    )
    dates_ns.iloc[4] = pd.NaT
    source["observation_date"] = dates_ns
    assert source["observation_date"].dtype == NS_DTYPE
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


def test_non_midnight_timestamp_is_invalid() -> None:
    """Reject an exact-ns timestamp with a non-midnight time component."""

    dates_ns = _ns_index(
        (
            "2023-01-07 00:00:00",
            "2023-01-08 00:00:00",
            "2023-01-09 00:00:00",
            "2023-01-10 00:00:00",
            "2023-01-11 12:30:00",
            "2023-01-12 00:00:00",
            "2023-01-13 00:00:00",
            "2023-01-14 00:00:00",
            "2023-01-15 00:00:00",
        )
    )
    source = _frame(dates=dates_ns)
    assert source["observation_date"].dtype == NS_DTYPE
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


def test_unsorted_dates_are_invalid() -> None:
    """Reject unique datetime64[ns] dates that are not ascending."""

    dates_ns = _ns_range("2023-01-07", 9)
    order = np.array([0, 1, 2, 4, 3, 5, 6, 7, 8])
    source = _frame(dates=dates_ns[order].as_unit("ns"))
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


def test_duplicate_dates_are_invalid() -> None:
    """Reject duplicate datetime64[ns] observation dates."""

    dates_ns = _ns_index(
        (
            "2023-01-07",
            "2023-01-08",
            "2023-01-09",
            "2023-01-10",
            "2023-01-11",
            "2023-01-11",
            "2023-01-13",
            "2023-01-14",
            "2023-01-15",
        )
    )
    source = _frame(dates=dates_ns)
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


@pytest.mark.parametrize("invalid_value", [0.0, -1.0, np.inf, -np.inf])
def test_invalid_nasdaq_values(invalid_value: float) -> None:
    """Reject zero, negative, and infinite NASDAQCOM values."""

    source = _frame()
    source.loc[4, "NASDAQCOM"] = invalid_value
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


@pytest.mark.parametrize("position", [0, -1])
def test_leading_or_trailing_missing_value_is_invalid(position: int) -> None:
    """Reject a missing endpoint because extrapolation is forbidden."""

    source = _frame()
    source.iloc[position, source.columns.get_loc("NASDAQCOM")] = np.nan
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


def test_entirely_missing_values_are_invalid() -> None:
    """Reject an entirely missing NASDAQCOM column."""

    source = _frame(values=np.full(9, np.nan, dtype=np.float64))
    with pytest.raises(ValueError):
        fill_in_missing_days(source)


def test_count_semantics_for_inserted_rows_and_existing_nan() -> None:
    """Count inserted rows plus a pre-existing NaN as interpolations."""

    dates_ns = _ns_index(
        (
            "2023-01-07",
            "2023-01-08",
            "2023-01-09",
            "2023-01-10",
            "2023-01-11",
            "2023-01-12",
            "2023-01-13",
            "2023-01-14",
            "2023-01-17",
        )
    )
    values = [
        10386.98,
        10458.76,
        np.nan,
        10569.29,
        10635.65,
        10742.63,
        10931.67,
        11001.10,
        11095.11,
    ]
    source = _frame(dates=dates_ns, values=values)

    result, added, interpolated = fill_in_missing_days(source)

    assert added == 2
    assert interpolated == 3
    assert result.loc[_ns_timestamp("2023-01-09"), "NASDAQCOM"] == pytest.approx(
        10514.025
    )
    assert result.loc[_ns_timestamp("2023-01-15"), "NASDAQCOM"] == pytest.approx(
        11032.4367
    )
    assert result.loc[_ns_timestamp("2023-01-16"), "NASDAQCOM"] == pytest.approx(
        11063.7733
    )


def test_numerical_rounding_does_not_require_display_zeroes() -> None:
    """Round numerically to four decimals without testing string formatting."""

    source = _frame()
    source.loc[4, "NASDAQCOM"] = np.nan

    result, _, interpolated = fill_in_missing_days(source)
    value = result.loc[_ns_timestamp("2023-01-11"), "NASDAQCOM"]

    assert interpolated == 1
    assert value == pytest.approx(round((10569.29 + 10742.63) / 2.0, 4))
    assert isinstance(value, np.floating)


def test_valid_input_values_keep_full_precision() -> None:
    """Round interpolated positions only and preserve valid input precision."""

    source = _frame()
    original_value = source.loc[0, "NASDAQCOM"]
    source.loc[4, "NASDAQCOM"] = np.nan

    result, _, _ = fill_in_missing_days(source)

    assert result.loc[_ns_timestamp("2023-01-07"), "NASDAQCOM"] == original_value


def test_exact_augmented_index_and_ascending_row_order() -> None:
    """Return the complete expected nanosecond DatetimeIndex in date order."""

    dates_ns = _ns_index(
        (
            "2023-01-07",
            "2023-01-08",
            "2023-01-09",
            "2023-01-10",
            "2023-01-11",
            "2023-01-12",
            "2023-01-13",
            "2023-01-14",
            "2023-01-17",
        )
    )
    source = _frame(dates=dates_ns)
    expected_index_ns = _ns_range("2023-01-07", 11)
    expected_index_ns.name = "observation_date"

    result, _, _ = fill_in_missing_days(source)

    assert_index_equal(result.index, expected_index_ns, exact=True)
    assert result.index.dtype == NS_DTYPE
    assert result["observation_date"].dtype == NS_DTYPE
    assert result.index.is_monotonic_increasing
