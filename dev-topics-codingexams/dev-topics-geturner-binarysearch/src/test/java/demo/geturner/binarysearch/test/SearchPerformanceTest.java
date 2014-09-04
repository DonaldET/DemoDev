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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.geturner.binarysearch.BinarySearch;
import demo.geturner.binarysearch.impl.IterativeSearchImpl;
import demo.geturner.binarysearch.impl.RecursiveSearchImpl;

/**
 * Test performance of both search methods
 * 
 * @author Donald Trummell
 */
public class SearchPerformanceTest
{
  private static final int TEST_SIZE = 1024 * 1024 + 1;
  private static final int PROB_SIZE = (int) (0.75 * TEST_SIZE);

  private final Integer[] array = defineArray(TEST_SIZE);
  private final List<Integer> shortProbs = generateProbPoints(128);
  private final List<Integer> probs = generateProbPoints(PROB_SIZE);

  private BinarySearch<Integer> searchItr = null;
  private BinarySearch<Integer> searchRec = null;

  private final boolean display = false;

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
    checkSearcherSetup();
    warmCode();

    final int testCount = 3;

    long totItr = 0;
    for (int i = 0; i < testCount; i++)
      totItr += doSearch(searchItr, probs);

    long totRec = 0;
    for (int i = 0; i < testCount; i++)
      totRec += doSearch(searchRec, probs);

    if (display)
    {
      // *** Searched 1048577 array using 786432 values.
      // Iterative total for 3 runs is: 414
      // Recursive total for 3 runs is: 268
      // Variance: 0.36

      System.err.println("\n*** Searched " + TEST_SIZE + " array using "
          + PROB_SIZE + " values.");
      System.err.println("    Iterative total for " + testCount + " runs is: "
          + totItr);
      System.err.println("    Recursive total for " + testCount + " runs is: "
          + totRec);
      System.err.println("    Variance: "
          + round2Places(Math.abs(totItr - totRec) / (double) totItr));
    }

    final double slower = round2Places((double) (totItr - totRec)
        / (double) totItr);

    final double expectedSlower = 0.40;
    final double allowedVariance = 0.20;
    final double actualVariance = Math.abs(slower - expectedSlower);
    Assert.assertTrue(
        "iterative unexpectedly differs from recursive, is slower by " + slower
            + " but expected: " + expectedSlower + ", leading to a "
            + actualVariance + " variance, which exceeds " + allowedVariance
            + ".  Running values are:  ITR: " + totItr + ";  REC: " + totRec,
        slower < 0.0 || actualVariance > allowedVariance);
  }

  // ---------------------------------------------------------------------------

  private double round2Places(double x)
  {
    final double scale2places = 100.0;

    final double signum = Math.signum(x);
    x = signum * (Math.rint(Math.abs(x * scale2places) + 0.5) / scale2places);

    return x;
  }

  private void checkSearcherSetup()
  {
    final int testKey = array[TEST_SIZE - 1];
    int idx = searchItr.find(array, testKey);
    Assert.assertEquals("Itr failed", TEST_SIZE - 1, idx);
    idx = searchRec.find(array, testKey);
    Assert.assertEquals("Itr failed", TEST_SIZE - 1, idx);
    Assert.assertTrue("prob setup failed", shortProbs.size() < probs.size());
  }

  private void warmCode()
  {
    doSearch(searchItr, shortProbs);
    doSearch(searchRec, shortProbs);
  }

  private long doSearch(final BinarySearch<Integer> searcher,
      final List<Integer> probs)
  {
    final long start = System.currentTimeMillis();
    for (Integer p : probs)
    {
      final int index = searcher.find(array, p);
      Assert.assertNotEquals("unable to locate " + p.intValue(),
          BinarySearch.KEY_NOT_FOUND, index);
    }

    return System.currentTimeMillis() - start;
  }

  // ---------------------------------------------------------------------------

  private Integer[] defineArray(final int lth)
  {
    final Integer[] array = new Integer[lth];
    for (int i = 0; i < lth; i++)
      array[i] = (i + 1);

    return array;
  }

  private List<Integer> generateProbPoints(final int lth)
  {
    final List<Integer> shuffled = new ArrayList<Integer>(array.length);
    for (int i = 0; i < array.length; i++)
      shuffled.add(array[0]);
    Collections.shuffle(shuffled);

    final List<Integer> probs = new ArrayList<Integer>(lth);
    for (int i = 0; i < lth; i++)
      probs.add(shuffled.get(i));

    return probs;
  }
}
