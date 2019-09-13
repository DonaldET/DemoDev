/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.api.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.api.PricingRule;
import demo.don.api.SkuNotFound;
import demo.don.impl.RegularPricingRuleImpl;

/**
 * Unit tests for building the pricing rules and the tests are numbered to show
 * build order. This is created and tested after Pricers are completed.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class RegularPricingRuleImplTest
{
  private PricingRule rule = null;

  @Before
  public void setUp() throws Exception
  {
    rule = new RegularPricingRuleImpl();
  }

  @After
  public void tearDown() throws Exception
  {
    rule = null;
  }

  /**
   * Test cart is not modifiable
   */
  @Test
  public void test040()
  {
    Assert.assertFalse("default modify state bad",
        ((RegularPricingRuleImpl) rule).isModifyCart());
  }

  /**
   * No values
   */
  @Test
  public void test041()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    Assert.assertEquals("price differs empty cart", 0, rule.price(cart));
  }

  /**
   * Standard Rule, single value
   */
  @Test
  public void test042()
  {
    final Character tsku = "A".toCharArray()[0];
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    cart.put(tsku, 1);

    Assert.assertEquals("price differs for sku " + tsku, 133, rule.price(cart));
  }

  /**
   * Standard Rule, multiple values
   */
  @Test
  public void test043()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    cart.put("A".toCharArray()[0], 1); // 133
    cart.put("G".toCharArray()[0], 1); // 222
    cart.put("H".toCharArray()[0], 1); // 893

    final int expected = (133) + (222) + (893);
    Assert.assertEquals("price differs for A,G,H", expected, rule.price(cart));
  }

  /**
   * Standard Rule, missing sku
   */
  @Test(expected = SkuNotFound.class)
  public void test044()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    final String badSku = "Z";
    cart.put(badSku.toCharArray()[0], 1);
    rule.price(cart);
    Assert.fail("Bad sku not trapped (" + badSku + ")");
  }
}
