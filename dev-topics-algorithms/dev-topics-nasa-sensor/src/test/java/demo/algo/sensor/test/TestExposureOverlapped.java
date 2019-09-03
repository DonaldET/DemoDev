package demo.algo.sensor.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import demo.algo.sensor.ExposureAreaFinder;
import demo.algo.sensor.MonitorExposureHybrid;

public class TestExposureOverlapped {
	private ExposureAreaFinder finder = null;

	@Before
	public void setUp() throws Exception {
		finder = new MonitorExposureHybrid();
	}

	@After
	public void tearDown() throws Exception {
		finder = null;
	}

	@Test
	public void testCases() {
		for (int id = 0; id < GenerateDataAndTest.testCases; id++) {
			GenerateDataAndTest.testData(id, finder, false);
		}
	}
}
