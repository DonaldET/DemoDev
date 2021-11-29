package don.demo.algo.cpuconcurrent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.IntStream;

import don.demo.algo.cpuconcurrent.support.DataGenerator;
import don.demo.algo.cpuconcurrent.support.DataGenerator.MockDataDTO;

/**
 * Compare parallel processing using <em>collectors</em> in Java 8
 * <code>Streams</code>. Each thread of parallel (multiple) execution accesses a
 * non-shared FIFO queue data structure that is combined across multiple threads
 * as they complete. Both Parallel and Non-parallel executions require no
 * locking.
 * <p>
 * Note: initial test!
 * 
 * @author Donald Trummell
 */
public class ParallelCollect {
	private static final double NANO_TO_MICRO = 1.0e+6;

	private static final AtomicLong accumulator = new AtomicLong();

	/**
	 * Holds the result of a worker computation
	 * 
	 * @author Donald Trummell
	 */
	public static class Computation implements Comparable<Computation> {
		/**
		 * The key associated with the result
		 */
		public final String id;
		/**
		 * The computational result associated with the value
		 */
		public final Integer value;

		/**
		 * Construct me
		 * 
		 * @param id    the key
		 * @param value the associated value
		 */
		public Computation(String id, Integer value) {
			super();
			this.id = id;
			this.value = value;
		}

		@Override
		public int hashCode() {
			final int prime = 71;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Computation other = (Computation) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "[Computation - 0x" + Integer.toHexString(hashCode()) + "; id: " + id + ", value: " + value + "]";
		}

		@Override
		public int compareTo(Computation other) {
			int cmpr = this.id.compareTo(other.id);
			return cmpr != 0 ? cmpr : Integer.compare(this.value, other.value);
		}
	}

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

		estimateAverageWorkerTime(2000, "non-parallel");

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
		System.out.print("\nWe will collect " + uniqueCount + " execution results in different lists using 1 or "
				+ parallelism + " threads.");
		timeSimulation(1, " non-Parallel");
		timeSimulation(parallelism, "     Parallel");
	}

	private static double timeSimulation(int threadCount, final String label) {
		accumulator.set(0);

		IntStream strm = Arrays.stream(data);
		if (threadCount > 1) {
			strm = strm.parallel();
		}

		long startTime = System.nanoTime();
		List<Computation> listCollection = strm.collect(() -> new ArrayList<Computation>(), (lc, s) -> {
			lc.add(new Computation("FE" + s, worker.apply(s)));
			accumulator.getAndAdd(s);
		}, (lc1, lc2) -> lc1.addAll(lc2));
		long elapsedTime = System.nanoTime() - startTime;
		double elp = elapsedTime / NANO_TO_MICRO;
		System.out.println("\n " + label + " - N=" + listCollection.size() + "; (Sum: " + accumulator.get() + "): "
				+ listFirstN(listCollection, 15));
		System.out.println(String.format("         Time: %.3f us", elp)
				+ (threadCount > 1 ? " [parallel" + threadCount + "]" : ""));

		//
		// Validate we computed results for the same data

		if (listCollection.size() != uniqueCount) {
			throw new IllegalStateException("expected queue size of " + uniqueCount);
		}

		if (accumulator.get() != sumData) {
			throw new IllegalStateException("expected accumulated value to be " + sumData);
		}

		Set<String> exp = new HashSet<String>(expectedKeySet);
		for (Computation c : listCollection) {
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

	private static String listFirstN(List<Computation> data, final int n) {
		StringBuilder msg = new StringBuilder(1000);
		int nListed = 0;
		for (Computation c : data) {
			if (nListed >= n) {
				break;
			}
			if (nListed > 0) {
				msg.append(", ");
			}
			msg.append(c.id);
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
		System.out.println("\nParallelization with non-shared data, this is an example for " + cores
				+ " JVM processors, some worker outputs are:");
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
	private static void estimateAverageWorkerTime(int nWorkerTrials, String label) {
		setupTestData(nWorkerTrials);
		double avgWorkerTime = timeWorker(nWorkerTrials) / NANO_TO_MICRO;
		System.out.println(
				String.format("  -- Average %s worker time: %.4f us, total %s worker time is %.3f us for %d executions",
						label, avgWorkerTime, label, avgWorkerTime * nWorkerTrials, nWorkerTrials));
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
