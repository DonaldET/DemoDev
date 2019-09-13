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
import demo.elance.diagonal.MatrixLister;

/**
 * Base class for maxtix listing implementations, providing error checking and a
 * default <code>toString</code>.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public abstract class AbstractMatrixLister implements MatrixLister
{
  private DiagLister diagLister = null;

  public AbstractMatrixLister()
  {
  }

  /*
   * (non-Javadoc)
   * @see demo.elance.diagonal.MatrixLister#matrixLister(char[][])
   */
  @Override
  public final String matrixLister(final char[][] matrix)
  {
    if (matrix == null)
      throw new IllegalArgumentException("matrix null");

    final int n = matrix.length;
    if (n < 1)
      throw new IllegalArgumentException("matrix empty");

    return matrixListerImpl(matrix);
  }

  abstract protected String matrixListerImpl(final char[][] matrix);

  @Override
  public String toString()
  {
    final StringBuilder msg = new StringBuilder();
    msg.append("[");
    msg.append(getClass().getSimpleName());
    msg.append("- 0x");
    msg.append(Integer.toHexString(hashCode()));
    msg.append("; diagLister=");
    msg.append(diagLister == null ? "null" : diagLister.getClass()
        .getSimpleName());
    msg.append("]");

    return msg.toString();
  }

  public DiagLister getDiagLister()
  {
    return diagLister;
  }

  public void setDiagLister(DiagLister diagLister)
  {
    this.diagLister = diagLister;
  }

}
