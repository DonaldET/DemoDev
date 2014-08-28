/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.geturner.binarysearch;

/**
 * Generalized binary search of an array of <code>Comparable</code> items
 * 
 * @author Donald Trummell
 *
 * @param <T>
 *          type of sorted and searchable items
 */
public interface BinarySearch<T extends Comparable<T>>
{
  public static final int KEY_NOT_FOUND = -1;

  /**
   * Precondition is that the array is sorted
   * 
   * @param array
   *          the sorted array to search
   * @param key
   *          the key value to locate
   * @return the array index of located key or <code>KEY_NOT_FOUND</code>
   */
  public abstract int find(final T[] array, final T key);
}