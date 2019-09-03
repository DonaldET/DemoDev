package demo.algo.sensor.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;

import demo.algo.sensor.ExposureAreaFinder;
import demo.algo.sensor.SensorMonitoring;
import demo.algo.sensor.SensorMonitoring.ExposureSession;
import demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Randomly generate pixel saturation data
 */
public class GenerateDataAndTest {
	private static final Random rand = new Random(6047L);

	public static int testCases = 0;

	private static final int n0 = 1;
	private static final int k0 = 1;
	private static final List<Rectangle> session0 = new ArrayList<Rectangle>();
	private static final int area0 = 12;

	private static final int n1 = 2;
	private static final int k1 = 1;
	private static final List<Rectangle> session1 = new ArrayList<Rectangle>();
	private static final int area1 = 24;

	private static final int n2 = 4;
	private static final int k2 = 2;
	private static final List<Rectangle> session2 = new ArrayList<Rectangle>();
	private static final int area2 = 14;

	private static final int n3 = 2;
	private static final int k3 = 2;
	private static final List<Rectangle> session3 = new ArrayList<Rectangle>();
	private static final int area3 = 4;

	private static final int n4 = 3;
	private static final int k4 = 2;
	private static final List<Rectangle> session4 = new ArrayList<Rectangle>();
	private static final int area4 = 8;

	private static final int n5 = 7;
	private static final int k5 = 2;
	private static final List<Rectangle> session5 = new ArrayList<Rectangle>();
	private static final int area5 = 20;

	static {
		testCases = 0;

		session0.add(new Rectangle(1, 1, 4, 5));
		testCases++;

		session1.add(new Rectangle(1, 6, 4, 10));
		session1.add(new Rectangle(1, 1, 4, 5));
		testCases++;

		session2.add(new Rectangle(1, 2, 3, 8));
		session2.add(new Rectangle(2, 3, 6, 7));
		session2.add(new Rectangle(0, 6, 8, 10));
		session2.add(new Rectangle(0, 0, 8, 4));
		testCases++;

		session3.add(new Rectangle(1, 1, 4, 3));
		session3.add(new Rectangle(2, 1, 4, 4));
		testCases++;

		session4.add(new Rectangle(1, 1, 5, 5));
		session4.add(new Rectangle(4, 4, 7, 6));
		session4.add(new Rectangle(3, 3, 8, 7));
		testCases++;

		session5.add(new Rectangle(9, 3, 13, 7));
		session5.add(new Rectangle(1, 2, 3, 8));
		session5.add(new Rectangle(2, 3, 6, 7));
		session5.add(new Rectangle(0, 0, 8, 4));
		session5.add(new Rectangle(12, 6, 15, 8));
		session5.add(new Rectangle(11, 5, 14, 9));
		session5.add(new Rectangle(0, 6, 8, 10));
		testCases++;
	}

	/**
	 * Prevent construction
	 */
	private GenerateDataAndTest() {
	}

	public static ExposureSession getSessionData(int id) {
		switch (id) {
		case 0:
			return new ExposureSession(n0, k0, area0, session0);
		case 1:
			return new ExposureSession(n1, k1, area1, session1);
		case 2:
			return new ExposureSession(n2, k2, area2, session2);
		case 3:
			return new ExposureSession(n3, k3, area3, session3);
		case 4:
			return new ExposureSession(n4, k4, area4, session4);
		case 5:
			return new ExposureSession(n5, k5, area5, session5);
		}

		throw new IllegalArgumentException("no data for ID " + id);
	}

	public static void testData(int id, ExposureAreaFinder finder, boolean display) {
		ExposureSession es = GenerateDataAndTest.getSessionData(id);
		if (display) {
			System.out.println("ID: " + id + " ==> " + es);
		}

		int exposedArea = finder.findArea(es.sessions, es.k);
		Assert.assertEquals(
				"test " + id + ", expected area " + es.expectedArea + " but got " + exposedArea + " for id " + id,
				es.expectedArea, exposedArea);
	}

	public static List<Rectangle> generateRandomBursts(int n) {
		List<Rectangle> exposures = new ArrayList<Rectangle>(n);
		for (int i = 0; i < n; i++) {
			int w = rand.nextInt(10) + 1;
			int h = rand.nextInt(5) + 1;
			int x = rand.nextInt(SensorMonitoring.XY_UPPER_BOUND - w);
			int y = rand.nextInt(SensorMonitoring.XY_UPPER_BOUND - h);
			exposures.add(new Rectangle(x, y, x + w, y + h));
		}

		return exposures;
	}
}
