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

import org.junit.Assert;

import demo.geturner.binarysearch.BinarySearch;

/**
 * Shared testing code to determine performance.
 * 
 * @author Donald Trummell
 */
public abstract class AbstractPerformanceChecker
{
  protected static final int TEST_SIZE = 1024 * 1024 + 1;
  protected static final int PROB_SIZE = (int) (0.75 * TEST_SIZE);
  protected final Integer[] array = defineArray(TEST_SIZE);
  protected final List<Integer> shortProbs = generateProbPoints(128);
  protected final List<Integer> probs = generateProbPoints(PROB_SIZE);
  protected BinarySearch<Integer> searchItr = null;
  protected final boolean display = false;

  protected AbstractPerformanceChecker()
  {
    super();
  }

  protected void checkSearcherSetup(final BinarySearch<Integer> searchAlt)
  {
    final int testKey = array[TEST_SIZE - 1];
    int idx = searchItr.find(array, testKey);
    Assert.assertEquals("Itr failed", TEST_SIZE - 1, idx);
    idx = searchAlt.find(array, testKey);
    Assert.assertEquals("Alt failed", TEST_SIZE - 1, idx);
    Assert.assertTrue("prob setup failed", shortProbs.size() < probs.size());
  }

  protected void warmCode(final BinarySearch<Integer> searchAlt)
  {
    doSearch(searchAlt, shortProbs);
    doSearch(searchItr, shortProbs);
  }

  protected long doSearch(final BinarySearch<Integer> searcher,
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

  protected Integer[] defineArray(final int lth)
  {
    final Integer[] array = new Integer[lth];
    for (int i = 0; i < lth; i++)
      array[i] = (i + 1);

    return array;
  }

  protected List<Integer> generateProbPoints(final int lth)
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

  protected double round2Places(double x)
  {
    final double scale2places = 100.0;

    final double signum = Math.signum(x);
    x = signum * (Math.rint(Math.abs(x * scale2places) + 0.5) / scale2places);

    return x;
  }
}