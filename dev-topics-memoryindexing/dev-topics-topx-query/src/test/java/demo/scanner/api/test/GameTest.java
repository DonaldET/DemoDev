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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.scanner.api.Game;

public class GameTest
{
  public static final String BEAN_TEST_CONTEXT = "beanTestContext.xml";

  private static final boolean[] loaded = new boolean[] { false };
  private static ApplicationContext gameContext;

  static
  {
    synchronized (loaded)
    {
      if (!loaded[0])
      {
        gameContext = new ClassPathXmlApplicationContext(BEAN_TEST_CONTEXT);
        loaded[0] = true;
      }
    }
  }

  private Game early;
  private Game later;

  @Before
  public void setUp() throws Exception
  {
    early = (Game) gameContext.getBean("test.early.game", Game.class);
    later = (Game) gameContext.getBean("test.later.game", Game.class);
  }

  @After
  public void tearDown() throws Exception
  {
    early = null;
    later = null;
  }

  @Test
  public void testToString()
  {
    Assert.assertNotNull("early not defined", early);

    String actual = early.toString();
    Assert.assertNotNull("toString null", actual);
    Assert.assertFalse("toString empty", actual.isEmpty());

    final int p = actual.indexOf("id: ");
    Assert.assertFalse("start data missing from toString", p < 0);
    final int pend = actual.indexOf(";  playedOn: ");
    Assert.assertFalse("end data missing from toString", pend < 0);
    Assert.assertTrue("data scrambled from toString", p < pend);
    actual = actual.substring(p, pend);

    final String expected = "id: ID001;  score: 257000;  playerName: Don T.";
    Assert.assertEquals("toString differs", expected, actual);
  }

  @Test
  public void testCompareToSelf()
  {
    Assert.assertNotNull("early not defined", early);
    Assert.assertTrue("self compare failed", early.compareTo(early) == 0);
    Assert.assertTrue("self equals failed", early.equals(early));
  }

  @Test
  public void testCompareToLess()
  {
    Assert.assertNotNull("early not defined", early);
    Assert.assertNotNull("later not defined", later);
    Assert.assertTrue("less compare failed", early.compareTo(later) < 0);
  }

  @Test
  public void testCompareToMore()
  {
    Assert.assertTrue("more compare failed", later.compareTo(early) > 0);
  }

  @Test
  public void testCloneAndCompareTo()
  {
    final Game copy = (Game) early.clone();
    Assert.assertTrue("clone not unique", copy != early);
    Assert.assertTrue("clone more compare failed", copy.compareTo(early) == 0);
    Assert.assertTrue("clone equal failed", copy.equals(early));
  }
}
