package demo.algo.sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import demo.algo.sensor.SensorMonitoring.BoundingBox;
import demo.algo.sensor.SensorMonitoring.Rectangle;
import demo.algo.sensor.SensorMonitoring.RectangleComparator;

/**
 * Graphics based technique where exposed regions are modeled at the pixel level
 * using an array. Inputs are grouped by chains of overlapping regions to
 * minimize the size of the pixel map (the Sensor.)
 */
public class MonitorExposureOverlapped implements ExposureAreaFinder {

	private static final Region ender = createEnder();

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
		List<Region> regions = orderInputRectangles(exposures, ender);

		int area = 0;
		LinkedList<Region> holding = new LinkedList<Region>();
		Iterator<Region> itr = regions.iterator();
		Region reg = itr.next();
		mergeIntoHoldings(reg, holding);

		while (itr.hasNext()) {
			reg = itr.next();
			if (reg == ender) {
				area = flushHolding(area, k, holding);
				holding = null;
				break;
			}

			//
			// Accumulate overlapping rectangles, process on flush

			if (isNonOverlapping(holding.getLast(), reg)) {
				area = flushHolding(area, k, holding);
				holding = new LinkedList<Region>();
			}

			//
			// Merge the new rectangle into the current holding
			mergeIntoHoldings(reg, holding);
		}

		if (reg != ender) {
			throw new IllegalStateException("Did not encounter ending record");
		}

		return area;
	}

	private List<Region> orderInputRectangles(List<? extends Rectangle> exposures, Region ender) {
		if (exposures == null | exposures.isEmpty()) {
			throw new IllegalArgumentException("exposures null or empty");
		}

		List<Region> regions = new ArrayList<Region>(exposures.size());
		for (Rectangle rec : exposures) {
			Region pr = new Region(rec.x1, rec.y1, rec.x2, rec.y2, 1);
			regions.add(pr);
		}

		Collections.sort(regions, new RectangleComparator<Region>());
		regions.add(ender);

		return regions;
	}

	private boolean isNonOverlapping(Region lhs, Region rhs) {
		// true if RHS is to the right of the LHS
		return rhs.x1 >= lhs.x2;
	}

	private int flushHolding(int area, int k, List<Region> holding) {
		BoundingBox lclBox = SensorMonitoring.findBoundingBox(holding);
		int[] lclSensor = new int[(lclBox.width) * (lclBox.height)];
		int lclArea = exposeSensor(lclSensor, lclBox, holding, k);
		area += lclArea;
		return area;
	}

	private void mergeIntoHoldings(Region reg, List<Region> holding) {
		holding.add(reg);
	}

	private int exposeSensor(int[] sensor, BoundingBox bbox, List<Region> regions, int k) {
		for (Region reg : regions) {
			for (int y = reg.y1; y < reg.y2; y++) {
				int ypos = y - bbox.lowerLeftY;
				int yposR = bbox.height - ypos - 1;
				for (int x = reg.x1; x < reg.x2; x++) {
					int xpos = x - bbox.lowerLeftX;
					int idx = yposR * bbox.width + xpos;
					sensor[idx] += reg.exposures;
				}
			}
		}

		return (int) Arrays.stream(sensor).filter(r -> r == k).count();
	}

	// -------------------------------------------------------------------------------------------------------------------------

	private static Region createEnder() {
		return new Region(SensorMonitoring.XY_UPPER_BOUND - 1, SensorMonitoring.XY_UPPER_BOUND - 1,
				SensorMonitoring.XY_UPPER_BOUND, SensorMonitoring.XY_UPPER_BOUND, Integer.MAX_VALUE);
	}

	// -------------------------------------------------------------------------------------------------------------------------

	private static class Region extends Rectangle {
		public int exposures;

		public Region(int x1, int y1, int x2, int y2, int exposures) {
			super(x1, y1, x2, y2);
			this.exposures = exposures;
		}

		@Override
		public String toString() {
			return super.toString() + "[exposures=" + exposures + "]";
		}
	}
}
