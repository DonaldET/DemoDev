/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package don.demo.don.bignumeric.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demo.bignumeric.api.Adder;
import don.demo.bignumeric.api.Result;
import don.demo.bignumeric.api.SequenceGenerator;
import don.demo.bignumeric.impl.NaiveAdder;
import don.demo.bignumeric.impl.RightShiftSequenceGenerator;

public class NiaveAdderTest {
	private static final double SP_DELTA = 0.000000001;
	private static final long MAX_ITERATIONS = 50;

	private SequenceGenerator generator = null;
	private Adder adder = null;
	private boolean echo = false;

	@Before
	public void setUp() throws Exception {
		generator = new RightShiftSequenceGenerator();
		adder = new NaiveAdder(generator, MAX_ITERATIONS);
	}

	@After
	public void tearDown() throws Exception {
		generator = null;
		adder = null;
	}

	@Test
	public void testName() {
		Assert.assertEquals("name differs", "NaiveAdder", adder.getName());
	}

	@Test
	public void testNaiveAdder() {
		if (echo)
			System.err.println("Inital Adder State      : " + adder);

		final Result result = adder.runSequence();
		if (echo) {
			System.err.println("Sequence [n:" + MAX_ITERATIONS + "] Result  : " + result);
			System.err.println("Final Adder State       : " + adder);
		}

		final double expectedSum = 9537536.0;
		final float actualSum = adder.getSum();
		Assert.assertEquals("sum differs", expectedSum, actualSum, SP_DELTA);
		final double expectedLastSum = 9537536.0;
		Assert.assertEquals("lastSum differs", expectedLastSum, adder.getLastSum(), SP_DELTA);

		final int lastIteration = 25;
		Assert.assertEquals("iteration count differs", lastIteration, result.getFinalIteration());
		Assert.assertFalse("did not underflow", result.isCompletedIterations());
		Assert.assertTrue("did not match last", result.isNewMatchedPrevious());

		Assert.assertEquals("in-exact sum for " + lastIteration, generator.correctSum(), actualSum, SP_DELTA);
	}
}
