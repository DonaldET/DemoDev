package don.demo.algo.cpuconcurrent.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demo.algo.cpuconcurrent.api.ConcurrentCollector.ComputationResult;

/**
 * Test computational result DTO
 *
 * @author Donald Trummell
 */
public class ComputationResultTest {

	private ComputationResult<Integer> one = new ComputationResult<Integer>("One", 1);

	/**
	 * Before each method
	 * 
	 * @throws Exception some error
	 */
	@Before
	public void setUp() throws Exception {
		one = new ComputationResult<Integer>("One", 1);
	}

	/**
	 * After each method
	 * 
	 * @throws Exception some error
	 */
	@After
	public void tearDown() throws Exception {
		one = null;
	}

	/**
	 * Check the equals methosd
	 */
	@Test
	public void testEquals() {
		Assert.assertEquals("self failed", one, one);
		Assert.assertTrue("self equals self failed", one.equals(one));

		ComputationResult<Integer> oneB = new ComputationResult<Integer>("One", 1);
		Assert.assertEquals("same failed", one, oneB);
		Assert.assertTrue("same equals self failed", one.equals(oneB));

		Assert.assertEquals("self compareTo", 0, one.compareTo(one));
		Assert.assertEquals("same compareTo self", 0, one.compareTo(oneB));
	}

	/**
	 * Test the hashcode
	 */
	@Test
	public void testHashCode() {
		Assert.assertEquals("unexpected hash", 5644572, one.hashCode());
	}

	/**
	 * Test the toString
	 */
	@Test
	public void testToString() {
		// e.g.: [ComputationResult - 0x56211c; id: One, value: 1]
		String msg = one.toString();
		int p = msg.indexOf("0x");
		int q = msg.indexOf(";", p + 1);
		msg = msg.substring(0, p) + msg.substring(q + 1);
		Assert.assertEquals("toString failed", "[ComputationResult -  id: One, value: 1]", msg);
	}

	/**
	 * Test compareTo
	 */
	@Test
	public void testCompareTo() {
		List<ComputationResult<Integer>> unordered = Arrays.asList(new ComputationResult<Integer>("One3", 3),
				new ComputationResult<Integer>("One2", 2), one);
		List<ComputationResult<Integer>> expected = Arrays.asList(one, new ComputationResult<Integer>("One2", 2),
				new ComputationResult<Integer>("One3", 3));
		List<ComputationResult<Integer>> ordered = new ArrayList<ComputationResult<Integer>>(unordered);
		ordered.sort(null);
		Assert.assertEquals("sort results differ", expected, ordered);

		unordered = Arrays.asList(new ComputationResult<Integer>("One", 3), new ComputationResult<Integer>("One", 2),
				one);
		expected = Arrays.asList(one, new ComputationResult<Integer>("One", 2),
				new ComputationResult<Integer>("One", 3));
		ordered = new ArrayList<ComputationResult<Integer>>(unordered);
		ordered.sort(null);
		Assert.assertEquals("sort on 2 results differ", expected, ordered);
	}
}
