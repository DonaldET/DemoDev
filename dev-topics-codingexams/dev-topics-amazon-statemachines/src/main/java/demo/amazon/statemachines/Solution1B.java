package demo.amazon.statemachines;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Solve the same problem described in
 * {@link demo.amazon.statemachines.Solution1A}. Solution uses a state machine
 * while parsing through the input string.
 * 
 * @author Donald Trummell
 */
public class Solution1B {

	private static class Result {
		/*
		 * Complete the 'numberOfItems' function below.
		 *
		 * The function is expected to return an INTEGER_ARRAY. The function accepts
		 * following parameters: 1. STRING s 2. INTEGER_ARRAY startIndices 3.
		 * INTEGER_ARRAY endIndices
		 */

		private static List<Integer> numberOfItems(String s, List<Integer> startIndices, List<Integer> endIndices) {
			int n = s.length();
			List<Integer> result = new ArrayList<Integer>();
			if (n < 1) {
				return result;
			}

			// assume start and end are same size
			int nbounds = startIndices.size();
			if (nbounds < 1) {
				return result;
			}

			for (int i = 0; i < nbounds; i++) {
				result.add(countItems(s.substring(startIndices.get(i) - 1, endIndices.get(i))));
			}

			return result;
		}

		private static Integer countItems(String subsequence) {
			int result = 0;
			int slth = subsequence.length();
			if (slth > 0) {
				boolean pipe = false;
				int count = 0;
				for (char c : subsequence.toCharArray()) {
					if (c == '|') {
						if (pipe) { // end of compartment
							if (count > 0) {
								result++;
								count = 0;
							}
						}
						pipe = true;
					} else {
						count++;
					}
				}
			}
			return result;
		}
	}

	public static void main(String[] args) {
		String s = "***|**|*|*";
		System.out.println("\nSolution 1B: " + s);
		List<Integer> beg = Arrays.asList(1, 1, 1, 1, 1);
		List<Integer> endr = Arrays.asList(3, 4, 6, 7, 8);
		List<Integer> expected = Arrays.asList(0, 0, 0, 1, 1);
		for (int i = 0; i < beg.size(); i++) {
			System.out.println(
					"(" + beg.get(i) + ", " + endr.get(i) + ") -> " + s.substring(beg.get(i) - 1, endr.get(i)));
		}
		List<Integer> r = Result.numberOfItems(s, beg, endr);
		System.out.println("containers = " + r + "\nexpected   = " + expected);

		s = "|***|**||*|*";
		System.out.println("\nSolution 1B: " + s);
		beg = Arrays.asList(1, 8, 1, 5, 1, 1);
		endr = Arrays.asList(8, 9, 9, 12, 11, 12);
		expected = Arrays.asList(2, 0, 2, 2, 3, 3);
		for (int i = 0; i < beg.size(); i++) {
			System.out.println(
					"(" + beg.get(i) + ", " + endr.get(i) + ") -> " + s.substring(beg.get(i) - 1, endr.get(i)));
		}
		r = Result.numberOfItems(s, beg, endr);
		System.out.println("containers = " + r + "\nexpected   = " + expected);
	}
}
