/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.gesail.twowriters.impl;

import demo.gesail.twowriters.AddOne;

/**
 * A <code>Conductor</code> that uses an array as a counter. This class has a
 * main method and may be used to display the incrementing run to offer a visual
 * confirmation of the run
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class ArrayAddOne extends AbstractAddOne<long[]> implements AddOne
{
  public ArrayAddOne(final long[] counter, final boolean display)
  {
    super(counter, display);
  }

  @Override
  protected long getCounterValue()
  {
    return counter[0];
  }

  @Override
  protected long incrementCounter(final long current)
  {
    final long nxt = current + 1;
    counter[0] = nxt;

    return nxt;
  }
}