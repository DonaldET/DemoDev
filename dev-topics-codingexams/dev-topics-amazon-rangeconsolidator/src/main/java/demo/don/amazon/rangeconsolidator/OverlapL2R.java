package demo.don.amazon.rangeconsolidator;

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
public class OverlapL2R extends AbstractOverlap implements Overlap
{
    public OverlapL2R() {
        super();
    }

    /**
     * Merge overlapping ranges, each range a low-high pair.
     * 
     * @see demo.don.amazon.rangeconsolidator.Overlap#merge(java.util.List)
     */
    @Override
    public Merger merge(final List<Interval> intervals)
    {
        if (intervals == null)
        {
            throw new IllegalArgumentException("intervals null");
        }
        final List<Interval> copyOfOrdered = sortIntervals(intervals);
        int n = copyOfOrdered.size();
        if (n < 2)
        {
            return new Merger(0, copyOfOrdered);
        }

        int merges = 0;
        int curnt_pos = 0;
        int nxt_pos = 1;

        do
        {
            final Interval curnt = copyOfOrdered.get(curnt_pos);
            final Interval nxt = copyOfOrdered.get(nxt_pos);
            if (nxt.low > curnt.hi)
            {
                // No overlap
                curnt_pos = nxt_pos;
                nxt_pos++;
            }
            else
            {
                // Overlap
                copyOfOrdered.set(nxt_pos, new Interval(curnt.low, nxt.hi));
                copyOfOrdered.remove(curnt_pos);
                n -= 1;
                merges++;
            }
        }
        while (nxt_pos < n);

        return new Merger(merges, copyOfOrdered);
    }
}
