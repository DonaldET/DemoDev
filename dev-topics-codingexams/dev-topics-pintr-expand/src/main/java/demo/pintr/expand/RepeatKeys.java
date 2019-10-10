package demo.pintr.expand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Write a function that expands a HashMap to an array. The HashMap contains a
 * key and a value, from which  your function will produce an array with the key
 * repeated the respective number of times. Output order of the array does not matter.
 *
 * @author Donald Trummell
 */
public class RepeatKeys {

	public static void main(String[] args) {
		runTest("Null", null);
		runTest("Empty", "");
		runTest("One word", "Hello");
		runTest("Unique words", "Hello There!");
		String doi = "We really really need to be really really faster than our "
				+ "competition who need to be slower than us for us to win";
		runTest("Some Repeats", doi);
	}

	private static List<String> repeatKey(Map<String, Integer> occurrence) {
		List<String> repeats = new ArrayList<String>();
		for (Map.Entry<String, Integer> e : occurrence.entrySet()) {
			repeats.add(Support.repeatString(e.getKey(), e.getValue()));
		}
		return repeats;
	}

	private static void runTest(String label, String input) {
		System.out.println("\n" + label + ":\n  - " + String.valueOf(input));
		Map<String, Integer> freqsByKey = Support.buildFrequencies(input);
		System.out.println("  - Frequencies: " + String.valueOf(freqsByKey));
		List<String> keys = repeatKey(freqsByKey);
		System.out.println("  - Keys       : " + String.valueOf(keys));
	}
}

class Support {
	static Map<String, Integer> buildFrequencies(String text) {
		Map<String, Integer> freq = new HashMap<String, Integer>();
		if (text == null || text.isEmpty()) {
			return freq;
		}

		String[] words = text.trim().split(" ");
		if (words.length < 1) {
			return freq;
		}

		for (int i = 0; i < words.length; i++) {
			freq.merge(words[i], 1, (x, y) -> x + y);
		}

		return freq;
	}

	// Java-11 has this function
	static String repeatString(String s, int n) {
		if (s == null || s.isEmpty() || n < 1) {
			return "";
		}

		StringBuilder sb = new StringBuilder(s);
		while (--n > 0) {
			sb.append(s);
		}

		return sb.toString();
	}
}
