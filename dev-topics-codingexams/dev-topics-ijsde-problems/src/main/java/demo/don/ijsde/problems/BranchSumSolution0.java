package demo.don.ijsde.problems;

import java.util.ArrayList;
import java.util.List;

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
 * Solution: Iterate through each level of the binary tree, collecting the indicies of
 * nodes of the left half for the left branch sum and similarily collecting nodes for the right
 * branch sum. Both sums are created by summing the values associated with the collected indicies.
 */

public class BranchSumSolution0 {
	public static final class Indicies {
		public final int size;
		public final List<Integer> leftIdx;
		public final List<Integer> rightIdx;

		public Indicies(int size, List<Integer> leftIdx, List<Integer> rightIdx) {
			super();
			this.size = size;
			this.leftIdx = leftIdx;
			this.rightIdx = rightIdx;
		}

		@Override
		public String toString() {
			return "Indicies [Size: " + size + ", leftIdx=" + leftIdx + ", rightIdx=" + rightIdx + "]";
		}
	}

	public static String solution(long[] arr) {
		if (arr.length <= 1) {
			return "";
		}

		Indicies indicies = branchIdx(arr.length);
		Long leftSum = sumIndicies(arr, indicies.leftIdx);
		Long rightSum = sumIndicies(arr, indicies.rightIdx);

		return leftSum > rightSum ? "Left" : (rightSum > leftSum ? "Right" : "");
	}

	static Indicies branchIdx(int n) {
		if (n < 2) {
			Indicies indicies = new Indicies(n, new ArrayList<Integer>(), new ArrayList<Integer>());
			if (n > 0) {
				indicies.leftIdx.add(0);
				indicies.rightIdx.add(0);
			}
			return indicies;
		}

		return branchIdxImpl(n);
	}

	static Indicies branchIdxImpl(int n) {
		int depth = depthFor(n);
		int size = sizeForDepth(depth);
		int half = (size - 1) / 2;
		Indicies indicies = new Indicies(n, new ArrayList<Integer>(half), new ArrayList<Integer>(half));
		int p = 0;
		indicies.leftIdx.add(p);
		indicies.rightIdx.add(p);
		for (int d = 2; d <= depth; d++) {
			int lth = lengthAtDepth(d);
			half = lth / 2;
			for (int i = 0; i < half; i++) {
				indicies.leftIdx.add(++p);
			}
			for (int i = half; i < lth; i++) {
				indicies.rightIdx.add(++p);
			}
		}

		return indicies;
	}

	static long sumIndicies(long[] arr, List<Integer> indicies) {
		long sum = 0L;
		int n = arr.length;
		int nIndicies = indicies.size();
		if (n < 1 || nIndicies < 1) {
			return sum;
		}

		for (int i = 0; i < nIndicies; i++) {
			int idx = indicies.get(i);
			if (0 <= idx && idx < n) {
				long val = arr[idx];
				sum += (-1 == val ? 0L : val);
			}
		}

		return sum;
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

//		System.out.println("Collect Indicies:");
//		for (int n = 0; n < 16; n++) {
//			System.out.print("N: " + n);
//			int d = depthFor(n);
//			System.out.print(";  d: " + d + " (" + (int) Math.pow(2.0, d) + ")");
//			Indicies indicies = branchIdx(n);
//			System.out.println(";  " + String.valueOf(indicies));
//		}

		int testCase = 0;
		int failedCount = 0;
		System.out.println("\nChecking " + tests.length + " test cases");
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
