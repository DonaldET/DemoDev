package demo.algo.sensor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import demo.algo.sensor.SensorMonitoring.BoundingBox;
import demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Graphics based technique where exposed regions are modeled at the pixel level
 * using an array. Inputs are grouped by chains of overlapping rectangular
 * regions to minimize the size of the pixel map (the portion of the Sensor
 * exposed.) We create the chain of overlap using a Counting Sort {O(n + k)}.
 */
public class MonitorExposureHybridCS implements ExposureAreaFinder {

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
	 */
	@Override
	public int findArea(List<? extends Rectangle> exposures, final int k) {
		if (exposures == null || exposures.size() < 1) {
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

		if (reg != ender) {
			throw new IllegalStateException("Did not encounter ending record");
		}

		return state.area;
	}

	private List<Rectangle> orderRectangles(List<? extends Rectangle> exposures) {
		Rectangle[] countingSorted = SensorMonitoring.countingSort(exposures, SensorMonitoring.XY_UPPER_BOUND);
		List<Rectangle> sorted = new ArrayList<Rectangle>(countingSorted.length);
		for (int i = 0; i < countingSorted.length; i++) {
			sorted.add(countingSorted[i]);
		}
		return sorted;
	}

	private boolean isNonOverlapping(Rectangle rhs, int rightBound) {
		// true if RHS left edge is to the right of the LHS right edge
		return rhs.x1 >= rightBound;
	}

	private State flushHolding(State state, int k, List<Rectangle> holding) {
		state.rgtHoldingBound = Integer.MIN_VALUE;
		int n = holding.size();
		if (n < 1) {
			return state;
		}

		BoundingBox lclBox = SensorMonitoring.findBoundingBox(holding);
		int[] lclSensor = new int[lclBox.width * lclBox.height];
		int lclArea = SensorMonitoring.exposeSensor(lclSensor, lclBox, holding, k);
		state.area += lclArea;

		return state;
	}

	private State mergeIntoHoldings(State state, Rectangle reg, List<Rectangle> holding) {
		holding.add(reg);
		if (reg.x2 > state.rgtHoldingBound) {
			state.rgtHoldingBound = reg.x2;
		}

		return state;
	}
}
