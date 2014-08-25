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

import java.util.Map;

import demo.don.api.Pricer;

/**
 * Applies regular/standard single-unit pricing
 * <p>
 * <em>Note</em> Developed after Pricers and before Special (promotional) rules.
 * 
 * @author Donald Trummell
 */
public class RegularPricingRuleImpl extends AbstractPricingRule
{
  final Pricer pricer = new RegularPricerImpl();

  public RegularPricingRuleImpl()
  {
  }

  @Override
  protected int priceImpl(final Map<Character, Integer> cart)
  {
    final int sum = pricer.tallyCart(cart);

    return sum;
  }
}
