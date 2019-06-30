package demo.liveramp.bitsearch.performance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import demo.liveramp.bitsearch.BitSearcher.PrefixCounter;
import demo.liveramp.bitsearch.BitSearcher.SubnetPopulationParameters;
import demo.liveramp.bitsearch.test.IPBuilder;

public class PerformanceRunner {
	public static final long SEED = 7901;
	private static final int TEST_SIZE = 3;

	private static final List<SubnetPopulationParameters> params = new ArrayList<SubnetPopulationParameters>();
	private static final List<PrefixCounter> implementations = new ArrayList<PrefixCounter>();

	static {
		params.add(new SubnetPopulationParameters(0xffff0000, 0x12340000, 100));
		params.add(new SubnetPopulationParameters(0xfff00000, 0x77700000, 30));
		params.add(new SubnetPopulationParameters(0xff000000, 0x13000000, 60));
		params.add(new SubnetPopulationParameters(0xf0000000, 0x60000000, 10));
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

	private Map<Integer, Integer> ipCounts = null;

	public static void main(String[] args) {
		System.out.println("\nPerformance Test IP access count SubNet Finder");
	}

	private List<SubnetPopulationParameters> setupIPCounts(int multiplier) {
		final int ntotal = params.stream().mapToInt((x) -> x.count).reduce(0, Integer::sum);
		List<SubnetPopulationParameters> testParams = new ArrayList<SubnetPopulationParameters>(params.size());
		for (SubnetPopulationParameters param : params) {
			testParams.add(new SubnetPopulationParameters(param.mask, param.pattern, param.count * multiplier));
		}
		ipCounts = IPBuilder.generateObservedCounts(testParams);
		final int npop = ipCounts.size();
		final int nexp = ntotal * multiplier;
		if (nexp != npop) {
			throw new IllegalStateException("population generation failed, expected " + nexp + " entries, but got "
					+ npop + "; parames are: " + testParams);
		}

		return testParams;
	}

	private RunResult getCountAndTime(String label, int mask, int pattern, int[] ipAccessCounts,
			PrefixCounter counter) {
		//
		// Throw away initial run for hot-spot optimization

		final int n1 = counter.countMatches(mask, pattern, ipAccessCounts);
		System.gc();

		//
		// Try and prevent compiler from optimizing the additional runs

		Random r = new Random();
		r.setSeed(SEED);
		int rv = r.nextInt();

		final long startTM = System.nanoTime();
		for (int i = 0; i < TEST_SIZE; i++) {
			int n = counter.countMatches(mask + (rv - rv), pattern, ipAccessCounts);
			if (n != n1) {
				throw new IllegalStateException(
						"Run {" + label + ":" + i + "} got wrong count of " + n + ", expected " + n1);
			}
		}
		final long elapsedTM = System.nanoTime() - startTM;

		return new RunResult(label, TEST_SIZE, elapsedTM, n1);
	}
}
