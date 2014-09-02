/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.scanner.api.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import demo.scanner.ScannerTimer;

public class ScannerTimingTest
{
  private ScannerTimer stt = null;

  @Before
  public void setUp() throws Exception
  {
    stt = new ScannerTimer();
  }

  @After
  public void tearDown() throws Exception
  {
    stt = null;
  }

  @Test
  public void test10Timing()
  {
    stt.runTimming(false, 10);
  }

  @Test(expected=IllegalArgumentException.class)
  public void test101Timing()
  {
    stt.runTimming(false, 101);
  }
}
