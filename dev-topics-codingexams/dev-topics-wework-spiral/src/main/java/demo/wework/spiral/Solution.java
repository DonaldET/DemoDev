package demo.wework.spiral;

/**
 * Problem: Given a 2D array, print it in spiral form. See the following
 * examples.
 *
 * <pre>
 * <code>
 * Input 1: 1    2   3   4
 *          5    6   7   8
 *          9   10  11  12
 *         13   14  15  16
 *
 * Output: 1 2 3 4 8 12 16 15 14 13 9 5 6 7 11 10
 *
 * Input 2: 1   2   3   4   5   6
 *          7   8   9  10  11  12
 *         13  14  15  16  17  18
 *
 * Output: 1 2 3 4 5 6 12 18 17 16 15 14 13 7 8 9 10 11
 * </code>
 * </pre>
 */
public class Solution {

	private static void printTopRowLR(int[][] x, int n, int m, int a, int b) {
		for (int i = 0; i < m; i++) {
			System.out.print(" " + x[a][i + b]);
		}
	}

	private static void printRgtColDown(int[][] x, int n, int m, int a, int b) {
		for (int i = 1; i < n; i++) {
			System.out.print(" " + x[i + a][b + m - 1]);
		}
	}

	private static void printBottomRowRL(int[][] x, int n, int m, int a, int b) {
		for (int i = 1; i < m; i++) {
			System.out.print(" " + x[a + n - 1][(m - 1) - i + b]);
		}
	}

	private static void printLftColUp(int[][] x, int n, int m, int a, int b) {
		for (int i = 1; i < (n - 1); i++) {
			System.out.print(" " + x[(n - 1) - i + a][b]);
		}
	}

	private static void printBand(int[][] x, int n, int m, int a, int b) {
		printTopRowLR(x, n, m, a, b);
		if (n < 2) {
			return;
		}

		printRgtColDown(x, n, m, a, b);
		printBottomRowRL(x, n, m, a, b);

		if (m > 1) {
			printLftColUp(x, n, m, a, b);
		}
	}

	private static void printBands(int[][] x, int n, int m, int a, int b) {
		if (n < 1) {
			if (m < 1) {
				System.out.print("[empty]");
				return;
			}
			throw new IllegalArgumentException("no rows");
		} else if (m < 1) {
			throw new IllegalArgumentException("no cols");
		}

		int bands2Print = (n + 1) / 2;
		for (int i = 0; i < bands2Print; i++) {
			printBand(x, n - 2 * i, m - 2 * i, i, i);
		}
	}

	public static void printSpiral(String label, int[][] x) {
		int n = x.length;
		int m = n > 0 ? x[0].length : 0;
		System.out.println("\n----- (" + n + ", " + m + ")  " + label);
		printBands(x, n, m, 0, 0);
	}

	public static void main(String[] args) {
		int emptyArray[][] = new int[0][0];
		printSpiral("empty array:", emptyArray);

		int singleEntryArray[][] = { { 1 } };
		printSpiral("single entry:", singleEntryArray);

		int singleColArray[][] = { { 1 }, { 2 } };
		printSpiral("single col:", singleColArray);

		int singleRowArray[][] = { { 1, 2 } };
		printSpiral("single row:", singleRowArray);

		int square3by3[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		// 1 2 3 6 9 8 7 4 5
		printSpiral("Square Array:", square3by3);

		int input1[][] = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 16 } };
		// 1 2 3 4 8 12 16 15 14 13 9 5 6 7 11 10
		printSpiral("Output 1:", input1);

		int input2[][] = { { 1, 2, 3, 4, 5, 6 }, { 7, 8, 9, 10, 11, 12 }, { 13, 14, 15, 16, 17, 18 } };
		// 1 2 3 4 5 6 12 18 17 16 15 14 13 7 8 9 10 11
		printSpiral("Output 2:", input2);

		int input3[][] = { { 1, 2, 3, 4, 5, 6 }, { 7, 8, 9, 10, 11, 12 }, { 13, 14, 15, 16, 17, 18 },
				{ 19, 20, 21, 22, 23, 24 }, { 25, 26, 27, 28, 29, 30 }, { 31, 32, 33, 34, 35, 36 },
				{ 37, 38, 39, 40, 41, 42 } };
		// 1 2 3 4 5 6 - 12 18 24 30 36 - 42 41 40 39 38 - 37 31 25 19 13 7 - 8 9 10 11
		// - 17 23 29 - 35 34 33 - 32 26 20 14 - 15 16 - 22 - 28 27 - 21
		printSpiral("Output 3:", input3);
	}
}
