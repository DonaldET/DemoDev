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
 * A wrapper class around a long value used as a counter
 * 
 * @author Donald Trummell
 */
public class WrappedCounter
{
  private long counter = 0;

  public WrappedCounter()
  {
  }

  public long increment()
  {
    return ++counter;
  }

  public long getCounter()
  {
    return counter;
  }
}
