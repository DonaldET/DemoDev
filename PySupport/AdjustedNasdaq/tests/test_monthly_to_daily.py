"""PYTEST tests for the monthly CPI CSV-to-daily CSV conversion module."""
import os
import sys

project_root = os.path.abspath(os.path.join(os.path.dirname(__file__), '..'))
sys.path.append(project_root)

import csv
import math
from pathlib import Path

import pandas as pd
import pytest

import monthly_to_daily as converter


def _write_monthly_file(path: Path, rows: list[tuple[str, str]]) -> None:
    """Write a small monthly-rate fixture using the Excel CSV dialect."""
    with path.open("w", encoding="utf-8", newline="") as output_file:
        writer = csv.writer(output_file, dialect="excel")
        writer.writerow(("date", "cpi"))
        writer.writerows(rows)


def test_convert_expands_months_and_returns_metadata(tmp_path: Path) -> None:
    """Conversion expands normal/leap months and returns all four values."""
    source = tmp_path / "monthly.csv"
    output = tmp_path / "daily.csv"
    _write_monthly_file(
        source,
        [("2023-12-01", "0.015105"), ("2024-01-01", "0.01"),
         ("2024-02-01", "0.02")],
    )

    result = converter.convert_cpi_to_daily(str(source), str(output))

    assert result == (
        91,
        3,
        pd.Timestamp("2023-12-01"),
        pd.Timestamp("2024-02-01"),
    )
    with output.open(encoding="utf-8", newline="") as input_file:
        rows = list(csv.DictReader(input_file, dialect="excel"))
    assert rows[0]["date"] == "2023-12-01"
    assert rows[-1]["date"] == "2024-02-29"
    december_rate = float(rows[0]["cpi"])
    assert math.isclose((1.0 + december_rate) ** 31, 1.015105, rel_tol=1e-14)


@pytest.mark.parametrize(
    "rows",
    [
        [("2024-01-01", "0.01"), ("2024-01-01", "0.02")],
        [("2024-01-01", "0.01"), ("2024-03-01", "0.02")],
        [("2024-02-01", "0.01"), ("2024-01-01", "0.02")],
    ],
)
def test_convert_rejects_invalid_month_sequences(
        tmp_path: Path, rows: list[tuple[str, str]]
) -> None:
    """Duplicate, missing, and decreasing monthly dates are rejected."""
    source = tmp_path / "monthly.csv"
    _write_monthly_file(source, rows)

    with pytest.raises(ValueError, match="unique, monotonically increasing"):
        converter.convert_cpi_to_daily(str(source), str(tmp_path / "daily.csv"))


def test_main_uses_constants_and_displays_summary(
        tmp_path: Path, monkeypatch: pytest.MonkeyPatch, capsys: pytest.CaptureFixture[str]
) -> None:
    """The public entry point uses constants and prints required metadata."""
    source = tmp_path / "monthly.csv"
    output = tmp_path / "daily.csv"
    _write_monthly_file(source, [("2024-02-01", "0.01")])
    monkeypatch.setattr(converter, "MONTHLY_CPI_FILE", str(source))
    monkeypatch.setattr(converter, "DAILY_CPI_FILE", str(output))

    assert converter.main() == 0
    displayed = capsys.readouterr().out
    assert f"Input file: {source}" in displayed
    assert f"Output file: {output}" in displayed
    assert "Input records: 1" in displayed
    assert "Written records: 29" in displayed
    assert "Input minimum date: 2024-02-01" in displayed
    assert "Output maximum date: 2024-02-29" in displayed
