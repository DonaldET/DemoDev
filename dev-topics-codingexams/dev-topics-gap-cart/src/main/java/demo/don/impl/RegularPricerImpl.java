package demo.don.impl;

import java.util.HashMap;
import java.util.Map;

import demo.don.api.Pricer;

/**
 * First concrete pricer from abstract base pricer
 * 
 * @author Don
 */
public class RegularPricerImpl extends AbstractPricer implements Pricer
{
  private static final Map<Character, Pricer.Offer> standard = new HashMap<Character, Pricer.Offer>();
  private static final boolean[] initialized = new boolean[1];

  static
  {
    synchronized (initialized)
    {
      if (!initialized[0])
      {
        standard.put("A".toCharArray()[0], new Pricer.Offer(133));
        standard.put("B".toCharArray()[0], new Pricer.Offer(1300));
        standard.put("C".toCharArray()[0], new Pricer.Offer(67));
        standard.put("D".toCharArray()[0], new Pricer.Offer(3));
        standard.put("E".toCharArray()[0], new Pricer.Offer(10));
        standard.put("F".toCharArray()[0], new Pricer.Offer(56));
        standard.put("G".toCharArray()[0], new Pricer.Offer(222));
        standard.put("H".toCharArray()[0], new Pricer.Offer(893));

        initialized[0] = true;
      }
    }
  }

  public RegularPricerImpl()
  {
    super();
  }

  /**
   * Original implementation refactored and moved to AbstractPricer
   */
  @Override
  protected int tallyCartImpl(final Map<Character, Integer> cart)
  {
    // Moved to AbstractPricer
    //
    // int sum = 0;
    // if (!cart.isEmpty())
    // {
    // . . .
    // return sum;

    return commonPricing(cart, standard);
  }
}
