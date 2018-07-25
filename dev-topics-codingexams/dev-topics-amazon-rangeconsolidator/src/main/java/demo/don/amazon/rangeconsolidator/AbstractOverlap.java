package demo.don.amazon.rangeconsolidator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractOverlap implements Overlap
{
    public AbstractOverlap() {
    }

    public List<Interval> sortIntervals(final List<Interval> unsorted)
    {
        final int n = unsorted.size();
        final List<Interval> sorted = new ArrayList<Interval>(n);
        if (n > 0)
        {
            sorted.addAll(unsorted);
            if (n > 1)
            {
                Collections.sort(sorted);
            }
        }
        return sorted;
    }
}
