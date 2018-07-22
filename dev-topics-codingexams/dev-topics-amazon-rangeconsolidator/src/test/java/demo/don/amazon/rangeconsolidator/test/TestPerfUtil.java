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
    static final int TEST_GROUP_REPETITION_FACTOR = 15;

    /**
     * Prevent construction
     */
    private TestPerfUtil() {
    }

    public static List<Interval> generateTestCases(final int seed, final int n, final int min, final int max)
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
    public static long timeRepeatedMerger(final int repetition, final List<Interval> testCases, final Overlap ovr)
    {
        ovr.merge(testCases.subList(0, Math.min(testCases.size(), TestPerfUtil.SMOOTHING_SAMPLE_SIZE))); // for
                                                                                                         // JIT
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
    public static long smoothRepeatedMergerTime(final int repetition, final List<Interval> testCases, final Overlap ovr,
            final int sampleSize)
    {
        final List<Long> times = new ArrayList<Long>(sampleSize);
        for (int i = 0; i < sampleSize; i++)
        {
            times.add(timeRepeatedMerger(repetition, testCases, ovr));
        }
        Collections.sort(times);
        return times.get(sampleSize / 2);
    }

    /**
     * Test increasing number of intervals
     */
    public static List<long[]> runTestSequence(final boolean display, final String title, final int repetition,
            final Overlap ovr, final int initial, final int step, final int testCnt)
    {
        final List<long[]> results = new ArrayList<long[]>(testCnt);
        int intervalCount = initial;
        if (display)
        {
            System.out.println(String.format("\n%s from %d by step %d to %d with %d repeats per interval count", title,
                    initial, step, intervalCount + (testCnt - 1) * step, repetition));
        }
        final List<Interval> testCases = TestPerfUtil.generateTestCases(TestPerfUtil.INITIAL_SEED, initial,
                TestPerfUtil.INTERVAL_LBOUND, TestPerfUtil.INTERVAL_UBOUND);
        TestPerfUtil.smoothRepeatedMergerTime(repetition, testCases, ovr, TestPerfUtil.SMOOTHING_SAMPLE_SIZE); // for
                                                                                                               // JIT
        for (int test = 1; test <= testCnt; test++)
        {
            intervalCount = testCases.size();
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

            intervalCount += step;
            testCases.addAll(TestPerfUtil.generateTestCases(TestPerfUtil.INITIAL_SEED + 31 * test, step,
                    TestPerfUtil.INTERVAL_LBOUND, TestPerfUtil.INTERVAL_UBOUND));
        }

        return results;
    }
}
