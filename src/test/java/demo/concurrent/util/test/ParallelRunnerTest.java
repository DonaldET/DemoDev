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
  /**
   * Test class increments the cumulation
   * 
   * @author Don
   */
  private class Cumulo implements Runnable
  {
    private int id;

    public Cumulo(final int id)
    {
      this.id = id;
    }

    @Override
    public void run()
    {
      Thread.yield();

      try
      {
        Thread.sleep(3);
      }
      catch (InterruptedException ignore)
      {
        // Ignore
      }

      ParallelRunnerTest.this.cumulator[id]++;
    }
  }

  private static final boolean TRACE_STATE = false;
  private ParallelRunner runner;

  private static final int NUM_THREAD_IDS = 10;
  private volatile int[] cumulator = new int[NUM_THREAD_IDS];

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
      runnables.add(new Cumulo(id));

    final String label = "Cumulate Test of " + NUM_THREAD_IDS + " threads";
    setupCheckRun(label, runnables);

    final int[] expectedCounts = new int[NUM_THREAD_IDS];
    for (int id = 0; id < NUM_THREAD_IDS; id++)
      expectedCounts[id] = 1;
    checkCounts(expectedCounts);
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
      runnables.add(new Cumulo(id));

    final String label = "Cumulate Test of " + totalTestThreads + " threads";
    setupCheckRun(label, runnables);

    final int[] expectedCounts = new int[NUM_THREAD_IDS];
    for (int id = 0; id < NUM_THREAD_IDS; id++)
      expectedCounts[id] = (id + 1);
    checkCounts(expectedCounts);
  }

  // ---------------------------------------------------------------------------

  private void setupCheckRun(final String label, final List<Runnable> runnables)
  {
    final int maxTimeoutSeconds = 1;
    List<Throwable> errors = null;
    try
    {
      errors = runner.runConcurrent(label, runnables, maxTimeoutSeconds);
    }
    catch (InterruptedException | TimeoutException ex)
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

  private void checkCounts(final int[] expectedCounts)
  {
    int diffCount = 0;
    for (int id = 0; id < NUM_THREAD_IDS; id++)
    {
      final int expectedCount = expectedCounts[id];
      final int actualCount = cumulator[id];
      if (actualCount != expectedCount)
      {
        System.err.println("Thread " + id + " count differs -- expected: "
            + expectedCount + ";  had: " + actualCount);
        diffCount++;
      }
    }
    Assert.assertEquals("Thread cumulation counts differ", 0, diffCount);
  }
}
