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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.procyon.sieve.searcher.SieveSearcher;

public class TreeMapSearcher implements SieveSearcher
{
  public static class Ends
  {
    public int endPoint;
    public int prior2End;

    public Ends(final int endPoint, final int prior2End)
    {
      this.endPoint = endPoint;
      this.prior2End = prior2End;
    }
  }

  @Override
  public int[] search(final int repeats, final int[] data, final int topCount)
  {
    final Map<Integer, Integer> topPicks = new TreeMap<Integer, Integer>();

    for (int i = 0; i < topCount && i < data.length; i++)
      topPicks.put(data[i], i);

    if (data.length <= topPicks.size())
      return copyToArray(topCount, topPicks);

    Ends ends = findEnd(topPicks);
    for (int i = topCount; i < data.length; i++)
    {
      Integer keyValue = data[i];
      if (keyValue < ends.endPoint && !topPicks.containsKey(keyValue))
      {
        topPicks.put(keyValue, i);
        ends = findEnd(topPicks);
        topPicks.remove(ends.endPoint);
        ends.endPoint = ends.prior2End;
      }
    }

    return copyToArray(topCount, topPicks);
  }

  private int[] copyToArray(final int topCount,
      final Map<Integer, Integer> topPicks)
  {
    final int[] results = new int[topCount];

    final Iterator<Entry<Integer, Integer>> topPickItr = topPicks.entrySet()
        .iterator();

    int j = 0;
    while (topPickItr.hasNext())
    {
      final Entry<Integer, Integer> e = topPickItr.next();
      results[j++] = e.getKey();
    }

    return results;
  }

  private Ends findEnd(final Map<Integer, Integer> topPicks)
  {
    final Iterator<Entry<Integer, Integer>> topPickItr = topPicks.entrySet()
        .iterator();

    Ends ends = new Ends(Integer.MIN_VALUE, Integer.MIN_VALUE);

    while (topPickItr.hasNext())
    {
      final Entry<Integer, Integer> e = topPickItr.next();
      ends.prior2End = ends.endPoint;
      ends.endPoint = e.getKey();
    }

    return ends;
  }

  @Override
  public String getName()
  {
    return "TreeMap - Red-Black Tree";
  }
}
