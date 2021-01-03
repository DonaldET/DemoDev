import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution1A {

	private static class Result {
		/*
		 * Complete the 'numberOfItems' function below.
		 *
		 * The function is expected to return an INTEGER ARRAY. The function accepts
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

		private static Integer countItems(String pull) {
			int result = 0;
			int plth = pull.length();
			if (plth > 0) {
				String[] compart = pull.split("[|]");
				int nc = compart.length;
				for (int j = 0; j < nc; j++) {
					if ((j == 0) && pull.charAt(0) != '|') {
						continue;

					} else if ((j == nc - 1) && pull.charAt(plth - 1) != '|') {
						continue;
					}
					if (!compart[j].isEmpty()) {
						result++;
					}
				}
			}
			return result;
		}
	}

	public static void main(String[] args) {
		String s = "***|**|*|*";
		System.out.println("\nSolution 1A: " + s);
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
		System.out.println("\nSolution 1A: " + s);
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
