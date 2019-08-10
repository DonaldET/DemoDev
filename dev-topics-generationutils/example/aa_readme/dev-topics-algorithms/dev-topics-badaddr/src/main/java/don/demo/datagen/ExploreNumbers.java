package don.demo.datagen;

import java.util.List;

import don.demo.badaddr.BadAddrRunner;
import don.demo.badaddr.impl.BadAddrRunnerImpl;
import don.demo.badaddr.impl.KahanGoodAddrImpl;
import don.demo.datagen.impl.SimpleSequenceDataGenerator;

/**
 * Explore floating point math and summation. Check runner implementations.
 *
 * @author Donald Trummell
 */
public class ExploreNumbers
{
    public static void main(String[] args)
    {
        System.out.println("\nNumeric representations:\n------------------------");
        System.out.println(String.format("max - [%22.17e]", Double.MAX_VALUE));
        System.out.println(String.format("min - [%22.17e]", Double.MIN_VALUE));
        final double big14 = 99999999999999.0e+0;
        System.out.println(String.format("big14 - [%22.17e]", big14));
        final double big15 = 999999999999999.0e+0;
        System.out.println(String.format("big15 - [%22.17e]", big15));
        final double big16 = 9999999999999999.0e+0;
        System.out.println(String.format("big16 - [%22.17e]", big16));
        final double big17 = 99999999999999999.0e+0;
        System.out.println(String.format("big17 - [%22.17e]", big17));
        final double bigI = 8999999999999999.0e+0;
        System.out.println(String.format("bigI - [%22.17e]", bigI));

        System.out.println("\nSummation Limits:\n-----------------");
        double uB = DataGenerator.DG_BIGGEST_INTVAL;
        final double ubEst = GeneratorUtil.limit_sum_long(uB);
        System.out.println(String.format("Solve for limit, upperBound %.1f ==> n = %.3f", uB, ubEst));
        final double n = Math.floor(ubEst);
        System.out.println(String.format("Sum first %.1f = %.1f", n, GeneratorUtil.sum_n(n)));
        final double np1 = n + 1.0;
        System.out.println(String.format("Sum first %.1f = %.1f", np1, GeneratorUtil.sum_n(np1)));
        System.out.println("-----------------");

        System.err.println("\nTest DataGenerator");
        final DataGenerator dg = new SimpleSequenceDataGenerator();
        final List<Double> seq = dg.generateSequence(1, 2, 5);
        System.err.println("SEQ    : " + seq);
        dg.sortSampleDescending(seq);
        System.err.println("SEQ -V : " + seq);
        dg.sortSampleAscending(seq);
        System.err.println("SEQ -^ : " + seq);
        dg.shuffleSample(seq);
        System.err.println("SEQ -s : " + seq);
        dg.divideScaleSample(seq, 2.0);
        System.err.println("SEQ /2 : " + seq);

        final int ni = 500;
        System.err.println("\nTest Summation to " + ni);
        BadAddrRunner baddr = new BadAddrRunnerImpl();
        final double expectedSum = GeneratorUtil.sum_n(ni);
        List<Double> testSequence = dg.generateSequence(1, 1, ni);
        System.err.println("Expected sum: " + expectedSum);
        BadAddrRunner.Summation operation = baddr.doOperation("Sequential", testSequence, false);
        System.err.println("Sequential Result: " + operation);
        final BadAddrRunner.Summation paraOperation = baddr.doOperation("Parallel", testSequence, true);
        System.err.println("Parallel Result  : " + paraOperation);

        System.err.println("\nTest Kahan Accurate Summation to " + ni);
        baddr = new KahanGoodAddrImpl();
        testSequence = dg.generateSequence(1, 1, ni);
        System.err.println("Expected sum: " + expectedSum);
        operation = baddr.doOperation("Kahn Sequential", testSequence, false);
        System.err.println("Kahan Sequential Result: " + operation);
    }
}
