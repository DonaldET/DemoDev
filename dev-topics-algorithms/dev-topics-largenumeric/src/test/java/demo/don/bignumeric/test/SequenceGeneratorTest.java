/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.bignumeric.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demo.bignumeric.api.SequenceGenerator;
import don.demo.bignumeric.impl.RightShiftSequenceGenerator;

public class SequenceGeneratorTest {
	private static final double SP_DELTA = 0.000000001;

	private SequenceGenerator generator = null;

	@Before
	public void setUp() throws Exception {
		generator = new RightShiftSequenceGenerator();
	}

	@After
	public void tearDown() throws Exception {
		generator = null;
	}

	@Test
	public void testSimpleGetNext() {
		float next = generator.getNext();
		float expected = (float) 4768768.0;
		Assert.assertEquals("itr1 next differs", expected, next, SP_DELTA);
		expected = Float.MIN_NORMAL; // 1.1754943508222875E-38
		Assert.assertEquals("itr count", 1, generator.getIteration());
	}

	@Test
	public void testMultiGetNext() {
		float next = generator.getNext();
		float expected = (float) 4768768.0;
		Assert.assertEquals("itr1 next differs", expected, next, SP_DELTA);

		expected = (float) 2384384.0;
		next = generator.getNext();
		Assert.assertEquals("itr2 next differs", expected, next, SP_DELTA);

		Assert.assertEquals("itr count", 2, generator.getIteration());
	}
}
