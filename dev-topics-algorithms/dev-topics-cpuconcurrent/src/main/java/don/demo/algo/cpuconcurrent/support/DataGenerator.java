package don.demo.algo.cpuconcurrent.support;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Creates the test data
 * 
 * @author Donald Trummell
 */
public class DataGenerator implements Serializable {

	private static final long serialVersionUID = -4607534050159466669L;

	/**
	 * DTO for test data
	 * 
	 * @author Donald Trummell
	 */
	public static class MockDataDTO implements Serializable {
		private static final long serialVersionUID = 4594882194090278930L;

		/**
		 * Associated test data
		 */
		public final int[] data;
		/**
		 * Sum of test data
		 */
		public final int sumData;
		/**
		 * Count of unique elements
		 */
		public final int uniqueCount;
		/**
		 * The expected test values
		 */
		public final Set<String> expectedKeySet;

		/**
		 * Construct me
		 * 
		 * @param data           test data
		 * @param sumData        the sum of data
		 * @param uniqueCount    count of unique elements
		 * @param expectedKeySet the expected set of values
		 */
		public MockDataDTO(int[] data, int sumData, int uniqueCount, Set<String> expectedKeySet) {
			super();
			this.data = data;
			this.sumData = sumData;
			this.uniqueCount = uniqueCount;
			this.expectedKeySet = expectedKeySet;
		}
	}

	/**
	 * Construct me
	 */
	public DataGenerator() {

	}

	/**
	 * Create a test dataset of the desired size
	 * 
	 * @param desiredSetSize size of test data to generate
	 * @return the DTO with the data
	 */
	public MockDataDTO generate(int desiredSetSize) {
		if (desiredSetSize < 10) {
			throw new IllegalArgumentException("desired set size too small");
		} else if (desiredSetSize > 1000000) {
			throw new IllegalArgumentException("desired set size too big");
		}

		//
		// Build the permutation of sequential integer data values

		List<Integer> range = IntStream.rangeClosed(1, desiredSetSize).boxed().collect(Collectors.toList());
		final Random r = new Random(71);
		Collections.shuffle(range, r);
		int[] data = range.stream().mapToInt(Integer::intValue).toArray();

		//
		// Store the key values for later verification that all keys were processed
		// exactly once by the pipeline

		int sumData = 0;
		final Set<String> testKeySet = new HashSet<String>();
		for (Integer x : range) {
			String key = "FE" + x.intValue();
			if (!testKeySet.add(key)) {
				throw new IllegalStateException("key " + key + " is duplicated");
			}
			sumData += x.intValue();
		}

		int uniqueCount = testKeySet.size();
		if (uniqueCount != desiredSetSize) {
			throw new IllegalStateException(
					"Counts don't match :: uniqueCount: " + uniqueCount + "; desiredSetSize: " + desiredSetSize);
		}

		return new MockDataDTO(data, sumData, uniqueCount, testKeySet);
	}
}
