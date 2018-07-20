package demo.don.amazon.rangeconsolidator;

import java.util.List;

/**
 * Problem taken from <a href=
 * "https://leetcode.com/problems/merge-intervals/description/">LeetCode</a>.
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface Overlap
{
    public static final class Merger
    {
        public final int merges;
        public final List<Interval> merged;

        public Merger(int merges, List<Interval> merged) {
            super();
            this.merges = merges;
            this.merged = merged;
        }

        @Override
        public String toString()
        {
            return "Merger -- merges=" + merges + ", merged=" + merged;
        }
    }

    /**
     * An interval has a low-high pair and knows how to self order (first by
     * low, then by high.)
     * 
     * @author Donald Trummell (dtrummell@gmail.com)
     */
    public static final class Interval implements Comparable<Interval>
    {
        public final int low;
        public final int hi;

        public Interval(final int low, final int hi) {
            this.low = low;
            this.hi = hi;
        }

        @Override
        public int compareTo(final Interval otherMe)
        {
            if (otherMe == null)
            {
                return -1;
            }

            int diff = this.low - otherMe.low;
            return diff != 0 ? diff : this.hi - otherMe.hi;
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

            return compareTo((Interval) obj) == 0;
        }

        @Override
        public String toString()
        {
            return "[" + low + ", " + hi + "]";
        }
    }

    public abstract Merger merge(final List<Interval> intervals);
}