package demo.algo.sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import demo.algo.sensor.SensorMonitoring.BoundingBox;
import demo.algo.sensor.SensorMonitoring.Rectangle;

/**
 * Graphics based technique where exposed regions are modeled at the pixel level
 * using an array. Inputs are grouped by chains of overlapping rectangular
 * regions to minimize the size of the pixel map (the portion of the Sensor
 * exposed.) We create the chain of overlap using a Counting Sort {O(n + k)}.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class MonitorExposureOverlapped implements ExposureAreaFinder {

	class State {
		public int rgtHoldingBound = Integer.MIN_VALUE;
		public int area = 0;
	}

	class Lapped {
		public final List<Region> resolved;
		public final boolean isOverlapped;
		public final int lftBound;
		public final int rgtBound;

		public Lapped(List<Region> resolved, boolean isOverlapped, int lftBound, int rgtBound) {
			this.resolved = resolved;
			this.isOverlapped = isOverlapped;
			this.lftBound = lftBound;
			this.rgtBound = rgtBound;
		}
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
		List<Region> holding = new LinkedList<Region>();
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
			// Accumulate overlapping rectangles, complete processing on flush

			if (isNonOverlapping(reg, state.rgtHoldingBound)) {
				state = flushHolding(state, k, holding);
				holding = new LinkedList<Region>();
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

	private State flushHolding(State state, int k, List<Region> holding) {
		state.rgtHoldingBound = Integer.MIN_VALUE;
		int n = holding.size();
		if (n < 1) {
			return state;
		}

		BoundingBox lclBox = SensorMonitoring.findBoundingBox(holding);
		int[] lclSensor = new int[lclBox.width * lclBox.height];
		int lclArea = exposeSensorRegion(lclSensor, lclBox, holding, k);
		state.area += lclArea;

		return state;
	}

	private State mergeIntoHoldings(State state, Rectangle rec, List<Region> holding) {
		Region reg = new Region(rec);
		if (reg.x2 > state.rgtHoldingBound) {
			state.rgtHoldingBound = reg.x2;
		}

		if (holding.isEmpty()) {
			holding.add(new Region(reg));
			return state;
		}

		//
		// Note: holdings are ordered by X1

		holding.add(new Region(reg));

		return state;
	}

	private Lapped overlapp(Region lhs, Region rhs) {
		if (lhs.x1 > rhs.x1) {
			throw new IllegalArgumentException("LHS to the right of RHS ==> LHS: " + lhs + ";  RHS: " + rhs);
		}

		if (rhs.x1 >= lhs.x2 || rhs.y1 >= lhs.y2 || rhs.y2 <= lhs.y1) {
			List<Region> ordered = new LinkedList<Region>();
			ordered.add(lhs);
			ordered.add(rhs);
			return new Lapped(ordered, false, lhs.x1, Math.max(lhs.x2, rhs.x2));
		}

		Lapped result = null;

		return result;
	}

	/**
	 * Expose unit-area regions of the sensor, then find the area meeting the
	 * criteria k.
	 */
	public static int exposeSensorRegion(int[] sensor, BoundingBox bbox, List<Region> regions, int k) {
		for (Region reg : regions) {
			for (int y = reg.y1; y < reg.y2; y++) {
				int ypos = y - bbox.lowerLeftY;
				int yposR = bbox.height - ypos - 1;
				for (int x = reg.x1; x < reg.x2; x++) {
					int xpos = x - bbox.lowerLeftX;
					int idx = yposR * bbox.width + xpos;
					sensor[idx] += reg.exposure;
				}
			}
		}

		return (int) Arrays.stream(sensor).filter(s -> s >= k).count();
	}
}

class Region extends Rectangle {
	public final int exposure;

	public Region(Rectangle rec) {
		this(rec.x1, rec.y1, rec.x2, rec.y2);
	}

	public Region(int x1, int y1, int x2, int y2) {
		this(x1, y1, x2, y2, 1);
	}

	public Region(int x1, int y1, int x2, int y2, int exposure) {
		super(x1, y1, x2, y2);
		this.exposure = exposure;
	}

	public int getExposure() {
		return exposure;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + exposure;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass())
			return false;
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj))
			return false;
		Region other = (Region) obj;
		if (exposure != other.exposure) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder leadin = new StringBuilder(super.toString());
		leadin.append("{exposure: ");
		leadin.append(exposure);
		leadin.append("}");

		return leadin.toString();
	}
}