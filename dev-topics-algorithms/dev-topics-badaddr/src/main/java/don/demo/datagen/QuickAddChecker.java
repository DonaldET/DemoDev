package don.demo.datagen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Test summation methods for a specific sequence size. This is the test bed for
 * the addition checker utility program called <em>Bad Addr</em>.
 * 
 * @author Donald Trummell
 */
public class QuickAddChecker {
	/* Sequence size */
	public static final int n = 50_000_000;

	public QuickAddChecker() {
	}

	public static void main(final String[] args) {
		System.out.println(String.format("Check Addition Accuracy Test for sequence %d long", n));
		final List<Double> test_seq = new ArrayList<Double>();
		for (int i = 0; i < n; i++) {
			test_seq.add((double)i + 1.0);
		}
		System.out.println("  forward: " + test_seq.subList(0, 6) + " . . . for " + test_seq.size() + " entries.");
		double sum_exp = ((double) n / 2.0) * ((double) n + 1.0);
		System.out.println(String.format("  exp sum: %.0f", sum_exp));
		assert Math.rint(sum_exp) == sum_exp;

		double sum_f = test_seq.stream().reduce(0.0, Double::sum);
		System.out.println(String.format("  act sum: %.0f", sum_f));
		assert sum_f == sum_exp;

		final double LARGE_PRIME = 7919.0;
		System.out.println("\nNow divide by large prime " + (int) LARGE_PRIME);
		for (int i = 0; i < n; i++) {
			test_seq.set(i, test_seq.get(i) / LARGE_PRIME);
		}
		sum_exp /= LARGE_PRIME;

		sum_f = test_seq.stream().reduce(0.0, Double::sum);
		double delta = sum_f - sum_exp;
		double re = delta / sum_exp;
		System.out.println(String.format(
				"\nNo-shuffle fractional stream sum is %f;  delta: %f;" + "  relative error: %e;  sigd: %.1f", sum_f,
				delta, re, GeneratorUtil.estimateSignificantDigits(re)));

		System.out.println("\nNow Shuffle");
		Collections.shuffle(test_seq, new Random(3677));

		System.out.println("\nUse streams SUM");
		sum_f = test_seq.stream().reduce(0.0, Double::sum);
		delta = sum_f - sum_exp;
		re = delta / sum_exp;
		System.out.println(
				String.format("Forward fractional stream sum is %f;  delta: %f;" + "  relative error: %e;  sigd: %.1f",
						sum_f, delta, re, GeneratorUtil.estimateSignificantDigits(re)));

		System.out.println("\nUse parallel streams SUM");
		sum_f = test_seq.stream().parallel().reduce(0.0, Double::sum);
		delta = sum_f - sum_exp;
		re = delta / sum_exp;
		System.out.println(String.format(
				"Forward fractional parallel stream sum is %f;  delta: %f;" + "  relative error: %e;  sigd: %.1f",
				sum_f, delta, re, GeneratorUtil.estimateSignificantDigits(re)));

		System.out.println("\nUse uncorrected simple SUM");
		sum_f = 0.0;
		for (int i = 0; i < n; i++) {
			sum_f += test_seq.get(i);
		}
		delta = sum_f - sum_exp;
		re = delta / sum_exp;
		System.out.println(
				String.format("Forward fractional simple sum is %f;" + "  delta: %f;  relative error: %e;  sigd: %.1f",
						sum_f, delta, re, GeneratorUtil.estimateSignificantDigits(re)));

		System.out.println("\nAccuracy test complete.\n");
	}
}
