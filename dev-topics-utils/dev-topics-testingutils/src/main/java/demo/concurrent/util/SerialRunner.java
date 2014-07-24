/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.concurrent.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Runs the list of <code>Runnable</code> instances serially on the calling
 * thread.
 * 
 * @author Donald Trummell
 */
public class SerialRunner implements Launch
{
  private boolean trace = false;

  public SerialRunner()
  {
  }

  /**
   * Single threaded runner.
   * 
   * @param label
   *          identity information for run
   * @param runnables
   *          the list of <code>Runnable</code? instances to execute
   * @param maxTimeoutSeconds
   *          ignored
   */
  @Override
  public List<Throwable> launchRunnables(final String label,
      final List<? extends Runnable> runnables, final int maxTimeoutSeconds)
  {
    if (label == null)
      throw new IllegalArgumentException("message null");

    if (runnables == null)
      throw new IllegalArgumentException("runnables null");

    final int numRunnables = runnables.size();
    if (numRunnables < 1)
      throw new IllegalArgumentException("runnables empty");

    final List<Throwable> exceptions = Collections
        .synchronizedList(new ArrayList<Throwable>());

    if (trace)
      System.err.println("++++ Serial: " + "Starting . . .");

    try
    {
      if (trace)
        System.err.println("++++ Serial: " + "wrap-run");
      wrapAndRun(runnables, exceptions);
    }
    catch (final Throwable th)
    {
      throw new IllegalStateException(label + " unexpected problem: "
          + th.getMessage(), th);
    }

    if (trace)
      System.err.println("++++ Serial: " + "Done");

    return exceptions;
  }

  /**
   * Run the <code>Runnable</code> instances and capture errors.
   * 
   * @param runnables
   *          the instances to run
   * @param exceptions
   *          the errors captured
   */
  private void wrapAndRun(final List<? extends Runnable> runnables,
      final List<Throwable> exceptions)
  {
    for (final Runnable submittedRunnable : runnables)
    {
      final Runnable wrappedRunner = wrapRunnable(submittedRunnable, exceptions);
      if (trace)
        System.err
            .println("++++ Serial - Instance <"
                + Thread.currentThread().getName()
                + ">: Starting wrapped runnable");
      wrappedRunner.run();
    }
  }

  /**
   * Wrap the <code>Runnable</code> with latches for synchronization; wait until
   * all are submitted and then start all.
   * 
   * @param submittedRunnable
   *          the runnable to wrap
   * @param exceptions
   *          the error list used to capture errors
   * 
   * @return the wrapped <code>Runnable</code>
   */
  private Runnable wrapRunnable(final Runnable submittedRunnable,
      final List<Throwable> exceptions)
  {
    return new Runnable()
    {
      public void run()
      {
        try
        {
          if (trace)
            System.err.println("++++ Serial - Runnable <"
                + Thread.currentThread().getName() + ">: Running");
          submittedRunnable.run();
        }
        catch (final Throwable th)
        {
          exceptions.add(th);
        }
        finally
        {
          if (trace)
            System.err.println("++++ Serial - Runnable <"
                + Thread.currentThread().getName() + ">: done");
        }
      }
    };
  }

  @Override
  public boolean isTrace()
  {
    return trace;
  }

  @Override
  public void setTrace(final boolean trace)
  {
    this.trace = trace;
  }
}
