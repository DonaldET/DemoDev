package demo.geturner.binarysearch.impl;

import demo.geturner.binarysearch.BinarySearch;

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
    if (lth < 1)
      return KEY_NOT_FOUND;

    if (lth > 1)
    {
      // Length is 2+
      final int half = lth / 2;
      final int mid = start + half;
      if (mid >= array.length)
        return KEY_NOT_FOUND;

      final int key2this = key.compareTo(array[mid]);
      if (key2this == 0)
        return mid;

      if (key2this < 0)
        return findImpl(array, key, start, half);
      else
      {
        // Account for even half donating the mid point
        return findImpl(array, key, mid + 1, (2 * half == lth) ? half - 1
            : half);
      }
    }
    else if (lth > 0)
    {
      // Length is 1
      final int index = key.compareTo(array[start]) == 0 ? start
          : KEY_NOT_FOUND;
      return index;
    }

    // Length is 0
    return KEY_NOT_FOUND;
  }
}
