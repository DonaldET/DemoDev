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
 * Shared parameter checking code that all <code>BinarySearch</code>
 * implementations use, and so they need to extend this class.
 * <p>
 * As an interesting performance note, the <code>iterative</code> implementation
 * is about 40% <em>slower</em> than the <code>recursive</code> implementation.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 * @param <T>
 *          type of sorted and searchable array
 */
public abstract class AbstractBinarySearch<T extends Comparable<T>> implements
    BinarySearch<T>
{
  protected AbstractBinarySearch()
  {
    super();
  }

  @Override
  public int find(final T[] array, final T key)
  {
    if (array == null)
      throw new IllegalArgumentException("array null");

    if (key == null)
      throw new IllegalArgumentException("key null");

    final int lth = array.length;
    if (lth < 1)
      return KEY_NOT_FOUND;

    return findImpl(array, key, 0, array.length);
  }

  /**
   * Find index of key in array or return KEY_NOT_FOUND.
   *
   * @param array
   *          the array to search
   * @param key
   *          the key to locate
   * @param start
   *          the starting position <code>[0, lth)</code>.
   * @param lth
   *          the length of the array to search (non-negative).
   * @return the index of the matching entry or KEY_NOT_FOUND
   */
  protected abstract int findImpl(final T[] array, final T key,
      final int start, final int lth);
}