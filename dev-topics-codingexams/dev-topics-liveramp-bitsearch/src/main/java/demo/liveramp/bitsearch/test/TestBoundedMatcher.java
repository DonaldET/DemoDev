package demo.liveramp.bitsearch.test;

import java.util.Map;

import demo.liveramp.bitsearch.BitSearcher;

class TestBoundedMatcher {
	private int failures = 0;
	private final Map<Integer, Integer> observedIPCounts;

	//
	// Mask and pattern pairs to with counts to validate counting

	private static final int[][] testCases = { { 0x80000000, 0x80000000, 6 }, { 0xc0000000, 0xc0000000, 4 },
			{ 0xc0000000, 0xa0000000, 2 }, { 0xe0000000, 0xe0000000, 2 }, { 0xe0000000, 0x20000000, 2 },
			{ 0xf0000000, 0xf0000000, 1 }, { 0xf0000000, 0x20000000, 0 } };

	public TestBoundedMatcher(final Map<Integer, Integer> observedIPCounts) {
		this.observedIPCounts = observedIPCounts;
	}

	public int runAll() {
		final int nKeys = observedIPCounts.size();
		System.out.println(
				"\n**** Testing Bounded Search Prefix Matcher [sort required] (" + nKeys + " IP observations.)");
		int[] keys = BitSearcher.getKeys(observedIPCounts, true);
		BitSearcher.PrefixCounter spc = new BitSearcher.BoundedSearchCounter();

		failures = 0;
		for (int i = 0; i < testCases.length; i++) {
			int mask = testCases[i][0];
			int pattern = testCases[i][1];
			int expCount = testCases[i][2];
			int act = spc.countMatches(mask, pattern, keys);
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