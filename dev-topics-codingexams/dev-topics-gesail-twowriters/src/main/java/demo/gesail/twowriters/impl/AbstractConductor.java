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
import demo.gesail.twowriters.Conductor;

/**
 * The shared thread creation, launch, and waiting for completion code. A
 * <code>Conductor</code> also collects statistics used for testing
 * 
 * @author Donald Trummell
 *
 * @param <C>
 *          the counter type
 */
public abstract class AbstractConductor<C> implements Conductor<C>
{
  protected final C counter;
  protected AddOne t1Addr = null;
  protected AddOne t2Addr = null;
  private Thread t1 = null;
  private Thread t2 = null;

  protected AbstractConductor(final C counter)
  {
    this.counter = counter;
  }

  @Override
  public void runme(final boolean display)
  {
    if (display)
    {
      System.out.println("\n" + getClass().getSimpleName()
          + " launching counters");
      System.out.println("Main Thread Started: "
          + Thread.currentThread().getName());
    }

    launch(display);

    if (display)
    {
      final int[] icnts = getAllIcounted();
      System.out.println("Main Thread Ended  : "
          + Thread.currentThread().getName() + ";  total counts[T1:" + icnts[0]
          + ", T2:" + icnts[1] + "] = " + (icnts[0] + icnts[1]));
    }
  }

  private void launch(final boolean display)
  {
    setupAdders(display);
    setupThreads();

    t1.start();
    t2.start();

    wait2Die(t1);
    wait2Die(t2);
  }

  protected abstract void setupAdders(final boolean display);

  private void setupThreads()
  {
    t1 = new Thread(t1Addr);
    t2 = new Thread(t2Addr);
  }

  private void wait2Die(final Thread thrd)
  {
    try
    {
      thrd.join();
    }
    catch (InterruptedException ex)
    {
      System.err.println(" Thread[" + thrd.getName() + "] interrupted, "
          + ex.getMessage());
    }
  }

  @Override
  public int[] getAllIcounted()
  {
    final int[] icnts = new int[2];
    icnts[0] = t1Addr.getIcounted();
    icnts[1] = t2Addr.getIcounted();

    return icnts;
  }

  @Override
  public C getCounter()
  {
    return counter;
  }

  @Override
  public AddOne getT1Addr()
  {
    return t1Addr;
  }

  @Override
  public AddOne getT2Addr()
  {
    return t2Addr;
  }
}
