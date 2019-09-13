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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.gesail.twowriters.Conductor;

/**
 * Shared testing code to used to check counter values obtained by a
 * <code>Conductor</code>
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 * @param <C>
 *          type of the counter
 */
public abstract class AbstractConductorChecker<C>
{
  protected Conductor<C> conductor;

  protected AbstractConductorChecker()
  {
    super();
  }

  @Before
  public abstract void setUp() throws Exception;

  @After
  public void tearDown() throws Exception
  {
    conductor = null;
  }

  protected abstract long getCounterValue();

  @Test
  public void testRunme()
  {
    conductor.runme(false);
    checkAddersDone();
    checkCounts();
    checkReached();
  }

  private void checkAddersDone()
  {
    Assert.assertTrue("t1 not done", conductor.getT1Addr().isDone());
    Assert.assertTrue("t2 not done", conductor.getT2Addr().isDone());
  }

  private void checkCounts()
  {
    Assert.assertEquals("counter wrong", Conductor.LIMIT, getCounterValue());

    final int[] allIcounted = conductor.getAllIcounted();
    Assert.assertTrue("t1 count bad, " + allIcounted[0], allIcounted[0] > 0);
    Assert.assertTrue("t2 count bad, " + allIcounted[1], allIcounted[1] > 0);
    Assert.assertEquals("total times counted failed", Conductor.LIMIT,
        allIcounted[0] + allIcounted[1]);
  }

  private void checkReached()
  {
    final int[] r1counts = conductor.getT1Addr().getReached();
    final int[] r2counts = conductor.getT2Addr().getReached();
    int rSum = 0;
    for (int i = 0; i < r1counts.length; i++)
    {
      final int r1 = r1counts[i];
      Assert.assertTrue("r1[" + i + "] bad, " + r1 + ",", r1 == 0 || r1 == 1);
      rSum += r1;

      final int r2 = r2counts[i];
      Assert.assertTrue("r2[" + i + "] bad, " + r2 + ",", r2 == 0 || r2 == 1);
      rSum += r2;

      Assert.assertEquals("r[" + i + "] bad", i < 1 ? 0 : 1, r1 + r2);
    }

    Assert.assertEquals("counts reached bad", Conductor.LIMIT, rSum);
  }
}