package demo.don.javascript.intersect;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <strong>Question</strong>:
 * <p>
 * Write a function, <code>findIntersection</code>, that reads an array of
 * strings which will contain two elements: the first element will represent a
 * list of comma-separated numbers sorted in ascending order, the second element
 * will represent a second list of comma-separated numbers (also sorted). Your
 * goal is to return a string of numbers that occur in both elements of the
 * input array in sorted order. If there is no intersection, return the string
 * <code>FALSE</code>.
 * <p>
 * For example: if the input array is ["1, 3, 4, 7, 15", "1, 2, 4, 15, 21"] the
 * output string should be "1, 4, 15" because those numbers appear in both
 * strings (they are the common elements). The array given will not be empty,
 * and each string inside the array will be of numbers sorted in ascending order
 * and may contain negative numbers.
 * <p>
 * Another example: if the input array is ["1, 3, 9, 10, 17, 18", "1, 4, 9, 10"]
 * the output string should be "1, 9, 10" because those numbers appear in both
 * of the strings.
 * <p>
 * Problem taken from <a href=
 * "https://javascript.plainenglish.io/a-common-javascript-phone-screen-question-asked-by-facebook-357a0139c458">a
 * JavaSript site</a> claiming it is a FaceBook interview question.
 * 
 * @author Donald Trummell
 */
public class Intersect {

	private static class InputParsing {

		private InputParsing() {
			// prevent construction
		}

		private static List<Integer> parseInts(final String intList) {
			if (intList == null) {
				return null;
			}

			final String intListFixed = intList.trim();
			if (intListFixed.isEmpty()) {
				return new ArrayList<Integer>();
			}

			return Stream.of(intListFixed.split(",")).map(String::trim).map(Integer::parseInt)
					.collect(Collectors.toList());
		}

		private static void testParseInts() {
			System.out.println("\nTest Parsing Input");
			System.out.println("NULL => " + InputParsing.parseInts(null));
			System.out.println("\"\" => " + InputParsing.parseInts(""));
			System.out.println("\"1\" => " + InputParsing.parseInts("1"));
			System.out.println("\"1, 2, 3\" => " + InputParsing.parseInts("1, 2, 3"));
			System.out.println("Parse test complete.\n");
		}
	}

	public static void main(String[] args) {
		System.out.println("Intersect two sorted lists of Integers");

		InputParsing.testParseInts();

		System.out.println("Testing Intersection:");
		Intersect.testIntersect("", "", "");
		Intersect.testIntersect("", "1", "");
		Intersect.testIntersect("1", "2", "");
		Intersect.testIntersect("2", "1", "");
		Intersect.testIntersect("1,2,2,3,3,3,4", "2,3,3,4,5", "2,3,3,4");
		Intersect.testIntersect("1, 3, 4, 7, 15", "1, 2, 4, 15, 21", "1, 4, 15");
		Intersect.testIntersect("1, 3, 9, 10, 17, 18", "1, 4, 9, 10", "1, 9, 10");
		System.out.println("Intersect test complete.\n");

		System.out.println("Done.");
	}

	private static void testIntersect(final String lhsStr, final String rhsStr, final String expectedStr) {
		List<Integer> lhs = InputParsing.parseInts(lhsStr);
		List<Integer> rhs = InputParsing.parseInts(rhsStr);
		List<Integer> shared = Intersect.intersect(lhs, rhs);
		System.out.println(
				"\"" + lhs + "\" <-> \"" + rhs + "\" ==> \"" + shared + "\", expected: \"" + expectedStr + "\"");
	}

	/**
	 * Merge the two lists, ignoring different values, and returning equal values.
	 * 
	 * @param lhs non-null list of sorted integers
	 * @param rhs non-null list of sorted integers
	 * @return a possibly empty sorted list of shared values
	 */
	private static List<Integer> intersect(List<Integer> lhs, List<Integer> rhs) {
		List<Integer> shared = new ArrayList<Integer>();
		final int nLhs = lhs.size();
		if (nLhs < 1) {
			return shared;
		}
		final int nRhs = rhs.size();
		if (nRhs < 1) {
			return shared;
		}

		int lPtr = 0;
		int rPtr = 0;
		do {
			final int lvalue = lhs.get(lPtr);
			final int delta = lvalue - rhs.get(rPtr);
			if (delta < 0) {
				lPtr++;
			} else if (delta > 0) {
				rPtr++;
			} else {
				shared.add(lvalue);
				lPtr++;
				rPtr++;
			}
		} while (lPtr < nLhs && rPtr < nRhs);

		return shared;
	}
}
