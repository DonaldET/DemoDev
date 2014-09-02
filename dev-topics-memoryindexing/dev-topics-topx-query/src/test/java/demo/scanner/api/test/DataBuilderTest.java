package demo.scanner.api.test;

/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.scanner.api.Game;
import demo.scanner.api.GameDataBuilder;
import demo.scanner.api.GameDataBuilder.ListOrder;

public class DataBuilderTest
{
  public static final String BEAN_TEST_CONTEXT = "builderTestContext.xml";

  private static final boolean[] loaded = new boolean[] { false };
  private static ApplicationContext dbContext;

  static
  {
    synchronized (loaded)
    {
      if (!loaded[0])
      {
        dbContext = new ClassPathXmlApplicationContext(BEAN_TEST_CONTEXT);
        loaded[0] = true;
      }
    }
  }

  private GameDataBuilder dataBuilder;
  private Long baseDate;

  @Before
  public void setUp() throws Exception
  {
    dataBuilder = (GameDataBuilder) dbContext.getBean("test.game.data.builder",
        GameDataBuilder.class);
    baseDate = (Long) dbContext.getBean("test.base.time", java.lang.Long.class);
  }

  @After
  public void tearDown() throws Exception
  {
    dataBuilder = null;
    baseDate = null;
  }

  @Test
  public void testLifeCycle()
  {
    Assert.assertNotNull("dataBuilder not injected", dataBuilder);
    Assert.assertFalse("dataBuilder pre-initialized",
        dataBuilder.isInitialized());

    final int nCases = 3;
    dataBuilder.initialize(nCases, baseDate);
    Assert.assertTrue("dataBuilder uninitialized", dataBuilder.isInitialized());

    final List<Game> unsortedData = dataBuilder.getData(ListOrder.unordered);
    Assert.assertNotNull("unsorted data null", unsortedData);
    Assert.assertEquals("unexpected size of generated unsorted data", nCases,
        unsortedData.size());

    dataBuilder.destroy();
    Assert.assertFalse("destroyed dataBuilder still initialized",
        dataBuilder.isInitialized());
  }

  @Test(expected = IllegalStateException.class)
  public void testInitializedStateViolation()
  {
    Assert.assertFalse("dataBuilder pre-initialized",
        dataBuilder.isInitialized());
    dataBuilder.getData(ListOrder.unordered);
  }

  @Test
  public void testGetDataSorted()
  {
    final int nCases = 3;
    dataBuilder.initialize(nCases, baseDate);
    Assert.assertTrue("dataBuilder uninitialized", dataBuilder.isInitialized());
    Assert.assertTrue("dataBuilder uninitialized", dataBuilder.isInitialized());

    final List<Game> ascendingData = dataBuilder.getData(ListOrder.ascending);
    Assert.assertNotNull("ascending data null", ascendingData);
    Assert.assertEquals("unexpected size of generated ascending data", nCases,
        ascendingData.size());

    final List<Game> descendingData = dataBuilder.getData(ListOrder.descending);
    Assert.assertNotNull("descending data null", descendingData);
    Assert.assertEquals("unexpected size of generated descending data", nCases,
        descendingData.size());

    dataBuilder.destroy();

    if (ascendingData.equals(descendingData))
    {
      System.out.println("\nAscending:\n" + ascendingData);
      System.out.println("Descending:\n" + descendingData);
      Assert.assertFalse("sort did not work",
          ascendingData.equals(descendingData));
    }
  }
}
