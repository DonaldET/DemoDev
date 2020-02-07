package don.demo.algo.cpuconcurrent.support;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataGenerator implements Serializable {

	private static final long serialVersionUID = -4607534050159466669L;

	public static class MockDataDTO implements Serializable {
		private static final long serialVersionUID = 4594882194090278930L;

		public final int[] data;
		public final int sumData;
		public final int uniqueCount;
		public final Set<String> expectedKeySet;

		public MockDataDTO(int[] data, int sumData, int uniqueCount, Set<String> expectedKeySet) {
			super();
			this.data = data;
			this.sumData = sumData;
			this.uniqueCount = uniqueCount;
			this.expectedKeySet = expectedKeySet;
		}
	}

	public DataGenerator() {

	}

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
