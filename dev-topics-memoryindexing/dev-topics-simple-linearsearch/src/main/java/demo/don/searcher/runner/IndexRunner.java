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
 * Note, there are no runtime parameters to this program and test data come from
 * the class path with configured file names.</p>
 * <p>
 * The test search data comes from a <code>NameBuilder</code> instance that has
 * more than 46,000 fake entries, and uses a <code>String</code> based search
 * index. The output of a typical timing run looks like this:
 *
 * <pre>
 * <code>
 * *** Setup:
 * [SearchIndex - 0xb2a2d8;
 *   userIds[46035]: {
 *       [UserID - 0x49489a28;  firstName: john;  lastName: adelson][email: john.adelson_0@gmail.com]
 *       [UserID - 0xa35379b4;  firstName: aaron;  lastName: adelson][email: aaron.adelson_0@gmail.com]
 *       [UserID - 0xcf61c721;  firstName: abigail;  lastName: adelson][email: abigail.adelson_0@aol.com]
 *       [UserID - 0x3eab53e3;  firstName: adalyn;  lastName: adelson][email: adalyn.adelson_0@hotmail.com]
 *       [UserID - 0x15e93777;  firstName: adam;  lastName: adelson][email: adam.adelson_0@yahoo.com]
 *       [UserID - 0x3b35c32b;  firstName: addison;  lastName: adelson][email: addison.adelson_0@gmail.com]
 *       [UserID - 0x677183b0;  firstName: addison;  lastName: adelson][email: addison.adelson_0@intrinsx.com]
 *       . . .};
 *   searchIndex[2210438]: {|john|adelson|john.adelson_0@gmail.com{000|aaron|adelson|aaron.adelson_0@gmail.com{001|abigail|adelson|abigail.adelson_0@aol.com{002|adalyn|adelson|adalyn.adelson_0@hotmail.com{003|adam|adelson|adam.a . . .|}
 * ]
 * 
 * *** Single name Timing using Like
 * 5 non-repeating tests using Like, selected from 278 test names, and searched in 46035 users
 * Single Test[0], pattern 'aaron' got 20 matches in 0 milliseconds
 * Single Test[1], pattern 'caroline' got 20 matches in 0 milliseconds
 * Single Test[2], pattern 'gage' got 20 matches in 0 milliseconds
 * Single Test[3], pattern 'kira' got 20 matches in 0 milliseconds
 * Single Test[4], pattern 'parker' got 20 matches in 0 milliseconds
 * 
 * *** Multiple names Timing using Like
 * Repeating tests for 278 test names in 46035 users
 * Multi-run[0] = [0.447, 0, 16, 6.722]
 * Multi-run[1] = [0.396, 0, 16, 6.067]
 * Multi-run[2] = [0.447, 0, 16, 6.722]
 * Multi-run[3] = [0.396, 0, 16, 6.067]
 * Multi-run[4] = [0.393, 0, 16, 5.958]
 * 
 * *** Multiple no-match names Timing (failed search) using Like
 * Repeating tests for 278 test names in 46035 users
 * Multi-run[0] = [3.421, 0, 16, 41.683]
 * Multi-run[1] = [3.371, 0, 17, 41.334]
 * Multi-run[2] = [3.425, 0, 16, 41.77]
 * Multi-run[3] = [3.375, 0, 16, 41.206]
 * Multi-run[4] = [3.367, 0, 16, 41.24]
 * 
 * *** Single name Timing using Contains
 * 5 non-repeating tests using Contains, selected from 278 test names, and searched in 46035 users
 * Single Test[0], pattern 'aaron' got 20 matches in 0 milliseconds
 * Single Test[1], pattern 'caroline' got 20 matches in 0 milliseconds
 * Single Test[2], pattern 'gage' got 20 matches in 0 milliseconds
 * Single Test[3], pattern 'kira' got 20 matches in 0 milliseconds
 * Single Test[4], pattern 'parker' got 20 matches in 0 milliseconds
 * 
 * *** Multiple names Timing using Contains
 * Repeating tests for 278 test names in 46035 users
 * Multi-run[0] = [0.903, 0, 16, 13.254]
 * Multi-run[1] = [0.842, 0, 16, 12.436]
 * Multi-run[2] = [0.9, 0, 16, 13.257]
 * Multi-run[3] = [0.896, 0, 16, 13.151]
 * Multi-run[4] = [0.9, 0, 16, 13.257]
 * 
 * *** Multiple no-match names Timing (failed search) using Contains
 * Repeating tests for 278 test names in 46035 users
 * Multi-run[0] = [8.083, 0, 17, 60.925]
 * Multi-run[1] = [8.029, 0, 16, 60.978]
 * Multi-run[2] = [8.029, 0, 17, 60.985]
 * Multi-run[3] = [8.105, 0, 22, 60.986]
 * Multi-run[4] = [8.231, 0, 16, 15.12]
 * 
 * *** Done!
 * </code>
 * </pre>
 *
 *
 * A value above shown in brackets is the average, minimum, maximum, and
 * standard deviation times for the test in milliseconds.
 *
 * @author Donald Trummell
 */
public class IndexRunner
{
  /**
   * Collect the results (matches and elapsed time) of test using different
   * patterns against a test list. Here are the collected items:
   * <ul>
   * <li>Count of the matches</li>
   * <li>The pattern searched for</li>
   * <li>Elapsed time in milliseconds</li>
   * </ul>
   *
   * @author Donald Trummell
   *
   */
  public static class MatchResults
  {
    private final int[] matches;
    private final String pattern;
    private final long elapsed;

    public MatchResults(final int[] matches, final String pattern,
        final long elapsed)
    {
      super();

      Validate.notNull(matches, "matches null");
      Validate.notEmpty(pattern, "pattern empty");
      Validate.isTrue(elapsed > -1, "elapsed time negative, ", elapsed);

      this.matches = matches;
      this.pattern = pattern;
      this.elapsed = elapsed;
    }

    public int[] getMatches()
    {
      return matches;
    }

    public String getPattern()
    {
      return pattern;
    }

    public long getElapsed()
    {
      return elapsed;
    }
  }

  private NameBuilder builder = null;
  private int firstNameCount = Integer.MIN_VALUE;
  private int fullNameCount = Integer.MIN_VALUE;
  private UserID[] fakeUids = null;
  private SearchIndex<String> searchIndex = null;
  private IndexSearcher<String> searcher = null;

  public IndexRunner()
  {
    setupFakeNames();
    setupIndex();
  }

  private void setupFakeNames()
  {
    builder = new NameBuilder();
    builder.initialize();
    Validate.isTrue(builder.isInitialized(), "builder uninitialized");

    firstNameCount = builder.getFirstNames().length;
    fullNameCount = builder.getFullNames().length;
    fakeUids = builder.createFakeNames(firstNameCount, fullNameCount);
    Validate.notEmpty(fakeUids, "fake ids null or empty");
  }

  private void setupIndex()
  {
    final IndexBuilder<String> builder = new StringIndexBuilderImpl();
    searchIndex = builder.buildIndex(fakeUids);
    searcher = new StringIndexSearcherImpl();
    searcher.setSearchIndex(searchIndex);
  }

  /**
   * Perform the desired query, locating up to the max parameter matches
   *
   * @param pattern
   *          the string to locate
   * @param maxReturned
   *          the maximum permitted number of matches
   * @param like
   *          if <code>true</code> use <em>like</em> semantics else use
   *          <em>contains</em> semantics
   *
   * @return a possibly empty set of results
   */
  private MatchResults timeSearch(final String pattern, final int maxReturned,
      final boolean like)
  {
    Validate.notEmpty(pattern, "pattern empty");

    final String cleanPattern = IndexHelper.filterPattern(pattern);
    final long start = System.currentTimeMillis();
    final int[] matches = like ? searcher.findUsingLike(cleanPattern,
        maxReturned) : searcher.findUsingContains(cleanPattern, maxReturned);
    final long elapsed = System.currentTimeMillis() - start;

    return new MatchResults(matches, pattern, elapsed);
  }

  /**
   * Perform a single timing run for the set of test patterns
   *
   * @param patterns
   *          the string patterns to locate
   * @param maxTolerance
   *          the largest acceptable runtime variation around the mean
   * @param like
   *          if <code>true</code> use <em>like</em> semantics else use
   *          <em>contains</em> semantics
   *
   * @return the average, min, max, and standard deviation of elapsed times to
   *         execute queries
   */
  private Number[] timingOne(final String[] patterns,
      final double maxTolerance, final boolean like)
  {
    // Validate we have successful matches

    final int maxMatchCount = IndexSearcher.DEFAULT_MATCH_LIMIT;
    final String p0 = patterns[0];
    MatchResults results = timeSearch(p0, maxMatchCount, like);
    Validate.notNull(results, "results null for " + p0);

    long min = Integer.MAX_VALUE;
    long max = Integer.MIN_VALUE;
    long sum = 0;
    long sum2 = 0;
    final long start = System.currentTimeMillis();
    for (String pattern : patterns)
    {
      results = timeSearch(pattern, maxMatchCount, like);
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

    Validate.isTrue(delta < maxTolerance,
        "timing tolerance too big, overhead is " + overheadMean
            + ", individual is " + u + ", max is " + maxTolerance
            + ", delta is ", delta);

    return new Number[] { appRound(u, 1000.0), min, max,
        appRound(sum2 / trials - u * u, 1000.0) };
  }

  private double appRound(double x, double scale)
  {
    return Math.rint(scale * x + 0.5) / scale;
  }

  /**
   * @param args
   */
  public static void main(final String[] args)
  {
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

  private static void doSingleTimings(final IndexRunner runner,
      final String[] firstNames, final boolean like)
  {
    System.out.println("\n*** Single name Timing using "
        + (like ? "Like" : "Contains"));
    final int nTests = 5;
    final int nameDelta = firstNames.length / nTests;
    System.out.println(nTests + " non-repeating tests using "
        + (like ? "Like" : "Contains") + ", selected from "
        + runner.firstNameCount + " test names, and searched in "
        + runner.fakeUids.length + " users");

    final int limit = IndexSearcher.DEFAULT_MATCH_LIMIT;

    // Make sure all code is JIT processed and things initialized

    for (int i = 0; i < nTests; i++)
    {
      final String pattern = firstNames[i * nameDelta];
      runner.timeSearch(pattern, limit, like);
    }

    // Now perform the actual timing

    for (int i = 0; i < nTests; i++)
    {
      final String pattern = firstNames[i * nameDelta];
      final MatchResults results = runner.timeSearch(pattern, limit, like);
      System.out.println("Single Test[" + i + "], pattern '" + pattern
          + "' got " + results.getMatches().length + " matches in "
          + results.getElapsed() + " milliseconds");
    }
  }

  private static void doMultipleTimings(final IndexRunner runner,
      final String[] firstNames, final double maxTolerance, final boolean like)
  {
    System.out.println("\n*** Multiple names Timing using "
        + (like ? "Like" : "Contains"));
    System.out.println("Repeating tests for " + runner.firstNameCount
        + " test names in " + runner.fakeUids.length + " users");

    final int nTests = 5;
    for (int i = 0; i < nTests; i++)
    {
      final Number[] timing = runner.timingOne(firstNames, maxTolerance, like);
      System.out.println("Multi-run[" + i + "] = " + Arrays.toString(timing));
    }
  }

  private static void doMultipleTimingsFailed(final IndexRunner runner,
      final String[] realFirstNames, final double maxTolerance,
      final boolean like)
  {
    System.out
        .println("\n*** Multiple no-match names Timing (failed search) using "
            + (like ? "Like" : "Contains"));
    System.out.println("Repeating tests for " + runner.firstNameCount
        + " test names in " + runner.fakeUids.length + " users");

    final String[] firstNames = new String[runner.firstNameCount];
    for (int i = 0; i < runner.firstNameCount; i++)
      firstNames[i] = realFirstNames[i] + "XXXXX";

    final int nTests = 5;
    for (int i = 0; i < nTests; i++)
    {
      final Number[] timing = runner.timingOne(firstNames, maxTolerance, like);
      System.out.println("Multi-run[" + i + "] = " + Arrays.toString(timing));
    }
  }
}
