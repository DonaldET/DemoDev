/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package com.procyon.sieve;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;

import com.procyon.sieve.runner.SieveSearchRunner;
import com.procyon.sieve.runner.impl.SieveSearchRunnerImpl;
import com.procyon.sieve.searcher.SieveSearcher;
import com.procyon.sieve.searcher.impl.BinaryArraySearcher;
import com.procyon.sieve.searcher.impl.NiaveSearcher;
import com.procyon.sieve.searcher.impl.RadixSearcher;
import com.procyon.sieve.searcher.impl.TreeMapSearcher;

/**
 * Runs various sieve searcher instances, implemented using different algorithms
 * while timing the runs configured with different parameters.
 * 
 * @author Donald Trummell
 */
public class SieveDemo
{
  public static final int TEST_SIZE = 1000000;
  public static final int TOP_PICKS = 30;
  public static final int REPEAT_COUNT = 20;

  public SieveDemo()
  {
  }

  public static void main(String[] args)
  {
    final int testSize = TEST_SIZE;
    final int topPicks = TOP_PICKS;
    final int bound = testSize / 4;
    System.out.println("Testing Sieve Search over " + testSize
        + " Data Elements in [1 .. " + bound + "]");
    System.out.println("");
    System.out.flush();

    System.out.println("  -- Creating " + testSize + " data elements");
    final int[] data = createData(testSize, bound);
    System.out.println("  The first " + (topPicks + 1) + " data values are:");
    final List<Integer> dataList = new ArrayList<Integer>();
    for (int i = 0; i <= topPicks; i++)
      dataList.add(data[i]);
    System.out.println("  " + dataList);

    SieveSearchRunner runner = new SieveSearchRunnerImpl();
    final int repeatCount = REPEAT_COUNT;

    SieveSearcher searcher = new NiaveSearcher();
    String searcherID = searcher.getName();
    doATest(runner, searcherID, searcher, repeatCount, data, topPicks, 0);

    searcherID = "Binary Search of Array";
    searcher = new BinaryArraySearcher();
    searcherID = searcher.getName();
    doATest(runner, searcherID, searcher, repeatCount, data, topPicks, 0);

    searcher = new TreeMapSearcher();
    searcherID = searcher.getName();
    doATest(runner, searcherID, searcher, 2 * repeatCount, data, topPicks, 0);

    searcher = new RadixSearcher(bound);
    searcherID = searcher.getName();
    doATest(runner, searcherID, searcher, 10 * repeatCount, data, topPicks,
        bound);

    System.out.println("\nAll Tests complete");
  }

  private static void doATest(final SieveSearchRunner runner,
      String searcherID, final SieveSearcher searcher, final int repeatCount,
      final int[] data, final int topPicks, final int optionalBound)
  {
    System.out.println("\n  -- Creating a " + searcherID
        + " searcher and repeating tests " + repeatCount + " times in runner");
    runner.init(repeatCount, data, topPicks, searcher);

    if (optionalBound > 0)
      System.out.println("  -- Using bound " + optionalBound);

    Long start = System.currentTimeMillis();
    final int[] results = runner.runTest();
    Long elapsed = System.currentTimeMillis() - start;

    Validate.notNull(results, "results null");
    Validate.isTrue(results.length == topPicks,
        "unexpected result length, expected " + topPicks + ", got ",
        results.length);

    float perExec = (float) elapsed / repeatCount;
    perExec = (float) (Math.rint(perExec * 10.0 + 0.5) / 10.0);
    System.out.println("Found the " + topPicks + " results in " + elapsed
        + " milliseconds, or " + perExec + " milliseconds per scan, are:");
    final List<Integer> resultList = new ArrayList<Integer>(topPicks);
    for (int i = 0; i < topPicks; i++)
      resultList.add(results[i]);
    System.out.println(resultList);

    System.out.println("  -- " + searcherID + " test complete");
  }

  private static int[] createData(final int testSize, final int bound)
  {
    List<Integer> testDataList = new ArrayList<Integer>(testSize);
    for (int i = 0; i < testSize; i++)
      testDataList.add(Math.min(i % bound + 1, testSize));

    Collections.shuffle(testDataList);

    final int[] testData = new int[testSize];

    int i = 0;
    for (Integer s : testDataList)
      testData[i++] = s;

    return testData;
  }
}
