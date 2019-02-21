package don.demo.datagen.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demo.datagen.DataGenerator;
import don.demo.datagen.impl.SimpleSequenceDataGenerator;

public class TestDataGen
{

    private DataGenerator dg = null;

    @Before
    public void setUp() throws Exception
    {
        dg = new SimpleSequenceDataGenerator();
    }

    @After
    public void tearDown() throws Exception
    {
        dg = null;
    }

    @Test
    public void testGenerate()
    {
        final List<Double> expSequence = makeSimpleTestExpectedSequence();
        final List<Double> testSequence = dg.generateSequence(1, 1, expSequence.size());
        Assert.assertEquals("length differs", expSequence.size(), testSequence.size());
        for (int i = 0; i < testSequence.size(); i++)
        {
            Assert.assertEquals("Sequence " + i + " differs", expSequence.get(i), testSequence.get(i));
        }
    }

    @Test
    public void testAscendingGenerate()
    {
        final List<Double> expSequence = makeSimpleTestExpectedSequence();
        final List<Double> testSequence = dg.generateSequence(1, 1, expSequence.size());
        Assert.assertEquals("length differs", expSequence.size(), testSequence.size());
        dg.sortSampleAscending(testSequence);
        for (int i = 0; i < testSequence.size(); i++)
        {
            Assert.assertEquals("Sequence " + i + " differs", expSequence.get(i), testSequence.get(i));
        }
    }

    @Test
    public void testDescendingGenerate()
    {
        final List<Double> expSequence = makeSimpleTestExpectedSequence();
        Collections.sort(expSequence,
                (x, y) -> x.doubleValue() < y.doubleValue() ? 1 : (x.doubleValue() > y.doubleValue() ? -1 : 0));
        final List<Double> testSequence = dg.generateSequence(1, 1, expSequence.size());
        Assert.assertEquals("length differs", expSequence.size(), testSequence.size());
        dg.sortSampleDescending(testSequence);
        for (int i = 0; i < testSequence.size(); i++)
        {
            Assert.assertEquals("Sequence " + i + " differs", expSequence.get(i), testSequence.get(i));
        }
    }

    @Test
    public void testDivideGenerate()
    {
        final List<Double> expSequence = makeSimpleTestExpectedSequence();
        final List<Double> testSequence = dg.generateSequence(1, 1, expSequence.size());
        Assert.assertEquals("length differs", expSequence.size(), testSequence.size());
        for (int i = 0; i < expSequence.size(); i++)
        {
            expSequence.set(i, expSequence.get(i) / 2.0);
        }
        dg.divideScaleSample(testSequence, 2.0);
        for (int i = 0; i < testSequence.size(); i++)
        {
            Assert.assertEquals("Sequence " + i + " differs", expSequence.get(i), testSequence.get(i));
        }
    }

    // ---------------------------------------------------------

    private List<Double> makeSimpleTestExpectedSequence()
    {
        final List<Number> tstSec = Arrays.asList(1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0);
        final int m = tstSec.size();
        final List<Double> testSequence = new ArrayList<Double>(m);
        for (final Number num : tstSec)
        {
            testSequence.add(num.doubleValue());
        }

        return testSequence;
    }
}
