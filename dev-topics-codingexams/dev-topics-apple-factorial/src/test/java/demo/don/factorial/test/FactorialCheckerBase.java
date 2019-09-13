/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.don.factorial.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

import demo.don.factorial.MathHelper;

/**
 * Test a <code>MathHelper</code> implementation for standard and boundary
 * conditions
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public abstract class FactorialCheckerBase
{
  public static final long SUM_FACTORIAL_LIMIT_VALUE = 2561327494111820314L;

  public static final int FACTORIAL_SUM_LIMIT = 20;

  private MathHelper mh = null;

  protected FactorialCheckerBase()
  {
  }

  public abstract void setUp() throws Exception;

  protected abstract long getMaxTimeAllowed();

  @After
  public void tearDown() throws Exception
  {
    mh = null;
  }

  protected MathHelper getMh()
  {
    return mh;
  }

  protected void setMh(MathHelper mh)
  {
    this.mh = mh;
  }

  // ----------------------------------------------------------------------------------------------

  @Test
  public void testToString()
  {
    final String val = mh.toString();
    assertNotNull("null string", val);
    assertTrue("no class name", val.indexOf(mh.getClass().getSimpleName()) > -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSmall()
  {
    mh.factorial(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBig()
  {
    mh.factorial(MathHelper.MAX_FACTORIAL_ARGUMENT + 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToOverflow()
  {
    computeMany(FACTORIAL_SUM_LIMIT + 1);
  }

  @Test
  public void testToSumLimit()
  {
    assertEquals("sum failed", 2561327494111820314L,
        computeMany(FACTORIAL_SUM_LIMIT));
  }

  @Test
  public void testMany()
  {
    long ex = 1;
    assertEquals("wrong value", ex, mh.factorial(0));
    assertEquals("wrong value", ex, mh.factorial(1));
    ex = 2;
    assertEquals("wrong value", ex, mh.factorial(2));
    ex = 6;
    assertEquals("wrong value", ex, mh.factorial(3));

    ex = MathHelper.MAX_FACTORIAL_VALUE;
    assertEquals("wrong value", ex,
        mh.factorial(MathHelper.MAX_FACTORIAL_ARGUMENT));
  }

  private long computeMany(final int limit)
  {
    long sum = 0;
    long last = 0;
    long next = 0;
    for (int i = 0; i <= limit; i++)
    {
      next = mh.factorial(i);
      if (next < last)
        throw new IllegalArgumentException("factorial overflow at " + i
            + ";  next: " + next + ";  last: " + last + ";  sum: " + sum);
      last = next;
      sum += next;
    }

    if (sum < last)
      throw new IllegalArgumentException("sum overflow at " + limit
          + ";  next: " + next + ";  last: " + last + ";  sum: " + sum);

    return sum;
  }
}
