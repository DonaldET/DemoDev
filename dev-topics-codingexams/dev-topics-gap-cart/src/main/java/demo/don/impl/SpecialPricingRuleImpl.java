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
 * Applies Special (Promotional) multi-unit pricing
 * <p>
 * <em>Note:</em>
 * <ul>
 * <li>Developed after Pricers and before Special (promotional) Rule</li>
 * <li>Pricer design modified to allow cart modification</li>
 * </ul>
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class SpecialPricingRuleImpl extends AbstractPricingRule
{
  final Pricer standardPricer = new RegularPricerImpl();
  final Pricer specialPricer = new SpecialPricerImpl();

  public SpecialPricingRuleImpl()
  {
    super();
    setModifyCart(true);
    ((SpecialPricerImpl) specialPricer).setEmptyCart(true);
    ((SpecialPricerImpl) specialPricer).setIgnoreMissingSku(true);
  }

  /**
   * Apply special price to cart, removing purchased items, then apply standard
   * prices to remaining items. Requires cart modification (handled by parent
   * method)
   */
  @Override
  protected int priceImpl(final Map<Character, Integer> cart)
  {
    final int promotionalTotal = specialPricer.tallyCart(cart);
    final int regularTotal = standardPricer.tallyCart(cart);

    return regularTotal + promotionalTotal;
  }
}
