package don.demo.en.calls;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import don.demo.en.performance.CallGenerator.Call;

public class SolutionEventsQueue implements MaxCallFinder {

	public SolutionEventsQueue() {
	}

	public static void main(String[] args) {
		final List<Call> calls = new ArrayList<Call>();
		calls.add(new Call(1, 5));
		calls.add(new Call(2, 3));
		calls.add(new Call(7, 10));
		calls.add(new Call(11, 12));
		int expMax = 2;

		MaxCallFinder mcf = new SolutionEventsQueue();
		int maxCalls = mcf.getMaxCallCount(calls, 1, 12);
		if (maxCalls != expMax) {
			throw new IllegalStateException("Wrong count: " + maxCalls + "; expected: " + expMax);
		}
	}

	/**
	 * Track active calls over the interval of interest, return with largest call
	 * count. Requires sorted input.
	 * 
	 * Time: O(n); Space: O(Max-Active-Calls);
	 */
	public int getMaxCallCount(List<Call> calls, int minRange, int maxRange) {
		//
		// Time: O(1); Space: O(Max-Active-Calls)

		// -- Allocate active call storage
		PriorityQueue<Integer> active = new PriorityQueue<Integer>(2048);

		//
		// Time: O(n * O(Max-Active-Calls * log(Max-Active-Calls))); for fixed small
		// Max-Active-Calls, o(N); Space: O(Max-Active-Calls)
		//
		// From JavaDocs: The PriorityQueue implementation provides O(log(n))
		// time for the enqueuing and dequeuing methods(offer, poll, remove() and
		// add); linear time for the remove(Object) and contains(Object) methods; and
		// constant time for the retrieval methods(peek, element, and size).

		// -- Add new active call, evict inactive calls (end-time >= newest start time
		int maxCalls = 0;
		for (Call c : calls) {
			for (Integer head = active.peek(); head != null && head < c.start; head = active.peek()) {
				active.poll(); // O(Max-Active-Calls * log(Max-Active-Calls))
			}
			active.offer(c.end); // O(Max-Active-Calls * log(Max-Active-Calls))
			maxCalls = Math.max(active.size(), maxCalls);
		}

		return maxCalls;
	}
}
