/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.scanner.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import demo.scanner.api.Game;
import demo.scanner.api.TopXScanner;

/**
 * Using a sorted array of top <em>X</em> elements with binary search to
 * determine the correct replacement or insertion position in the candidate
 * array
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class SortedArrayScanner extends AbstractTopXScanner implements
    TopXScanner
{
  private static final long serialVersionUID = 1707086795590231230L;

  public SortedArrayScanner()
  {
    super();
  }

  /**
   * Template method implementing the collection of top elements
   */
  @Override
  protected void getTopX(final int x, final Iterator<Game> it,
      final List<Game> leaders)
  {
    //
    // Handle nothing to collect

    boolean more = it.hasNext();
    if (!more)
      return;

    int actualX = x;
    Game[] topAry = new Game[actualX];

    //
    // The first X just go into the array, no check for replacement

    int captured = 0;
    do
    {
      final Game g = it.next();
      topAry[captured++] = g;
      more = it.hasNext();
    }
    while (more && captured < actualX);

    //
    // The remaining games replace something in the array when added

    if (more)
    {
      Arrays.sort(topAry); // Initially sort, maintain order over scan
      while (more)
      {
        final Game g = it.next();
        handleTopGameCandidate(g, topAry);
        more = it.hasNext();
      }
    }
    else
    {
      //
      // Early EOF, so array not full, adjust size

      if (captured < actualX)
      {
        actualX = captured;
        final Game[] shortAry = new Game[actualX];
        System.arraycopy(topAry, 0, shortAry, 0, captured);
        topAry = shortAry;
        Arrays.sort(topAry);
      }
    }

    //
    // Enter captured top X into output in reverse order

    for (int i = actualX; i >= 1; i--)
      leaders.add(topAry[i - 1]);
  }

  /**
   * We have potential top game, stuff into array if we need to keep it
   *
   * @param g
   *          the candidate game
   * @param topAry
   *          the top game candidates
   */
  private void handleTopGameCandidate(final Game g, final Game[] topAry)
  {
    final int x = topAry.length;

    if (g.compare(g, topAry[0]) > 0)
    {
      //
      // G greater than lower bound - capture and put into array

      final int highCompare = g.compare(g, topAry[x - 1]);
      if (highCompare < 0)
      {
        //
        // G is below upper bound and above lower bound - insert into array,
        // but ignore if already in array or before the first element

        final int idx = Arrays.binarySearch(topAry, g);
        if (idx < 0)
        {
          final int ip = -idx - 1;
          if (ip > 1)
          {
            if (ip >= x)
              throw new IllegalStateException("array sort and search failed");

            putGameIntoArrayBefore(g, topAry, ip);
          }
        }
      }
      else if (highCompare > 0)
      {
        //
        // G exceeds upper bound - so replace top in array for sure

        for (int i = 1; i < x; i++)
          // Optimization over arraycopy
          topAry[i - 1] = topAry[i]; // no, intermediate buffer

        topAry[x - 1] = g;
      }
    }
  }

  /**
   * Slide left (lower) part of the array left, before idx. We are only keeping
   * largest set of g, so insertion before [0] (smallest in set) is illegal.
   * Beyond end of array (idx >= x) is also illegal
   *
   * @param g
   *          the candidate game
   * @param topAry
   *          the top game candidates
   * @param ip
   *          index before which we insert the new candidate
   */
  private void putGameIntoArrayBefore(final Game g, final Game[] topAry,
      final int ip)
  {
    if (ip < 1)
      throw new IllegalArgumentException("ip " + ip + " negative or small");

    final int x = topAry.length;
    if (ip >= x)
      throw new IllegalArgumentException("ip " + ip
          + " equals or exceeds length " + x);

    //
    // Move left, [1, (IP - 1)] => [0, (IP-2)] as a block of (ip - 1) entries

    for (int i = 1; i <= ip - 1; i++)
    {
      // Optimization over arraycopy
      topAry[i - 1] = topAry[i]; // no temporary buffer
    }

    topAry[ip - 1] = g;
  }
}
