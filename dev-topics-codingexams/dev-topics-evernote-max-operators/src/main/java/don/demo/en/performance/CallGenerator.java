package don.demo.en.performance;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Create randomly distributed mock call data
 */
public class CallGenerator {

	private static final long SEED = 7793L;
	private static final Random rand = new Random(SEED);

	public static final class Call implements Comparable<Call>, Comparator<Call> {
		public final int start;
		public final int end;

		public Call(int start, int end) {
			super();
			if (start > end) {
				throw new IllegalArgumentException("Start (" + start + "0 > end (" + end + ")!");
			}

			this.start = start;
			this.end = end;
		}

		@Override
		public int compare(Call o1, Call o2) {
			return diff(o1, o2);
		}

		@Override
		public int compareTo(Call o) {
			return diff(this, o);
		}

		private static final int diff(Call lhs, Call rhs) {
			final int d1 = lhs.start - rhs.start;
			return d1 == 0 ? lhs.end - rhs.end : d1;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + end;
			result = prime * result + start;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			return compareTo((Call) obj) == 0;
		}

		@Override
		public String toString() {
			return "[" + start + ", " + end + " d: " + (end - start + 1) + "]";
		}
	}

	private final int rangeStart;
	private final int rangeEnd;
	private final int[] peaks;
	private final int durationMin;
	private final int durationMax;

	public CallGenerator(int rangeStart, int rangeEnd, int[] peaks, int durationMin, int durationMax) {
		super();
		this.rangeStart = rangeStart;
		this.rangeEnd = rangeEnd;
		this.peaks = peaks;
		this.durationMin = durationMin;
		this.durationMax = durationMax;
	}

	public static void main(String[] args) {
		final int n = 25;
		System.out.println("Call Generator Demo for " + n + " calls:");

		System.out.println("\nN, Start, End, Duration");
		CallGenerator cg = new CallGenerator(CallFinderEvaluator.rangeStart, CallFinderEvaluator.rangeEnd,
				CallFinderEvaluator.peaks, CallFinderEvaluator.durationMin, CallFinderEvaluator.durationMax);
		List<Call> calls = cg.generate(n);
		int i = 0;
		for (Call c : calls) {
			System.out.println((++i) + ", " + c.start + ", " + c.end + ", " + (c.end - c.start + 1));
		}
		System.out.println("Done");
	}

	public List<Call> generate(int n) {
		rand.setSeed(SEED);
		List<Call> calls = new ArrayList<Call>(n);
		for (int i = 0; i < n; i++) {
			int[] times = getCallTimes(rangeStart, rangeEnd, peaks, durationMin, durationMax);
			calls.add(new Call(times[0], times[1]));
		}

		return calls;
	}

	public static List<Call> replicateDays(int days, final List<Call> calls) {
		final List<Call> moreCalls = new ArrayList<Call>(calls.size() * days);
		for (int i = 0; i < days; i++) {
			for (Call c : calls) {
				int delta = i * 1440;
				moreCalls.add(new Call(c.start + delta, c.end + delta));
			}
		}
		return moreCalls;
	}

	private static int[] getCallTimes(int rangeStart, int rangeEnd, int[] peaks, int durationMin, int durationMax) {

		final int duration = durationMin + rand.nextInt(durationMax - durationMin);
		final int callRange = rangeEnd - rangeStart + 1;
		final int selector = rand.nextInt(peaks.length + 1);
		final int centerPoint = selector < peaks.length ? peaks[selector] : rangeStart + rand.nextInt(callRange);
		final int dither = rand.nextInt((int) (0.1 * callRange));
		final int startLow = Math.max(rangeStart, centerPoint + (rand.nextBoolean() ? dither : -dither));
		final int startTime = Math.min(rangeEnd, startLow);
		final int endTime = Math.min(rangeEnd, startTime + duration);

		return new int[] { startTime, endTime };
	}
}
