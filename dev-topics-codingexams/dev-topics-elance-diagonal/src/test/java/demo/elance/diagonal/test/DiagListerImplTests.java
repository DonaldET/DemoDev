/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.elance.diagonal.test;

import junit.framework.TestCase;
import demo.elance.diagonal.DiagLister;
import demo.elance.diagonal.impl.DiagListerImpl;

/**
 * Test <code>DiagLister</code> implementations
 * 
 * @author Donald Trummell
 */
public class DiagListerImplTests extends TestCase
{
  private DiagLister diagLister = null;

  private final char[][] matrix = new char[][] {
      new char[] { 'a', 'b', 'c', 'd' }, new char[] { 'e', 'f', 'g', 'h' },
      new char[] { 'i', 'j', 'k', 'l' }, new char[] { 'm', 'n', 'o', 'p' } };

  public DiagListerImplTests()
  {
    super();
  }

  public DiagListerImplTests(final String name)
  {
    super(name);
  }

  /**
   * @throws java.lang.Exception
   */
  public void setUp() throws Exception
  {
    diagLister = new DiagListerImpl();
  }

  /**
   * @throws java.lang.Exception
   */
  public void tearDown() throws Exception
  {
    diagLister = null;
  }

  /**
   * Verify class name Test method for {@link java.lang.Object#toString()}.
   */
  public void testToString()
  {
    final String state = diagLister.toString();
    assertNotNull("no lister toString", state);
    assertFalse("state empty", state.isEmpty());
    String pattern = "DiagListerImpl";
    assertTrue(pattern + " not in " + state, state.indexOf(pattern) > -1);
    pattern = "delta=1";
    assertTrue(pattern + " not in " + state, state.indexOf(pattern) > -1);
  }

  /**
   * Test PUC for method
   * {@link demo.elance.diagonal.impl.AbstractDiagLister#listDiag(char[][], int, int)}
   * .
   */
  public void testMainListDiag()
  {
    //
    // Main diagonal
    int row = 0;
    int col = 0;
    String expected = "a, f, k, p";
    String diag = diagLister.listDiag(matrix, row, col);
    assertNotNull("diag null", diagLister);
    assertFalse("diag empty", diag.isEmpty());
    assertEquals("diag differs", expected, diag);
  }

  public void testMainListSmallDiag()
  {
    //
    // Main diagonal
    int row = 0;
    int col = 0;
    char[][] smallMatrix = new char[][] { new char[] { 's' } };
    String expected = "s";
    String diag = diagLister.listDiag(smallMatrix, row, col);
    assertNotNull("diag null", diagLister);
    assertFalse("diag empty", diag.isEmpty());
    assertEquals("diag differs", expected, diag);
  }

  public void testCornerListDiag()
  {
    //
    // Lower Corner
    int row = 3;
    int col = 0;
    String expected = "m";
    String diag = diagLister.listDiag(matrix, row, col);
    assertNotNull("diag null", diagLister);
    assertFalse("diag empty", diag.isEmpty());
    assertEquals("diag differs", expected, diag);

    //
    // Upper Corner
    row = 0;
    col = 3;
    expected = "d";
    diag = diagLister.listDiag(matrix, row, col);
    assertNotNull("diag null", diagLister);
    assertFalse("diag empty", diag.isEmpty());
    assertEquals("diag differs", expected, diag);
  }

  public void testBadDimensions()
  {
    boolean caught = false;
    int row = 4;
    int col = 0;
    String diag = null;
    try
    {
      diag = diagLister.listDiag(matrix, row, col);
    }
    catch (IllegalArgumentException ignore)
    {
      caught = true;
    }
    assertTrue("error uncaught", caught);
    assertNull("diag not null - row", diag);

    caught = false;
    row = 0;
    col = 4;
    diag = null;
    try
    {
      diag = diagLister.listDiag(matrix, row, col);
    }
    catch (IllegalArgumentException ignore)
    {
      caught = true;
    }
    assertTrue("error uncaught", caught);
    assertNull("diag not null - col", diag);

    caught = false;
    row = 1;
    col = 1;
    diag = null;
    try
    {
      diag = diagLister.listDiag(matrix, row, col);
    }
    catch (IllegalArgumentException ignore)
    {
      caught = true;
    }
    assertTrue("error uncaught", caught);
    assertNull("diag not null - non-edge", diag);
  }

  public void testBadEmpty()
  {
    boolean caught = false;
    int row = 3;
    int col = 0;
    String diag = null;
    final char[][] emptyMatrix = new char[][] {};
    try
    {
      diag = diagLister.listDiag(emptyMatrix, row, col);
    }
    catch (IllegalArgumentException ignore)
    {
      caught = true;
    }
    assertTrue("error uncaught", caught);
    assertNull("diag not null - row", diag);
  }
}
