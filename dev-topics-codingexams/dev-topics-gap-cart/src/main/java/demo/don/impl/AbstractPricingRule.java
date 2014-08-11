package demo.don.impl;

import java.util.HashMap;
import java.util.Map;

import demo.don.api.PricingRule;

/**
 * Embodies applying Pricers in the correct order to price out a cart
 * 
 * @author Don
 */
public abstract class AbstractPricingRule implements PricingRule
{
  private boolean modifyCart = false;

  protected AbstractPricingRule()
  {
  }

  /**
   * Return price associated with the cart
   * 
   * @param cart
   *          the items scanned
   * @return the price of the items
   */
  @Override
  public final int price(final Map<Character, Integer> cart)
  {
    if (cart == null)
      throw new IllegalArgumentException("cart null");

    if (cart.isEmpty())
      return 0;

    final Map<Character, Integer> workingCart = isModifyCart() ? new HashMap<Character, Integer>(
        cart) : cart;

    return priceImpl(workingCart);
  }

  /**
   * Template method; override to provide specific behavior
   * 
   * @param cart
   * @return
   */
  protected abstract int priceImpl(final Map<Character, Integer> cart);

  /**
   * @return the modifyCart
   */
  public boolean isModifyCart()
  {
    return modifyCart;
  }

  /**
   * @param modifyCart
   *          the modifyCart to set
   */
  public void setModifyCart(final boolean modifyCart)
  {
    this.modifyCart = modifyCart;
  }
}
