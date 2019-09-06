package demo.algo.sensor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import demo.algo.sensor.SensorMonitoring.BoundingBox;
import demo.algo.sensor.SensorMonitoring.Rectangle;
import demo.algo.sensor.SensorMonitoring.RectangleComparator;

/**
 * Graphics based technique where exposed regions are modeled at the pixel level
 * using an array. Inputs are grouped by chains of overlapping rectangular
 * regions to minimize the size of the pixel map (the portion of the Sensor
 * exposed.)
 */
public class MonitorExposureHybrid implements ExposureAreaFinder {

	private static final Rectangle ender = createEnder();
	private int rgtBound = Integer.MIN_VALUE;

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
		if (exposures.size() < 1) {
			return 0;
		}

		List<Rectangle> regions = orderRectangles(exposures);
		regions.add(ender);

		int area = 0;
		LinkedList<Rectangle> holding = new LinkedList<Rectangle>();
		Iterator<Rectangle> itr = regions.iterator();
		Rectangle reg = itr.next();
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

			if (isNonOverlapping(reg, rgtBound)) {
				area = flushHolding(area, k, holding);
				holding = new LinkedList<Rectangle>();
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

	private List<Rectangle> orderRectangles(List<? extends Rectangle> exposures) {
		if (exposures == null | exposures.isEmpty()) {
			throw new IllegalArgumentException("exposures null or empty");
		}

		List<Rectangle> regions = new ArrayList<Rectangle>(exposures.size());
		for (Rectangle rec : exposures) {
			Rectangle pr = new Rectangle(rec.x1, rec.y1, rec.x2, rec.y2);
			regions.add(pr);
		}

		Collections.sort(regions, new RectangleComparator<Rectangle>());

		return regions;
	}

	private boolean isNonOverlapping(Rectangle rhs, int rightBound) {
		// true if RHS is to the right of the LHS
		return rhs.x1 >= rightBound;
	}

	private int flushHolding(int area, int k, List<Rectangle> holding) {
		rgtBound = Integer.MIN_VALUE;
		int n = holding.size();
		if (n < 1) {
			return 0;
		}

		BoundingBox lclBox = SensorMonitoring.findBoundingBox(holding);
		int[] lclSensor = new int[lclBox.width * lclBox.height];
		int lclArea = SensorMonitoring.exposeSensor(lclSensor, lclBox, holding, k);
		area += lclArea;

		return area;
	}

	private void mergeIntoHoldings(Rectangle reg, List<Rectangle> holding) {
		holding.add(reg);
		if (reg.x2 > rgtBound) {
			rgtBound = reg.x2;
		}
	}

	// -------------------------------------------------------------------------------------------------------------------------

	private static Rectangle createEnder() {
		return new Rectangle(SensorMonitoring.XY_UPPER_BOUND - 1, SensorMonitoring.XY_UPPER_BOUND - 1,
				SensorMonitoring.XY_UPPER_BOUND, SensorMonitoring.XY_UPPER_BOUND);
	}
}
