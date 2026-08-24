# Transformation-Coverage Review: NASDAQ-to-CPI Interpolation Prompt

## Review Scope

- **Template prompt:** `Prompt_NASDAQ_Interpolation.md`
- **Transformer prompt:** `Prompt_NASDAQ_2_CPI_Interpolation.md`
- **Future target prompt:** `Prompt_CPI_Interpolation.md` (not reviewed and not generated)

This review evaluates the transformer prompt for language quality and technical adequacy, using the template prompt as the baseline.

## Executive Summary

### Goal

The transformer instructs an LLM to derive a CPI interpolation software-specification prompt from an existing NASDAQ interpolation software-specification prompt. The resulting target prompt is expected to specify Python 3.13+/pandas 3+ code that fills missing monthly Chained CPI observations and generates pytest coverage.

### Relevant context

The source series changes from daily NASDAQ Composite values (`NASDAQCOM`, `observation_date`) to monthly Chained CPI values (`cpi`, `date`) for FRED series `SUUR0000SA0`. The intended output preserves most validation, immutability, counting, precision, schema, indexing, documentation, and testing requirements while changing names, cadence, value bounds, examples, and date rules.

### Expected target-prompt content and deliverables

The transformer expressly calls for:

1. `Prompt_CPI_Interpolation.md`, formatted compatibly with `mdformat`.
2. A specification for `fill_in_missing_months(chained_cpi_df)` in `interpolate_cpi.py`.
3. A two-column `date`/`cpi` DataFrame contract.
4. Monthly first-of-month dates at midnight and CPI values strictly between 100 and 1000.
5. Missing-month generation and elapsed-day-weighted linear interpolation.
6. Transformed examples based on the template values divided by 34.719 and rounded to two decimals.
7. A generated pytest module named `test_interpolate_cpi.py`, including invalid non-first-of-month dates.

## Overall Readiness

**Ready with revisions.**

No blocking contradiction makes transformation impossible, but several major omissions and ambiguities could materially change the generated CPI specification. Most importantly, the transformer does not define the monthly reindexing rule, does not reconcile elapsed-day interpolation with the template's positional `method="linear"`, and does not explicitly dispose of daily-specific test cases and terminology. These issues should be corrected before the transformer is used.

## Findings

| ID | Source | Category | Severity | Supporting text | Explanation | Recommended correction | Inference |
| --- | --- | --- | --- | --- | --- | --- | --- |
| F-01 | Cross-file comparison | Technical adequacy — interpolation semantics | Major | Transformer: “Use actual elapsed-day interpolation, not equal monthly-step interpolation.” Template: “`Series.interpolate(method="linear")`.” | After monthly reindexing, pandas `method="linear"` is positional and gives equal weight to adjacent rows. It does not account for differing month lengths. The retained implementation example conflicts with the intended elapsed-day weighting unless it is replaced. | State that the target must use a `DatetimeIndex` and time-based interpolation (for example, `Series.interpolate(method="time")`) or an explicit timestamp-distance formula. Explicitly prohibit positional `method="linear"` for CPI. | No |
| F-02 | Transformer | Completeness — missing-month generation | Major | “All generated dates are the first of the month...” | The transformer never defines the inclusive monthly sequence or the pandas frequency. A generator could use month-end dates, preserve irregular dates, or infer a cadence inconsistently. | Require an inclusive month-start range from minimum through maximum input date, explicitly using `freq="MS"`, with `datetime64[ns]` resolution. | No |
| F-03 | Cross-file comparison | Ambiguity — example transformation | Major | “Take the `observation_date` values ... but have the example dates start with 2023-01-01 and increase by one month in each row.” | The template contains several examples with deliberately absent dates and one pre-existing `NaN`. “Increase by one month in each row” can erase the gaps those examples are meant to demonstrate. It also does not specify how January 14–17 gap examples map to monthly dates. | Provide explicit transformed example tables or a deterministic mapping rule that preserves the same missing-value and missing-row patterns. Identify which months are omitted and which CPI cell is `NaN`. | No |
| F-04 | Transformer | Completeness — output contract | Major | The transformer lists function and module names, input columns, and general substitutions. | It does not explicitly restate the returned tuple names/meanings. The template’s `count_of_added_rows` is daily-specific, while the target logically needs `count_of_added_months` or an expressly retained generic name. | Define the exact signature and returned tuple, including the names and semantics of both counts after monthly reindexing. | No |
| F-05 | Cross-file comparison | Completeness — daily test conversion | Major | Template tests include “missing days,” consecutive absent calendar dates, and weekend/holiday generation. Transformer only adds tests for dates not on the first of a month. | A broad “in general” substitution does not reliably tell the target generator how every daily test becomes a monthly test. Weekend/holiday concepts are inapplicable to monthly CPI. | Require every missing-day test to become its missing-month equivalent; explicitly remove weekend/holiday semantics; preserve single, nonadjacent, consecutive, and mixed pre-existing-`NaN` plus absent-month coverage. | No |
| F-06 | Transformer | Ambiguity — minimum row requirement | Major | Template requires at least nine rows “for later processing”; transformer does not mention whether this changes. | The requirement may be preserved by implication, but its daily forecasting rationale may not apply to monthly CPI. Silent preservation or omission would materially alter validation. | State explicitly whether the minimum remains nine rows. If preserved, explain that it applies to monthly observations; otherwise specify the new threshold and rationale. | No |
| F-07 | Cross-file comparison | Ambiguity — rounding rules | Major | Transformer: source example values are “divide[d] ... by 34.719 and round[ed] ... to two decimal places.” Template: interpolate missing positions and round only those values to four decimals while preserving valid input precision. | The two-decimal instruction appears to govern example-source conversion, not runtime output precision, but the scope is not explicit. A generator may round all CPI input/output values to two decimals or replace the four-decimal interpolation rule. | Separate example-display precision from runtime precision. State whether interpolated CPI values remain rounded to four decimals and reaffirm that valid input values are never rounded. | No |
| F-08 | Transformer | Technical adequacy — transformed example arithmetic | Major | “Take the numeric `NASDAQCOM` values ... divide them by 34.719 and round them to two decimal places” and “Use actual elapsed-day interpolation.” | If rounded CPI endpoints are interpolated, results can differ from results obtained by interpolating full-precision divided values and rounding later. The order of division, endpoint rounding, interpolation, and result rounding is unspecified. | Define the exact order of operations for examples and require displayed calculations to match that order. Prefer: convert displayed endpoints to two decimals, then compute time-weighted example values, then apply the target’s interpolation rounding rule. | No |
| F-09 | Transformer | Unsupported addition — source rationale | Minor | “You should always pair stock data with Not Seasonally Adjusted (NSA) inflation figures.” | This absolute methodological assertion is not needed to transform the software prompt and is not a requirement in the template. It may distract the generator or become an unintended normative software requirement. | Rephrase as contextual motivation specific to this use case, or remove it from the transformation instructions. | No |
| F-10 | Transformer | Language quality — incomplete sentence | Editorial | “Using a seasonally adjusted series would inject artificial distortions into your market” | The sentence is incomplete. | Complete the sentence or delete it. | No |
| F-11 | Transformer | Clarity — filenames | Minor | “uploaded prompt file `Prompt_NASDAQ_Interpolation.md`” and “all file names must be shown with out directory paths.” | “with out” is a typo, and the actual attachment filenames are versioned. The logical names are nevertheless declared by the reviewer. | Write “without directory paths” and clarify that logical filenames in the target are unversioned regardless of uploaded attachment names. | No |
| F-12 | Transformer | Completeness — substitutions | Minor | “In general, references to `observation_date` become `date` and references to `NASDAQCOM` become `cpi`.” | “In general” is weaker than a complete substitution rule and may leave old identifiers in error messages, headings, examples, or test names. | Require exhaustive terminology replacement except where the target explains its derivation from the template. Include parameter names, count labels, errors, headings, examples, and tests. | No |
| F-13 | Cross-file comparison | Completeness — exact dtype rules | Major | Template requires exact `datetime64[ns]` and `float64`, including explicit `.as_unit("ns")` use. Transformer specifies first-of-month/midnight and says `cpi` is “a floating-point value,” but does not explicitly restate exact dtypes. | The general substitution could preserve exact dtypes, but the transformer’s new wording could also weaken `float64` into any floating dtype. | Explicitly preserve `date == datetime64[ns]` and `cpi == float64`, including exact dtype assertions and explicit nanosecond construction throughout implementation/tests. | No |
| F-14 | Cross-file comparison | Completeness — validation and exceptions | Major | Template distinguishes `TypeError` for non-DataFrame/wrong dtype and `ValueError` for all other validation failures. Transformer does not mention this exception mapping. | A target generator may omit or change the exception taxonomy. | Explicitly preserve the exception mapping and require field/rule fragments in error-message assertions. | No |
| F-15 | Cross-file comparison | Completeness — immutability and value preservation | Major | Template requires a deep copy, no input mutation, exact preservation of valid original values, and rounding only interpolated positions. Transformer does not mention these directly. | These are core behavioral guarantees and are too important to leave solely to “much ... will be a substitution of names.” | Enumerate these guarantees as preserved requirements. | No |
| F-16 | Cross-file comparison | Ambiguity — date range context | Minor | Template gives a typical 2023-01-01–2026-07-31 range; transformer’s source context cites approximately 1/3/2023–7/2/2026. | These are NASDAQ-specific daily ranges and do not state the representative CPI monthly range. | Replace with an explicit illustrative monthly CPI range, or state that no fixed date-range validation is intended. | No |
| F-17 | Transformer | Conciseness and relevance | Minor | Two sections explain data-source choice before the actual transformation rules. | The rationale is longer than needed and includes claims irrelevant to code-generation behavior. It dilutes the operational requirements. | Condense the source rationale to one paragraph and move precise transformation rules ahead of background. | No |
| F-18 | Template | Template defect — malformed sentence | Editorial | “... generated weekend and holiday values that are  strictly increasing`observation_date` are synthetic ...” | This sentence is grammatically corrupt and combines datatype, ordering, and synthetic-value disclaimers. The transformer should not reproduce it mechanically. | In the target, replace it with separate clear statements for date semantics, ascending uniqueness, and the analytical/synthetic status of generated CPI estimates. | No |
| F-19 | Template | Template defect — undefined range | Minor | Test “out of range NASDAQ values”; earlier rule only requires positive, finite values. | “Out of range” has no defined upper limit in the template. The CPI transformer does add explicit bounds, so CPI tests can be well-defined, but the source defect should not be copied. | For CPI, define boundary-invalid values precisely as `<= 100` and `>= 1000`, plus infinities and `NaN` handling. | No |
| F-20 | Cross-file comparison | Ambiguity — strict CPI bounds | Minor | “greater than 100 and less than 1000.” | The bounds are logically strict, but boundary test expectations are not expressly stated. | Require tests for exactly 100.0 and 1000.0 as invalid, plus just-inside valid values. | No |
| F-21 | Cross-file comparison | Completeness — index contract | Minor | Template requires retaining `observation_date` as a column and also setting it as the index. Transformer does not explicitly mention the corresponding CPI index behavior. | General substitution may preserve it, but this is an exact schema invariant worth stating. | Require `augmented_df.set_index("date", drop=False, inplace=True)` and assertions for `DatetimeIndex`, name `date`, order, and dtype. | No |
| F-22 | Cross-file comparison | Completeness — dependency/docstring constraints | Minor | Template requires Python 3.13+, pandas 3+, module-level docstrings, and function docstrings. Transformer does not mention them. | These are easy to omit during transformation. | List them explicitly as preserved. | No |

## Structured Transformation-Coverage Report

The classification below treats an implicit global substitution as **Ambiguous** when the transformer does not clearly state whether a consequential template rule survives.

| # | Template requirement | Classification | Transformer evidence / assessment |
| --- | --- | --- | --- |
| 1 | Generate `interpolate_nasdaq.py` | Intentionally Changed | Changed to `interpolate_cpi.py`. |
| 2 | Function `fill_in_missing_days` | Intentionally Changed | Changed to `fill_in_missing_months`. |
| 3 | Parameter `nasdaq_composite_df` | Intentionally Changed | Changed to `chained_cpi_df`. |
| 4 | Exactly two columns in order | Preserved | Substitution specifies `date` and `cpi`; exactness/order is inherited but should be restated. |
| 5 | `observation_date` name | Intentionally Changed | Replaced with `date`. |
| 6 | `NASDAQCOM` name | Intentionally Changed | Replaced with `cpi`. |
| 7 | Non-empty pandas DataFrame | Preserved | Expressly says non-empty DataFrame. |
| 8 | At least nine input rows | Ambiguous | Not explicitly changed or preserved. |
| 9 | Exact `datetime64[ns]` date dtype | Ambiguous | First-of-month/midnight stated; exact dtype is only implied by reuse. |
| 10 | Exact `float64` value dtype | Ambiguous | “floating-point value” may weaken exact `float64`. |
| 11 | Date values normalized to midnight | Preserved | Explicitly preserved. |
| 12 | Date values at the required cadence anchor | Intentionally Changed | Any normalized day becomes first day of month. |
| 13 | Unique, strictly increasing dates | Ambiguous | Not directly addressed. |
| 14 | No `NaT` dates | Ambiguous | Not directly addressed. |
| 15 | Values may be `NaN`; otherwise finite and valid | Ambiguous | New numeric bounds are explicit; `NaN`/finite rules are not restated. |
| 16 | Values strictly positive, no maximum | Intentionally Changed | CPI values must be `>100` and `<1000`. |
| 17 | Value column not entirely missing | Ambiguous | Not directly addressed. |
| 18 | First and last values may not be missing | Ambiguous | Not directly addressed. |
| 19 | Input is not modified; use deep copy | Ambiguous | Not directly addressed. |
| 20 | Preserve every valid original value exactly | Ambiguous | Not directly addressed. |
| 21 | Generate dates only between input minimum/maximum | Ambiguous | Monthly boundaries are not defined. |
| 22 | Inclusive daily date range | Intentionally Changed | Intended to become monthly, but `freq="MS"` is not specified. |
| 23 | Reindex before interpolation | Ambiguous | “same linear interpolation technique” implies preservation but does not state the ordering. |
| 24 | Count rows added by reindexing | Ambiguous | Returned count names/semantics are not stated. |
| 25 | Count all missing values after reindexing, before interpolation | Ambiguous | Not directly addressed. |
| 26 | Use one saved mask for counting and selective rounding | Ambiguous | Not directly addressed. |
| 27 | Interpolate only interior missing values | Ambiguous | Not directly addressed. |
| 28 | No extrapolation | Ambiguous | Not directly addressed. |
| 29 | Date-distance-weighted interpolation | Intentionally Changed | Explicitly requires actual elapsed-day weighting for monthly observations. |
| 30 | Example implementation `method="linear"` | Intentionally Excluded | Must be replaced because it is positional after monthly reindexing; exclusion is implied, not explicit. |
| 31 | Reject any missing value remaining after interpolation | Ambiguous | Not directly addressed. |
| 32 | Round only interpolated values to four decimals | Ambiguous | Transformer’s two-decimal example conversion creates unclear precision scope. |
| 33 | Preserve full precision of valid input values | Ambiguous | Not directly addressed. |
| 34 | Return same columns/order/dtypes | Ambiguous | Column substitutions are stated; exact output invariants are not. |
| 35 | Set date as index while retaining date column | Ambiguous | Not directly addressed. |
| 36 | `TypeError` for non-DataFrame and wrong dtypes | Ambiguous | Not directly addressed. |
| 37 | `ValueError` for all other validation failures | Ambiguous | Not directly addressed. |
| 38 | Descriptive error-message fragments | Ambiguous | Not directly addressed. |
| 39 | Generate pytest module | Intentionally Changed | Renamed to `test_interpolate_cpi.py`. |
| 40 | Nominal no-missing-data test | Preserved | Implied by reuse; should be expressly listed. |
| 41 | Interior single/nonadjacent/consecutive `NaN` tests | Preserved | Implied by reuse; terminology substitution needed. |
| 42 | First/last missing-value tests | Preserved | Implied by reuse; should be expressly listed. |
| 43 | Single/nonadjacent/consecutive missing-day tests | Intentionally Changed | Should become missing-month tests, but transformer does not say so explicitly. |
| 44 | Weekend/holiday generation semantics | Intentionally Excluded | Inapplicable to monthly CPI; exclusion is inferred, not stated. |
| 45 | Tests for `NaT`, non-midnight, unsorted, duplicate dates | Preserved | Implied; transformer adds non-first-of-month cases. |
| 46 | Tests for wrong columns/order/extra columns/dtypes | Preserved | Implied by reuse; exact CPI dtypes need clarification. |
| 47 | Tests for zero/negative/infinite values | Intentionally Changed | CPI domain is strict `(100,1000)`; infinities remain invalid by implication. |
| 48 | Input immutability test | Preserved | Implied but not restated. |
| 49 | Count-semantics tests | Ambiguous | Monthly count names and timing are unspecified. |
| 50 | Exact output index/schema/dtype/order tests | Preserved | Implied but not restated. |
| 51 | Python 3.13+ compatibility | Ambiguous | Not directly addressed. |
| 52 | pandas 3+ requirement | Ambiguous | Not directly addressed. |
| 53 | Module and function docstrings | Ambiguous | Not directly addressed. |
| 54 | Explicit nanosecond construction for all dates/indexes | Ambiguous | Not directly addressed. |
| 55 | Transform example dates and numeric values | Intentionally Changed | New start, monthly progression, division factor, and example rounding specified, but gap mapping/order of arithmetic remain ambiguous. |

## Unsupported Additions

| Addition | Source | Assessment | Recommendation |
| --- | --- | --- | --- |
| FRED SUUR0000SA0 source and economic rationale | Transformer | Useful context, but not required by the template. | Retain a concise, accurate source statement if provenance belongs in the target prompt. |
| Absolute recommendation that stock data should “always” use NSA inflation | Transformer | Overbroad and not a software requirement. | Qualify as a use-case choice or remove. |
| CPI value domain `(100, 1000)` | Transformer | Intentional domain-specific validation addition. | Retain and add explicit boundary tests. |
| Divide NASDAQ example values by `34.719` | Transformer | Intentional example-conversion mechanism, not derived from the template. | Retain only with a clear statement that this is for synthetic examples, not a real NASDAQ-to-CPI conversion formula. |
| Tests for dates not on the first of the month | Transformer | Appropriate new domain test. | Retain and specify beginning, middle, and end positions where relevant. |

## Unit-Testing Opportunities for the Future Target

### Normal behavior

- Complete monthly series with no missing rows or values; counts are both zero.
- One interior `NaN` with adjacent monthly bounds.
- One absent month, two nonadjacent absent months, and several consecutive absent months.
- A pre-existing `NaN` plus absent months inside the same pair of bounding values.
- Input spanning February in leap and non-leap years to demonstrate elapsed-day weighting.

### Boundary behavior

- Exactly nine input rows and one fewer than nine, once the minimum-row rule is confirmed.
- CPI values just above 100 and just below 1000 are valid.
- CPI values exactly 100 and exactly 1000 are invalid.
- First and last dates of a multi-year monthly range.
- Missing value in the first or last row, which must reject rather than extrapolate.

### Invalid input and exceptions

- Non-DataFrame argument (`TypeError`).
- Empty DataFrame and insufficient rows (`ValueError`).
- Wrong, reordered, or extra columns (`ValueError`).
- `date` not exact `datetime64[ns]` or `cpi` not exact `float64` (`TypeError`, if template taxonomy is preserved).
- `NaT`; duplicate; unsorted; non-midnight; or non-first-of-month dates (`ValueError`).
- CPI at/outside bounds, positive/negative infinity, and an entirely missing CPI column (`ValueError`).
- Assertions use relevant message fragments without assuming unrelated validation order.

### Precision and interpolation

- A January-to-March gap in a leap year and non-leap year, proving time weighting differs from a 50/50 positional midpoint when appropriate.
- Full-precision valid input values remain bitwise/equality-preserved.
- Only positions selected by the pre-interpolation mask are rounded.
- Confirm the specified order of endpoint conversion, interpolation, and output rounding in examples.
- Verify that no missing CPI values remain.

### Schema and transformation coverage

- Exact output column order `['date', 'cpi']` and dtypes.
- Exact `DatetimeIndex`, index name `date`, ascending order, and retained `date` column.
- Input DataFrame and its index remain unchanged.
- Correct added-month count and total interpolation count.
- Source-token sweep of the generated target prompt: no unintended `NASDAQCOM`, `observation_date`, `nasdaq_composite_df`, `fill_in_missing_days`, “daily,” “weekend,” or “holiday” terminology remains.
- Deliverable-name checks for `Prompt_CPI_Interpolation.md`, `interpolate_cpi.py`, and `test_interpolate_cpi.py`.
- Markdown formatting check with `mdformat --check`.

## Recommended Transformer Corrections (Priority Order)

1. Define monthly reindexing as an inclusive `freq="MS"` range with exact `datetime64[ns]` resolution.
2. Replace positional `method="linear"` guidance with time-aware interpolation or an explicit elapsed-day formula.
3. Specify the complete function signature and returned count names/semantics.
4. State exactly which template requirements are preserved, especially dtype, exceptions, immutability, precision, index, documentation, and version constraints.
5. Explicitly transform all daily/missing-day tests into monthly/missing-month tests and exclude weekend/holiday language.
6. Supply unambiguous monthly example tables and define the arithmetic/rounding order.
7. Confirm whether the nine-row minimum remains and clarify the example/source date range.
8. Tighten the prose, complete the truncated NSA sentence, and qualify unsupported economic claims.

