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
import java.util.Map.Entry;
import java.util.Set;

import demo.don.api.Pricer;
import demo.don.api.SkuNotFound;

/**
 * Centralizes error checking and empty cart processing, provides bean to
 * represent package pricing
 * <p>
 * <em>Note:</em> Developed after initial cart implementation
 * <p>
 * Steps are:
 * <ol>
 * <li>Develop common error checking</li>
 * <li>Create Pricer interface and apply it</li>
 * <li>Move Offer bean to Pricer interface</li>
 * <li>Centralize pricing (commonPricing) from each Pricer implementation</li>
 * <li>Design change: add ability to remove scanned items from cart</li>
 * <li>Design change: add ability to ignore missing sku</li>
 * </ol>
 * 
 * @author Donald Trummell
 */
public abstract class AbstractPricer implements Pricer
{
  //
  // Note Offer bean refactored and moved to Pricer interface

  private boolean emptyCart = false;
  private boolean ignoreMissingSku = false;

  protected AbstractPricer()
  {
  }

  /**
   * Compute cost of cart
   */
  @Override
  public final int tallyCart(final Map<Character, Integer> cart)
  {
    if (cart == null)
      throw new IllegalArgumentException("cart null");

    if (cart.isEmpty())
      return 0;

    return tallyCartImpl(cart);
  }

  /**
   * Template method; override to provide specific behavior
   *
   * @param cart
   * @return
   */
  protected abstract int tallyCartImpl(final Map<Character, Integer> cart);

  /**
   * Apply the packet pricing model represented by the second parameter to the
   * cart, this includes optionally modifying the cart by <em>removing</em>
   * purchased items
   * 
   * @param cart
   * @param pricesBySku
   * 
   * @return the total price and optionally modify the cart
   */
  protected int commonPricing(final Map<Character, Integer> cart,
      final Map<Character, Pricer.Offer> pricesBySku)
  {
    int sum = 0;
    if (!cart.isEmpty())
    {
      final Set<Entry<Character, Integer>> entrys = cart.entrySet();
      for (final Map.Entry<Character, Integer> e : entrys)
      {
        final Character cartSku = e.getKey();
        final Pricer.Offer pricing = pricesBySku.get(cartSku);
        if (pricing == null)
        {
          if (ignoreMissingSku)
            continue;

          throw new SkuNotFound(String.valueOf(cartSku));
        }
        int values = e.getValue();
        if (values > 0)
        {
          final int itemsPerPack = pricing.getItemsPerPack();
          final int packets = values / itemsPerPack;
          if (packets > 0)
          {
            final int packetPrice = pricing.getPrice();
            sum += (packets * packetPrice);
            if (emptyCart)
            {
              values -= packets * itemsPerPack;
              cart.put(cartSku, values);
            }
          }
        }
      }
    }

    return sum;
  }

  /**
   * @return the emptyCart
   */
  public boolean isEmptyCart()
  {
    return emptyCart;
  }

  /**
   * @param emptyCart
   *          the emptyCart to set
   */
  public void setEmptyCart(final boolean emptyCart)
  {
    this.emptyCart = emptyCart;
  }

  /**
   * @return the ignoreMissingSku
   */
  public boolean isIgnoreMissingSku()
  {
    return ignoreMissingSku;
  }

  /**
   * @param ignoreMissingSku
   *          the ignoreMissingSku to set
   */
  public void setIgnoreMissingSku(final boolean ignoreMissingSku)
  {
    this.ignoreMissingSku = ignoreMissingSku;
  }
}
