package don.demodev.romannumerals.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demodev.romannumerals.Converter;
import don.demodev.romannumerals.ConverterImpl;

/**
 * This <a href= "https://en.wikipedia.org/wiki/Roman_numerals">Wikipedia Roman
 * Numerals</a> reference describes Roman numerals and provides test cases.
 * 
 * @author Don
 */
public class Arabic2RomanTest {

	private Converter converter;

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

	// Initial test sequence

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
}
