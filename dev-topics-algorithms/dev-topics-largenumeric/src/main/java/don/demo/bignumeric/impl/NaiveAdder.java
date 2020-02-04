/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package don.demo.bignumeric.impl;

import don.demo.bignumeric.api.Adder;
import don.demo.bignumeric.api.SequenceGenerator;

/**
 * A simple adder that eventually ignores additional addends because the running
 * sum is much larger than the addin.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class NaiveAdder extends AbstractAdder implements Adder {
	public NaiveAdder(final SequenceGenerator generator, final long maxIterations) {
		super(generator, maxIterations);
		this.name = getClass().getSimpleName();
	}

	@Override
	protected float updateSum(final float sum, final float element) {
		return sum + element;
	}
}