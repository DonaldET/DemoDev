/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package com.procyon.sieve.runner.impl;

import org.apache.commons.lang.Validate;

import com.procyon.sieve.runner.SieveSearchRunner;
import com.procyon.sieve.searcher.SieveSearcher;

/**
 * Runs a sieve search instance that incorporates an algorithm run repeatedly
 * while being timmed
 * 
 * @author Donald Trummell
 */
public class SieveSearchRunnerImpl implements SieveSearchRunner
{
  private boolean initialized = false;
  private int repeats;
  private int[] data;
  private int topCount;
  private int inputSize;
  private SieveSearcher searcher;

  public SieveSearchRunnerImpl()
  {
  }

  public void init(final int repeats, final int[] data, final int topCount,
      final SieveSearcher searcher)
  {
    Validate.isTrue(repeats > 0, "repeats < 1, ", repeats);
    Validate.notNull(data, "data null");
    inputSize = data.length;
    Validate.isTrue(inputSize > 0, "inputSize < 1, ", inputSize);
    Validate.isTrue(topCount > 0, "topCount < 1, ", topCount);
    Validate.isTrue(inputSize >= topCount, "inputSize < topCount");
    Validate.notNull(searcher, "searcher null");

    this.repeats = repeats;
    this.data = data;
    this.topCount = topCount;
    this.searcher = searcher;

    initialized = true;

    return;
  }

  public int[] runTest()
  {
    Validate.isTrue(initialized, "uninitialized");

    int[] topValues = null;
    for (int i = 0; i < repeats; i++)
    {
      topValues = searcher.search(repeats, data, topCount);
      Validate.notNull(topValues, "test iteration " + (i + 1)
          + " returned null");
      int lth = topValues.length;
      Validate.isTrue(lth == topCount, "bad return length " + lth
          + ", expected ", topCount);
    }

    return topValues;
  }

  public int getRepeats()
  {
    return repeats;
  }

  public int[] getData()
  {
    return data;
  }

  public int getTopCount()
  {
    return topCount;
  }

  public SieveSearcher getSearcher()
  {
    return searcher;
  }
}
