package demo.don.impl;

import java.util.Map;

import demo.don.api.Pricer;

/**
 * Applies regular/standard single-unit pricing
 * <p>
 * <em>Note</em> Developed after Pricers and before Special (promotional) rules.
 * 
 * @author Don
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
