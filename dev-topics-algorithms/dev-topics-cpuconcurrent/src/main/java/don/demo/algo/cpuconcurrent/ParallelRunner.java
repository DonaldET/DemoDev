package don.demo.algo.cpuconcurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import don.demo.algo.cpuconcurrent.api.ConcurrentCollector;
import don.demo.algo.cpuconcurrent.api.StreamRunner;
import don.demo.algo.cpuconcurrent.support.DataGenerator;
import don.demo.algo.cpuconcurrent.support.DataGenerator.MockDataDTO;

/**
 * Compare parallel processing performance of CPU intensive operations using
 * both <em>collectors</em> and <em>forEach</em> in Java 8 <code>Streams</code>.
 * There are two basic approaches for computing a collection of results as a
 * batch: <em>collector</em> and <em>forEach</em>.
 * <ul>
 * <li>The <em>collector</em> approach has each thread create an immutable
 * collection of results that are further combined into larger collections.</li>
 * <li>The <em>forEach</em> approach has each thread of parallel (multiple)
 * execution accesses a non-shared FIFO queue data structure that is combined
 * across multiple threads as they complete.</li>
 * </ul>
 * Parallel streams with <em>collector</em> and non-parallel executions require
 * no locking, but parallel streams with <em>forEach</em> require locking.
 * 
 * @author Donald Trummell
 */
public class ParallelRunner implements ConcurrentCollector {
	private static final long serialVersionUID = 6613509485305782384L;

	private static final double NANO_TO_MICRO = 1.0e+6;

	//
	// Shared global context:
	// Input data and expected map structure output data

	private static final AtomicLong accumulator = new AtomicLong(); // check sum of results
	private static int streamRunnerSelector = -1; // selects technique to time
	private static int[] data;
	private static Set<String> expectedKeySet;
	private static int sumData;
	private static int uniqueCount;
	private static List<StreamRunner<Collection<ComputationResult<Integer>>, IntStream, Integer, Integer>> runners = new ArrayList<>();

	static {
		runners.add(new ListCollector());
		runners.add(new ForEachCollector());
		runners.add(new ForEachConcurrentCollector());
	}

	/**
	 * A List collector, uses accumulating immutable lists to collect intermediate
	 * results.
	 *
	 * @author Donald Trummell
	 */
	public static class ListCollector
			implements StreamRunner<Collection<ComputationResult<Integer>>, IntStream, Integer, Integer> {
		private static final long serialVersionUID = -4439508226744565477L;

		@Override
		public Collection<ComputationResult<Integer>> runStream(IntStream stream, Function<Integer, Integer> worker) {
			Collection<ComputationResult<Integer>> result = stream
					.collect(() -> new ArrayList<ComputationResult<Integer>>(), (lc, s) -> {
						accumulator.getAndAdd(s);
						lc.add(new ComputationResult<Integer>("FE" + s, worker.apply(s)));
					}, (lc1, lc2) -> lc1.addAll(lc2));
			return result;
		}
	}

	/**
	 * A <em>forEach</em> collector uses a shared locking map to collect
	 * intermediate results. Previous tests indicate that a synchronized map
	 * performs better than a concurrent map.
	 *
	 * @author Donald Trummell
	 */
	public static class ForEachCollector
			implements StreamRunner<Collection<ComputationResult<Integer>>, IntStream, Integer, Integer> {
		private static final long serialVersionUID = -2199755972826098232L;

		@Override
		public Collection<ComputationResult<Integer>> runStream(IntStream stream, Function<Integer, Integer> worker) {
			final Map<String, ComputationResult<Integer>> map = Collections
					.synchronizedMap(new HashMap<String, ComputationResult<Integer>>());
			stream.forEach((s) -> {
				accumulator.getAndAdd(s);
				map.put("FE" + s, new ComputationResult<Integer>("FE" + s, worker.apply(s)));
			});

			return map.values().stream().collect(Collectors.toList());
		}
	}

	/**
	 * A <em>forEach</em> collector uses a shared locking map to collect
	 * intermediate results. Previous tests indicate that a synchronized map
	 * performs better than a concurrent map, but we repeat the timing here.
	 *
	 * @author Donald Trummell
	 */
	public static class ForEachConcurrentCollector
			implements StreamRunner<Collection<ComputationResult<Integer>>, IntStream, Integer, Integer> {
		private static final long serialVersionUID = 8744234588294187908L;

		@Override
		public Collection<ComputationResult<Integer>> runStream(IntStream stream, Function<Integer, Integer> worker) {
			final Map<String, ComputationResult<Integer>> map = new ConcurrentHashMap<String, ComputationResult<Integer>>();
			stream.forEach((s) -> {
				accumulator.getAndAdd(s);
				map.put("FE" + s, new ComputationResult<Integer>("FE" + s, worker.apply(s)));
			});

			return map.values().stream().collect(Collectors.toList());
		}
	}

	/**
	 * Compare different data structures performance under different concurrency
	 * scenarios.
	 * 
	 * @param args arg[0] is an integer where 0 = <code>ListCollector</code> and 1 =
	 *             <code>ForEachCollector</code>
	 */
	public static void main(String[] args) {
		if (args.length < 1) {
			throw new IllegalArgumentException("No selector parameter supplied");
		}
		String selectorParam = String.valueOf(args[0]);
		streamRunnerSelector = Integer.parseInt(selectorParam.trim());
		if (streamRunnerSelector < 0 || streamRunnerSelector >= runners.size()) {
			throw new IllegalArgumentException("Selector incorrect! Saw " + selectorParam);
		}
		int parallelism = displayParameters();

		estimateAverageWorkerTime(2000);
		System.out.println("\nCollector,Mode,N,MicroSec");

		Arrays.stream(new int[] { 10, 50, 75, 100, 125, 250, 500, 1000, 2000, 4000, 5000 }).boxed()
				.forEach((s) -> runSimulation(s, parallelism));
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
		timeSimulation(1, "non-Parallel");
		timeSimulation(parallelism, "Parallel");
	}

	private static double timeSimulation(int threadCount, final String label) {
		accumulator.set(0);

		IntStream strm = Arrays.stream(data);
		if (threadCount > 1) {
			strm = strm.parallel();
		}

		long startTime = System.nanoTime();
		StreamRunner<Collection<ComputationResult<Integer>>, IntStream, Integer, Integer> streamRunner = runners
				.get(streamRunnerSelector);
		System.out.print(streamRunner.getClass().getSimpleName() + "," + label);
		Collection<ComputationResult<Integer>> batchResults = streamRunner.runStream(strm, worker);
		long elapsedTime = System.nanoTime() - startTime;
		double elp = elapsedTime / NANO_TO_MICRO;
		System.out.println(String.format(",%d,%.3f", batchResults.size(), elp));

		//
		// Validate we computed results for the same data

		if (batchResults.size() != uniqueCount) {
			throw new IllegalStateException("expected queue size of " + uniqueCount);
		}

		if (accumulator.get() != sumData) {
			throw new IllegalStateException("expected accumulated value to be " + sumData);
		}

		Set<String> exp = new HashSet<String>(expectedKeySet);
		for (ComputationResult<Integer> c : batchResults) {
			String key = c.id;
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

	// ------------------------------------------------------------------------------

	/**
	 * Obtain the basic threading associated with <code>parallel</code> streams
	 * 
	 * @return number of threads in use
	 */
	private static int displayParameters() {
		int cores = Runtime.getRuntime().availableProcessors();
		int threads = ForkJoinPool.getCommonPoolParallelism();
		System.out.println("\nParallelization performance using run selector " + streamRunnerSelector + " running on "
				+ cores + " cores and " + threads + " threads.");
		return threads;
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
