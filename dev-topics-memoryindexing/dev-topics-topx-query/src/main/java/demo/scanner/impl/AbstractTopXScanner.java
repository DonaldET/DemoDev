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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import demo.scanner.api.Game;
import demo.scanner.api.TopXScanner;

/**
 * Encapsulates the input checking that a stream scanner should perform; offers
 * and extension point in the template method <code>getTopX</code>. Working
 * scanners are expected to extend this class, providing uniform error checking.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public abstract class AbstractTopXScanner implements TopXScanner
{
  private static final long serialVersionUID = -8789578044244816344L;

  protected AbstractTopXScanner()
  {
  }

  /**
   * Shared code for all implementations finding the "<em>top</em>" scoring
   * games
   */
  @Override
  public List<Game> topX(final int x, final Iterator<Game> it)
  {
    if (x < 1 || x > MAX_TOP_GAME_LIST_SIZE)
      throw new IllegalArgumentException(x + " not between 1 and "
          + MAX_TOP_GAME_LIST_SIZE);

    if (it == null)
      throw new IllegalArgumentException("it null");

    final List<Game> leaders = new ArrayList<Game>();

    getTopX(x, it, leaders);

    return leaders;
  }

  /**
   * Template method that is overridden by concrete implementation supplying the
   * algorithm to capture the top <em>X</em> entries
   *
   * @param x
   *          the number of top elements to capture
   * @param it
   *          the input data
   * @param leaders
   *          the collected top elements in descenging order
   */
  protected abstract void getTopX(final int x, final Iterator<Game> it,
      final List<Game> leaders);
}