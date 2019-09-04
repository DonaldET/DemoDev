package demo.algo.sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import demo.algo.sensor.SensorMonitoring.BoundingBox;
import demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Graphics based technique where exposed regions are modeled at the pixel level
 * using an array. Inputs are grouped by chains of overlapping regions to
 * minimize the size of the pixel map (the Sensor.)
 */
public class MonitorExposureHybrid implements ExposureAreaFinder {

	private int mergedCount = 0;
	private int processedCount = 0;
	private static final Region ender = createEnder();

	private BoundingBox lclBox = null;
	private int[] lclSensor = null;

	private BoundingBox gblBox = null;
	private int[] gblSensor = null;

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
		gblBox = SensorMonitoring.findBoundingBox(regions);
		gblSensor = new int[(gblBox.width + 1) * (gblBox.height + 1)];
		int masterArea = updateSensor(gblSensor, gblBox, regions, k);
		gblSensor = new int[(gblBox.width + 1) * (gblBox.height + 1)];

		int area = 0;
		LinkedList<Region> holding = new LinkedList<Region>();
		Iterator<Region> itr = regions.iterator();
		Region reg = itr.next();
		mergeIntoHoldings(reg, holding);

		processedCount = 1;
		while (itr.hasNext()) {
			reg = itr.next();
			processedCount++;
			if (reg == ender) {
				area = flushHolding(area, k, holding);
				System.out.println(" - - - Done at " + processedCount + "; merged " + mergedCount);
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
			throw new IllegalStateException("Did not encounter ending record; processed " + processedCount);
		}

		if (masterArea != area) {
			System.out.println(" . . . did " + processedCount + "; global area: " + area + " for " + regions.size()
					+ " entries, expected " + masterArea);
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
		lclBox = SensorMonitoring.findBoundingBox(holding);
		lclSensor = new int[(lclBox.width + 1) * (lclBox.height + 1)];
		int lclArea = updateSensor(lclSensor, lclBox, holding, k);
		lclSensor = null;

		System.out.println(
				"\n . . . at " + processedCount + "; local area: " + lclArea + " for " + holding.size() + " entries.");

		area += lclArea;
		int gblArea = updateSensor(gblSensor, gblBox, holding, k);
		if (area != gblArea) {
			System.out.println(" . . . at " + processedCount + "; global area: " + gblArea + " for " + holding.size()
					+ " entries, expected " + area);
		}

		return area;
	}

	private void mergeIntoHoldings(Region reg, List<Region> holding) {
		mergedCount++;
		holding.add(reg);
	}

	private int updateSensor(int[] sensor, BoundingBox bbox, List<Region> regions, int k) {
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

		return Arrays.stream(sensor).filter(r -> r == k).sum();
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

	/**
	 * Total ordering over rectangles; up and to the right is further away.
	 * <ol>
	 * <li>lower left X</li>
	 * <li>lower left Y</li>
	 * <li>upper right X</li>
	 * <li>upper right Y</li>
	 * </ol>
	 */
	private static class RectangleComparator<T extends Rectangle> implements Comparator<T> {

		public RectangleComparator() {
		}

		@Override
		public int compare(T lhs, T rhs) {
			int delta = lhs.x1 - rhs.x1;
			if (delta == 0) {
				delta = lhs.y1 - rhs.y1;
				if (delta == 0) {
					delta = lhs.x2 - rhs.x2;
					if (delta == 0) {
						delta = lhs.y2 - rhs.y2;
					}
				}
			}
			return delta;
		}
	}
}
