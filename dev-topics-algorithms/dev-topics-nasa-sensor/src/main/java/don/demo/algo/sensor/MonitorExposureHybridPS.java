package don.demo.algo.sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import don.demo.algo.sensor.SensorMonitoring.BoundingBox;
import don.demo.algo.sensor.SensorMonitoring.Rectangle;
import don.demo.algo.sensor.SensorMonitoring.RectangleComparator;

/**
 * Graphics based technique where exposed regions are modeled at the pixel level
 * using an array. Inputs are grouped by chains of overlapping rectangular
 * regions to minimize the size of the pixel map (the portion of the Sensor
 * exposed.) We create the chain of overlap using array parallel sort {O(n +
 * k)}.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class MonitorExposureHybridPS implements ExposureAreaFinder {

	class State {
		public int rgtHoldingBound = Integer.MIN_VALUE;
		public int area = 0;
	}

	private static final Rectangle ender = SensorMonitoring.createEnder();

	public static void main(String[] args) {
	}

	/**
	 * Time complexity is O(n * a), where n = number of exposure sessions, and a is
	 * a measure of algorithm effort in a session (e.g., the mean area exposed per
	 * session.) If the exposed area is relatively fixed and small, and the number
	 * of sessions n is large, then the complexity is, by definition, O(n).
	 * <p>
	 * Overlapping regions attempts to make each region smaller, thereby lowering
	 * the number of squares needed to represent the exposures. However, it
	 * introduces the need for sorting the input (an O(n * log(n)) operation, but
	 * done in parallel).
	 */
	@Override
	public int findArea(List<? extends Rectangle> exposures, final int k) {
		if (exposures.isEmpty()) {
			return 0;
		}

		List<Rectangle> regions = orderRectangles(exposures);
		regions.add(ender);

		State state = new State();
		List<Rectangle> holding = new ArrayList<Rectangle>();
		Iterator<Rectangle> itr = regions.iterator();
		Rectangle reg = itr.next();
		state = mergeIntoHoldings(state, reg, holding);

		while (itr.hasNext()) {
			reg = itr.next();
			if (reg == ender) {
				state = flushHolding(state, k, holding);
				holding = null;
				break;
			}

			//
			// Accumulate overlapping rectangles, process on flush

			if (isNonOverlapping(reg, state.rgtHoldingBound)) {
				state = flushHolding(state, k, holding);
				holding = new ArrayList<Rectangle>();
			}

			//
			// Merge the new rectangle into the current holding

			state = mergeIntoHoldings(state, reg, holding);
		}

		if (reg != ender || holding != null) {
			throw new IllegalStateException("Did not encounter ending record, or did not clear holdings");
		}

		return state.area;
	}

	private List<Rectangle> orderRectangles(List<? extends Rectangle> exposures) {
		if (exposures == null) {
			return new ArrayList<Rectangle>();
		}

		final int n = exposures.size();
		List<Rectangle> sorted = new ArrayList<Rectangle>(n);
		if (n < 2) {
			sorted.add(exposures.get(0));
		}

		Rectangle[] parallelSorted = new Rectangle[n];
		for (int i = 0; i < parallelSorted.length; i++) {
			parallelSorted[i] = exposures.get(i);
		}
		Arrays.parallelSort(parallelSorted, new RectangleComparator<Rectangle>());

		for (int i = 0; i < n; i++) {
			sorted.add(parallelSorted[i]);
		}
		return sorted;
	}

	private boolean isNonOverlapping(Rectangle rhs, int rightBound) {
		// true if RHS left edge is to the right of the LHS right edge
		return rhs.x1 >= rightBound;
	}

	private State flushHolding(State state, int k, List<Rectangle> holding) {
		state.rgtHoldingBound = Integer.MIN_VALUE;
		if (holding.isEmpty()) {
			return state;
		}

		BoundingBox lclBox = SensorMonitoring.findBoundingBox(holding);
		int[] lclSensor = new int[lclBox.width * lclBox.height];
		int lclArea = SensorMonitoring.exposeSensor(lclSensor, lclBox, holding, k);
		state.area += lclArea;

		return state;
	}

	private State mergeIntoHoldings(State state, Rectangle rec, List<Rectangle> holding) {
		holding.add(rec);
		if (rec.x2 > state.rgtHoldingBound) {
			state.rgtHoldingBound = rec.x2;
		}

		return state;
	}
}
