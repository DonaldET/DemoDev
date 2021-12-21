/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.don.apple.factorial.impl;

import demo.don.apple.factorial.MathHelper;

/**
 * Use a table of pre-computed factorial values to by-pass execution all
 * together or decrease the total number of required multiplications
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class PreComputedMathHelper implements MathHelper {
	/**
	 * Pre-computed Table of size M has 0!, 1! . . . (M - 1)!
	 */
	private static long[] zeroBasedPrecompute = { 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L,
			39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L };
	public static int knownLength = zeroBasedPrecompute.length;

	public PreComputedMathHelper() {
	}

	@Override
	public String getName() {
		return "PreComputed";
	}

	@Override
	public long factorial(final int n) {
		if (n < 0)
			throw new IllegalArgumentException("undefined factorial for " + n);

		//
		// Use the table with 0! ... (M - 1)!

		final int lastIndex = knownLength - 1;
		if (n <= lastIndex)
			return zeroBasedPrecompute[n];

		if (n > MAX_FACTORIAL_ARGUMENT)
			throw new IllegalArgumentException(
					n + " is too big to compute factorial, max is " + MAX_FACTORIAL_ARGUMENT);

		//
		// Pre-computed Table of size M has 0!, 1! . . . (M - 1)!
		// Compute N! beyond end of table,
		// (M - 1)! * M * M+1 * ... N
		//

		return bumpFactorial(n, knownLength, zeroBasedPrecompute[lastIndex]);
	}

	private static final long bumpFactorial(final int n, final int start, final long tableValue) {
		long val = tableValue;
		for (long f = start; f <= n; f++)
			val *= f;

		return val;
	}
}
