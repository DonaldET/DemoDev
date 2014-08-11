package demo.don.api.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.api.PricingRule;
import demo.don.api.SkuNotFound;
import demo.don.impl.SpecialPricingRuleImpl;

/**
 * Unit tests for building the pricing rules and the tests are numbered to show
 * build order. This is created and tested after Pricers are completed.
 * 
 * @author Don
 */
public class SpecialPricingRuleImplTest
{
  private PricingRule rule = null;

  @Before
  public void setUp() throws Exception
  {
    rule = new SpecialPricingRuleImpl();
  }

  @After
  public void tearDown() throws Exception
  {
    rule = null;
  }

  /**
   * Test cart is not modifiable
   */
  @Test
  public void test050()
  {
    Assert.assertTrue("default modify state bad",
        ((SpecialPricingRuleImpl) rule).isModifyCart());
  }

  /**
   * No values
   */
  @Test
  public void test051()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    Assert.assertEquals("price differs empty cart", 0, rule.price(cart));
  }

  /**
   * Special Rule, single value
   */
  @Test
  public void test052()
  {
    final Character tsku = "A".toCharArray()[0];
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    cart.put(tsku, 1);

    Assert.assertEquals("price differs for sku " + tsku, 133, rule.price(cart));
  }

  /**
   * Standard Rule, multiple values
   */
  @Test
  public void test053()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    cart.put("A".toCharArray()[0], 1); // 133
    cart.put("G".toCharArray()[0], 1); // 222
    cart.put("H".toCharArray()[0], 1); // 893

    final int expected = (133) + (222) + (893);
    Assert.assertEquals("price differs for A,G,H", expected, rule.price(cart));
  }

  /**
   * Special Rule, missing sku
   */
  @Test(expected = SkuNotFound.class)
  public void test054()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    final String badSku = "Z";
    cart.put(badSku.toCharArray()[0], 1);
    rule.price(cart);
    Assert.fail("Bad sku not trapped (" + badSku + ")");
  }

  //
  // Add additional test for standard overridden by promotions pricing

  /**
   * Standard Rule + Promotional, multiple values with more than one packet
   */
  @Test
  public void test055()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    cart.put("A".toCharArray()[0], 1 + 2 * 3); // 133 ---- 255, 3
    cart.put("G".toCharArray()[0], 1 + 1 * 7); // 222 ---- 711, 7
    cart.put("H".toCharArray()[0], 1 + 3 * 4); // 893 ---- 1200, 4

    final int expected = (1 * 133 + 2 * 255) + (1 * 222 + 1 * 711)
        + (1 * 893 + 3 * 1200);
    Assert
        .assertEquals("price differs for A,B,G,H", expected, rule.price(cart));
  }

  /**
   * Standard Rule + Promotional, multiple values with more than one packet plus
   * non-promotional item
   */
  @Test
  public void test056()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    cart.put("A".toCharArray()[0], 1 + 2 * 3); // 133 ---- 255, 3
    cart.put("B".toCharArray()[0], 2 * 1 + 0 * 0); // 1300
    cart.put("G".toCharArray()[0], 1 + 1 * 7); // 222 ---- 711, 7
    cart.put("H".toCharArray()[0], 1 + 3 * 4); // 893 ---- 1200, 4

    final int expected = (1 * 133 + 2 * 255) + (2 * 1300 + 0 * 0)
        + (1 * 222 + 1 * 711) + (1 * 893 + 3 * 1200);
    Assert
        .assertEquals("price differs for A,B,G,H", expected, rule.price(cart));
  }
}
