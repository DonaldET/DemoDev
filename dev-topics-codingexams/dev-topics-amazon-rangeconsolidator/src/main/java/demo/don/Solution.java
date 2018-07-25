package demo.don;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Format required by Leet Code
 * 
 * @author Donald Trummell <dtrummell@gmail.com>
 */
public class Solution
{
    public static final class Interval implements Comparable<Solution.Interval>
    {
        public final int start;
        public final int end;

        public Interval(final int start, final int end) {
            this.start = start;
            this.end = end;
        }

        public Interval() {
            this(0, 0);
        }

        @Override
        public int compareTo(final Solution.Interval otherMe)
        {
            if (otherMe == null)
            {
                return -1;
            }

            int diff = this.start - otherMe.start;
            return diff != 0 ? diff : this.end - otherMe.end;
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

            return compareTo((Solution.Interval) obj) == 0;
        }

        @Override
        public String toString()
        {
            return "[" + start + ", " + end + "]";
        }
    }

    public static void main(String[] args)
    {
        System.err.println("iNVOKED");
    }

    public List<Solution.Interval> merge(List<Solution.Interval> intervals)
    {
        final int n = intervals.size();
        final List<Solution.Interval> copyOfOrdered = new ArrayList<Solution.Interval>(n);
        if (n > 0)
        {
            copyOfOrdered.addAll(intervals);
            if (n > 1)
                Collections.sort(copyOfOrdered);
            else
                return copyOfOrdered;

        }
        else
            return copyOfOrdered;

        int lhs_pos = n - 2;
        int rhs_pos = n - 1;

        do
        {
            final Solution.Interval lhs = copyOfOrdered.get(lhs_pos);
            final Solution.Interval rhs = copyOfOrdered.get(rhs_pos);
            if (rhs.start > lhs.end)
            {
                // No overlap
                rhs_pos = lhs_pos;
                lhs_pos--;
            }
            else
            {
                // Overlap
                copyOfOrdered.set(lhs_pos, new Solution.Interval(lhs.start, rhs.end));
                copyOfOrdered.remove(rhs_pos);
                rhs_pos = lhs_pos;
                lhs_pos--;
            }
        }
        while (lhs_pos >= 0);

        return copyOfOrdered;
    }
}
