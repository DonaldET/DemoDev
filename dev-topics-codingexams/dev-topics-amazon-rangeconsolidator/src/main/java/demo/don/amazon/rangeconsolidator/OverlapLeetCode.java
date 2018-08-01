package demo.don.amazon.rangeconsolidator;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Isolate sort work from actual interval merging
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class OverlapLeetCode extends AbstractOverlap implements Overlap
{
    public static final class IntervalComparator implements Comparator<Interval>
    {
        @Override
        public int compare(final Interval a, final Interval b)
        {
            return a.start < b.start ? -1 : a.start == b.start ? 0 : 1;
        }
    }

    public OverlapLeetCode() {
        super();
    }

    /**
     * Sort overlapping ranges, each range a low-high pair.
     * 
     * @see demo.don.amazon.rangeconsolidator.Overlap#merge(java.util.List)
     */
    @Override
    public Merger merge(final List<Interval> intervals, final Comparator<Interval> optionalComparator)
    {
        if (intervals == null)
        {
            throw new IllegalArgumentException("intervals null");
        }
        assert optionalComparator == null;
        final List<Interval> copyOfOrdered = sortIntervals(intervals,
                optionalComparator == null ? new IntervalComparator() : optionalComparator);
        int n = copyOfOrdered.size();
        if (n < 2)
        {
            return new Merger(0, copyOfOrdered);
        }

        int merges = 0;
        LinkedList<Interval> merged = new LinkedList<Interval>();
        for (Interval interval : copyOfOrdered)
        {
            // if the list of merged intervals is empty or if the current
            // interval does not overlap with the previous, simply append it.
            if (merged.isEmpty() || merged.getLast().end < interval.start)
            {
                merged.add(interval);
            }
            else
            {
                // otherwise, there is overlap, so we merge the current and
                // previous intervals.
                merged.getLast().end = Math.max(merged.getLast().end, interval.end);
                merges++;
            }
        }

        return new Merger(merges, merged);
    }
}
