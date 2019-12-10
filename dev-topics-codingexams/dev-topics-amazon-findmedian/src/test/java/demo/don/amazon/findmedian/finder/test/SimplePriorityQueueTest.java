package demo.don.amazon.findmedian.finder.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.amazon.findmedian.finder.SimplePriorityQueueFinder;
import demo.don.amazon.findmedian.supplier.RandomStreamSupplier;

public class SimplePriorityQueueTest {
	private static final int[] TEST_DATA_EVEN = { 6, 5, 3, 9, 1, 2, 4, 10, 8, 7 };
	private static final int[] TEST_DATA_ODD = { 6, 5, 3, 9, 1, 2, 4, 10, 8, 7, 11 };
	private static final int TEST_CAPACITY = TEST_DATA_EVEN.length;
	private SimplePriorityQueueFinder finder = null;

	@Before
	public void setUp() throws Exception {
		finder = new SimplePriorityQueueFinder(new RandomStreamSupplier(TEST_DATA_EVEN));
	}

	@After
	public void tearDown() throws Exception {
		finder = null;
	}

	@Test
	public void testGetDescription() {
		Assert.assertEquals("Description differs", "SimplePriorityQueueFinder -- RandomStreamSupplier",
				finder.getDescription());
	}

	@Test
	public void testGetMedianRandomEven() {
		double[] medians = { 6.0, 5.5, 5.0, 5.5, 5.0, 4.0, 4.0, 4.5, 5.0, 5.5 };
		for (int i = 0; i < TEST_CAPACITY; i++) {
			double actual = finder.getMedian();
			Assert.assertEquals("Even median[" + i + "] differs", medians[i], actual, 0.000000001);
		}
	}

	@Test
	public void testGetMedianRandomOdd() {
		SimplePriorityQueueFinder finder2 = new SimplePriorityQueueFinder(new RandomStreamSupplier(TEST_DATA_ODD));
		double[] medians = { 6.0, 5.5, 5.0, 5.5, 5.0, 4.0, 4.0, 4.5, 5.0, 5.5, 6.0 };
		for (int i = 0; i < TEST_DATA_ODD.length; i++) {
			double actual = finder2.getMedian();
			Assert.assertEquals("Odd median[" + i + "] differs", medians[i], actual, 0.000000001);
		}
	}

	@Test
	public void testGetMedianAscendingEven() {
		int[] testCopy = TEST_DATA_EVEN.clone();
		Arrays.sort(testCopy);
		SimplePriorityQueueFinder finder2 = new SimplePriorityQueueFinder(new RandomStreamSupplier(testCopy));
		double[] medians = { 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5 };
		for (int i = 0; i < testCopy.length; i++) {
			double actual = finder2.getMedian();
			Assert.assertEquals("Even median ASC[" + i + "] differs", medians[i], actual, 0.000000001);
		}
	}

	@Test
	public void testGetMedianAscendingOdd() {
		int[] testCopy = TEST_DATA_ODD.clone();
		Arrays.sort(testCopy);
		SimplePriorityQueueFinder finder2 = new SimplePriorityQueueFinder(new RandomStreamSupplier(testCopy));
		double[] medians = { 1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0 };
		for (int i = 0; i < testCopy.length; i++) {
			double actual = finder2.getMedian();
			Assert.assertEquals("Odd median ASC[" + i + "] differs", medians[i], actual, 0.000000001);
		}
	}

	@Test
	public void testGetMedianDescendingEven() {
		List<Integer> testDataList = Arrays.stream(TEST_DATA_EVEN.clone()).boxed().collect(Collectors.toList());
		Collections.sort(testDataList, (x, y) -> y - x);
		int n = testDataList.size();
		int[] testCopy = new int[n];
		for (int i = 0; i < n; i++) {
			testCopy[i] = testDataList.get(i);
		}

		SimplePriorityQueueFinder finder2 = new SimplePriorityQueueFinder(new RandomStreamSupplier(testCopy));
		double[] medians = { 10.0, 9.5, 9.0, 8.5, 8.0, 7.5, 7.0, 6.5, 6.0, 5.5 };
		for (int i = 0; i < testCopy.length; i++) {
			double actual = finder2.getMedian();
			Assert.assertEquals("Even median DESC[" + i + "] differs", medians[i], actual, 0.000000001);
		}
	}

	@Test
	public void testGetMedianDescendingOdd() {
		List<Integer> testDataList = Arrays.stream(TEST_DATA_ODD.clone()).boxed().collect(Collectors.toList());
		Collections.sort(testDataList, (x, y) -> y - x);
		int n = testDataList.size();
		int[] testCopy = new int[n];
		for (int i = 0; i < n; i++) {
			testCopy[i] = testDataList.get(i);
		}

		SimplePriorityQueueFinder finder2 = new SimplePriorityQueueFinder(new RandomStreamSupplier(testCopy));
		double[] medians = { 11.0, 10.5, 10.0, 9.5, 9.0, 8.5, 8.0, 7.5, 7.0, 6.5, 6.0 };
		for (int i = 0; i < testCopy.length; i++) {
			double actual = finder2.getMedian();
			Assert.assertEquals("Odd median DESC[" + i + "] differs", medians[i], actual, 0.000000001);
		}
	}

	@Test
	public void testGetMedianSameEven() {
		int[] testCopy = new int[] { 9, 9, 9, 9, 9, 9 };
		SimplePriorityQueueFinder finder2 = new SimplePriorityQueueFinder(new RandomStreamSupplier(testCopy));
		double[] medians = { 9.0, 9.0, 9.0, 9.0, 9.0, 9.0 };
		for (int i = 0; i < testCopy.length; i++) {
			double actual = finder2.getMedian();
			Assert.assertEquals("Even median Same[" + i + "] differs", medians[i], actual, 0.000000001);
		}
	}

	@Test
	public void testGetMedianSamegOdd() {
		int[] testCopy = new int[] { 9, 9, 9, 9, 9, 9, 9 };
		SimplePriorityQueueFinder finder2 = new SimplePriorityQueueFinder(new RandomStreamSupplier(testCopy));
		double[] medians = { 9.0, 9.0, 9.0, 9.0, 9.0, 9.0, 9.0 };
		for (int i = 0; i < testCopy.length; i++) {
			double actual = finder2.getMedian();
			Assert.assertEquals("Odd median Same[" + i + "] differs", medians[i], actual, 0.000000001);
		}
	}

	@Test
	public void testSize() {
		Assert.assertEquals("size differs", TEST_CAPACITY, finder.size());
	}
}
