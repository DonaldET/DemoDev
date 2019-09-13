/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.factorial.impl;

import demo.don.factorial.MathHelper;

/**
 * The conventional recursion computed factorial value
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class SimpleMathHelper implements MathHelper
{
  public SimpleMathHelper()
  {
  }

  @Override
  public String getName()
  {
    return "Simple";
  }

  /**
   * @see demo.don.factorial.MathHelper#factorial(int)
   */
  @Override
  public long factorial(final int n)
  {
    if (n < 0)
      throw new IllegalArgumentException("factorial for " + n + " undefined");

    if (n > MAX_FACTORIAL_ARGUMENT)
      throw new IllegalArgumentException(n
          + " is too big to compute factorial, max is "
          + MAX_FACTORIAL_ARGUMENT);

    if (n < 2)
      return 1L;

    final long nextVal = factorial(n - 1);
    if (nextVal < 1L)
      throw new IllegalStateException("fact(" + (n - 1) + ") is bad");

    final long retVal = n * nextVal;
    if (retVal < 1L)
      throw new IllegalStateException("fact(" + n + ") is bad");

    return retVal;
  }

  @Override
  public String toString()
  {
    final StringBuffer msg = new StringBuffer();
    msg.append("[");
    msg.append(getClass().getSimpleName());
    msg.append(" - 0x");
    msg.append(Integer.toHexString(hashCode()));
    msg.append("]");

    return msg.toString();
  }
}
