package demo.geturner.binarysearch.impl;

import demo.geturner.binarysearch.BinarySearch;

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