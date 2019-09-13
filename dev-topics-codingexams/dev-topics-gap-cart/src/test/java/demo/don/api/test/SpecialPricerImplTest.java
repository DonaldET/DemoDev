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

import demo.don.api.Pricer;
import demo.don.api.SkuNotFound;
import demo.don.impl.SpecialPricerImpl;

/**
 * Unit tests for building the pricing algorithm and the tests are numbered to
 * show build order.
 * <p>
 * Refactoring:
 * <ol>
 * <li>convert to interface</li>
 * <li>delegate to common pricing method</li>
 * </ol>
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class SpecialPricerImplTest
{
  private Pricer pricer = null;

  @Before
  public void setUp() throws Exception
  {
    pricer = new SpecialPricerImpl();
  }

  @After
  public void tearDown() throws Exception
  {
    pricer = null;
  }

  /**
   * Standard Pricer, no values
   */
  @Test
  public void test030()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    Assert.assertEquals("price differs empty cart", 0, pricer.tallyCart(cart));
  }

  /**
   * Standard Pricer, test default settings
   */
  @Test
  public void test030A()
  {
    Assert.assertFalse("default emptyCart incorrect",
        ((SpecialPricerImpl) pricer).isEmptyCart());
    Assert.assertFalse("default ignoreMissingSku incorrect",
        ((SpecialPricerImpl) pricer).isIgnoreMissingSku());
  }

  /**
   * Standard Pricer, single value
   */
  @Test
  public void test031()
  {
    final Character tsku = "A".toCharArray()[0];
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    cart.put(tsku, 3);

    Assert.assertEquals("price differs for sku " + tsku, 255,
        pricer.tallyCart(cart));
  }

  /**
   * Standard Pricer, multiple values
   */
  @Test
  public void test032()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    cart.put("A".toCharArray()[0], 3); // 133 ---- 255, 3
    cart.put("G".toCharArray()[0], 7); // 222 ---- 711, 7
    cart.put("H".toCharArray()[0], 4); // 893 ---- 1200, 4

    final int expected = (255) + (711) + (1200);
    Assert.assertEquals("price differs for A,G,H", expected,
        pricer.tallyCart(cart));
  }

  /**
   * Standard Pricer, missing sku
   */
  @Test(expected = SkuNotFound.class)
  public void test033()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    final String badSku = "Z";
    cart.put(badSku.toCharArray()[0], 1);
    pricer.tallyCart(cart);
    Assert.fail("Bad sku not trapped (" + badSku + ")");
  }
}
