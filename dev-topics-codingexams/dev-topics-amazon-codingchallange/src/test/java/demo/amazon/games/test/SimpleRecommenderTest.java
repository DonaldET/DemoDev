/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.amazon.games.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.amazon.games.CustomerID;
import demo.amazon.games.ProductID;
import demo.amazon.games.Recommend;
import demo.amazon.games.daostrategy.StubDao;
import demo.amazon.games.recommenders.SimpleRecommender;

/**
 * Verify recommender operation
 * 
 * @author Donald Trummell
 */
public class SimpleRecommenderTest
{
  private StubDao dao = null;
  private Recommend recommender = null;

  @Before
  public void setUp() throws Exception
  {
    dao = new StubDao();
    recommender = new SimpleRecommender(dao);
  }

  @After
  public void tearDown() throws Exception
  {
    recommender = null;
    dao = null;
  }

  @Test
  public void testGetRankedRecommendationsMany()
  {
    final CustomerID customerID = StubDao.testIds.get(0);
    final List<ProductID> rankedRecommendations = checkRankedRecommendation(customerID);

    final List<CustomerID> expected = new ArrayList<CustomerID>();
    expected.add(new CustomerID("G10"));
    expected.add(new CustomerID("G9"));
    expected.add(new CustomerID("G4"));
    expected.add(new CustomerID("G5"));
    expected.add(new CustomerID("G6"));
    expected.add(new CustomerID("G7"));
    expected.add(new CustomerID("G8"));
    expected.add(new CustomerID("G2"));
    expected.add(new CustomerID("G3"));

    Assert.assertEquals("recommendations differ for " + customerID, expected,
        rankedRecommendations);
  }

  @Test
  public void testGetRankedRecommendationsOne()
  {
    final CustomerID customerID = StubDao.testIds.get(1);
    final List<ProductID> rankedRecommendations = checkRankedRecommendation(customerID);

    final List<CustomerID> expected = new ArrayList<CustomerID>();
    expected.add(new CustomerID("G10"));

    Assert.assertEquals("recommendations differ for " + customerID, expected,
        rankedRecommendations);
  }

  @Test
  public void testGetRankedRecommendationsNone()
  {
    final CustomerID customerID = StubDao.testIds.get(2);
    final List<ProductID> rankedRecommendations = checkRankedRecommendation(customerID);

    final List<CustomerID> expected = new ArrayList<CustomerID>();

    Assert.assertEquals("recommendations differ for " + customerID, expected,
        rankedRecommendations);
  }

  // ---------------------------------------------------------------------------

  private List<ProductID> checkRankedRecommendation(final CustomerID customerID)
  {
    Assert.assertNotNull("customerID null", customerID);

    final List<ProductID> rankedRecommendations = recommender
        .getRankedRecommendations(customerID);

    Assert.assertNotNull("rankedRecommendations null for " + customerID,
        rankedRecommendations);

    return rankedRecommendations;
  }
}
