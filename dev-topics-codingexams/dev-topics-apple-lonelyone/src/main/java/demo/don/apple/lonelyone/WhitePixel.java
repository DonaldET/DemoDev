package demo.don.apple.lonelyone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class WhitePixel {

	/**
	 * Represents a Pixel and the display of a pixel
	 * 
	 * @author Donald Trummell
	 */
	public static final class Pixel {
		public int row;
		public int col;

		public Pixel(int row, int col) {
			super();
			this.row = row;
			this.col = col;
		}

		@Override
		public int hashCode() {
			return Objects.hash(col, row);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Pixel other = (Pixel) obj;
			return col == other.col && row == other.row;
		}

		@Override
		public String toString() {
			return "Pixel [row=" + row + ", col=" + col + "]";
		}
	}

	/**
	 * Prevent construction
	 */
	private WhitePixel() {
	}

	/**
	 * The lone pixel finding algorithm; it is O(n * m) in time complexity. Finds
	 * lone white pixels in column order.
	 * 
	 * @param image an n x m byte array
	 * 
	 * @return a list of @quot;lone&quot; white pixels
	 */
	public static List<Pixel> findLonePixels(byte[][] image) {
		if (image == null) {
			return null;
		}

		List<Pixel> whites = new ArrayList<>();
		final int n = image.length;
		final int m = image[0].length;

		if (n == 1 && m == 1) {
			return whites;
		}

		//
		// The trick: A lone white pixel in (r, c), placed in an (n, m), will have
		// rowSums[r] == (m - 1) AND colSums(r) == (n - 1)

		//
		// Accumulate row sums
		Set<Integer> candidates = new HashSet<>();
		for (int r = 0; r < n; r++) {
			int sum = 0;
			for (int c = 0; c < m; c++) {
				sum += image[r][c];
			}
			if (sum == m - 1) {
				candidates.add(r);
			}
		}
		if (candidates.isEmpty()) {
			return whites;
		}

		//
		// Accumulate column sums
		for (int c = 0; c < m; c++) {
			int whiteRow = -1;
			int sum = 0;
			for (int r = 0; r < n; r++) {
				if (image[r][c] == 0 && candidates.contains(r)) {
					whiteRow = r;
				}
				sum += image[r][c];
			}
			if (sum == n - 1 && whiteRow != -1) {
				whites.add(new Pixel(whiteRow, c));
			}
		}

		return whites;
	}
}
