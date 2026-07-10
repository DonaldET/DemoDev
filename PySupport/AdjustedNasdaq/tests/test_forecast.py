"""Tests for the Chained CPI forecasting module."""
import pandas as pd
from pandas.testing import assert_frame_equal

from forecast import forecast_cpi


def _test_forecast_cpi_appends_future_values(label: str, fed_cpi_index: pd.DataFrame, future_dates: list[str], expected_result: pd.DataFrame) -> None:
    print(f"{60 * '_'}")
    print(f" --Testing {label}")
    print(f" --Input   :\n{fed_cpi_index}")
    actual = forecast_cpi(fed_cpi_index, future_dates)
    print(f" --Expected:\n{expected_result}")
    print(f" --Actual  :\n{actual}")
    assert_frame_equal(actual, expected_result, check_exact=False, rtol=1e-12, atol=1e-12)


def _run_tests():
    """Run the unit tests."""
    fed_cpi_index = pd.DataFrame(
        {
            "date": pd.to_datetime(
                ["2026-02-01", "2026-03-01", "2026-04-01", "2026-05-01"]
            ),
            "cpi": [126.960, 130.745, 135.988, 141.089],
        }
    )
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
    _test_forecast_cpi_appends_future_values("Small Sample", fed_cpi_index, future_dates, expected)


if __name__ == "__main__":
    print("==============================")
    print("==== Test Forcast Results ====")
    print("==============================")
    _run_tests()
    print("--- Done")
