"""Independent tests for the CPI missing-month interpolation function."""

from __future__ import annotations

import pandas as pd
from pandas.testing import assert_frame_equal

from interpolate import fill_in_missing


def test_fill_in_missing_two_separated_months() -> None:
    """Verify two nonconsecutive missing months are inserted correctly."""
    input_df = pd.DataFrame(
        {
            "date": pd.to_datetime(
                [
                    "2026-01-01",
                    "2026-02-01",
                    "2026-04-01",
                    "2026-05-01",
                    "2026-07-01",
                    "2026-08-01",
                ]
            ),
            "cpi": [100.0, 104.0, 112.0, 116.0, 124.0, 128.0],
        }
    )

    expected_dates = pd.to_datetime(
        [
            "2026-01-01",
            "2026-02-01",
            "2026-03-01",
            "2026-04-01",
            "2026-05-01",
            "2026-06-01",
            "2026-07-01",
            "2026-08-01",
        ]
    )

    expected_df = pd.DataFrame(
        {
            "date": expected_dates,
            "cpi": [100.0, 104.0, 108.0, 112.0, 116.0, 120.0, 124.0, 128.0],
        },
        index=expected_dates,
    )
    expected_df.index.name = "date"

    actual_df = fill_in_missing(input_df)

    assert_frame_equal(actual_df, expected_df, check_dtype=True)
    assert actual_df.index.equals(pd.Index(actual_df["date"], name="date"))
    assert input_df.shape == (6, 2), "The input DataFrame must not be modified."


def main() -> None:
    """Run the module's interpolation test."""
    test_fill_in_missing_two_separated_months()
    print("All interpolate tests passed.")


if __name__ == "__main__":
    main()
