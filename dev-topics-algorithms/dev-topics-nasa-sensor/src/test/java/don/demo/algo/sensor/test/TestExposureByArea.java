package don.demo.algo.sensor.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import don.demo.algo.sensor.ExposureAreaFinder;
import don.demo.algo.sensor.MonitorExposureByArea;

/**
 * Apply bursts and compute area, compare to expected.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class TestExposureByArea {
	private ExposureAreaFinder finder = null;

	@Before
	public void setUp() throws Exception {
		finder = new MonitorExposureByArea();
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
