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
 * Shared code for synchronizing and incrementing a counter, parameterized by
 * counter type
 * 
 * @author Donald Trummell
 *
 * @param <C>
 *          the counter type
 */
public abstract class AbstractAddOne<C> implements AddOne
{
  private static final int limit = Conductor.LIMIT;
  protected final C counter;
  private final int[] reached = new int[limit + 1];
  protected final boolean display;
  private volatile int icounted = 0;
  private volatile boolean done = false;

  protected AbstractAddOne(final C counter, final boolean display)
  {
    this.counter = counter;
    this.display = display;
  }

  @Override
  public void run()
  {
    long current = -1;
    while (!done)
    {
      delay(); // required to prevent starvation if no display

      synchronized (counter)
      {
        current = getCounterValue();
        done = current >= limit;
        if (!done)
        {
          current = incrementCounter(current);
          icounted++;
          reached[(int) current]++;
        }
      }

      if (display)
        System.out.println("    [" + Thread.currentThread().getName()
            + "] counted " + icounted + " times to " + current
            + ((done) ? "; done" : ""));
    }
  }

  private void delay()
  {
    try
    {
      Thread.sleep((long) (Math.random() * 5.0));
    }
    catch (InterruptedException ignore)
    {
      // Ignored
    }
  }

  protected abstract long getCounterValue();

  protected abstract long incrementCounter(final long current);

  @Override
  public boolean isDone()
  {
    return done;
  }

  @Override
  public int getIcounted()
  {
    return icounted;
  }

  @Override
  public int[] getReached()
  {
    return reached;
  }
}