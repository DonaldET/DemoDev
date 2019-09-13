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

import demo.don.api.Pricer;

/**
 * Second concrete pricer built from abstract base pricer
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class SpecialPricerImpl extends AbstractPricer implements Pricer
{
  private static final Map<Character, Pricer.Offer> special = new HashMap<Character, Pricer.Offer>();
  private static final boolean[] specialInitialized = new boolean[1];

  static
  {
    synchronized (specialInitialized)
    {
      if (!specialInitialized[0])
      {
        special.put("A".toCharArray()[0], new Pricer.Offer(255, 3));
        special.put("E".toCharArray()[0], new Pricer.Offer(7, 2));
        special.put("G".toCharArray()[0], new Pricer.Offer(711, 7));
        special.put("H".toCharArray()[0], new Pricer.Offer(1200, 4));

        specialInitialized[0] = true;
      }
    }
  }

  public SpecialPricerImpl()
  {
    super();
  }

  @Override
  protected int tallyCartImpl(final Map<Character, Integer> cart)
  {
    // Moved to AbstractPricer
    //
    // int sum = 0;
    // if (!cart.isEmpty())
    // {
    // final Set<Entry<Character, Integer>> entrys = cart.entrySet();
    // . . .
    // return sum;

    return commonPricing(cart, special);
  }
}
