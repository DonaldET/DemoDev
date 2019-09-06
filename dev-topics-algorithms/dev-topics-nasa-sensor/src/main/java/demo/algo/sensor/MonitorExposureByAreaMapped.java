package demo.algo.sensor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import demo.algo.sensor.SensorMonitoring.BoundingBox;
import demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Graphics based technique where exposed regions are modeled at the pixel
 * level, but pixels are represented in a map (the Sensor), assuming sparse
 * exposed regions over the interval of interest. The area represented by the
 * array is minimized using a bouding box around the exposed region.
 */
public class MonitorExposureByAreaMapped implements ExposureAreaFinder {
	private Map<Integer, Integer> sensorRegions = null;
	private BoundingBox bbox = null;

	public static void main(String[] args) {
	}

	/**
	 * Time complexity is O(n * a), where n = number of radiation exposure sessions,
	 * and a is a measure of effort in a session (e.g., the mean area exposed per
	 * session.) If, as implied by the problem statement, the area is relatively
	 * fixed and small, and the number of sessions n is large, then the complexity
	 * is, by definition, O(n).
	 */
	@Override
	public int findArea(List<? extends Rectangle> exposures, final int k) {
		//
		// Get exposure session bounding box: O(n)
		bbox = SensorMonitoring.findBoundingBox(exposures);

		//
		// Allocate bounding box of exposed Sensor region: O(1)
		int squares = bbox.width * bbox.height;
		sensorRegions = new HashMap<Integer, Integer>(squares);

		//
		// Apply radiation per exposure: O(n * a)
		for (Rectangle r : exposures) {
			for (int y = r.y1; y < r.y2; y++) {
				int ypos = y - bbox.lowerLeftY;
				int yposR = bbox.height - ypos - 1;
				for (int x = r.x1; x < r.x2; x++) {
					int xpos = x - bbox.lowerLeftX;
					int idx = yposR * bbox.width + xpos;
					sensorRegions.merge(idx, 1, (a, b) -> a + b);
				}
			}
		}

		//
		// Count squares with at least K exposures: O(n)
		int area = 0;
		for (Entry<Integer, Integer> e : sensorRegions.entrySet()) {
			if (e.getValue() >= k) {
				area++;
			}
		}
		return area;
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public Map<Integer, Integer> getSensorRegions() {
		return sensorRegions;
	}

	public BoundingBox getBbox() {
		return bbox;
	}
}
