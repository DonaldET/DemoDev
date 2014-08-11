package demo.don.api.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.api.Pricer;
import demo.don.api.SkuNotFound;
import demo.don.impl.RegularPricerImpl;

/**
 * Unit tests for building the pricing algorithm and the tests are numbered to
 * show build order
 * 
 * @author Don
 */
public class RegularPricerImplTest
{
  private Pricer pricer = null;

  @Before
  public void setUp() throws Exception
  {
    pricer = new RegularPricerImpl();
  }

  @After
  public void tearDown() throws Exception
  {
    pricer = null;
  }

  /**
   * Standard Pricer, no values
   */
  @Test
  public void test020()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    Assert.assertEquals("price differs empty cart", 0, pricer.tallyCart(cart));
  }

  /**
   * Standard Pricer, single value
   */
  @Test
  public void test021()
  {
    final Character tsku = "A".toCharArray()[0];
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    cart.put(tsku, 1);

    Assert.assertEquals("price differs for sku " + tsku, 133,
        pricer.tallyCart(cart));
  }

  /**
   * Standard Pricer, multiple values
   */
  @Test
  public void test022()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    cart.put("A".toCharArray()[0], 1);
    cart.put("B".toCharArray()[0], 1);
    cart.put("C".toCharArray()[0], 1);

    final int expected = (133) + (1300) + (67);
    Assert
        .assertEquals("price differs for A,B,C", expected, pricer.tallyCart(cart));
  }

  /**
   * Standard Pricer, missing sku
   */
  @Test(expected = SkuNotFound.class)
  public void test023()
  {
    final Map<Character, Integer> cart = new HashMap<Character, Integer>();
    final String badSku = "Z";
    cart.put(badSku.toCharArray()[0], 1);
    pricer.tallyCart(cart);
    Assert.fail("Bad sku not trapped (" + badSku + ")");
  }
}
