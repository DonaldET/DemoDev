/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.cisco.sieve.searcher.impl;

import java.util.Arrays;

import demo.cisco.sieve.searcher.SieveSearcher;

public class BinaryArraySearcher implements SieveSearcher {
	public static class Ends {
		public int endPoint;
		public int prior2End;

		public Ends(final int endPoint, final int prior2End) {
			this.endPoint = endPoint;
			this.prior2End = prior2End;
		}
	}

	@Override
	public int[] search(final int[] data, final int topCount) {
		final int[] topPicks = new int[topCount];

		int lth = Math.min(data.length, topCount);
		System.arraycopy(data, 0, topPicks, 0, lth);
		Arrays.sort(topPicks);

		if (data.length <= topPicks.length)
			return topPicks;

		final int endIndex = topCount - 1;
		int endPointValue = topPicks[endIndex];
		for (int i = topCount; i < data.length; i++) {
			final Integer keyValue = data[i];
			if (keyValue < endPointValue) {
				//
				// Look and see if we are already there

				int ip = Arrays.binarySearch(topPicks, keyValue);
				if (ip < 0) {
					// ip = -(insertion_point) - 1, so insertion_point = -(ip + 1)

					final int insertP = -ip - 1;
					if (insertP <= endIndex) {
						if (insertP != endIndex) {
							lth = endIndex - insertP;
							if (lth > 0)
								System.arraycopy(topPicks, insertP, topPicks, insertP + 1, lth);
						}
						topPicks[insertP] = keyValue;
					}
				}
			}
		}

		return topPicks;
	}

	@Override
	public String getName() {
		return "Sorted Array / Binary Search";
	}
}
