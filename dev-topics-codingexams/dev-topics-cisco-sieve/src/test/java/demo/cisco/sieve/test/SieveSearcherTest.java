/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.cisco.sieve.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.cisco.sieve.searcher.SieveSearcher;
import demo.cisco.sieve.searcher.impl.BinaryArraySearcher;
import demo.cisco.sieve.searcher.impl.NiaveSearcher;
import demo.cisco.sieve.searcher.impl.RadixSearcher;
import demo.cisco.sieve.searcher.impl.TreeMapSearcher;

/**
 * Functional test, find &quot;top&quot; five values - meaning least five values
 * - in a larger list
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class SieveSearcherTest {
	private static final List<Integer> valuesList = Arrays.asList(14, 31, 57, 2, 3, 4, 21, 18, 7, 9, 11, 5, 102, 9, 1,
			101);

	private static final int[] values = new int[valuesList.size()];

	private static final int[] collected = { 1, 2, 3, 4, 5 };

	private static final int topCount = 5;

	static {
		int i = 0;
		for (final int v : valuesList)
			values[i++] = v;
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBASearch() {
		final SieveSearcher searcher = new BinaryArraySearcher();
		checkName(searcher, "Sorted Array / Binary Search");
		checkSearch(searcher);
	}

	@Test
	public void testNiaSearch() {
		final SieveSearcher searcher = new NiaveSearcher();
		checkName(searcher, "Sort All First");
		checkSearch(searcher);
	}

	@Test
	public void testRadSearch() {
		// the size of the "buckets" that bin the search; i.e., the max int value
		final int bound = Collections.max(valuesList);
		final SieveSearcher searcher = new RadixSearcher(bound);
		checkName(searcher, "Radix Search");
		checkSearch(searcher);
	}

	@Test
	public void testTreeSearch() {
		final SieveSearcher searcher = new TreeMapSearcher();
		checkName(searcher, "TreeMap - Red-Black Tree");
		checkSearch(searcher);
	}

	// ---------------------------------------------------------------------------

	private void checkName(final SieveSearcher searcher, final String name) {
		Assert.assertEquals("unexpected name", name, searcher.getName());
	}

	private void checkSearch(final SieveSearcher searcher) {
		final int[] searchResult = searcher.search(values, topCount);
		Assert.assertNotNull(searcher.getName() + " got null", searchResult);
		Assert.assertEquals(searcher.getName() + " found wrong count of values", collected.length, searchResult.length);

		for (int i = 0; i < collected.length; i++)
			Assert.assertEquals(searcher.getName() + " found wrong value at [" + i + "]", collected[i],
					searchResult[i]);
	}
}
