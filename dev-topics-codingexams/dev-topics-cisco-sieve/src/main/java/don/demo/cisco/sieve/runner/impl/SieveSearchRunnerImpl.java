/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package don.demo.cisco.sieve.runner.impl;

import org.apache.commons.lang3.Validate;

import don.demo.cisco.sieve.runner.SieveSearchRunner;
import don.demo.cisco.sieve.searcher.SieveSearcher;

/**
 * Runs a sieve search instance that incorporates an algorithm run repeatedly
 * while being timed.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class SieveSearchRunnerImpl implements SieveSearchRunner {
	private boolean initialized = false;
	private int repeats;
	private int[] data;
	private int topCount;
	private int inputSize;
	private SieveSearcher searcher;

	public SieveSearchRunnerImpl() {
	}

	@Override
	public void init(final int repeatCounts, final int[] inputData, final int maxCount, final SieveSearcher sieveSearcher) {
		Validate.isTrue(repeatCounts > 0, "repeats < 1, ", repeatCounts);
		Validate.notNull(inputData, "data null");
		inputSize = inputData.length;
		Validate.isTrue(inputSize > 0, "inputSize < 1, ", inputSize);
		Validate.isTrue(maxCount > 0, "topCount < 1, ", maxCount);
		Validate.isTrue(inputSize >= maxCount, "inputSize < topCount");
		Validate.notNull(sieveSearcher, "searcher null");

		this.repeats = repeatCounts;
		this.data = inputData;
		this.topCount = maxCount;
		this.searcher = sieveSearcher;

		initialized = true;

		return;
	}

	@Override
	public int[] runTest() {
		Validate.isTrue(initialized, "uninitialized");

		int[] topValues = null;
		for (int i = 0; i < repeats; i++) {
			topValues = searcher.search(data, topCount);
			Validate.notNull(topValues, "test iteration " + (i + 1) + " returned null");
			int lth = topValues.length;
			Validate.isTrue(lth == topCount, "bad return length " + lth + ", expected ", topCount);
		}

		return topValues;
	}

	@Override
	public int getRepeats() {
		return repeats;
	}

	@Override
	public int[] getData() {
		return data;
	}

	@Override
	public int getTopCount() {
		return topCount;
	}

	@Override
	public SieveSearcher getSearcher() {
		return searcher;
	}
}
