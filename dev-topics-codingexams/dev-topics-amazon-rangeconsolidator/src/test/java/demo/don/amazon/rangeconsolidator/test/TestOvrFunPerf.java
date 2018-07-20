package demo.don.amazon.rangeconsolidator.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.amazon.rangeconsolidator.Overlap;
import demo.don.amazon.rangeconsolidator.Overlap.Interval;
import demo.don.amazon.rangeconsolidator.OverlapL2R;
import demo.don.amazon.rangeconsolidator.OverlapR2L;

public class TestOvrFunPerf
{
    // Provides Repeatability for the test
    private static final int INITIAL_SEED = 7187;

    // rerun an individual test-group and compute a central-tendency statistic
    // for execution time
    private static final int SMOOTHING_SAMPLE_SIZE = 3;

    // test execution times are too variable in run one, so run several times to
    // reduce variability, creating a test group
    private static final int TEST_GROUP_REPETITION_FACTOR = 12;

    // creates ITERATIONS test batches with INITIAL test groups in the first one
    // and incrementing by STEP test groups each iteration
    private static final int TEST_COUNT = 12;
    private static final int INITIAL_TRIALS = 500;
    private static final int STEP_TRIALS = 1000;

    // each interval to collapse fits in this range (just for testing limits)
    private static final int INTERVAL_LBOUND = 0;
    private static final int INTERVAL_UBOUND = 10000;

    // Dump timings for analysis
    private static final boolean display = true;

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
        ovr.merge(testCases.subList(0, Math.min(testCases.size(), SMOOTHING_SAMPLE_SIZE))); // for
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

    /**
     * Test increasing number of intervals
     */
    private static List<long[]> runTestSequence(final boolean display, final String title, final int repetition,
            final Overlap ovr, final int initial, final int step, final int testCnt)
    {
        final List<long[]> results = new ArrayList<long[]>(testCnt);
        int intervalCount = initial;
        if (display)
        {
            System.out.println(String.format("\n%s from %d by step %d to %d with %d repeats per interval count", title,
                    initial, step, intervalCount + (testCnt - 1) * step, repetition));
        }
        final List<Interval> testCases = generateTestCases(INITIAL_SEED, initial, INTERVAL_LBOUND, INTERVAL_UBOUND);
        smoothRepeatedMergerTime(repetition, testCases, ovr, SMOOTHING_SAMPLE_SIZE); // for
                                                                                     // JIT
        for (int test = 1; test <= testCnt; test++)
        {
            intervalCount = testCases.size();
            if (display)
            {
                System.out.print(String.format("%3d.  %5d intervals", test, intervalCount));
            }
            final long elapsed = smoothRepeatedMergerTime(repetition, testCases, ovr, SMOOTHING_SAMPLE_SIZE);
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
            testCases.addAll(generateTestCases(INITIAL_SEED + 31 * test, step, INTERVAL_LBOUND, INTERVAL_UBOUND));
        }

        return results;
    }

    @Before
    public void setUp() throws Exception
    {
        return;
    }

    @After
    public void tearDown() throws Exception
    {
        return;
    }

    @Test
    public void testPerfL2R()
    {
        System.gc();
        final int repetition = TEST_GROUP_REPETITION_FACTOR;
        final int n_tests = TEST_COUNT;
        final List<long[]> testResult = runTestSequence(display, "Left-to-Right", repetition, new OverlapL2R(),
                INITIAL_TRIALS, STEP_TRIALS, n_tests);
        long last = Long.MIN_VALUE;
        for (int i = 0; i < n_tests; i++)
        {
            final long current = testResult.get(i)[1];
            Assert.assertTrue("non-monotic at " + i + ", last: " + last + "; current: " + current + "; count: "
                    + testResult.get(i)[0], last < current);
            last = current;
        }
    }

    @Test
    public void testPerfR2L()
    {
        System.gc();
        final int repetition = TEST_GROUP_REPETITION_FACTOR + 3;
        final int n_tests = TEST_COUNT;
        final List<long[]> testResult = runTestSequence(display, "Right-to-left", repetition, new OverlapR2L(),
                INITIAL_TRIALS, STEP_TRIALS, n_tests);
        long last = Long.MIN_VALUE;
        for (int i = 0; i < n_tests; i++)
        {
            final long current = testResult.get(i)[1];
            Assert.assertTrue("non-monotic at " + i + ", last: " + last + "; current: " + current + "; count: "
                    + testResult.get(i)[0], last < current);
            last = current;
        }
    }
}
