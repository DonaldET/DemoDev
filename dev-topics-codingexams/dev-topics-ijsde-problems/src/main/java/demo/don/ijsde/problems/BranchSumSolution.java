package demo.don.ijsde.problems;

/**
 * Suppose you're given a binary tree represented as an array. For example,
 * [3,6,2,9,-1,10] represents the following binary tree (where -1 is a
 * non-existent node):
 * <pre>
 * <code>
 *        3
 *   6         2
 * 9   -1   10   **
 * </code>
 * </pre>
 * Write a function that determines whether the left or right branch of the tree
 * is larger. The size of each branch is the sum of the node values. The
 * function should return the string " Right" if the right side is larger and
 * "Left" if the left side is larger. If the tree has 0 nodes or if the size of
 * the branches are equal, return the empty string.
 * <p>
 * Example Input: [3, 6, 2, 9, -1,10]; left.sum = 15; right.sum = 12; Example
 * Output: Left
 * <p>
 * <strong>Note:</strong> Binary tree depth d has 2**d - 1 nodes 1 == 1; 2 == 3;
 * 3 == 7, so, from n: 2**d &lt;= n ==&lt;&lt; d &lt;= log2(n)
 * <p>
 * Level K has 2**K elements, potentially -1 (empty), and
 * <p>
 * LeftBranch(K) = 0 .. 2**(K/2) - 1, and
 * <p>
 * RightBranch(K) = 2**(K/2) .. 2**k - 1
 * <p>
 * Solution: Iterate through each level of the binary tree, adding the node
 * values of the left half to the left branch sum and similarily for the right
 * branch sum. Both sums start with the root node value.
 */

public class BranchSumSolution {
	public static final class BranchSums {
		public final int size;
		public long leftSum;
		public long rightSum;

		public BranchSums(int size, long leftSum, long righSum) {
			super();
			this.size = size;
			this.leftSum = leftSum;
			this.rightSum = righSum;
		}

		@Override
		public String toString() {
			return "BranchSums [Size: " + size + ", leftSum=" + leftSum + ", rightSum=" + rightSum + "]";
		}
	}

	public static String solution(long[] arr) {
		if (arr.length <= 1) {
			return "";
		}

		BranchSums branchSums = sumBranchs(arr);
		return branchSums.leftSum > branchSums.rightSum ? "Left"
				: (branchSums.rightSum > branchSums.leftSum ? "Right" : "");
	}

	static BranchSums sumBranchs(long[] arr) {
		int n = arr.length;
		if (n < 2) {
			BranchSums branch = new BranchSums(n, 0L, 0L);
			if (n > 0) {
				long val = ((long) -1 == arr[0]) ? 0 : arr[0];
				branch.leftSum += val;
				branch.rightSum += val;
			}
			return branch;
		}

		return sumBranchsImpl(arr);
	}

	static BranchSums sumBranchsImpl(long[] arr) {
		int n = arr.length;
		int depth = depthFor(n);
		int size = sizeForDepth(depth);
		int half = (size - 1) / 2;
		BranchSums branchSums = new BranchSums(n, 0L, 0L);
		int p = 0;
		long val = (-1 == arr[p]) ? 0L : arr[p];
		branchSums.leftSum += val;
		branchSums.rightSum += val;
		for (int d = 2; d <= depth; d++) {
			int lth = lengthAtDepth(d);
			half = lth / 2;
			for (int i = 0; i < half; i++) {
				if (++p >= n) {
					break;
				}
				val = (-1 == arr[p]) ? 0L : arr[p];
				branchSums.leftSum += val;
			}
			for (int i = half; i < lth; i++) {
				if (++p >= n) {
					break;
				}
				val = (-1 == arr[p]) ? 0L : arr[p];
				branchSums.rightSum += val;
			}
		}

		return branchSums;
	}

	static int sizeForDepth(int depth) {
		return (int) Math.pow(2.0, depth) - 1;
	}

	static int lengthAtDepth(int depth) {
		return sizeForDepth(depth) - sizeForDepth(depth - 1);
	}

	static int depthFor(int n) {
		if (n < 1) {
			return 0;
		}

		int d = (int) Math.ceil(Math.log((double) n) / Math.log(2.0));
		return sizeForDepth(d) < n ? d + 1 : d;
	}

	// ---------------------------------------------------------------------------------------------------------

	public static void main(String[] args) {
		long[] arr0 = { 3L, 6L, 2L, 9L, -1L, 10L }; // Left
		long[] arr1 = { 1L, 4L, 100L, 5L }; // Right
		long[] arr2 = { 1L, 10L, 5L, 1L, 0L, 6L }; // ""
		long[] arr3 = {}; // ""
		long[] arr4 = { 1L }; // ""
		long[][] tests = new long[][] { arr0, arr1, arr2, arr3, arr4 };

		String[] expected = { "Left", "Right", "", "", "" };

		int testCase = 0;
		int failedCount = 0;
		System.out.println("Checking " + tests.length + " test cases");
		for (int i = 0; i < tests.length; i++) {
			long[] arr = tests[i];
			String act = solution(arr);
			String exp = expected[i];
			if (act != exp) {
				System.out.println("Test case " + testCase + " failed, expected [" + exp + "], but got [" + act + "]");
				failedCount++;
			}
		}

		if (failedCount > 0) {
			System.out.println("\n*** Had " + failedCount + " failures!");
		} else {
			System.out.println("all " + tests.length + " passed.");
		}
	}
}
