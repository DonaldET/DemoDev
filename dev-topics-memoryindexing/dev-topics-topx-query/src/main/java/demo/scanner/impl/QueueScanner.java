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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import demo.scanner.api.Game;
import demo.scanner.api.TopXScanner;

/**
 * A concrete <code>TopXScanner</code> implementation that uses a
 * <code>PriorityQueue</code> to collect the top <em>X</em> elements
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class QueueScanner extends AbstractTopXScanner implements TopXScanner
{
  private static final long serialVersionUID = 8541070747341231842L;

  public QueueScanner()
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

    final PriorityQueue<Game> pq = new PriorityQueue<Game>();

    //
    // The first X scores just go into the queue, no check for replacement

    do
    {
      final Game g = it.next();
      pq.offer(g);
      more = it.hasNext();
    }
    while (more && pq.size() < x);

    //
    // The remaining scores replace something in the queue when added to the
    // queue

    while (more)
    {
      final Game g = it.next();
      final Game least = pq.peek();
      if (g.compareTo(least) > 0)
      {
        pq.remove();
        pq.offer(g);
      }
      more = it.hasNext();
    }

    //
    // Pull out ordered from least value to largest value, and then reverse
    // the order so largest to least is entered into output

    final Deque<Game> lifo = new ArrayDeque<Game>();
    Game g = pq.poll();
    do
    {
      lifo.push(g);
      g = pq.poll();
    }
    while (g != null);

    do
    {
      g = lifo.pop();
      leaders.add(g);
    }
    while (!lifo.isEmpty());
  }
}
