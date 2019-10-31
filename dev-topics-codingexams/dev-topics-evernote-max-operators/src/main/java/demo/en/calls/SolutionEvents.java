package demo.en.calls;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import demo.en.performance.CallFinderEvaluator;
import demo.en.performance.CallGenerator;
import demo.en.performance.CallGenerator.Call;

public class SolutionEvents implements MaxCallFinder {

	public SolutionEvents() {
	}

	//
	// *** Array List ***
	// Events: Find max simultaneous calls in 1440 minutes that captured 750000
	// calls while tracking active calls
	// ...
	// Max calls = 20019 in 77.457 seconds.
	//
	// *** Linked List ***
	// Events: Find max simultaneous calls in 1440 minutes that captured 750000
	// calls while tracking active calls
	// ...
	// Max calls = 20019 in 74.564 seconds.
	//
	public static void main(String[] args) {
		final int n = 750_000;
		final int m = CallFinderEvaluator.rangeEnd - CallFinderEvaluator.rangeStart + 1;
		System.out.println("Events: Find max simultaneous calls in " + m + " minutes that captured " + n
				+ " calls while tracking active calls");
		CallGenerator cg = new CallGenerator(CallFinderEvaluator.rangeStart, CallFinderEvaluator.rangeEnd,
				CallFinderEvaluator.peaks, CallFinderEvaluator.durationMin, CallFinderEvaluator.durationMax);
		final List<Call> calls = cg.generate(n);
		Collections.sort(calls);

		SolutionEvents sb = new SolutionEvents();
		System.out.println("...");
		long startTm = System.nanoTime();
		final int maxCalls = sb.getMaxCallCount(calls, CallFinderEvaluator.rangeStart, CallFinderEvaluator.rangeEnd);
		long elapsed = System.nanoTime() - startTm;
		String msg = String.format("Max calls = %d in %.3f seconds.", maxCalls,
				elapsed / CallFinderEvaluator.NANO_TO_SECOND);
		System.out.println(msg);
	}

	/**
	 * Track active calls over the interval of interest, return with largest call
	 * count. Requires sorted input.
	 * 
	 * Time: O(n * Max-Active-Calls); Space: O(Max-Active-Calls);
	 */
	@Override
	public int getMaxCallCount(List<Call> calls, int minRange, int maxRange) {
		//
		// Time: O(1); Space: O(Max-Active-Calls)

		// -- Allocate active call storage
		int maxCalls = 0;
		List<Call> active = new LinkedList<Call>();

		//
		// Time: O(n * Max-Active-Calls); Space: O(Max-Active-Calls)

		// -- Add new active call, evict inactive calls (end-time >= newest start time
		for (Call c : calls) {
			ListIterator<Call> iter = active.listIterator();
			while (iter.hasNext()) {
				if (iter.next().end < c.start) {
					iter.remove();
				}
			}
			active.add(c);
			maxCalls = Math.max(active.size(), maxCalls);
		}

		return maxCalls;
	}
}
