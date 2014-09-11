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
import demo.geturner.binarysearch.impl.NativeSearchImpl;

/**
 * Test performance of Iterative and Native search methods
 * 
 * @author Donald Trummell
 */
public class SearchNativePerformanceTest extends AbstractPerformanceChecker
{
  private BinarySearch<Integer> searchNat = null;

  public SearchNativePerformanceTest()
  {
    super();
  }

  @Before
  public void setUp() throws Exception
  {
    searchItr = new IterativeSearchImpl<Integer>();
    searchNat = new NativeSearchImpl<Integer>();
  }

  @After
  public void tearDown() throws Exception
  {
    searchItr = null;
    searchNat = null;
  }

  @Test
  public void testInstantiation()
  {
    Assert.assertTrue("classes same",
        searchItr.getClass() != searchNat.getClass());

    Assert.assertTrue("searchItr not a " + BinarySearch.class,
        searchItr instanceof BinarySearch);

    Assert.assertTrue("searchNat not a " + BinarySearch.class,
        searchNat instanceof BinarySearch);

    Assert.assertEquals("wrong data size", TEST_SIZE, array.length);
  }

  @Test
  public void testPerformance()
  {
    checkSearcherSetup(searchNat);
    warmCode(searchNat);

    final int testCount = 3;

    long totItr = 0;
    for (int i = 0; i < testCount; i++)
      totItr += doSearch(searchItr, probs);

    long totAlt = 0;
    for (int i = 0; i < testCount; i++)
      totAlt += doSearch(searchNat, probs);

    if (display)
    {
      System.err.println("\n*** Searched " + TEST_SIZE + " array using "
          + PROB_SIZE + " values.");
      System.err.println("    Iterative total for " + testCount + " runs is: "
          + totItr);
      System.err.println("    Native total for " + testCount + " runs is: "
          + totAlt);
      System.err.println("    Variance: "
          + round2Places(Math.abs(totItr - totAlt) / (double) totItr));
    }

    final double faster = round2Places((double) (totAlt - totItr)
        / (double) totItr);

    Assert.assertTrue("Iterative unexpectedly slower by " + faster
        + ".  Running values are:  ITR: " + totItr + ";  REC: " + totAlt,
        faster >= -0.05);

    final double expectedfaster = 0.30;
    final double allowedVariance = 0.20;
    final double actualVariance = round2Places(Math
        .abs(faster - expectedfaster));

    Assert.assertTrue("Iterative unexpectedly faster by "
        + round2Places(faster) + ";  expected: " + expectedfaster
        + ";  actual: " + actualVariance + ";  which exceeds "
        + allowedVariance + " variance.  Running values are:  ITR: " + totItr
        + ";  ALT: " + totAlt, actualVariance <= allowedVariance);
  }
}
