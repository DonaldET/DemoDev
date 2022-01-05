/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.geturner.binarysearch.test;

import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.geturner.binarysearch.BinarySearch;

/**
 * Common tests for different binary search methods
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public abstract class BinarySearchChecker {
	public static final int POWER_OF_2 = 2 * 2 * 2 * 2;

	protected BinarySearch<Integer> search = null;

	public BinarySearchChecker() {
		super();
	}

	@Before
	public abstract void setUp() throws Exception;

	@After
	public void tearDown() throws Exception {
		search = null;
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindKeyNull() {
		Assert.assertTrue("wrong find", search.find(null, 0) == BinarySearch.KEY_NOT_FOUND);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindArrayNull() {
		Assert.assertTrue("wrong find", search.find(new Integer[0], null) == BinarySearch.KEY_NOT_FOUND);
	}

	@Test
	public void testArray0() {
		final Integer key = 9;
		final Integer[] array = new Integer[0];
		Assert.assertEquals("lth0 not there found", BinarySearch.KEY_NOT_FOUND, search.find(array, key));
	}

	public void testArray1() {
		final Integer key = 9;
		final Integer[] array = new Integer[] { key };
		Assert.assertEquals("lth1 there not found", 0, search.find(array, key));
		final Integer notKey = 17;
		Assert.assertEquals("lth1 not there found", BinarySearch.KEY_NOT_FOUND, search.find(array, notKey));
	}

	public void testArray2() {
		final Integer key = 9;
		final Integer[] array = new Integer[] { 1, key };
		Assert.assertEquals("lth2 there not found", 1, search.find(array, key));
		final Integer[] arrayR = new Integer[] { key, 1 };
		Assert.assertEquals("lth2 there not found", 0, search.find(arrayR, key));
		final Integer notKey = 17;
		Assert.assertEquals("lth2 not there found", BinarySearch.KEY_NOT_FOUND, search.find(array, notKey));
	}

	public void testArray3() {
		final Integer key = 9;
		final Integer[] array = new Integer[] { 1, 7, key };
		Assert.assertEquals("lth3 there not found", 2, search.find(array, key));
		final Integer[] arrayR = new Integer[] { 1, key, 11 };
		Assert.assertEquals("lth3 there not found", 1, search.find(arrayR, key));
		final Integer[] arrayRR = new Integer[] { key, 10, 11 };
		Assert.assertEquals("lth3 there not found", 0, search.find(arrayRR, key));
		final Integer notKey = 17;
		Assert.assertEquals("lth3 not there found", BinarySearch.KEY_NOT_FOUND, search.find(array, notKey));
	}

	@Test
	public void testArrayN2() {
		checkArray(POWER_OF_2);
	}

	@Test
	public void testArrayN2Odd() {
		checkArray(POWER_OF_2 + 1);
	}

	private void checkArray(final int lth) {
		final Integer[] array = new Integer[lth];
		for (int i = 0; i < lth; i++)
			array[i] = (i + 1);

		int key = 0;
		Assert.assertEquals("lth" + lth + " not found for " + key, BinarySearch.KEY_NOT_FOUND, search.find(array, key));

		key = 1;
		Assert.assertEquals("lth" + lth + " not found for " + key, Arrays.binarySearch(array, key),
				search.find(array, key));

		final int half = lth / 2;
		key = half - 1;
		Assert.assertEquals("lth" + lth + " not found for " + key, Arrays.binarySearch(array, key),
				search.find(array, key));

		key = half + 1;
		Assert.assertEquals("lth" + lth + " not found for " + key, Arrays.binarySearch(array, key),
				search.find(array, key));

		key = lth;
		Assert.assertEquals("lth" + lth + " not found for " + key, Arrays.binarySearch(array, key),
				search.find(array, key));

		key = lth + 1;
		Assert.assertEquals("lth" + lth + " not found for " + key, BinarySearch.KEY_NOT_FOUND, search.find(array, key));
	}
}