package demo.don.api;

import java.util.Map;

/**
 * Rules use pricers to compute cart total
 * 
 * @author Don
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