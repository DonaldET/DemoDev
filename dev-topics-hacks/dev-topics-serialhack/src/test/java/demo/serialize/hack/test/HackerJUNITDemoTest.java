/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.serialize.hack.test;

import java.awt.Point;
import java.lang.Thread.State;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HackerJUNITDemoTest
{
  private static final boolean TRACE_STATE = false;

  private static boolean[] initialized = new boolean[1];

  private Point p;

  static
  {
    static_setup();
  }

  private static void static_setup()
  {
    if (TRACE_STATE)
      System.err.print("\n====Setup");

    final Thread curTh = Thread.currentThread();
    if (TRACE_STATE)
      System.err.println("[thread]: " + curTh);

    synchronized (initialized)
    {
      if (initialized[0])
        if (TRACE_STATE)
          System.err.println("    . . . initialized");
        else
        {
          if (TRACE_STATE)
            System.err.println("    initializing!");
          initialized[0] = true;
        }
    }
  }

  @Before
  public void setUp() throws Exception
  {
    p = new Point(1, 2);
  }

  @After
  public void tearDown() throws Exception
  {
    p = null;
  }

  @Test
  public void test1()
  {
    checkThread(1);
  }

  @Test
  public void test2()
  {
    checkThread(2);
  }

  @Test
  public void test3()
  {
    checkThread(3);
  }

  @Test
  public void test4()
  {
    checkThread(4);
  }

  @Test
  public void test5()
  {
    checkThread(5);
  }

  @Test
  public void test6()
  {
    checkThread(6);
  }

  @Test
  public void test7()
  {
    checkThread(7);
  }

  @Test
  public void test8()
  {
    checkThread(8);
  }

  @Test
  public void test9()
  {
    checkThread(9);
  }

  // ---------------------------------------------------------------------------

  private void checkThread(final int id)
  {
    final Thread curTh = Thread.currentThread();
    if (TRACE_STATE)
      System.err.println("Test[" + id + "]: " + curTh);
    final State state = curTh.getState();
    Assert.assertNotNull("Bad thread state", state);
    Assert.assertEquals("Bad state", state.name(), State.RUNNABLE.name());
    Assert.assertEquals("Bad test x state", 1, p.x);
    Assert.assertEquals("Bad test y state", 2, p.y);
  }
}
