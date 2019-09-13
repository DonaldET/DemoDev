package don.demo.datagen;

/**
 * Generation utilities:
 * <ul>
 * <li>Find sample data generation limits.</li>
 * <li>Compute mathematical (exact) sum.</li>
 * </ul>
 *
 * <pre>
 * <code>
 * A constant holding the largest positive finite value of type double,
 * (2-2-52)·21023. It is equal to the hexadecimal floating-point literal
 * 0x1.fffffffffffffP+1023 and also equal to
 * Double.longBitsToDouble(0x7fefffffffffffffL) and ±1.79769313486231570E+308
 * (15 significant decimal digits)
 *
 * From direct output:
 * o max - [1.79769313486231570e+308]
 * o min - [4.90000000000000000e-324]
 * </code>
 * </pre>
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 */
public class GeneratorUtil {
	private GeneratorUtil() {
		// prevent construction
	}

	/**
	 * Solve for max n such that: n * (n - 1) / 2 @lt@eq upperBound.
	 *
	 * <pre>
	 * <code>
	 * Solution of n^2 - n - 2 * uB @lt@eq is:
	 * (-1 ± sqrt(1 + 8 * uB)) / 2
	 * </code>
	 * </pre>
	 *
	 * @param upperBound largest value allowed for sum-of-integers
	 *
	 * @return the n solving the relationship above.
	 */
	public static double limit_sum_long(final double upperBound) {
		return (-1.0 + Math.sqrt(1.0 + 8.0 * upperBound)) / 2.0;
	}

	/**
	 * Compute exact sum of first n integers.
	 *
	 * @param n upper limit
	 * @return sum of 1 .. n
	 */
	public static double sum_n(final double n) {
		if (n % 2.0 == 0.0) {
			return (n / 2.0) * (n + 1);
		} else {
			return n * ((n + 1.0) / 2.0);
		}
	}

	/**
	 * Given: abs(re) &lt;= 0.5 * 10**(-M) for M significant digits. As a result, M
	 * &lt;= log10(0.5) - log10(abs(re))
	 *
	 * @param rel relative error.
	 *
	 * @return the fractional number of significant digits.
	 */
	public static double estimateSignificantDigits(final double rel) {
		final double absrel = Math.abs(rel);

		if (absrel < 1.0E-15)
			return 16.0;

		final double SIG_DIGIT_OFFSET = Math.log10(0.5);

		final double sd = SIG_DIGIT_OFFSET - Math.log10(absrel);
		assert sd >= 0.0;

		return Math.rint(10.0 * sd + 0.5) / 10.0;
	}
}
