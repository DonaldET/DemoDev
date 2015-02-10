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
 * Step through starting columns and find rows from edge
 * 
 * @author Donald Trummell
 */
public class ColFirstMatrixListerImpl extends AbstractMatrixLister implements
    MatrixLister
{
  public ColFirstMatrixListerImpl()
  {
  }

  @Override
  protected String matrixListerImpl(final char[][] matrix)
  {
    final DiagLister diagLister = getDiagLister();
    final int n = matrix.length;
    final StringBuilder msg = new StringBuilder();

    final int edge = 0;
    for (int col = 0; col < n; col++)
    {
      final String diag = diagLister.listDiag(matrix, edge, col);
      if (msg.length() > 0)
        msg.append("\n");
      msg.append(diag);
    }

    for (int row = 1; row < n; row++)
    {
      final String diag = diagLister.listDiag(matrix, row, edge);
      if (msg.length() > 0)
        msg.append("\n");
      msg.append(diag);
    }

    return msg.toString();
  }
}
