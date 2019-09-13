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

import java.util.Map;

/**
 * Rules use pricers to compute cart total
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface PricingRule
{
  /**
   * Compute the pricing for a cart, enforcing error checking and copy cart for
   * modifiable Pricers
   *
   * @param cart
   *          the cart to price
   *
   * @return cart total
   */
  public abstract int price(Map<Character, Integer> cart);
}