package demo.don.amazon.rangeconsolidator;

import java.util.Comparator;
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
     * An interval has a low-high pair and is ordered for searching (first by
     * low, then by high.) Note that this class must be mutable for the Leet
     * Code solution to work, and had been adapted by the other solutions.
     * 
     * @author Donald Trummell (dtrummell@gmail.com)
     */
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

    public abstract Merger merge(final List<Interval> intervals, final Comparator<Interval> comparator);
}