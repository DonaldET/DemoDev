package demo.don.amazon.findmedian.finder;

import java.util.PriorityQueue;

import demo.don.amazon.findmedian.api.MedianFinder;
import demo.don.amazon.findmedian.api.StreamSupplier;

/**
 * Median finder implementation based on using a single PriorityQueue using the
 * default ordering.
 * 
 * @author Donald Trummell
 */
public class SimplePriorityQueueFinder implements MedianFinder {
	private final StreamSupplier supplier;
	private final PriorityQueue<Integer> queue;

	public SimplePriorityQueueFinder(StreamSupplier supplier) {
		this.supplier = supplier;
		this.queue = new PriorityQueue<Integer>(supplier.size());
	}

	@Override
	public String getDescription() {
		return getClass().getSimpleName() + " -- " + supplier.getClass().getSimpleName();
	}

	@Override
	public double getMedian() {
		queue.offer(supplier.getNext());
		int n = queue.size();
		if (n < 2) {
			return queue.peek();
		}

		PriorityQueue<Integer> copy = new PriorityQueue<Integer>(queue);

		int mid = n / 2;
		boolean even = 2 * mid == n;
		int skipCnt = mid;
		if (!even) {
			skipCnt++;
		}

		Integer xmid = -1;
		for (int npoll = 0; npoll < skipCnt; npoll++) {
			xmid = copy.poll();
		}

		if (even) {
			return (xmid + copy.poll()) / 2.0;
		} else {
			return xmid;
		}
	}

	@Override
	public int size() {
		return supplier.size();
	}

	@Override
	public String toString() {
		StringBuilder msg = new StringBuilder("{");
		msg.append(getDescription());
		int n = queue.size();
		msg.append(";  n: " + n);
		msg.append(";  remaining: " + supplier.remaining());
		msg.append("}");
		if (queue.isEmpty()) {
			msg.append(listQueue(queue));
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
