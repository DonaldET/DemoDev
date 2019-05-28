package demo.trulia.treesearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is the search algorithm for the <strong>DAG</strong>.
 */
public class TreeSearcher {

	/*
	 * Defines the capacity of persistence of last values, only needs to be 2 for
	 * the 'next largest', but bigger for debugging the algorithm.
	 */
	public static final int LAST_SAVED_LIMIT = 5;

	public static class NextLargest {
		public String nodeName;
		public int value;

		public NextLargest(String nodeName, int value) {
			super();
			this.nodeName = nodeName;
			this.value = value;
		}

		@Override
		public String toString() {
			return "[NextLargest- 0x" + Integer.toHexString(hashCode()) + "; nodeName=" + nodeName + ", value=" + value
					+ "]";
		}
	}

	public static List<NextLargest> locatedNodes = null;

	// Prevent construction
	private TreeSearcher() {
	}

	/**
	 * Returns the number of saved ordered results.
	 *
	 * @param tree       the <strong>DAG</strong> tree to search that has already
	 *                   been checked for correctness by the tree constructor.
	 * @param parentName the name of the parent node.
	 * @return number of ordered unique values saved, limited by parameter.
	 */
	public static int getNextLargest(Map<String, TSNode> tree, String parentName) {
		locatedNodes = new ArrayList<NextLargest>();
		if (tree.size() < 3) {
			return 0;
		}
		return familyVisitor(tree, tree.get(parentName));
	}

	/**
	 * Given a family of a parent and two children, first check the parent as the
	 * next largest (<code>familyChecker</code>), then check the children ordered
	 * smallest to largest.
	 * <p>
	 * If more to check, then check the children as a family, using recursion, and
	 * order smallest to largest child.
	 * <p>
	 * Recursion is: <code>familyVisitor</code> ==&gt;
	 * <code>checkOrderedFamily</code> ==&gt; <code>familyVisitor</code>
	 */
	private static int familyVisitor(Map<String, TSNode> tree, TSNode parent) {
		int nSaved = locatedNodes.size();
		if (nSaved >= LAST_SAVED_LIMIT) {
			return nSaved;
		}

		nSaved = familyChecker(tree, parent);
		if (nSaved >= LAST_SAVED_LIMIT) {
			return nSaved;
		}

		TSNode leftChild = tree.get(parent.leftChild);
		if (leftChild == null) {
			return nSaved;
		}
		TSNode rightChild = tree.get(parent.rightChild);

		//
		// Add smallest child first, note that list is not empty!
		nSaved = (leftChild.value <= rightChild.value) ? checkOrderedChildren(tree, leftChild, rightChild)
				: checkOrderedChildren(tree, leftChild, rightChild);

		//
		// Recursively check smallest child Node family first, note that list is not
		// empty
		nSaved = (leftChild.value <= rightChild.value) ? checkOrderedFamily(tree, leftChild, rightChild)
				: checkOrderedFamily(tree, rightChild, leftChild);

		return nSaved;
	}

	private static int checkOrderedChildren(Map<String, TSNode> tree, TSNode smallestChild, TSNode largestChild) {
		int nSaved = locatedNodes.size();
		int savedValue = locatedNodes.get(nSaved - 1).value;

		if (smallestChild.value > savedValue) {
			locatedNodes.add(new NextLargest(smallestChild.name, smallestChild.value));
			nSaved++;
			savedValue = smallestChild.value;
		}

		if (largestChild.value > savedValue) {
			locatedNodes.add(new NextLargest(largestChild.name, largestChild.value));
			nSaved++;
			savedValue = largestChild.value;
		}

		return nSaved;
	}

	private static int checkOrderedFamily(Map<String, TSNode> tree, TSNode smallestChild, TSNode largestChild) {
		int nSaved = familyVisitor(tree, smallestChild);
		if (nSaved < LAST_SAVED_LIMIT) {
			nSaved = familyVisitor(tree, largestChild);
		}

		return nSaved;
	}

	/**
	 * Saves a parent node if it is the next largest. This is not called
	 * recursively.
	 */
	private static int familyChecker(Map<String, TSNode> tree, TSNode parent) {
		return saveNextLargest(parent);
	}

	private static int saveNextLargest(TSNode parent) {
		int nSaved = locatedNodes.size();
		if (nSaved >= LAST_SAVED_LIMIT) {
			return nSaved;
		}

		int pval = parent.value;
		if (nSaved < 1 || pval > locatedNodes.get(nSaved - 1).value) {
			locatedNodes.add(new NextLargest(parent.name, pval));
			nSaved++;
		}

		return nSaved;
	}
}
