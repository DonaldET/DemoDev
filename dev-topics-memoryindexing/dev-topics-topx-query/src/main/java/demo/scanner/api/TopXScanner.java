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
import java.util.Iterator;
import java.util.List;

/**
 * A <em>Scanner</em> implementation summarizes information about data items it
 * examines through an iterator; primarily capturing the <em>top X</em> scores
 * in a gaming context, and providing them in descending order.
 * <p>
 * There are two primary implementations; a PriorityQueue based scanner and a
 * sorted array with binary search scanner.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface TopXScanner extends Serializable
{
  /**
   * The maximum size of a top scoring game collection over an iterator
   */
  public static final int MAX_TOP_GAME_LIST_SIZE = 100;

  /**
   * Find the <em>x</em> largest elements, in descending order, from the input
   * data
   *
   * @param x
   *          the number of <em>top</em> elements to capture
   * @param it
   *          the input data iterator
   * @return the top <em>x</em> elements
   */
  public abstract List<Game> topX(final int x, final Iterator<Game> it);
}