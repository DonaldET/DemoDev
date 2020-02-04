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
import don.demo.bignumeric.api.Result;
import don.demo.bignumeric.api.SequenceGenerator;

/**
 * Shared implementation for adders with specific accumulation strategies in the
 * template method.
 * <p>
 * <strong>Note:</strong>
 * <p>
 * This approach to floating point addition is based on work done at the
 * <em>Univesity of California</em>, Berkeley, in the early 70's, and originally
 * coded in <code>C++</code>.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public abstract class AbstractAdder implements Adder {
	protected String name = "AbstractAdder";
	protected long maxIterations;
	protected long iteration = 0;
	protected SequenceGenerator generator;
	private float element = Float.MIN_NORMAL;
	private float sum = 0;
	private float lastSum = Float.MIN_NORMAL;

	protected AbstractAdder(final SequenceGenerator generator, final long maxIterations) {
		this.generator = generator;
		this.maxIterations = maxIterations;
	}

	@Override
	public Result runSequence() {
		boolean newMatchedPrevious = false;
		boolean finalIteration = iteration >= maxIterations;
		while (!newMatchedPrevious && !finalIteration) {
			iteration++;

			lastSum = sum;
			element = generator.getNext();
			sum = updateSum(sum, element);

			finalIteration = iteration >= maxIterations;
			newMatchedPrevious = sum == lastSum;
		}

		return new Result(finalIteration, newMatchedPrevious, iteration);
	}

	protected abstract float updateSum(final float sum, final float element);

	@Override
	public long getMaxIterations() {
		return maxIterations;
	}

	@Override
	public float getSum() {
		return sum;
	}

	@Override
	public float getLastSum() {
		return lastSum;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "[" + getClass().getSimpleName() + " - 0x" + Integer.toHexString(hashCode()) + ";  name: " + getName()
				+ ";  maxIterations: " + maxIterations + ";  generator: " + generator + ";  element: " + element
				+ ";  iteration: " + iteration + ";  sum: " + sum + ";  lastSum: " + lastSum + "]";
	}
}