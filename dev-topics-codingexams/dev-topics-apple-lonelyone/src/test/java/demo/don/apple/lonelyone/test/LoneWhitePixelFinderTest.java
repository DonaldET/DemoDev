package demo.don.apple.lonelyone.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.apple.lonelyone.WhitePixel;
import demo.don.apple.lonelyone.WhitePixel.Pixel;

public class LoneWhitePixelFinderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSinglePixel() {
		String label = "Single pixel";
		byte[][] testImage = { { 0 } };
		List<Pixel> expected = new ArrayList<>();
		List<Pixel> actual = WhitePixel.findLonePixels(testImage);
		testFindPixel(label, expected, actual);
	}

	@Test
	public void testAt22() {
		String label = "OneAt(2,2)";
		byte[][] testImage1 = { { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 1, 1, 0, 1 }, { 0, 0, 1, 0 } };
		List<Pixel> expected = Arrays.asList(new Pixel(2, 2));
		List<Pixel> actual = WhitePixel.findLonePixels(testImage1);
		testFindPixel(label, expected, actual);
	}

	@Test
	public void testAt00() {
		String label = "OneAt(0,0)";
		byte[][] testImage2 = { { 0, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 } };
		List<Pixel> expected = Arrays.asList(new Pixel(0, 0));
		List<Pixel> actual = WhitePixel.findLonePixels(testImage2);
		testFindPixel(label, expected, actual);
	}

	@Test
	public void testAt2213() {
		String label = "At(2,2)(1,3)";
		byte[][] testImage3 = { { 1, 1, 1, 1 }, { 1, 1, 1, 0 }, { 1, 1, 0, 1 }, { 0, 0, 1, 1 } };
		List<Pixel> expected = Arrays.asList(new Pixel(2, 2), new Pixel(1, 3));
		List<Pixel> actual = WhitePixel.findLonePixels(testImage3);
		testFindPixel(label, expected, actual);
	}

	@Test
	public void testNone() {
		String label = "None";
		byte[][] testImage4 = { { 1, 1, 0, 0 }, { 1, 1, 1, 0 }, { 1, 1, 0, 1 }, { 0, 0, 1, 1 } };
		List<Pixel> expected = new ArrayList<>();
		List<Pixel> actual = WhitePixel.findLonePixels(testImage4);
		testFindPixel(label, expected, actual);
	}

	public static void testFindPixel(String label, List<Pixel> expected, List<Pixel> actual) {
		Assert.assertEquals(label, expected, actual);
	}
}
