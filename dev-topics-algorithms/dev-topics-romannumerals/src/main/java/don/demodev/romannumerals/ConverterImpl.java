package don.demodev.romannumerals;

/**
 * Convert a binary integer to Roman numerals; convert a string of Roman
 * numerals to binary. We generally print integers (binary numbers) using Arabic
 * numerals. This is an iterative solution.
 * 
 * @author Don
 */
public class ConverterImpl implements Converter {

	private static final RomanNotation EMPTY_ROMAN_NOTATION = new RomanNotation("", 0);
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

	// ------------------------------------------------------------------------------------------------

	/**
	 * Iteratively reduce the arabic value producing roman numerals left to right,
	 * applying additive and subtractive notations repeatedly.
	 * 
	 * @param arabic the binary value to convert to roman numerals.
	 * 
	 * @return a string with roman numerals representing the arabic input value.
	 */
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
	 * Additive notation uses up to three sequential instances of a roman numeral to
	 * represent the Arabic value.
	 * 
	 * @param arabic       binary value to convert to roman numeral
	 * @param numeralIndex current roman numeral to work on
	 * 
	 * @return the roman numerals corresponding to the potential additive notation.
	 */
	public RomanNotation useAdditiveNotation(final int arabic, final int numeralIndex) {
		final Roman2Arabic currentNumeral = Converter.mapping[numeralIndex];
		final int romanValue = currentNumeral.arabic;
		final int howMany = arabic / romanValue;
		if (howMany < 1) {
			return EMPTY_ROMAN_NOTATION;
		}

		final int limitedUsed = Math.min(3, howMany);
		return new RomanNotation(symbols[numeralIndex].substring(0, limitedUsed), limitedUsed * romanValue);
	}

	public static final int[] powerOf10 = { 2, 4, 6 }; // C, X, I

	/**
	 * Subtractive notation assumes additive notation has already been used to find
	 * whole number multiples of the current Roman numeral, starting with largest
	 * numeral first.
	 * 
	 * @param arabic       binary value to convert to roman numeral
	 * @param numeralIndex current Roman numeral to work on in the representation.
	 * 
	 * @return the roman numerals corresponding to the potential subtractive
	 *         notation.
	 */
	public RomanNotation useSubtractiveNotation(final int arabic, final int numeralIndex) {
		final int romanValue = Converter.mapping[numeralIndex].arabic;
		if (arabic < 1 || numeralIndex == Converter.mapping.length - 1) {
			return EMPTY_ROMAN_NOTATION;
		}

		//
		// Try to subtract the largest value possible

		for (int i = powerOf10.length - 1; i >= 0; i--) {
			int subtracted = romanValue - mapping[powerOf10[i]].arabic;
			if (subtracted > 0 && subtracted <= arabic) {
				return new RomanNotation(mapping[powerOf10[i]].romanSymbol + mapping[numeralIndex].romanSymbol,
						subtracted);
			}
		}

		return EMPTY_ROMAN_NOTATION;
	}
}