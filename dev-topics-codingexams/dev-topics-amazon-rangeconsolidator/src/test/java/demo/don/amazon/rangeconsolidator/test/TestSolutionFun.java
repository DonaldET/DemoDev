package demo.don.amazon.rangeconsolidator.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.Solution;
import demo.don.amazon.rangeconsolidator.Overlap;

public class TestSolutionFun {
	private static List<TestDef> testCases = TestPerfUtil.getFunctionalTestData();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSolutionFun() {
		final TestSolutionRunner runner = new TestSolutionRunner(new Solution(), "LC_format::right-to-left");
		int i = 0;
		for (final TestDef testDef : testCases) {
			i++;
			runner.applyTest(" " + i + ". " + testDef.testLabel, testDef.inputSet, testDef.expected);
		}
	}
}

class TestSolutionRunner {
	private final Solution lcSolution;
	private final String label;

	public TestSolutionRunner(final Solution lcSolution, final String label) {
		super();
		Assert.assertNotNull("lcSolution null", lcSolution);
		Assert.assertNotNull("label null", label);
		this.lcSolution = lcSolution;
		this.label = label;
	}

	public void applyTest(final String title, final List<Overlap.Interval> inputSet,
			final List<Overlap.Interval> expected) {
		final List<Solution.Interval> mergeOut = lcSolution.merge(Ovr2LC(inputSet));
		final String rangeTestTitle = "testing " + label + ": " + title + " using "
				+ lcSolution.getClass().getSimpleName();
		final int n = mergeOut.size();
		Assert.assertEquals(rangeTestTitle + " :: N differs", expected.size(), n);
		for (int i = 0; i < n; i++) {
			final Overlap.Interval expInterval = expected.get(i);
			Assert.assertNotNull(i + ". EXP NULL - " + rangeTestTitle, expInterval);
			final Solution.Interval actInterval = mergeOut.get(i);
			Assert.assertNotNull(i + ". ACT NULL - " + rangeTestTitle, actInterval);
			Assert.assertTrue(i + ". " + rangeTestTitle + " => EXP: " + expInterval + ", ACT: " + actInterval,
					expInterval.equals(LC2Ovr(actInterval)));
		}
	}

	private List<Solution.Interval> Ovr2LC(final List<Overlap.Interval> ovrInt) {
		final int n = ovrInt.size();
		final List<Solution.Interval> solInt = new ArrayList<Solution.Interval>(n);
		for (final Overlap.Interval ovr : ovrInt)
			solInt.add(new Solution.Interval(ovr.start, ovr.end));
		return solInt;
	}

	private Overlap.Interval LC2Ovr(final Solution.Interval lcInt) {
		return new Overlap.Interval(lcInt.start, lcInt.end);
	}
}
