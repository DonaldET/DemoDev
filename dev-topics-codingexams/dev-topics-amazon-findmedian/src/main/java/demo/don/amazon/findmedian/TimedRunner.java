package demo.don.amazon.findmedian;

import demo.don.amazon.findmedian.api.MedianFinder;
import demo.don.amazon.findmedian.api.StreamSupplier;
import demo.don.amazon.findmedian.finder.DualPriorityQueueFinder;
import demo.don.amazon.findmedian.finder.SimplePriorityQueueFinder;
import demo.don.amazon.findmedian.supplier.RandomStreamSupplier;

/**
 * Quick execution time checker
 * 
 * @author Donald Trummell
 */
public class TimedRunner {
	private static final int MAX_SIZE = MedianFinder.DEFAULT_MAX_FINDER;
	private static final int UPPER_BOUND = 1000;
	private static final int[] testCapacities = { 10, 15, 25, 50, 75, 100, 150, 200, 500, 1000 };
	private static final int[] masterTestData = new RandomStreamSupplier(new int[] { 1, 2, 3 }).getRandomArray(MAX_SIZE,
			UPPER_BOUND);

	public TimedRunner() {
	}

	private static long runSimpleQueueTest(int capacity) {
		int[] testData = new int[capacity];
		System.arraycopy(masterTestData, 0, testData, 0, capacity);
		StreamSupplier stream = new RandomStreamSupplier(testData);
		MedianFinder finder = new SimplePriorityQueueFinder(stream);

		final long start = System.nanoTime();
		for (int i = 0; i < capacity; i++) {
			finder.getMedian();
		}
		final long elapsed = System.nanoTime() - start;

		return elapsed;
	}

	private static long runDualQueueTest(int capacity) {
		int[] testData = new int[capacity];
		System.arraycopy(masterTestData, 0, testData, 0, capacity);
		StreamSupplier stream = new RandomStreamSupplier(testData);
		MedianFinder finder = new DualPriorityQueueFinder(stream);

		final long start = System.nanoTime();
		for (int i = 0; i < capacity; i++) {
			finder.getMedian();
		}
		final long elapsed = System.nanoTime() - start;

		return elapsed;
	}

	public static void main(String[] args) {
		final double NANO_MILLI_SECONDS = 1000000.0;
		System.out.println("\n*** Time Median implementations for random input - execution time in milli-seconds.");
		for (int i = 0; i < testCapacities.length; i++) {
			int testSize = testCapacities[i];
			System.out.print("Test[" + i + "]; N: " + testSize + ";  ");
			long elapsed = runSimpleQueueTest(testSize);
			System.out.print("  Simple: " + elapsed / NANO_MILLI_SECONDS);
			elapsed = runDualQueueTest(testSize);
			System.out.println(";  Dual: " + elapsed / NANO_MILLI_SECONDS);
		}
		System.out.println("*** Done");
	}
}
