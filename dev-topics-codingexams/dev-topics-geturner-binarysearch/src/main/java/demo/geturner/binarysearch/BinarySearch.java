package demo.geturner.binarysearch;

public interface BinarySearch<T extends Comparable<T>>
{
  public static final int KEY_NOT_FOUND = -1;

  public abstract int find(final T[] array, final T key);
}