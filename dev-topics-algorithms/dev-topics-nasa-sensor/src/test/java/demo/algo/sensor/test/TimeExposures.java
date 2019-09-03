package demo.algo.sensor.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import demo.algo.sensor.ExposureAreaFinder;
import demo.algo.sensor.MonitorExposureByArea;
import demo.algo.sensor.MonitorExposureByAreaMapped;
import demo.algo.sensor.MonitorExposureHybrid;
import demo.algo.sensor.SensorMonitoring.Rectangle;

public class TimeExposures {
	private static final int[] nbursts = { 435, 500, 1_000, 10_000, 100_000, 500_000, 1_000_000, 5_000_000, 10_000_000 };
	private static final int[] kval = { 2, 2, 2, 2, 2, 2, 2, 2, 7 };
	private static final int[] areas = { 3, 16, 184, 12008, 260655, 10563, 731, 1, 0 };
	private static final List<Integer> monitors = new ArrayList<Integer>();
	private static final List<ExposureAreaFinder> finders = new ArrayList<ExposureAreaFinder>();

	static {
		finders.add(new MonitorExposureByAreaMapped());
		finders.add(new MonitorExposureByArea());
		finders.add(new MonitorExposureHybrid());

		for (int i = 0; i < finders.size(); i++) {
			monitors.add(i);
		}
		
		int n = nbursts.length;
		if (kval.length != n) {throw new IllegalArgumentException("kval length differs, expected " + n + " but got " + kval.length);}
		if (areas.length != n) {throw new IllegalArgumentException("areas length differs, expected " + n + " but got " + areas.length);}
	}

	private static final Random rand = new Random(337L);

	public static void main(String[] args) {
		System.out.println("ALGO, ID, N, K, Time");
		for (int i = 0; i < nbursts.length; i++) {
			if (i != 1) continue;
			if (i > 1) break;
			System.gc();
			List<Rectangle> exposureSessions = GenerateDataAndTest.generateRandomBursts(nbursts[i]);
			Collections.shuffle(monitors, rand);
			for (Integer implKey : monitors) {
				ExposureAreaFinder finder = finders.get(implKey);
				int id = 100 + i;
				System.out.print(
						finder.getClass().getSimpleName() + ", " + (i + 100) + ", " + nbursts[i] + ", " + kval[i]);
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
