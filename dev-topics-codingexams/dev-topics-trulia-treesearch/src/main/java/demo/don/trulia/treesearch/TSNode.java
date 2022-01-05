package demo.don.trulia.treesearch;

public class TSNode {
	public String name;
	public int value;
	public String leftChild;
	public String rightChild;

	/**
	 * A (binary) tree node has a left child reference and a right child reference.
	 * The defined tree will be a <strong>DAG</strong> with no self-references.
	 */
	public TSNode(String name, int value, String leftChild, String rightChild) {
		super();
		this.name = name;
		this.value = value;
		this.leftChild = leftChild;
		this.rightChild = rightChild;
	}

	@Override
	public String toString() {
		return "[TSNode - 0x" + Integer.toHexString(hashCode()) + "; name=" + name + ", value=" + value + ", leftChild="
				+ leftChild + ", rightChild=" + rightChild + "]";
	}
}
