package don.demodev.romannumerals.test;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demodev.romannumerals.Converter;
import don.demodev.romannumerals.ConverterImpl;
import don.demodev.romannumerals.Converter.Roman2Arabic;

/**
 * This <a href= "https://en.wikipedia.org/wiki/Roman_numerals">Wikipedia Roman
 * Numerals</a> reference describes Roman numerals and provides test cases. A
 * clear definition of how numbers are represented is <a href=
 * "http://www.solano.edu/academic_success_center/forms/math/Roman%20Numerals.pdf">in
 * this teaching reference</a>.
 * 
 * @author Don
 */
public class Arabic2RomanBaseTest {

	protected Converter converter;

	@Before
	public void setUp() throws Exception {
		converter = new ConverterImpl();
	}

	@After
	public void tearDown() throws Exception {
		converter = null;
	}

	@Test
	public void testZeroParameter() {
		Assert.assertEquals("small param", "", converter.arabic2Roman(0));
	}

	// Validation test sequence

	@Test(expected = IllegalArgumentException.class)
	public void testNegativeParameterFails() {
		Assert.assertEquals("negative param fails", "BAD", converter.arabic2Roman(-1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBigParameterFails() {
		Assert.assertEquals("Big param fails", "BAD", converter.arabic2Roman(4001));
	}

	@Test
	public void testInitial0() {
		Assert.assertEquals("0 failed", "", converter.arabic2Roman(0));
	}

	// Functionality testing -- additive numerals

	@Test
	public void testClock() {
		Assert.assertEquals("One failed", "I", converter.arabic2Roman(1));
		Assert.assertEquals("two failed", "II", converter.arabic2Roman(2));
		Assert.assertEquals("three failed", "III", converter.arabic2Roman(3));
		Assert.assertEquals("four failed", "IIII", converter.arabic2Roman(4));
		Assert.assertEquals("five failed", "V", converter.arabic2Roman(5));
		Assert.assertEquals("six failed", "VI", converter.arabic2Roman(6));
		Assert.assertEquals("seven failed", "VII", converter.arabic2Roman(7));
		Assert.assertEquals("eight failed", "VIII", converter.arabic2Roman(8));
		Assert.assertEquals("nine failed", "VIIII", converter.arabic2Roman(9));
		Assert.assertEquals("ten failed", "X", converter.arabic2Roman(10));
		Assert.assertEquals("eleven failed", "XI", converter.arabic2Roman(11));
		Assert.assertEquals("twelve failed", "XII", converter.arabic2Roman(12));
	}

	@Test
	public void testAdditive() {
		Assert.assertEquals("27 failed", "XXVII", converter.arabic2Roman(27));
		Assert.assertEquals("30 failed", "XXX", converter.arabic2Roman(30));
		Assert.assertEquals("1600 failed", "MDC", converter.arabic2Roman(1600));
		Assert.assertEquals("1161 failed", "MCLXI", converter.arabic2Roman(1161));
		int sum = Arrays.stream(Converter.mapping).map(n -> n.arabic).mapToInt(Integer::intValue).sum();
		Assert.assertEquals("all digits sum value, " + sum + ", failed", "MDCLXVI", converter.arabic2Roman(sum));
	}
}
