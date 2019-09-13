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
import demo.gesail.twowriters.WrappedCounter;

/**
 * An <code>AddOne</code> based on a <code>WrappedCounter</code>
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class WrappedAddOne extends AbstractAddOne<WrappedCounter> implements
    AddOne
{
  public WrappedAddOne(final WrappedCounter counter, final boolean display)
  {
    super(counter, display);
  }

  @Override
  protected long getCounterValue()
  {
    return counter.getCounter();
  }

  @Override
  protected long incrementCounter(final long current)
  {
    final long nxt = counter.increment();

    return nxt;
  }
}