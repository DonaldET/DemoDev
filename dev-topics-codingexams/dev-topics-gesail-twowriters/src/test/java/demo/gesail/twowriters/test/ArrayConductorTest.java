/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.gesail.twowriters.test;

import org.junit.Before;

import demo.gesail.twowriters.impl.ArrayConductor;

/**
 * A test based on using an array as a counter
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class ArrayConductorTest extends AbstractConductorChecker<long[]>
{
  @Before
  @Override
  public void setUp() throws Exception
  {
    conductor = new ArrayConductor();
  }

  @Override
  protected long getCounterValue()
  {
    return conductor.getCounter()[0];
  }
}
