package demo.don.amazon.rangeconsolidator.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.amazon.rangeconsolidator.Overlap;
import demo.don.amazon.rangeconsolidator.Overlap.Interval;
import demo.don.amazon.rangeconsolidator.Overlap.Merger;
import demo.don.amazon.rangeconsolidator.OverlapL2R;
import demo.don.amazon.rangeconsolidator.OverlapR2L;

public class TestOvrFun
{
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

        inputSet = Arrays.asList();
        testLabel = "Set 2 - Trivial";
        expected = new ArrayList<Interval>();
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
    public void testFunL2R()
    {
        final TestOvrRunner runner = new TestOvrRunner(new OverlapL2R(), "left-to-right");
        int i = 0;
        for (final TestDef testDef : testCases)
        {
            i++;
            runner.applyTest(" " + i + ". " + testDef.testLabel, testDef.inputSet, testDef.expected, testDef.expMerges);
        }
    }

    @Test
    public void testFunR2L()
    {
        final TestOvrRunner runner = new TestOvrRunner(new OverlapR2L(), "right-to-left");
        int i = 0;
        for (final TestDef testDef : testCases)
        {
            i++;
            runner.applyTest(" " + i + ". " + testDef.testLabel, testDef.inputSet, testDef.expected, testDef.expMerges);
        }
    }
}

class TestOvrRunner
{
    private final Overlap ovr;
    private final String label;

    public TestOvrRunner(final Overlap ovr, final String label) {
        super();
        Assert.assertNotNull("ovr null", ovr);
        Assert.assertNotNull("label null", label);
        this.ovr = ovr;
        this.label = label;
    }

    public void applyTest(final String title, final List<Interval> inputSet, final List<Interval> expected,
            final int expMerges)
    {
        final Merger mergeOut = ovr.merge(inputSet);
        Assert.assertEquals("testing merges for " + label + ": " + title + " using " + ovr.getClass().getSimpleName(),
                expMerges, mergeOut.merges);
        final String rangeTestTitle = "testing " + label + ": " + title + " using " + ovr.getClass().getSimpleName();
        final int n = mergeOut.merged.size();
        Assert.assertEquals(rangeTestTitle + " :: N differs", expected.size(), n);
        for (int i = 0; i < n; i++)
        {
            final Interval expInterval = expected.get(i);
            Assert.assertNotNull(i + ". EXP NULL - " + rangeTestTitle, expInterval);
            final Interval actInterval = mergeOut.merged.get(i);
            Assert.assertNotNull(i + ". ACT NULL - " + rangeTestTitle, actInterval);
            Assert.assertTrue(i + ". " + rangeTestTitle, expInterval.equals(actInterval));
        }
    }
}
