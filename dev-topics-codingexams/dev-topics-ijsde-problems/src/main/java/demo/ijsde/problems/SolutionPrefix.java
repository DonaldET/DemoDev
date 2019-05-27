package demo.ijsde.problems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Problem: Find the longest prefix string in a collection of strings. If there
 * is no common prefix, then return the empty string. An empty list of strings,
 * and a list withn an empty string, also have no common prefix, and should
 * return the empty string.
 * <p>
 * Solution: Examine all possible substrings of the <em>shortest</em> string in
 * the input list.
 */
public class SolutionPrefix {
	//
	// last parameter is an optimization to skip potentially long string compare for
	// prefix candidate

	static boolean allStartWith(String[] str, String commonPrefix, int skipIdx) {
		if (commonPrefix.length() < 1) {
			return false;
		}

		for (int i = 0; i < str.length; i++) {
			if (i == skipIdx) {
				continue;
			}
			if (!str[i].startsWith(commonPrefix)) {
				return false;
			}
		}

		return true;
	}

	public static String solution(String[] str) {
		if (str == null || str.length < 1) {
			return "";
		}

		if (str.length < 2) {
			return str[0];
		}

		return solutionImpl(str);
	}

	static String solutionImpl(String[] str) {
		String prefix = null;
		int prefixIdx = Integer.MIN_VALUE;
		int prefixLth = Integer.MAX_VALUE;

		//
		// Find shortest non-empty string in input list

		for (int i = 0; i < str.length; i++) {
			String s = str[i];
			if (s == null || s.length() < 1) {
				return "";
			}

			if (s.length() < prefixLth) {
				prefixLth = s.length();
				prefix = s;
				prefixIdx = i;
			}
		}

		//
		// Keep looking at the prefix and making it smaller by dropping right-most
		// character on compare failure

		boolean inAll = allStartWith(str, prefix, prefixIdx);
		while (!inAll && prefix.length() > 0) {
			prefix = prefix.substring(0, prefix.length() - 1);
			inAll = allStartWith(str, prefix, prefixIdx);
		}
		return prefix;
	}

	public static void main(String[] args) {
		System.out.println("\n*** Find Longest Common Prefix ***");

		String[] test0 = { "abcdef", "abcghi", "abcabc" };
		String[] test1 = { "pqr", "stuv", "wxyza", "123456" };
		String[] test2 = { "pqr", "stuv", "wxyza", "123456", "", "aabb" };
		String[] test3 = {};
		String[] test4 = { "" };
		String[] test5 = { "123abc" };
		String[] test6 = { "123456789A", "12345", "1234567", "12345678" };

		List<String[]> tests = new ArrayList<String[]>();
		tests.add(test0);
		tests.add(test1);
		tests.add(test2);
		tests.add(test3);
		tests.add(test4);
		tests.add(test5);
		tests.add(test6);

		String[] expected = { "abc", "", "", "", "", "123abc", "12345" };

		int n = tests.size();
		int ne = expected.length;
		if (n != expected.length) {
			throw new IllegalStateException("tests incorrectly set up, n=" + n + ", n exp=" + ne);
		}
		;

		int failCount = 0;
		for (int i = 0; i < n; i++) {
			int tn = i + 1;
			System.out.print("  Test " + tn);
			String[] test = tests.get(i);
			System.out.print(";  test value: " + Arrays.toString(test));
			String exp = expected[i];
			String act = solution(test);
			if (exp.equals(act)) {
				System.out.println("; PASSED.");
			} else {
				failCount++;
				System.out.println("; FAILED!");
				System.out.println("    EXP: [" + exp + "]");
				System.out.println("    ACT: [" + act + "]");
			}
		}

		System.out.println("Tests complete" + ((failCount > 0) ? ", with " + failCount + " failures!" : "."));
	}
}
