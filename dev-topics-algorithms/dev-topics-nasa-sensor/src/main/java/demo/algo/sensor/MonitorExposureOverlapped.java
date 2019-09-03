package demo.algo.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Overlapping rectangles-based technique where exposed regions are tracked.
 */
public class MonitorExposureOverlapped implements ExposureAreaFinder {

	private static final Region ender = createEnder();
	private Region lastMerged = null;

	public static void main(String[] args) {
	}

	/**
	 * Time complexity is O(n).
	 */
	@Override
	public int findArea(List<? extends Rectangle> exposedRegions, final int k) {
		//
		// Create ordered regions: O(n * log(n))
		List<Region> regions = orderInputRectangles(exposedRegions, ender);

		int area = 0;
		LinkedList<Region> holding = new LinkedList<Region>();
		Iterator<Region> itr = regions.iterator();
		Region reg = itr.next();
		holding.add(reg);
		lastMerged = reg;

		while (itr.hasNext()) {
			//
			// Hit the end?
			reg = itr.next();
			if (reg.equals(ender)) {
				area = flushHolding(area, k, holding);
				break;
			}

			//
			// New rectangle outside last holding rectangle, then overlap analysis
			if (isNonOverlapping(lastMerged, reg)) {
				area = flushHolding(area, k, holding);
				holding = new LinkedList<Region>();
				holding.add(reg);
				lastMerged = reg;
			} else {
				//
				// Merge the new rectangle into the current holding
				mergeIntoHoldings(reg, holding);
			}
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
		int matchingExposures = holding.stream().filter(r -> r.exposures == k).mapToInt(r -> r.area()).sum();
		return area + matchingExposures;
	}

	private void mergeIntoHoldings(Region reg, List<Region> holding) {
		lastMerged = reg;
		holding.add(reg);
	}

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
	 * Total ordering over rectangles;
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
