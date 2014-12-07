package com.procyon.sieve.searcher.impl;

import java.util.Arrays;

import com.procyon.sieve.searcher.SieveSearcher;

public class NiaveSearcher implements SieveSearcher
{
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

  public String getName()
  {
    return "Sort All First";
  }
}
