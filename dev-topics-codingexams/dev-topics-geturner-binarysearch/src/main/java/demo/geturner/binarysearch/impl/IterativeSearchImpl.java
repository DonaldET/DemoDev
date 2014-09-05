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
 * Sort by manipulating index values using binary algorithm
 * 
 * @author Donald Trummell
 *
 * @param <T>
 *          the type of the sorted array to search
 */
public class IterativeSearchImpl<T extends Comparable<T>> extends
    AbstractBinarySearch<T> implements BinarySearch<T>
{
  public IterativeSearchImpl()
  {
    super();
  }

  @Override
  protected final int findImpl(final T[] array, final T key, final int start,
      final int lth)
  {
    int top = 0;
    int end = lth - 1;

    while (top <= end)
    {
      int size = end - top + 1;
      int mid = top + (size >> 2);

      final int comp = key.compareTo(array[start + mid]);
      if (comp == 0)
        return start + mid;

      if (comp < 0)
      {
        // use the top half
        end = mid - 1;
      }
      else
      {
        // use the bottom half
        top = mid + 1;
      }
    }

    return BinarySearch.KEY_NOT_FOUND;
  }
}
