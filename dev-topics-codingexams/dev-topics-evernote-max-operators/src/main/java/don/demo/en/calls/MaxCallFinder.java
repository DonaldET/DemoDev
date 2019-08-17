package don.demo.en.calls;

import java.util.List;

import don.demo.en.performance.CallGenerator.Call;

/**
 * Scan the call list and determine the maximum number of active calls.
 */
public interface MaxCallFinder {
	/**
	 * Find maximum number of active calls in list.
	 * 
	 * @param calls    - a non-null possibly empty list of calls.
	 * @param minRange minimum start time in range.
	 * @param maxRange maximum start time in range.
	 * 
	 * @return maximum number of active calls.
	 */
	public abstract int getMaxCallCount(List<Call> calls, int minRange, int maxRange);
}