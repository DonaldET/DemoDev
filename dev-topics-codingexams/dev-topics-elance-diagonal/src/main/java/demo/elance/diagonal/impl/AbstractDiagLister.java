/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.elance.diagonal.impl;

import demo.elance.diagonal.DiagLister;

/**
 * Validate inputs before delegating to diagonal lister implementation
 * 
 * @author Donald Trummell
 */
public abstract class AbstractDiagLister implements DiagLister
{
  protected AbstractDiagLister()
  {
  }

  /**
   * Validate the parameters and delegate actual listing to the subclass using
   * the template (pattern) method <code>listDiagImpl</code>.
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
  @Override
  public String listDiag(final char[][] matrix, final int row, final int col)
  {
    if (matrix == null)
      throw new IllegalArgumentException("matrix null");

    final int n = matrix.length;
    if (n < 1)
      throw new IllegalArgumentException("matrix empty");

    if (row < 0)
      throw new IllegalArgumentException("row negative");
    if (row >= n)
      throw new IllegalArgumentException("row too big");

    if (col < 0)
      throw new IllegalArgumentException("col negative");
    if (col >= n)
      throw new IllegalArgumentException("col too big");

    if (!(row == 0 || col == 0))
      throw new IllegalArgumentException("[" + row + ", " + col
          + "] is not an edge");

    return listDiagImpl(matrix, n, row, col);
  }

  /**
   * Override this method with an implementation that uses the pre-validated
   * parameters to do the actual listing.
   * 
   * @param matrix
   *          the non-null, non-empty two dimensional square matrix to list
   * @param n
   *          the size of the square matrix
   * @param row
   *          the starting row
   * @param col
   *          the starting column
   * 
   * @return A string listing the elements in a diagonal of the matrix
   */
  protected abstract String listDiagImpl(final char[][] matrix, final int n,
      final int row, final int col);
}
