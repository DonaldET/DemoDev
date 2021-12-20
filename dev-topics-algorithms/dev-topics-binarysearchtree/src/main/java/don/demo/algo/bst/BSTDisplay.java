package don.demo.algo.bst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import don.demo.algo.bst.BST.BSTNode;

/**
 * Display a Binary Search Tree
 * 
 * @author Donald Trummell
 */
public class BSTDisplay<KY extends Comparable<KY>, VAL> {
	/**
	 * Orders tree items to properly display
	 * 
	 * @author Donald Trummell
	 *
	 * @param <T> type of display item is really the key type
	 */
	private static final class DispItem<T extends Comparable<T>> {
		public final int level;
		public final int parentPath;
		public final T key;

		public DispItem(int level, int parentPath, T key) {
			super();
			this.level = level;
			this.parentPath = parentPath;
			this.key = key;
		}

		@Override
		public String toString() {
			return "DispItem [level=" + level + ", parentPath=" + parentPath + ", key=" + key + "]";
		}
	}

	private static final String PADDING = "   ";

	public void printBST(String label, BSTNode<KY, VAL> root) {
		System.out.println(label);

		if (root == null) {
			System.out.println("root null");
		} else {
			List<DispItem<KY>> tree = new ArrayList<>();
			buildDisplayTree(root, 0, 0, tree);
			Collections.sort(tree, (d1, d2) -> {
				int d = d1.level - d2.level;
				if (d == 0) {
					d = d1.parentPath - d2.parentPath;
					if (d == 0) {
						d = d1.key.compareTo(d2.key);
					}
				}
				return d;
			});

			final int n = tree.size();
			DispItem<KY> last = tree.get(n - 1);
			int maxCells = 2*last.parentPath;
			int offset = maxCells / 2;
			int maxLevels = last.level;

			String[] cells = new String[maxCells];
			for (int i = 0; i < maxCells; i++) {
				cells[i] = PADDING;
			}

			int level = 0;
			int p = 0;
			while (p < n) {
				DispItem<KY> nxt = tree.get(p);
				if (nxt.level != level) {
					System.out.println(Arrays.toString(cells));
					level = nxt.level;
					offset--;
					for (int i = 0; i < maxCells; i++) {
						cells[i] = PADDING;
					}
				}
				cells[nxt.parentPath + offset] = String.format("%3s", nxt.key);
				p++;
			}
			assert level == maxLevels;
			System.out.println(Arrays.toString(cells));
		}

	}

	private void buildDisplayTree(BSTNode<KY, VAL> node, int level, int parentPath, List<DispItem<KY>> tree) {
		if (node == null) {
			return;
		}
		tree.add(new DispItem<KY>(level, parentPath, node.key));
		buildDisplayTree(node.left, level + 1, (parentPath << 1), tree);
		buildDisplayTree(node.right, level + 1, (parentPath << 1) + 1, tree);
	}
}
