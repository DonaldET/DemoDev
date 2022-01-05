package demo.don.ijsde.problems;

import java.util.Arrays;

/**
 * Problem: Given a history of stock prices (a time series), compute the maximum
 * potential profit based on buying and selling at optimal time.
 * <p>
 * Solution: Compute all possible buy/sell combinations and select the largest.
 * <p>
 * Example: the price history (6L, 0L, -1L, 10L) yields a maximum profit of 11.
 * <p>
 * Solution: Examine all possible pairs of buy-sell and record the maximum. With
 * a left-to-right scan, the last found profit value wins in the case of a tie.
 */
public class SolutionStocks {

	public static long solution(long[] prices) {
		if (prices.length < 2) {
			return 0;
		}

		long profit = Long.MIN_VALUE;

		for (int i = 0; i < prices.length - 1; i++) {
			long buyVal = prices[i];
			for (int j = i + 1; j < prices.length; j++) {
				long sellVal = prices[j];
				long sale = sellVal - buyVal;
				if (sale > profit) {
					profit = sale;
				}
			}
		}

		return profit;
	}

	public static void main(String[] args) {

		long[] pr0 = {};
		long profit = solution(pr0);
		System.out.println("History " + Arrays.toString(pr0) + ", profit is " + profit);

		long[] pr0A = { 22L };
		profit = solution(pr0A);
		System.out.println("History " + Arrays.toString(pr0A) + ", profit is " + profit);

		long[] pr1 = { 6L, 0L, -1L, 10L };
		profit = solution(pr1);
		System.out.println("History " + Arrays.toString(pr1) + ", profit is " + profit);

		long[] pr2 = { 13L, 7L, 5L, 4L };
		profit = solution(pr2);
		System.out.println("History " + Arrays.toString(pr2) + ", profit is " + profit);
	}
}
