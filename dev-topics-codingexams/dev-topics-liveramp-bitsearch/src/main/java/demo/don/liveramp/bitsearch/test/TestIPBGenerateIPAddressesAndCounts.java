package demo.don.liveramp.bitsearch.test;

import java.util.Map;

class TestIPBGenerateIPAddressesAndCounts {
	private int failures = 0;

	public TestIPBGenerateIPAddressesAndCounts() {
	}

	public int runAll() {
		System.out.println("\n**** Testing IPBuilder method generateIPAddressesAndCounts");

		failures = 0;
		final int seenHostCount = 256;
		final int mask = 0xffffff00;
		final int pattern = 0x00003400;
		Map<Integer, Integer> collectedIP = IPBuilder.generateIPAddressesAndCounts(seenHostCount, mask, pattern);
		final int n = collectedIP.size();
		if (n != seenHostCount) {
			System.err.println("--- expected " + seenHostCount + " but found " + n);
			failures++;

		} else {
			final int subnet = mask & pattern;
			for (int hostID = 0; hostID < seenHostCount; hostID++) {
				final int ip = subnet | hostID;
				final Integer count = collectedIP.get(ip);
				if (count == null) {
					System.err.println("--- key " + ip + " not generated");
					failures++;
				} else {
					if (1 != count) {
						System.err.println("--- key " + ip + " count differs from 1, " + count);
						failures++;
					}
				}
			}
		}

		return failures;
	}
}