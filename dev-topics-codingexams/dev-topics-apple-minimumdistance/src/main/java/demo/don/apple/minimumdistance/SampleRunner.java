package demo.don.apple.minimumdistance;

/**
 * Runs a sample of the minimum distance to a zero finder.
 * 
 * @author Donald Trummell
 */
public class SampleRunner {

	public SampleRunner() {
	}

	public static void main(String[] args) {
		System.out.println("Find nearest zero - Optimized");
		String label = "Test 1: 3 X 4 - all zero";
		int[][] mat0 = { { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 } };
		runTest(label, mat0);

		label = "Test 1: 3 X 4";
		int[][] mat = { { 0, 0, 0, 0 }, { 0, 1, 0, 0 }, { 1, 1, 1, 1 } };
		runTest(label, mat);

		label = "Test 2: 3 X 4";
		int[][] mat2 = { { 0, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 0 } };
		runTest(label, mat2);

		label = "Test 3: 3 X 4";
		int[][] mat3 = { { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 0 } };
		runTest(label, mat3);

		label = "Test 4: 3 X 4 - all one";
		int[][] mat4 = { { 1, 1, 1, 1 }, { 1, 1, 1, 1 }, { 1, 1, 1, 1 } };
		runTest(label, mat4);
	}

	private static void runTest(String label, int[][] mat) {
		System.out.println("\n" + label);
		printImage(mat);
		int[][] ret = DistanceFinder.findNearestZero(mat);
		System.out.println("Answer:");
		printImage(ret);
	}

	private static void printImage(int[][] image) {
		int n = image.length;
		int m = image[0].length;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				System.out.print(String.format(" %d", image[i][j]));
			}
			System.out.println();
		}
	}
}
