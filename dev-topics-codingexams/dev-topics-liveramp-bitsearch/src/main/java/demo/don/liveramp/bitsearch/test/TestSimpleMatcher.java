package demo.don.liveramp.bitsearch.test;

import java.util.Map;

import demo.don.liveramp.bitsearch.BitSearcher;

class TestSimpleMatcher {
	//
	// Note: changed matching criteria from matching servers to access counts of
	// matching servers:
	// **** Testing Simple Search Prefix Matcher [no sort required] (20 IP observations.)
	// Case 0. mask: 80000000; pattern: 80000000; Wanted Prefix: 80000000; exp=6;
	// act=4102 . . . failed
	// Case 1. mask: C0000000; pattern: C0000000; Wanted Prefix: C0000000; exp=4;
	// act=3066 . . . failed
	// Case 2. mask: C0000000; pattern: A0000000; Wanted Prefix: 80000000; exp=2;
	// act=1036 . . . failed
	// Case 3. mask: E0000000; pattern: E0000000; Wanted Prefix: E0000000; exp=2;
	// act=2040 . . . failed
	// Case 4. mask: E0000000; pattern: 20000000; Wanted Prefix: 20000000; exp=2;
	// act=1036 . . . failed
	// Case 5. mask: F0000000; pattern: F0000000; Wanted Prefix: F0000000; exp=1;
	// act=1026 . . . failed

	private int failures = 0;
	private final Map<Integer, Integer> observedIPCounts;

	//
	// Mask and pattern pairs to with counts to validate counting

	private static final int[][] testCases = { { 0x80000000, 0x80000000, 4102 }, { 0xc0000000, 0xc0000000, 3066 },
			{ 0xc0000000, 0xa0000000, 1036 }, { 0xe0000000, 0xe0000000, 2040 }, { 0xe0000000, 0x20000000, 1036 },
			{ 0xf0000000, 0xf0000000, 1026 }, { 0xf0000000, 0x20000000, 0 } };

	public TestSimpleMatcher(final Map<Integer, Integer> observedIPCounts) {
		this.observedIPCounts = observedIPCounts;
	}

	public int runAll() {
		final int nKeys = observedIPCounts.size();
		System.out.println(
				"\n**** Testing Simple Search Prefix Matcher [no sort required] (" + nKeys + " IP observations.)");
		int[] keys = BitSearcher.getKeys(observedIPCounts, true);
		BitSearcher.PrefixCounter spc = new BitSearcher.SimpleCounter();

		failures = 0;
		for (int i = 0; i < testCases.length; i++) {
			int mask = testCases[i][0];
			int pattern = testCases[i][1];
			int expCount = testCases[i][2];
			int act = spc.countMatches(observedIPCounts, mask, pattern, keys);
			if (act != expCount) {
				System.out.println("Case " + i + ".  mask: " + String.format("%08X", mask) + ";  pattern: "
						+ String.format("%08X", pattern) + ";  Wanted Prefix: " + String.format("%08X", mask & pattern)
						+ ";  exp=" + expCount + ";  act=" + act + " . . . failed");
				failures++;
			}
		}

		return failures;
	}
}