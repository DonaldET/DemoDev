"""Utilities for converting monthly CPI rates to equivalent daily rates.

This module exposes :func:`monthly_to_daily`, which converts one positive
monthly CPI inflation rate into a constant daily rate. Both input and output
rates use decimal notation; for example, 1 percent is represented by ``0.01``.

The conversion preserves compounding over the supplied month length::

    1 + monthly_rate == (1 + daily_rate) ** day_in_month

The implementation uses :func:`math.log1p` and :func:`math.expm1` to reduce
floating-point precision loss, particularly for rates close to zero.
"""

import math
from numbers import Real


def monthly_to_daily(monthly_rate: float, day_in_month: int) -> float:
    """Convert a monthly CPI rate to an equivalent compounded daily rate.

    The calculation is ``expm1(log1p(monthly_rate) / day_in_month)``. Thus,
    subject to normal floating-point precision, applying the returned daily
    rate once per day produces the same end-of-month value as applying the
    monthly rate once.

    Args:
        monthly_rate: A positive, finite monthly CPI rate in decimal form.
            For example, pass ``0.01`` for a monthly rate of 1 percent.
        day_in_month: The integer number of days in the month. Valid values
            are 28, 29, 30, and 31.

    Returns:
        A positive floating-point daily rate in decimal form. The result is
        no greater than ``monthly_rate``.

    Raises:
        ValueError: If ``monthly_rate`` is not a positive finite real number,
            if ``day_in_month`` is not an integer from 28 through 31, or if
            the calculated result fails its range validation.

    Example:
        >>> daily_rate = monthly_to_daily(0.01, 30)
        >>> round(daily_rate, 8)
        0.00033173
    """
    if (
        isinstance(monthly_rate, bool)
        or not isinstance(monthly_rate, Real)
        or not math.isfinite(monthly_rate)
        or monthly_rate <= 0
    ):
        raise ValueError("monthly_rate must be a positive, finite number")

    if (
        isinstance(day_in_month, bool)
        or not isinstance(day_in_month, int)
        or not 28 <= day_in_month <= 31
    ):
        raise ValueError("day_in_month must be an integer between 28 and 31")

    daily_rate = math.expm1(math.log1p(monthly_rate) / day_in_month)

    if (
        not math.isfinite(daily_rate)
        or daily_rate <= 0
        or daily_rate > monthly_rate
    ):
        raise ValueError("calculated daily rate is outside the valid range")

    return daily_rate
