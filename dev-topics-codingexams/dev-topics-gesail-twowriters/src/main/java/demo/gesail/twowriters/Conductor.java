/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.gesail.twowriters;

/**
 * A <code>Conductor</code> creates, launches, and waits for two
 * counter-incrementing threads to complete. It can optionally display thread
 * actions during the execution, and return the <code>AddOne</code>
 * implementations used to collect testing values.
 * <p>
 * A <code>Conductor</code> is expected to alternate incrementing the counter
 * between two threads. Testing verifies that each thread participates, running
 * as an application allows visual verification of the alternation.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 * @param <C>
 *          the counter type
 */
public interface Conductor<C>
{
  public static final int LIMIT = 100;

  public abstract void runme(final boolean display);

  public abstract int[] getAllIcounted();

  public abstract C getCounter();

  public abstract AddOne getT1Addr();

  public abstract AddOne getT2Addr();
}