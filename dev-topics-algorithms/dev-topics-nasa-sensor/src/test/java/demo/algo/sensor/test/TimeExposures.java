package demo.algo.sensor.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import demo.algo.sensor.ExposureAreaFinder;
import demo.algo.sensor.MonitorExposureByArea;
import demo.algo.sensor.MonitorExposureByAreaMapped;
import demo.algo.sensor.MonitorExposureHybrid;
import demo.algo.sensor.SensorMonitoring;
import demo.algo.sensor.SensorMonitoring.Rectangle;

public class TimeExposures {
	private static final int[] nbursts = { 500, 1_000, 10_000, 100_000, 500_000, 1_000_000, 2_000_000, 5_000_000,
			10_000_000, 15_000_000 };
	private static final int[] kval = { 2, 2, 3, 5, 18, 25, 51, 111, 217, 307 };
	private static final int[] areas = { 16, 185, 622, 27060, 2382, 33141, 2538, 2341, 126, 277 };
	private static List<Integer> monitors = null;
	private static final List<ExposureAreaFinder> finders = new ArrayList<ExposureAreaFinder>();

	static {
		finders.add(new MonitorExposureByAreaMapped());
		finders.add(new MonitorExposureByArea());
		finders.add(new MonitorExposureHybrid());

		monitors = new ArrayList<Integer>(finders.size());
		for (int i = 0; i < finders.size(); i++) {
			monitors.add(i);
		}

		int n = nbursts.length;
		if (kval.length != n) {
			throw new IllegalArgumentException("kval length differs, expected " + n + " but got " + kval.length);
		}
		if (areas.length != n) {
			throw new IllegalArgumentException("areas length differs, expected " + n + " but got " + areas.length);
		}
	}

	private static final Random rand = new Random(337L);

	public static void main(String[] args) {
		System.out.println("ALGO, ID, PIXELS, N, K, Time");
		int pixels = SensorMonitoring.XY_UPPER_BOUND - SensorMonitoring.XY_LOWER_BOUND;
		pixels *= pixels;
		for (int i = 0; i < nbursts.length; i++) {
			System.gc();
			List<Rectangle> exposureSessions = GenerateDataAndTest.generateRandomBursts(nbursts[i]);
			Collections.shuffle(monitors, rand); // randomize test order to minimize order effects
			for (Integer implKey : monitors) {
				ExposureAreaFinder finder = finders.get(implKey);
				int id = 100 + i;
				System.out.print(finder.getClass().getSimpleName() + ", " + (i + 100) + ", " + pixels + ", "
						+ nbursts[i] + ", " + kval[i]);
				double time = testFinder(id, nbursts[i], kval[i], areas[i], exposureSessions, finder);
				System.out.println(", " + time);
			}
		}
		System.out.println("Done");
	}

	private static double testFinder(int id, int n, int k, int expectedArea, List<Rectangle> exposures,
			ExposureAreaFinder finder) {
		long start = System.nanoTime();
		int exposedArea = finder.findArea(exposures, k);
		long elapsed = System.nanoTime() - start;
		if (exposedArea != expectedArea) {
			throw new IllegalStateException(
					"test " + id + ", expected area " + expectedArea + " but got " + exposedArea + " for id " + id);
		}

		return (double) elapsed / 1.0E9;
	}
}
