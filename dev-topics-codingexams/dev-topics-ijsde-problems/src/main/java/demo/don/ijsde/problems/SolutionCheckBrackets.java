package demo.don.ijsde.problems;

import java.util.Stack;

/**
 * Problem:
 * <p>
 * Given a list of different open-close brackets, such as "{}[]()" , check
 * whether the brackets are matched in an input string. Note: different bracket
 * types may not be interleaved.
 * <p>
 * Example Input: brackets: "([{})]"
 * <p>
 * Example Output: <strong>false</strong>
 * <p>
 * Explanation:
 * <p>
 * Although there is a closing bracket for every opening bracket, the
 * expressions inside the brackets are not themselves valid. Inside the
 * parentheses of "([{})", the invalid expression is "[{}".
 * <p>
 * Solution: Examine character input stream left-to-right, using a stack of
 * _matchers_ to record the state of the scan. A _matcher_ hold the bracket type
 * and balance count.
 * <p>
 * If a new opener is encountered, push a new matcher into the stacked matchers.
 * For additional matching openers,add one to the balance count of the stacked
 * matcher. opener, add one to the balance count and continue parsing.
 * <p>
 * Otherwise, if a closer is encountered, then check that the closer type
 * matches the stacked opener bracket type. A different closer type indicates an
 * interleaving error. Subtract one from the balance count. If the balance count
 * is zero, then pop the stacked matcher. and continue parsing.
 */
public class SolutionCheckBrackets {
	//
	// Opener and closer types must be matched
	public static final String openers = String.join("", "{", "[", "(");
	public static final String closers = String.join("", "}", "]", ")");
	public static final int nbrackets = openers.length();

	/**
	 * Record nested progress from an opener while scanning for closers
	 */
	public static class SequenceMatcher {
		public final int bracketIndex;
		public int balance = 0;

		public SequenceMatcher(int bracketIndex) {
			super();
			this.bracketIndex = bracketIndex;
		}
	}

	public static boolean solution(String brackets) {
		if (brackets == null) {
			return false;
		}

		String clnBrackets = brackets.trim();
		int n = clnBrackets.length();
		if (n < 1) {
			return true;
		}

		return solutionImpl(clnBrackets);
	}

	//
	// Now an even-length string at least two characters long

	static boolean solutionImpl(String brackets) {
		Stack<SequenceMatcher> matchers = new Stack<>();
		SequenceMatcher matcher = new SequenceMatcher(-1);
		matchers.push(matcher);
		int n = brackets.length();
		int p = 0;
		int bracketIdx = -1;
		String c = null;
		while (p < n) {
			c = brackets.substring(p, p + 1);
			matcher = matchers.peek();
			if ((bracketIdx = closers.indexOf(c)) >= 0) {
				//
				// --- Process a closer ---
				if (bracketIdx != matcher.bracketIndex) {
					return false;
				} else {
					// closes off this sequence
					matcher.balance--;
					if (matcher.balance < 0) {
						return false;
					}
					if (matcher.balance == 0) {
						matchers.pop();
					}
				}
			} else if ((bracketIdx = openers.indexOf(c)) >= 0) {
				//
				// --- Process an opener ---
				if (matcher.bracketIndex != bracketIdx) {
					matcher = new SequenceMatcher(bracketIdx);
					matchers.push(matcher);
				}
				matcher.balance++;
			} else {
				throw new IllegalStateException("character [" + c + "] at " + p + " is not a bracket (cls: "
						+ (bracketIdx = closers.indexOf(c)) + ", opn: " + (openers.indexOf(c)) + ")");
			}
			p++;
		}

		return matchers.size() == 1 && p == n;
	}

	public static void main(String[] args) {
		System.out.println("\n*** Testing Open-Close Multiple Brackets String for Validity ***");

		String bracketA = "";
		String bracketB = "{";
		String bracketC = "}";
		String bracketD = "((}";
		String bracketE = "(}}";
		String bracketF = "((((";
		String bracketG = "((({{{{{{}}}]]])))";
		String brackets0 = "{}[]{}()";
		String brackets1 = "{[()]}";
		String brackets2 = "{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}";
		String brackets3 = "{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([[[[]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}{{}{[([]{})](){{}}}(((({}){()[]}((){})))())}";

		String[] tests = { bracketA, bracketB, bracketC, bracketD, bracketE, bracketF, bracketG, brackets0, brackets1,
				brackets2, brackets3 };
		boolean[] expected = { true, false, false, false, false, false, false, true, true, true, false };

		int n = tests.length;
		int ne = expected.length;
		if (n != ne) {
			throw new IllegalStateException("tests incorrectly set up, n=" + n + ", and n exp=" + ne);
		}

		int failCount = 0;
		for (int i = 0; i < n; i++) {
			int tn = i + 1;
			System.out.print("  Test " + tn);
			String test = tests[i];
			boolean exp = expected[i];
			System.out.print(": " + exp + " -- test value: [" + test.substring(0, Math.min(20, test.length()))
					+ (test.length() > 20 ? "..." : "") + "]");
			boolean act = solution(test);
			if (exp == act) {
				System.out.println("; PASSED.");
			} else {
				failCount++;
				System.out.println("; FAILED!");
				System.out.println("    EXP: " + exp);
				System.out.println("    ACT: " + act);
			}
		}

		System.out.println("Tests complete" + ((failCount > 0) ? ", with " + failCount + " failures!" : "."));
	}
}
