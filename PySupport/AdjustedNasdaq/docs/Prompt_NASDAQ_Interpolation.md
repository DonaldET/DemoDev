# NASDAQ Missing Date and Missing Value Interpolation

You will create a Python function named `fill_in_missing_days` in a module named `interpolate_nasdaq.py`. The function accepts a non-empty pandas DataFrame parameter with two columns: `observation_date` and `NASDAQCOM`. The returned augmented DataFrame must contain exactly the same two columns, in the same order and with the same column datatypes. Its original index is not preserved; the returned index must be replaced with `observation_date`.

The output contains inserted rows and interpolated values when the input requires them, along with counts of insertions and interpolations made. The input DataFrame must have at least nine rows; this is a requirement for later processing steps.

The function signature is:

```python
import pandas as pd


def fill_in_missing_days(
    nasdaq_composite_df: pd.DataFrame,
) -> tuple[pd.DataFrame, int, int]:
```

where:

- `nasdaq_composite_df`: a DataFrame with values to be augmented by interpolation.
- `augmented_df`: a returned DataFrame with the same columns as `nasdaq_composite_df` containing interpolated `NASDAQCOM` values and added rows of generated dates and interpolated `NASDAQCOM` values if required.
- `count_of_added_rows` equals the number of added rows filling in missing date values.
- `count_interpolations` equals the number of missing `NASDAQCOM` values immediately after daily reindexing and before interpolation. It therefore includes both pre-existing `NaN` values and `NaN` values introduced for generated rows.

> **Note:** Raise a `TypeError` when:
- `nasdaq_composite_df` is not a pandas DataFrame.
- `observation_date` or `NASDAQCOM` has an incorrect datatype.

Raise a `ValueError` for every other validation failure.

Only two columns are allowed in the input and output DataFrame, with the output DataFrame having the same two columns. The two columns are `observation_date` and `NASDAQCOM`. The `observation_date` column represents a NASDAQ closing date (datatype exactly `datetime64[ns]`); and it represents generated weekend and holiday values that are  strictly increasing`observation_date` are synthetic analytical estimates and are not official NASDAQ closing values. The `observation_date` column is in ascending order with unique dates, and typically ranges between 2023-01-01 through 2026-07-31. Its datatype is `datetime64[ns]` exactly and may not contain missing values (marked by `NaT`). The `NASDAQCOM` column represents the NASDAQ Composite index closing value for that date; it is a positive non-zero floating-point number. `NASDAQCOM` must have datatype `float64` exactly. Missing values, if present, must be represented by IEEE floating-point `NaN`. No other datatype is permitted. The input DataFrame column `NASDAQCOM` must be kept to full precision; for column `NASDAQCOM`, apply `pandas.Series.round(4)` only to positions that were missing immediately before interpolation; do not round valid input values.

## Input Data Constraint Validations

1. The `observation_date` column shall contain only normalized datetime64[ns] timestamps.
1. `observation_date` column must be strictly monotonically increasing and unique.
1. `NASDAQCOM` column values may be missing (represented by `NaN`); otherwise, they must be a number greater than zero.
1. `NASDAQCOM` cannot be entirely missing.

## Interpolation Definition

Using a Python function `fill_in_missing_days`, you will clean up and reformat that input DataFrame and create a new output DataFrame with the same column names containing the cleaned data. Rows for missing calendar dates are generated only between the minimum and maximum input dates. Do not modify the input DataFrame. Note that generation and interpolation will be used to fill in non-market days.

Do not modify input rows, copied to the output DataFrame, that have valid `observation_date` and valid `NASDAQCOM` values. For every input row whose `NASDAQCOM` value is not missing, the corresponding output value must compare exactly equal to the input value. Note that interpolation can only be performed on interior rows bounded by non-missing values. Do not extrapolate `NASDAQCOM` values. Raise a `ValueError` if the first or last `NASDAQCOM` value is missing, or if any `NASDAQCOM` value remains missing after interior interpolation.

Fill missing `NASDAQCOM` values using date-distance-weighted linear interpolation. Here is an example:

### With missing

| observation_date | NASDAQCOM |
| ---------------- | --------- |
| 1/7/2023         | 10386.98  |
| 1/8/2023         | 10458.76  |
| 1/9/2023         | 10305.24  |
| 1/10/2023        | 10569.29  |
| 1/11/2023        | 10635.65  |
| 1/12/2023        | 10742.63  |
| 1/13/2023        | 10931.67  |
| 1/14/2023        | 11001.1   |
| 1/15/2023        | 11079.16  |
| 1/16/2023        | **NaN**   |
| 1/17/2023        | 11095.11  |

### Interpolated

| observation_date | NASDAQCOM        |
| ---------------- | ---------------- |
| 1/7/2023         | 10386.98         |
| 1/8/2023         | 10458.76         |
| 1/9/2023         | 10305.24         |
| 1/10/2023        | 10569.29         |
| 1/11/2023        | 10635.65         |
| 1/12/2023        | 10742.63         |
| 1/13/2023        | 10931.67         |
| 1/14/2023        | 11001.1          |
| 1/15/2023        | 11079.16         |
| 1/16/2023        | ***11087.1350*** |
| 1/17/2023        | 11095.11         |

In the above example, the missing `NASDAQCOM` value for date 1/16/2023 is computed by:

- (`NASDAQCOM` [1/15/2023] + `NASDAQCOM` [1/17/2023]) / 2 = 11087.1350.

It may also be possible that entire rows are missing, not just the `NASDAQCOM` value. In that case, both the date and the `NASDAQCOM` values are provided in a new row using generation for the `observation_date` and linear interpolation for the associated `NASDAQCOM` value. Compute calendar days and provide the interpolation even if it is not a market trading day. From the following example table, there are two missing rows for dates 1/15/2023 and 1/16/2023:

There are two missing dates and three one-day intervals between January 14 and January 17. The `NASDAQCOM` value difference is 94.01, and is the difference between the January 14 and January 17 bounding values across three one-day intervals. We see that:

- `delta = (11095.11 - 11001.10) / 3`
- Value at 2023-01-15 = `round(11001.10 + delta, 4)` = 11032.4367
- Value at 2023-01-16 = `round(11001.10 + 2 × delta, 4)` = 11063.7733
- Value at 2023-01-17 = `round(11001.10 + 3 × delta, 4)` = 11095.11 (verification only)

Below is an example (note, 1/17/2023 not modified):

### With missing

| observation_date | NASDAQCOM |
| ---------------- | --------- |
| 1/7/2023         | 10386.98  |
| 1/8/2023         | 10458.76  |
| 1/9/2023         | 10305.24  |
| 1/10/2023        | 10569.29  |
| 1/11/2023        | 10635.65  |
| 1/12/2023        | 10742.63  |
| 1/13/2023        | 10931.67  |
| 1/14/2023        | 11001.1   |
| 1/17/2023        | 11095.11  |

### Interpolated

| observation_date | NASDAQCOM        |
| ---------------- | ---------------- |
| 1/7/2023         | 10386.98         |
| 1/8/2023         | 10458.76         |
| 1/9/2023         | 10305.24         |
| 1/10/2023        | 10569.29         |
| 1/11/2023        | 10635.65         |
| 1/12/2023        | 10742.63         |
| 1/13/2023        | 10931.67         |
| 1/14/2023        | 11001.1          |
| ***1/15/2023***  | ***11032.4367*** |
| ***1/16/2023***  | ***11063.7733*** |
| 1/17/2023        | 11095.11         |

Make sure the output DataFrame has an updated `observation_date` column. It must set `observation_date` as the index while retaining it as a column (i.e. `augmented_df.set_index("observation_date", drop=False, inplace=True)`). First, add new rows for skipped `observation_date` values having missing `NASDAQCOM` values. Then, interpolate all interior missing `NASDAQCOM` values.

## Function Usage Example

The following is a function invocation:

```python
augmented_df, count_added_rows, count_interpolations = fill_in_missing_days(
    nasdaq_com_df
)
```

### Sample NASDAQ Input (`nasdaq_com_df`)

| observation_date | NASDAQCOM |
| ---------------- | --------- |
| 1/7/2023         | 10386.98  |
| 1/8/2023         | 10458.76  |
| 1/9/2023         | NaN       |
| 1/10/2023        | 10569.29  |
| 1/11/2023        | 10635.65  |
| 1/12/2023        | 10742.63  |
| 1/13/2023        | 10931.67  |
| 1/14/2023        | 11001.1   |
| 1/17/2023        | 11095.11  |

### Sample Function Return DataFrame (`augmented_df`)

| observation_date | NASDAQCOM        |
| ---------------- | ---------------- |
| 1/7/2023         | 10386.98         |
| 1/8/2023         | 10458.76         |
| 1/9/2023         | **10,514.0250**  |
| 1/10/2023        | 10569.29         |
| 1/11/2023        | 10635.65         |
| 1/12/2023        | 10742.63         |
| 1/13/2023        | 10931.67         |
| 1/14/2023        | 11001.1          |
| ***1/15/2023***  | ***11032.4367*** |
| ***1/16/2023***  | ***11063.7733*** |
| 1/17/2023        | 11095.11         |

The `count_added_rows` is 2 and the `count_interpolations` is 3.

Note that (10458.76 + 10569.29) /2 = **10,514.0250**. Dates 1/15/2023 and 1/16/2023 are taken from the above example about inserting new rows for gaps in dates.

## Algorithm

1. Validate that the input is a nonempty DataFrame with exactly these columns, in this order:
   1. `observation_date`, `NASDAQCOM`
1. Verify the DataFrame has more than 8 rows.
1. Require exact datatypes:
   1. `observation_date`: `datetime64[ns]`
   1. `NASDAQCOM`: `float64`
1. Validate normalized, unique, strictly increasing `observation_date` values.
1. Validate that every `NASDAQCOM` value is either `NaN` or a finite positive floating-point value.
1. Reject missing first or last `NASDAQCOM` values.
1. Work from a deep copy of the input.
1. Generate an inclusive daily date range from the minimum through maximum input date.
1. Reindex onto that range and count the newly created rows.
1. Immediately after daily reindexing and before interpolation, save a Boolean mask such as interpolation_mask = augmented_df["NASDAQCOM"].isna(). Use this same mask both to calculate count_interpolations and to round only the values filled by interpolation.
1. Count all missing `NASDAQCOM` values at that point.
1. Perform date-distance-weighted linear interpolation only between valid bounding `NASDAQCOM` values (e.g., `Series.interpolate(method="linear")`)
1. Verify that no missing `NASDAQCOM` values remain.
1. Round interpolated `NASDAQCOM` values to four decimals, and
1. Set `observation_date` as the index with `drop=False` and `inplace=True`.

Interpolation _must_ occur after reindexing because any `NASDAQCOM` interpolated values computed prior to adding a new `observation_date` between the bounding dates of the prior interpolation would invalidate the calculation.

## Additional Generated Modules

Generate a Python module named `test_interpolate_nasdaq.py` that provides unit tests for the `fill_in_missing_days` function. The module must use test data like the examples above for the `observation_date` column. Test dates must be pandas timestamps normalized to midnight (00:00:00).

## Test Cases

Implement these test cases using pytest and express all dates as datatype `datetime64[ns]`.  Unit tests should assert the exception type and relevant message fragment, but should not depend on the ordering of unrelated validation checks. Do not allow any other datatype in the following tests:

Sample error messages require descriptive text containing the failed field or rule, for example:
- The `nasdaq_composite_df` must be a pandas DataFrame
- Input must contain at least 9 rows
- Column names must be exactly ['observation_date', 'NASDAQCOM'].
- `NASDAQCOM` must contain finite positive values or `NaN`.
- `observation_date` must contain valid normalized timestamps and must not contain `NaT`.
- First and last `NASDAQCOM` values must not be missing.

### Nominal Correct Execution

- An input DataFrame with no missing days or NASDAQCOM values.

### Input Parameter Validation

- Test non-DataFrame argument (invalid) and issue a `TypeError`.
- Empty input DataFrame (invalid).
- The input DataFrame must contain at least nine rows; otherwise, raise `ValueError`.

### Missing NASDAQCOM Values

- One interior row with a missing NASDAQCOM value
- Two nonadjacent rows with missing NASDAQCOM values
- Multiple consecutive rows with missing NASDAQCOM values.
- NASDAQCOM values missing in first or last row
-  A pre-existing `NaN` and one or more absent calendar-date rows occur between the same bounding values.

### Missing observation_date Values

- One interior absent calendar-date row.
- Two nonadjacent interior absent calendar-date rows.
- Multiple consecutive absent interior calendar-date rows.
- First row contains `NaT`.
- Last row contains `NaT`.
- Middle row contains a `NaT`.

### General Constraints

- Input DataFrame remains unchanged.
- Correct output column order, datatypes, and index.
- Invalid dates, duplicate dates, out of order dates, out of range NASDAQ values.
- Incorrect column names.
- Reordered column names.
- Extra columns.
- Wrong column datatypes.
- `NaT` and timestamps that are not midnight.
- Unsorted and duplicate dates.
- Zero, negative, positive infinity, and negative infinity NASDAQCOM values (invalid).

### Output DataFrame Constraints
- Correct counts when no interpolation is needed.
- Correct count semantics for inserted rows plus pre-existing `NaN` values.
- Preservation of the original non-interpolated DataFrame rows.
- Exact output `DatetimeIndex`, index name, column order, datatypes, and ascending row order.

Note that later processing steps of this data use a forecasting algorithm that requires more than eight rows of data.

## Implementation Notes

Generate code compatible with Python 3.13 or later. Pandas 3+ is also required. All generated modules must provide module-level docstrings and generated functions must have docstrings. The module-level docstrings will describe the interpolation process.

Every date or date range created by either the implementation or unit tests must explicitly use datetime64[ns] resolution. Do not rely on pandas datatype inference from string literals. Apply .as_unit("ns") to results from pd.date_range() and pd.to_datetime(), or construct a Series with dtype="datetime64[ns]". This requirement applies to input data, intermediate values, expected test values, DataFrame columns, and expected DatetimeIndex objects. Unit tests must assert that these objects have exact datatype datetime64[ns].
