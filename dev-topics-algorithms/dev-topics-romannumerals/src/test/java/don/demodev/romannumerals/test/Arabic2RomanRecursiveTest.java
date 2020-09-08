package don.demodev.romannumerals.test;

import org.junit.Before;

import don.demodev.romannumerals.ConverterImplRecursive;

/**
 * This <a href= "https://en.wikipedia.org/wiki/Roman_numerals">Wikipedia Roman
 * Numerals</a> reference describes Roman numerals and provides test cases. A
 * clear definition of how numbers are represented is <a href=
 * "http://www.solano.edu/academic_success_center/forms/math/Roman%20Numerals.pdf">in
 * this teaching reference</a>, as well as test cases.
 * <p>
 * This tests the recursive implementation.
 * 
 * @author Don
 */
public class Arabic2RomanRecursiveTest extends Arabic2RomanBaseTest {
	@Override
	@Before
	public void setUp() throws Exception {
		converter = new ConverterImplRecursive();
	}
}
