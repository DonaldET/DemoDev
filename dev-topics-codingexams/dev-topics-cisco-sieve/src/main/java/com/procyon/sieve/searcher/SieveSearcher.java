package com.procyon.sieve.searcher;

public interface SieveSearcher
{
  public abstract String getName();
  
  public abstract int[] search(final int repeats, final int[] data,
      final int topCount);
}
