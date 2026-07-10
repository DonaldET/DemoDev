"""Forecast monthly Chained CPI index values using an automatically selected ARIMA model.

The module treats the CPI index as a non-stationary monthly level series and uses
first differencing (d=1). For normal-sized histories, it fits a small grid of
ARIMA(p, 1, q) models and selects the converged model with the lowest corrected
Akaike information criterion (AICc). A linear drift term is included because CPI
index levels generally trend over time.

Very short histories do not contain enough observations to estimate and compare
multiple ARIMA orders reliably. When fewer than eight observations are supplied,
the module uses an ARIMA(0, 1, 0) random-walk-with-drift fallback, with drift
estimated as the mean historical monthly change.
"""

from __future__ import annotations

import warnings
from collections.abc import Sequence

import numpy as np
import pandas as pd
from statsmodels.tsa.arima.model import ARIMA

_REQUIRED_COLUMNS = ("date", "cpi")
_MINIMUM_OBSERVATIONS = 3
_AUTO_ARIMA_MINIMUM_OBSERVATIONS = 8


def _validate_inputs(df: pd.DataFrame, future_dates: Sequence[str]) -> tuple[pd.DataFrame, pd.DatetimeIndex]:
    """Validate and normalize the historical dataframe and requested dates."""
    if not isinstance(df, pd.DataFrame):
        raise TypeError("df must be a pandas DataFrame.")

    missing = [column for column in _REQUIRED_COLUMNS if column not in df.columns]
    if missing:
        raise ValueError(f"df is missing required column(s): {', '.join(missing)}")

    if len(df) < _MINIMUM_OBSERVATIONS:
        raise ValueError(f"df must contain at least {_MINIMUM_OBSERVATIONS} observations.")

    if not future_dates:
        return df.loc[:, _REQUIRED_COLUMNS].copy(), pd.DatetimeIndex([])

    history = df.loc[:, _REQUIRED_COLUMNS].copy()
    history["date"] = pd.to_datetime(history["date"], errors="raise")
    history["cpi"] = pd.to_numeric(history["cpi"], errors="raise").astype(float)

    if history.isna().any().any():
        raise ValueError("df cannot contain missing date or cpi values.")
    if (history["cpi"] <= 0).any() or not np.isfinite(history["cpi"]).all():
        raise ValueError("Every cpi value must be a finite, positive, non-zero number.")
    if not history["date"].is_monotonic_increasing or history["date"].duplicated().any():
        raise ValueError("The date column must be strictly increasing with no duplicates.")
    if not history["date"].dt.is_month_start.all():
        raise ValueError("Every historical date must be the first day of a month.")

    expected_history = pd.date_range(history["date"].iloc[0], history["date"].iloc[-1], freq="MS")
    if not history["date"].reset_index(drop=True).equals(pd.Series(expected_history)):
        raise ValueError("Historical dates must form a continuous monthly sequence.")

    requested = pd.DatetimeIndex(pd.to_datetime(list(future_dates), errors="raise"))
    if requested.has_duplicates or not requested.is_monotonic_increasing:
        raise ValueError("future_dates must be unique and in ascending order.")
    if not requested.is_month_start.all():
        raise ValueError("Every future date must be the first day of a month.")
    if (requested <= history["date"].iloc[-1]).any():
        raise ValueError("Every future date must occur after the last historical date.")

    return history, requested


def _forecast_short_history(values: pd.Series, steps: int) -> np.ndarray:
    """Forecast an ARIMA(0,1,0) with drift for a very short series."""
    drift = float(values.diff().dropna().mean())
    horizons = np.arange(1, steps + 1, dtype=float)
    return float(values.iloc[-1]) + drift * horizons


def _aicc(aic: float, parameter_count: int, observation_count: int) -> float:
    """Return corrected AIC, or infinity when the correction is undefined."""
    denominator = observation_count - parameter_count - 1
    if denominator <= 0:
        return float("inf")
    return float(aic + (2 * parameter_count * (parameter_count + 1)) / denominator)


def _forecast_auto_arima(values: pd.Series, steps: int) -> np.ndarray:
    """Select an ARIMA(p,1,q) by AICc and return its forecast."""
    best_result = None
    best_score = float("inf")
    observation_count = len(values)

    # Restrict the search to parsimonious models suitable for a short monthly series.
    max_order = min(3, max(1, observation_count // 10))
    candidate_orders = [
        (p, 1, q)
        for p in range(max_order + 1)
        for q in range(max_order + 1)
        if p + q <= max_order + 1
    ]

    for order in candidate_orders:
        try:
            with warnings.catch_warnings():
                warnings.simplefilter("ignore")
                result = ARIMA(
                    values,
                    order=order,
                    trend="t",
                    enforce_stationarity=True,
                    enforce_invertibility=True,
                ).fit()

            score = _aicc(result.aic, len(result.params), observation_count)
            if np.isfinite(score) and score < best_score:
                best_score = score
                best_result = result
        except (ValueError, np.linalg.LinAlgError):
            continue

    if best_result is None:
        return _forecast_short_history(values, steps)

    return np.asarray(best_result.forecast(steps=steps), dtype=float)


def forecast_cpi(df: pd.DataFrame, future_dates: Sequence[str]) -> pd.DataFrame:
    """Append forecasts for requested future months to a copy of CPI history.

    The function uses AICc to select a parsimonious ARIMA(p,1,q) model with drift.
    Histories shorter than eight months use an ARIMA(0,1,0)-with-drift fallback.

    Args:
        df: DataFrame containing continuous monthly ``date`` and positive ``cpi``
            columns, ordered by ascending date.
        future_dates: Ascending future month-start dates in ``yyyy-mm-01`` format.

    Returns:
        A new DataFrame containing the original rows followed by one forecast row
        for each requested future date.
    """
    history, requested_dates = _validate_inputs(df, future_dates)
    if requested_dates.empty:
        return history.reset_index(drop=True)

    last_date = history["date"].iloc[-1]
    maximum_horizon = (requested_dates[-1].year - last_date.year) * 12 + (
            requested_dates[-1].month - last_date.month
    )

    values = pd.Series(
        history["cpi"].to_numpy(dtype=float),
        index=pd.DatetimeIndex(history["date"]),
        name="cpi",
    )

    if len(values) < _AUTO_ARIMA_MINIMUM_OBSERVATIONS:
        complete_forecast = _forecast_short_history(values, maximum_horizon)
    else:
        complete_forecast = _forecast_auto_arima(values, maximum_horizon)

    offsets = [
        (date.year - last_date.year) * 12 + (date.month - last_date.month) - 1
        for date in requested_dates
    ]
    selected_forecasts = complete_forecast[offsets]

    forecast_rows = pd.DataFrame(
        {"date": requested_dates, "cpi": selected_forecasts.astype(float)}
    )
    return pd.concat([history, forecast_rows], ignore_index=True)
