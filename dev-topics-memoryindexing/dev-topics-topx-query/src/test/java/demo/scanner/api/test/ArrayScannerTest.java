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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.scanner.api.Game;
import demo.scanner.api.TopXScanner;
import demo.scanner.impl.SortedArrayScanner;

public class ArrayScannerTest
{
  public static final String BEAN_TEST_CONTEXT = "arrayTestContext.xml";

  private static final String SCANNER_BEAN_NAME = "test.array.scanner";

  private static final boolean[] loaded = new boolean[] { false };
  private static ApplicationContext arrayContext;

  static
  {
    synchronized (loaded)
    {
      if (!loaded[0])
      {
        arrayContext = new ClassPathXmlApplicationContext(BEAN_TEST_CONTEXT);
        loaded[0] = true;
      }
    }
  }

  private final boolean listNatural = false;
  private final boolean listSorted = false;

  private Long baseDate;
  private Game game1;
  private List<Game> gameList;
  private TopXScanner scanner;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() throws Exception
  {
    baseDate = (Long) arrayContext.getBean("test.base.time",
        java.lang.Long.class);
    game1 = (Game) arrayContext.getBean("test.game.001", Game.class);
    gameList = (List<Game>) arrayContext.getBean("test.game.list", List.class);
    scanner = (SortedArrayScanner) arrayContext.getBean(SCANNER_BEAN_NAME,
        SortedArrayScanner.class);
  }

  @After
  public void tearDown() throws Exception
  {
    baseDate = null;
    game1 = null;
    gameList = null;
    scanner = null;
  }

  @Test
  public void testDataSetup()
  {
    Assert.assertNotNull("baseDate not injected", baseDate);
    Assert.assertNotNull("game1 not injected", game1);
    Assert.assertNotNull("gameList not injected", gameList);
    Assert.assertFalse("gameList empty", gameList.isEmpty());
    Assert.assertNotNull("scanner not injected", scanner);

    if (listNatural)
    {
      System.out.println("\nGames have " + gameList.size() + " entries");
      int i = 0;
      for (final Game g : gameList)
        System.out.println((++i) + ". " + g);
    }

    if (listSorted)
    {
      final List<Game> ordered = new ArrayList<Game>();
      ordered.addAll(gameList);
      Collections.sort(ordered);

      System.out.println("\nSorted Games have " + gameList.size() + " entries");
      int i = 0;
      for (final Game g : ordered)
        System.out.println((++i) + ". " + g);
    }
  }

  @Test
  public void testScannerNone()
  {
    final List<Game> empty = new ArrayList<Game>();
    final List<Game> topList = scanner.topX(3, empty.iterator());
    Assert.assertNotNull("topList null", topList);
    Assert.assertTrue("topList not empty", topList.isEmpty());
  }

  @Test
  public void testScannerOneExact()
  {
    final List<Game> shortList = new ArrayList<Game>();
    shortList.add(game1);

    final int x = 1;
    final List<Game> topList = scanner.topX(x, shortList.iterator());
    Assert.assertNotNull("topList null", topList);
    Assert.assertFalse("topList empty", topList.isEmpty());
    Assert.assertEquals("topList count bad", 1, topList.size());

    final List<Game> expected = new ArrayList<Game>();
    expected.add(game1);
    Assert.assertEquals("topList content bad", expected, topList);
  }

  @Test
  public void testScannerTwoShort()
  {
    final List<Game> shortList = new ArrayList<Game>();
    shortList.add(game1);

    final int x = 2;
    final List<Game> topList = scanner.topX(x, shortList.iterator());
    Assert.assertNotNull("topList null", topList);
    Assert.assertFalse("topList empty", topList.isEmpty());
    Assert.assertEquals("topList count bad", 1, topList.size());

    final List<Game> expected = new ArrayList<Game>();
    expected.add(game1);
    Assert.assertEquals("topList content bad", expected, topList);
  }

  @Test
  public void testScannerTwoExact()
  {
    final List<Game> shortList = new ArrayList<Game>();
    shortList.add(game1);
    shortList.add(gameList.get(0));

    final int x = 2;
    final List<Game> topList = scanner.topX(x, shortList.iterator());
    Assert.assertNotNull("topList null", topList);
    Assert.assertFalse("topList empty", topList.isEmpty());
    Assert.assertEquals("topList count bad", x, topList.size());

    final List<Game> expected = new ArrayList<Game>();
    expected.add(gameList.get(0));
    expected.add(game1);
    Assert.assertEquals("topList content bad", expected, topList);
  }

  @Test
  public void testScannerOneOverAll()
  {
    final int x = 1;
    final List<Game> topList = scanner.topX(x, gameList.iterator());
    Assert.assertNotNull("topList null", topList);
    Assert.assertFalse("topList empty", topList.isEmpty());
    Assert.assertEquals("topList count bad", x, topList.size());

    final List<Game> expected = new ArrayList<Game>();
    expected.add(gameList.get(15));
    Assert.assertEquals("topList content bad", expected, topList);
  }

  @Test
  public void testScannerTwoOverAll()
  {
    final int x = 2;
    final List<Game> topList = scanner.topX(x, gameList.iterator());
    Assert.assertNotNull("topList null", topList);
    Assert.assertFalse("topList empty", topList.isEmpty());
    Assert.assertEquals("topList count bad", x, topList.size());

    final List<Game> expected = new ArrayList<Game>();
    expected.add(gameList.get(15));
    expected.add(gameList.get(0));
    Assert.assertEquals("topList content bad", expected, topList);
  }

  @Test
  public void testScannerTenOverAll()
  {
    final int x = 10;
    final List<Game> topList = scanner.topX(x, gameList.iterator());
    Assert.assertNotNull("topList null", topList);
    Assert.assertFalse("topList empty", topList.isEmpty());
    Assert.assertEquals("topList count bad", x, topList.size());

    final List<Game> expected = new ArrayList<Game>();
    expected.add(gameList.get(15));
    expected.add(gameList.get(0));
    expected.add(gameList.get(8));
    expected.add(gameList.get(5));
    expected.add(gameList.get(2));
    expected.add(gameList.get(9));
    expected.add(gameList.get(6));
    expected.add(gameList.get(1));
    expected.add(gameList.get(13));
    expected.add(gameList.get(11));
    Assert.assertEquals("topList content bad", expected, topList);
  }
}
