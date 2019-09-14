package demo.algo.sensor.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import demo.algo.sensor.ExposureAreaFinder;
import demo.algo.sensor.MonitorExposureOverlapped;
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
		finder = new MonitorExposureOverlapped();
	}

	@After
	public void tearDown() throws Exception {
		finder = null;
	}

	@Ignore
	@Test
	public void testOneNoOverlap() {
		int n = 1;
		int k = 1;
		final List<Rectangle> sessions = new ArrayList<Rectangle>();
		sessions.add(new Rectangle(1, 1, 4, 5));
		int area = 12;
		ExposureSession es = new ExposureSession(n, k, area, sessions);
		runTest("One-No-Overlap", es);
	}

	@Ignore
	@Test
	public void testTwoNoOverlap() {
		int n = 2;
		int k = 1;
		final List<Rectangle> sessions = new ArrayList<Rectangle>();
		sessions.add(new Rectangle(1, 1, 4, 5));
		sessions.add(new Rectangle(1, 6, 4, 10));
		int area = 24;
		ExposureSession es = new ExposureSession(n, k, area, sessions);
		runTest("Two-No-Overlap", es);
	}

	@Ignore
	@Test
	public void testThreeNoOverlap() {
		int n = 3;
		int k = 1;
		final List<Rectangle> sessions = new ArrayList<Rectangle>();
		sessions.add(new Rectangle(1, 1, 4, 5));
		sessions.add(new Rectangle(5, 1, 8, 5));
		sessions.add(new Rectangle(9, 1, 12, 5));
		int area = 36;
		ExposureSession es = new ExposureSession(n, k, area, sessions);
		runTest("Three-No-Overlap-x", es);
	}

	@Ignore
	@Test
	public void testThreeNoOverlapVertical() {
		int n = 3;
		int k = 1;
		final List<Rectangle> sessions = new ArrayList<Rectangle>();
		sessions.add(new Rectangle(1, 1, 4, 5));
		sessions.add(new Rectangle(1, 6, 4, 10));
		sessions.add(new Rectangle(1, 11, 4, 15));
		int area = 36;
		ExposureSession es = new ExposureSession(n, k, area, sessions);
		runTest("Three-No-Overlap-y", es);
	}

	@Test
	public void testFourNoOverlapTossOut() {
		int n = 4;
		int k = 1;
		final List<Rectangle> sessions = new ArrayList<Rectangle>();
		sessions.add(new Rectangle(1, 1, 4, 5));
		sessions.add(new Rectangle(1, 6, 4, 10));
		sessions.add(new Rectangle(1, 11, 4, 15));
		sessions.add(new Rectangle(5, 1, 8, 5));
		int area = 36;// 48;
		ExposureSession es = new ExposureSession(n, k, area, sessions);
		runTest("Four-No-Overlap-Toss", es);
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
