package demo.don.liveramp.bitsearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Defines mechanisms to search a Map based population of IP addresses and
 * access counts. This also provides an unsigned integer (@code Comparator) and
 * an enhanced binary search technique to improve performance of counting subnet
 * entries.
 * <p>
 * <strong>Problem</strong>
 * <p>
 * Given a collection of IP address and access counts, sum the access counts for
 * a specified subnet entry. IP values are 32 bit integers, and a subnet is the
 * first N bits of the IP address. As background, please review some definitions
 * of network vocabulary and the nature of the problem - isolating subnets -
 * that are explained at
 * <a href="https://www.pcwdld.com/subnet-mask-cheat-sheet-guide">this
 * tutorial</a>.
 * <p>
 * <strong>Solution</strong>
 * <p>
 * This searching utility assumes IP address and counts are represented in a
 * <code>Map</code> structure and provides two ways of inspecting the
 * <code>Map</code> for subnet counts:
 * <ul>
 * <li>the <code>SimpleCounter</code> that provides a linear search</li>
 * <li>the <code>BoundedSearchCounter</code> that orders the IP/Count pairs,
 * does a binary search to locate the beginning of a subnet group, and limits
 * the end of the search based on the sorted order</li>
 * </ul>
 */
public class BitSearcher {
	public static final long SEED = 7687;
	private static final boolean RANDOMIZED = true;

	/**
	 * Prevent construction
	 */
	private BitSearcher() {
	}

	/**
	 * Logical comparison of two 32 bit integers to provide an ordering defined by
	 * the (@code Comparator) interface.. We illustrate the ordering imposed by the
	 * comparator using the 4-bit integer number line as an example:
	 * 
	 * <pre>
	 * <code>
	 * 0000 (+0), 0001 (+1), 0010 (+2), 0011 (+3), 0100 (+4), 0101 (+5), 0110 (+6) 0111 (+7), then
	 * 1000 (-8), 1001 (-7), 1010 (-6), 1011 (-5), 1100 (-4), 1101 (-3), 1110 (-2) 1111 (-1)
	 * </code>
	 * </pre>
	 * 
	 * All negative values are to the right of all positive values, and more
	 * negative values are to the left of less negative values.
	 */
	public static class UnsignedComparator implements Comparator<Integer> {
		
		/**
		 * Logical (<em>unsigned</em>) comparison of two 32 bit integers.
		 * 
		 * @param ref1: left-hand side of comparison
		 * @param ref2: right-hand side of comparison
		 * 
		 * @return -1 if o1 @lt; 02, +1 if o1 @gt; o2, and 0 if o1 == o2.
		 */		
		@Override
		public int compare(final Integer ref1, final Integer ref2) {
			final int lhs = ref1; // Avoid Java autoboxing
			final int rhs = ref2;
			return lhs == rhs ? 0 : Integer.compareUnsigned(lhs, rhs);
		}
	}

	/**
	 * Extract optionally sorted (unsigned) integer keys from a map; return
	 * Primitive ints in an array to speed binary search.
	 * 
	 * @param usageCount a (@code HashMap) instance with IP as key and usage count
	 *                   as value
	 * @param order      sort ascending of (@code true)
	 * @return the optionally ordered list of keys extracted from the (@code
	 *         usageCount) input map.
	 */
	public static int[] getKeys(Map<Integer, Integer> usageCount, boolean order) {
		Set<Integer> keySet = usageCount.keySet();
		final int n = keySet.size();

		int[] keyAry = new int[0];
		if (n < 1) {
			return keyAry;
		} else if (n < 2) {
			return new int[] { usageCount.keySet().iterator().next() };
		}

		if (order) {
			Integer[] keys = new Integer[n];
			keySet.toArray(keys);
			keySet = null;

			//
			// Sort for later search
			Arrays.parallelSort(keys, new UnsignedComparator());

			keyAry = new int[n];
			for (int i = 0; i < n; i++) {
				keyAry[i] = keys[i];
			}
			keys = null;
		} else {
			List<Integer> keys = new ArrayList<Integer>(keySet);
			keySet = null;

			if (RANDOMIZED) {
				Random r = new Random();
				r.setSeed(SEED);

				//
				// Randomize to insure original order does not influence search
				Collections.shuffle(keys, r);
			}

			keyAry = new int[n];
			for (int i = 0; i < n; i++) {
				keyAry[i] = keys.get(i);
			}
			keys = null;
		}

		return keyAry;
	}

	public static class Found {
		public final int probe;
		public final int cmp;

		public Found(int probe, int cmp) {
			super();
			this.probe = probe;
			this.cmp = cmp;
		}

		@Override
		public String toString() {
			return "[Found - 0x" + Integer.toHexString(hashCode()) + ";  probe=" + probe + ",  cmp=" + cmp + "]";
		}

	}

	/**
	 * Locate a search key in list, or a point into which insertion is possible,
	 * depending on the comparison key returned in the search result.
	 * 
	 * @param keys    a non-empty list to search
	 * @param mask    a bit mask selecting the left-most bits of the pattern to use
	 *                in comparisons
	 * @param pattern the left-most set of bits used to define the search value
	 * @return <code>null</code> if list is empty, else a <code>Found</code>
	 *         instance with the last probe point and the comparison result.
	 */
	public static Found findInsertionPoint(final int[] keys, final int mask, final int pattern) {
		final int n = keys.length;
		if (n < 1) {
			return null;
		}

		final int searchKey = mask & pattern; // isolate meaningful part of key
		final Comparator<Integer> uc = new UnsignedComparator();

		int low = 0, high = n - 1, probe = -1, lastProbe = -1, cmp = -1;
		do {
			probe = (low + high) / 2;
			cmp = uc.compare(searchKey, mask & keys[probe]);
			if (cmp == 0) {
				break;
			} else if (cmp < 0) {
				// search key is left of prob
				high = probe - 1;
				if (high < low) {
					break;
				}
			} else {
				// search key is right of prob
				low = probe + 1;
				if (low > high) {
					break;
				}
			}
			lastProbe = probe;
			probe = (low + high) / 2;
		} while (probe != lastProbe);

		return new Found(probe, cmp);
	}

	// -----------------------------------------------------------------------------------
	// ---------- Bit Pattern Counter Implementations
	// -----------------------------------------------------------------------------------

	private static int countMatchesBeforeFirstFound(final Map<Integer, Integer> ipCounts, final Comparator<Integer> uc,
			final int mask, final int pattern, final int[] keys, int start) {
		int count = 0;
		final int wantedPrefix = mask & pattern;
		int lookBack = start - 1;
		while (lookBack >= 0) {
			if (uc.compare(wantedPrefix, mask & keys[lookBack]) == 0) {
				count += ipCounts.get(keys[lookBack]);
				lookBack--;
			} else {
				break;
			}
		}
		return count;
	}

	private static int countMatchesAfterFirstFound(final Map<Integer, Integer> ipCounts, final Comparator<Integer> uc,
			final int mask, final int pattern, final int[] keys, int start, int length) {
		int count = 0;
		if (length < 1) {
			return count;
		}

		final int wantedPrefix = mask & pattern;
		for (int i = start; i < (start + length); i++) {
			final int cmp = uc.compare(wantedPrefix, mask & keys[i]);
			if (cmp == 0) {
				count += ipCounts.get(keys[i]);
			} else if (cmp > 0) {
				break;
			}
		}

		return count;
	}

	private static int countMatchesInUnsortedArray(final Map<Integer, Integer> ipCounts, final int mask,
			final int pattern, final int[] keys, int start, int length) {
		int count = 0;
		if (length < 1) {
			return count;
		}

		final int wantedPrefix = mask & pattern;
		for (int i = start; i < (start + length); i++) {
			if (wantedPrefix == (mask & keys[i])) {
				count += ipCounts.get(keys[i]);
			}
		}

		return count;
	}

	// ------------------------------------------------------------------------------------
	// ------ Counter Implementations
	// ------------------------------------------------------------------------------------

	public static interface PrefixCounter {
		public abstract String getName();

		public abstract boolean isOrdered();

		public abstract int countMatches(final Map<Integer, Integer> ipCounts, int mask, int pattern, int[] keys);
	}

	/**
	 * Sequential search for desired prefix
	 */
	public static class SimpleCounter implements PrefixCounter {
		@Override
		public String getName() {
			return "PrefixCounter::" + getClass().getSimpleName();
		}

		@Override
		public boolean isOrdered() {
			return false;
		}

		@Override
		public int countMatches(final Map<Integer, Integer> ipCounts, final int mask, final int pattern,
				final int[] keys) {
			return countMatchesInUnsortedArray(ipCounts, mask, pattern, keys, 0, keys.length);
		}
	}

	/**
	 * Binary search to start, then stop after maximum is passed
	 */
	public static class BoundedSearchCounter implements PrefixCounter {
		@Override
		public String getName() {
			return getClass().getSimpleName();
		}

		@Override
		public boolean isOrdered() {
			return true;
		}

		/**
		 * Keys are unsigned integers in ascending order
		 */
		@Override
		public int countMatches(final Map<Integer, Integer> ipCounts, final int mask, final int pattern,
				final int[] keys) {
			final int n = keys.length;
			int count = 0;
			if (n > 0) {
				if (n < 4) {
					count = countMatches(ipCounts, mask, pattern, keys);
				} else {
					final Comparator<Integer> uc = new UnsignedComparator();
					final Found insertp = findInsertionPoint(keys, mask, pattern);
					int start = insertp.probe;
					if (insertp.cmp == 0) {
						count += countMatchesBeforeFirstFound(ipCounts, uc, mask, pattern, keys, start);
						count += countMatchesAfterFirstFound(ipCounts, uc, mask, pattern, keys, start, n - start);
					}
				}
			}

			return count;
		}
	}

	/**
	 * Binary search to start, then stop after maximum is passed
	 */
	public static class SimpleSearchCounter implements PrefixCounter {
		@Override
		public String getName() {
			return getClass().getSimpleName();
		}

		@Override
		public boolean isOrdered() {
			return false;
		}

		/**
		 * Keys are unsigned integers in ascending order
		 */
		@Override
		public int countMatches(final Map<Integer, Integer> ipCounts, final int mask, final int pattern,
				final int[] keys) {
			final int n = keys.length;
			int count = 0;
			if (n > 0) {
				count += countMatchesInUnsortedArray(ipCounts, mask, pattern, keys, 0, keys.length);
			}

			return count;
		}
	}

	/**
	 * Defines a test population of IP addresses and counts, with subnet mask and
	 * pattern, each having count access entries
	 */
	public static class SubnetPopulationParameters {
		public final int mask;
		public final int pattern;
		public final int count;

		public SubnetPopulationParameters(int mask, int pattern, int count) {
			this.mask = mask;
			this.pattern = pattern;
			this.count = count;
		}

		@Override
		public String toString() {
			return "[SubnetPopulationParameters - 0x" + Integer.toHexString(hashCode()) + "; mask=0x"
					+ Integer.toHexString(mask) + ",  pattern=0x" + Integer.toHexString(pattern) + ", count=" + count
					+ "]";
		}
	}
}
