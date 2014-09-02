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
import java.util.List;

/**
 * An implementation of this interface is a factory for Game data; usually test
 * data
 * 
 * @author Donald Trummell
 */
public interface GameDataBuilder extends Serializable
{
  /**
   * Defines sort orders on a list of games
   * 
   * @author Donald Trummell
   */
  public enum ListOrder
  {
    ascending(1), descending(-1), unordered(0);

    private int value;

    private ListOrder(final int value)
    {
      this.value = value;
    }

    public int getValue()
    {
      return value;
    }
  }

  public abstract boolean isInitialized();

  public abstract void initialize(final long nCases, final long baseDate);

  public abstract void destroy();

  public abstract List<Game> getData(final ListOrder sort);
}