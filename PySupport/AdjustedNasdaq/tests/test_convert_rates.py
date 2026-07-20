"""Unit tests for converting monthly CPI rates to equivalent daily rates.

Mathematical solution
---------------------
The monthly and daily rates must produce the same value at the end of an
``N``-day month. If ``R`` is the monthly CPI rate and ``r`` is the constant
daily CPI rate, then::

    1 + R = (1 + r) ** N, where 27 < N < 32

Taking the natural logarithm of both sides and solving for ``r`` gives::

    log(1 + R) = N * log(1 + r)
    log(1 + r) = log(1 + R) / N
    1 + r = exp(log(1 + R) / N)
    r = exp(log(1 + R) / N) - 1

Therefore, the daily CPI estimate returned by
:func:`convert_rates.monthly_to_daily` is::

    r = exp(log(1 + R) / N) - 1

For a positive monthly rate and a valid month length of 28 through 31 days,
the resulting daily rate is positive and less than the monthly rate.

Test coverage
-------------
The suite tests all six spreadsheet examples from the requirements, additional
valid boundary cases, and invalid arguments. Valid cases verify the calculated
rate, the required result range, and the end-of-month compounding identity.
Invalid cases verify that ``ValueError`` is raised.

Run this module directly from the command line::

    python test_convert_rates.py
"""

import math
from typing import Any

from convert_rates import monthly_to_daily


def _test_conversion(
    monthly_rate: Any,
    days_in_month: Any,
    expected: float | None = None,
    expect_error: bool = False,
) -> None:
    """Execute one conversion test case.

    Args:
        monthly_rate: Value passed as the converter's monthly-rate argument.
        days_in_month: Value passed as the converter's month-length argument.
        expected: Expected daily rate for a valid case. This must be supplied
            when ``expect_error`` is false.
        expect_error: Whether the converter is expected to raise ``ValueError``.

    Raises:
        AssertionError: If the result differs from ``expected``, violates the
            required range or compounding identity, fails to raise an expected
            ``ValueError``, or lacks an expected value for a valid case.
    """
    if expect_error:
        try:
            monthly_to_daily(monthly_rate, days_in_month)
        except ValueError:
            return
        raise AssertionError(
            f"ValueError not raised for ({monthly_rate!r}, {days_in_month!r})"
        )

    result = monthly_to_daily(monthly_rate, days_in_month)
    if expected is None:
        raise AssertionError("a valid test case requires an expected value")

    assert math.isclose(result, expected, rel_tol=1e-12, abs_tol=1e-15), (
        f"expected {expected!r}, received {result!r}"
    )
    assert 0 < result <= monthly_rate
    assert math.isclose(
        (1.0 + result) ** days_in_month,
        1.0 + monthly_rate,
        rel_tol=1e-12,
        abs_tol=1e-15,
    )


def _test_excel_examples() -> None:
    """Test the six successful conversion examples from the Excel table.

    The table's daily percentages are rounded to four decimal places. Each
    case is first tested against the full-precision mathematical result and is
    then checked against the displayed percentage after applying the same
    rounding.

    Returns:
        None.

    Raises:
        AssertionError: If a conversion fails its full-precision checks or
            does not round to the daily percentage shown in the Excel table.
    """
    excel_cases = (
        # monthly rate, days, displayed daily percentage
        (0.015105, 30, 0.0500),
        (0.010, 29, 0.0343),
        (0.008, 30, 0.0266),
        (0.004, 28, 0.0143),
        (0.005, 31, 0.0161),
        (0.001, 30, 0.0033),
    )

    for monthly_rate, days_in_month, displayed_daily_percent in excel_cases:
        expected = math.expm1(math.log1p(monthly_rate) / days_in_month)
        _test_conversion(monthly_rate, days_in_month, expected=expected)

        actual_daily_percent = monthly_to_daily(monthly_rate, days_in_month) * 100
        assert round(actual_daily_percent, 4) == displayed_daily_percent, (
            f"expected displayed daily rate {displayed_daily_percent:.4f}%, "
            f"received {actual_daily_percent:.4f}%"
        )


def run_tests() -> None:
    """Run the complete command-line test suite.

    The public function invokes the private Excel-example test and repeatedly
    invokes :func:`_test_conversion` for additional valid and invalid cases.
    It prints a summary after every case passes and allows an ``AssertionError``
    to propagate if a case fails.

    Returns:
        None.

    Raises:
        AssertionError: If any valid- or invalid-input test fails.
    """
    _test_excel_examples()

    additional_valid_cases = (
        (1e-12, 31),
        (2.5, 28),
    )
    for monthly_rate, days_in_month in additional_valid_cases:
        expected = math.expm1(math.log1p(monthly_rate) / days_in_month)
        _test_conversion(monthly_rate, days_in_month, expected=expected)

    invalid_cases = (
        (0.0, 30),
        (-0.01, 30),
        (math.inf, 30),
        (math.nan, 30),
        ("0.01", 30),
        (True, 30),
        (0.01, 27),
        (0.01, 32),
        (0.01, 30.0),
        (0.01, "30"),
        (0.01, True),
    )
    for monthly_rate, days_in_month in invalid_cases:
        _test_conversion(monthly_rate, days_in_month, expect_error=True)

    total_cases = 6 + len(additional_valid_cases) + len(invalid_cases)
    print(f"All {total_cases} tests passed.")


if __name__ == "__main__":
    run_tests()
