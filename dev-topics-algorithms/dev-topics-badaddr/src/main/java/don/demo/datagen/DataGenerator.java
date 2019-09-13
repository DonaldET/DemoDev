package don.demo.datagen;

import java.util.List;

/**
 * API for a generator that creates samples for testing summation error
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface DataGenerator
{
    public static final double DG_BIGGEST_INTVAL = 8999999999999999.0e+0;

    public abstract List<Double> generateSequence(final int start, final int step, final int n);

    public abstract void shuffleSample(final List<Double> sample);

    public abstract void divideScaleSample(final List<Double> sample, final double divisor);

    public abstract void sortSampleAscending(final List<Double> sample);

    public abstract void sortSampleDescending(final List<Double> sample);
}