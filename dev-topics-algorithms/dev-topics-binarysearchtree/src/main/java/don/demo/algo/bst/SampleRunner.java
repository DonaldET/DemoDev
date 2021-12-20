package don.demo.algo.bst;

import java.util.Arrays;

/**
 * Create a simple binary search tree with unique keys and non-null values
 * associated with the keys.
 * 
 * @author Donald Trummell
 */
public class SampleRunner {

	public static void main(String[] args) {
		System.out.println("Build the tree");
		BST<Integer, Integer> bst = new BST<Integer, Integer>();

		BSTDisplay<Integer, Integer> disp = new BSTDisplay<>();
		disp.printBST("Empty", bst.getRoot());

		//
		// Build a sample tree

		int[] test1 = { 45, 10, 7, 90, 12, 50, 13, 39, 57 };
		System.out.println("Will enter: " + Arrays.toString(test1));
		final int testLength = test1.length;
		for (int i = 0; i < testLength; i++) {
			int key = test1[i];
			int value = 10 * key;
			System.out.println("    -- inserting (" + key + "," + value + ");  nodes: " + bst.getNodes());
			bst.insert(key, value);
		}
		disp.printBST("Post add all " + bst.getNodes() + " nodes", bst.getRoot());

		int key = 50;
		int val = bst.find(key);
		System.out.println("The value for " + key + " is " + val);

		// Replace a value
		int newVal = 111;
		bst.insert(key, newVal);
		System.out.println("replaced value " + val + " for key " + key + " with new value " + newVal);
		System.out.println("now find " + bst.find(key));

		//
		// Start a new balanced BST

		System.out.println("\n\nCreate balanced binary search tree");
		Arrays.sort(test1);
		System.out.println("Will enter: " + Arrays.toString(test1));

		//
		// Fill in BST from the middle out

		bst = new BST<Integer, Integer>();
		int toLow = testLength / 2;
		int toHigh = toLow;

		boolean inserted = false;
		key = Integer.MIN_VALUE;
		do {
			inserted = false;

			if (toHigh == toLow) {
				key = test1[toLow];
				bst.insert(key, 10 * key);
				inserted = true;
			} else {
				if (toLow >= 0) {
					key = test1[toLow];
					bst.insert(key, 10 * key);
					inserted = true;
				}

				if (toHigh < testLength) {
					key = test1[toHigh];
					bst.insert(key, 10 * key);
					inserted = true;
				}
			}

			if (!inserted) {
				break;
			}

			toLow--;
			toHigh++;
		} while (inserted);

		disp.printBST("Balanced Tree", bst.getRoot());
	}
}
