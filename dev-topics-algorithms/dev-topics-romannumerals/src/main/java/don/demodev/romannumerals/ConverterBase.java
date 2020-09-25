package don.demodev.romannumerals;

/**
 * Shared code between implementations; conforming to problem definition in
 * interface.
 * 
 * @author Don
 */
public abstract class ConverterBase implements Converter {

	protected static final String[] symbols;
	protected static final int[] powerOf10 = { 2, 4, 6 }; // C, X, I

	protected static final class RomanNumeralNotation {
		public final String romanNumerals;
		public final int remainder;

		public RomanNumeralNotation(String romanNumerals, int remainder) {
			super();
			this.romanNumerals = romanNumerals;
			this.remainder = remainder;
		}
	}

	static {
		final int nseq = 3;
		symbols = new String[Converter.mapping.length];
		int j = 0;
		for (Roman2Arabic ra : Converter.mapping) {
			StringBuilder sb = new StringBuilder(nseq);
			for (int i = 0; i < nseq; i++) {
				sb.append(ra.romanSymbol);
			}
			symbols[j++] = sb.toString();
		}
	}

	protected ConverterBase() {
		super();
	}
}