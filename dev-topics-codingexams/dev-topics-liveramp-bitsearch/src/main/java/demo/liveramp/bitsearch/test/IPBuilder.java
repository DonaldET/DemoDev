package demo.liveramp.bitsearch.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import demo.liveramp.bitsearch.BitSearcher;

/**
 * Generates test IP and count populations for subnet extraction. As background,
 * please review some definitions of network vocabulary and the nature of the
 * problem - isolating subnets - that are explained at
 * <a href="https://www.pcwdld.com/subnet-mask-cheat-sheet-guide">this
 * tutorial</a>.
 */
public class IPBuilder {

	public static final int SEED = 7793;

	/**
	 * Prevent construction
	 */
	private IPBuilder() {
	}

	/**
	 * Generate (@code seenHostCount) host IP addresses beginning with (@code
	 * pattern) conditioned my (@code) mask.
	 * 
	 * @param seenHostCount the number of hosts to generate
	 * @param mask          the subnet bit mask
	 * @param pattern       the subnet pattern value
	 * @return a mock usage count map with IP key and usage count value
	 */
	public static Map<Integer, Integer> generateIPAddressesAndCounts(final int seenHostCount, final int mask,
			final int pattern) {

		final int hostBitCount = Integer.numberOfTrailingZeros(mask);
		if (hostBitCount < 1) {
			throw new IllegalArgumentException("hostCount SMALL, " + hostBitCount);
		}

		final int subnetBitCount = Integer.SIZE - hostBitCount;
		if (subnetBitCount < 1) {
			throw new IllegalArgumentException("subnetBitCount SMALL, " + subnetBitCount);
		}

		if (seenHostCount < 1) {
			throw new IllegalArgumentException("seenHostCount SMALL, " + subnetBitCount);
		}

		final int maxHosts = (int) Math.pow(2.0D, hostBitCount);
		if (seenHostCount > maxHosts) {
			throw new IllegalArgumentException("seenHostCount TOO BIG, " + seenHostCount + "; max=" + maxHosts);
		}

		Map<Integer, Integer> ipCounts = new HashMap<Integer, Integer>(seenHostCount);
		final int subnetPortion = mask & pattern;

		Random r = new Random();
		r.setSeed(SEED);

		while (ipCounts.size() < seenHostCount) {
			final int ip = r.nextInt(maxHosts) | subnetPortion;
			if (!ipCounts.containsKey(ip)) {
				ipCounts.put(ip, 1);
			}
		}

		return ipCounts;
	}

	/**
	 * Create IP populations defined by the (@code params) input list.
	 * 
	 * @param params A list of populations to generate
	 * @return a collection of generated populations
	 */
	public static Map<Integer, Integer> generateObservedCounts(
			final List<BitSearcher.SubnetPopulationParameters> params) {
		final int ntotal = params.stream().mapToInt((x) -> x.count).reduce(0, Integer::sum);
		final Map<Integer, Integer> population = new HashMap<Integer, Integer>(ntotal);

		for (BitSearcher.SubnetPopulationParameters pp : params) {
			final Map<Integer, Integer> onePop = generateIPAddressesAndCounts(pp.count, pp.mask, pp.pattern);
			population.putAll(onePop);
		}

		return population;
	}
}
