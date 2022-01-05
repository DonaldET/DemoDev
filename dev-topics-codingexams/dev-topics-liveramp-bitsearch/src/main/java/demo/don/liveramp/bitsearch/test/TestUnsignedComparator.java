package demo.don.liveramp.bitsearch.test;

import java.util.Comparator;

import demo.don.liveramp.bitsearch.BitSearcher;

class TestUnsignedComparator {
	private int failures = 0;
	private final int[][] testCases;

	public TestUnsignedComparator(final int[][] testCases) {
		this.testCases = testCases;
	}

	public int runAll() {
		final int nTests = testCases.length;
		System.out.println("\n**** Testing Unsigned Comparator (" + nTests + " test cases.)");
		final Comparator<Integer> uc = new BitSearcher.UnsignedComparator();
		failures = 0;
		for (int i = 0; i < nTests; i++) {
			int lhs = testCases[i][0];
			int rhs = testCases[i][1];
			int exp = testCases[i][2];
			int act = uc.compare(lhs, rhs);
			if (act != exp) {
				System.out.println(
						"Case " + i + ".  lhs: " + Integer.toHexString(lhs) + ((String) ((lhs < 0) ? "*" : " "))
								+ ";  rhs: " + Integer.toHexString(rhs) + ((String) ((lhs < 0) ? "*" : " ")) + ";  exp="
								+ exp + ";  act=" + act + ";  diff: " + (lhs - rhs) + " . . . failed");
			}
		}

		return failures;
	}
}