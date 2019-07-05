package demo.liveramp.bitsearch.performance;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import demo.liveramp.bitsearch.BitSearcher;
import demo.liveramp.bitsearch.BitSearcher.BoundedSearchCounter;
import demo.liveramp.bitsearch.BitSearcher.PrefixCounter;
import demo.liveramp.bitsearch.BitSearcher.SimpleSearchCounter;
import demo.liveramp.bitsearch.BitSearcher.SubnetPopulationParameters;
import demo.liveramp.bitsearch.test.IPBuilder;

public class PerformanceRunner {
	public static final long SEED = 7901;
	private static final int TEST_SIZE = 3;
	private static final int EXTRACT_SIZE = 3;

	private static final List<SubnetPopulationParameters> params = new ArrayList<SubnetPopulationParameters>();
	private static final List<PrefixCounter> implementations = new ArrayList<PrefixCounter>();
	private static final int[] multipliers = { 1000, 5000, 10000, 20000, 25000, 35000 };

	static {
		params.add(new SubnetPopulationParameters(0xfff00000, 0x00100000, 25));
		params.add(new SubnetPopulationParameters(0xfff00000, 0x00200000, 21));
		params.add(new SubnetPopulationParameters(0xfff00000, 0x00400000, 27));
		params.add(new SubnetPopulationParameters(0xfff00000, 0x00800000, 22));
		params.add(new SubnetPopulationParameters(0xfff00000, 0x01000000, 24));
		params.add(new SubnetPopulationParameters(0xfff00000, 0x02000000, 20));

		implementations.add(new BoundedSearchCounter());
		implementations.add(new SimpleSearchCounter());
	}

	/**
	 * Return performance results
	 */
	public static class RunResult {
		public String label;
		public int runs;
		public long elapsedNanoSec;
		public int count;

		public RunResult(String label, int runs, long elapsedNanoSec, int count) {
			super();
			this.label = label;
			this.runs = runs;
			this.elapsedNanoSec = elapsedNanoSec;
			this.count = count;
		}
	}

	private Map<Integer, Integer> ipAccessCounts = null;
	private int[] keysUnordered = null;
	private int[] keysOrdered = null;

	public static void main(String[] args) {
		System.out.println("\nPerformance Test IP access count SubNet Finder");
		System.out.println("Impl, Multiplier, Mask, Pattern, N, Count, LoadRuns, LoadTM, SearchRuns, SearchTM");

		PerformanceRunner pr = new PerformanceRunner();
		final Map<String, Integer> testedCounts = pr.orchestrateTestRuns();
		System.out.println("\nCount Done!");

		System.out.println("\nCounts:\n   [i] Implementation           Mask     Pattern       POP         Counted");
		int i = 0;
		for (Map.Entry<String, Integer> e : testedCounts.entrySet()) {
			System.out.println("  " + String.format("%3d", (i++)) + ". " + e.getKey() + "  =>  "
					+ String.format("%10d", e.getValue()));
		}
		System.out.println("\nSummary Done!");
	}

	private Map<String, Integer> orchestrateTestRuns() {
		final Map<String, Integer> testedCounts = new LinkedHashMap<>();

		for (int i = 0; i < multipliers.length; i++) {
			int multipler = multipliers[i];
			List<SubnetPopulationParameters> testParams = setupIPCounts(multipler);

			RunResult extrUnordered = extractKeys("M:" + multipler, false);
			RunResult extrOrdered = extractKeys("M:" + multipler, true);
			if (extrOrdered.count != extrUnordered.count) {
				throw new IllegalStateException("extract key length differs for ordered vs unordered");
			}

			for (PrefixCounter pc : implementations) {
				final boolean ordered = pc.isOrdered();
				for (SubnetPopulationParameters spp : testParams) {
					String label = String.format("%-20s", pc.getName()) + (ordered ? "-ORD" : "-RND");

					int loadRuns = 0;
					long loadTM = 0;
					RunResult result = null;
					long searchTM = 0;
					int searchRuns = 0;

					if (ordered) {
						loadRuns = extrOrdered.runs;
						loadTM = round2Milli(extrOrdered.elapsedNanoSec, loadRuns);
						result = getCountAndTime(label, spp.mask, spp.pattern, keysOrdered, pc);
						searchRuns = result.runs;
						searchTM = round2Milli(result.elapsedNanoSec, searchRuns);
					} else {
						loadRuns = extrUnordered.runs;
						loadTM = round2Milli(extrUnordered.elapsedNanoSec, loadRuns);
						result = getCountAndTime(label, spp.mask, spp.pattern, keysUnordered, pc);
						searchRuns = result.runs;
						searchTM = round2Milli(result.elapsedNanoSec, searchRuns);
					}

					final String countKey = label + ":" + String.format("%08x", spp.mask) + ":"
							+ String.format("%08x", spp.pattern) + ":" + String.format("%8d", spp.count);
					if (testedCounts.put(countKey, result.count) != null) {
						throw new IllegalStateException("key " + countKey + " repeated!");
					}

					System.out.println(label + ", " + multipler + ", " + Integer.toHexString(spp.mask) + ", "
							+ Integer.toHexString(spp.pattern) + ", " + ipAccessCounts.size() + ", " + result.count
							+ ", " + loadRuns + ", " + loadTM + ", " + searchRuns + ", " + searchTM);
				}
			}
		}

		return testedCounts;
	}

	private List<SubnetPopulationParameters> setupIPCounts(int multipler) {
		final int ntotal = params.stream().mapToInt((x) -> x.count).reduce(0, Integer::sum);
		List<SubnetPopulationParameters> testParams = new ArrayList<SubnetPopulationParameters>(params.size());
		for (SubnetPopulationParameters param : params) {
			testParams.add(new SubnetPopulationParameters(param.mask, param.pattern, param.count * multipler));
		}

		try {
			ipAccessCounts = IPBuilder.generateObservedCounts(testParams);
		} catch (Exception ex) {
			throw new IllegalStateException("failure with " + testParams, ex);
		}

		final int npop = ipAccessCounts.size();
		final int nexp = ntotal * multipler;
		if (nexp != npop) {
			throw new IllegalStateException("population generation failed, expected " + nexp + " entries, but got "
					+ npop + "; parames are: " + testParams);
		}

		return testParams;
	}

	private RunResult extractKeys(final String label, final boolean ordered) {
		//
		// Throw away initial run for hot-spot optimization

		if (keysUnordered == null && keysOrdered == null) {
			keysOrdered = BitSearcher.getKeys(ipAccessCounts, ordered);
			keysUnordered = BitSearcher.getKeys(ipAccessCounts, ordered);
		}

		int n = 0;
		final long startTM = System.nanoTime();
		for (int i = 0; i < EXTRACT_SIZE; i++) {
			if (ordered) {
				keysOrdered = BitSearcher.getKeys(ipAccessCounts, true);
				n = keysOrdered.length;
			} else {
				keysUnordered = BitSearcher.getKeys(ipAccessCounts, false);
				n = keysUnordered.length;
			}
		}
		final long elapsedTM = System.nanoTime() - startTM;

		return new RunResult(label, EXTRACT_SIZE, elapsedTM, n);
	}

	private RunResult getCountAndTime(String label, int mask, int pattern, int[] keys, PrefixCounter counter) {
		//
		// Throw away initial run for hot-spot optimization

		final int n1 = counter.countMatches(ipAccessCounts, mask, pattern, keys);
		System.gc();

		//
		// Try and prevent compiler from optimizing the additional runs

		Random r = new Random();
		r.setSeed(SEED);
		int rv = r.nextInt();

		final long startTM = System.nanoTime();
		for (int i = 0; i < TEST_SIZE; i++) {
			int n = counter.countMatches(ipAccessCounts, mask + (rv - rv), pattern, keys);
			if (n != n1) {
				throw new IllegalStateException(
						"Run {" + label + ":" + i + "} got wrong count of " + n + ", expected " + n1);
			}
		}
		final long elapsedTM = System.nanoTime() - startTM;

		return new RunResult(label, TEST_SIZE, elapsedTM, n1);
	}

	private long round2Milli(long nanoElapsed, int nRuns) {
		long perRun = (nanoElapsed + nRuns - 1) / nRuns;

		return (long) Math.rint(perRun / (double) 1E+6);
	}
}
