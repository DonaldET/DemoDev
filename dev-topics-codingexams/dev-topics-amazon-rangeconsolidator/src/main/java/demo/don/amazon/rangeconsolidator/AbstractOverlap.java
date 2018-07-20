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
        final List<Interval> sorted = new ArrayList<Interval>();
        sorted.addAll(unsorted);
        Collections.sort(sorted);
        return sorted;
    }
}
