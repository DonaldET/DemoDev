package don.demodev.romannumerals;

/**
 * Convert a binary integer to a Roman numeral string representation, and scan a
 * string of Roman numerals and convert to binary. This
 * <a href= "https://en.wikipedia.org/wiki/Roman_numerals">Wikipedia Roman
 * Numerals</a> reference describes Roman numerals and their uses.
 * <p>
 * The problem of Roman numerals representing numbers is similar the classical
 * change-making problem see
 * <a href="https://en.wikipedia.org/wiki/Change-making_problem">the change
 * problem</a>.)
 * <p>
 * Rules for Roman Numeral representation are found at <a href=
 * "http://www.solano.edu/academic_success_center/forms/math/Roman%20Numerals.pdf">solono.edu</a>.
 * <p>
 * This implementation uses up to three occurrences of the largest roman numeral
 * <strong>N</strong> no greater than the Arabic value. This is the
 * <em>additive</em> strategy. There may be a &quot;remainder&quot; after
 * applying the additive strategy, which may be reduced by using the
 * <em>subtractive</em> strategy. This approach is applied iteratively until the
 * entire arabic value is converted to Roman numerals.
 * 
 * @author Don
 */
public interface Converter {

	/**
	 * Integer value associated with a Roman Numeral symbol.
	 * 
	 * @author Don
	 */
	public static final class Roman2Arabic {
		public final String romanSymbol;
		public final int arabic;

		public Roman2Arabic(String romanSymbol, int arabic) {
			super();
			this.romanSymbol = romanSymbol;
			this.arabic = arabic;
		}

		@Override
		public String toString() {
			return "Roman2Arabic [Symbol=" + romanSymbol + ", Arabic=" + arabic + "]";
		}
	}

	/**
	 * Table of Roman numerals and associated integer value, ordered from largest
	 * value numeral to smallest value.
	 */
	public static final Roman2Arabic[] mapping = { new Roman2Arabic("M", 1000), new Roman2Arabic("D", 500),
			new Roman2Arabic("C", 100), new Roman2Arabic("L", 50), new Roman2Arabic("X", 10), new Roman2Arabic("V", 5),
			new Roman2Arabic("I", 1) };

	/**
	 * Upper limit of non-negative integers to convert
	 */
	public static final int MAX_CONVERSION = 3999;

	/**
	 * Convert a non-negative integer not greater than <code>MAX_CONVERSION</code>
	 * to Roman Numerals
	 * 
	 * @param arabic integer to convert
	 * 
	 * @return String representation as Roman Numerals
	 */
	public String arabic2Roman(int arabic);

	/**
	 * Scan a String of Roman Numerals and convert to an integer
	 * 
	 * @param roman non-null string of Roman Numerals
	 * 
	 * @return integer representation of Roman Numerals
	 */
	public int roman2Arabic(String roman);
}