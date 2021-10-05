package demo.don.amazon.rangeconsolidator;

import java.util.Comparator;
import java.util.List;

/**
 * Isolate sort work from actual interval merging
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class OverlapSort extends AbstractOverlap implements Overlap {
	public OverlapSort() {
		super();
	}

	/**
	 * Sort overlapping ranges, each range a low-high pair.
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
		return new Merger(0, copyOfOrdered);
	}
}
