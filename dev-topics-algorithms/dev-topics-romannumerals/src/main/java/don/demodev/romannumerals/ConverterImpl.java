package don.demodev.romannumerals;

/**
 * Convert a binary integer to Roman numerals; convert a string of Roman
 * numerals to binary. We generally print integers (binary numbers) using Arabic
 * numerals. This
 * <a href= "https://en.wikipedia.org/wiki/Roman_numerals">Wikipedia Roman
 * Numerals</a> reference describes Roman numerals.
 * <p>
 * The problem of Roman numerals representing numbers is similar the classical
 * change-making problem see
 * <a href="https://en.wikipedia.org/wiki/Change-making_problem">the change
 * problem</a>.)
 * <p>
 * Rules for Roman Numeral representation are found at <a href=
 * "http://www.solano.edu/academic_success_center/forms/math/Roman%20Numerals.pdf">solono.edu</a>.
 * <p>
 * This implementation uses up to three occurrances of the largest roman numeral
 * <strong>N</strong> no greater than the arabic value. This is the
 * <em>additive</em> strategy. There may be a &quot;remainder&quot; after
 * applying the additive strategy that may be reduced by using the
 * <em>subtractive</em> strategy.
 * 
 * @author Don
 */
public class ConverterImpl implements Converter {

	public ConverterImpl() {
	}

	@Override
	public String arabic2Roman(int arabic) {
		if (arabic < 0) {
			throw new IllegalArgumentException("argument small");
		}
		if (arabic > MAX_CONVERSION) {
			throw new IllegalArgumentException("argument large");
		}

		return convertRoman2ArabicImpl(arabic);
	}

	@Override
	public int roman2Arabic(String roman) {
		throw new IllegalStateException("no implemented");
	}

	private static final class RomanNotation {
		public final String romanNumerals;
		public final int subtracted;

		public RomanNotation(String romanNumerals, int subtracted) {
			super();
			this.romanNumerals = romanNumerals;
			this.subtracted = subtracted;
		}
	}

	private String convertRoman2ArabicImpl(final int arabic) {
		final StringBuilder roman = new StringBuilder();
		int numeralIndex = 0;
		int value = arabic;
		while (value > 0) {
			RomanNotation romanNotation = useAdditiveNotation(value, numeralIndex);
			if (romanNotation.subtracted > 0) {
				value -= romanNotation.subtracted;
				roman.append(romanNotation.romanNumerals);
			}

			if (value > 0) {
				romanNotation = useSubtractiveNotation(value, numeralIndex);
				if (romanNotation.subtracted > 0) {
					value -= romanNotation.subtracted;
					roman.append(romanNotation.romanNumerals);
				}
			}
			numeralIndex++;
		}

		return roman.toString();

	}

	/**
	 * Additive notation uses up to three sequential instances of a roman numeral.
	 * 
	 * @param arabic       binary value to convert to roman numeral
	 * @param numeralIndex current roman numeral to work on
	 * 
	 * @return the roman numerals corresponding to the potential additive notation.
	 */
	public RomanNotation useAdditiveNotation(final int arabic, final int numeralIndex) {
		if (arabic < 1) {
			return new RomanNotation("", 0);
		}

		final int romanValue = Converter.mapping[numeralIndex].arabic;
		final int howMany = arabic / romanValue;
		if (howMany < 1) {
			return new RomanNotation("", 0);
		}

		final StringBuilder roman = new StringBuilder();
		final int limitedUsed = Math.min(3, howMany);
		for (int i = 0; i < limitedUsed; i++) {
			roman.append(Converter.mapping[numeralIndex].romanSymbol);
		}

		return new RomanNotation(roman.toString(), limitedUsed * romanValue);
	}

	public static final int[] powerOf10 = { 2, 4, 6 }; // C, X, I

	/**
	 * Subtractive notation assumes additive notation has already been used to find
	 * whole number multiples of the current roman numeral, starting with largest
	 * numeral first.
	 * 
	 * @param arabic       binary value to convert to roman numeral
	 * @param numeralIndex current roman numeral to work on in the representation.
	 * 
	 * @return the roman numerals corresponding to the potential subtractive
	 *         notation.
	 */
	public RomanNotation useSubtractiveNotation(final int arabic, final int numeralIndex) {
		final int romanValue = Converter.mapping[numeralIndex].arabic;
		if (romanValue <= arabic) {
			throw new IllegalArgumentException("arabic value " + arabic + " is bigger than the current roman numeral "
					+ romanValue + " [" + numeralIndex + "]");
		}

		if (arabic < 1 || numeralIndex == Converter.mapping.length - 1) {
			return new RomanNotation("", 0);
		}

		//
		// Try to subtract the largest value possible

		for (int i = powerOf10.length - 1; i >= 0; i--) {
			int subtracted = romanValue - mapping[powerOf10[i]].arabic;
			if (subtracted > 0) {
				if (subtracted <= arabic) {
					return new RomanNotation(mapping[powerOf10[i]].romanSymbol + mapping[numeralIndex].romanSymbol,
							subtracted);
				}
			}
		}

		return new RomanNotation("", 0);
	}
}