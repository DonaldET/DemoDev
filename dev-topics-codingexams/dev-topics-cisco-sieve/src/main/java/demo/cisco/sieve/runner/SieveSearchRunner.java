/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.cisco.sieve.runner;

import demo.cisco.sieve.searcher.SieveSearcher;

/**
 * An implementation wraps a search algorithm in a &quot;runner&quot; that runs
 * repeated executions for different &quot;top&quot; counts and different
 * &quot;repeat&quot; counts
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface SieveSearchRunner {
	public abstract void init(final int repeats, final int[] data, final int topCount, final SieveSearcher searcher);

	public abstract int[] runTest();

	public abstract int getRepeats();

	public abstract int[] getData();

	public abstract int getTopCount();

	public abstract SieveSearcher getSearcher();
}
