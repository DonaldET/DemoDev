/*
 * Copyright (c) 2021. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.api.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.api.GapCart;
import demo.don.impl.GapCartImpl;
import demo.don.impl.RegularPricingRuleImpl;
import demo.don.impl.SpecialPricingRuleImpl;

/**
 * Unit tests for building the cart and the tests are numbered to show build
 * order
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class GapCartImplTest {
	// Begin with concrete class and refactor to interface
	private GapCart gapCart = null;

	@Before
	public void setUp() throws Exception {
		gapCart = new GapCartImpl();
	}

	@After
	public void tearDown() throws Exception {
		gapCart = null;
	}

	/**
	 * Check collecting SKUs; cart initially empty
	 */
	@Test
	public void test010() {
		final Map<Character, Integer> internalCart = ((GapCartImpl) gapCart).getCart();
		Assert.assertNotNull("null stored SKU", internalCart);
		Assert.assertTrue("empty stored SKU", internalCart.isEmpty());
	}

	/**
	 * Check collecting SKU; check Scan single item call
	 * <ul>
	 * <li>Discover API change to return count making more testable</li>
	 * <li>Discover that API change to allow char array parameter is useful</li>
	 * </ul>
	 */
	@Test
	public void test011() {
		final String singleItemSku = "A";
		// cart.scan(FIRST_ITTEM_SKU.charAt(0)); refactored away
		final int[] cnts = gapCart.scan(singleItemSku.toCharArray());
		Assert.assertNotNull("returned count null", cnts);
		Assert.assertEquals("counts size bad", 1, cnts.length);
		Assert.assertEquals("count differs", 1, cnts[0]);

		final Map<Character, Integer> internalCart = ((GapCartImpl) gapCart).getCart();
		Assert.assertNotNull("returned internalCart null", internalCart);
		Assert.assertEquals("internalCart size bad", 1, internalCart.size());

		final char singleItemSkuKey = singleItemSku.toCharArray()[0];
		Assert.assertEquals("internalCart stored count differs", 1, internalCart.get(singleItemSkuKey).intValue());
	}

	/**
	 * Check collecting SKU; check Scan call of multiple items
	 */
	@Test
	public void test012() {
		final String mixedItemSku = "ABCBDAEFFGDCBA";
		checkSkuCounts(mixedItemSku);
	}

	//
	// These test added after Pricing Rules developed

	/**
	 * Standard and Special Rule, test empty
	 */
	@Test
	public void test060() {
		GapCart gapCart = new GapCartImpl();

		gapCart.setPricerRule(new RegularPricingRuleImpl());
		Assert.assertEquals("regular price differs for empty", 0, gapCart.total());

		gapCart.setPricerRule(new SpecialPricingRuleImpl());
		Assert.assertEquals("special price differs for empty", 0, gapCart.total());
	}

	/**
	 * Standard Rule, multiple values
	 */
	@Test
	public void test061() {
		final GapCart gapCart = new GapCartImpl();
		gapCart.setPricerRule(new RegularPricingRuleImpl());

		final String scanBlock = "ABC";
		gapCart.scan(scanBlock.toCharArray());

		final int expected = (133) + (1300) + (67);
		Assert.assertEquals("price differs for A,B,C", expected, gapCart.total());
	}

	/**
	 * Special Rule, multiple values
	 */
	@Test
	public void test062() {
		final GapCart gapCart = new GapCartImpl();
		gapCart.setPricerRule(new SpecialPricingRuleImpl());

		final String scanBlock = "AAAAAAABBGGGGGGGGHHHHHHHHHHHHH";
		gapCart.scan(scanBlock.toCharArray());

		final int expected = (1 * 133 + 2 * 255) + (2 * 1300 + 0 * 0) + (1 * 222 + 1 * 711) + (1 * 893 + 3 * 1200);
		Assert.assertEquals("price differs for A,B,G,H", expected, gapCart.total());
	}

	/**
	 * Special Rule, multiple values randomly ordered
	 */
	@Test
	public void test063() {
		final GapCart gapCart = new GapCartImpl();
		gapCart.setPricerRule(new SpecialPricingRuleImpl());

		final String scanBlock = "HABAAHAAHBGGGGHHHGGGGHHAHHHHHA";
		gapCart.scan(scanBlock.toCharArray());

		final int expected = (1 * 133 + 2 * 255) + (2 * 1300 + 0 * 0) + (1 * 222 + 1 * 711) + (1 * 893 + 3 * 1200);
		Assert.assertEquals("price differs for A,B,G,H", expected, gapCart.total());
	}

	// ---------------------------------------------------------------------------

	private void checkSkuCounts(final String mixedItemSku) {
		final List<SkuCount> skuCounts = countSkus(mixedItemSku);
		Assert.assertNotNull("test sku counter counts null", skuCounts);

		gapCart.scan(mixedItemSku.toCharArray());

		final Map<Character, Integer> internalCart = new TreeMap<Character, Integer>(((GapCartImpl) gapCart).getCart());

		final Set<Entry<Character, Integer>> internalEntrySet = internalCart.entrySet();
		Assert.assertEquals("Sku count differs", skuCounts.size(), internalEntrySet.size());
		int i = 0;
		for (final Entry<Character, Integer> e : internalEntrySet) {
			final SkuCount scnt = skuCounts.get(i);
			Assert.assertEquals("Sku keys differ for " + i, e.getKey(), scnt.getKey());
			Assert.assertEquals("Sku counts differ for " + i, e.getValue(), scnt.getValue());
			i++;
		}
	}

	private static final class SkuCount implements Map.Entry<Character, Integer> {
		private final Character key;
		private Integer value;

		public SkuCount(final Character key, final Integer value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public Character getKey() {
			return key;
		}

		@Override
		public Integer getValue() {
			return value;
		}

		@Override
		public Integer setValue(final Integer value) {
			this.value = value;
			return value;
		}
	}

	private static List<SkuCount> countSkus(final String skuValues) {
		final char[] vals = orderSkus(skuValues);

		final List<SkuCount> counts = new ArrayList<SkuCount>();
		final int nSku = vals.length;
		if (nSku > 0) {
			Character last = null;
			SkuCount sc = null;
			for (int i = 0; i < nSku; i++) {
				Character sku = vals[i];
				if (sku.equals(last))
					sc.setValue(sc.getValue() + 1);
				else {
					if (sc != null)
						counts.add(sc);
					sc = new SkuCount(sku, 1);
					last = sku;
				}
			}
			counts.add(sc);
		}

		return counts;
	}

	private static char[] orderSkus(final String skuValues) {
		final char[] ar = skuValues.toCharArray();
		Arrays.sort(ar);
		return ar;
	}
}
