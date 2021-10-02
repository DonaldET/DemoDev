package demo.don.amazon.findmedian.finder;

import java.util.Collections;
import java.util.PriorityQueue;

import demo.don.amazon.findmedian.api.MedianFinder;
import demo.don.amazon.findmedian.api.StreamSupplier;

/**
 * A Median finder implementation based on using two PriorityQueue instances,
 * here called lower and upper. The queues hold the lower and upper half of the
 * input stream. The largest entry in the lower queue is less than than the
 * smallest entry in the upper queue.
 * <p>
 * The lower queue is ordered such that the <em>top</em> of the queue is the
 * largest value (reversed from the default queue ordering.) The upper queue is
 * ordered such that the <em>top</em> of the queue is the smallest value in the
 * queue (the default queue ordering.)
 * <p>
 * The algorithm requires the lower and upper queues to be balanced. The is, if
 * the total number of elements in both queues is even, then they should hold an
 * equal number of elements. If the total number of elements in both queues is
 * odd, then one queue holds the extra element.
 * <p>
 * In general, a new element is added to one of the queues as follows:
 * </p>
 * <p>
 * <Strong>case</strong> <em>Top elements of both queues differ:</em>
 * <ul>
 * <li>if the new element is not greater than the top value of the lower queue,
 * then add to the lower queue; else</li>
 * <li>the new element is not less than the top value of the upper queue, then
 * add to the upper queue; else</li>
 * <li>add to the queue with the fewest elements</li>
 * </ul>
 * <p>
 * <Strong>case</strong> <em>Top elements of both queues are equal:</em>
 * <ul>
 * <li>if the new element is less than the common <em>top</em> value, then add
 * to the lower queue; else</li>
 * <li>if the new element is greater than the common top value, then add to the
 * upper queue; else</li>
 * <li>add to the queue with the fewest elements</li>
 * </ul>
 * <p>
 * <strong>Re-balancing</strong>
 * <p>
 * After the new element is added to one of the queues, the queues are checked
 * for balance as explained above. If the queues are not balanced, then the
 * larger queue will be too large by one element. There are two cases:
 * <ol>
 * <li><strong>case</strong> <em>lower queue large</em>: move top (largest) to
 * upper queue</li>
 * <li><strong>case</strong> <em>upper queue large</em>: move top (smallest) to
 * lower queue</li>
 * </ol>
 * <strong>The Median</strong>
 * <p>
 * The median is easily obtained from the top values(s) of the two queues. There
 * are again two cases:
 * <ol>
 * <li><strong>case</strong> <em>total number of entries is odd</em>, then the
 * <em>top</em> value of the largest queue is the median; else</li>
 * <li><strong>case</strong> <em>total number of entries is even </em>, then the
 * median is the average of the two <em>top</em> values.</li>
 * </ol>
 * This algorithm removes the need to iterate through a single priority queue to
 * obtain the element(s) required to compute the median.
 * <p>
 * 
 * @author Donald Trummell
 */
public class DualPriorityQueueFinder implements MedianFinder {
	private final StreamSupplier supplier;
	private final PriorityQueue<Integer> upper;
	private final PriorityQueue<Integer> lower;

	public DualPriorityQueueFinder(StreamSupplier supplier) {
		this.supplier = supplier;
		int n = supplier.size();
		int nhalf = (n + 1) / 2;
		this.upper = new PriorityQueue<Integer>(nhalf + 1);
		this.lower = new PriorityQueue<Integer>(nhalf + 1, Collections.reverseOrder());
	}

	@Override
	public String getDescription() {
		return getClass().getSimpleName() + " -- " + supplier.getClass().getSimpleName();
	}

	@Override
	public double getMedian() {
		int element = supplier.getNext();

		Double median = addToEmptyQueue(element);
		if (!Double.isNaN(median))
			return median;

		if (lower.peek() == upper.peek()) {
			addToEqualTopElementQueues(element);
		} else {
			addToDifferentTopElementQueues(element);
		}

		//
		// Re-balance queues so they are equal size

		int nlower = lower.size();
		int nupper = upper.size();
		int delta = Math.abs(nupper - nlower);
		if (delta > 2) {
			throw new IllegalStateException("lower length(" + nlower + ") differs from upper (" + nupper + ") by "
					+ delta + ", which exceeds 2");
		} else if (delta > 1) {
			if (nlower < nupper) {
				// move upper to lower
				lower.offer(upper.poll());
			} else {
				// move lower to upper
				upper.offer(lower.poll());
			}
		}

		return computeMedianFromQueues();
	}

	/**
	 * Only add to a queue if either lower or upper queue is empty.
	 * 
	 * @param element the element to add to a queue
	 * @return NaN if nothing was added, or the appropriated median.
	 */
	private Double addToEmptyQueue(Integer element) {
		Integer lowerTop = lower.peek();
		Integer upperTop = upper.peek();

		boolean isEmpty = false;
		if (upperTop == null) {
			upper.offer(element);
			upperTop = element;
			isEmpty = true;
		} else if (lowerTop == null) {
			lower.offer(element);
			lowerTop = element;
			isEmpty = true;
		}

		//
		// No empty queue processing required
		if (!isEmpty) {
			return Double.NaN;
		}

		Double median = null;
		if (lowerTop == null) {
			median = (double) upperTop;
		} else if (upperTop == null) {
			median = (double) lowerTop;
		} else {
			median = computeMedianFromQueues();
		}

		//
		// Adjust to place smaller element into lower queue by swapping
		if (upperTop == null || (lowerTop != null && lowerTop > upperTop)) {
			lower.remove();
			lower.offer(upperTop);
			upper.remove();
			upper.offer(lowerTop);
		}

		return median;
	}

	/**
	 * Queues are equal at top, so select favoring lower queue
	 * 
	 * @param element the element to add to a queue
	 * @return the median
	 */
	private void addToEqualTopElementQueues(Integer element) {
		Integer lowerTop = lower.peek();
		Integer upperTop = upper.peek();
		if (lowerTop != upperTop) {
			throw new IllegalStateException("lower (top: " + lowerTop + ") NOT equal to upper (top: " + upperTop + ")");
		}

		if (element <= lowerTop) {
			if (lower.size() <= upper.size()) {
				lower.offer(element);
			} else {
				upper.offer(element);
			}
		}
	}

	/**
	 * Queues differ at the top, so through into the correct queue, unless element
	 * is between top values, then throw into the smaller queue.
	 * 
	 * @param element the element to add to a queue
	 * @return the median
	 */
	private void addToDifferentTopElementQueues(Integer element) {
		Integer lowerTop = lower.peek();
		Integer upperTop = upper.peek();
		if (lowerTop == upperTop) {
			throw new IllegalStateException("lower (top: " + lowerTop + ") NOT equal to upper (top: " + upperTop + ")");
		}

		if (element <= lowerTop) {
			// into lower
			lower.offer(element);
		} else if (element >= upperTop) {
			upper.offer(element);
		} else {
			// into smaller length queue
			if (lower.size() <= upper.size()) {
				lower.offer(element);
			} else {
				upper.offer(element);
			}
		}
	}

	/**
	 * Compute the median as the single middle value if combined length is odd (and
	 * that is the top of the largest queue), otherwise average the top values of
	 * the two queues.
	 * 
	 * @return the median
	 */
	private Double computeMedianFromQueues() {
		Integer lowerTop = lower.peek();
		Integer upperTop = upper.peek();
		int lowerLth = lower.size();
		int upperLth = upper.size();
		double median = -1.0;
		if (lowerLth == upperLth) {
			median = (lowerTop + upperTop) / 2.0;
		} else {
			median = ((lowerLth > upperLth) ? lowerTop.intValue() : upperTop.intValue());
		}

		return median;
	}

	@Override
	public int size() {
		return supplier.size();
	}

	@Override
	public String toString() {
		StringBuilder msg = new StringBuilder("{");
		msg.append(getDescription());
		msg.append(";  combined n: " + (lower.size() + upper.size()));
		msg.append(";  remaining: " + supplier.remaining());
		msg.append("}");
		if (!lower.isEmpty()) {
			msg.append("\n  -- lower[" + lower.size() + "]:");
			msg.append(listQueue(lower));
		}
		if (!upper.isEmpty()) {
			msg.append("\n  -- upper[" + upper.size() + "]:");
			msg.append(listQueue(upper));
		}
		return msg.toString();
	}

	private String listQueue(PriorityQueue<Integer> queue) {
		int n = queue.size();
		if (n < 1) {
			return "";
		}

		StringBuilder msg = new StringBuilder();
		PriorityQueue<Integer> copy = new PriorityQueue<Integer>(queue);
		for (int i = 0; i < n; i++) {
			msg.append("\n  ");
			msg.append(i);
			msg.append(".  ");
			msg.append(copy.poll());
		}

		return msg.toString();
	}
}
