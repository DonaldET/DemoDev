package don.demo.algo.cpuconcurrent;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.IntStream;

import don.demo.algo.cpuconcurrent.support.DataGenerator;
import don.demo.algo.cpuconcurrent.support.DataGenerator.MockDataDTO;

/**
 * Compare parallel processing using <em>forEach</em> in Java 8
 * <code>Streams</code>. Each thread of the parallel (multiple) <em>forEach</em>
 * execution accesses a shared or common data structure. Non-parallel executions
 * require no locking.
 * <p>
 * Note: initial test!
 * 
 * @author Donald Trummell
 */
public class ParallelForEach {
	private static final double NANO_TO_MICRO = 1.0e+6;

	private static final AtomicLong accumulator = new AtomicLong();

	//
	// Shared global context:
	// Input data and expected map structure output data

	private static int[] data;
	private static Set<String> expectedKeySet;
	private static int sumData;
	private static int uniqueCount;

	/**
	 * The CPU-intensive calculation to split among threads
	 */
	private static Function<Integer, Integer> worker = (Integer x) -> {
		long a = (long) Math.log1p(20.0 * (double) x) + (long) (Math.expm1(x + 1.0) * 200.0);
		long b = (long) (200.0 * (double) x * Math.abs(Math.sin((double) x)));
		long c = (long) (Math.tanh(x) * Math.atan(x));
		long d = ("a:" + a + "b:" + b + "c:" + c).length();
		return (int) (a + b * c + d);
	};

	/**
	 * Compare different data structures performance under different concurrency
	 * scenarios.
	 * 
	 * @param args ignored
	 */
	public static void main(String[] args) {
		int parallelism = displayParallelism();

		estimateAverageWorkerTime(2000);

		runSimulation(75, parallelism);
		runSimulation(125, parallelism);
		runSimulation(250, parallelism);
		runSimulation(500, parallelism);
		runSimulation(1000, parallelism);
		runSimulation(2000, parallelism);
		runSimulation(5000, parallelism);
		System.out.println("\n--- Done");
	}

	/**
	 * Build data and test structures with parallel and non-parallel.
	 * 
	 * @param numTestCases
	 * @param parallelism
	 */
	private static void runSimulation(int numTestCases, int parallelism) {
		System.gc();
		setupTestData(numTestCases);
		System.out.print("\nWe will collect " + uniqueCount + " execution results in different maps using 1 or "
				+ parallelism + " threads.");
		timeSimulation(1, new HashMap<String, Integer>(), " non-Parallel");
		timeSimulation(parallelism, new ConcurrentHashMap<String, Integer>(), "Concurent Map");
		timeSimulation(parallelism, Collections.synchronizedMap(new HashMap<String, Integer>()), "     Sync Map");
	}

	private static double timeSimulation(int threadCount, Map<String, Integer> map, final String label) {
		accumulator.set(0);
		map.clear();
		IntStream strm = Arrays.stream(data);
		if (threadCount > 1) {
			strm = strm.parallel();
		}

		long startTime = System.nanoTime();
		strm.forEach((s) -> {
			map.put("FE" + s, worker.apply(s));
			accumulator.getAndAdd(s);
		});
		long elapsedTime = System.nanoTime() - startTime;
		double elp = elapsedTime / NANO_TO_MICRO;
		System.out.println(
				"\n " + label + " - N=" + map.size() + "; (Sum: " + accumulator.get() + "): " + listFirstN(map, 15));
		System.out.println(String.format("         Time: %.3f us", elp)
				+ (threadCount > 1 ? " [parallel" + threadCount + "]" : ""));

		//
		// Validate we computed results for the same data

		if (map.size() != uniqueCount) {
			throw new IllegalStateException("expected map size of " + uniqueCount);
		}

		if (accumulator.get() != sumData) {
			throw new IllegalStateException("expected accumulated value to be " + sumData);
		}

		Set<String> exp = new HashSet<String>(expectedKeySet);
		for (String key : map.keySet()) {
			if (exp.contains(key)) {
				exp.remove(key);
			} else {
				throw new IllegalStateException("key " + key + " not in expected keys");
			}
		}

		if (exp.size() != 0) {
			throw new IllegalStateException("some expected keys not found");
		}

		return elp;
	}

	/**
	 * Create a set of random unique test identifiers representing test data, in the
	 * random order to be tested, and offer a queue structure for validating that a
	 * parallel stream generates exactly one answer for all test data processed.
	 * 
	 * @param desiredSetSize the number of unique data points to generate
	 */
	private static void setupTestData(int desiredSetSize) {

		MockDataDTO mockData = (new DataGenerator()).generate(desiredSetSize);
		data = mockData.data;
		sumData = mockData.sumData;
		uniqueCount = mockData.uniqueCount;
		expectedKeySet = mockData.expectedKeySet;
	}

	private static String listFirstN(Map<String, Integer> data, final int n) {
		StringBuilder msg = new StringBuilder(1000);
		int nListed = 0;
		for (Map.Entry<String, Integer> e : data.entrySet()) {
			if (nListed >= n) {
				break;
			}
			if (nListed > 0) {
				msg.append(", ");
			}
			msg.append(e.getKey());
			nListed++;
		}

		if (nListed < data.size()) {
			msg.append(", ...");
		}

		return msg.toString();
	}

	// ------------------------------------------------------------------------------

	/**
	 * Obtain the basic threading associated with <code>parallel</code> streams
	 * 
	 * @return number of threads in use
	 */
	private static int displayParallelism() {
		int cores = Runtime.getRuntime().availableProcessors();
		System.out.println("\nParallelization Example for " + cores + " JVM processors, some worker outputs are:");
		int x = 0;
		System.out.println("  -- work(" + x + ") = " + worker.apply(x));
		x = 1;
		System.out.println("  -- work(" + x + ") = " + worker.apply(x));
		x = 10;
		System.out.println("  -- work(" + x + ") = " + worker.apply(x));
		return ForkJoinPool.getCommonPoolParallelism();
	}

	/**
	 * Compute the average time for a single execution of the working function
	 * 
	 * @param ntrials the number of executions needed to get a stable timing
	 */
	private static void estimateAverageWorkerTime(int nWorkerTrials) {
		setupTestData(nWorkerTrials);
		double avgWorkerTime = timeWorker(nWorkerTrials) / NANO_TO_MICRO;
		System.out.println(String.format(
				"  -- Average worker time: %.4f us, total non-parallel worker time is %.3f us for %d executions",
				avgWorkerTime, avgWorkerTime * nWorkerTrials, nWorkerTrials));
	}

	/**
	 * Apply the worker function over the number of trials; assume global data for
	 * timing test is defined.
	 * 
	 * @param nTrials the number of executions
	 * @return average time for execution, in nano-sec
	 */
	private static double timeWorker(int nTrials) {
		int lth = data.length;
		long startTime = System.nanoTime();
		for (int i = 0; i < nTrials; i++) {
			worker.apply(data[i % lth]);
		}
		return (double) (System.nanoTime() - startTime) / (double) nTrials;
	}
}
