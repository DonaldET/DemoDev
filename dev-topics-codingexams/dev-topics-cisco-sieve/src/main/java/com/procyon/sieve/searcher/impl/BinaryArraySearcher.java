package com.procyon.sieve.searcher.impl;

import java.util.Arrays;

import com.procyon.sieve.searcher.SieveSearcher;

public class BinaryArraySearcher implements SieveSearcher
{
  public static class Ends
  {
    public int endPoint;
    public int prior2End;

    public Ends(final int endPoint, final int prior2End)
    {
      this.endPoint = endPoint;
      this.prior2End = prior2End;
    }
  }

  public int[] search(final int repeats, final int[] data, final int topCount)
  {
    final int[] topPicks = new int[topCount];

    int lth = Math.min(data.length, topCount);
    System.arraycopy(data, 0, topPicks, 0, lth);
    Arrays.sort(topPicks);

    if (data.length <= topPicks.length)
      return topPicks;

    final int endIndex = topCount - 1;
    int endPointValue = topPicks[endIndex];
    for (int i = topCount; i < data.length; i++)
    {
      final Integer keyValue = data[i];
      if (keyValue < endPointValue)
      {
        //
        // Look and see if we are already there

        int ip = Arrays.binarySearch(topPicks, keyValue);
        if (ip < 0)
        {
          // ip = -(insertion_point) - 1, so insertion_point = -(ip + 1)

          final int insertP = -ip - 1;
          if (insertP <= endIndex)
          {
            if (insertP != endIndex)
            {
              lth = endIndex - insertP;
              if (lth > 0)
                System.arraycopy(topPicks, insertP, topPicks, insertP + 1, lth);
            }
            topPicks[insertP] = keyValue;
          }
        }
      }
    }

    return topPicks;
  }

  public String getName()
  {
    return "Sorted Array / Binary Search";
  }
}
