# CPI Missing Date and Missing Value Interpolation

You will create a Python function named `fill_in_missing_months` in a module named `../interpolate_cpi.py`. The function accepts a non-empty pandas DataFrame parameter with two columns: `date` and `cpi`. It returns an augmented DataFrame with the same two columns and datatypes, and its index is intentionally replaced with `date`. The output optionally contains inserted generated `date` and interpolated `cpi` values where required, along with counts of insertions and interpolations made. The count of interpolations is the number of `cpi` missing values after monthly reindexing and before interpolation, including both original missing values and inserted rows. The input DataFrame must contain at least nine rows; otherwise, raise `ValueError`.

The function signature is:

```python
import pandas as pd


def fill_in_missing_months(
    chained_cpi_df: pd.DataFrame,
) -> tuple[pd.DataFrame, int, int]:
```

where:

- `chained_cpi_df`: a DataFrame with values to be augmented by interpolation.
- `Return_value[0]`: a DataFrame with the same columns as `chained_cpi_df`, optionally containing interpolated `cpi` values and added rows of generated dates and interpolated `cpi` values.
- `Return_value[1]`: number of added rows filling in missing monthly date values.
- `Return_value[2]`: number of interpolations performed.

> **Note:** Raise `ValueError` for every constraint violation.

Only two columns are allowed in the input and output DataFrame, with the output DataFrame having the same two columns. The two columns are `date` and `cpi`. The `date` column represents the first day of a month for the Chained Consumer Price Index for All Urban Consumers (C-CPI-U), series `SUUR0000SA0` (datatype exactly `datetime64[ns]`). Generated dates represent missing monthly observations between existing CPI observations. The `date` column is in ascending order with unique dates, and typically ranges from 2023-01-01 through 2026-07-01. Its datatype is `datetime64[ns]` exactly, every value must be the first day of its month at midnight 00:00:00, and it may not contain missing values marked by `NaT`. The `cpi` column represents a floating-point CPI value greater than `-1.0` and less than `2.0`. The `cpi` column must have datatype `float64` exactly. Missing values, if present, must be represented by IEEE floating-point `NaN`. No other datatype is permitted. The input DataFrame column `cpi` must be kept to full precision; apply `pandas.Series.round(4)` only to positions that were missing immediately before interpolation. Do not round valid input values.

## Input Constraint Validations

1. The `date` column must have datatype exactly `datetime64[ns]`.
1. Every `date` value must be the first day of its month and have a time component of midnight 00:00:00.
1. The `date` column must be strictly monotonically increasing and unique.
1. `cpi` values may be missing, represented by `NaN`; otherwise, each value must be finite, greater than `-1.0`, and less than `2.0`.
1. The `cpi` column cannot be entirely missing.

## Interpolation Definition

Using the Python function `fill_in_missing_months`, clean and reformat the input DataFrame and create a new output DataFrame with the same column names holding the cleaned data. Generate rows for missing first-of-month dates only between the minimum and maximum input dates. Do not modify the input DataFrame.

Do not modify input rows copied to the output DataFrame that have valid `date` and valid `cpi` values. Preserve valid input values and round only interpolated `cpi` values. Interpolation can only be performed on interior rows bounded by non-missing values. Do not extrapolate. Raise `ValueError` if the first or last `cpi` value is missing, or if any `cpi` value remains missing after interior interpolation.

Fill missing `cpi` values using date-distance-weighted linear interpolation. Because every generated date is separated by a whole number of months and is the first day of its month, interpolation weights must be based on the actual timestamp distance between bounding dates. Here is an example.

### With missing

| date       | cpi    |
| ---------- | ------ |
| 2023-01-01 | 0.0104 |
| 2023-02-01 | 0.0105 |
| 2023-03-01 | 0.0103 |
| 2023-04-01 | 0.0106 |
| 2023-05-01 | 0.0106 |
| 2023-06-01 | 0.0107 |
| 2023-07-01 | 0.0109 |
| 2023-08-01 | 0.0110 |
| 2023-09-01 | 0.0111 |
| 2023-10-01 | **NaN** |
| 2023-11-01 | 0.0111 |

### Interpolated

| date       | cpi          |
| ---------- | ------------ |
| 2023-01-01 | 0.0104       |
| 2023-02-01 | 0.0105       |
| 2023-03-01 | 0.0103       |
| 2023-04-01 | 0.0106       |
| 2023-05-01 | 0.0106       |
| 2023-06-01 | 0.0107       |
| 2023-07-01 | 0.0109       |
| 2023-08-01 | 0.0110       |
| 2023-09-01 | 0.0111       |
| 2023-10-01 | ***0.0111*** |
| 2023-11-01 | 0.0111       |

In the example, the missing `cpi` value for 2023-10-01 is bounded by equal values, so the date-distance-weighted interpolation is:

- `0.0111 + (0.0111 - 0.0111) × ((2023-10-01 - 2023-09-01) / (2023-11-01 - 2023-09-01)) = 0.0111`.

It may also be possible that entire rows are missing, not just the `cpi` value. In that case, provide both the generated first-of-month `date` and the linearly interpolated `cpi` value in each new row. From the following example table, rows for 2023-09-01 and 2023-10-01 are missing.

There are two missing monthly dates between the bounding dates 2023-08-01 and 2023-11-01. Use the actual date distance for each generated timestamp:

- `total_span = 2023-11-01 - 2023-08-01`
- `weight_2023_09_01 = (2023-09-01 - 2023-08-01) / total_span`
- `weight_2023_10_01 = (2023-10-01 - 2023-08-01) / total_span`
- Value at 2023-09-01 = `round(0.0110 + (0.0111 - 0.0110) × weight_2023_09_01, 4)` = `0.0110`
- Value at 2023-10-01 = `round(0.0110 + (0.0111 - 0.0110) × weight_2023_10_01, 4)` = `0.0111`
- Value at 2023-11-01 remains `0.0111` and is not modified.

Below is the complete example.

### With missing

| date       | cpi    |
| ---------- | ------ |
| 2023-01-01 | 0.0104 |
| 2023-02-01 | 0.0105 |
| 2023-03-01 | 0.0103 |
| 2023-04-01 | 0.0106 |
| 2023-05-01 | 0.0106 |
| 2023-06-01 | 0.0107 |
| 2023-07-01 | 0.0109 |
| 2023-08-01 | 0.0110 |
| 2023-11-01 | 0.0111 |

### Interpolated

| date             | cpi          |
| ---------------- | ------------ |
| 2023-01-01       | 0.0104       |
| 2023-02-01       | 0.0105       |
| 2023-03-01       | 0.0103       |
| 2023-04-01       | 0.0106       |
| 2023-05-01       | 0.0106       |
| 2023-06-01       | 0.0107       |
| 2023-07-01       | 0.0109       |
| 2023-08-01       | 0.0110       |
| ***2023-09-01*** | ***0.0110*** |
| ***2023-10-01*** | ***0.0111*** |
| 2023-11-01       | 0.0111       |

Make sure the output DataFrame has an updated `date` column. Set `date` as the index while retaining it as a column, using `augmented_df.set_index("date", drop=False, inplace=True)`. First add new rows for skipped first-of-month `date` values with missing `cpi` values. Then interpolate all interior missing `cpi` values.

## Function Usage Example

The following is a function invocation:

```python
augmented_df, count_added_rows, count_interpolations = fill_in_missing_months(
    chained_cpi_df
)
```

### Sample CPI Input (`chained_cpi_df`)

| date       | cpi    |
| ---------- | ------ |
| 2023-01-01 | 0.0104 |
| 2023-02-01 | 0.0105 |
| 2023-03-01 | NaN    |
| 2023-04-01 | 0.0106 |
| 2023-05-01 | 0.0106 |
| 2023-06-01 | 0.0107 |
| 2023-07-01 | 0.0109 |
| 2023-08-01 | 0.0110 |
| 2023-11-01 | 0.0111 |

### Sample Function Return DataFrame (`augmented_df`)

| date             | cpi          |
| ---------------- | ------------ |
| 2023-01-01       | 0.0104       |
| 2023-02-01       | 0.0105       |
| 2023-03-01       | **0.0105**   |
| 2023-04-01       | 0.0106       |
| 2023-05-01       | 0.0106       |
| 2023-06-01       | 0.0107       |
| 2023-07-01       | 0.0109       |
| 2023-08-01       | 0.0110       |
| ***2023-09-01*** | ***0.0110*** |
| ***2023-10-01*** | ***0.0111*** |
| 2023-11-01       | 0.0111       |

The `count_added_rows` is 2 and the `count_interpolations` is 3.

The 2023-03-01 value is interpolated between 2023-02-01 and 2023-04-01 using actual timestamp distance and rounded to four decimal places. Dates 2023-09-01 and 2023-10-01 are taken from the example about inserting rows for gaps in monthly dates.

## Algorithm

1. Validate that the input is a non-empty DataFrame with exactly these columns, in this order:
   1. `date`, `cpi`
1. Verify that the DataFrame has more than eight rows.
1. Require exact datatypes:
   1. `date`: `datetime64[ns]`
   1. `cpi`: `float64`
1. Validate that every timestamp is normalized to midnight and is the first day of its month.
1. Validate unique, strictly increasing dates and finite `cpi` values that are optionally missing but otherwise greater than `-1.0` and less than `2.0`.
1. Reject missing first or last `cpi` values.
1. Work from a deep copy of the input.
1. Generate an inclusive first-of-month date range from the minimum through maximum input date using monthly-start frequency.
1. Reindex onto that range and count the newly created rows.
1. Count all missing `cpi` values at that point.
1. Perform date-distance-weighted linear interpolation only between valid bounding `cpi` values.
1. Verify that no missing `cpi` values remain.
1. Round interpolated `cpi` values to four decimal places.
1. Set `date` as the index with `drop=False` and `inplace=True`.

## Additional Generated Modules

Generate a Python module named `test_interpolate_cpi.py` that provides unit tests for the `fill_in_missing_months` function. The module must use test data like the examples above for the `date` column. Test dates must be pandas timestamps representing the first day of each month at midnight 00:00:00.

## Test Cases

Implement these test cases using pytest and express all dates as datatype `datetime64[ns]`. Do not allow any other datatype in the following tests:

- Test non-DataFrame argument (invalid).
- Empty input DataFrame (invalid).
- The input DataFrame must contain at least nine rows; otherwise, raise `ValueError`.
- Input DataFrame with nine rows (valid).
- One interior missing month.
- Two nonadjacent interior missing months.
- Multiple interior consecutive missing months.
- Input with no missing months.
- Input DataFrame remains unchanged.
- Output column order, datatypes, row order, and index.
- Invalid dates, duplicates, and out-of-range `cpi` values.
- Dates that are not the first day of the month.
- Incorrect or reordered column names and extra columns.
- Wrong DataFrame and column datatypes.
- `NaT` and timestamps that are not midnight.
- Unsorted and duplicate dates.
- Values equal to or below `-1.0`, values equal to or above `2.0`, positive infinity, and negative infinity (invalid).
- Leading, trailing, and entirely missing `cpi` values.
- Correct counts when no interpolation is needed.
- Correct count semantics for inserted rows plus pre-existing `NaN` values.
- Numerical rounding without requiring trailing-zero display formatting.
- Preservation of the original DataFrame, including its index and values.
- Exact output `DatetimeIndex`, index name, column order, datatypes, and ascending row order.

## Implementation Notes

Generate code compatible with Python 3.13 or later. All generated modules must provide module-level docstrings, and generated functions must have docstrings. The module-level docstrings must describe the monthly CPI interpolation process.

Every date or date range created by either the implementation or unit tests must explicitly use `datetime64[ns]` resolution. Do not rely on pandas datatype inference from string literals. Apply `.as_unit("ns")` to results from `pd.date_range()` and `pd.to_datetime()`, or construct a Series with `dtype="datetime64[ns]"`. This requirement applies to input data, intermediate values, expected test values, DataFrame columns, and expected `DatetimeIndex` objects. Unit tests must assert that these objects have exact datatype `datetime64[ns]`.

Use `pd.date_range(start=min_date, end=max_date, freq="MS").as_unit("ns")` or an equivalent explicit construction to generate every first-of-month date between the minimum and maximum input dates.
