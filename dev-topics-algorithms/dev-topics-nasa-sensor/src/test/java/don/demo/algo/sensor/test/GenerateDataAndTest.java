package don.demo.algo.sensor.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Assert;

import don.demo.algo.sensor.ExposureAreaFinder;
import don.demo.algo.sensor.SensorMonitoring;
import don.demo.algo.sensor.SensorMonitoring.ExposureSession;
import don.demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Randomly generate pixel saturation data as bursts in rectangular regions.
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
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
	private static final int area2 = 16;

	private static final int n3 = 2;
	private static final int k3 = 2;
	private static final List<Rectangle> session3 = new ArrayList<Rectangle>();
	private static final int area3 = 4;

	private static final int n4 = 3;
	private static final int k4 = 2;
	private static final List<Rectangle> session4 = new ArrayList<Rectangle>();
	private static final int area4 = 9;

	private static final int n5 = 7;
	private static final int k5 = 2;
	private static final List<Rectangle> session5 = new ArrayList<Rectangle>();
	private static final int area5 = 23;

	private static final int n6 = 4;
	private static final int k6 = 4;
	private static final List<Rectangle> session6 = new ArrayList<Rectangle>();
	private static final int area6 = 625;

	private static final int n7 = 0;
	private static final int k7 = 1;
	private static final List<Rectangle> session7 = new ArrayList<Rectangle>();
	private static final int area7 = 0;

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

		session6.add(new Rectangle(350, 350, 375, 375));
		session6.add(new Rectangle(200, 200, 500, 500));
		session6.add(new Rectangle(300, 300, 400, 400));
		session6.add(new Rectangle(0, 0, 1000, 1000));
		testCases++;

		session7.clear();
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
		case 6:
			return new ExposureSession(n6, k6, area6, session6);
		case 7:
			return new ExposureSession(n7, k7, area7, session7);
		}

		throw new IllegalArgumentException("no data for ID " + id);
	}

	public static void testData(int id, ExposureAreaFinder finder, boolean display) {
		ExposureSession es = GenerateDataAndTest.getSessionData(id);
		if (display) {
			System.out.println("ID: " + id + " ==> " + es);
		}

		int exposedArea = finder.findArea(es.sessions, es.k);
		if (es.k != es.expectedArea) {
			StringBuilder msg = new StringBuilder(
					"test " + id + ", sessions size: " + es.sessions.size() + ", k: " + es.k);
			int i = 0;
			for (Rectangle rec : es.sessions) {
				msg.append("\n    " + (++i) + ". " + rec);
			}
			msg.append("\n");
			Assert.assertEquals(msg.toString(), es.expectedArea, exposedArea);
		}
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
