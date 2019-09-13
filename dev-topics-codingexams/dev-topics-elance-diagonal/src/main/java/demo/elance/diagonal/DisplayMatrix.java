/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.elance.diagonal;

import demo.elance.diagonal.impl.AbstractMatrixLister;
import demo.elance.diagonal.impl.ColFirstMatrixListerImpl;
import demo.elance.diagonal.impl.DiagListerImpl;
import demo.elance.diagonal.impl.RowFirstMatrixListerImpl;

/**
 * A sample program that uses hard-coded test data and a @quot;
 * <em>row first</em> &quot; matrix diagonal listing implementation
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class DisplayMatrix
{
  final private DiagLister diagLister = new DiagListerImpl();
  private MatrixLister rowFirst = null;
  private MatrixLister colFirst = null;
  private final char[][] matrix = new char[][] {
      new char[] { 'a', 'b', 'c', 'd' }, new char[] { 'e', 'f', 'g', 'h' },
      new char[] { 'i', 'j', 'k', 'l' }, new char[] { 'm', 'n', 'o', 'p' } };

  public DisplayMatrix()
  {
    AbstractMatrixLister lister = new RowFirstMatrixListerImpl();
    lister.setDiagLister(diagLister);
    rowFirst = lister;
    lister = new ColFirstMatrixListerImpl();
    lister.setDiagLister(diagLister);
    colFirst = lister;
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    System.out.println("Listing Matrices\n");

    final DisplayMatrix dm = new DisplayMatrix();

    System.out.println("Original:");
    final int n = dm.matrix.length;
    for (int i = 0; i < n; i++)
    {
      System.out.print("   ");
      for (int j = 0; j < n; j++)
      {
        if (j > 0)
          System.out.print(", ");
        System.out.print(dm.matrix[i][j]);
      }
      System.out.println();
    }

    System.out.println("\nRow wise:\n" + dm.rowFirst.matrixLister(dm.matrix));

    System.out.println("\nCol wise:\n" + dm.colFirst.matrixLister(dm.matrix));

    System.out.println("\nDone");

  }
}
