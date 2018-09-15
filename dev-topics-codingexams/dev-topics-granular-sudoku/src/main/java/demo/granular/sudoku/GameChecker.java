/*
 * Copyright (c) 2018. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.granular.sudoku;

/**
 * Sudoku puzzle checker (see
 * <a href="https://en.wikipedia.org/wiki/Sudoku">Sudoku on Wikipedia</a>.)
 * Completed games are always a type of Latin square with an additional
 * constraint on the contents of individual regions. For example, the same
 * single integer may not appear twice in the same row, column, or any of the
 * nine 3×3 subregions of the 9x9 playing board.
 * 
 * @author Donald Trummell
 */
public interface GameChecker {
	public static final int SD_ROWS = 9;
	public static final int SD_COLS = 9;
	public static final int SD_SUBGRIDS = 9;

	/**
	 * Validate a fully populated Sudoku grid, a puzzle, to evaluate if it is a
	 * valid solution.
	 * 
	 * @param puzzle
	 * @return <code>true</code> if a valid puzzle, else false
	 */
	public abstract boolean isSolution(final int[][] puzzle);
}