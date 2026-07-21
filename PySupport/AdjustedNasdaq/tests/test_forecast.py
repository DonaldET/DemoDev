"""Tests for the Chained CPI forecasting module."""
import os
import sys

project_root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
sys.path.append(project_root)

import pandas as pd
from pandas.testing import assert_frame_equal

from forecast import forecast_cpi

SMALL_SAMPLE = "Small Sample"  # initial test cases


def _test_forecast_cpi_appends_future_values(label: str, fed_cpi_index: pd.DataFrame, future_dates: list[str],
                                             expected_result: pd.DataFrame) -> None:
    print(f"{60 * '_'}")
    print(f" --Testing {label} Start.")
    print(f" --Input   :shape={fed_cpi_index.shape}; index:\n{fed_cpi_index.index}")
    actual = forecast_cpi(fed_cpi_index, future_dates)
    print(f" --Expected: shape={expected_result.shape}; index:\n{expected_result.index}")
    print(f" --Actual  : shape={actual.shape}; index:\n{actual.index}")
    assert_frame_equal(actual, expected_result, check_exact=False, rtol=1e-12, atol=1e-12)
    print(f" --Testing {label} Done.")
    print(f"{60 * '_'}")


def _run_tests() -> int:
    """Run the unit tests."""
    err_count = 0
    print(f"--------- Start Running Unit Tests ----------------\n")

    print(f"Setting up test data for {SMALL_SAMPLE}")
    dates: pd.Series[pd.Timestamp] = pd.to_datetime(pd.Series(
        ["2026-02-01", "2026-03-01", "2026-04-01", "2026-05-01"],
        name="date"))
    cpi_vals = pd.Series([126.960, 130.745, 135.988, 141.089], name="cpi")
    fed_cpi_index = pd.concat([dates, cpi_vals], axis=1)
    fed_cpi_index.set_index(fed_cpi_index['date'], inplace=True)

    print(f"-- Created Test Input DataFrame: {fed_cpi_index.shape}")
    fed_cpi_index.info()
    print(f"index:\n{fed_cpi_index.index}")

    future_dates = ["2026-06-01", "2026-07-01"]

    # Mean monthly change = (3.785 + 5.243 + 5.101) / 3 = 4.7096666667.
    expected = pd.DataFrame(
        {
            "date": pd.to_datetime(
                [
                    "2026-02-01",
                    "2026-03-01",
                    "2026-04-01",
                    "2026-05-01",
                    "2026-06-01",
                    "2026-07-01",
                ]
            ),
            "cpi": [
                126.960,
                130.745,
                135.988,
                141.089,
                145.79866666666666,
                150.50833333333333,
            ],
        }
    )
    expected.set_index(expected["date"], inplace=True)
    print("-- Created Expected Output DataFrame: {expected.shape}")
    expected.info()
    print(f"index:\n{expected.index}")

    try:
        _test_forecast_cpi_appends_future_values(SMALL_SAMPLE, fed_cpi_index, future_dates, expected)
        print("--> Success!")
    except AssertionError as error_details:
        print(error_details);
        err_count += 1

    print(f"--------- Stop Running Unit Tests  ----------------\n")

    return err_count


if __name__ == "__main__":
    print("==============================")
    print("==== Test Forcast Results ====")
    print("==============================")
    errors: int = _run_tests()
    if errors > 0:
        raise AssertionError(f"FAILED: {errors} errors were encountered.")
    print("--- Done.")
