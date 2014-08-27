package demo.geturner.binarysearch.impl;

import demo.geturner.binarysearch.BinarySearch;

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
    if (lth < 1)
      return BinarySearch.KEY_NOT_FOUND;

    int top = 0;
    int end = lth - 1;

    while (top <= end)
    {
      int size = end - top + 1;
      int half = size / 2;
      int mid = top + half;

      int comp = key.compareTo(array[start + mid]);
      if (comp == 0)
        return start + mid;

      if (comp < 0)
        end = mid - 1;
      else
        top = mid + 1;
    }

    return BinarySearch.KEY_NOT_FOUND;
  }
}
