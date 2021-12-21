package demo.don.apple.lonelyone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import demo.don.apple.lonelyone.WhitePixel.Pixel;

/**
 * Runs a sample of the lone white cell finder.
 * 
 * @author Donald Trummell
 */
public class SampleRunner {

	public SampleRunner() {
	}

	public static void main(String[] args) {
		System.out.print("Find lone pixels examples");

		String label = "Single pixel";
		byte[][] testImage = { { 0 } };
		List<Pixel> expected = new ArrayList<>();
		List<Pixel> actual = WhitePixel.findLonePixels(testImage);
		testFindPixel(label, expected, actual);

		label = "OneAt(2,2)";
		byte[][] testImage1 = { { 0, 0, 1, 0 }, { 0, 0, 1, 0 }, { 1, 1, 0, 1 }, { 0, 0, 1, 0 } };
		expected = Arrays.asList(new Pixel(2, 2));
		actual = WhitePixel.findLonePixels(testImage1);
		testFindPixel(label, expected, actual);
		printImage(testImage1);

		label = "OneAt(0,0)";
		byte[][] testImage2 = { { 0, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 } };
		expected = Arrays.asList(new Pixel(0, 0));
		actual = WhitePixel.findLonePixels(testImage2);
		testFindPixel(label, expected, actual);
		printImage(testImage2);

		label = "At(2,2)(1,3)";
		byte[][] testImage3 = { { 1, 1, 1, 1 }, { 1, 1, 1, 0 }, { 1, 1, 0, 1 }, { 0, 0, 1, 1 } };
		expected = Arrays.asList(new Pixel(2, 2), new Pixel(1, 3));
		actual = WhitePixel.findLonePixels(testImage3);
		testFindPixel(label, expected, actual);
		printImage(testImage3);

		label = "None";
		byte[][] testImage4 = { { 1, 1, 0, 0 }, { 1, 1, 1, 0 }, { 1, 1, 0, 1 }, { 0, 0, 1, 1 } };
		expected = new ArrayList<>();
		actual = WhitePixel.findLonePixels(testImage4);
		testFindPixel(label, expected, actual);
		printImage(testImage4);
	}

	public static void testFindPixel(String label, List<Pixel> expected, List<Pixel> actual) {
		if (expected.equals(actual)) {
			System.out.println("\n" + label + " passed");
		} else {
			System.out.println("\n" + label + " failed!");
			System.out.println("Expected: " + expected);
			System.out.println("Actual  : " + actual);
		}
	}

	public static void printImage(byte[][] image) {
		int n = image.length;
		int m = image[0].length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				System.out.print(String.format(" %d", image[i][j]));
			}
			System.out.println();
		}
	}
}
