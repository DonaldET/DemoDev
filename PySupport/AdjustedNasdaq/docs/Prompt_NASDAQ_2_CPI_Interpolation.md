# Convert NASDAQ Interpolator to CPI Interpolator

You are an LLM prompt creator who will analyze the uploaded prompt file Prompt_NASDAQ_Interpolation.md and from it create the prompt file Prompt_CPI_Interpolation.md that modifies the interpolation process for the NASDAQCOM to fill in missing CPI data using the same linear interpolation technique. Do not modify the input prompt and all file names must be shown with out directory paths.

## Old Data Source

The source for the raw NASDAQ Composite data in prompt Prompt_NASDAQ_Interpolation is FED provided [NASDAQ Composite Index Values](https://fred.stlouisfed.org/series/NASDAQCOM), and we will be using a date range approximately 1/3/2023 through 7/2/2026 as an example.

## New Source data to Transform

The output file Prompt_CPI_Interpolation.md uses the recommended Chained Consumer Price Index series for adjusting a broad stock index like the NASDAQ Composite; that is the [SUUR0000SA0 series from the St. Louis Fed FRED](https://fred.stlouisfed.org/series/SUUR0000SA0), which tracks the *Chained Consumer Price Index for All Urban Consumers (C-CPI-U): All Items in U.S. City Average, Not Seasonally Adjusted*. \[[1](https://fred.stlouisfed.org/series/SUUR0000SA0), [2](https://fred.stlouisfed.org/tags/series?t=chained%3Bcpi)\]

**Why Use the SUUR0000SA0 Series?**

- **Account for Substitution Bias:** Unlike standard CPI, Chained CPI updates its consumer spending baskets monthly. This reflects real-world substitution (e.g., buying apples when pears get too expensive). It acts as a highly realistic measure of actual consumer purchasing power over multi-year periods. \[[1](https://www.bls.gov/cpi/additional-resources/chained-cpi-questions-and-answers.htm), [2](https://www.intechopen.com/online-first/1252099)\]

- **Matches Stock Price Nature:** You should always pair stock data with **Not Seasonally Adjusted (NSA)** inflation figures. Stock prices naturally absorb raw, unadjusted seasonal economic factors in real time. Using a seasonally adjusted series would inject artificial distortions into your market

## Process Differences between NASDAQCOM interpolation and CPI interpolation

Much of the NASDAQCOM interpolation prompt transformation will be a substitution of names. For example:

1)  The variable nasdaq_composite_df becomes chained_cpi_df.

2)  The generated python function is named **fill_in_missing_months**, and will be created in a module named **interpolate_cpi.py.**

3)  The generated **fill_in_missing_months** function accepts a non-empty pandas DataFrame parameter with two columns: **date** and **cpi**.

    1.  The **date** column must be the first of the month (format yyyy-mm-01).

    2.  The time element of the **date** column must be midnight (00:00:00).

    3.  The **cpi** is a floating-point value greater than 100 and less than 1000.

4)  In general, references to **observation_date** become **date** and references to **NASDAQCOM** become **cpi** respectively.

5)  The function in the source prompt called **fill_in_missing_days** becomes the template for generated function **fill_in_missing_months**.

6)  All generated dates are the first of the month at midnight (00:00:00).

7)  Reuse the DataFrame and examples from Prompt_NASDAQ_Interpolation.md, but:

    1.  Change the column names to match the substitutions from \#3 above.

    2.  Modify the example and example calculations as follows:

        1.  Take the **observation_date** values from the Prompt_NASDAQ_Interpolation examples but have the example dates start with 2023-01-01 and increase by one month in each row.

        2.  Take the numeric **NASDAQCOM** values from the Prompt_NASDAQ_Interpolation examples and divide them by 34.719 and round them to two decimal places.

        3.  Use actual elapsed-day interpolation, not equal monthly-step interpolation.

8)  Add tests for dates that are not the first of the month, and call the generated test module **test_interpolate_cpi.py**.

## Output Prompt File

Generate the output prompt file Prompt_CPI_Interpolation.md as a mdformat compatible markdown file ready to download.
