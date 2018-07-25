package demo.don.amazon.rangeconsolidator.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.Solution;
import demo.don.amazon.rangeconsolidator.Overlap;

public class TestLeetCodeFun
{
    private static final List<TestDef> testCases = new ArrayList<TestDef>();

    static
    {
        List<Overlap.Interval> inputSet = Arrays.asList(new Overlap.Interval(8, 10), new Overlap.Interval(1, 4),
                new Overlap.Interval(3, 6), new Overlap.Interval(15, 18));
        String testLabel = "Set 0 - Problem statement";
        List<Overlap.Interval> expected = Arrays.asList(new Overlap.Interval(1, 6), new Overlap.Interval(8, 10),
                new Overlap.Interval(15, 18));
        testCases.add(new TestDef(inputSet, testLabel, expected, 1));

        inputSet = Arrays.asList();
        testLabel = "Set 1 - Empty";
        expected = Arrays.asList();
        testCases.add(new TestDef(inputSet, testLabel, expected, 0));

        inputSet = Arrays.asList();
        testLabel = "Set 2 - Trivial";
        expected = new ArrayList<Overlap.Interval>();
        testCases.add(new TestDef(inputSet, testLabel, expected, 0));

        inputSet = Arrays.asList(new Overlap.Interval(1, 10), new Overlap.Interval(1, 10), new Overlap.Interval(1, 10));
        testLabel = "Set 3 - All Same";
        expected = Arrays.asList(new Overlap.Interval(1, 10));
        testCases.add(new TestDef(inputSet, testLabel, expected, 2));

        inputSet = Arrays.asList(new Overlap.Interval(1, 10), new Overlap.Interval(1, 12), new Overlap.Interval(1, 15),
                new Overlap.Interval(-2, -1));
        testLabel = "Set 4 - Inclusive";
        expected = Arrays.asList(new Overlap.Interval(-2, -1), new Overlap.Interval(1, 15));
        testCases.add(new TestDef(inputSet, testLabel, expected, 2));

        inputSet = Arrays.asList(new Overlap.Interval(1, 10), new Overlap.Interval(10, 12),
                new Overlap.Interval(12, 15), new Overlap.Interval(-2, -1));
        testLabel = "Set 5 - Adjacent";
        expected = Arrays.asList(new Overlap.Interval(-2, -1), new Overlap.Interval(1, 15));
        testCases.add(new TestDef(inputSet, testLabel, expected, 2));

        inputSet = Arrays.asList(new Overlap.Interval(17, 17), new Overlap.Interval(1, 15),
                new Overlap.Interval(-2, -1));
        testLabel = "Set 6 - No overlap";
        expected = Arrays.asList(new Overlap.Interval(-2, -1), new Overlap.Interval(1, 15),
                new Overlap.Interval(17, 17));
        testCases.add(new TestDef(inputSet, testLabel, expected, 0));
    }

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testLeetCodeFunR2L()
    {
        final TestLeetCodeRunner runner = new TestLeetCodeRunner(new Solution(), "LC::right-to-left");
        int i = 0;
        for (final TestDef testDef : testCases)
        {
            i++;
            runner.applyTest(" " + i + ". " + testDef.testLabel, testDef.inputSet, testDef.expected);
        }
    }
}

class TestLeetCodeRunner
{
    private final Solution lcSolution;
    private final String label;

    public TestLeetCodeRunner(final Solution lcSolution, final String label) {
        super();
        Assert.assertNotNull("lcSolution null", lcSolution);
        Assert.assertNotNull("label null", label);
        this.lcSolution = lcSolution;
        this.label = label;
    }

    public void applyTest(final String title, final List<Overlap.Interval> inputSet,
            final List<Overlap.Interval> expected)
    {
        final List<Solution.Interval> mergeOut = lcSolution.merge(Ovr2LC(inputSet));
        final String rangeTestTitle = "testing " + label + ": " + title + " using "
                + lcSolution.getClass().getSimpleName();
        final int n = mergeOut.size();
        Assert.assertEquals(rangeTestTitle + " :: N differs", expected.size(), n);
        for (int i = 0; i < n; i++)
        {
            final Overlap.Interval expInterval = expected.get(i);
            Assert.assertNotNull(i + ". EXP NULL - " + rangeTestTitle, expInterval);
            final Solution.Interval actInterval = mergeOut.get(i);
            Assert.assertNotNull(i + ". ACT NULL - " + rangeTestTitle, actInterval);
            Assert.assertTrue("Differs[" + i + "]. " + rangeTestTitle, expInterval.equals(LC2Ovr(actInterval)));
        }
    }

    private List<Solution.Interval> Ovr2LC(final List<Overlap.Interval> ovrInt)
    {
        final int n = ovrInt.size();
        final List<Solution.Interval> solInt = new ArrayList<Solution.Interval>(n);
        for (final Overlap.Interval ovr : ovrInt)
            solInt.add(new Solution.Interval(ovr.low, ovr.hi));
        return solInt;
    }

    private Overlap.Interval LC2Ovr(final Solution.Interval lcInt)
    {
        return new Overlap.Interval(lcInt.start, lcInt.end);
    }
}
