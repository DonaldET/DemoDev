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

	private String convertRoman2ArabicImpl(final int arabic) {
		final StringBuilder roman = new StringBuilder();
		int numeralIndex = 0;
		int value = arabic;
		while (value > 0) {
			int romanValue = Converter.mapping[numeralIndex].arabic;
			int howMany = value / romanValue;
			if (howMany > 0) {
				for (int i = 0; i < howMany; i++) {
					roman.append(Converter.mapping[numeralIndex].romanSymbol);
				}
				value -= howMany * Converter.mapping[numeralIndex].arabic;
			}
			numeralIndex++;
		}

		return roman.toString();
	}
}