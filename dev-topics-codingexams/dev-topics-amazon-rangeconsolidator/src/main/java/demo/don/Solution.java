package demo.don;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Formatted (almost) as required for Leet Code submission (rank 53% with Collections.remove, 68% with manual remove)
 *
 * @author Donald Trummell  (dtrummell@gmail.com)
 */
public class Solution
{
    public static final class Interval
    {
        public int start;
        public int end;

        public Interval(final int start, final int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + end;
            result = prime * result + start;
            return result;
        }

        @Override
        public boolean equals(final Object obj)
        {
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
        public String toString()
        {
            return "[" + start + ", " + end + "]";
        }
    }

    private static final class SolutionMergeComparator implements Comparator<Solution.Interval>
    {
        @Override
        public int compare(final Solution.Interval a, final Solution.Interval b)
        {
            return a.start < b.start ? -1 : (a.start > b.start ? 1 : (a.end < b.end ? -1 : (a.end > b.end ? 1 : 0)));
        }
    }

    public List<Solution.Interval> merge(final List<Solution.Interval> intervals)
    {
        final List<Solution.Interval> copyOfOrdered = sortIntervals(intervals, new SolutionMergeComparator());
        final int n = copyOfOrdered.size();
        if (n < 2)
        {
            return copyOfOrdered;
        }

        int lhs_pos = 0;
        int rhs_pos = 1;

        do
        {
            final Solution.Interval lhs = copyOfOrdered.get(lhs_pos);
            final Solution.Interval rhs = copyOfOrdered.get(rhs_pos);
            if (rhs.start <= lhs.end)
            {
                // Overlap
                rhs.start = lhs.start;
                rhs.end = Math.max(lhs.end, rhs.end);
                copyOfOrdered.set(lhs_pos, null);
            }
            lhs_pos = rhs_pos;
            rhs_pos++;
        }
        while (rhs_pos < n);

        copyOfOrdered.removeAll(Collections.singleton(null));

        return copyOfOrdered;
    }

    private List<Solution.Interval> sortIntervals(final List<Solution.Interval> unsorted,
            final Comparator<Interval> comparator)
    {
        final int n = unsorted.size();
        final List<Solution.Interval> sorted = new ArrayList<Solution.Interval>(n);
        if (n > 0)
        {
            for (final Solution.Interval intr : unsorted)
            {
                sorted.add(new Solution.Interval(intr.start, intr.end));
            }
            if (n > 1)
            {
                Collections.sort(sorted, comparator);
            }
        }
        return sorted;
    }
}
