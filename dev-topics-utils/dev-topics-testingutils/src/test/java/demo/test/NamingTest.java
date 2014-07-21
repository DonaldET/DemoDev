/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.DemoDev;

public class NamingTest
{
  private DemoDev dd = null;

  @Before
  public void setUp() throws Exception
  {
    dd = new DemoDev();
  }

  @After
  public void tearDown() throws Exception
  {
    dd = null;
  }

  @Test
  public void testCreateName()
  {
    final String person = "sam addams";
    final String expected = "Sam addams";
    Assert.assertEquals("named value differs", expected, dd.nameMe(person));
  }
}
