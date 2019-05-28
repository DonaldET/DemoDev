package demo.trulia.treesearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Supplies test data for the demo.
 */
public class TestDataManager {

	private static List<List<TSNode>> testData = new ArrayList<>();
	private static List<TSNode> nodes = null;

	static {
		nodes = new ArrayList<TSNode>();
		nodes.add(new TSNode("A", 5, "B", "C"));
		nodes.add(new TSNode("B", 4, null, null));
		nodes.add(new TSNode("C", 7, null, null));
		testData.add(nodes);

		nodes = new ArrayList<TSNode>();
		nodes.add(new TSNode("A", 5, null, null));
		testData.add(nodes);

		nodes = new ArrayList<TSNode>();
		nodes.add(new TSNode("A", 5, "B", "C"));
		nodes.add(new TSNode("B", 5, null, null));
		nodes.add(new TSNode("C", 5, null, null));
		testData.add(nodes);

		nodes = new ArrayList<TSNode>();
		nodes.add(new TSNode("A", 5, "B", "C"));
		nodes.add(new TSNode("B", 5, null, null));
		nodes.add(new TSNode("C", 7, null, null));
		testData.add(nodes);

		nodes = new ArrayList<TSNode>();
		nodes.add(new TSNode("A", 5, "B", "C"));
		nodes.add(new TSNode("B", 5, "D", "E"));
		nodes.add(new TSNode("C", 5, "F", "G"));
		nodes.add(new TSNode("D", 5, null, null));
		nodes.add(new TSNode("E", 5, null, null));
		nodes.add(new TSNode("F", 5, null, null));
		nodes.add(new TSNode("G", 5, "H", "I"));
		nodes.add(new TSNode("H", 5, "J", "K"));
		nodes.add(new TSNode("I", 6, null, null));
		nodes.add(new TSNode("J", 7, null, null));
		nodes.add(new TSNode("K", 8, null, null));
		testData.add(nodes);
	}

	// Prevent construction
	private TestDataManager() {
	}

	public static List<TSNode> getTestData(int setIdx) {
		return testData.get(setIdx);
	}
}
