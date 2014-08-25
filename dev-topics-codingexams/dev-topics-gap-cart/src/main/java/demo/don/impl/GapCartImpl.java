/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.impl;

import java.util.HashMap;
import java.util.Map;

import demo.don.api.GapCart;
import demo.don.api.PricingRule;

/**
 * This is the implementation for a shopping cart described below.
 * <p>
 * <em>Assumptions:</em>
 * <ul>
 * <li>SKU is Character, the basic unit price (standard price) for single item
 * must exist</li>
 * <li>Prices are positive integers, and are associated with packets of specific
 * size in an Offer</li>
 * <li>Packets of purchased items all have the same SKU</li>
 * <li>Special (promotional) pricing rules apply packets with size &gt; 1</li>
 * <li><em>Scanning</em> will present zero or more SKU items in arbitrary order</li>
 * </ul>
 * <p>
 * <em>Design Approach:</em>
 * <p>
 * Store packets of scanned items and apply pricing rules on a &quot;
 * <em>total</em>&quot; invocation. Build implementations as concrete classes
 * and refactor to extract interfaces. Pricing rules is developed separately,
 * then added to cart implementation.
 * <p>
 * <em>Build/Test implementation order:</em>
 * <ol>
 * <li>Initial Scan implementation to collect item SKUs (
 * <code>GapCartImpl</code>)</li>
 * <li>Develop &quot;<em>pricers</em>&quot;; pricing implementations that have
 * SKU maps with packet pricing data and associated pricing calculation</li>
 * <li>Centralize pricing algorithm and interface for <em>pricers</em></li>
 * <li>Develop pricing <em>Rules</em>, using <em>pricer</em> implementations as
 * required, to price out a cart</li>
 * <li>Extract <em>rule</em> interface and apply</li>
 * <li>Use Rules in <code>GapCartImpl</code></li>
 * <li>Extract <code>GapCart</code> interface and apply</li>
 * </ol>
 * <p>
 * <em>Solution Summary:</em>
 * <p>
 * Packets of items are initially priced by promotions (special prices) if
 * applicable, then by regular (standard) prices. A cart collects scanned
 * packets that are priced by rules orchestrating pricers holding collections of
 * offers. The <em>price</em> operation may be invoked on the cart at any time.
 * 
 * @author Donald Trummell
 */
public class GapCartImpl implements GapCart
{
  private final Map<Character, Integer> cart = new HashMap<Character, Integer>();

  private PricingRule pricingRule;

  public GapCartImpl()
  {
  }

  /*
   * (non-Javadoc)
   * @see demo.don.impl.GapCart#scan(char[])
   */
  @Override
  public int[] scan(final char[] skuValues)
  {
    if (skuValues == null)
      throw new IllegalArgumentException("skuValues null");

    final int itemsToScan = skuValues.length;
    final int[] counts = new int[itemsToScan];
    if (itemsToScan > 0)
      for (int i = 0; i < itemsToScan; i++)
        counts[i] = scan(skuValues[i]);

    return counts;
  }

  /*
   * (non-Javadoc)
   * @see demo.don.impl.GapCart#scan(char)
   */
  @Override
  public int scan(final char sku)
  {
    final int count = (cart.containsKey(sku) ? cart.get(sku) : 0) + 1;
    cart.put(sku, count);

    return count;
  }

  /**
   * @return the cart
   */
  public Map<Character, Integer> getCart()
  {
    return new HashMap<Character, Integer>(cart);
  }

  /**
   * Returns the total price of items in the internal cart at any point in a
   * scan sequence
   * 
   * @return the price of the items
   */
  @Override
  public int total()
  {
    if (pricingRule == null)
      throw new IllegalStateException("no pricingRule defined");

    return pricingRule.price(cart);
  }

  /**
   * Return the pricer
   * 
   * @return returns the associated pricer or null
   */
  @Override
  public PricingRule getPricerRule()
  {
    return pricingRule;
  }

  /**
   * Assign a pricer
   * 
   * @param pricerRule
   *          the pricing rule associated with this cart
   */
  @Override
  public void setPricerRule(final PricingRule pricerRule)
  {
    this.pricingRule = pricerRule;
  }
}
