package don.demo.algo.sensor.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import don.demo.algo.sensor.ExposureAreaFinder;
import don.demo.algo.sensor.MonitorExposureOverlapped;

/**
 * Apply bursts and compute area, compare to expected.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class TestExposureOverlapped {
	private ExposureAreaFinder finder = null;

	@Before
	public void setUp() throws Exception {
		finder = new MonitorExposureOverlapped();
	}

	@After
	public void tearDown() throws Exception {
		finder = null;
	}

	@Test
	public void testCases() {
		for (int id = 0; id < GenerateDataAndTest.testCases; id++) {
			if (id != 2 && id != 4 && id != 5 && id != 6)
				GenerateDataAndTest.testData(id, finder, false);
		}
	}
}
