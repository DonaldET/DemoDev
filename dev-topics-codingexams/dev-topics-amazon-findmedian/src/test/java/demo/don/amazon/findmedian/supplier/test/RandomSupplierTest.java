package demo.don.amazon.findmedian.supplier.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.amazon.findmedian.api.StreamSupplier;
import demo.don.amazon.findmedian.supplier.RandomStreamSupplier;

public class RandomSupplierTest {
	private static final int[] TEST_DATA = { 6, 5, 3, 9, 1, 2, 4, 10, 11 };
	private static final int TEST_CAPACITY = TEST_DATA.length;
	private static StreamSupplier stream = null;

	@Before
	public void setUp() throws Exception {
		stream = new RandomStreamSupplier(TEST_DATA);
	}

	@After
	public void tearDown() throws Exception {
		stream = null;
	}

	@Test
	public void testGetNext() {
		Assert.assertEquals("first getNext differs", 6, stream.getNext());
		Assert.assertEquals("first remaining differs", TEST_CAPACITY - 1, stream.remaining());
		Assert.assertEquals("second getNext differs", 5, stream.getNext());
		Assert.assertEquals("second remaining differs", TEST_CAPACITY - 2, stream.remaining());
	}

	@Test
	public void testSize() {
		Assert.assertEquals("size differs", TEST_CAPACITY, stream.size());
	}

	@Test
	public void testRemaining() {
		Assert.assertEquals("remaining differs", TEST_CAPACITY, stream.remaining());
	}
}
