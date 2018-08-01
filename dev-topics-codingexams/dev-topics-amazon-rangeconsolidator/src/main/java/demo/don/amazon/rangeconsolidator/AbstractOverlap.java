package demo.don.amazon.rangeconsolidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractOverlap implements Overlap
{
    public static final class MergeComparator implements Comparator<Interval>
    {
        @Override
        public int compare(final Interval a, final Interval b)
        {
            return a.start < b.start ? -1 : (a.start > b.start ? 1 : (a.end < b.end ? -1 : (a.end > b.end ? 1 : 0)));
        }
    }

    public AbstractOverlap() {
    }

    public List<Interval> sortIntervals(final List<Interval> unsorted, final Comparator<Interval> comparator)
    {
        final int n = unsorted.size();
        final List<Interval> sorted = new ArrayList<Interval>(n);
        if (n > 0)
        {
            for (final Interval intr : unsorted)
            {
                sorted.add(new Interval(intr.start, intr.end));
            }
            if (n > 1)
            {
                Collections.sort(sorted, comparator);
            }
        }
        return sorted;
    }
}
