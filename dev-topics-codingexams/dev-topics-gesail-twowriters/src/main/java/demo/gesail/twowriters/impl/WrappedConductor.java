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

import demo.gesail.twowriters.Conductor;
import demo.gesail.twowriters.WrappedCounter;

/**
 * A <code>Conductor</code> implementation based on a
 * <code>WrappedCounter</code> incrementing counter
 * 
 * @author Donald Trummell
 */
public class WrappedConductor extends AbstractConductor<WrappedCounter>
    implements Conductor<WrappedCounter>
{
  public WrappedConductor()
  {
    super(new WrappedCounter());
  }

  @Override
  protected void setupAdders(final boolean display)
  {
    t1Addr = new WrappedAddOne(counter, display);
    t2Addr = new WrappedAddOne(counter, display);
  }
}