/**
 * Eratosthenes.java
 */
package don.demo.llmexamples.filechecker;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Find primes in an interval using the Sieve of Eratosthenes method (see
 * <a href="https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes">Wiki
 * Sieve</a>.)
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class Eratosthenes {
	private static final int First_Odd_Prime = 3;

	/**
	 * Prevent construction
	 */
	private Eratosthenes() {
	}

	/**
	 * Find primes using <em>Sieve</em> method.
	 *
	 * @param n     the size of the sieve
	 * @param start the starting value if the sieve
	 * @return An optionally empty list of prime integers found in the sieve
	 */
	public static List<Integer> findValues(final int n, final int start) {
		assert n > 0;
		assert start >= 2;

		final int beginVal = 1 + 2 * (start / 2);
		final int endVal = beginVal + 2 * (n - 1);
		final int limit = checkingUpperBoundValue(endVal);

		final BitSet sieve = new BitSet(n);
		for (int p = First_Odd_Prime; p <= limit; p += 2) {
			int pmult = beginVal / p;
			if (pmult % 2 == 0) {
				pmult++;
			}
			int pstart = pmult * p;
			if (pstart < beginVal) {
				pmult += 2;
				pstart += 2 * p;
			}
			if (pstart <= endVal) {
				boolean skip = pmult < 2;
				for (int markOff = pstart; markOff <= endVal; markOff += 2 * p) {
					if (skip) {
						skip = false;
					} else {
						sieve.set((markOff - beginVal) / 2);
					}
				}
			}
		}

		final List<Integer> primes = new ArrayList<Integer>();
		for (int i = beginVal; i <= endVal; i += 2) {
			if (!sieve.get((i - beginVal) / 2)) {
				primes.add(i);
			}
		}

		return primes;
	}

	private static int checkingUpperBoundValue(final int endVal) {
		//
		// First odd value S.T. limit * limit > endVal

		int limit = (int) Math.ceil(Math.sqrt(endVal));
		if (limit % 2 == 0) {
			limit++;
		}
		if (limit * limit <= endVal) {
			limit += 2;
		}

		return limit;
	}
}
