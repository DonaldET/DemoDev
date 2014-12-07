package com.procyon.sieve.searcher.impl;

import com.procyon.sieve.searcher.SieveSearcher;

public class RadixSearcher implements SieveSearcher
{
  final int bound;

  public RadixSearcher(final int bound)
  {
    this.bound = bound;
  }

  public int[] search(final int repeats, final int[] data, final int topCount)
  {
    byte[] flags = new byte[bound + 1];

    for (int i = 0; i < data.length; i++)
      flags[data[i]] = 1;

    final int[] results = new int[topCount];
    int foundCount = 0;
    for (int i = 1; foundCount < topCount && i <= flags.length; i++)
    {
      if (flags[i] != 0)
        results[foundCount++] = i;
    }

    return results;
  }

  public String getName()
  {
    return "Radix Search";
  }
}
