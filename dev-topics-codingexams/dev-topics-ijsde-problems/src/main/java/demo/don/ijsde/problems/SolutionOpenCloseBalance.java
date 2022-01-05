package demo.don.ijsde.problems;

import java.util.ArrayList;
import java.util.List;

/**
 * Problem:
 * <p>
 * Given a sequence of open-close brackets (&lt; and &gt;), correct the sequence
 * so that open and close brackets are balanced and open-brackets precede
 * close-brackets.
 * <p>
 * Solution:
 * <p>
 * Scan a bracket list from left to right, keeping a <em>balance</em> count. An
 * open-bracket increments the balance count by one, and a close-bracket
 * decrements the count by one. A valid bracket string ends with a zero balance
 * count and the balance count does not go negative during the scan.
 * <p>
 * Some inputs will be invalid and have a negative balance-count during the
 * scan. We correct this as follows:
 * <ul>
 * <li>Keep a record of the last zero-balance count position during the scan,
 * updating the last zero-balance position as the scan progresses from left to
 * right.</li>
 * <li>When a negative balance count is encountered, then insert an open bracket
 * at the last zero-balance position and</li>
 * <li>Update the last zero-balance position to the current position, and set
 * the balance count position to zero, and</li>
 * <li>Continue the scan from the next character after the current
 * position.</li>
 * </ul>
 * Note that we use a list to represent the input to allow easy insertions.
 */
public class SolutionOpenCloseBalance {

	private static final String OPEN_BRACKET = "<";
	private static final String CLOSE_BRACKET = ">";

	static List<String> cleanupBracketList(String inp) {
		String good = OPEN_BRACKET + CLOSE_BRACKET;
		List<String> in = new ArrayList<String>();
		for (int i = 0; i < inp.length(); i++) {
			String s = Character.toString(inp.charAt(i));
			if (good.contains(s)) {
				in.add(s);
			}
		}

		return in;
	}

	public static String solution(String brackets) {
		String inp = (brackets == null) ? "" : brackets.trim();
		if (inp.isEmpty()) {
			return inp;
		}

		List<String> in = cleanupBracketList(inp);
		if (in.size() < 1) {
			return "";
		}

		return solutionImpl(in);
	}

	static String solutionImpl(List<String> in) {
		int balance = 0;
		int scanIdx = 0;
		int lastBalancedIdx = 0;
		do {
			String e = in.get(scanIdx);
			if (e.equals(OPEN_BRACKET)) {
				balance++;
			} else {
				balance--;
				if (balance < 0) {
					in.add(lastBalancedIdx, OPEN_BRACKET);
					balance = 0;
					lastBalancedIdx = scanIdx++;
				}
			}
			scanIdx++;
		} while (scanIdx < in.size());

		if (balance < 0) {
			while (balance < 0) {
				in.add(lastBalancedIdx, OPEN_BRACKET);
				balance++;
			}
		} else if (balance > 0) {
			while (balance > 0) {
				in.add(CLOSE_BRACKET);
				balance--;
			}
		}

		StringBuilder sb = new StringBuilder(in.size());
		for (String s : in) {
			sb.append(s);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println("\n*** Testing Correcting Open-Close Bracket String to Achieve Matching Brackets ***");

		String[] testVals = { "", "<>", ">", ">>", "<", "<<", "<><>>>>>", "<<<<>>><>" };
		String[] expected = { "", "<>", "<>", "<<>>", "<>", "<<>>", "<<><<><><>>>", "<<<<>>><>>" };

		int n = testVals.length;
		int ne = expected.length;
		if (n != ne) {
			throw new IllegalStateException("tests incorrectly set up, n=" + n + ", n exp=" + ne);
		}
		;

		int failCount = 0;
		for (int i = 0; i < n; i++) {
			int tn = i + 1;
			System.out.print("  Test " + tn);
			String test = testVals[i];
			System.out.print(";  test value: [" + test + "]");
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
