/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.don.apple.factorial.test;

import org.junit.Before;

import demo.don.apple.factorial.impl.SimpleMathHelper;

/**
 * Test the simple implementation
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class TestFactorialSimple extends FactorialCheckerBase
{
  private static final int MAX_TIME_ALLOWED = 6;

  public TestFactorialSimple()
  {
  }

  @Before
  @Override
  public void setUp() throws Exception
  {
    setMh(new SimpleMathHelper());
  }

  @Override
  protected long getMaxTimeAllowed()
  {
    return MAX_TIME_ALLOWED;
  }
}
