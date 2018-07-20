package demo.don.amazon.rangeconsolidator.test;

import java.util.List;

import demo.don.amazon.rangeconsolidator.Overlap.Interval;

public final class TestDef
{
    public final List<Interval> inputSet;
    public final String testLabel;
    public final List<Interval> expected;
    public final int expMerges;

    public TestDef(final List<Interval> inputSet, final String testLabel, final List<Interval> expected,
            final int expMerges) {
        super();
        this.inputSet = inputSet;
        this.testLabel = testLabel;
        this.expected = expected;
        this.expMerges = expMerges;
    }
}