package demo.don.ijsde.problems;

import java.util.HashSet;
import java.util.Set;

/**
 * Find the length of the longest substring of non-repeating characters in a
 * given string.
 * <p>
 * Solution:
 * <ul>
 * <li>Scan all possible substrings left-to-right, remembering the largest.</li>
 * <li>Within a potential substring, use a hash set to keep track of unique
 * characters.</li>
 * <li>Optimize uniqueness search my skipping potential substrings shorter than
 * the saved maximum size.</li>
 * </ul>
 */
public class SolutionNonRepeat {

	public static long solution(String s) {
		if (s == null || s.isEmpty()) {
			return 0;
		}

		final int n = s.length();
		if (n < 2) {
			return 1;
		}

		if (n < 3) {
			return (s.charAt(0) == s.charAt(1) ? 1 : 2);
		}

		//
		// At this point, a string is at least 3 long

		int maxLth = 0;
		int checkCnt = n - maxLth;
		int p = 0;
		while (p < n && checkCnt > 0) {
			// Check from p through remaining (n - maxLength) chars
			maxLth = Math.max(maxLth, longestNonRepeat(s.substring(p, p + checkCnt)));
			p++;
			int n2 = (n - 1) - p + 1;
			checkCnt = n2 - maxLth;
		}

		return (maxLth);
	}

	static int longestNonRepeat(String s) {
		if (s == null || s.isEmpty()) {
			return 0;
		}

		int n = s.length();
		if (n < 2) {
			return 1;
		}

		if (n < 3) {
			return (s.charAt(0) == s.charAt(1) ? 1 : 2);
		}

		Set<Character> known = new HashSet<Character>();
		for (int i = 0; i < s.length(); i++) {
			if (!known.add(s.charAt(i))) {
				break;
			}
		}

		return known.size();
	}

	public static void main(String[] args) {
		String[] tests = { "", "nndfddf", "abcdefghijkdef" };
		long[] exp = { 0L, 3L, 11L };

		for (int i = 0; i < tests.length; i++) {
			System.out.print((i + 1) + ". Testing " + tests[i] + "; exp=" + exp[i] + ";");
			long act = solution(tests[i]);
			System.out.println(" act=" + act + ((exp[i] == act) ? "." : " FAILED!"));
		}
	}
}
