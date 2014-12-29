/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package com.procyon.sieve.searcher;

/**
 * This project compares techniques to search an array of integer.
 * 
 * @author Donald Trummell
 */
public interface SieveSearcher
{
  public abstract String getName();

  public abstract int[] search(final int repeats, final int[] data,
      final int topCount);
}
