package don.demo.algo.cpuconcurrent.api;

import java.io.Serializable;
import java.util.function.Function;

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
		public final String id;
		public final T value;

		public ComputationResult(final String id, final T value) {
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

		@Override
		public String toString() {
			return "[" + getClass().getSimpleName() + " - 0x" + Integer.toHexString(hashCode()) + "; id: " + id
					+ ", value: " + value + "]";
		}

		@Override
		public int compareTo(ComputationResult<T> other) {
			int cmpr = this.id.compareTo(other.id);
			return cmpr != 0 ? cmpr : this.value.compareTo(other.value);
		}
	}

	/**
	 * The default CPU intensive worker function that is applied to a batch of inputs
	 */
	public static Function<Integer, Integer> worker = (Integer x) -> {
		long a = (long) Math.log1p(20.0 * (double) x) + (long) (Math.expm1(x + 1.0) * 200.0);
		long b = (long) (200.0 * (double) x * Math.abs(Math.sin((double) x)));
		long c = (long) (Math.tanh(x) * Math.atan(x));
		long d = ("a:" + a + "b:" + b + "c:" + c).length();
		return (int) (a + b * c + d);
	};
}
