package don.demo.algo.cpuconcurrent.test;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demo.algo.cpuconcurrent.support.DataGenerator;
import don.demo.algo.cpuconcurrent.support.DataGenerator.MockDataDTO;

/**
 * Test the mock data generator
 * 
 * @author Donald Trummell
 */
public class DataGeneratorTest {

	private DataGenerator generator = null;

	/**
	 * Before each method
	 * 
	 * @throws Exception some error
	 */
	@Before
	public void setUp() throws Exception {
		generator = new DataGenerator();
	}

	/**
	 * After each method
	 * 
	 * @throws Exception some error
	 */
	@After
	public void tearDown() throws Exception {
		generator = null;
	}

	/**
	 * Test too small of a computation
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateSamll() {
		generator.generate(2);
	}

	/**
	 * Test too big of a computation
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateBig() {
		generator.generate(123000000);
	}

	/**
	 * Test 10 values
	 */
	@Test
	public void testOnly10() {
		int n = 10;
		MockDataDTO mockData = generator.generate(n);
		Assert.assertEquals("sum differs", 55, mockData.sumData);
		Assert.assertEquals("unique count differs", 10, mockData.uniqueCount);
		int[] data = mockData.data;
		Assert.assertNotNull("data null", data);
		Assert.assertEquals("data size wrong", n, data.length);
		int[] expected = { 10, 9, 6, 4, 7, 3, 1, 5, 8, 2 };
		Assert.assertTrue("data values differ", Arrays.equals(expected, data));
	}
}
