package demo.don.apple.minimumdistance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Find distance in a matrix of bits (0/1). Distance is up/down/left/right
 * movements from a one bit to the nearest zero bit. Diagonal distances are not
 * allowed.
 * 
 * @author Donald Trummell
 */
public class DistanceFinder {

	/**
	 * Prevent construction
	 */
	private DistanceFinder() {
	}

	/** The value used for ALL ONES */
	public static final int NONE = Integer.MAX_VALUE;

	/**
	 * Find minimum distance to a zero for each 0/1 bit in a matrix and output as a
	 * matrix. The NONE value is used for an input of all one bits.
	 * <p>
	 * <code><pre>
	 * 0 0 0 0      0 0 0 0    0 1 1 1       0 1 2 2
	 * 0 1 0 0  =>  0 1 0 0    1 1 1 1  =>   1 2 2 1
	 * 1 1 1 1      1 2 1 1    1 1 1 0       2 2 1 0
	
	 * 1 1 1 1      5 4 3 2    1 1 1 1       x x x x 
	 * 1 1 1 1  =>  4 3 2 1    1 1 1 1 =>    x x x x
	 * 1 1 1 0      3 2 1 0    1 1 1 1       x x x x
	 * </pre></code>
	 */
	public static int[][] findNearestZero(int[][] a) {
		int n = a.length;
		int m = a[0].length;

		// Setup output matrix
		List<List<Integer>> found = new ArrayList<>();
		int[][] r = new int[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (a[i][j] == 0) {
					r[i][j] = 0;
					found.add(Arrays.asList(i, j));
				} else {
					r[i][j] = NONE;
				}
			}
		}

		// Update a nearest neighbor one entry at a time for each increasing distance
		// until nothing is updated
		int nextDist = 1;
		while (!found.isEmpty()) {
			List<List<Integer>> found2 = updateNeighbors(r, nextDist, found);
			if (found2.isEmpty()) {
				break;
			}
			found = found2;
			nextDist++;
		}

		return r;
	}

	/**
	 * From the last marked distance recorded in the last update, we now update all
	 * the nearest neighbors to the next distance (that is, plus one).
	 */
	private static List<List<Integer>> updateNeighbors(int[][] r, int nextDist, List<List<Integer>> lastFound) {
		List<List<Integer>> replaced = new ArrayList<>();
		int n = r.length;
		int m = r[0].length;

		for (List<Integer> dist : lastFound) {
			int i = dist.get(0);
			int j = dist.get(1);

			if (i - 1 >= 0) {
				// look up
				if (r[i - 1][j] == NONE) {
					r[i - 1][j] = nextDist;
					replaced.add(Arrays.asList(i - 1, j));
				}
			}

			if (i + 1 < n) {
				// look down
				if (r[i + 1][j] == NONE) {
					r[i + 1][j] = nextDist;
					replaced.add(Arrays.asList(i + 1, j));
				}
			}

			if (j - 1 >= 0) {
				// look left
				if (r[i][j - 1] == NONE) {
					r[i][j - 1] = nextDist;
					replaced.add(Arrays.asList(i, j - 1));
				}
			}

			if (j + 1 < m) {
				// look right
				if (r[i][j + 1] == NONE) {
					r[i][j + 1] = nextDist;
					replaced.add(Arrays.asList(i, j + 1));
				}
			}
		}
		return replaced;
	}
}
