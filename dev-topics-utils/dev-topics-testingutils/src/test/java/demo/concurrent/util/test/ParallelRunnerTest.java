/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.concurrent.util.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.concurrent.util.ParallelRunner;

public class ParallelRunnerTest
{
  private static final boolean TRACE_STATE = false;
  private ParallelRunner runner;

  private static final int NUM_THREAD_IDS = 10;
  private int[] cumulator = new int[NUM_THREAD_IDS];

  @Before
  public void setUp() throws Exception
  {
    runner = new ParallelRunner();
    runner.setTrace(TRACE_STATE);
    for (int i = 0; i < cumulator.length; i++)
      cumulator[i] = 0;
  }

  @After
  public void tearDown() throws Exception
  {
    runner = null;
  }

  @Test
  public void testInitialState()
  {
    Assert.assertTrue("invalid default trace", runner.isTrace() == TRACE_STATE);
    Assert.assertEquals("invalid execution timeout",
        ParallelRunner.DEF_MAX_INIT_TIMEOUT_MS, runner.getMaxInitTimeoutMS());
  }

  @Test
  public void testCumulate()
  {
    // runner.setTrace(true);

    final List<Runnable> runnables = new ArrayList<Runnable>();
    for (int id = 0; id < NUM_THREAD_IDS; id++)
      runnables.add(new CumuloTestData(id, cumulator, false));

    final String label = "Cumulate Test of " + NUM_THREAD_IDS + " threads";
    launchCheckRun(label, runnables);

    final int[] expectedCounts = new int[NUM_THREAD_IDS];
    for (int id = 0; id < NUM_THREAD_IDS; id++)
      expectedCounts[id] = 1;

    checkCounts(label, expectedCounts);
  }

  @Test
  public void testMultiCumulate()
  {
    // runner.setTrace(true);

    final List<Integer> ids = new ArrayList<Integer>();
    final int totalTestThreads = (NUM_THREAD_IDS * (NUM_THREAD_IDS + 1)) / 2;
    for (int id = 0; id < NUM_THREAD_IDS; id++)
      for (int i = 1; i <= (id + 1); i++)
        ids.add(id);
    Assert.assertEquals("cumulation counts differ", totalTestThreads,
        ids.size());
    Collections.shuffle(ids);

    final List<Runnable> runnables = new ArrayList<Runnable>();
    for (final Integer id : ids)
      runnables.add(new CumuloTestData(id, cumulator, false));

    final String label = "Cumulate Test of " + totalTestThreads + " threads";
    launchCheckRun(label, runnables);

    final int[] expectedCounts = new int[NUM_THREAD_IDS];
    for (int id = 0; id < NUM_THREAD_IDS; id++)
      expectedCounts[id] = (id + 1);
    checkCounts(label, expectedCounts);
  }

  // ---------------------------------------------------------------------------

  private void launchCheckRun(final String label, final List<Runnable> runnables)
  {
    final int maxTimeoutSeconds = 1;
    List<Throwable> errors = null;
    try
    {
      errors = runner.launchRunnables(label, runnables, maxTimeoutSeconds);
    }
    catch (final InterruptedException | TimeoutException ex)
    {
      Assert.fail(label + " failed with " + ex.getMessage());
    }

    Assert.assertNotNull("errors null", errors);
    if (!errors.isEmpty())
    {
      System.err.println("Run failed with " + errors.size() + " errors:");
      int i = 0;
      for (final Throwable th : errors)
        System.err.println("  " + (i++) + ". " + th.getMessage());
      Assert.assertTrue("errors not empty", errors.isEmpty());
    }
  }

  private void checkCounts(final String label, final int[] expectedCounts)
  {
    int diffCount = 0;
    for (int id = 0; id < NUM_THREAD_IDS; id++)
    {
      final int expectedCount = expectedCounts[id];
      final int actualCount = cumulator[id];
      if (actualCount != expectedCount)
      {
        System.err.println(label + "::Thread ID[" + id
            + "] count differs -- expected: " + expectedCount + ";  had: "
            + actualCount);
        diffCount++;
      }
    }

    Assert.assertEquals(label + "::Thread cumulation counts differ", 0,
        diffCount);
  }
}
