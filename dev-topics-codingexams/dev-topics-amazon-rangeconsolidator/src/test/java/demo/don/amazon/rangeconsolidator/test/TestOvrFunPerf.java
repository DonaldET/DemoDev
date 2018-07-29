package demo.don.amazon.rangeconsolidator.test;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import demo.don.amazon.rangeconsolidator.OverlapL2R;
import demo.don.amazon.rangeconsolidator.OverlapLeetCode;
import demo.don.amazon.rangeconsolidator.OverlapR2L;
import demo.don.amazon.rangeconsolidator.OverlapSort;

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
    public void testPerfAll()
    {
        final int repetition = TestPerfUtil.TEST_GROUP_REPETITION_FACTOR;
        final int n_tests = TEST_COUNT;

        // ---------------------------

        final List<long[]> testResultLC = TestPerfUtil.timeAndTest(display, "Leet-Code", new OverlapLeetCode(),
                repetition, n_tests, 2, INITIAL_TRIALS, STEP_TRIALS);
        final List<long[]> testResultR2L = TestPerfUtil.timeAndTest(display, "Right-to-left", new OverlapR2L(),
                repetition, n_tests, 2, INITIAL_TRIALS, STEP_TRIALS);
        TestPerfUtil.timeAndTest(display, "Left-to-right", new OverlapL2R(), repetition, n_tests, 2, INITIAL_TRIALS,
                STEP_TRIALS);
        TestPerfUtil.timeAndTest(display, "Sort", new OverlapSort(), repetition, n_tests, 2, INITIAL_TRIALS,
                STEP_TRIALS);

        // ---------------------------

        if (display)
        {
            int lcSlow = 0;
            int r2lSlow = 0;
            for (int i = 0; i < n_tests; i++)
            {
                final long lc = testResultLC.get(i)[1];
                final long r2l = testResultR2L.get(i)[1];
                final double delta = (double) (lc - r2l) / (double) Math.max(lc, r2l);
                final double equalityInterval = 0.05;
                if (Math.abs(delta) > equalityInterval)
                {
                    if (delta > 0.0)
                    {
                        lcSlow++;
                    }
                    else
                        r2lSlow++;
                    System.err.println("Unequal speed at " + i + ".  lcSLow: " + lcSlow + "  r2lSlow: " + r2lSlow
                            + "; delta: " + delta);
                }
            }
        }
    }
}
