/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.searcher.runner;

import java.util.Arrays;

import org.apache.commons.lang3.Validate;

import demo.don.searcher.SearchIndex;
import demo.don.searcher.UserID;
import demo.don.searcher.databuilder.NameBuilder;
import demo.don.searcher.index.IndexBuilder;
import demo.don.searcher.index.IndexHelper;
import demo.don.searcher.index.impl.StringIndexBuilderImpl;
import demo.don.searcher.match.IndexSearcher;
import demo.don.searcher.match.impl.StringIndexSearcherImpl;

/**
 * Test program is used to determine elapsed time various kinds of searches:
 * <ol>
 * <li>single pattern successful search,</li>
 * <li>multiple pattern successful search, and</li>
 * <li>multiple pattern <em>failed</em> search</li>
 * <p>
 * </ol>
 * Note, there are no runtime command-line parameters to this program and test
 * data come from the class path with configured file names.
 * </p>
 * <p>
 * The test search data comes from a <code>NameBuilder</code> instance that has
 * more than 46,000 fake entries, and uses a <code>String</code> based search
 * index. A list of timing values, shown in brackets, is: [the average, minimum,
 * maximum, and standard deviation] times for the test, calculated in
 * milliseconds.
 *
 * @author Donald Trummell
 */
public class IndexRunner {

	private static final String TIMING_LABEL = "A list of timing values, shown in brackets, is:"
			+ " [the average, minimum, maximum, and standard deviation] times for the test, all"
			+ " calculated in milliseconds.";

	/**
	 * Collect the results (matches and elapsed time) of test using different
	 * patterns against a test list. Here are the collected items:
	 * <ul>
	 * <li>Count of the matches</li>
	 * <li>The pattern searched for</li>
	 * <li>Elapsed time in milliseconds</li>
	 * </ul>
	 *
	 * @author Donald Trummell (dtrummell@gmail.com)
	 *
	 */
	public static class MatchResults {
		private final int[] matches;
		private final String pattern;
		private final long elapsed;

		public MatchResults(final int[] matches, final String pattern, final long elapsed) {
			super();

			Validate.notNull(matches, "matches null");
			Validate.notEmpty(pattern, "pattern empty");
			Validate.isTrue(elapsed > -1, "elapsed time negative, ", elapsed);

			this.matches = matches;
			this.pattern = pattern;
			this.elapsed = elapsed;
		}

		public int[] getMatches() {
			return matches;
		}

		public String getPattern() {
			return pattern;
		}

		public long getElapsed() {
			return elapsed;
		}
	}

	private NameBuilder builder = null;
	private int firstNameCount = Integer.MIN_VALUE;
	private int fullNameCount = Integer.MIN_VALUE;
	private UserID[] fakeUids = null;
	private SearchIndex<String> searchIndex = null;
	private IndexSearcher<String> searcher = null;

	public IndexRunner() {
		setupFakeNames();
		setupIndex();
	}

	private void setupFakeNames() {
		builder = new NameBuilder();
		builder.initialize();
		Validate.isTrue(builder.isInitialized(), "builder uninitialized");

		firstNameCount = builder.getFirstNames().length;
		fullNameCount = builder.getFullNames().length;
		fakeUids = builder.createFakeNames(firstNameCount, fullNameCount);
		Validate.notEmpty(fakeUids, "fake ids null or empty");
	}

	private void setupIndex() {
		final IndexBuilder<String> builder = new StringIndexBuilderImpl();
		searchIndex = builder.buildIndex(fakeUids);
		searcher = new StringIndexSearcherImpl();
		searcher.setSearchIndex(searchIndex);
	}

	/**
	 * Perform the desired query, locating up to the max parameter matches,
	 * returning the matching values and timing results.
	 *
	 * @param pattern     the string to locate
	 * @param maxReturned the maximum permitted number of matches
	 * @param like        if <code>true</code> use <em>like</em> semantics else use
	 *                    <em>contains</em> semantics
	 *
	 * @return a possibly empty set of results
	 */
	private MatchResults timeSingleSearch(final String pattern, final int maxReturned, final boolean like) {
		Validate.notEmpty(pattern, "pattern empty");

		final String cleanPattern = IndexHelper.filterPattern(pattern);
		final long start = System.currentTimeMillis();
		final int[] matches = like ? searcher.findUsingLike(cleanPattern, maxReturned)
				: searcher.findUsingContains(cleanPattern, maxReturned);
		final long elapsed = System.currentTimeMillis() - start;

		return new MatchResults(matches, pattern, elapsed);
	}

	/**
	 * Perform a single timing run for each pattern in a set of test patterns; used
	 * to find average search time of set of patterns.
	 *
	 * @param patterns     the string patterns to locate
	 * @param maxTolerance the largest acceptable runtime variation around the mean
	 * @param like         if <code>true</code> use <em>like</em> semantics else use
	 *                     <em>contains</em> semantics
	 *
	 * @return the average, min, max, and standard deviation of elapsed times to
	 *         execute queries
	 */
	private Number[] timeMultiplePatterns(final String[] patterns, final double maxTolerance, final boolean like) {
		// Validate we have successful matches

		final int maxMatchCount = IndexSearcher.DEFAULT_MATCH_LIMIT;

		// Make sure all code is JIT processed and things initialized
		final String p0 = patterns[0];
		MatchResults results = timeSingleSearch(p0, maxMatchCount, like);
		Validate.notNull(results, "results null for " + p0);

		// Now perform the actual timing
		long min = Integer.MAX_VALUE;
		long max = Integer.MIN_VALUE;
		long sum = 0;
		long sum2 = 0;
		final long start = System.currentTimeMillis();
		for (String pattern : patterns) {
			results = timeSingleSearch(pattern, maxMatchCount, like);
			final long time4One = results.getElapsed();

			if (time4One < min)
				min = time4One;

			if (time4One > max)
				max = time4One;

			sum += time4One;

			sum2 += time4One * time4One;
		}
		final long elapsed = System.currentTimeMillis() - start;
		final double trials = patterns.length;
		final double overheadMean = elapsed / trials;
		double u = sum / trials;
		double delta = appRound(overheadMean - u, 10000.0);

		Validate.isTrue(delta < maxTolerance, "timing tolerance too big, overhead is " + overheadMean
				+ ", individual is " + u + ", max is " + maxTolerance + ", delta is ", delta);

		return new Number[] { appRound(u, 1000.0), min, max, appRound(sum2 / trials - u * u, 1000.0) };
	}

	/**
	 * Return scaled results with rounding.
	 * 
	 * @param x     the value to scale
	 * @param scale the scale factor (usually a power of ten)
	 * @return the scaled result, rounded up to the nearest scaled integer value
	 */
	private double appRound(double x, double scale) {
		return Math.rint(scale * x + (x < 0.0 ? -0.5 : 0.5)) / scale;
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final IndexRunner runner = new IndexRunner();

		final String[] firstNames = runner.builder.getFirstNames();
		System.out.println("\n*** Setup:\n" + runner.searchIndex);

		boolean like = true;
		doSingleTimings(runner, firstNames, like);
		doMultipleTimings(runner, firstNames, 0.03, like);
		doMultipleTimingsFailed(runner, firstNames, 0.06, like);

		like = false;
		doSingleTimings(runner, firstNames, like);
		doMultipleTimings(runner, firstNames, 0.02, like);
		doMultipleTimingsFailed(runner, firstNames, 0.06, like);

		System.out.println("\n*** Done!");
	}

	private static void doSingleTimings(final IndexRunner runner, final String[] firstNames, final boolean like) {
		System.out.println("\n*** Single name timing using " + (like ? "Like" : "Contains")
				+ ";\n    Average time for a single pattern");
		final int nTests = 5;
		final int nameDelta = firstNames.length / nTests;
		System.out.println(nTests + " non-repeating tests using " + (like ? "Like" : "Contains") + ", selected from "
				+ runner.firstNameCount + " test names, and searched in " + runner.fakeUids.length + " users");

		final int limit = IndexSearcher.DEFAULT_MATCH_LIMIT;

		// Make sure all code is JIT processed and things initialized
		for (int i = 0; i < nTests; i++) {
			final String pattern = firstNames[i * nameDelta];
			runner.timeSingleSearch(pattern, limit, like);
		}

		// Now perform the actual timing
		for (int i = 0; i < nTests; i++) {
			final String pattern = firstNames[i * nameDelta];
			final MatchResults results = runner.timeSingleSearch(pattern, limit, like);
			System.out.println("Single Test[" + i + "], pattern '" + pattern + "' got " + results.getMatches().length
					+ " matches in " + results.getElapsed() + " milliseconds");
		}
	}

	private static void doMultipleTimings(final IndexRunner runner, final String[] firstNames,
			final double maxTolerance, final boolean like) {
		System.out.println("\n*** Multiple names timing (averaging) using " + (like ? "Like" : "Contains")
				+ ";  Average time per pattern;\n    " + TIMING_LABEL);
		System.out.println(
				"Repeating tests for " + runner.firstNameCount + " test names in " + runner.fakeUids.length + " users");

		final int nTests = 5;
		for (int i = 0; i < nTests; i++) {
			final Number[] timing = runner.timeMultiplePatterns(firstNames, maxTolerance, like);
			System.out.println("Multi-run[" + i + "] = " + Arrays.toString(timing));
		}
	}

	private static void doMultipleTimingsFailed(final IndexRunner runner, final String[] realFirstNames,
			final double maxTolerance, final boolean like) {
		System.out.println("\n*** Multiple no-match names timing (averaging) using " + (like ? "Like" : "Contains")
				+ ";  Average time per pattern;\n    " + TIMING_LABEL);
		System.out.println(
				"Repeating tests for " + runner.firstNameCount + " test names in " + runner.fakeUids.length + " users");

		final String[] firstNames = new String[runner.firstNameCount];
		for (int i = 0; i < runner.firstNameCount; i++)
			firstNames[i] = realFirstNames[i] + "XXXXX";

		final int nTests = 5;
		for (int i = 0; i < nTests; i++) {
			final Number[] timing = runner.timeMultiplePatterns(firstNames, maxTolerance, like);
			System.out.println("Multi-run[" + i + "] = " + Arrays.toString(timing));
		}
	}
}
