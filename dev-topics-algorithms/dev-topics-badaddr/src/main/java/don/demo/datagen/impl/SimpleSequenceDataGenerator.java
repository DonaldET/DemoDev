package don.demo.datagen.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import don.demo.datagen.DataGenerator;

/**
 * Generate summation testing samples.
 * 
 * @author Donald Trummell
 */
public class SimpleSequenceDataGenerator implements DataGenerator
{
    public static long COMMON_SEED = 3677;

    public SimpleSequenceDataGenerator() {
    }

    /**
     * Create a simple sequence.
     */
    @Override
    public List<Double> generateSequence(final int start, final int step, final int n)
    {
        final List<Double> sequence = new ArrayList<Double>(n);
        Double v = (double) start;
        for (int i = 1; i <= n; i++)
        {
            sequence.add(v);
            v += (double) step;
        }

        return sequence;
    }

    /**
     * Randomize sample.
     */
    @Override
    public void shuffleSample(final List<Double> sample)
    {
        Collections.shuffle(sample, new Random(COMMON_SEED));
    }

    /**
     * Scale down sample.
     */
    @Override
    public void divideScaleSample(final List<Double> sample, final double divisor)
    {
        for (int i = 0; i < sample.size(); i++)
        {
            sample.set(i, sample.get(i) / divisor);
        }
    }

    /**
     * Sort sample small to large.
     * 
     */
    @Override
    public void sortSampleAscending(List<Double> sample)
    {
        Collections.sort(sample);
    }

    /**
     * Sort sample large to small.
     */
    @Override
    public void sortSampleDescending(List<Double> sample)
    {
        Collections.sort(sample, (d1, d2) -> d1 < d2 ? 1 : (d1 > d2 ? -1 : 0));
    }
}
