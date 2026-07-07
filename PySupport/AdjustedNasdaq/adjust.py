"""
NOMINAL PRICE HISTORY:
NASDAQ Composite obtained from https://fred.stlouisfed.org/series/NASDAQCOM
Index Feb 5, 1971=100, Not Seasonally Adjusted

U.S. CONSUMER PRICE INDEX (CPI)
U.S. Bureau of Labor Statistics - https://www.bls.gov/cpi/
Methodology: https://www.bls.gov/cpi/additional-resources/chained-cpi.htm
Available Files:
download.bls.gov - /pub/time.series/su/
 6/10/2026  8:30 AM           90 su.area
 6/10/2026  8:30 AM           32 su.base
 6/10/2026  8:30 AM       401786 su.data.0.Current    <-- these two are identical as of 7/4/2026
 6/10/2026  8:30 AM       401786 su.data.1.AllItems
 6/10/2026  8:30 AM           51 su.footnote
 6/10/2026  8:30 AM          891 su.item
 6/10/2026  8:30 AM           46 su.periodicity
10/12/2018  1:41 PM           79 su.seasonal
 6/10/2026  8:30 AM         4914 su.series
 2/14/2018 10:30 AM        10689 su.txt
"""
import pandas as pd

START_DATE = '2023-07-01'
END_DATE = '2026-07-01'
RAW_NASDAQ = r'data\NASDAQCOM_IXIC.csv'
CPI_INFO = 'CPIAUCNS.csv'

OUTPUT_FILE = r'output\nasdaq_daily_inflation_model.csv'


def _input_data_and_filter(nasdaq_file: str, cpi_file: str, start_date: str, end_date: str):
    # 1. Load the raw Yahoo Finance and FRED data
    print(f"\t#1a read NASDAQ file: {nasdaq_file}")
    nasdaq_df = pd.read_csv(nasdaq_file, parse_dates=['observation_date'])
    print(f"\t  - Columns: {nasdaq_df.columns}  Length: {len(nasdaq_df)}")
    print(nasdaq_df)

    print(f"\n\t#2a read CPI file: {cpi_file}")
    cpi_df = pd.read_csv(cpi_file, parse_dates=['DATE'])

    # 2. Filter dates to matching project bounds (e.g. July 2023 through July 2026)
    nasdaq_df = nasdaq_df[(nasdaq_df['Date'] >= start_date) & (nasdaq_df['Date'] <= end_date)]
    cpi_df = cpi_df[(cpi_df['DATE'] >= start_date) & (cpi_df['DATE'] <= end_date)]
    return nasdaq_df, cpi_df


def _interpolate_monthly_inflation(start_date: str, end_date: str):
    # 3. Create a continuous daily calendar matrix to hold the interpolation
    daily_dates = pd.date_range(start=start_date, end=end_date, freq='D')
    daily_dates_df = pd.DataFrame({'Date': daily_dates})
    return daily_dates_df


def _merge_model(nasdaq_df, model_df, cpi_df):
    # 4. Merge monthly CPI onto the continuous calendar and linearly interpolate it
    model_df = pd.merge(model_df, cpi_df, left_on='Date', right_on='DATE', how='left').drop(columns=['DATE'])
    model_df['CPIAUCNS'] = model_df['CPIAUCNS'].interpolate(method='linear')

    # 5. Establish your target base period currency (e.g., July 2026 dollars)
    # Note: If July 2026 CPI is not yet published, use the latest available datapoint (e.g., June 2026)
    latest_cpi = model_df['CPIAUCNS'].iloc[-1]

    # 6. Merge the interpolated framework with the active stock trading days
    merged_model = pd.merge(nasdaq_df[['Date', 'Close']], model_df, on='Date', how='inner')
    return merged_model, latest_cpi


def _create_final_model(merged_model, latest_cpi, output_file: str):
    # 7. Compute the exact daily-interpolated inflation-adjusted column
    final_model = merged_model
    final_model['Inflation_Adjusted_Close'] = final_model['NASDAQCOM'] * (latest_cpi / final_model['CPIAUCNS'])

    # 8. Export your clean raw model data
    final_model.to_csv(output_file, index=False)
    return final_model


def main() -> int:
    print(f"Creating Inflation Adjusted NASDAQ Composite for {START_DATE} to {END_DATE}")
    nasdaq_df, cpi_df = _input_data_and_filter(RAW_NASDAQ, CPI_INFO, START_DATE, END_DATE)
    daily_dates_df = _interpolate_monthly_inflation(START_DATE, END_DATE)
    merge_model, latest_cpi = _merge_model(nasdaq_df, daily_dates_df, cpi_df)
    final_model = _create_final_model(merge_model, latest_cpi, OUTPUT_FILE)
    print(f"Processing complete for {final_model['Date'].strftime('%Y-%m-%d')}.")
    return 0

if __name__ == '__main__':
    main()