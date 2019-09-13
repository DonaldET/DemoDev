/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.geturner.binarysearch.impl;

import java.util.Arrays;

import demo.geturner.binarysearch.BinarySearch;

/**
 * Search using Java <code>Arrays</code> native binary search method.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 * @param <T>
 *          type of sorted array to search
 */
public class NativeSearchImpl<T extends Comparable<T>> extends
    AbstractBinarySearch<T> implements BinarySearch<T>
{
  public NativeSearchImpl()
  {
    super();
  }

  /**
   * Find index of key in array by finding the array midpoint and recursively
   * dividing the region in half. The array segment to consider is in positions
   * <code>[start + 0, start + lth - 1]</code>. A zero length array always
   * returns KEY_NOT_FOUND.
   */
  @Override
  protected final int findImpl(final T[] array, final T key, final int start,
      final int lth)
  {
    if (lth < 1)
      return BinarySearch.KEY_NOT_FOUND;

    final int nkey = Arrays.binarySearch(array, key);

    return nkey < 0 ? BinarySearch.KEY_NOT_FOUND : nkey;
  }
}
