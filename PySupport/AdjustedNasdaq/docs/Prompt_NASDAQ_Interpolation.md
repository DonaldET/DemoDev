# NASDAQ Missing Date and Missing Value Interpolation

You will create a Python function named `fill_in_missing_days` in a module named `../interpolate_nasdaq.py`. The function accepts a non-empty pandas DataFrame parameter with two columns: `observation_date` and `NASDAQCOM`. It returns an augmented DataFrame with the same two columns and datatypes, and its index is intentionally replaced with `observation_date`. The output optionally contains inserted generated `observation_date` and interpolated `NASDAQCOM` values where required, along with counts of insertions and interpolations made. The count of interpolations is the number of `NASDAQCOM` missing values after daily reindexing and before interpolation, including both original missing values and inserted rows. The input DataFrame must contain at least nine rows; otherwise, raise `ValueError`.

The function signature is:

```python
import pandas as pd


def fill_in_missing_days(
    nasdaq_composite_df: pd.DataFrame,
) -> tuple[pd.DataFrame, int, int]:
```

where:

- `nasdaq_composite_df`: a DataFrame with values to be augmented by interpolation.
- `Return_value[0]`: a DataFrame with the same columns as `nasdaq_composite_df` optionally containing interpolated `NASDAQCOM` values and added rows of generated dates and interpolated `NASDAQCOM` values.
- `Return_value[1]`: number of added rows filling in missing date values.
- `Return_value[2]`: number of interpolations performed.

> **Note:** Raise `ValueError` for every constraint violation.

Only two columns are allowed in the input and output DataFrame, with the output DataFrame having the same two columns. The two columns are `observation_date` and `NASDAQCOM`. The `observation_date` column typically represents a NASDAQ closing date (datatype exactly `datetime64[ns]`). Generated weekend and holiday values are synthetic analytical estimates and are not official NASDAQ closing values. The `observation_date` column is in ascending order with unique dates, and typically ranges between 2023-01-01 through 2026-07-31. Its datatype is `datetime64[ns]` exactly and may not contain missing values (marked by `NaT`). The `NASDAQCOM` column generally represents the NASDAQ Composite index closing value for that date; it is a positive non-zero floating-point number. `NASDAQCOM` must have datatype `float64` exactly. Missing values, if present, must be represented by IEEE floating-point `NaN`. No other datatype is permitted. The input DataFrame column `NASDAQCOM` must be kept to full precision; for column `NASDAQCOM`, apply `pandas.Series.round(4)` only to positions that were missing immediately before interpolation; do not round valid input values.

## Input Constraint Validations

1. `observation_date` column must be a valid `datetime64[ns]` value where the time component is midnight 00:00:00.
1. `observation_date` column must be strictly monotonically increasing and unique.
1. `NASDAQCOM` column values may be missing (represented by `NaN`); otherwise, they must be a number greater than zero.
1. `NASDAQCOM` cannot be entirely missing.

## Interpolation Definition

Using a Python function `fill_in_missing_days`, you will clean up and reformat that input DataFrame and create a new output DataFrame with the same column names holding the cleaned data. Rows for missing calendar dates are generated only between the minimum and maximum input dates. Do not modify the input DataFrame. Note that generation and interpolation will be used to fill in non-market days.

Do not modify input rows, copied to the output DataFrame, that have valid `observation_date` and valid `NASDAQCOM` values. Preserve valid input values and round only interpolated `NASDAQCOM` values. Note that interpolation can only be performed on interior rows bounded by non-missing values. Do not extrapolate. Raise a `ValueError` if the first or last `NASDAQCOM` value is missing, or if any `NASDAQCOM` value remains missing after interior interpolation.

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
1. Validate normalized, unique, strictly increasing dates and finite positive or optionally missing `NASDAQCOM` values.
1. Reject missing first or last `NASDAQCOM` values.
1. Work from a deep copy of the input.
1. Generate an inclusive daily date range from the minimum through maximum input date.
1. Reindex onto that range and count the newly created rows.
1. Count all missing `NASDAQCOM` values at that point.
1. Perform date-distance-weighted linear interpolation only between valid bounding `NASDAQCOM` values.
1. Verify that no missing `NASDAQCOM` values remain.
1. Round interpolated `NASDAQCOM` values to four decimals, and
1. Set `observation_date` as the index with `drop=False` and `inplace=True`.

## Additional Generated Modules

Generate a Python module named `test_interpolate_nasdaq.py` that provides unit tests for the `fill_in_missing_days` function. The module must use test data like the examples above for the `observation_date` column. Test dates must be pandas timestamps normalized to midnight (00:00:00).

## Test Cases

Implement these test cases using pytest and express all dates as datatype `datetime64[ns]`. Do not allow any other datatype in the following tests:

- Test non-DataFrame argument (invalid).
- Empty input DataFrame (invalid).
- The input DataFrame must contain at least nine rows; otherwise, raise `ValueError`.
- Input DataFrame with 9 rows (valid).
- One interior missing day.
- Two nonadjacent interior missing days.
- Multiple interior consecutive missing days.
- Input with no missing days.
- Input DataFrame remains unchanged.
- Output column order, datatypes, row order, and index.
- Invalid dates, duplicates, nonpositive NASDAQ values.
- Incorrect or reordered column names and extra columns.
- Wrong DataFrame and column datatypes.
- `NaT` and timestamps that are not midnight.
- Unsorted and duplicate dates.
- Zero, negative, positive infinity, and negative infinity (invalid).
- Leading, trailing, and entirely missing `NASDAQCOM` values.
- Correct counts when no interpolation is needed.
- Correct count semantics for inserted rows plus pre-existing `NaN` values.
- Numerical rounding without requiring trailing-zero display formatting.
- Preservation of the original DataFrame, including its index and values.
- Exact output `DatetimeIndex`, index name, column order, datatypes, and ascending row order.

## Implementation Notes

Generate code compatible with Python 3.13 or later. All generated modules must provide module-level docstrings and generated functions must have docstrings. The module-level docstrings will describe the interpolation process.

Every date or date range created by either the implementation or unit tests must explicitly use datetime64[ns] resolution. Do not rely on pandas datatype inference from string literals. Apply .as_unit("ns") to results from pd.date_range() and pd.to_datetime(), or construct a Series with dtype="datetime64[ns]". This requirement applies to input data, intermediate values, expected test values, DataFrame columns, and expected DatetimeIndex objects. Unit tests must assert that these objects have exact datatype datetime64[ns].
