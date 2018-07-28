package demo.don;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Formated as required by Leet Code
 * 
 * @author Donald Trummell <dtrummell@gmail.com>
 */
public class Solution
{
    public static final class Interval
    {
        public int low;
        public int hi;

        public Interval(final int low, final int hi) {
            this.low = low;
            this.hi = hi;
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + hi;
            result = prime * result + low;
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

            return this.low == other.low ? (this.hi == other.hi ? true : false) : false;
        }

        @Override
        public String toString()
        {
            return "[" + low + ", " + hi + "]";
        }
    }

    private static final class SolutionMergeComparator implements Comparator<Solution.Interval>
    {
        @Override
        public int compare(final Solution.Interval a, final Solution.Interval b)
        {
            return a.low < b.low ? -1 : (a.low > b.low ? 1 : (a.hi < b.hi ? -1 : (a.hi > b.hi ? 1 : 0)));
        }
    }

    public static void main(String[] args)
    {
        System.err.println("Invoked");
    }

    public List<Solution.Interval> merge(final List<Solution.Interval> intervals)
    {
        final List<Solution.Interval> copyOfOrdered = sortIntervals(intervals, new SolutionMergeComparator());
        int n = copyOfOrdered.size();
        if (n < 2)
        {
            return copyOfOrdered;
        }

        int lhs_pos = n - 2;
        int rhs_pos = n - 1;

        do
        {
            final Solution.Interval lhs = copyOfOrdered.get(lhs_pos);
            final Solution.Interval rhs = copyOfOrdered.get(rhs_pos);
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
                rhs_pos = lhs_pos;
                lhs_pos--;
            }
        }
        while (lhs_pos >= 0);

        return copyOfOrdered;
    }

    private List<Solution.Interval> sortIntervals(final List<Solution.Interval> unsorted,
            final Comparator<Solution.Interval> comparator)
    {
        final int n = unsorted.size();
        final List<Solution.Interval> sorted = new ArrayList<Solution.Interval>(n);
        if (n > 0)
        {
            sorted.addAll(unsorted);
            if (n > 1)
            {
                Collections.sort(sorted, comparator);
            }
        }
        return sorted;
    }
}
