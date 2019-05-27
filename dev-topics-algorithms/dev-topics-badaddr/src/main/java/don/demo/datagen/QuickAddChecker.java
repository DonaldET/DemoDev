package don.demo.datagen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Test summation methods for a specific sequence size. This is the test bed for
 * the addition checker utility program called <em>Bad Addr</em>. Note that Java
 * does not exactly support IEEE math, and a summary found <a href=
 * "http://www6.uniovi.es/java-http/1.0alpha3/doc/javaspec/javaspec_10.html">here</a>.
 * 
 * @author Donald Trummell
 */
public class QuickAddChecker {
	/* Sequence size */
	public static final int SEQUENCE_SIZE = 50_000_000;

	public QuickAddChecker() {
	}

	private static double sumNEven(int n) {
		return ((double) n / 2.0) * ((double) n + 1.0);
	}

	private static double sumNOdd(int n) {
		return (double) n * (((double) n + 1.0) / 2.0);
	}

	public static void main(final String[] args) {
		final int n = SEQUENCE_SIZE;
		System.out.println(String.format("\nCheck Addition Accuracy Test for sequence %d long", n));
		final List<Double> test_seq = new ArrayList<Double>();
		for (int i = 0; i < n; i++) {
			test_seq.add((double) i + 1.0);
		}
		System.out.println("  forward: " + String.valueOf(test_seq.subList(0, 6)).trim() + " . . . for "
				+ test_seq.size() + " entries.");
		double sum_exp = (n % 2 == 0) ? sumNEven(n) : sumNOdd(n);
		System.out.println(String.format("  exp sum: %.0f", sum_exp));
		assert Math.rint(sum_exp) == sum_exp;

		double sum_f = test_seq.stream().reduce(0.0, Double::sum);
		System.out.println(String.format("  act sum: %.0f  -- by stream-reduce", sum_f));
		assert sum_f == sum_exp;

		final double LARGE_PRIME = 7919.0;
		System.out.println("\n--- Now divide by large prime " + (int) LARGE_PRIME + " ---");
		for (int i = 0; i < n; i++) {
			test_seq.set(i, test_seq.get(i) / LARGE_PRIME);
		}
		sum_exp /= LARGE_PRIME;
		System.out.println(String.format("True sum is now %f", sum_exp));

		/* Divide by a large prime to introduce representational error */

		sum_f = test_seq.stream().reduce(0.0, Double::sum);
		double delta = sum_f - sum_exp;
		double re = delta / sum_exp;
		System.out.println(
				String.format("\nNo-shuffle stream sum is %f;  delta: %f;" + "  relative error: %e;  sigd: %.1f", sum_f,
						delta, re, GeneratorUtil.estimateSignificantDigits(re)));

		sum_f = 0.0;
		for (int i = 0; i < n; i++) {
			sum_f += test_seq.get((n - 1) - i);
		}
		delta = sum_f - sum_exp;
		re = delta / sum_exp;
		System.out.println(
				String.format("\nLargest to smallest sum is %f;  delta: %f;" + "  relative error: %e;  sigd: %.1f",
						sum_f, delta, re, GeneratorUtil.estimateSignificantDigits(re)));

		/* Shuffle to introduce randomization in summation */

		System.out.println("\n--- Now Shuffle ---");
		Collections.shuffle(test_seq, new Random(3677));
		System.out.println("Randomized");

		sum_f = test_seq.stream().reduce(0.0, Double::sum);
		delta = sum_f - sum_exp;
		re = delta / sum_exp;
		System.out.println(String.format("\nStream sum is %f;  delta: %f;" + "  relative error: %e;  sigd: %.1f", sum_f,
				delta, re, GeneratorUtil.estimateSignificantDigits(re)));

		sum_f = test_seq.stream().parallel().reduce(0.0, Double::sum);
		delta = sum_f - sum_exp;
		re = delta / sum_exp;
		System.out
				.println(String.format("\nParallel stream sum is %f;  delta: %f;" + "  relative error: %e;  sigd: %.1f",
						sum_f, delta, re, GeneratorUtil.estimateSignificantDigits(re)));

		sum_f = 0.0;
		for (int i = 0; i < n; i++) {
			sum_f += test_seq.get(i);
		}
		delta = sum_f - sum_exp;
		re = delta / sum_exp;
		System.out.println(String.format("\nSimple sum is %f;" + "  delta: %f;  relative error: %e;  sigd: %.1f", sum_f,
				delta, re, GeneratorUtil.estimateSignificantDigits(re)));

		System.out.println("\nAccuracy test complete.\n");
	}
}
