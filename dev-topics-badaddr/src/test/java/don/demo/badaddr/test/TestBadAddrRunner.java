package don.demo.badaddr.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demo.badaddr.BadAddrRunner;
import don.demo.badaddr.BadAddrRunner.Summation;
import don.demo.badaddr.impl.BadAddrRunnerImpl;
import don.demo.datagen.GeneratorUtil;
import don.demo.datagen.impl.SimpleSequenceDataGenerator;

public class TestBadAddrRunner
{

    private BadAddrRunner bdr = null;

    @Before
    public void setUp() throws Exception
    {
        bdr = new BadAddrRunnerImpl();
    }

    @After
    public void tearDown() throws Exception
    {
        bdr = null;
    }

    @Test
    public void testDoopSequential()
    {
        final int n = 10;
        final String label = "Test " + n + "Sequential Additions";
        final Summation result = bdr.doOperation(label, new SimpleSequenceDataGenerator().generateSequence(1, 1, n),
                false);
        Assert.assertNotNull("test run result null for " + label, result);
        Assert.assertEquals("unexpected sum for " + label, GeneratorUtil.sum_n(n), result.testSum, 0.0);
    }

    @Test
    public void testDoopParallel()
    {
        final int n = 10;
        final String label = "Test " + n + "Parallel Additions";
        final Summation result = bdr.doOperation(label, new SimpleSequenceDataGenerator().generateSequence(1, 1, n),
                true);
        Assert.assertNotNull("test run result null for " + label, result);
        Assert.assertEquals("unexpected sum for " + label, GeneratorUtil.sum_n(n), result.testSum, 0.0);
    }
}
