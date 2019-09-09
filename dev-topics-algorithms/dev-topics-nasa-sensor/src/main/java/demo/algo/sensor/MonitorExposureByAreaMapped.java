package demo.algo.sensor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import demo.algo.sensor.SensorMonitoring.BoundingBox;
import demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Graphics based technique where exposed regions are modeled at the pixel
 * level, and pixels are represented in a map (the Sensor), assuming sparse
 * exposed regions over the interval of interest. The area represented by the
 * array is minimized using a bounding box around the exposed region.
 */
public class MonitorExposureByAreaMapped implements ExposureAreaFinder {
	private Map<Integer, Integer> sensorRegions = null;
	private BoundingBox bbox = null;

	public static void main(String[] args) {
	}

	/**
	 * Time complexity is O(n * a), where n = number of radiation exposure sessions,
	 * and a is a measure of algorithm effort in a session (e.g., the mean area per
	 * session.) If the exposed area is relatively fixed and small, and the number
	 * of sessions n is large, then the complexity is, by definition, O(n).
	 */
	@Override
	public int findArea(List<? extends Rectangle> exposures, final int k) {
		//
		// Get exposure session bounding box: O(n)
		bbox = SensorMonitoring.findBoundingBox(exposures);

		//
		// Allocate bounding box of exposed Sensor region: O(1)
		sensorRegions = new HashMap<Integer, Integer>(bbox.width * bbox.height);

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

		return (int) sensorRegions.entrySet().stream().filter(e -> e.getValue() >= k).count();
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public int[] getSensorRegions() {
		int[] sensorGrid = new int[bbox.width * bbox.height];
		for (Entry<Integer, Integer> e : sensorRegions.entrySet()) {
			if (sensorGrid[e.getKey()] != 0) {
				throw new IllegalStateException(e.getKey() + " has non-zero entry");
			}
			sensorGrid[e.getKey()] = e.getValue();
		}

		return sensorGrid;
	}

	public BoundingBox getBbox() {
		return bbox;
	}
}
