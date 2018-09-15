package demo.granular.sudoku.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import demo.granular.sudoku.GameChecker;

public class SinglePassChecker implements GameChecker {

	public SinglePassChecker() {
	}

	@Override
	public boolean isSolution(final int[][] puzzle) {
		final List<Set<Integer>> rowChk = createCheckers(GameChecker.SD_ROWS);
		final List<Set<Integer>> colChk = createCheckers(GameChecker.SD_COLS);
		final List<Set<Integer>> gridChk = createCheckers(GameChecker.SD_SUBGRIDS);

		for (int r = 0; r < GameChecker.SD_ROWS; r++) {
			final int sg_r = r / 3;
			for (int c = 0; c < GameChecker.SD_COLS; c++) {
				final int v = puzzle[r][c];
				if (v < 1 || v > 9) {
					return false;
				}

				if (!rowChk.get(r).add(v)) {
					return false;
				}

				if (!colChk.get(c).add(v)) {
					return false;
				}

				final int gridIdx = 3 * sg_r + c / 3;
				if (!gridChk.get(gridIdx).add(v)) {
					return false;
				}
			}
		}

		return true;
	}

	private List<Set<Integer>> createCheckers(final int n) {
		final List<Set<Integer>> checkers = new ArrayList<Set<Integer>>(n);
		for (int i = 0; i < n; i++) {
			checkers.add(new HashSet<Integer>());
		}
		return checkers;
	}
}
