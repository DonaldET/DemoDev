package don.demo.en.performance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import don.demo.en.calls.MaxCallFinder;
import don.demo.en.calls.SolutionBins;
import don.demo.en.calls.SolutionBinsLinear;
import don.demo.en.calls.SolutionEventsQueue;
import don.demo.en.performance.CallGenerator.Call;

public class PerformanceRunner {
	private static final int EXPECTED_MAX_CALLS = 6489;

	/**
	 * Test range of interest
	 */
	private static final int[] testDays = { 1, 10, 20, 40, 50, 60, 70, 80, 90, 100, 110, 120 };

	private enum Algo {
		EVENT_QUEUE, MAP_BIN, LINEAR_BIN
	};

	public static void main(String[] args) {
		final int n = 250_000;
		final int m = CallFinderEvaluator.rangeEnd - CallFinderEvaluator.rangeStart + 1;
		System.out.println("\nFind max simultaneous calls in " + n + " calls per day, tracked in " + m
				+ " bins per day for Binning");
		System.out.println("  -- Days to Scan: " + Arrays.toString(testDays));
		String msg = String.format("algo, days, findTm, sortTm, totalTm,volume");
		System.out.println("\n" + msg);

		Random r = new Random(737L);

		final List<Call> calls = generateBaseDays(n);
		List<Algo> algos = new ArrayList<Algo>();
		for (Algo a : Algo.values()) {
			algos.add(a);
		}

		for (int days : testDays) {
			Collections.shuffle(algos, r);
			for (Algo a : algos) {
				performOneRun(a, days, calls, EXPECTED_MAX_CALLS);
			}
		}
		System.out.println("Done");
	}

	private static void performOneRun(Algo algo, int days, List<Call> calls, int expectedMaxCalls) {
		System.gc();
		String label = algo.name();
		MaxCallFinder finder = null;
		switch (algo) {
		case EVENT_QUEUE:
			finder = new SolutionEventsQueue();
			break;
		case MAP_BIN:
			finder = new SolutionBins();
			break;
		case LINEAR_BIN:
			finder = new SolutionBinsLinear();
			break;
		}
		int maxCalls = processSample(label, finder, days, calls, algo == Algo.EVENT_QUEUE);
		if (maxCalls != expectedMaxCalls) {
			throw new IllegalStateException("wrong maxCalls for " + label + " on " + days + ": " + maxCalls
					+ "; expected: " + expectedMaxCalls);
		}
	}

	private static List<Call> generateBaseDays(final int n) {
		CallGenerator cg = new CallGenerator(CallFinderEvaluator.rangeStart, CallFinderEvaluator.rangeEnd,
				CallFinderEvaluator.peaks, CallFinderEvaluator.durationMin, CallFinderEvaluator.durationMax);
		return cg.generate(n);
	}

	/**
	 * Evaluate MaxCallFinder performance
	 * 
	 * @param label     call finder testing name.
	 * @param finder    the max call finder instance to test.
	 * @param days      number of consecutive days to test.
	 * @param calls     a list of calls for a day - random order.
	 * @param sortFirst if true, then sort calls before processing with max call
	 *                  finder.
	 */
	private static int processSample(String label, MaxCallFinder finder, int days, List<Call> calls,
			boolean sortFirst) {
		List<Call> moreCalls = CallGenerator.replicateDays(days, calls);

		long sortTm = 0;
		if (sortFirst) {
			long startSortTm = System.nanoTime();
			Collections.sort(moreCalls);
			sortTm = System.nanoTime() - startSortTm;
		}

		long startTm = System.nanoTime();
		final int m = CallFinderEvaluator.rangeEnd - CallFinderEvaluator.rangeStart + 1;
		final int maxCalls = finder.getMaxCallCount(moreCalls, CallFinderEvaluator.rangeStart, m * days - 1);
		long elapsed = System.nanoTime() - startTm;
		String msg = String.format("%s, %d, %.3f, %.3f, %.3f, %d", label, days,
				elapsed / CallFinderEvaluator.NANO_TO_SECOND, sortTm / CallFinderEvaluator.NANO_TO_SECOND,
				(elapsed + sortTm) / CallFinderEvaluator.NANO_TO_SECOND, moreCalls.size());
		System.out.println(msg);

		return maxCalls;
	}
}
