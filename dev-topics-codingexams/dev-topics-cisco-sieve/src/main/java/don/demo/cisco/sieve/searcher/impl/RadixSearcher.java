/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package don.demo.cisco.sieve.searcher.impl;

import don.demo.cisco.sieve.searcher.SieveSearcher;

public class RadixSearcher implements SieveSearcher {
	final int bound;

	public RadixSearcher(final int bound) {
		this.bound = bound;
	}

	@Override
	public int[] search(final int[] data, final int topCount) {
		byte[] flags = new byte[bound + 1];

		for (int i = 0; i < data.length; i++)
			flags[data[i]] = 1;

		final int[] results = new int[topCount];
		int foundCount = 0;
		for (int i = 1; foundCount < topCount && i <= flags.length; i++) {
			if (flags[i] != 0)
				results[foundCount++] = i;
		}

		return results;
	}

	@Override
	public String getName() {
		return "Radix Search";
	}
}
