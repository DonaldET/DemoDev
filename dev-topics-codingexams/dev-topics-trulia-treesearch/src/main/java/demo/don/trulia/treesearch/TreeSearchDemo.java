package demo.don.trulia.treesearch;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import demo.don.trulia.treesearch.TreeConstructor.TreeSetup;

/**
 * Runs tests of the search algorithm.
 */
public class TreeSearchDemo {

	public static void main(String[] args) {
		System.out.println("\nTree Search Demo Program");

		try {
			runTestCase(0, "Adding node less than parent", Integer.MIN_VALUE);
		} catch (IllegalStateException ex) {
			System.out.println("Caught expected error: " + ex.getMessage());
		}

		runTestCase(1, "Single childless node", Integer.MIN_VALUE);
		runTestCase(2, "Single parent node, has one level, None", Integer.MIN_VALUE);
		runTestCase(3, "Single parent node, has one level", 7);
		runTestCase(4, "Single parent node, has five levels", 6);
	}

	private static void runTestCase(int testIdx, String label, int expNextSmallestVal) {
		System.out.print("\n\nTest " + testIdx + ": " + label);
		List<TSNode> testData = TestDataManager.getTestData(testIdx);
		System.out.println("; with " + testData.size() + " nodes.");

		TreeSetup ts = TreeConstructor.createTreeFromList(testData);
		System.out.println(
				"  Parent '" + ts.parentName + "'; has total of " + ts.tree.size() + " nodes, has last minimum value "
						+ ts.lastMinimumValue + ", and expected next largest value is " + expNextSmallestVal);
		printTreeAsNameOrderedList(ts.tree);

		int nSaved = TreeSearcher.getNextLargest(ts.tree, ts.parentName);

		int nFound = TreeSearcher.locatedNodes.size();
		if (nSaved != nFound) {
			throw new IllegalStateException("ERROR - thought we saved " + nSaved + ", but actually saved " + nFound);
		}
		System.out.println("  Search: " + TreeSearcher.locatedNodes);

		int nextLargest = (nFound < 2) ? Integer.MIN_VALUE : TreeSearcher.locatedNodes.get(1).value;
		System.out.print("    -- Next Largest Value = " + nextLargest);
		if (nextLargest == expNextSmallestVal) {
			System.out.print(".");
		} else {
			System.out.println(".  ERROR - does not match expected value " + expNextSmallestVal);
		}
	}

	private static void printTreeAsNameOrderedList(Map<String, TSNode> tree) {

		Set<String> keySet = tree.keySet();
		int nkeys = keySet.size();
		if (nkeys < 1) {
			System.out.println("  -- Empty --");
			return;
		}

		String[] names = new String[nkeys];
		keySet.toArray(names);
		if (nkeys > 0) {
			Arrays.sort(names);
		}

		for (int i = 0; i < nkeys; i++) {
			System.out.println("    " + i + ". " + tree.get(names[i]));
		}
	}
}
