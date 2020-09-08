package don.demodev.romannumerals;

/**
 * Convert a binary integer to Roman numerals; convert a string of Roman
 * numerals to binary. We generally print integers (binary numbers) using Arabic
 * numerals. This is a recursive implementation.
 * 
 * @author Don
 */
public class ConverterImplRecursive implements Converter {

	private static final String[] symbols;

	static {
		final int n = Converter.mapping.length;
		final int nseq = 3;
		symbols = new String[n];
		int j = 0;
		for (Roman2Arabic ra : Converter.mapping) {
			StringBuilder sb = new StringBuilder(nseq);
			for (int i = 0; i < nseq; i++) {
				sb.append(ra.romanSymbol);
			}
			symbols[j++] = sb.toString();
		}
	}

	private static final class RomanNumeralNotation {
		public final String romanNumerals;
		public final int remainder;

		public RomanNumeralNotation(String romanNumerals, int remainder) {
			super();
			this.romanNumerals = romanNumerals;
			this.remainder = remainder;
		}
	}

	public ConverterImplRecursive() {
	}

	@Override
	public String arabic2Roman(int arabic) {
		if (arabic < 0) {
			throw new IllegalArgumentException("argument small");
		}
		if (arabic > MAX_CONVERSION) {
			throw new IllegalArgumentException("argument large");
		}

		return convert2Roman(new RomanNumeralNotation("", arabic), 0).romanNumerals;
	}

	@Override
	public int roman2Arabic(String roman) {
		throw new IllegalStateException("no implemented");
	}

	// ------------------------------------------------------------------------------------------------

	/**
	 * Recursively represent a binary arabic input as roman numerals, beginning with
	 * the largest roman numeral and continuing until the arabic value is
	 * represented. Apply first the additive and then the subtractive notations.
	 * 
	 * @param input        the roman numeral representation and arabic value.
	 * @param numeralIndex the roman numeral value to use in the representation.
	 * 
	 * @return the roman numeral representation of the reduced arabic value.
	 */
	private RomanNumeralNotation convert2Roman(final RomanNumeralNotation input, final int numeralIndex) {
		if (input.remainder < 1) {
			return input;
		}
		final RomanNumeralNotation current = useSubtractiveNotation(useAdditiveNotation(input, numeralIndex),
				numeralIndex);
		return current.remainder == 0 ? current : convert2Roman(current, numeralIndex + 1);
	}

	/**
	 * Subtractive notation assumes additive notation has already been used to find
	 * whole number multiples of the current roman numeral, starting with largest
	 * numeral first.
	 * 
	 * @param input        the current roman numeral representation and the
	 *                     remaining arabic value to represent.
	 * @param numeralIndex the roman numeral value to use representing the arabic
	 *                     value.
	 * 
	 * @return the enhanced Roman numeral representation and the reduced arabic
	 *         value.
	 */
	public RomanNumeralNotation useAdditiveNotation(final RomanNumeralNotation input, final int numeralIndex) {
		final int romanValue = Converter.mapping[numeralIndex].arabic;
		final int howMany = input.remainder / romanValue;
		if (howMany < 1) {
			return input;
		}

		final int limitedUsed = Math.min(3, howMany);
		return new RomanNumeralNotation(input.romanNumerals + symbols[numeralIndex].substring(0, limitedUsed),
				input.remainder - limitedUsed * romanValue);
	}

	public static final int[] powerOf10 = { 2, 4, 6 }; // C, X, I

	public RomanNumeralNotation useSubtractiveNotation(final RomanNumeralNotation input, final int numeralIndex) {
		if (input.remainder < 1 || numeralIndex == Converter.mapping.length - 1) {
			return input;
		}

		//
		// Try to subtract the largest value possible

		final int romanValue = Converter.mapping[numeralIndex].arabic;
		for (int i = powerOf10.length - 1; i >= 0; i--) {
			final int subtracted = romanValue - mapping[powerOf10[i]].arabic;
			if (subtracted > 0 && subtracted <= input.remainder) {
				return new RomanNumeralNotation(
						input.romanNumerals + mapping[powerOf10[i]].romanSymbol + mapping[numeralIndex].romanSymbol,
						input.remainder - subtracted);
			}
		}

		return input;
	}
}