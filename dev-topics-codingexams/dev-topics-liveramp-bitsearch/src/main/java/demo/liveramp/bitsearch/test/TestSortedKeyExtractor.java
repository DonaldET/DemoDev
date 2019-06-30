package demo.liveramp.bitsearch.test;

import java.util.Comparator;
import java.util.Map;

import demo.liveramp.bitsearch.BitSearcher;

class TestSortedKeyExtractor {
	private int failures = 0;
	private final Map<Integer, Integer> observedIPCounts;

	public TestSortedKeyExtractor(final Map<Integer, Integer> observedIPCounts) {
		this.observedIPCounts = observedIPCounts;
	}

	public int runAll() {
		final int nKeys = observedIPCounts.size();
		System.out.println("\n**** Testing unsigned-sorted IP Count key extractor (" + nKeys + " IP observations.)");

		failures = 0;
		int[] sortedKeys = BitSearcher.getKeys(observedIPCounts, true);
		if (sortedKeys.length != nKeys) {
			System.err
					.println("--- expected " + nKeys + " keys but got " + sortedKeys.length + " IP counts in extract!");
			failures++;
		} else {
			if (nKeys > 1) {
				final Comparator<Integer> uc = new BitSearcher.UnsignedComparator();
				int nDiff = 0;
				for (int i = 0; i < nKeys - 1; i++) {
					if (uc.compare(sortedKeys[i], sortedKeys[i + 1]) > 0) {
						nDiff++;
					}
				}
				if (nDiff > 0) {
					System.err.println("--- expected total ordering, but found " + nDiff + " order difference!");
					failures++;
				}
			}
		}

		return failures;
	}
}