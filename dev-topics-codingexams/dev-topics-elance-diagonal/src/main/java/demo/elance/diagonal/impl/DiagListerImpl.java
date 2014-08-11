/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.elance.diagonal.impl;

import demo.elance.diagonal.DiagLister;

/**
 * This basic diagonal lister implementation provides a list either in the
 * upper-left-to-lower-right direction or the upper-right-to-lower left
 * direction, driven by the <code>delta</code> parameter.
 * <ul>
 * <li>upper-left-to-lower-right is delta==1</li>
 * <li>upper-right-to-lower left is delta==-1</li>
 * </ul>
 * 
 * @author Donald Trummell
 */
public class DiagListerImpl extends AbstractDiagLister implements DiagLister
{
  /**
   * The direction and step size that controls diagonal traversal
   */
  private int delta = 1;

  public DiagListerImpl()
  {
    super();
  }

  /**
   * Provide left-to-right with top-to-bottom traversal of matrix
   */
  @Override
  protected String listDiagImpl(final char[][] matrix, final int n,
      final int row, final int col)
  {
    final StringBuilder txt = new StringBuilder();

    for (int k = 0; k < n; k++)
    {
      final int step = delta * k;

      final int i = row + step;
      if (i >= n)
        break;

      final int j = col + step;
      if (j >= n)
        break;

      final char[] cols = matrix[i];
      int lth = cols.length;
      if (lth != n)
        throw new IllegalArgumentException("expected " + n + " cols, but got "
            + lth);
      if (txt.length() > 0)
        txt.append(", ");
      txt.append(cols[j]);
    }

    return txt.toString();
  }

  @Override
  public String toString()
  {
    final StringBuilder msg = new StringBuilder();
    msg.append("[");
    msg.append(getClass().getSimpleName());
    msg.append("- 0x");
    msg.append(Integer.toHexString(hashCode()));
    msg.append("; delta=");
    msg.append(delta);
    msg.append("]");

    return msg.toString();
  }

  public int getDelta()
  {
    return delta;
  }

  public void setDelta(int delta)
  {
    this.delta = delta;
  }
}
