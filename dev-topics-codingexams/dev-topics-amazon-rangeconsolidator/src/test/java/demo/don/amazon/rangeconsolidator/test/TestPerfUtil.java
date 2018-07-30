package demo.don.amazon.rangeconsolidator.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.Assert;

import demo.don.amazon.rangeconsolidator.Overlap;
import demo.don.amazon.rangeconsolidator.Overlap.Interval;

public abstract class TestPerfUtil
{
    // Provides Repeatability for the test
    public static final int INITIAL_SEED = 7187;

    // rerun an individual test-group and compute a central-tendency statistic
    // for execution time
    public static final int SMOOTHING_SAMPLE_SIZE = 3;

    // each interval to collapse fits in this range (just for testing limits)
    public static final int INTERVAL_LBOUND = 0;
    public static final int INTERVAL_UBOUND = 10000;

    // test execution times are too variable in run one, so run several times to
    // reduce variability, creating a test group
    static final int TEST_GROUP_REPETITION_FACTOR = 25;

    //
    // Functional test data
    private static final List<TestDef> testCases = new ArrayList<TestDef>();

    static
    {
        List<Interval> inputSet = Arrays.asList(new Interval(8, 10), new Interval(1, 4), new Interval(3, 6),
                new Interval(15, 18));
        String testLabel = "Set 0 - Problem statement";
        List<Interval> expected = Arrays.asList(new Interval(1, 6), new Interval(8, 10), new Interval(15, 18));
        testCases.add(new TestDef(inputSet, testLabel, expected, 1));

        inputSet = Arrays.asList();
        testLabel = "Set 1 - Empty";
        expected = Arrays.asList();
        testCases.add(new TestDef(inputSet, testLabel, expected, 0));

        inputSet = Arrays.asList(new Interval(1, 4));
        testLabel = "Set 2 - Trivial";
        expected = Arrays.asList(new Interval(1, 4));
        testCases.add(new TestDef(inputSet, testLabel, expected, 0));

        inputSet = Arrays.asList(new Interval(1, 10), new Interval(1, 10), new Interval(1, 10));
        testLabel = "Set 3 - All Same";
        expected = Arrays.asList(new Interval(1, 10));
        testCases.add(new TestDef(inputSet, testLabel, expected, 2));

        inputSet = Arrays.asList(new Interval(1, 10), new Interval(1, 12), new Interval(1, 15), new Interval(-2, -1));
        testLabel = "Set 4 - Inclusive";
        expected = Arrays.asList(new Interval(-2, -1), new Interval(1, 15));
        testCases.add(new TestDef(inputSet, testLabel, expected, 2));

        inputSet = Arrays.asList(new Interval(1, 10), new Interval(10, 12), new Interval(12, 15), new Interval(-2, -1));
        testLabel = "Set 5 - Adjacent";
        expected = Arrays.asList(new Interval(-2, -1), new Interval(1, 15));
        testCases.add(new TestDef(inputSet, testLabel, expected, 2));

        inputSet = Arrays.asList(new Interval(17, 17), new Interval(1, 15), new Interval(-2, -1));
        testLabel = "Set 6 - No overlap";
        expected = Arrays.asList(new Interval(-2, -1), new Interval(1, 15), new Interval(17, 17));
        testCases.add(new TestDef(inputSet, testLabel, expected, 0));

        inputSet = Arrays.asList(new Interval(1, 4), new Interval(2, 3));
        testLabel = "Set 7 - Leet Code overlap failure";
        expected = Arrays.asList(new Interval(1, 4));
        testCases.add(new TestDef(inputSet, testLabel, expected, 1));

        inputSet = Arrays.asList(new Interval(3, 4), new Interval(2, 5));
        testLabel = "Set 8 - Leet Code overlap failure";
        expected = Arrays.asList(new Interval(2, 5));
        testCases.add(new TestDef(inputSet, testLabel, expected, 1));
    }

    /**
     * Prevent construction
     */
    private TestPerfUtil() {
    }

    private static List<Interval> generateTestCases(final int seed, final int n, final int min, final int max)
    {
        final List<Interval> testCases = new ArrayList<Interval>(n);
        final Random random = new Random(seed);
        for (int i = 0; i < n; i++)
        {
            final int v1 = random.nextInt((max - min) + 1) + min;
            final int v2 = random.nextInt((max - min) + 1) + min;
            final Interval v = new Interval(Math.min(v1, v2), Math.max(v1, v2));
            testCases.add(v);
        }
        return testCases;
    }

    /**
     * Repeat execution to average out variation
     */
    private static long timeRepeatedMerger(final int repetition, final List<Interval> testCases, final Overlap ovr,
            final Comparator<Interval> comparator)
    {
        final long startNano = System.nanoTime();
        for (int i = 0; i < repetition; i++)
        {
            ovr.merge(testCases, comparator);
        }

        return System.nanoTime() - startNano;
    }

    /**
     * Return median execution time
     */
    private static long smoothRepeatedMergerTime(final int repetition, final List<Interval> testCases,
            final Overlap ovr, final Comparator<Interval> comparator, final int sampleSize)
    {
        final List<Long> times = new ArrayList<Long>(sampleSize);
        for (int i = 0; i < sampleSize; i++)
        {
            times.add(timeRepeatedMerger(repetition, testCases, ovr, comparator));
        }
        Collections.sort(times);
        return times.get(sampleSize / 2);
    }

    private static List<Interval> generateTestData(final int seed, final int length)
    {
        return TestPerfUtil.generateTestCases(seed, length, TestPerfUtil.INTERVAL_LBOUND, TestPerfUtil.INTERVAL_UBOUND);
    }

    /**
     * Returns precreated test data - do not mung!
     */
    public static List<TestDef> getFunctionalTestData()
    {
        return testCases;
    }

    /**
     * Test increasing number of intervals
     */
    public static List<long[]> runTestSequence(final boolean display, final String title, final int repetition,
            final Overlap ovr, final Comparator<Interval> comparator, final int initial, final int step,
            final int testCnt)
    {
        if (display)
        {
            System.out.println(String.format("\n%s from %d by step %d to %d with %d repeats per interval count", title,
                    initial, step, initial + (testCnt - 1) * step, repetition));
            System.out.println("case,n_intervals,n_exec,t_nano,u_nano,algo");
        }
        final List<long[]> results = new ArrayList<long[]>(testCnt);
        for (int test = 1; test <= testCnt; test++)
        {
            final List<Interval> testCases = TestPerfUtil.generateTestData(TestPerfUtil.INITIAL_SEED,
                    initial + (test - 1) * step);
            final int intervalCount = testCases.size();
            if (display)
            {
                System.out.print(String.format("%d,%d,", test, intervalCount));
            }
            final long elapsed = TestPerfUtil.smoothRepeatedMergerTime(repetition, testCases, ovr, comparator,
                    TestPerfUtil.SMOOTHING_SAMPLE_SIZE);
            if (display)
            {
                final int totalExecCnt = repetition * intervalCount;
                final double perExec = (double) elapsed / (double) totalExecCnt;
                System.out.println(String.format("%d,%d,%.1f,%s", totalExecCnt, elapsed, perExec, title));
            }
            results.add(new long[]
            { intervalCount, elapsed });
        }

        return results;
    }

    /**
     * run a test sequence repeatedly and test while timing
     */
    public static final List<long[]> timeAndTest(final boolean display, final String label, final Overlap ovr,
            final int repetition, final int n_tests, final int limit, int initialTrials, int stepTrails)
    {
        // System.gc();
        final List<long[]> testResult = TestPerfUtil.runTestSequence(display, label, repetition, ovr, null,
                initialTrials, stepTrails, n_tests);

        int reversed = 0;
        long last = Integer.MIN_VALUE;
        for (int i = 0; i < n_tests; i++)
        {
            final long l2r = testResult.get(i)[1];
            if (last > l2r)
                reversed++;
            last = l2r;
        }
        Assert.assertTrue("too many " + label + " reversals, expected less than " + limit + " but got " + reversed,
                reversed <= limit);

        return testResult;
    }
}
