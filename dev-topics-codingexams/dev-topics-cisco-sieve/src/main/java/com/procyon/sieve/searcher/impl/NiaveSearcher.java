/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package com.procyon.sieve.searcher.impl;

import java.util.Arrays;

import com.procyon.sieve.searcher.SieveSearcher;

public class NiaveSearcher implements SieveSearcher
{
  @Override
  public int[] search(final int repeats, final int[] data, final int topCount)
  {
    int[] copy = data.clone();
    Arrays.sort(copy);

    int[] result = new int[topCount];
    int j = 0;
    result[j] = copy[0];
    for (int i = 1; j < topCount - 1 && i < data.length; i++)
    {
      int v = copy[i];
      if (v != result[j])
        result[++j] = v;
    }

    return result;
  }

  @Override
  public String getName()
  {
    return "Sort All First";
  }
}
