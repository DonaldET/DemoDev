package demo.don.amazon.rangeconsolidator.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import demo.don.amazon.rangeconsolidator.OverlapLeetCode;

public class TestOvrFunPerf10xLC {
	// creates ITERATIONS test batches with INITIAL test groups in the first one
	// and incrementing by STEP test groups each iteration
	private static final int TEST_COUNT = 12;
	private static final int INITIAL_TRIALS = 5000;
	private static final int STEP_TRIALS = 10000;

	// Dump timings for analysis
	private static final boolean display = false;

	@Before
	public void setUp() throws Exception {
		return;
	}

	@After
	public void tearDown() throws Exception {
		return;
	}

	@Test
	@Ignore		// performance testing
	public void testPerfR2Lx10() {
		final int repetition = TestPerfUtil.TEST_GROUP_REPETITION_FACTOR / 2;
		final int n_tests = TEST_COUNT;

		TestPerfUtil.timeAndTest(display, "Leet-Code", new OverlapLeetCode(), repetition, n_tests, 2, INITIAL_TRIALS,
				STEP_TRIALS);
	}
}
