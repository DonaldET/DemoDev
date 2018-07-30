package demo.don.amazon.rangeconsolidator.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.amazon.rangeconsolidator.AbstractOverlap;
import demo.don.amazon.rangeconsolidator.Overlap;
import demo.don.amazon.rangeconsolidator.Overlap.Interval;
import demo.don.amazon.rangeconsolidator.Overlap.Merger;
import demo.don.amazon.rangeconsolidator.OverlapL2R;
import demo.don.amazon.rangeconsolidator.OverlapLeetCode;
import demo.don.amazon.rangeconsolidator.OverlapR2L;
import demo.don.amazon.rangeconsolidator.OverlapSort;

public class TestOvrFun
{
    private static List<TestDef> testCases = TestPerfUtil.getFunctionalTestData();

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testTestCases()
    {
        System.err.println("\nTest Data");
        for (final TestDef testDef : testCases)
        {
            System.err.println(testDef.inputSet);
        }
        Assert.assertTrue("no tests", testCases.size() > 0);
    }

    @Test
    public void testFunL2R()
    {
        final TestOvrRunner runner = new TestOvrRunner(new OverlapL2R(), "left-to-right");
        int i = 0;
        for (final TestDef testDef : testCases)
        {
            i++;
            runner.applyTest(" " + i + ". " + testDef.testLabel, testDef.inputSet, testDef.expected, testDef.expMerges,
                    null);
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
            runner.applyTest(" " + i + ". " + testDef.testLabel, testDef.inputSet, testDef.expected, testDef.expMerges,
                    null);
        }
    }

    @Test
    public void testFunSort()
    {
        final Overlap sortOvr = new OverlapSort();

        final TestDef td = testCases.get(5);
        final List<Interval> inputSet = td.inputSet;
        final Merger mergeOut = sortOvr.merge(inputSet, null);
        Assert.assertEquals("wrong merge count", 0, mergeOut.merges);

        final List<Interval> actual = mergeOut.merged;
        Assert.assertEquals("length wrong", inputSet.size(), actual.size());

        final int n = inputSet.size();
        final List<Interval> expSort = new ArrayList<Interval>(n);
        expSort.addAll(inputSet);
        Collections.sort(expSort, new AbstractOverlap.MergeComparator());

        final String rangeTestTitle = "SORTED RANGE";
        for (int i = 0; i < n; i++)
        {
            final Interval expInterval = expSort.get(i);
            Assert.assertNotNull(i + ". EXP NULL - " + rangeTestTitle, expInterval);
            final Interval actInterval = mergeOut.merged.get(i);
            Assert.assertNotNull(i + ". ACT NULL - " + rangeTestTitle, actInterval);
            Assert.assertTrue(i + ". " + rangeTestTitle + " => EXP: " + expInterval + "; ACT: " + actInterval,
                    expInterval.equals(actInterval));
        }
    }

    @Test
    public void testFunLeetCode()
    {
        final TestOvrRunner runner = new TestOvrRunner(new OverlapLeetCode(), "LeetCode");
        int i = 0;
        for (final TestDef testDef : testCases)
        {
            i++;
            runner.applyTest(" " + i + ". " + testDef.testLabel, testDef.inputSet, testDef.expected, testDef.expMerges,
                    null);
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
            final Integer expMerges, final Comparator<Interval> comparator)
    {
        final Merger mergeOut = ovr.merge(inputSet, comparator);
        if (expMerges != null)
        {
            Assert.assertEquals(
                    "testing merges for " + label + ": " + title + " using " + ovr.getClass().getSimpleName(),
                    expMerges.intValue(), mergeOut.merges);
        }

        final String rangeTestTitle = "testing " + label + ": " + title + " using " + ovr.getClass().getSimpleName();
        final int n = mergeOut.merged.size();
        Assert.assertEquals(rangeTestTitle + " :: Merged interval count differs", expected.size(), n);

        for (int i = 0; i < n; i++)
        {
            final Interval expInterval = expected.get(i);
            Assert.assertNotNull(i + ". EXP NULL - " + rangeTestTitle, expInterval);
            final Interval actInterval = mergeOut.merged.get(i);
            Assert.assertNotNull(i + ". ACT NULL - " + rangeTestTitle, actInterval);
            Assert.assertTrue(i + ". " + rangeTestTitle + " => EXP: " + expInterval + ", ACT: " + actInterval,
                    expInterval.equals(actInterval));
        }
    }
}
