package demo.don.amazon.rangeconsolidator;

import java.util.Comparator;
import java.util.List;

/**
 * Problem taken from <a href=
 * "https://leetcode.com/problems/merge-intervals/description/">LeetCode</a>.
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface Overlap {
	public static final class Merger {
		public final int merges;
		public final List<Interval> merged;

		public Merger(int merges, List<Interval> merged) {
			super();
			this.merges = merges;
			this.merged = merged;
		}

		@Override
		public String toString() {
			return "Merger -- merges=" + merges + ", merged=" + merged;
		}
	}

	/**
	 * An interval has a start-end pair and is ordered for searching (first by
	 * start, then by end.) Note that this class must be mutable for the Leet Code
	 * solution to work, and had been adapted by the other solutions.
	 * 
	 * @author Donald Trummell (dtrummell@gmail.com)
	 */
	public static final class Interval {
		public int start;
		public int end;

		public Interval(final int start, final int end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + end;
			result = prime * result + start;
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;

			final Interval other = (Interval) obj;

			return this.start == other.start ? (this.end == other.end ? true : false) : false;
		}

		@Override
		public String toString() {
			return "[" + start + ", " + end + "]";
		}
	}

	public abstract Merger merge(final List<Interval> intervals, final Comparator<Interval> comparator);
}