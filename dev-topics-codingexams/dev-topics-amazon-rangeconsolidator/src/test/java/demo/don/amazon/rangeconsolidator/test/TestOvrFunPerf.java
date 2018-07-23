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
    public void testPerfLftOrRgt()
    {
        final int repetition = TestPerfUtil.TEST_GROUP_REPETITION_FACTOR;
        final int n_tests = TEST_COUNT;

        System.gc();
        final List<long[]> testResultL2R = TestPerfUtil.runTestSequence(display, "Left-to-Right", repetition,
                new OverlapL2R(), INITIAL_TRIALS, STEP_TRIALS, n_tests);
        System.gc();
        final List<long[]> testResultR2L = TestPerfUtil.runTestSequence(display, "Right-to-left", repetition,
                new OverlapR2L(), INITIAL_TRIALS, STEP_TRIALS, n_tests);

        int reversed = 0;
        long last = Integer.MIN_VALUE;
        for (int i = 0; i < n_tests; i++)
        {
            final long l2r = testResultL2R.get(i)[1];
            final long r2l = testResultR2L.get(i)[1];
            Assert.assertTrue("incorrect orger at " + i + ", LRS: " + l2r + ";  R2L: " + r2l + "; count: "
                    + testResultL2R.get(i)[0] + " and " + testResultR2L.get(i)[0], l2r > r2l);
            if (last > l2r)
                reversed++;
            last = l2r;
        }
        Assert.assertTrue("too many reversals", reversed < 2);
    }
}
