package don.demodev.romannumerals;

/**
 * Convert a binary integer to Roman numerals; convert a string of Roman
 * numerals to binary.. We generally print binary numbers using Arabic numerals.
 * This <a href= "https://en.wikipedia.org/wiki/Roman_numerals">Wikipedia Roman
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

	public String arabic2Roman(final int arabic) {
		if (arabic < 0) {
			throw new IllegalArgumentException("argument small");
		}
		if (arabic > MAX_CONVERSION) {
			throw new IllegalArgumentException("argument large");
		}

		return convertRoman2ArabicImpl(0, arabic);
	}

	private String convertRoman2ArabicImpl(final int idx, final int num) {
		return null;
	}

	public int roman2Arabic(final String roman) {
		throw new IllegalStateException("no implemented");
	}
}