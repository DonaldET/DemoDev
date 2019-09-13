package demo.algo.sensor.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.algo.sensor.ExposureAreaFinder;
import demo.algo.sensor.MonitorExposureHybrid;
import demo.algo.sensor.SensorMonitoring.ExposureSession;
import demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Apply bursts and compute area, compare to expected.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class TestExposureOverlappedTDD {
	private ExposureAreaFinder finder = null;
	private boolean display = false;

	@Before
	public void setUp() throws Exception {
		finder = new MonitorExposureHybrid();
	}

	@After
	public void tearDown() throws Exception {
		finder = null;
	}

	@Test
	public void testOneNoOverlapp() {
		int n = 1;
		int k = 1;
		final List<Rectangle> sessions = new ArrayList<Rectangle>();
		sessions.add(new Rectangle(1, 1, 4, 5));
		int area = 12;
		ExposureSession es = new ExposureSession(n, k, area, sessions);
		runTest("One-No-Overlapp", es);
	}

	@Test
	public void testTwoNoOverlapp() {
		int n = 2;
		int k = 1;
		final List<Rectangle> sessions = new ArrayList<Rectangle>();
		sessions.add(new Rectangle(1, 1, 4, 5));
		sessions.add(new Rectangle(1, 6, 4, 10));
		int area = 24;
		ExposureSession es = new ExposureSession(n, k, area, sessions);
		runTest("Two-No-Overlapp", es);
	}

	// -------------------------------------------------------------------------------------------------------------------------

	private void runTest(String id, ExposureSession es) {
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
}
