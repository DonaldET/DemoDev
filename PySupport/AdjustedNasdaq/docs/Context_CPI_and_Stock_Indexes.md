# CPI and Stock Indexes

The Consumer Price Index (CPI) measures how the prices paid by consumers for a representative basket of goods and services change over time. It is commonly used as an indicator of inflation.

For example, if CPI rises from 300 to 309 over one year, the measured inflation rate is:

$$
\text{Inflation rate} = \frac{309 - 300}{300} = 3\%
$$

CPI is not normally applied directly to the calculation of a stock-market index such as the S&P 500. Stock indexes are generally calculated from the market prices of their constituent stocks. CPI is instead used to interpret the index in inflation-adjusted, or “real,” terms.

## Adjusting a stock index for inflation

To express an index’s historical values in the purchasing power of a selected base date:

$$
\text{Real index}_t = \text{Nominal index}_t \times \frac{\text{CPI}_{\text{base}}}{\text{CPI}_t}
$$

Suppose:

- The stock index was 4,000 when CPI was 250.
- The stock index is now 5,200 when CPI is 325.

Expressing the current index in the earlier period’s purchasing power:

$$
5{,}200 \times \frac{250}{325} = 4{,}000
$$

Although the nominal index increased by 30%, its inflation-adjusted value did not increase. The entire nominal gain was offset by the rise in consumer prices.

## Calculating a real stock return

A more precise inflation adjustment for investment returns is:

$$
1 + r_{\text{real}} = \frac{1 + r_{\text{nominal}}}{1 + \pi}
$$

where:

- $r_{\text{nominal}}$ is the stock-index return.
- $\pi$ is the CPI inflation rate.
- $r_{\text{real}}$ is the return after inflation.

If an index returns 10% while CPI inflation is 4%:

$$
r_{\text{real}} = \frac{1.10}{1.04} - 1 \approx 5.77\%
$$

Simply subtracting inflation gives 6%, which is a useful approximation, but 5.77% is the compounded result.

For a total-return stock index, dividends are included before the CPI adjustment. A price index excludes dividends, so its inflation-adjusted performance does not represent the investor’s complete return.

CPI also affects stock indexes indirectly. Higher inflation can influence interest rates, corporate costs, consumer demand, expected profits, and the discount rates investors use to value companies. Consequently, CPI announcements may change stock prices even though CPI is not itself part of the stock index formula.
