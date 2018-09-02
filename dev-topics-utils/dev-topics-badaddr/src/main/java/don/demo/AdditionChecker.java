package don.demo;

/**
 * Copyright (c) 2018. Donald E. Trummell. All Rights Reserved. Permission to
 * use, copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities. package don.demo;
 */

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import don.demo.badaddr.BadAddrRunner;
import don.demo.badaddr.BadAddrRunner.Summation;
import don.demo.badaddr.impl.BadAddrRunnerImpl;
import don.demo.badaddr.impl.KahanGoodAddrImpl;
import don.demo.datagen.DataGenerator;
import don.demo.datagen.GeneratorUtil;
import don.demo.datagen.impl.SimpleSequenceDataGenerator;

/**
 * Run a series of ADDITION tests to validate accuracy along with timing.
 * <p>
 * A raw test sequence is a sequence of natural numbers that fit in a Java
 * double-precision floating-point value without loss of significance (see
 * <a href=
 * "https://docs.oracle.com/javase/specs/jvms/se6/html/Overview.doc.html">Oracle
 * Docs</a>.) Java DP values have 53 bits of significance and hold a natural
 * number exactly up to 9,007,199,254,740,991. There are a little more than 15
 * digits held exactly.
 * <p>
 * When computing sums of natural numbers, exact DP calculations limit the
 * sequence to 134,164,078 with an integer summation value of
 * 8,999,999,979,877,081. This is our "<em>Natural</em>" test sequence of
 * numbers that our <em>adder</em> implementations will sum.
 * <p>
 * We also create a "<em>scaled</em>" test sequence by dividing a natural number
 * test sequence by a prime number; we do this to force round off error into the
 * sequence, and therefore error into the computed sum (e.g., the prime
 * <em>7919</em>.)
 * <p>
 * Given an initial sequence of natural numbers, and fractions from those
 * natural numbers (scaled by a prime), we can then randomly order those
 * numbers. Order is important because naive addition performs best when summed
 * smallest-to-largest, and worst when ordered largest-to-smallest. Randomized
 * sums are considered an average case.
 * <p>
 * Please note that our tests have sizes that are roughly logarithmically
 * increasing in size. We also time the overall tests for performance analysis.
 * </p>
 * <table summary="Test Sequence Labeling">
 * <tr>
 * <th>Sequence</th>
 * <th>Order</th>
 * <th>Distribution</th>
 * <th>Label</th>
 * </tr>
 * <tr>
 * <td>Natural</td>
 * <td>Smallest first</td>
 * <td>Sequential</td>
 * <td>NAT-SML-SEQ</td>
 * </tr>
 * <tr>
 * <td>Natural</td>
 * <td>Largest first</td>
 * <td>Sequential</td>
 * <td>NAT-LRG-SEQ</td>
 * </tr>
 * <tr>
 * <td>Natural</td>
 * <td>Random</td>
 * <td>Parallel</td>
 * <td>NAT-RND-PAR</td>
 * </tr>
 * <tr>
 * <td>Scaled</td>
 * <td>Smallest first</td>
 * <td>Sequential</td>
 * <td>SCL-SML-SEQ</td>
 * </tr>
 * <tr>
 * <td>Scaled</td>
 * <td>Largest first</td>
 * <td>Sequential</td>
 * <td>SCL-LRG-SEQ</td>
 * </tr>
 * <tr>
 * <td>Scaled</td>
 * <td>Random</td>
 * <td>Sequential</td>
 * <td>SCL-RND-SEQ</td>
 * </tr>
 * <tr>
 * <td>Scaled</td>
 * <td>Random</td>
 * <td>Parallel</td>
 * <td>SCL-RND-PAR</td>
 * </tr>
 * <tr>
 * <td>Scaled-Kahan</td>
 * <td>Random</td>
 * <td>Sequential</td>
 * <td>SCLK-RND-SEQ</td>
 * </tr>
 * </table>
 * <p>
 * Test output is in CSV format to allow Excel analysis. The columns in the
 * report are:
 * <li><em>n</em> - number of values to sum</li>
 * <li><em>expected</em> - the expected or true sum</li>
 * <li><em>actual</em> - the observed or computed sum</li>
 * <li><em>delta</em> - the difference between expected and actual (expected /
 * actual)</li>
 * <li><em>relative</em> - the relative error: (expected - actual) / expected;
 * multiplied by 10**9</li>
 * <li><em>sigd</em> - the number of significant digits [computed as log10(0.5)
 * - log10(abs(relative))]</li>
 * <li><em>elapsed</em> - summation time in milliseconds</li>
 * <li><em>label</em> - test identification (taken from table above)</li>
 * </ol>
 * <p>
 * 
 * @author Donald Trummell
 */
public class AdditionChecker
{

    private static final String version = "1.0";

    private static final double RELATIVE_ERROR_PPB = 1.0E+9;

    private static final String TEST_HEADER = "n,expected,actual,delta,relative,sigd,elapsed,label";

    private static List<Integer> testRanges = Arrays.asList(10, 100, 1_000, 10_000, 100_000, 1_000_000, 5_000_000,
            10_000_000, 50_000_000, 75_000_000, 100_000_000);

    private static final double PRIME_DIVISOR = 7919.0;

    /**
     * Create a test sequence and it's actual sum.
     * 
     * @author Donald Trummell
     */
    private interface SequenceBuilder
    {
        public abstract List<Double> readyTestSequence(final int n);

        public abstract double trueSum(final int n);
    }

    /**
     * Run a test for each element in the test sequence
     * 
     * @author Donald Trummell
     */
    private interface SingleTestRunner
    {
        public abstract Result runATest(final String label, final List<Double> testSeq, final BadAddrRunner badr,
                final double trueSum, final boolean isParallel);
    }

    public AdditionChecker() {
    }

    public static void main(String[] args)
    {
        System.out.println("\n==== Addition Checker (Version " + version + ") ====\n  Using prime " + PRIME_DIVISOR);
        final String JVM_PARALLEL_PROP = "java.util.concurrent.ForkJoinPool.common.parallelism";

        final String parallelProperty = System.getProperty(JVM_PARALLEL_PROP);
        System.out.println("  " + JVM_PARALLEL_PROP + "=" + String.valueOf(parallelProperty));
        System.out.println("  " + "Common Pool Parallelism = " + ForkJoinPool.getCommonPoolParallelism());

        final AdditionChecker ac = new AdditionChecker();
        ac.showTestSequence();

        final SingleTestRunner aTestRunner = new SingleTestRunner()
        {
            @Override
            public Result runATest(final String label, final List<Double> testSeq, final BadAddrRunner badr,
                    final double trueSum, final boolean isParallel)
            {
                final Summation testResult = badr.doOperation(label, testSeq, isParallel);
                final double estimatedSum = testResult.testSum;
                final double deltaErr = trueSum - estimatedSum;
                final double relErr = deltaErr / trueSum;

                return new Result(estimatedSum, deltaErr, relErr, testResult.elapsed);
            }
        };

        // ---------------------------------------------------------------------
        // ---- Natural Numbers ------------------------------------------------
        // ---------------------------------------------------------------------

        ac.runTestGroup("NAT-SML-SEQ", aTestRunner, new SequenceBuilder()
        {
            @Override
            public List<Double> readyTestSequence(final int n)
            {
                final DataGenerator dg = new SimpleSequenceDataGenerator();
                return dg.generateSequence(1, 1, n);
            }

            @Override
            public double trueSum(final int n)
            {
                return GeneratorUtil.sum_n(n);
            }
        }, testRanges, new BadAddrRunnerImpl(), System.out, false);

        ac.runTestGroup("NAT-LRG-SEQ", aTestRunner, new SequenceBuilder()
        {
            @Override
            public List<Double> readyTestSequence(final int n)
            {
                final DataGenerator dg = new SimpleSequenceDataGenerator();
                final List<Double> testSequence = dg.generateSequence(1, 1, n);
                dg.sortSampleDescending(testSequence);
                return testSequence;
            }

            @Override
            public double trueSum(final int n)
            {
                return GeneratorUtil.sum_n(n);
            }
        }, testRanges, new BadAddrRunnerImpl(), System.out, false);

        ac.runTestGroup("NAT-RND-PAR", aTestRunner, new SequenceBuilder()
        {
            @Override
            public List<Double> readyTestSequence(final int n)
            {
                final DataGenerator dg = new SimpleSequenceDataGenerator();
                final List<Double> testSequence = dg.generateSequence(1, 1, n);
                dg.shuffleSample(testSequence);
                return testSequence;
            }

            @Override
            public double trueSum(final int n)
            {
                return GeneratorUtil.sum_n(n);
            }
        }, testRanges, new BadAddrRunnerImpl(), System.out, true);

        // ---------------------------------------------------------------------
        // ---- Scaled Natural Numbers -----------------------------------------
        // ---------------------------------------------------------------------

        ac.runTestGroup("SCL-SML-SEQ", aTestRunner, new SequenceBuilder()
        {
            @Override
            public List<Double> readyTestSequence(final int n)
            {
                final DataGenerator dg = new SimpleSequenceDataGenerator();
                final List<Double> testSequence = dg.generateSequence(1, 1, n);
                dg.divideScaleSample(testSequence, PRIME_DIVISOR);
                return testSequence;
            }

            @Override
            public double trueSum(final int n)
            {
                return GeneratorUtil.sum_n(n) / PRIME_DIVISOR;
            }
        }, testRanges, new BadAddrRunnerImpl(), System.out, false);

        ac.runTestGroup("SCL-LRG-SEQ", aTestRunner, new SequenceBuilder()
        {
            @Override
            public List<Double> readyTestSequence(final int n)
            {
                final DataGenerator dg = new SimpleSequenceDataGenerator();
                final List<Double> testSequence = dg.generateSequence(1, 1, n);
                dg.divideScaleSample(testSequence, PRIME_DIVISOR);
                dg.sortSampleDescending(testSequence);
                return testSequence;
            }

            @Override
            public double trueSum(final int n)
            {
                return GeneratorUtil.sum_n(n) / PRIME_DIVISOR;
            }
        }, testRanges, new BadAddrRunnerImpl(), System.out, false);

        ac.runTestGroup("SCL-RND-SEQ", aTestRunner, new SequenceBuilder()
        {
            @Override
            public List<Double> readyTestSequence(final int n)
            {
                final DataGenerator dg = new SimpleSequenceDataGenerator();
                final List<Double> testSequence = dg.generateSequence(1, 1, n);
                dg.divideScaleSample(testSequence, PRIME_DIVISOR);
                dg.shuffleSample(testSequence);
                return testSequence;
            }

            @Override
            public double trueSum(final int n)
            {
                return GeneratorUtil.sum_n(n) / PRIME_DIVISOR;
            }
        }, testRanges, new BadAddrRunnerImpl(), System.out, false);

        ac.runTestGroup("SCL-RND-PAR", aTestRunner, new SequenceBuilder()
        {
            @Override
            public List<Double> readyTestSequence(final int n)
            {
                final DataGenerator dg = new SimpleSequenceDataGenerator();
                final List<Double> testSequence = dg.generateSequence(1, 1, n);
                dg.divideScaleSample(testSequence, PRIME_DIVISOR);
                dg.shuffleSample(testSequence);
                return testSequence;
            }

            @Override
            public double trueSum(final int n)
            {
                return GeneratorUtil.sum_n(n) / PRIME_DIVISOR;
            }
        }, testRanges, new BadAddrRunnerImpl(), System.out, true);

        // ---------------------------------------------------------------------
        // ---- Accurate summation of Scaled Natural Numbers -------------------
        // ---------------------------------------------------------------------

        ac.runTestGroup("SCLK-RND-SEQ", aTestRunner, new SequenceBuilder()
        {
            @Override
            public List<Double> readyTestSequence(final int n)
            {
                final DataGenerator dg = new SimpleSequenceDataGenerator();
                final List<Double> testSequence = dg.generateSequence(1, 1, n);
                dg.divideScaleSample(testSequence, PRIME_DIVISOR);
                dg.shuffleSample(testSequence);
                return testSequence;
            }

            @Override
            public double trueSum(final int n)
            {
                return GeneratorUtil.sum_n(n) / PRIME_DIVISOR;
            }
        }, testRanges, new KahanGoodAddrImpl(), System.out, false);
    }

    private void showTestSequence()
    {
        System.out.println("=== Test Sequence ===");
        System.out.println("     N: " + testRanges);
        System.out.print("  logN: [");
        for (int i = 0; i < testRanges.size(); i++)
        {
            if (i > 0)
            {
                System.out.print(", ");
            }
            System.out.print(String.format("%.2f", Math.log((double) testRanges.get(i))));
        }
        System.out.println("]");
    }

    /**
     * Individual test result
     */
    private static final class Result
    {
        public final double estimated;
        public final double delErr;
        public final double relErr;
        public final long elapsed;

        public Result(final double estimated, final double delErr, final double relErr, final long elapsed) {
            super();
            this.estimated = estimated;
            this.delErr = delErr;
            this.relErr = relErr;
            this.elapsed = elapsed;
        }
    }

    /**
     * Print a line of the test.
     */
    private void runTestGroup(final String label, final SingleTestRunner singleTest, final SequenceBuilder sb,
            final List<Integer> testSeqSizes, final BadAddrRunner badr, final PrintStream report,
            final boolean isParallel)
    {

        report.println("\n" + TEST_HEADER);

        final long start = System.currentTimeMillis();
        for (final Integer testSize : testSeqSizes)
        {
            final List<Double> testSequence = sb.readyTestSequence(testSize);
            final double trueSum = sb.trueSum(testSize);
            final Result theResult = singleTest.runATest(label, testSequence, badr, trueSum, isParallel);
            final double rel = theResult.relErr;
            final double timeInMS = (double) theResult.elapsed / 1000000.0;
            report.print(String.format("%d, %.5f, %.5f", testSize, trueSum, theResult.estimated));
            report.print(String.format(", %.4e, %.3e, %.1f", theResult.delErr, rel * RELATIVE_ERROR_PPB,
                    estimateSignificantDigits(rel)));
            report.println(String.format(", %.3f, %s", timeInMS, label));
        }
        final long elapsed = System.currentTimeMillis() - start;
        report.println(String.format("Total Test Time: %.3f seconds.", (double) elapsed / 1000.0));
    }

    /**
     * Given: abs(re) <= 0.5 * 10**(-M) for M significant digits. As a result, M
     * <= log10(0.5) - log10(abs(re))
     */
    private double estimateSignificantDigits(final double rel)
    {
        final double absrel = Math.abs(rel);

        if (absrel < 1.0E-15)
            return 16.0;

        final double SIG_DIGIT_OFFSET = Math.log10(0.5);

        final double sd = SIG_DIGIT_OFFSET - Math.log10(absrel);
        assert sd >= 0.0;

        return Math.rint(10.0 * sd + 0.5) / 10.0;
    }
}
