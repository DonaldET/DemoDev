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
    private static final boolean display = false;

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
    public void testRegressionCases()
    {
        if (display)
        {
            System.err.println("\nIndividual Regression Test Data");
            for (final TestDef testDef : testCases)
            {
                System.err.println(testDef.inputSet);
            }
        }

        Assert.assertTrue("no tests", testCases.size() > 0);

        for (final TestDef testDef : testCases)
        {
            int i = 0;
            for (final Interval intr : testDef.inputSet)
            {
                Assert.assertNotNull(testDef.testLabel + "[" + i + "] - null", intr);
                Assert.assertTrue(testDef.testLabel + "[" + i + "] - start small: " + intr.start, intr.start >= -2);
                Assert.assertTrue("testDef.testLabel + \"[\" + i + \"] - end big: " + intr.end, intr.end <= 9573);
                Assert.assertTrue(
                        "testDef.testLabel + \"[\" + i + \"] - out of order: [" + intr.start + "," + intr.end + "]",
                        intr.start <= intr.end);
            }
            i += 1;
        }
    }

    @Test
    public void testRandomGeneration()
    {
        final int randomTestCaseCount = 3;
        final List<Interval> randomTestCases = TestPerfUtil.generateTestCases(TestPerfUtil.INITIAL_SEED,
                randomTestCaseCount, TestPerfUtil.INTERVAL_LBOUND, TestPerfUtil.INTERVAL_UBOUND);
        Assert.assertEquals("random test data size wrong", randomTestCaseCount, randomTestCases.size());
        for (final Interval intr : randomTestCases)
        {
            Assert.assertTrue("start small", intr.start >= TestPerfUtil.INTERVAL_LBOUND);
            Assert.assertTrue("end big", intr.end <= TestPerfUtil.INTERVAL_UBOUND);
            Assert.assertTrue("out of order", intr.start <= intr.end);
        }

        final List<Interval> ordered = new ArrayList<Interval>(randomTestCases);
        ordered.addAll(randomTestCases);
        Collections.sort(ordered, new AbstractOverlap.MergeComparator());
        Assert.assertFalse("unexpectedly equal", randomTestCases.equals(ordered));

        if (display)
        {
            System.err.println("\nSample Unordered random test data: " + randomTestCases.subList(0, 20));
            System.err.println("Sample ordered random test data  : " + ordered.subList(0, 20));
        }
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

    @Test
    public void testCompareLeetCodeAndR2L()
    {
        final int randomTestCaseCount = 5;
        final List<Interval> randomTestCases = TestPerfUtil.generateTestCases(TestPerfUtil.INITIAL_SEED,
                randomTestCaseCount, TestPerfUtil.INTERVAL_LBOUND, TestPerfUtil.INTERVAL_UBOUND);
        System.err.println("\n------\nLC-R2L Input     : " + randomTestCases);

        final OverlapLeetCode overlapLeetCode = new OverlapLeetCode();
        List<Interval> sortedIntervals = overlapLeetCode.sortIntervals(randomTestCases,
                new OverlapLeetCode.IntervalComparator());
        System.err.println("LC Ordered       : " + sortedIntervals);

        final OverlapR2L overlapR2L = new OverlapR2L();
        sortedIntervals = overlapR2L.sortIntervals(randomTestCases, new OverlapR2L.MergeComparator());
        System.err.println("R2L Ordered      : " + sortedIntervals);

        final Merger lcMerge = overlapLeetCode.merge(randomTestCases, null);
        final Merger r2lMerge = overlapR2L.merge(randomTestCases, null);
        String label = "LeetCode vs Right-to-left";
        final String rangeTestTitle = "testing " + label;

        final int n_result_lc = lcMerge.merged.size();
        final int n_result_r2l = r2lMerge.merged.size();

        System.err.println("\nLC-R2L Input2    : " + randomTestCases);
        System.err.println("LC  merged       : " + lcMerge.merged);
        System.err.println("R2L merged       : " + r2lMerge.merged);

        final int minMatchingSize = Math.min(n_result_lc, n_result_r2l);
        for (int i = 0; i < minMatchingSize; i++)
        {
            final Interval lcInterval = lcMerge.merged.get(i);
            Assert.assertNotNull(i + ". LC NULL - " + rangeTestTitle, lcInterval);
            final Interval r2lInterval = r2lMerge.merged.get(i);
            Assert.assertNotNull(i + ". R2L NULL - " + rangeTestTitle, r2lInterval);
            Assert.assertTrue(
                    i + ". " + rangeTestTitle + " merged result differs => LC: " + lcInterval + ", R2L: " + r2lInterval,
                    lcInterval.equals(r2lInterval));
        }

        Assert.assertEquals(rangeTestTitle + " :: Resulting merged LC and R2L count differs", n_result_lc,
                n_result_r2l);

        final int n_mrg_lc = lcMerge.merges;
        final int n_mrg_r2l = r2lMerge.merges;
        Assert.assertEquals(rangeTestTitle + " :: Merged LC and R2L intervals count differs", n_mrg_lc, n_mrg_r2l);
    }

    @Test
    public void testCompareLeetCodeAndL2R()
    {
        final int randomTestCaseCount = 5;
        final List<Interval> randomTestCases = TestPerfUtil.generateTestCases(TestPerfUtil.INITIAL_SEED,
                randomTestCaseCount, TestPerfUtil.INTERVAL_LBOUND, TestPerfUtil.INTERVAL_UBOUND);
        System.err.println("\n------\nLC-L2R Input     : " + randomTestCases);

        final OverlapLeetCode overlapLeetCode = new OverlapLeetCode();
        List<Interval> sortedIntervals = overlapLeetCode.sortIntervals(randomTestCases,
                new OverlapLeetCode.IntervalComparator());
        System.err.println("LC Ordered       : " + sortedIntervals);

        final OverlapL2R overlapL2R = new OverlapL2R();
        sortedIntervals = overlapL2R.sortIntervals(randomTestCases, new OverlapR2L.MergeComparator());
        System.err.println("L2R Ordered      : " + sortedIntervals);

        final Merger lcMerge = overlapLeetCode.merge(randomTestCases, null);
        final Merger l2rMerge = overlapL2R.merge(randomTestCases, null);
        String label = "LeetCode vs Left-to-right";
        final String rangeTestTitle = "testing " + label;

        final int n_result_lc = lcMerge.merged.size();
        final int n_result_l2r = l2rMerge.merged.size();

        System.err.println("\nLC-L2R Input2    : " + randomTestCases);
        System.err.println("LC  merged       : " + lcMerge.merged);
        System.err.println("L2R merged       : " + l2rMerge.merged);

        final int minMatchingSize = Math.min(n_result_lc, n_result_l2r);
        for (int i = 0; i < minMatchingSize; i++)
        {
            final Interval lcInterval = lcMerge.merged.get(i);
            Assert.assertNotNull(i + ". LC NULL - " + rangeTestTitle, lcInterval);
            final Interval r2lInterval = l2rMerge.merged.get(i);
            Assert.assertNotNull(i + ". L2R NULL - " + rangeTestTitle, r2lInterval);
            Assert.assertTrue(
                    i + ". " + rangeTestTitle + " merged result differs => LC: " + lcInterval + ", L2R: " + r2lInterval,
                    lcInterval.equals(r2lInterval));
        }

        Assert.assertEquals(rangeTestTitle + " :: Resulting merged LC and L2R count differs", n_result_lc,
                n_result_l2r);

        final int n_mrg_lc = lcMerge.merges;
        final int n_mrg_l2r = l2rMerge.merges;
        Assert.assertEquals(rangeTestTitle + " :: Merged LC and L2R intervals count differs", n_mrg_lc, n_mrg_l2r);
    }

    @Test
    public void testCompareR2LandL2R()
    {
        final int randomTestCaseCount = 5;
        final List<Interval> randomTestCases = TestPerfUtil.generateTestCases(TestPerfUtil.INITIAL_SEED,
                randomTestCaseCount, TestPerfUtil.INTERVAL_LBOUND, TestPerfUtil.INTERVAL_UBOUND);
        System.err.println("\n------\nR2L-L2R Input    : " + randomTestCases);

        final OverlapR2L overlapR2L = new OverlapR2L();
        final List<Interval> sortedIntervals = overlapR2L.sortIntervals(randomTestCases,
                new OverlapR2L.MergeComparator());
        System.err.println("R2L-L2R Ordered  : " + sortedIntervals);

        final Merger rtlMerge = overlapR2L.merge(randomTestCases, null);
        final Merger l2rMerge = new OverlapL2R().merge(randomTestCases, null);
        String label = "Right-to-left vs Left-to-right";
        final String rangeTestTitle = "testing " + label;

        final int n_result_lc = rtlMerge.merged.size();
        final int n_result_r2l = l2rMerge.merged.size();

        System.err.println("\nR2L-L2R Input2   : " + randomTestCases);
        System.err.println("R2L merged       : " + rtlMerge.merged);
        System.err.println("L2R merged       : " + l2rMerge.merged);

        final int minMatchingSize = Math.min(n_result_lc, n_result_r2l);
        for (int i = 0; i < minMatchingSize; i++)
        {
            final Interval rtlInterval = rtlMerge.merged.get(i);
            Assert.assertNotNull(i + ". RTL NULL - " + rangeTestTitle, rtlInterval);
            final Interval l2rInterval = l2rMerge.merged.get(i);
            Assert.assertNotNull(i + ". L2R NULL - " + rangeTestTitle, l2rInterval);
            Assert.assertTrue(i + ". " + rangeTestTitle + " merged result differs => RTL: " + rtlInterval + ", L2R: "
                    + l2rInterval, rtlInterval.equals(l2rInterval));
        }

        Assert.assertEquals(rangeTestTitle + " :: Resulting merged RTL and L2R count differs", n_result_lc,
                n_result_r2l);

        final int n_mrg_r2l = rtlMerge.merges;
        final int n_mrg_l2r = l2rMerge.merges;
        Assert.assertEquals(rangeTestTitle + " :: Merged R2L and L2R intervals count differs", n_mrg_r2l, n_mrg_l2r);
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
