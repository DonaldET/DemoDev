package demo.liveramp.bitsearch.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import demo.liveramp.bitsearch.BitSearcher;

/**
 * Runs the entire test suite offering optional test enablement. Also supplies
 * definitions of test cases.
 */
public class TestRunner {
	private static final boolean testComparator = true;
	private static final boolean testBinarySearch = true;
	private static final boolean testUnsortedKeyExtract = true;
	private static final boolean testSortedKeyExtract = true;
	private static final boolean testSimpleMatcher = true;
	private static final boolean testBoundedSearchMatcher = true;
	private static final boolean testIPGenerator = true;
	private static final boolean testIPPopulation = true;

	// ---------------------------------------------------------------------------------
	// ------ Collected test cases
	// -----------------------------------------------------

	private static int[][] generateTestCases() {
		final int big = Integer.MAX_VALUE;
		final int halfBig = big >> 1;
		final int quarterBig = big >> 2;

		final int small = Integer.MIN_VALUE;
		final int halfSmall = small >> 1;
		final int quarterSmall = small >> 2;

		final int[][] testCases = { { 0, 0, 0 }, { -1, -1, 0 }, { 1, 1, 0 }, { 3, 4, -1 }, { 4, 3, 1 }, { 127, 126, 1 },
				{ 126, 127, -1 }, { 256, 255, 1 }, { 255, 256, -1 }, { small, small - 1, 1 }, { small - 1, small, -1 },
				{ halfSmall, halfSmall - 1, 1 }, { halfSmall - 1, halfSmall, -1 },
				{ quarterSmall, quarterSmall - 1, 1 }, { quarterSmall - 1, quarterSmall, -1 },
				{ quarterBig, quarterBig - 1, 1 }, { quarterBig - 1, quarterBig, -1 }, { halfBig, halfBig - 1, 1 },
				{ halfBig - 1, halfBig, -1 }, { big, big - 1, 1 }, { big - 1, big, -1 }, { big, big, 0 },
				{ small, small, 0 }, { small, big, 1 }, { big, small, -1 }, { 0xFFFFFFFF, 0x7FFFFFFF, 1 },
				{ 0x7FFFFFFF, 0xFFFFFFFF, -1 } };
		return testCases;
	}

	private static List<BitSearcher.SubnetPopulationParameters> generateIPPopulationParams() {
		final List<BitSearcher.SubnetPopulationParameters> params = new ArrayList<BitSearcher.SubnetPopulationParameters>();
		params.add(new BitSearcher.SubnetPopulationParameters(0xfffffff0, 0x09000000, 16));
		params.add(new BitSearcher.SubnetPopulationParameters(0xffffff00, 0x34000000, 256));
		params.add(new BitSearcher.SubnetPopulationParameters(0xfffff000, 0x56000000, 343));

		return params;
	}

	private static Map<Integer, Integer> generateObservedIPCounts() {
		final int[][] testCases = generateTestCases();
		final int nKeys = testCases.length;

		Map<Integer, Integer> observedIPCounts = new TreeMap<Integer, Integer>();
		for (int i = 0; i < nKeys; i++) {
			observedIPCounts.put(testCases[i][0], i);
			observedIPCounts.put(testCases[i][1], i + 1000);
		}
		return observedIPCounts;
	}

	public static void displayKeys(final int mask, final int pattern, final Integer[] keys) {
		final int n = keys.length;
		System.out.println("\nDisplay " + n + " keys, their prefix, and pattern match.");
		final int wantedPrefix = mask & pattern;
		System.out.println("   Pattern: " + Integer.toHexString(pattern) + ";  Mask: " + Integer.toHexString(mask)
				+ ";  Wanted Prefix: " + Integer.toHexString(wantedPrefix) + "\n");

		int matched = 0;
		System.out.println("   Key        Prefix     Matched");
		for (int i = 0; i < n; i++) {
			final int key = keys[i];
			final int prefix = mask & key;
			System.out.print(i + ". " + String.format("%08X", key) + "   " + String.format("%08X", (prefix)));
			if ((wantedPrefix ^ prefix) == 0) {
				System.out.print("   ^^^");
				matched++;
			}
			System.out.println("");
		}
		System.out.println("------------------------- " + matched + " matches.");
	}

	public static void displayKeys(final int mask, final int pattern, final int[] keys) {
		final int n = keys.length;
		System.out.println("\nDisplay " + n + " keys from Array, their prefix, and pattern match.");
		final int wantedPrefix = mask & pattern;
		System.out.println("   Pattern: " + Integer.toHexString(pattern) + ";  Mask: " + Integer.toHexString(mask)
				+ ";  Wanted Prefix: " + Integer.toHexString(wantedPrefix) + "\n");

		int matched = 0;
		System.out.println("   Key        Prefix     Matched");
		for (int i = 0; i < n; i++) {
			final int key = keys[i];
			final int prefix = mask & key;
			System.out.print(i + ". " + String.format("%08X", key) + "   " + String.format("%08X", (prefix)));
			if ((wantedPrefix ^ prefix) == 0) {
				System.out.print("   ^^^");
				matched++;
			}
			System.out.println("");
		}
		System.out.println("------------------------- " + matched + " array matches.");
	}

	public static void main(String[] args) {
		if (testComparator) {
			TestUnsignedComparator test = new TestUnsignedComparator(generateTestCases());
			int failures = test.runAll();
			if (failures > 0) {
				throw new IllegalStateException(failures + " failed tests!");
			}
		}

		if (testBinarySearch) {
			TestUnsignedBinarySearch test = new TestUnsignedBinarySearch(generateObservedIPCounts());
			int failures = test.runAll();
			if (failures > 0) {
				throw new IllegalStateException(failures + " failed tests!");
			}
		}

		if (testUnsortedKeyExtract) {
			TestUnsortedKeyExtractor test = new TestUnsortedKeyExtractor(generateObservedIPCounts());
			int failures = test.runAll();
			if (failures > 0) {
				throw new IllegalStateException(failures + " failed tests!");
			}
		}

		if (testSortedKeyExtract) {
			TestSortedKeyExtractor test = new TestSortedKeyExtractor(generateObservedIPCounts());
			int failures = test.runAll();
			if (failures > 0) {
				throw new IllegalStateException(failures + " failed tests!");
			}
		}

		if (testSimpleMatcher) {
			TestSimpleMatcher test = new TestSimpleMatcher(generateObservedIPCounts());
			int failures = test.runAll();
			if (failures > 0) {
				throw new IllegalStateException(failures + " failed tests!");
			}
		}

		if (testBoundedSearchMatcher) {
			TestBoundedMatcher test = new TestBoundedMatcher(generateObservedIPCounts());
			int failures = test.runAll();
			if (failures > 0) {
				throw new IllegalStateException(failures + " failed tests!");
			}
		}

		if (testIPGenerator) {
			TestIPBGenerateIPAddressesAndCounts test = new TestIPBGenerateIPAddressesAndCounts();
			int failures = test.runAll();
			if (failures > 0) {
				throw new IllegalStateException(failures + " failed tests!");
			}
		}

		if (testIPPopulation) {
			TestIPBGenerateObservedCounts test = new TestIPBGenerateObservedCounts(generateIPPopulationParams());
			int failures = test.runAll();
			if (failures > 0) {
				throw new IllegalStateException(failures + " failed tests!");
			}
		}
	}
}
