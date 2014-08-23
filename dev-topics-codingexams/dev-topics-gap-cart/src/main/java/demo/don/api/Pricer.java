package demo.don.api;

import java.util.Map;

/**
 * Extracted after two example Pricers are built, the <code>Offer</code> bean
 * was moved from <code>AbstractPricer</code>. <code>Offer</code> refactored
 * here because it is shared by <code>Pricer</code> implementations.
 * <p>
 * <em>Note:</em> Design change required to allow pricers to modify cart.
 *
 * @author Donald Trummell
 */
public interface Pricer
{
  /**
   * Defines the number of items offered at the recorded price to drive Pricer
   *
   * @author Don
   */
  public static final class Offer
  {
    private final int price;
    private final int itemsPerPack;

    public Offer(final int price)
    {
      this.price = price;
      this.itemsPerPack = 1;
    }

    public Offer(final int price, final int itemsPerPack)
    {
      this.price = price;
      this.itemsPerPack = itemsPerPack;
    }

    /**
     * @return the itemsPerPack
     */
    public int getItemsPerPack()
    {
      return itemsPerPack;
    }

    /**
     * @return the price
     */
    public int getPrice()
    {
      return price;
    }
  }

  /**
   * Compute the pricing for a cart
   *
   * @param cart
   *          the cart to price
   *
   * @return cart total
   */
  public abstract int tallyCart(final Map<Character, Integer> cart);
}