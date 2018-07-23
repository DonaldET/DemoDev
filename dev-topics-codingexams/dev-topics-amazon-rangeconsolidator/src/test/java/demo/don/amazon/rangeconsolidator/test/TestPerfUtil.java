package demo.don.amazon.rangeconsolidator.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
    static final int TEST_GROUP_REPETITION_FACTOR = 21;

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
    private static long timeRepeatedMerger(final int repetition, final List<Interval> testCases, final Overlap ovr)
    {
        final long startNano = System.nanoTime();
        for (int i = 0; i < repetition; i++)
        {
            ovr.merge(testCases);
        }

        return System.nanoTime() - startNano;
    }

    /**
     * Return median execution time
     */
    private static long smoothRepeatedMergerTime(final int repetition, final List<Interval> testCases,
            final Overlap ovr, final int sampleSize)
    {
        final List<Long> times = new ArrayList<Long>(sampleSize);
        for (int i = 0; i < sampleSize; i++)
        {
            times.add(timeRepeatedMerger(repetition, testCases, ovr));
        }
        Collections.sort(times);
        return times.get(sampleSize / 2);
    }

    private static List<Interval> generateTestData(final int seed, final int length)
    {
        return TestPerfUtil.generateTestCases(seed, length, TestPerfUtil.INTERVAL_LBOUND, TestPerfUtil.INTERVAL_UBOUND);
    }

    /**
     * Test increasing number of intervals
     */
    public static List<long[]> runTestSequence(final boolean display, final String title, final int repetition,
            final Overlap ovr, final int initial, final int step, final int testCnt)
    {
        if (display)
        {
            System.out.println(String.format("\n%s from %d by step %d to %d with %d repeats per interval count", title,
                    initial, step, initial + (testCnt - 1) * step, repetition));
        }
        final List<long[]> results = new ArrayList<long[]>(testCnt);
        for (int test = 1; test <= testCnt; test++)
        {
            final List<Interval> testCases = TestPerfUtil.generateTestData(TestPerfUtil.INITIAL_SEED,
                    initial + (test - 1) * step);
            final int intervalCount = testCases.size();
            if (display)
            {
                System.out.print(String.format("%3d.  %5d intervals", test, intervalCount));
            }
            final long elapsed = TestPerfUtil.smoothRepeatedMergerTime(repetition, testCases, ovr,
                    TestPerfUtil.SMOOTHING_SAMPLE_SIZE);
            if (display)
            {
                final int totalExecCnt = repetition * intervalCount;
                final double perExec = (double) elapsed / (double) totalExecCnt;
                System.out.println(String.format(" => %6d executions --  %8d, or %.1f NS/execution", totalExecCnt,
                        elapsed, perExec));
            }
            results.add(new long[]
            { intervalCount, elapsed });
        }

        return results;
    }
}
