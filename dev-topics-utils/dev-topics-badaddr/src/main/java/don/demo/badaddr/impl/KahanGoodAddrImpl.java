package don.demo.badaddr.impl;

import java.util.List;

import don.demo.badaddr.BadAddrRunner;

/**
 * The Kahan algorithm provides a "<em>safe adder</em>" that more accurately
 * sums floating point numbers. This version is based on the one presented in
 * <a href="https://en.wikipedia.org/wiki/Kahan_summation_algorithm">Kahn
 * Summation Algorithm</a> found on Wikipedia. Supporting references are:
 * <ol>
 * <li><a href=
 * "https://people.eecs.berkeley.edu/~demmel/AccurateSummation.pdf">An in depth
 * academic analysis</a></li>
 * <li><a href=
 * "http://ipl.physics.harvard.edu/wp-uploads/2013/03/PS3_Error_Propagation_sp13.pdf">A
 * Summary of Error Propagation</a></li>
 * <li><a href="http://lectureonline.cl.msu.edu/~mmp/labs/error/e2.htm">Error
 * Propagation</a></li>
 * <li><a href="http://www.utm.edu/staff/cerkal/Lect4.html">ERROR
 * ANALYSIS</a></li>
 * </ol>
 * 
 * @author Donald Trummell
 */
public class KahanGoodAddrImpl implements BadAddrRunner
{

    @Override
    public Summation doOperation(String label, List<Double> testSequence, final boolean isParallel)
    {
        if (isParallel)
        {
            throw new UnsupportedOperationException("Kahan Adder not parallelizable");
        }
        final long start = System.nanoTime();
        final double summation = KahanSum(testSequence);
        final long elapsed = System.nanoTime() - start;

        return new Summation(label, testSequence.size(), summation, elapsed);
    }

    private double KahanSum(final List<Double> input)
    {
        double sum = 0.0;
        double c = 0.0; // A running compensation for lost low-order bits.
        for (int i = 0; i < input.size(); i++)
        {
            double y = input.get(i) - c; // So far, so good: c is zero.
            double t = sum + y; // Alas, sum is big, y small, so low-order
                                // digits of y are lost.
            c = (t - sum) - y; // (t - sum) cancels the high-order part of y;
                               // subtracting y recovers negative (low part of
                               // y)
            sum = t; // Algebraically, c should always be zero. Beware
                     // overly-aggressive optimizing compilers!
        } // Next time around, the lost low part will be added to y in a fresh
          // attempt.
        return sum;
    }
}
