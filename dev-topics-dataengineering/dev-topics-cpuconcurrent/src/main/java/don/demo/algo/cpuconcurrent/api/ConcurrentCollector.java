package don.demo.algo.cpuconcurrent.api;

import java.io.Serializable;
import java.util.function.Function;
import java.util.stream.LongStream;

/**
 * Parallel Computation Facility; defines the result to be collected and a
 * default <em>worker</em> function that is applied to a batch of intputs.
 * 
 * @author Donald Trummell
 */
public interface ConcurrentCollector extends Serializable {

	/**
	 * The class is used to hold the key and computation result
	 * 
	 * @author Donald Trummell
	 *
	 * @param <T> the computation result class that is collected in each thread
	 */
	public static class ComputationResult<T extends Comparable<T>> implements Comparable<ComputationResult<T>> {
		/**
		 * The key associated with the computation value
		 */
		public final String id;
		/**
		 * The value associated with the key
		 */
		public final T value;

		/**
		 * Construct me
		 * 
		 * @param id    the key
		 * @param value the associated value
		 */
		public ComputationResult(final String id, final T value) {
			super();
			this.id = id;
			this.value = value;
		}

		/**
		 * Hashcode from state
		 */
		@Override
		public int hashCode() {
			final int prime = 71;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		/**
		 * Object equals based on state
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			ComputationResult<T> other = (ComputationResult<T>) obj;
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

		/**
		 * Internal state as a stream
		 */
		@Override
		public String toString() {
			return "[" + getClass().getSimpleName() + " - 0x" + Integer.toHexString(hashCode()) + "; id: " + id
					+ ", value: " + value + "]";
		}

		/**
		 * CompareTo based on state
		 */
		@Override
		public int compareTo(ComputationResult<T> other) {
			int cmpr = this.id.compareTo(other.id);
			return cmpr != 0 ? cmpr : this.value.compareTo(other.value);
		}
	}

	/**
	 * The default CPU intensive worker function that is applied to a batch of
	 * inputs
	 */
	public static Function<Integer, Integer> fastWorker = (Integer x) -> {
		long a = (long) Math.log1p(20.0 * (double) x) + (long) (Math.expm1(x + 1.0) * 200.0);
		long b = (long) (200.0 * (double) x * Math.abs(Math.sin((double) x)));
		long c = (long) (Math.tanh(x) * Math.atan(x));
		long d = ("a:" + a + "b:" + b + "c:" + c).length();

		return (int) (a + b * c + d);
	};

	/**
	 * The default highly CPU intensive worker function that is applied to a batch
	 * of inputs
	 */
	public static Function<Integer, Integer> slowWorker = (Integer x) -> {
		long a = (long) Math.log1p(20.0 * (double) x) + (long) (Math.expm1(x + 1.0) * 200.0);
		long b = (long) (200.0 * (double) x * Math.abs(Math.sin((double) x)));
		long c = (long) (Math.tanh(x) * Math.atan(x));
		long d = ("a:" + a + "b:" + b + "c:" + c).length();
		long limit = 10000L;
		long f = LongStream.rangeClosed(1L, limit).boxed().reduce(0L, Long::sum);
		long g = LongStream.iterate(1L, i -> i + 1).limit(limit).boxed().reduce(0L, Long::sum);

		return (int) (a + b * c + d + (f - g));
	};
}
