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

import org.junit.Before;
import org.junit.Test;

import demo.don.factorial.impl.PreComputedMathHelper;

/**
 * Test the Precomputed implementation
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class TestFactorialPreComputed extends FactorialCheckerBase
{
  private static final int MAX_TIME_ALLOWED = 6;

  public TestFactorialPreComputed()
  {
  }

  @Before
  @Override
  public void setUp() throws Exception
  {
    setMh(new PreComputedMathHelper());
  }

  @Override
  protected long getMaxTimeAllowed()
  {
    return MAX_TIME_ALLOWED;
  }

  @Test
  public void testLastInTable()
  {
    final int m = PreComputedMathHelper.knownLength - 1;
    final long exp = 20922789888000L; // 16!
    assertEquals("end of table [" + m + "] unequal", exp, getMh().factorial(m));
  }

  @Test
  public void testFirstBeyondTable()
  {
    final int m = PreComputedMathHelper.knownLength;
    final long exp = 355687428096000L; // 17!
    assertEquals("end of table [" + m + "] unequal", exp, getMh().factorial(m));
  }
}
