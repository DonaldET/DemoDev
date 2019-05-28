package demo.trulia.treesearch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Constructs the test data tree (<code>TreeSetup</code>) such that there is a
 * single root node and all other nodes are referenced exactly once. There are
 * no self-references and the tree is a directed acyclic graph
 * (<strong>DAG</strong>). Each node has a value and references two children.
 * The node values of children must equal or exceed the parent node value.
 */
public class TreeConstructor {

	/**
	 * The tree created from a list by this constructor.
	 */
	public static final class TreeSetup {
		public String parentName;
		public Map<String, TSNode> tree;
		public int lastMinimumValue;

		public TreeSetup(String parentName, Map<String, TSNode> tree, int lastMinimumValue) {
			super();
			this.parentName = parentName;
			this.tree = tree;
			this.lastMinimumValue = lastMinimumValue;
		}
	}

	// Prevent construction
	private TreeConstructor() {
	}

	public static TreeSetup createTreeFromList(List<TSNode> treeSpec) {
		if (treeSpec.isEmpty()) {
			throw new IllegalArgumentException("tree specification empty");
		}

		Set<String> referencedNodes = new HashSet<String>();
		Map<String, TSNode> tree = new HashMap<String, TSNode>();
		for (TSNode spec : treeSpec) {

			//
			// Parent has two nodes or none
			if (spec.leftChild == null) {
				if (spec.rightChild != null) {
					throw new IllegalArgumentException("ERROR: left child null, right child specified: " + spec.name);
				}
			} else {
				if (spec.rightChild == null) {
					throw new IllegalArgumentException("ERROR: left child specifed, right child null: " + spec.name);
				}

				if (spec.leftChild == spec.name) {
					throw new IllegalArgumentException("ERROR: left child a self reference: " + spec.name);
				}

				if (spec.rightChild == spec.name) {
					throw new IllegalArgumentException("ERROR: right child a self reference: " + spec.name);
				}

				if (!referencedNodes.add(spec.leftChild)) {
					throw new IllegalArgumentException("ERROR: left child referenced more than once: " + spec.name);
				}

				if (!referencedNodes.add(spec.rightChild)) {
					throw new IllegalArgumentException("ERROR: right child referenced more than once: " + spec.name);
				}
			}

			if (tree.put(spec.name, spec) != null) {
				throw new IllegalArgumentException("Entered duplicate node name " + spec.name);
			}
		}

		//
		// Check the tree references, a single tree parent node is the only unreferenced
		// node
		//
		// Note: copy keyset because it is backed by the original set and removal will
		// lose entries
		Set<String> namedNodes = new HashSet<String>(tree.keySet());
		namedNodes.removeAll(referencedNodes);
		if (namedNodes.size() != 1) {
			throw new IllegalStateException("expected 1 unreferenced node (parent), but found " + namedNodes.size());
		}
		String[] parentName = new String[1];
		namedNodes.toArray(parentName);

		return new TreeSetup(parentName[0], tree, treeChecker(tree, parentName[0]));
	}

	private static int treeChecker(Map<String, TSNode> tree, final String parentName) {
		TSNode parent = tree.get(parentName);
		return familyVisitor(tree, parent, parent.value);
	}

	private static int familyVisitor(Map<String, TSNode> tree, TSNode parent, int lastValue) {
		int currentLast = familyChecker(tree, parent, lastValue);
		TSNode leftChild = tree.get(parent.leftChild);
		if (leftChild == null) {
			return currentLast;
		}
		TSNode rightChild = tree.get(parent.rightChild);

		int leftFamilyValue = familyVisitor(tree, leftChild, currentLast);
		int rightFamilyValue = familyVisitor(tree, rightChild, currentLast);

		return Math.min(leftFamilyValue, rightFamilyValue);
	}

	private static int familyChecker(Map<String, TSNode> tree, TSNode parent, int lastValue) {
		int pval = parent.value;
		if (pval < lastValue) {
			throw new IllegalStateException("parent " + parent.name + " has value " + pval + " less than " + lastValue);
		}

		TSNode leftChild = tree.get(parent.leftChild);
		if (leftChild == null) {
			return pval;
		}
		TSNode rightChild = tree.get(parent.rightChild);

		if (leftChild.value < pval) {
			throw new IllegalStateException("leftChild value " + leftChild.value + " less than parent value " + pval);
		}

		if (rightChild.value < pval) {
			throw new IllegalStateException("rightChild value " + rightChild.value + " less than parent value " + pval);
		}

		return Math.min(leftChild.value, rightChild.value);
	}
}
