package demo.granular.sudoku.impl;

import java.util.HashSet;
import java.util.Set;

import demo.granular.sudoku.GameChecker;

public class MultiPassChecker implements GameChecker {

	public MultiPassChecker() {
	}

	@Override
	public boolean isSolution(final int[][] puzzle) {
		// Check rows
		for (final int[] row : puzzle) {
			if (foundDupInVec(row)) {
				return false;
			}
		}

		// Check cols
		int[] content = new int[GameChecker.SD_ROWS];
		for (int c = 0; c < GameChecker.SD_COLS; c++) {
			for (int r = 0; r < GameChecker.SD_ROWS; r++) {
				content[r] = puzzle[r][c];
			}
			if (foundDupInVec(content)) {
				return false;
			}
		}

		// Check subgrid
		content = new int[GameChecker.SD_SUBGRIDS];
		for (int r = 0; r < 3; r++)
			for (int c = 0; c < 3; c++) {
				int k = 0;
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						content[k++] = puzzle[r * 3 + i][c * 3 + j];
					}
				}
				if (foundDupInVec(content)) {
					return false;
				}
			}

		return true;
	}

	private boolean foundDupInVec(final int[] vec) {
		final Set<Integer> occurances = new HashSet<Integer>();
		for (int e : vec) {
			if (e < 1 || e > 9)
				return true;
			if (!occurances.add(e)) {
				return true;
			}
		}
		return false;
	}
}
