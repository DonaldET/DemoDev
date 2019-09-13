package don.demo.badaddr.impl;

import java.util.List;
import java.util.stream.Stream;

import don.demo.badaddr.BadAddrRunner;

/**
 * Simple summation without error propagation protection
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class BadAddrRunnerImpl implements BadAddrRunner
{

    public BadAddrRunnerImpl() {
    }

    /**
     * Perform both Non-parallel or paralle addition
     */
    @Override
    public BadAddrRunner.Summation doOperation(final String label, final List<Double> testSequence,
            final boolean isParallel)
    {
        final long start = System.nanoTime();
        Stream<Double> stream = testSequence.stream();
        if (isParallel)
        {
            stream = stream.parallel();
        }
        final Double summation = stream.reduce(0.0, Double::sum);
        final long elapsed = System.nanoTime() - start;
        return new Summation(label, testSequence.size(), summation, elapsed);
    }
}
