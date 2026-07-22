"""Expand monthly CPI rates into equivalent daily CPI-rate records.

The input and output are Excel-compatible CSV text files.  Input records have
the columns ``date`` and ``cpi``; each date must be the first day of a month.
For every input record, this module writes one output record for every calendar
day in that month.  The daily rate is calculated by
:func:`convert_rates.monthly_to_daily`, preserving the compounded monthly rate.

The public :func:`convert_cpi_to_daily` function provides the programmatic API.
The public :func:`main` function is the command-line entry point.
"""

from __future__ import annotations

import calendar
import csv
import math
import os
from datetime import date, timedelta
from pathlib import Path

import pandas as pd

from convert_rates import monthly_to_daily

_FIELD_NAMES = ("date", "cpi")
MONTHLY_CPI_FILE = os.path.join("data", "chained_cpi_projected.csv")
DAILY_CPI_FILE = os.path.join("data", "daily_cpi.csv")


def convert_cpi_to_daily(
        monthly_cpi_file: str, daily_cpi_file: str
) -> tuple[int, int, pd.Timestamp, pd.Timestamp]:
    """Convert a monthly CPI-rate CSV file into a daily CPI-rate CSV file.

    Args:
        monthly_cpi_file: Name of an input CSV file whose columns are ``date``
            and ``cpi``. Dates must have the form ``yyyy-mm-01`` and rates must
            be positive, finite floating-point values.
        daily_cpi_file: Name of the output CSV file to create. It will contain
            the columns ``date`` and ``cpi``, with one record per calendar day.

    Returns:
        A ``(n_written, n_read, min_date, max_date)`` tuple. The first two
        values are the number of daily records written and monthly records
        read. The final two values are pandas timestamps for the earliest and
        latest monthly input dates.

    Raises:
        TypeError: If either file name is not a string.
        ValueError: If file names identify the same file or an input field,
            header, date, or CPI rate is invalid.
        OSError: If an input or output file operation fails.
        csv.Error: If malformed CSV data cannot be parsed.
    """
    _validate_file_names(monthly_cpi_file, daily_cpi_file)
    return _convert_file(Path(monthly_cpi_file), Path(daily_cpi_file))


def main() -> int:
    """Convert the CPI files named by the module's input/output constants.

    Returns:
        Zero after the conversion and summary display complete successfully.

    Raises:
        TypeError: If a configured file name has an invalid type.
        ValueError: If an input record or configured file name is invalid.
        OSError: If a required file operation fails.
        csv.Error: If malformed CSV data cannot be parsed.
    """
    print(f"Converting monthly CPI file {MONTHLY_CPI_FILE} to daily CPI file {DAILY_CPI_FILE}.")
    _orchestrate_conversion(MONTHLY_CPI_FILE, DAILY_CPI_FILE)
    print("Done.")
    return 0


def _orchestrate_conversion(monthly_cpi_file: str, daily_cpi_file: str) -> None:
    """Invoke conversion and display the requested file and result summary."""
    n_written, n_read, min_date, max_date = convert_cpi_to_daily(
        monthly_cpi_file, daily_cpi_file
    )
    output_max_date = max_date + pd.offsets.MonthEnd(0)
    print(f"Input file: {monthly_cpi_file}")
    print(f"Output file: {daily_cpi_file}")
    print(f"Input records: {n_read}")
    print(f"Written records: {n_written}")
    print(f"Input minimum date: {min_date.strftime('%Y-%m-%d')}")
    print(f"Output maximum date: {output_max_date.strftime('%Y-%m-%d')}")


def _convert_file(
        input_path: Path, output_path: Path
) -> tuple[int, int, pd.Timestamp, pd.Timestamp]:
    """Orchestrate reading, validating, expanding, and writing CPI records."""
    monthly_records = _read_monthly_records(input_path)
    daily_records = _expand_monthly_records(monthly_records)
    _write_daily_records(output_path, daily_records)
    min_date = pd.Timestamp(monthly_records[0][0])
    max_date = pd.Timestamp(monthly_records[-1][0])
    return len(daily_records), len(monthly_records), min_date, max_date


def _read_monthly_records(input_path: Path) -> list[tuple[date, float]]:
    """Read and validate all monthly CPI records from ``input_path``."""
    records: list[tuple[date, float]] = []
    with input_path.open("r", encoding="utf-8-sig", newline="") as input_file:
        reader = csv.DictReader(input_file, dialect="excel", strict=True)
        if reader.fieldnames != list(_FIELD_NAMES):
            raise ValueError("input CSV header must contain exactly: date,cpi")

        for line_number, row in enumerate(reader, start=2):
            records.append(
                (
                    _parse_month_date(row["date"], line_number),
                    _parse_cpi(row["cpi"], line_number),
                )
            )
    if not records:
        raise ValueError("input CSV must contain at least one data record")
    _validate_month_sequence(records)
    return records


def _validate_month_sequence(records: list[tuple[date, float]]) -> None:
    """Require unique, increasing monthly dates with no missing months."""
    for index in range(1, len(records)):
        previous_date = records[index - 1][0]
        current_date = records[index][0]
        expected_year = previous_date.year + (previous_date.month // 12)
        expected_month = (previous_date.month % 12) + 1
        expected_date = date(expected_year, expected_month, 1)
        if current_date != expected_date:
            raise ValueError(
                "input dates must be unique, monotonically increasing, "
                f"and consecutive; expected {expected_date.isoformat()} "
                f"but found {current_date.isoformat()}"
            )


def _parse_month_date(value: str | None, line_number: int) -> date:
    """Parse a strict ``yyyy-mm-01`` monthly date from one CSV field."""
    if value is None:
        raise ValueError(f"missing date on input line {line_number}")
    try:
        parsed = date.fromisoformat(value)
    except ValueError as error:
        raise ValueError(
            f"invalid date on input line {line_number}: {value!r}"
        ) from error
    if parsed.isoformat() != value or parsed.day != 1:
        raise ValueError(
            f"date on input line {line_number} must use yyyy-mm-01 format"
        )
    return parsed


def _parse_cpi(value: str | None, line_number: int) -> float:
    """Parse one positive, finite CPI rate from a CSV field."""
    try:
        rate = float(value) if value is not None else math.nan
    except ValueError as error:
        raise ValueError(
            f"invalid cpi on input line {line_number}: {value!r}"
        ) from error
    if not math.isfinite(rate) or rate <= 0:
        raise ValueError(
            f"cpi on input line {line_number} must be positive and finite"
        )
    return rate


def _expand_monthly_records(
        monthly_records: list[tuple[date, float]],
) -> list[tuple[str, float]]:
    """Expand validated monthly records into dated daily-rate records."""
    daily_records: list[tuple[str, float]] = []
    for month_date, monthly_rate in monthly_records:
        days_in_month = calendar.monthrange(month_date.year, month_date.month)[1]
        daily_rate = monthly_to_daily(monthly_rate, days_in_month)
        for day_offset in range(days_in_month):
            daily_date = month_date + timedelta(days=day_offset)
            daily_records.append((daily_date.isoformat(), daily_rate))
    return daily_records


def _write_daily_records(
        output_path: Path, daily_records: list[tuple[str, float]]
) -> None:
    """Write daily CPI records as an Excel-compatible CSV text file."""
    with output_path.open("w", encoding="utf-8", newline="") as output_file:
        writer = csv.writer(output_file, dialect="excel", lineterminator="\r\n")
        writer.writerow(_FIELD_NAMES)
        writer.writerows(daily_records)


def _validate_file_names(monthly_cpi_file: str, daily_cpi_file: str) -> None:
    """Validate input/output file-name types and ensure they differ."""
    if not isinstance(monthly_cpi_file, str):
        raise TypeError("monthly_cpi_file must be a string")
    if not isinstance(daily_cpi_file, str):
        raise TypeError("daily_cpi_file must be a string")
    if not monthly_cpi_file:
        raise ValueError("monthly_cpi_file must not be empty")
    if not daily_cpi_file:
        raise ValueError("daily_cpi_file must not be empty")
    if os.path.abspath(monthly_cpi_file) == os.path.abspath(daily_cpi_file):
        raise ValueError("input and output file names must be different")


if __name__ == "__main__":
    raise SystemExit(main())
