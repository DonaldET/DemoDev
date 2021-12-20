package don.demo.algo.bst;

/**
 * The binary search tree class that maintains the search tree nodes linked
 * list.
 * 
 * @author Donald Trummell
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class BST<K extends Comparable<K>, V> {
	/**
	 * The linked list node stored in the binary search tree
	 * 
	 * @author Donald Trummell
	 *
	 * @param <K> the key type
	 * @param <V> the value type
	 */
	public static final class BSTNode<K extends Comparable<K>, V> {
		public K key;
		public V value;
		public BSTNode<K, V> left;
		public BSTNode<K, V> right;

		public BSTNode(K key, V value) {
			super();
			this.key = key;
			this.value = value;
		}

		@Override
		public String toString() {
			StringBuilder msg = new StringBuilder();
			msg.append("BSTNode [(key=" + key + ", value=" + value + "); left=(");
			msg.append(left == null ? "null" : left.key + "," + left.value);
			msg.append("); right=(");
			msg.append(right == null ? "null" : right.key + "," + right.value);
			msg.append(")]");
			return msg.toString();
		}
	}

	private BSTNode<K, V> root;
	private int nodes = 0;

	public BST(BSTNode<K, V> root) {
		super();
		this.root = root;
	}

	/**
	 * Construct me
	 */
	public BST() {
		this(null);
	}

	/**
	 * Locate a value associated with a key
	 * 
	 * @param key the key to locate
	 * @return the associated value or null of not found
	 */
	public V find(K key) {
		if (root == null) {
			return null;
		}
		BSTNode<K, V> toFind = new BSTNode<K, V>(key, null);
		BSTNode<K, V> testNode = root;

		do {
			int cmp = toFind.key.compareTo(testNode.key);
			if (cmp < 0) {
				// toFind < test
				testNode = testNode.left;
			} else if (cmp > 0) {
				// toFind > test
				testNode = testNode.right;
			} else {
				// equal key
				return testNode.value;
			}
		} while (testNode != null);
		return null;
	}

	/**
	 * Insert or replace a key in the tree
	 * 
	 * @param key   the key to add or replace
	 * @param value the value to associate with the key
	 * @return true if added and false if replaced
	 */
	public boolean insert(K key, V value) {
		BSTNode<K, V> toAdd = new BSTNode<K, V>(key, value);
		if (root == null) {
			root = toAdd;
			nodes++;
			return true;
		}

		BSTNode<K, V> testNode = root;
		BSTNode<K, V> lastNode = null;
		int cmp = 0;
		do {
			lastNode = testNode;
			cmp = toAdd.key.compareTo(testNode.key);
			if (cmp < 0) {
				// toAdd < test
				testNode = testNode.left;
			} else if (cmp > 0) {
				// toAdd > test
				testNode = testNode.right;
			} else {
				// equal key
				testNode.value = toAdd.value;
				return false;
			}
		} while (testNode != null);
		nodes++;
		if (cmp < 0) {
			// toAdd went left
			lastNode.left = toAdd;
		} else {
			// toAddd went right
			lastNode.right = toAdd;
		}
		return true;
	}

	public BSTNode<K, V> getRoot() {
		return root;
	}

	public int getNodes() {
		return nodes;
	}
}