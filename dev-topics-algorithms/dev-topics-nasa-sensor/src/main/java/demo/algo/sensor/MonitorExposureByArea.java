package demo.algo.sensor;

import java.util.Arrays;
import java.util.List;

import demo.algo.sensor.SensorMonitoring.BoundingBox;
import demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Graphics based technique where exposed regions are modeled at the pixel
 * level, and pixels are represented using an array. The area represented by the
 * array is minimized using a bouding box around the exposed region.
 */
public class MonitorExposureByArea implements ExposureAreaFinder {
	private int[] sensorRegions = null;
	private BoundingBox bbox = null;

	public static void main(String[] args) {
	}

	/**
	 * Time complexity is O(n * a), where n = number of exposure sessions, and a is
	 * a measure of effort in a session (e.g., the mean area exposed regions per
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
		sensorRegions = new int[squares];

		//
		// Apply radiation per exposure: O(n * a)
		for (Rectangle r : exposures) {
			for (int y = r.y1; y < r.y2; y++) {
				int ypos = y - bbox.lowerLeftY;
				int yposR = bbox.height - ypos - 1;
				for (int x = r.x1; x < r.x2; x++) {
					int xpos = x - bbox.lowerLeftX;
					int idx = yposR * bbox.width + xpos;
					sensorRegions[idx]++;
				}
			}
		}

		//
		// Count squares with at least K exposures: O(n)
		return (int) Arrays.stream(sensorRegions).filter(x -> x == k).count();
	}

	// -------------------------------------------------------------------------------------------------------------------------

	public int[] getSensorRegions() {
		return sensorRegions;
	}

	public BoundingBox getBbox() {
		return bbox;
	}
}
