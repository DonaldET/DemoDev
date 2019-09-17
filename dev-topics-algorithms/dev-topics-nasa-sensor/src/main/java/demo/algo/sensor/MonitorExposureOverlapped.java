package demo.algo.sensor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

	static class State {
		public int rgtHoldingBound = Integer.MIN_VALUE;
		public int area = 0;

		@Override
		public String toString() {
			return "[" + getClass().getSimpleName() + " - 0x" + Integer.toHexString(hashCode()) + "; rgtHoldingBound: "
					+ rgtHoldingBound + ", area: " + area + "]";
		}
	}

	static class Region extends Rectangle {
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

		public int area() {
			return (x2 - x1) * (y2 - y1);
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
			leadin.append(", area: ");
			leadin.append(area());
			leadin.append("}");

			return leadin.toString();
		}
	}

	static class IntervalLap {
		public final int a;
		public final int b;

		public IntervalLap(int a, int b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public String toString() {
			return "[" + getClass().getSimpleName() + " - 0x" + Integer.toHexString(hashCode()) + "; (a: " + a + ", b: "
					+ b + ")]";
		}
	}

	static class Lapped {
		public final boolean isOverlapped;
		public final List<Region> resolved;

		public Lapped(boolean isOverlapped, List<Region> resolved) {
			this.isOverlapped = isOverlapped;
			this.resolved = resolved;
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
		if (exposures.isEmpty()) {
			return 0;
		}

		List<Rectangle> regions = orderRectangles(exposures);
		regions.add(ender);

		State state = new State();
		List<Region> holding = new LinkedList<Region>();
		Iterator<Rectangle> itr = regions.iterator();
		Rectangle rec = itr.next();
		state = mergeIntoHoldings(state, rec, k, holding);

		while (itr.hasNext()) {
			rec = itr.next();
			if (rec == ender) {
				state = flushHolding(state, k, holding);
				holding = null;
				break;
			}

			//
			// Accumulate overlapping rectangles, complete processing on flush

			if (isNonOverlapping(rec, state.rgtHoldingBound)) {
				state = flushHolding(state, k, holding);
				holding = new LinkedList<Region>();
			}

			//
			// Merge the new rectangle into the current holding

			state = mergeIntoHoldings(state, rec, k, holding);
		}

		if (rec != ender || holding != null) {
			throw new IllegalStateException("Did not encounter ending record, or did not clear holdings");
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
		if (holding.isEmpty()) {
			return state;
		}

		int lclArea = holding.stream().filter(h -> h.exposure >= k).mapToInt(h -> h.area()).sum();
		state.area += lclArea;

		return state;
	}

	/**
	 * Merge new rectangle into correct location in holdings, handling overlap
	 * during the merge.
	 * <p>
	 * 
	 * @param state   the current holding state (area and right edge)
	 * @param rec     the rectangle to merge into holdingd
	 * @param k       the exposure saturation limit defining exposed ares
	 * @param holding the current list of exposed regions, ordered ascending by X1
	 * @return new state after merging in the new rectangle
	 * 
	 *         <p>
	 *         <strong>Pre-conditions:</strong>
	 *         <ul>
	 *         <li>successive input (<em>rec</em>) is always ordered ascending in
	 *         X1</li>
	 *         <li>input is always part of holding, but may not overlap</li>
	 *         <li>input is placed just to the right of the last holding entry with
	 *         holding.x1 <= input.x1</li>
	 *         <li>the holding collection is always subject to overlap</li>
	 *         </ul>
	 */
	private State mergeIntoHoldings(State state, Rectangle rec, int k, List<Region> holding) {
		Region region = new Region(rec);
		if (region.x2 > state.rgtHoldingBound) {
			state.rgtHoldingBound = region.x2;
		}

		if (holding.isEmpty()) {
			holding.add(new Region(region));
			return state;
		}

		boolean addedRegion = false;
		int pos = 0;
		while (pos < holding.size()) {
			Region cur = holding.get(pos);
			if (rec.x1 > cur.x2) {
				//
				// No further overlap, link in and continue

				pos++;
				holding.add(pos, region);
				pos++;
				addedRegion = true;
				break;
			}

			Lapped overlap = overlap(region, cur);
			if (!overlap.isOverlapped) {
				pos++;
				continue;
			}

			int n = overlap.resolved.size();
			if (n < 1) {
				throw new IllegalStateException("expected overlap at " + pos + " for " + cur);
			}

			holding.remove(pos);
			for (int i = n - 1; i >= 0; i--) {
				holding.add(pos, overlap.resolved.get(i));
			}

			pos += n;
		}

		if (!addedRegion) {
			holding.add(region);
		}

		return state;
	}

	/**
	 * Output priorBurst as a collection of regions that include the overlap;
	 * proceeding left to right
	 * 
	 * @param newBurst   radiation exposure added to a region of sensor having a
	 *                   prior exposure
	 * @param priorBurst a sensor region already exposed
	 *
	 * @return a list of updated regions of the prior burst ordered in X1
	 */
	private Lapped overlap(Region newBurst, Region priorBurst) {
		if (priorBurst.x1 >= newBurst.x2 || newBurst.x1 >= priorBurst.x2 || priorBurst.y1 >= newBurst.y2
				|| priorBurst.y2 <= newBurst.y1) {
			return new Lapped(false, new LinkedList<Region>());
		}

		IntervalLap dx = linearOverlap(newBurst.x1, newBurst.x2, priorBurst.x1, priorBurst.x2);
		IntervalLap dy = linearOverlap(newBurst.y1, newBurst.y2, priorBurst.y1, priorBurst.y2);

		LinkedList<Region> exposed = new LinkedList<Region>();
		int pos = priorBurst.x1;
		if (pos < dx.a) {
			exposed.add(new Region(pos, priorBurst.y1, dx.a, priorBurst.y2, priorBurst.exposure));
			pos = dx.a;
		}

		exposed.add(new Region(pos, dy.a, dx.b, dy.b, priorBurst.exposure + 1));
		pos = dx.b + 1;

		if (pos < priorBurst.x2) {
			exposed.add(new Region(pos, priorBurst.y1, priorBurst.x2, priorBurst.y2, priorBurst.exposure));
		}

		return new Lapped(true, exposed);
	}

	private IntervalLap linearOverlap(int a1, int b1, int a2, int b2) {
		int a = Math.max(a1, a2);
		int b = Math.min(b1, b2);

		if (a >= b) {
			String msg = "Overlap Error! [Params: a: (" + a1 + ", " + b1 + "); b: (" + a2 + ", " + b2 + ") ==> (" + a
					+ ", " + b + ")]";
			throw new IllegalArgumentException(msg);
		}

		return new IntervalLap(a, b);
	}
}
