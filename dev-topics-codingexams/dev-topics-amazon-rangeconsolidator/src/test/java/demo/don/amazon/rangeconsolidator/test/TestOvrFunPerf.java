package demo.don.amazon.rangeconsolidator.test;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.amazon.rangeconsolidator.OverlapL2R;
import demo.don.amazon.rangeconsolidator.OverlapR2L;

public class TestOvrFunPerf
{
    // creates ITERATIONS test batches with INITIAL test groups in the first one
    // and incrementing by STEP test groups each iteration
    private static final int TEST_COUNT = 12;
    private static final int INITIAL_TRIALS = 500;
    private static final int STEP_TRIALS = 1000;

    // Dump timings for analysis
    private static final boolean display = false;

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
        final int repetition = TestPerfUtil.TEST_GROUP_REPETITION_FACTOR;
        final int n_tests = TEST_COUNT;
        final List<long[]> testResult = TestPerfUtil.runTestSequence(display, "Left-to-Right", repetition,
                new OverlapL2R(), INITIAL_TRIALS, STEP_TRIALS, n_tests);
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
        final int repetition = TestPerfUtil.TEST_GROUP_REPETITION_FACTOR + 3;
        final int n_tests = TEST_COUNT;
        final List<long[]> testResult = TestPerfUtil.runTestSequence(display, "Right-to-left", repetition,
                new OverlapR2L(), INITIAL_TRIALS, STEP_TRIALS, n_tests);
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
