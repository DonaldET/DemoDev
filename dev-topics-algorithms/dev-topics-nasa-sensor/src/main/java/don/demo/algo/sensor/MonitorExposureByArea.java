package don.demo.algo.sensor;

import java.util.List;

import don.demo.algo.sensor.SensorMonitoring.BoundingBox;
import don.demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Graphics based technique where exposed regions are modeled at the pixel level
 * using an array. The area represented by the array is minimized using a
 * bounding box around the entire sensor grid of exposed regions.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class MonitorExposureByArea implements ExposureAreaFinder {

	public static void main(String[] args) {
	}

	/**
	 * Time complexity is O(n * a), where n = number of radiation exposure sessions,
	 * and a is a measure of algorithm effort in a session (e.g., the mean area
	 * exposed per session.) If the exposed area is relatively fixed and small, and
	 * the number of sessions n is large, then the complexity is, by definition,
	 * O(n).
	 */
	@Override
	public int findArea(List<? extends Rectangle> exposures, final int k) {
		if (exposures.isEmpty()) {
			return 0;
		}

		//
		// Get exposure session bounding box: O(n)
		BoundingBox bbox = SensorMonitoring.findBoundingBox(exposures);

		//
		// Allocate bounding box of exposed Sensor region: O(1)
		int[] sensorRegions = new int[bbox.width * bbox.height];

		//
		// Apply radiation per exposure: O(n * a)
		return SensorMonitoring.exposeSensor(sensorRegions, bbox, exposures, k);
	}
}
