/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.api;

/**
 * Conceptual cart with price total strategy
 * 
 * @author Donald Trummell
 */
public interface GapCart
{
  /**
   * A convenience method, the need for which was discovered during test
   * creation
   * 
   * @param skuValues
   *          an array of sku values to scan
   * @return array of counts
   */
  public abstract int[] scan(final char[] skuValues);

  /**
   * Scan an item into cart based on sku
   * <p>
   * <em>Note:</em> Discover returning count makes testing easier, and allows
   * for changing pricing algorithm based on scanned count.
   * 
   * @param sku
   *          the sku of a single item
   * @return the count of scanned items with this queue
   */
  public abstract int scan(final char sku);

  /**
   * Return the total of items in the associated cart
   * 
   * @return item total
   */
  public abstract int total();

  /**
   * @return the pricerRule
   */
  public abstract PricingRule getPricerRule();

  /**
   * @param pricerRule
   *          the pricerRule to set
   */
  public abstract void setPricerRule(PricingRule pricerRule);
}