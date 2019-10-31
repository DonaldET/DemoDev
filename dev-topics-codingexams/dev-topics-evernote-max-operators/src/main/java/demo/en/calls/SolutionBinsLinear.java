package demo.en.calls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import demo.en.performance.CallGenerator.Call;

/**
 * Directly map a date range to an array to accumulate active calls based on
 * time.
 */
public class SolutionBinsLinear implements MaxCallFinder {

	public SolutionBinsLinear() {
	}

	public static void main(String[] args) {
		final List<Call> calls = new ArrayList<Call>();
		calls.add(new Call(1, 5));
		calls.add(new Call(2, 3));
		calls.add(new Call(7, 10));
		calls.add(new Call(11, 12));
		int expMax = 2;

		MaxCallFinder mcf = new SolutionBinsLinear();
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
		final int m = maxRange - minRange + 1;
		int[] callCounts = new int[m];

		//
		// Time: O(n * m); for fixed small m, O(n)

		// -- Collect bin counts
		for (Call c : calls) {
			for (int time = c.start; time <= c.end; time++) {
				callCounts[time - minRange]++;
			}
		}

		//
		// Time: O(m); Space: O(1)

		// -- Find maximum count

		return Arrays.stream(callCounts).max().getAsInt();
	}
}
