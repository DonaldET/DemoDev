package demo.liveramp.bitsearch.test;

import java.util.Map;

import demo.liveramp.bitsearch.BitSearcher;
import demo.liveramp.bitsearch.BitSearcher.Found;

class TestUnsignedBinarySearch {
	private int failures = 0;
	private final Map<Integer, Integer> observedIPCounts;

	//
	// Mask, pattern, table probe value, and comparison result test cases

	private static final int[][] testCases = { { 0x000000ff, 0x00000002, 2, -1 }, { 0x000000ff, 0x0000007f, 5, 0 },
			{ 0x80000000, 0x80000000, 14, 0 }, { 0XFFFFFFFF, 0x1FFFFFFE, 8, 0 }, { 0xFFFFFFFF, 0xFFFFFFFE, 19, -1 } };

	public TestUnsignedBinarySearch(final Map<Integer, Integer> observedIPCounts) {
		this.observedIPCounts = observedIPCounts;
	}

	public int runAll() {
		final int nKeys = observedIPCounts.size();
		System.out.println("\n**** Testing Unsigned Binary Search (" + nKeys + " IP observations.)");
		final int[] sortedKeys = BitSearcher.getKeys(observedIPCounts, true);

		failures = 0;
		for (int i = 0; i < testCases.length; i++) { // testCases.length
			int mask = testCases[i][0];
			int pattern = testCases[i][1];
			int probe = testCases[i][2];
			int cmp = testCases[i][3];
			Found insertp = BitSearcher.findInsertionPoint(sortedKeys, mask, pattern);
			int actp = insertp.probe;
			int actc = insertp.cmp;
			if (actp != probe || actc != cmp) {
				System.out.println("Case " + i + ".  mask: " + String.format("%08X", mask) + ";  pattern: "
						+ String.format("%08X", pattern) + ";  Wanted Prefix: " + String.format("%08X", mask & pattern)
						+ ";  exp probe=" + probe + ";  act probe=" + actp + ";  exp cmp=" + cmp + ";  act cmp=" + actc
						+ " . . . failed");
				failures++;
			}
		}

		return failures;
	}
}