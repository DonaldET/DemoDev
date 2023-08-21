package demo.en.calls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import demo.en.performance.CallGenerator.Call;

/**
 * Map a date range to an arraya map to accumulate active calls based on time,
 * map uses less space when calls are sparse.
 */
public class SolutionBins implements MaxCallFinder {
	/**
	 * For returning all call information
	 */
	public static class ActiveCall implements Comparable<ActiveCall> {
		public final int time;
		public final int count;

		public ActiveCall(int time, int count) {
			super();
			this.time = time;
			this.count = count;
		}

		@Override
		public int compareTo(ActiveCall o) {
			int d1 = this.time - o.time;
			return d1 == 0 ? this.count - o.count : d1;
		}

		@Override
		public int hashCode() {
			final int prime = 37;
			int result = 1;
			result = prime * result + count;
			result = prime * result + time;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			return compareTo((ActiveCall) obj) == 0;
		}

		@Override
		public String toString() {
			return time + ", " + count;
		}
	}

	public SolutionBins() {
	}

	public static void main(String[] args) {
		final List<Call> calls = new ArrayList<Call>();
		calls.add(new Call(1, 5));
		calls.add(new Call(2, 3));
		calls.add(new Call(7, 10));
		calls.add(new Call(11, 12));
		int expMax = 2;

		MaxCallFinder mcf = new SolutionBins();
		int maxCalls = mcf.getMaxCallCount(calls, 1, 12);
		if (maxCalls != expMax) {
			throw new IllegalStateException("Wrong count: " + maxCalls + "; expected: " + expMax);
		}
	}

	/**
	 * Bin calls over the interval of interest, return bin with largest call count.
	 * No input order requirement.
	 * 
	 * Time: O(n * m); for fixed small m, O(n); Space: O(m);
	 */
	@Override
	public int getMaxCallCount(List<Call> calls, int minRange, int maxRange) {
		//
		// Time: O(1); Space: O(m)

		// -- Allocate bin storage
		final int n = calls.size();
		final int m = maxRange - minRange + 1;
		final int space = Math.min(n, m);
		Map<Integer, Integer> callCounts = new HashMap<Integer, Integer>(Math.min(space, 2048));

		//
		// Time: O(n * m); for fixed small m, O(n)

		// -- Collect bin counts
		for (Call c : calls) {
			for (int time = c.start; time <= c.end; time++) {
				callCounts.merge(time, 1, (x, y) -> x + y);
			}
		}

		//
		// Time: O(m); Space: O(1)

		// -- Find maximum count
		int maxCalls = 0;
		Iterator<Integer> itr = callCounts.values().iterator();
		while (itr.hasNext()) {
			int v = itr.next();
			if (v > maxCalls) {
				maxCalls = v;
			}
		}

		return maxCalls;
	}

	/**
	 * Supporting method for analysis; not part of original problem solution.
	 * Returns maximum call count for a minute in an hour.
	 * 
	 * @param calls
	 * @return array of call counts
	 */
	public int[] getHourlyMaxCallCounts(List<Call> calls) {
		Map<Integer, Integer> callCounts = new HashMap<Integer, Integer>(1024);
		for (Call c : calls) {
			for (int time = c.start; time <= c.end; time++) {
				callCounts.merge(time, 1, (x, y) -> x + y);
			}
		}

		int[] counts = new int[24];
		int lth = callCounts.size();
		if (lth > 0) {
			for (Entry<Integer, Integer> e : callCounts.entrySet()) {
				int t = e.getKey() / 60;
				counts[t] = Math.max(counts[t], e.getValue());
			}
		}

		return counts;
	}
}
