# Pandas Interpolation Index

## Question

> Python pandas DataFrame has an index. Suppose the index should also relate to one of the data columns. When doing linear interpolation, what are the issues, and how should the index be derived from the data column?

The key question is: should “linear” mean linear across row positions, or linear across the actual values of a data column?

In pandas, `method="linear"` treats rows as equally spaced and ignores the index values. If spacing in the related column is irregular, this can produce incorrect results. To interpolate according to that column, convert it into a suitable index and use `method="index"` for numeric coordinates or `method="time"` for timestamps. See the [pandas interpolation documentation](https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.interpolate.html).

## Why the index matters

Suppose:

```python
import pandas as pd
import numpy as np

df = pd.DataFrame({
    "distance": [0.0, 1.0, 10.0],
    "value": [0.0, np.nan, 10.0],
})
```

Using ordinary linear interpolation:

```python
df["value"].interpolate(method="linear")
```

This produces `5.0` at the middle row because pandas treats the three rows as equally spaced:

```text
row positions: 0, 1, 2
```

But the actual coordinate is `distance=1`, between `0` and `10`. Coordinate-based interpolation should produce:

$$
0 + (10 - 0)\frac{1 - 0}{10 - 0} = 1
$$

Use the data column as the index:

```python
result = (
    df.set_index("distance")
      .sort_index()
)

result["value"] = result["value"].interpolate(
    method="index",
    limit_area="inside",
)
```

Result:

```text
          value
distance
0.0         0.0
1.0         1.0
10.0       10.0
```

## Recommended index derivation

The index should be derived from the column that represents the independent coordinate:

- Time series: timestamp or date
- Spatial data: distance, elevation, latitude, or another coordinate
- Experimental data: elapsed time, temperature, pressure, or sample position
- Financial data: trading date or elapsed time—not an arbitrary row number

For a numeric coordinate:

```python
df["coordinate"] = pd.to_numeric(df["coordinate"], errors="raise")

if df["coordinate"].isna().any():
    raise ValueError("The interpolation coordinate contains missing values.")

if df["coordinate"].duplicated().any():
    raise ValueError("The interpolation coordinate contains duplicates.")

df = df.set_index("coordinate").sort_index()

df["measurement"] = df["measurement"].interpolate(
    method="index",
    limit_area="inside",
)
```

If the coordinate must remain visible as both an index and a column:

```python
df = df.set_index("coordinate", drop=False).sort_index()
```

Be careful to interpolate only the measurement columns, not the duplicated coordinate column.

## Datetime example

```python
df["date"] = pd.to_datetime(df["date"], errors="raise")

df = df.set_index("date").sort_index()

df["measurement"] = df["measurement"].interpolate(
    method="time",
    limit_area="inside",
)
```

`method="time"` accounts for the actual elapsed time between observations. Thus, a missing value one day after the previous observation is not treated as halfway between observations that are ten days apart.

## Important issues to check

### 1. Irregular spacing

`method="linear"` ignores coordinate spacing. Use `"index"` or `"time"` when spacing matters.

### 2. Unsorted index

Sort by the coordinate before interpolation:

```python
df = df.sort_index()
```

### 3. Duplicate coordinates

Duplicate index values make the relationship ambiguous. Decide whether duplicates should be rejected or aggregated:

```python
df = df.groupby(level=0).mean()
```

Aggregation is appropriate only when multiple observations at the same coordinate may legitimately be combined.

### 4. Missing coordinate values

The independent variable cannot normally be missing because pandas would not know where the observation belongs. Drop, repair, or reject those rows before interpolation.

### 5. Missing rows are not created automatically

`interpolate()` fills existing `NaN` cells; it does not create absent timestamps or coordinates. Create the desired grid first:

```python
desired_index = pd.Index(
    [0.0, 1.0, 2.0, 5.0, 10.0],
    name="distance",
)

df = (
    df.set_index("distance")
      .reindex(desired_index)
      .interpolate(method="index", limit_area="inside")
)
```

Reindexing introduces `NaN` values at new coordinates, which can then be interpolated. See the [pandas reindex documentation](https://pandas.pydata.org/docs/reference/api/pandas.DataFrame.reindex.html).

### 6. Extrapolation at the boundaries

Leading or trailing missing values are outside the observed range. To restrict interpolation to gaps surrounded by known values, use:

```python
limit_area="inside"
```

### 7. Circular derivation

Do not derive the index from the same values you are trying to interpolate. The index should be an independently known coordinate. For example, interpolate temperature by timestamp—not temperature by an index derived from temperature.

### 8. MultiIndex limitations

Pandas supports only `method="linear"` directly on a `MultiIndex`, and that method ignores index distances. For entity/time data, interpolate each entity separately using its time or numeric coordinate.

## Summary

Derive a unique, nonmissing, ordered index from the independent data column, and then use `method="index"` or `method="time"`. Use ordinary `method="linear"` only when every row truly represents an equal interval.
