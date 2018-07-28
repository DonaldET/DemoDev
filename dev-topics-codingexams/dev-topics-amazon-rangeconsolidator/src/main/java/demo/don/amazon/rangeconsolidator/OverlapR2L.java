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
 * <strong>Note:</strong>this version merges right to left.
 * 
 * <a href=
 * "https://livecode.amazon.jobs/session/04d9e66e-ce1a-42fd-8854-cb9c1c800268">Built
 * for Amazon here</a>, but taken from <a href=
 * "https://leetcode.com/problems/merge-intervals/description/">LeetCode</a>.
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class OverlapR2L extends AbstractOverlap implements Overlap
{
    public OverlapR2L() {
        super();
    }

    /**
     * Merge overlapping ranges, each range a low-high pair.
     * 
     * @see demo.don.amazon.rangeconsolidator.Overlap#merge(java.util.List)
     */
    @Override
    public Merger merge(final List<Interval> intervals, final Comparator<Interval> comparator)
    {
        if (intervals == null)
        {
            throw new IllegalArgumentException("intervals null");
        }
        final List<Interval> copyOfOrdered = sortIntervals(intervals,
                comparator == null ? new AbstractOverlap.MergeComparator() : comparator);
        int n = copyOfOrdered.size();
        if (n < 2)
        {
            return new Merger(0, copyOfOrdered);
        }

        int merges = 0;
        int lhs_pos = n - 2;
        int rhs_pos = n - 1;

        do
        {
            final Interval lhs = copyOfOrdered.get(lhs_pos);
            final Interval rhs = copyOfOrdered.get(rhs_pos);
            if (rhs.low > lhs.hi)
            {
                // No overlap
                rhs_pos = lhs_pos;
                lhs_pos--;
            }
            else
            {
                // Overlap
                lhs.hi = rhs.hi;
                copyOfOrdered.remove(rhs_pos);
                merges++;
                rhs_pos = lhs_pos;
                lhs_pos--;
            }
        }
        while (lhs_pos >= 0);

        return new Merger(merges, copyOfOrdered);
    }
}
