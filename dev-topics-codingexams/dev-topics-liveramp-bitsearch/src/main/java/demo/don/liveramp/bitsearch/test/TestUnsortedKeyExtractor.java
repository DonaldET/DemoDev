package demo.don.liveramp.bitsearch.test;

import java.util.Comparator;
import java.util.Map;

import demo.don.liveramp.bitsearch.BitSearcher;

class TestUnsortedKeyExtractor {
	private int failures = 0;
	private final Map<Integer, Integer> observedIPCounts;

	public TestUnsortedKeyExtractor(final Map<Integer, Integer> observedIPCounts) {
		this.observedIPCounts = observedIPCounts;
	}

	public int runAll() {
		final int nKeys = observedIPCounts.size();
		System.out.println("\n**** Testing unsorted IP Count key extractor (" + nKeys + " IP observations.)");

		failures = 0;
		int[] keys = BitSearcher.getKeys(observedIPCounts, false);
		if (keys.length != nKeys) {
			System.err.println("--- expected " + nKeys + " keys but got " + keys.length + " IP counts in extract!");
			failures++;
		} else {
			if (nKeys > 1) {
				final Comparator<Integer> uc = new BitSearcher.UnsignedComparator();
				int nDiff = 0;
				for (int i = 0; i < nKeys - 1; i++) {
					if (uc.compare(keys[i], keys[i + 1]) > 0) {
						nDiff++;
					}
				}
				if (nDiff < 1) {
					System.err.println("--- expected some mis-ordering, but everything was ordered!");
					failures++;
				}
			}
		}

		return failures;
	}
}