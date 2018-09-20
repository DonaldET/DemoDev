/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.geturner.binarysearch.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.geturner.binarysearch.BinarySearch;
import demo.geturner.binarysearch.impl.IterativeSearchImpl;
import demo.geturner.binarysearch.impl.RecursiveSearchImpl;

/**
 * Test performance of Iterative and Recursive binary search methods
 * 
 * @author Donald Trummell
 */
public class SearchPerformanceTest extends AbstractPerformanceChecker
{
  private BinarySearch<Integer> searchRec = null;

  public SearchPerformanceTest()
  {
    super();
  }

  @Before
  public void setUp() throws Exception
  {
    searchItr = new IterativeSearchImpl<Integer>();
    searchRec = new RecursiveSearchImpl<Integer>();
  }

  @After
  public void tearDown() throws Exception
  {
    searchItr = null;
    searchRec = null;
  }

  //@SuppressWarnings("cast")
  @Test
  public void testInstantiation()
  {
    Assert.assertTrue("classes same",
        searchItr.getClass() != searchRec.getClass());

    Assert.assertTrue("searchItr not a " + BinarySearch.class,
        searchItr instanceof BinarySearch);

    Assert.assertTrue("searchRec not a " + BinarySearch.class,
        searchRec instanceof BinarySearch);

    Assert.assertEquals("wrong data size", TEST_SIZE, array.length);
  }

  @Test
  public void testPerformance()
  {
    checkSearcherSetup(searchRec);
    warmCode(searchRec);

    final int testCount = 3;

    long totRec = 0;
    for (int i = 0; i < testCount; i++)
      totRec += doSearch(searchRec, probs);

    long totItr = 0;
    for (int i = 0; i < testCount; i++)
      totItr += doSearch(searchItr, probs);

    if (display)
    {
      System.err.println("\n*** Searched " + TEST_SIZE + " array using "
          + PROB_SIZE + " values.");
      System.err.println("    Iterative total for " + testCount + " runs is: "
          + totItr);
      System.err.println("    Recursive total for " + testCount + " runs is: "
          + totRec);
      System.err.println("    Variance: "
          + round2Places(Math.abs(totItr - totRec) / (double) totItr));
    }

    final double faster = round2Places((double) (totRec - totItr)
        / (double) totItr);

    Assert.assertTrue("Iterative unexpectedly slower by " + faster
        + ".  Running values are:  ITR: " + totItr + ";  REC: " + totRec,
        faster >= 0.0);

    final double expectedfaster = 1.00;
    final double allowedVariance = 1.0;
    final double actualVariance = round2Places(Math
        .abs(faster - expectedfaster));

    Assert.assertTrue("Iterative unexpectedly faster by "
        + round2Places(faster) + ";  expected: " + expectedfaster
        + ";  actual: " + actualVariance + ";  which exceeds "
        + allowedVariance + " variance.  Running values are:  ITR: " + totItr
        + ";  REC: " + totRec, actualVariance <= allowedVariance);
  }
}
