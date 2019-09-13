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

import demo.geturner.binarysearch.BinarySearch;

/**
 * Search by recursively invoking this method on the greater or lesser half of
 * the remaining values; this is a binary search implementation.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 * @param <T>
 *          type of sorted array to search
 */
public class RecursiveSearchImpl<T extends Comparable<T>> extends
    AbstractBinarySearch<T> implements BinarySearch<T>
{
  public RecursiveSearchImpl()
  {
    super();
  }

  /**
   * Find index of key in array by finding the array midpoint and recursively
   * dividing the region in half. The array segment to consider is in positions
   * <code>[start + 0, start + lth - 1]</code>. A zero length array always
   * returns KEY_NOT_FOUND.
   * <p>
   * If <code>lth</code> is <strong><em>odd</em></strong> and <code>lth</code> >
   * 2, OR <code>lth</code> is <strong><em>even</em></strong>, then:
   * <ul>
   * <li><em>top half</em> is in <code>[0, lth / 2 - 1]</code>.</li>
   * <li><em>mid</em> is in <code>[lth /2, lth /2]</code>.</li>
   * <li><em>bottom half</em> is in <code>[lth / 2 + 1, lth - 1]</code>.</li>
   * </ul>
   */
  @Override
  protected final int findImpl(final T[] array, final T key, final int start,
      final int lth)
  {
    final int half = lth >> 1;
    final int mid = start + half;

    while (lth > 0)
    {
      final int key2this = key.compareTo(array[mid]);

      if (key2this < 0)
        return findImpl(array, key, start, half);
      else if (key2this > 0)
      {
        // Account for even half donating the mid point
        return findImpl(array, key, mid + 1, (2 * half == lth) ? half - 1
            : half);
      }
      else
        return mid;
    }

    return BinarySearch.KEY_NOT_FOUND;
  }
}
