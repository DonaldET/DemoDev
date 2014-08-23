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
 * An <code>AddOne</code> implementation is a thread <code>Runnable</code> that
 * increments the counter and records the work done for testing, and expected to
 * return the values on completion
 * 
 * @author Donald Trummell
 */
public interface AddOne extends Runnable
{
  public abstract boolean isDone();

  public abstract int getIcounted();

  public abstract int[] getReached();
}