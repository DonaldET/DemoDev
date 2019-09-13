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
 * A <code>MatrixLister</code> implementation provides this service method
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface MatrixLister
{
  /**
   * List the elements of the matrix
   *
   * @param matrix
   *          input matrix to list
   *
   * @return the string of diagonal elements
   */
  public abstract String matrixLister(final char[][] matrix);
}