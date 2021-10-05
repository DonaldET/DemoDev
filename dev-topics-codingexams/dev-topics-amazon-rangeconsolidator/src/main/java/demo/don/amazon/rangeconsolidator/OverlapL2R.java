package demo.don.amazon.rangeconsolidator;

import java.util.Comparator;
import java.util.List;

/**
 * <strong>Problem statement:</strong>
 * 
 * <pre>
 * Given a collection of intervals (inclusive), merge all overlapping intervals.
 * Example: Input [[8,10], [1,4], [3,6], [15,18]].
 * Return [[8,10], [1,6], [15,18]]
 * </pre>
 * 
 * Note these clarifications:
 * <ul>
 * <li>Range boundaries are int values (full range)</li>
 * <li>A range is always low bound followed by high bound, with high bond less
 * than or equal to low bound</li>
 * <li>Ranges may not be unique</li>
 * <li>Merge candidates have low of one range included in a second range, or
 * high greater than or equal to the second candidate high</li>
 * </ul>
 * 
 * <strong>Note:</strong>this version merges left to right.
 * 
 * <a href=
 * "https://livecode.amazon.jobs/session/04d9e66e-ce1a-42fd-8854-cb9c1c800268">Built
 * for Amazon here</a>, but also taken from the educational site <a href=
 * "https://leetcode.com/problems/merge-intervals/description/">LeetCode</a>.
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class OverlapL2R extends AbstractOverlap implements Overlap {
	public OverlapL2R() {
		super();
	}

	/**
	 * Merge overlapping ranges, each range a low-high pair.
	 * 
	 * @see demo.don.amazon.rangeconsolidator.Overlap#merge(java.util.List)
	 */
	@Override
	public Merger merge(final List<Interval> intervals, final Comparator<Interval> optionalComparator) {
		assert optionalComparator == null;
		final List<Interval> copyOfOrdered = sortIntervals(intervals,
				optionalComparator == null ? new AbstractOverlap.MergeComparator() : optionalComparator);
		int n = copyOfOrdered.size();
		if (n < 2) {
			return new Merger(0, copyOfOrdered);
		}

		int merges = 0;
		int lhs_pos = 0;
		int rhs_pos = 1;

		do {
			final Interval lhs = copyOfOrdered.get(lhs_pos);
			final Interval rhs = copyOfOrdered.get(rhs_pos);
			if (rhs.start > lhs.end) {
				// No overlap
				lhs_pos = rhs_pos;
				rhs_pos++;
			} else {
				// Overlap
				rhs.start = lhs.start;
				rhs.end = Math.max(lhs.end, rhs.end);
				copyOfOrdered.remove(lhs_pos);
				n -= 1;
				merges++;
			}
		} while (rhs_pos < n);

		return new Merger(merges, copyOfOrdered);
	}
}
