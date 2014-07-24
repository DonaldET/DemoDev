/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.concurrent.util.test;

/**
 * Increments an element of a shared array for testing. Random wait times are
 * used to simulate differing processing loads. Note that the parallel runner
 * coordinates starting and stopping threads, but does not synchronize the
 * incrementing.
 * 
 * @author Don
 */
public class CumuloTestData implements Runnable
{
  private static final double RANDOM_WAIT_RANGE = 6.0;
  private final int id;
  private final int[] cumulator;
  private final boolean sync;

  /**
   * Set up an incremented slot in array, defined by thread id.
   * 
   * @param id
   *          the slot to increment that is tied to a thread
   * @param cumulator
   *          the counter for incrementing each thread
   * @param sync
   *          if true, then synchronize on the cumulator
   */
  public CumuloTestData(final int id, final int[] cumulator, final boolean sync)
  {
    this.id = id;
    this.cumulator = cumulator;
    this.sync = sync;
  }

  @Override
  public void run()
  {
    try
    {
      Thread.sleep(Math.round(Math.random() * RANDOM_WAIT_RANGE));
    }
    catch (final InterruptedException ignore)
    {
      System.err.println("\n  **** Sleep interupted for "
          + Thread.currentThread().getName());
    }

    if (sync)
      synchronized (cumulator)
      {
        cumulator[id]++;
      }
    else
      cumulator[id]++;
  }
}