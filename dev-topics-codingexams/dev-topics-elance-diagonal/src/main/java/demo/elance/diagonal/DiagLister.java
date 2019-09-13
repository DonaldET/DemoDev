/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.elance.diagonal;

/**
 * An implementation lists the diagonal of a square matrix starting at an edge
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface DiagLister
{
  /**
   * List the diagonal elements of a square matrix starting at the matrix
   * element specified by row and col; with the restriction that
   * <code>row</code> in [0, n] and <code>col</code> in [0, n), specifying an
   * outer edge. Note that an edge element has either <code>row</code> zero or
   * <code>col</code> zero. The matrix must not be empty
   *
   * @param matrix
   *          the non-null, non-empty two dimensional square matrix to list
   * @param row
   *          the starting row
   * @param col
   *          the starting column
   *
   * @return A string listing the elements in a diagonal of the matrix
   */
  public abstract String listDiag(final char[][] matrix, final int row,
      final int col);
}
