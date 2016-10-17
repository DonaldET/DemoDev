/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.concurrent.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This multithreaded runner, intended for thread testing, is originally taken
 * from the <code>JUNIT.ORG</code> Wiki page about threading (see <a href=
 * "https://github.com/junit-team/junit/wiki/Multithreaded-code-and-concurrency"
 * > multithreaded runner</a>. This class runs one thread per
 * <code>Runnable</code> and starts all threads at (almost) the same time, then
 * waits for all of them to complete, and limits the total execution time.
 * <p>
 * Threads are placed in the pool until all are submitted. Once all threads are
 * submitted, initialization is complete and all threads are unblocked. Each
 * thread then runs and counts down the thread-completed latch. Finally, all the
 * threads are either counted down or time out.
 * <p>
 * <strong>Note:</strong> Threads are coordinated but not synchronized, so the
 * <code>Runnable</code> instances in the pool must perform their own
 * synchronization.
 * 
 * @author Donald Trummell
 */
public class ParallelRunner implements Launch
{
  public static final int DEF_MAX_INIT_TIMEOUT_MS = 3;

  /**
   * Initialization phase wait time (time to build the queue); set before run
   */
  private int maxInitTimeoutMS = DEF_MAX_INIT_TIMEOUT_MS;

  /**
   * If true then trace setup and thread run; set before run
   */
  private boolean trace = true;

  /**
   * Construct with defaults
   */
  public ParallelRunner()
  {
  }

  /**
   * Multi-threaded execution of <code>Runnable</code>instances.
   * 
   * @see demo.concurrent.util.Launch#launchRunnables(java.lang.String,
   *      java.util.List, int)
   */
  @Override
  public List<Throwable> launchRunnables(final String label,
      final List<? extends Runnable> runnables, final int maxTimeoutSeconds)
      throws InterruptedException, TimeoutException
  {
    if (label == null)
      throw new IllegalArgumentException("message null");

    if (runnables == null)
      throw new IllegalArgumentException("runnables null");

    final int numThreads = runnables.size();
    if (numThreads < 1)
      throw new IllegalArgumentException("runnables empty");

    final List<Throwable> exceptions = Collections
        .synchronizedList(new ArrayList<Throwable>());

    ExecutorService threadPool = null;
    boolean runFinishedInTime = false;
    boolean initFinishedInTime = false;
    String step = "?";
    try
    {
      step = "allocate";
      if (trace)
        System.err.println("++++ Parallel: " + "allocate");
      threadPool = Executors.newFixedThreadPool(numThreads);
      final CountDownLatch allReady = new CountDownLatch(numThreads);
      final CountDownLatch allDone = new CountDownLatch(numThreads);
      final CountDownLatch initDone = new CountDownLatch(1);

      step = "create-pool";
      if (trace)
        System.err.println("++++ Parallel: " + "create-pool");
      createRunningThreadPool(runnables, allReady, threadPool, initDone,
          allDone, exceptions);

      step = "wait-all-submitted";
      if (trace)
        System.err.println("++++ Parallel: " + "wait-all-submitted");
      initFinishedInTime = allReady.await(maxInitTimeoutMS,
          TimeUnit.MILLISECONDS);

      if (initFinishedInTime)
      {
        step = "start-all-execute";
        if (trace)
          System.err.println("++++ Parallel: " + "start-all-execute");
        initDone.countDown(); // Initialization complete, unblock

        step = "wait-all-execute";
        if (trace)
          System.err.println("++++ Parallel: " + "wait-all-execute");
        runFinishedInTime = allDone.await(maxTimeoutSeconds, TimeUnit.SECONDS);

        step = "done";
      }
    }
    catch (final Throwable th)
    {
      throw new IllegalStateException(label + " unexpected problem (at " + step
          + "): " + th.getMessage(), th);
    }
    finally
    {
      if (threadPool != null)
        threadPool.shutdownNow();
    }

    if (trace)
      System.err.println("++++ Parallel: " + "Done;  init finished: "
          + initFinishedInTime + ";  execute finished: " + runFinishedInTime);

    if (!initFinishedInTime)
      throw new TimeoutException(label + ": initialization timeout! More than"
          + maxTimeoutSeconds + " milliseconds elapsed");

    if (!runFinishedInTime)
      throw new TimeoutException(label + ": execution timeout! More than"
          + maxTimeoutSeconds + " seconds elapsed");

    return exceptions;
  }

  /**
   * Setup the thread pool with the <code>Runnable</code> instances ready to
   * start
   * 
   * @param runnables
   *          the threads to start
   * @param allReady
   * @param threadPool
   * @param initDone
   * @param allDone
   * @param exceptions
   */
  private void createRunningThreadPool(
      final List<? extends Runnable> runnables, final CountDownLatch allReady,
      final ExecutorService threadPool, final CountDownLatch initDone,
      final CountDownLatch allDone, final List<Throwable> exceptions)
  {
    for (final Runnable submittedRunnable : runnables)
    {
      final Runnable wrappedRunner = wrapRunnable(submittedRunnable, allReady,
          initDone, allDone, exceptions);
      if (trace)
        System.err.println("++++ Parallel - Pool <"
            + Thread.currentThread().getName() + ">: submitting runnable");
      threadPool.submit(wrappedRunner);
    }
  }

  /**
   * Wrap the <code>Runnable</code> with latches for synchronization; wait until
   * all are submitted and then start all.
   * 
   * @param submittedRunnable
   * @param allReady
   * @param initDone
   * @param allDone
   * @param exceptions
   * 
   * @return the wrapped <code>Runnable</code>
   */
  private Runnable wrapRunnable(final Runnable submittedRunnable,
      final CountDownLatch allReady, final CountDownLatch initDone,
      final CountDownLatch allDone, final List<Throwable> exceptions)
  {
    return new Runnable()
    {
      @Override
      public void run()
      {
        if (isTrace())
          System.err
              .println("++++ Parallel - Runnable <"
                  + Thread.currentThread().getName()
                  + ">: counting down all ready");
        allReady.countDown(); // Count this thread as running
        try
        {
          if (isTrace())
            System.err
                .println("++++ Parallel - Runnable <"
                    + Thread.currentThread().getName()
                    + ">: waiting init complete");
          initDone.await(); // Block run until pool is created

          if (isTrace())
            System.err.println("++++ Parallel - Runnable <"
                + Thread.currentThread().getName() + ">: submitting");
          submittedRunnable.run();
        }
        catch (final Throwable th)
        {
          exceptions.add(th);
        }
        finally
        {
          if (isTrace())
            System.err.println("++++ Parallel - Runnable <"
                + Thread.currentThread().getName()
                + ">: counting down thread as done");
          allDone.countDown(); // Count this thread as done
        }
      }
    };
  }

  /**
   * @see demo.concurrent.util.Launch#isTrace()
   */
  @Override
  public boolean isTrace()
  {
    return trace;
  }

  /**
   * @see demo.concurrent.util.Launch#setTrace(boolean)
   */
  @Override
  public void setTrace(boolean trace)
  {
    this.trace = trace;
  }

  /**
   * @return the maxInitTimeoutMS
   */
  public int getMaxInitTimeoutMS()
  {
    return maxInitTimeoutMS;
  }

  /**
   * @param maxInitTimeoutMS
   *          the maxInitTimeoutMS to set
   */
  public void setMaxInitTimeoutMS(final int maxInitTimeoutMS)
  {
    this.maxInitTimeoutMS = maxInitTimeoutMS;
  }
}
