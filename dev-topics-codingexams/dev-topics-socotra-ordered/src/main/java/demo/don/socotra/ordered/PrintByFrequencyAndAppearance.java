package demo.don.socotra.ordered;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Order by frequency and then order of appearance (pre-Java 8.)
 *
 * <pre>
 * <code>
 * 3, 3, 7, 7, 7, 5, 5, 18 input
 * 7, 7, 7, 3, 3, 5, 5, 18 ouput
 * 5, 3, 3, 7, 7, 7, 5, 18 input
 * 7, 7, 7, 5, 5, 3, 3, 18 output
 * </code>
 * </pre>
 *
 * @author Don Trummell
 */
public class PrintByFrequencyAndAppearance {
	public PrintByFrequencyAndAppearance() {
	}

	public static void main(String[] args) {
		System.out.println("\nOrder lists by frequency and then order-of-appearance");
		printOrderedList(Arrays.asList(3, 3, 7, 7, 7, 5, 5, 18));
		printOrderedList(Arrays.asList(5, 3, 3, 7, 7, 7, 5, 18));
	}

	private static void printOrderedList(final List<Integer> input) {
		assert input != null;
		final int n = input.size();
		System.out.println("\nProcessing " + n + " entries:");
		if (n < 1) {
			System.out.println("Empty");
			return;
		}
		System.out.println("  Input    : " + input);

		final Map<Integer, Integer> freq = new LinkedHashMap<>();
		for (final Integer inpt : input) {
			Integer value = freq.get(inpt);
			if (value == null) {
				freq.put(inpt, 1);
			} else {
				freq.put(inpt, value + 1);
			}
		}
		System.out.println("  Frequency: " + freq);

		final Set<Integer> keys = freq.keySet();
		System.out.println("  keys     : " + keys);

		class FreqOrderInfo {
			public final int frequency;
			public final int order;
			public final int key;

			public FreqOrderInfo(final int inputFreq, final int order, final int key) {
				super();
				this.frequency = inputFreq;
				this.order = order;
				this.key = key;
			}

			@Override
			public String toString() {
				// return "[" + key + " {" + freq + "," + order + "}]";
				return String.valueOf(key);
			}
		}

		final List<FreqOrderInfo> freqOrder = new ArrayList<FreqOrderInfo>(keys.size());
		int order = 0;
		for (Integer key : keys) {
			order += 1;
			freqOrder.add(new FreqOrderInfo(freq.get(key), order, key));
		}
		System.out.println("unsrt Out  : " + freqOrder);

		class FixOrder implements Comparator<FreqOrderInfo> {
			@Override
			public int compare(final FreqOrderInfo o1, final FreqOrderInfo o2) {
				return (o1.frequency < o2.frequency) ? 1
						: ((o1.frequency > o2.frequency) ? -1 : ((o1.order < o2.order) ? -1 : (o1.order > o2.order) ? 1 : 0));
			}

		}
		Collections.sort(freqOrder, new FixOrder());
		System.out.println("  srt Out  : " + freqOrder);
	}

//    Order lists by frequency and then order-of-appearance
//
//    Processing 8 entries:
//      Input    : [3, 3, 7, 7, 7, 5, 5, 18]
//      Frequency: {3=2, 7=3, 5=2, 18=1}
//      keys     : [3, 7, 5, 18]
//    unsrt Out  : [3, 7, 5, 18]
//      srt Out  : [7, 3, 5, 18]
//
//    Processing 8 entries:
//      Input    : [5, 3, 3, 7, 7, 7, 5, 18]
//      Frequency: {5=2, 3=2, 7=3, 18=1}
//      keys     : [5, 3, 7, 18]
//    unsrt Out  : [5, 3, 7, 18]
//      srt Out  : [7, 5, 3, 18]
}
