package don.demo.algo.sieve.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import don.demo.algo.sieve.Eratosthenes;

/**
 * Unit test for simple App.
 */
public class EratosthenesTest {

	@Test(expected = AssertionError.class)
	public void testSieveBadSize() {
		int n = 0;
		int start = 3;
		Eratosthenes.findValues(n, start);
	}

	@Test(expected = AssertionError.class)
	public void testSieveBadStart() {
		int n = 1;
		int start = 0;
		Eratosthenes.findValues(n, start);
	}

	@Test
	public void testSieveSmall() {
		int n = 1;
		int start = 3;
		List<Integer> values = Eratosthenes.findValues(n, start);
		Assert.assertNotNull("values null for n=" + n + ", start=" + start, values);

		final List<Integer> expected = Arrays.asList(3);
		Assert.assertEquals("values count wrong for n=" + n + ", start=" + start, expected.size(), values.size());
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertEquals("value[" + i + "] differs for n=" + n + ", start=" + start, expected, values);
		}
	}

	@Test
	public void testSieveEven() {
		int n = 1;
		int start = 2;
		List<Integer> values = Eratosthenes.findValues(n, start);
		Assert.assertNotNull("values null for n=" + n + ", start=" + start, values);

		final List<Integer> expected = Arrays.asList(3);
		Assert.assertEquals("values count wrong for n=" + n + ", start=" + start, expected.size(), values.size());
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertEquals("value[" + i + "] differs for n=" + n + ", start=" + start, expected, values);
		}
	}

	@Test
	public void testSieveStartEQL3() {
		int n = 4;
		int start = 3;
		List<Integer> values = Eratosthenes.findValues(n, start);
		Assert.assertNotNull("values null for n=" + n + ", start=" + start, values);

		final List<Integer> expected = Arrays.asList(3, 5, 7);
		Assert.assertEquals("values count wrong for n=" + n + ", start=" + start, expected.size(), values.size());
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertEquals("value[" + i + "] differs for n=" + n + ", start=" + start, expected, values);
		}
	}

	@Test
	public void testSieveStartEQL3Big() {
		int n = 12;
		int start = 3;
		List<Integer> values = Eratosthenes.findValues(n, start);
		Assert.assertNotNull("values null for n=" + n + ", start=" + start, values);

		final List<Integer> expected = Arrays.asList(3, 5, 7, 11, 13, 17, 19, 23);
		Assert.assertEquals("values count wrong for n=" + n + ", start=" + start, expected.size(), values.size());
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertEquals("value[" + i + "] differs for n=" + n + ", start=" + start, expected, values);
		}
	}

	@Test
	public void testSieveStartGTR3() {
		int n = 10;
		int start = 7;
		List<Integer> values = Eratosthenes.findValues(n, start);
		Assert.assertNotNull("values null for n=" + n + ", start=" + start, values);

		final List<Integer> expected = Arrays.asList(7, 11, 13, 17, 19, 23);
		Assert.assertEquals("values count wrong for n=" + n + ", start=" + start, expected.size(), values.size());
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertEquals("value[" + i + "] differs for n=" + n + ", start=" + start, expected, values);
		}
	}

	@Test
	public void testSieveStart7755() {
		int n = 61;
		int start = 7755;
		List<Integer> values = Eratosthenes.findValues(n, start);
		Assert.assertNotNull("values null for n=" + n + ", start=" + start, values);

		final List<Integer> expected = Arrays.asList(7757, 7759, 7789, 7793, 7817, 7823, 7829, 7841, 7853, 7867, 7873);
		Assert.assertEquals("values count wrong for n=" + n + ", start=" + start, expected.size(), values.size());
		for (int i = 0; i < expected.size(); i++) {
			Assert.assertEquals("value[" + i + "] differs for n=" + n + ", start=" + start, expected, values);
		}
	}
}
