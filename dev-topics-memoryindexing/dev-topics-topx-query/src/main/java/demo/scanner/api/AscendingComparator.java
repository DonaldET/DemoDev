/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.scanner.api;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Orders using <code>Game</code> bean fields that form a unique key across game
 * instances. The ordering can be ascending or descending, depending on the
 * constructor parameter.
 * 
 * @author Donald Trummell
 */
public class AscendingComparator implements Comparator<Game>, Serializable
{
  private static final long serialVersionUID = -8077339869637111095L;

  private final boolean ascending;

  public AscendingComparator(final boolean ascending)
  {
    this.ascending = ascending;
  }

  /**
   * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
   */
  @Override
  public int compare(final Game lhs, final Game rhs)
  {
    final int order = compareLeftToRight(lhs, rhs);

    return ascending ? order : -order;
  }

  private int compareLeftToRight(final Game lhs, final Game rhs)
  {
    if (lhs == rhs)
      return 0;

    if (lhs == null)
      return rhs == null ? 0 : -1;
    else if (rhs == null)
      return 1;

    int compareOrder = compareTwo(lhs.getScore(), rhs.getScore());
    if (compareOrder == 0)
    {
      compareOrder = compareTwo(lhs.getPlayerName(), rhs.getPlayerName());
      if (compareOrder == 0)
      {
        compareOrder = -compareTwo(lhs.getPlayedOn(), rhs.getPlayedOn());
        if (compareOrder == 0)
        {
          compareOrder = compareTwo(lhs.getId(), rhs.getId());
        }
      }
    }

    return compareOrder;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private int compareTwo(final Comparable x, final Comparable y)
  {
    if (x == null)
    {
      return y == null ? 0 : -1;
    }
    else if (y == null)
      return 1;
    else
      return x.compareTo(y);
  }

  // ---------------------------------------------------------------------------

  @Override
  public Comparator<Game> reversed()
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Comparator<Game> thenComparing(Comparator<? super Game> other)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <U> Comparator<Game> thenComparing(
      Function<? super Game, ? extends U> keyExtractor,
      Comparator<? super U> keyComparator)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <U extends Comparable<? super U>> Comparator<Game> thenComparing(
      Function<? super Game, ? extends U> keyExtractor)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Comparator<Game> thenComparingInt(
      ToIntFunction<? super Game> keyExtractor)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Comparator<Game> thenComparingLong(
      ToLongFunction<? super Game> keyExtractor)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Comparator<Game> thenComparingDouble(
      ToDoubleFunction<? super Game> keyExtractor)
  {
    throw new UnsupportedOperationException("Not implemented");
  }
}