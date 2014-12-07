package com.procyon.sieve.runner;

import com.procyon.sieve.searcher.SieveSearcher;

public interface SieveSearchRunner
{
  public abstract void init(final int repeats, final int[] data,
      final int topCount, final SieveSearcher searcher);

  public abstract int[] runTest();

  public abstract int getRepeats();

  public abstract int[] getData();

  public abstract int getTopCount();

  public abstract SieveSearcher getSearcher();
}
