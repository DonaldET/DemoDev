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
import don.demo.bignumeric.impl.KahanAdder;
import don.demo.bignumeric.impl.ShuffleSequenceGenerator;

public class KahanShuffleAdderTest {
	private static final double SP_DELTA = 0.000000001;
	private static final long MAX_ITERATIONS = 2000;

	private SequenceGenerator generator = null;
	private Adder adder = null;
	private boolean echo = false;

	@Before
	public void setUp() throws Exception {
		generator = new ShuffleSequenceGenerator();
		adder = new KahanAdder(generator, MAX_ITERATIONS);
	}

	@After
	public void tearDown() throws Exception {
		generator = null;
		adder = null;
	}

	@Test
	public void testName() {
		Assert.assertEquals("name differs", "KahanAdder", adder.getName());
	}

	@Test
	public void testKahanAdder() {
		if (echo)
			System.err.println("Inital Adder State      : " + adder);

		final Result result = adder.runSequence();
		if (echo) {
			System.err.println("Sequence [n:" + MAX_ITERATIONS + "] Result  : " + result);
			System.err.println("Final Adder State       : " + adder);
		}

		final double expectedSum = 922681.375;
		final float actualSum = adder.getSum();
		Assert.assertEquals("sum differs", expectedSum, actualSum, SP_DELTA);
		final double expectedLastSum = 922103.25;
		Assert.assertEquals("lastSum differs", expectedLastSum, adder.getLastSum(), SP_DELTA);

		final int lastIteration = 2000;
		Assert.assertEquals("iteration count differs", lastIteration, result.getFinalIteration());
		Assert.assertTrue("underflow", result.isCompletedIterations());
		Assert.assertFalse("matched last", result.isNewMatchedPrevious());

		Assert.assertEquals("in-exact sum for " + lastIteration, generator.correctSum(), actualSum, SP_DELTA);
	}
}