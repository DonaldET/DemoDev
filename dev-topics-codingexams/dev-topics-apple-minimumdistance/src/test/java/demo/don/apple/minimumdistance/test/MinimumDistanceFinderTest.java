package demo.don.apple.minimumdistance.test;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.apple.minimumdistance.DistanceFinder;

public class MinimumDistanceFinderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAllZero() {
		String label = "Test 1: 3 X 4 - all zero";
		int[][] mat = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
		int[][] exp = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
		runTest(label, mat, exp);
	}

	@Test
	public void test01() {
		String label = "Test 1: 3 X 4";
		int[][] mat = { { 0, 0, 0, 0 }, { 0, 1, 0, 0 }, { 1, 1, 1, 1 } };
		int[][] exp = { { 0, 0, 0, 0 }, { 0, 1, 0, 0 }, { 1, 2, 1, 1 } };
		runTest(label, mat, exp);
	}

	@Test
	public void test02() {
		String label = "Test 1: 3 X 4";
		int[][] mat = { { 0, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 0 } };
		int[][] exp = { { 0, 1, 2, 2 }, { 1, 2, 2, 1 }, { 2, 2, 1, 0 } };
		runTest(label, mat, exp);
	}

	@Test
	public void test03() {
		String label = "Test 3: 3 X 4";
		int[][] mat = { { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 0 } };
		int[][] exp = { { 5, 4, 3, 2 }, { 4, 3, 2, 1 }, { 3, 2, 1, 0 } };
		runTest(label, mat, exp);
	}

	@Test
	public void test04() {
		String label = "Test 4: 3 X 4 - all one";
		int[][] mat = { { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 } };
		int[][] exp = { { 2147483647, 2147483647, 2147483647, 2147483647 },
				{ 2147483647, 2147483647, 2147483647, 2147483647 },
				{ 2147483647, 2147483647, 2147483647, 2147483647 } };
		runTest(label, mat, exp);
	}

	private static void runTest(String label, int[][] mat, int[][] expected) {
		int[][] actual = DistanceFinder.findNearestZero(mat);
		boolean passed = compare2DArrays(actual, expected);
		if (!passed) {
			System.out.println("\n" + label + " failed!\n-- expected:");
			printImage(expected);
			System.out.println("-- Actual:");
			printImage(actual);
			Assert.assertTrue(label + " failed!", Arrays.equals(expected, actual));
		}
	}

	private static boolean compare2DArrays(int[][] actual, int[][] expected) {
		if (actual == null) {
			return expected == null;
		}

		int nact = actual.length;
		int mact = actual[0].length;

		int nexp = expected.length;
		int mexp = expected[0].length;

		if (nact != nexp || mact != mexp) {
			return false;
		}

		for (int r = 0; r < nact; r++) {
			for (int c = 0; c < mact; c++) {
				if (actual[r][c] != expected[r][c]) {
					return false;
				}
			}
		}

		return true;
	}

	private static void printImage(int[][] image) {
		int n = image.length;
		int m = image[0].length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				System.out.print(String.format(" %d", image[i][j]));
			}
			System.out.println();
		}
	}

}