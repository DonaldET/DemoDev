package demo.en.performance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import demo.en.calls.SolutionBins;
import demo.en.calls.SolutionBins.ActiveCall;
import demo.en.performance.CallGenerator.Call;

public class CallAnalyzer {

	public static void main(String[] args) {
		final int n = 250_000;
		System.out.println("\nCall Generator Analyzer for " + n + " calls in a day:");

		CallGenerator cg = new CallGenerator(CallFinderEvaluator.rangeStart, CallFinderEvaluator.rangeEnd,
				CallFinderEvaluator.peaks, CallFinderEvaluator.durationMin, CallFinderEvaluator.durationMax);
		List<Call> rawCalls = cg.generate(n);
		System.out.println("Raw (to the minute):\n  -- n            : " + rawCalls.size());

		int minT = Integer.MAX_VALUE;
		int maxT = Integer.MIN_VALUE;
		int minD = Integer.MAX_VALUE;
		int maxD = Integer.MIN_VALUE;
		for (Call rc : rawCalls) {
			minT = Math.min(minT, rc.start);
			maxT = Math.max(maxT, rc.end);
			int d = rc.end - rc.start + 1;
			minD = Math.min(minD, d);
			maxD = Math.max(maxD, d);
		}
		System.out.println("  -- Min Time     : " + minT);
		System.out.println("  -- Max Time     : " + maxT);
		System.out.println("  -- Min duration : " + minD);
		System.out.println("  -- Max duration : " + maxD);

		SolutionBins sb = new SolutionBins();
		int[] allCounts = sb.getHourlyMaxCallCounts(rawCalls);
		System.out.println("\nHourly Max Call Count:\n  -- n       : " + allCounts.length);
		int minC = Arrays.stream(allCounts).min().getAsInt();
		int maxC = Arrays.stream(allCounts).max().getAsInt();
		System.out.println("  -- Min Cnt : " + minC);
		System.out.println("  -- Max Cnt : " + maxC);

		System.out.println("\nHourly Detail:\nHour, Count");
		for (int h = 0; h < 24; h++) {
			System.out.println(h + ", " + allCounts[h]);
		}
	}

	public static List<ActiveCall> getHourCounts(List<ActiveCall> allCounts) {
		int lth = allCounts.size();
		List<ActiveCall> hourlyCounts = new ArrayList<ActiveCall>(lth);
		if (lth > 0) {
			Map<Integer, Integer> hourly = new TreeMap<Integer, Integer>();
			for (ActiveCall ac : allCounts) {
				hourly.merge(ac.time / 60, 1, (x, y) -> x + y);
			}

			for (Entry<Integer, Integer> e : hourly.entrySet()) {
				hourlyCounts.add(new ActiveCall(e.getKey(), e.getValue()));
			}
		}

		return hourlyCounts;
	}
}
