"""
NASDAQ Formatter

Reads raw NASDAQ Composite index data from a CSV file, validates and reformats
the data, fills missing index values using linear interpolation, and writes the
corrected data to a new Excel-dialect CSV file.
"""

import csv

import pandas as pd
from pandas import Timestamp, Timedelta

DEFAULT_INPUT_FILE_NAME = r'data\NASDAQCOM_RAW.csv'
DEFAULT_OUTPUT_FILE_NAME = r'data\NASDAQCOM.csv'

DATE_COLUMN = "observation_date"
NASDAQ_COLUMN = "NASDAQCOM"

MIN_DATE: Timestamp = Timestamp(pd.Timestamp("2023-01-01"))
MAX_DATE: Timestamp = Timestamp(pd.Timestamp("2026-07-02"))
NA_T_TYPE_TIMEDELTA: Timedelta = Timedelta(pd.Timedelta(0))

MIN_NASDAQ_VALUE = 10000.0
MAX_NASDAQ_VALUE = 29000.0


def _check_missing_values(df: pd.DataFrame) -> pd.DataFrame:
    if list(df.columns) != [DATE_COLUMN, NASDAQ_COLUMN]:
        raise ValueError(
            f"ERROR: Input CSV must contain these columns: "
            f"{DATE_COLUMN}, {NASDAQ_COLUMN}"
        )

    df[DATE_COLUMN] = df[DATE_COLUMN].fillna("").str.strip()
    df[NASDAQ_COLUMN] = df[NASDAQ_COLUMN].fillna("").str.strip()

    if df[DATE_COLUMN].eq("").any():
        raise ValueError("ERROR: observation_date must not be missing.")
    df[DATE_COLUMN] = pd.to_datetime(df[DATE_COLUMN])

    return df


def _validate_df(df: pd.DataFrame) -> pd.DataFrame:
    if df[DATE_COLUMN].isna().any():
        raise ValueError("ERROR: One or more observation_date values are invalid.")

    if ((df[DATE_COLUMN] < MIN_DATE) | (df[DATE_COLUMN] > MAX_DATE)).any():
        raise ValueError("ERROR: One or more observation_date values are outside the valid range.")

    duplicate_count = int(df[DATE_COLUMN].duplicated().sum())
    if duplicate_count > 0:
        raise ValueError("ERROR: observation_date values must be unique.")

    dates_not_increasing = int((df[DATE_COLUMN].diff() <= NA_T_TYPE_TIMEDELTA).sum())
    if dates_not_increasing > 0:
        raise ValueError(
            "ERROR: observation_date values must be monotonically increasing,"
            " found {dates_not_increasing} out of order.")

    df[NASDAQ_COLUMN] = df[NASDAQ_COLUMN].replace("", pd.NA)
    na_before_coerce = df[NASDAQ_COLUMN].isna().sum()
    df[NASDAQ_COLUMN] = pd.to_numeric(df[NASDAQ_COLUMN], errors="coerce")
    na_after_coerce = df[NASDAQ_COLUMN].isna().sum()
    invalid_numeric_count = na_after_coerce - na_before_coerce
    if invalid_numeric_count > 0:
        raise ValueError(
            "ERROR: f{NASDAQ_COLUMN} wasn't all valid numeric values or blank, na in {invalid_numeric_count} rows")

    valid_numeric_values = df[NASDAQ_COLUMN].dropna()
    if ((valid_numeric_values < MIN_NASDAQ_VALUE) | (valid_numeric_values > MAX_NASDAQ_VALUE)).any():
        raise ValueError(f"One or more NASDAQCOM values are outside the valid range.")
    print(f"  - NASDAQCOM had {len(valid_numeric_values)} valid numeric values.")

    return df


def _interpolate(df: pd.DataFrame) -> pd.DataFrame:
    missing_before = int(df[NASDAQ_COLUMN].isna().sum())
    df[NASDAQ_COLUMN] = df[NASDAQ_COLUMN].interpolate(method="linear")
    missing_after = int(df[NASDAQ_COLUMN].isna().sum())

    interpolations_performed = missing_before - missing_after
    print(f"  - Performed {interpolations_performed} interpolations.")

    if missing_after > 0:
        raise ValueError(
            f"ERROR: Unable to interpolate {missing_after} missing NASDAQCOM values. "
            "Missing values may occur at the beginning or end of the file."
        )

    if ((df[NASDAQ_COLUMN] < MIN_NASDAQ_VALUE) | (df[NASDAQ_COLUMN] > MAX_NASDAQ_VALUE)).any():
        raise ValueError(
            f"ERROR: One or more NASDAQCOM values are outside the valid range {MIN_NASDAQ_VALUE} to {MAX_NASDAQ_VALUE}.")

    return df


def _process_nasdaq_file(input_file_name: str, output_file_name: str) -> None:
    """
    Clean, validate, interpolate, force date format, and write NASDAQ Composite index data.
    """
    print(f"Reading input from {input_file_name}")
    df = pd.read_csv(input_file_name, dtype=str)
    records_read = len(df)
    if records_read < 1:
        raise ValueError("ERROR: No NASDAQCOM records found.")

    df = _check_missing_values(df)
    df = _validate_df(df)
    df - _interpolate(df)
    df[DATE_COLUMN] = df[DATE_COLUMN].dt.strftime("%Y-%m-%d")

    df.to_csv(output_file_name, index=False, mode="w", header=[DATE_COLUMN, NASDAQ_COLUMN])

    with open(output_file_name, "w", newline="", encoding="utf-8") as output_file:
        writer = csv.writer(output_file, dialect="excel")
        print(f"Writing output to {output_file_name}")
        writer.writerow([DATE_COLUMN, NASDAQ_COLUMN])

        min_date = df[DATE_COLUMN].min()
        max_date = df[DATE_COLUMN].max()

        min_value = df[NASDAQ_COLUMN].min()
        max_value = df[NASDAQ_COLUMN].max()
        avg_value = df[NASDAQ_COLUMN].mean()
        std_value = df[NASDAQ_COLUMN].std()

        print(f"  - minimum date : {min_date}")
        print(f"  - maximum date : {max_date}")
        print(f"\n  - minimum value: {min_value}")
        print(f"  - maximum value: {max_value}")
        print(f"  - average value: {avg_value}")
        print(f"  - SD           : {std_value}")

        for _, row in df.iterrows():
            writer.writerow([
                row[DATE_COLUMN],
                f"{row[NASDAQ_COLUMN]:.2f}",
            ])

    print(f"\n  - Records read   : {records_read}")
    print(f"  - Records written: {len(df)}")


def main() -> None:
    """
    Define input and output file names and delegate file processing.
    """
    input_file_name = DEFAULT_INPUT_FILE_NAME
    output_file_name = DEFAULT_OUTPUT_FILE_NAME
    print(f"Formatting NASDAQ Composite Index data; reading {input_file_name} and producing {output_file_name}.")
    _process_nasdaq_file(input_file_name, output_file_name)


if __name__ == "__main__":
    main()
