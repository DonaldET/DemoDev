package demo.algo.sensor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * <strong>Problem Definition</strong>
 * <p>
 * We have a square radiation sensor with an overlaid grid of exposure regions,
 * with the lower left grid as the origin. The positive X direction, to the
 * right of the origin, is labeled 0 through 1000. Similarly, the positive Y
 * direction is upward from the origin, and is also labeled 0 through 1000. The
 * value 1000 is symbolically named <strong>XY_UPPER_BOUND</strong>. The grid
 * uses arbitrary length units (e.g., microns) and defines a logical partition
 * of the physical sensor area. An exposed area is a rectangle defined by the
 * coordinates of its lower left and upper right corners {(x1, y1) to (x2, y2)}.
 * <p>
 * We validate the sensor using a radiation generator that emits rapid, random
 * bursts of radiation falling on a rectangular region of the sensor. A sensor
 * in use is expected to receive no more than a million bursts a second. The
 * measurement period will encounter <strong>N</strong> total radiation bursts.
 * If the accumulated bursts reach or exceed a threshold of <strong>K</strong>
 * bursts over a monitoring period, then the sensor will &quot;white out&quot;
 * for a period of time. For example, more than two bursts per millisecond in a
 * grid square temporarily &quot;blinds&quot; that square.
 * <p>
 * Ideally, we are able to compute the exposure level by area (bursts impinging
 * on a sensor region) in sufficient time for the filter to be deployed. For
 * example, we need to deploy the filter if we detect that 60% (600K squares) of
 * the exposed sensor area has reached a critical value. That is areas exceeding
 * the threshold of <strong>K</strong> bursts of radiation after
 * <strong>N</strong> exposures.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface SensorMonitoring {
	public static final int XY_UPPER_BOUND = 1000;
	public static final int XY_LOWER_BOUND = 0;

	/**
	 * A radiation-exposed rectangle with ordering on the XY plain; this rectangle
	 * represents one exposure session.
	 */
	public static class Rectangle {
		public final int x1; // Lower left
		public final int y1;
		public final int x2; // Upper right
		public final int y2;

		public Rectangle(int x1, int y1, int x2, int y2) {
			super();
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
			validate();
		}

		private int side(int lleft, int uright) {
			return uright - lleft;
		}

		private void validate() {
			if (x1 < XY_LOWER_BOUND || x1 > XY_UPPER_BOUND || y1 < XY_LOWER_BOUND || y1 > XY_UPPER_BOUND) {
				throw new IllegalArgumentException("lower left bounds error; " + this.toString());
			}

			if (x2 < XY_LOWER_BOUND || x2 > XY_UPPER_BOUND || y2 < XY_LOWER_BOUND || y2 > XY_UPPER_BOUND) {
				throw new IllegalArgumentException("upper right bounds error; " + this.toString());
			}

			if (side(x1, x2) <= 0 || side(y1, y2) <= 0) {
				throw new IllegalArgumentException("area violation; " + this.toString());
			}
		}

		@Override
		public int hashCode() {
			final int prime = 4273;
			int result = 1;
			result = prime * result + x1;
			result = prime * result + x2;
			result = prime * result + y1;
			result = prime * result + y2;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Rectangle other = (Rectangle) obj;
			if (x1 != other.x1)
				return false;
			if (x2 != other.x2)
				return false;
			if (y1 != other.y1)
				return false;
			if (y2 != other.y2)
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "[" + getClass().getSimpleName() + " - 0x" + Integer.toHexString(hashCode()) + "; (" + x1 + ", " + y1
					+ "), (" + x2 + ", " + y2 + ")]";
		}
	}

	/**
	 * Total ordering over rectangles; we are only concerned about x dimension
	 * overlap.
	 */
	public static class RectangleComparator<T extends Rectangle> implements Comparator<T> {

		public RectangleComparator() {
		}

		@Override
		public int compare(T lhs, T rhs) {
			return lhs.x1 - rhs.x1;
		}
	}

	/**
	 * This collection represents inputs defining a radiation exposure session from
	 * which we seek the rectangles exposed at least K times. Each rectangle
	 * represents one exposure.
	 */
	public static class ExposureSession {
		public final int n;
		public final int k;
		public final int expectedArea;
		public final List<Rectangle> sessions;

		public ExposureSession(int n, int k, int expectedArea, List<Rectangle> sessions) {
			super();
			this.n = n;
			this.k = k;
			this.expectedArea = expectedArea;
			this.sessions = sessions;

			if (n < 0) {
				throw new IllegalArgumentException("n small, " + n);
			}
			if (n != sessions.size()) {
				throw new IllegalArgumentException(
						"n (" + n + ") differs from actual sessions (" + sessions.size() + ")");
			}
			if (k < 0 || (n > 0 && k > n)) {
				throw new IllegalArgumentException("exposures (" + k + ") is out of bounds [1-" + n + "]");
			}
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder(
					"[" + getClass().getSimpleName() + " - 0x" + Integer.toHexString(hashCode()) + "[n: " + n + ", k: "
							+ k + ", expected area: " + expectedArea + ", sessions:");
			int n = sessions.size();
			if (n < 1) {
				sb.append(" empty]");
			} else {
				for (int i = 0; i < n; i++) {
					if (i > 0) {
						sb.append(",");
					}
					sb.append("\n\t");
					sb.append(i + 1);
					sb.append(". ");
					sb.append(sessions.get(i));
				}
				sb.append("\n]");
			}
			return sb.toString();
		}
	}

	public static class BoundingBox {
		public final int lowerLeftX;
		public final int lowerLeftY;
		public final int width;
		public final int height;

		public BoundingBox(int lowLftX, int lowLftY, int width, int height) {
			super();
			this.lowerLeftX = lowLftX;
			this.lowerLeftY = lowLftY;
			this.width = width;
			this.height = height;
		}

		@Override
		public String toString() {
			return "[BoundingBox - 0x" + Integer.toHexString(hashCode()) + "; lowerLeft: (" + lowerLeftX + ", "
					+ lowerLeftY + "), {" + width + " x " + height + "}, area=" + width * height + "]";
		}
	}

	/**
	 * Find the smallest rectangle that encloses all rectangles in the exposure
	 * list.
	 * 
	 * @param exposures a collection of exposed rectangles
	 * @return the smallest rectangle that contains all the regions
	 */
	public static BoundingBox findBoundingBox(List<? extends Rectangle> exposures) {
		if (exposures == null) {
			throw new IllegalArgumentException("exposures null");
		}
		if (exposures.isEmpty()) {
			throw new IllegalArgumentException("exposures empty");
		}

		int lowLftX = Integer.MAX_VALUE;
		int lowLftY = Integer.MAX_VALUE;
		int upRgtX = Integer.MIN_VALUE;
		int upRgtY = Integer.MIN_VALUE;
		int i = 0;
		for (Rectangle r : exposures) {
			if (r == null) {
				throw new IllegalArgumentException("Rectange[" + i + "] null");
			}
			i++;

			if (r.x1 < lowLftX) {
				lowLftX = r.x1;
			}
			if (r.y1 < lowLftY) {
				lowLftY = r.y1;
			}
			if (r.x2 > upRgtX) {
				upRgtX = r.x2;
			}
			if (r.y2 > upRgtY) {
				upRgtY = r.y2;
			}
		}

		return new BoundingBox(lowLftX, lowLftY, upRgtX - lowLftX, upRgtY - lowLftY);
	}

	/**
	 * Expose unit-area regions of the sensor, then find the area meeting the
	 * criteria k.
	 * 
	 * @param sensor  the array representing exposure counts for the [X][Y] to
	 *                [index] mapping
	 * @param bbox    the smallest rectangular region that contains the exposed
	 *                rectangles
	 * @param regions the collection of exposed-region rectangles
	 * @param k       the critical exposure value
	 * @return the area of sensor exposed to at least <code>k</code> radiation
	 *         bursts
	 */
	public static int exposeSensor(int[] sensor, BoundingBox bbox, List<? extends Rectangle> regions, int k) {
		for (Rectangle reg : regions) {
			for (int y = reg.y1; y < reg.y2; y++) {
				int ypos = y - bbox.lowerLeftY;
				int yposR = bbox.height - ypos - 1;
				for (int x = reg.x1; x < reg.x2; x++) {
					int xpos = x - bbox.lowerLeftX;
					int idx = yposR * bbox.width + xpos;
					sensor[idx]++;
				}
			}
		}

		return (int) Arrays.stream(sensor).filter(s -> s >= k).count();
	}

	/**
	 * Create end-of-list marker for rectangles.
	 * 
	 * @return a rectangle used to identify the end of a list of exposure rectangles
	 */
	public static Rectangle createEnder() {
		return new Rectangle(SensorMonitoring.XY_UPPER_BOUND - 1, SensorMonitoring.XY_UPPER_BOUND - 1,
				SensorMonitoring.XY_UPPER_BOUND, SensorMonitoring.XY_UPPER_BOUND);
	}

	/**
	 * Sort the input by rectangle position X1
	 * 
	 * @param input the list of rectangles to be ordered ascending by X1
	 * @param max_x the value of the largest key, keys are numbered 0 through
	 *              <code>x_max</code>
	 * @return an ordered copy of the input
	 */
	public static Rectangle[] countingSort(List<? extends Rectangle> input, int max_x) {
		final int n = input.size();
		Rectangle[] output = new Rectangle[n];
		if (n < 1) {
			return output;
		} else if (n < 2) {
			output[0] = input.get(0);
			return output;
		}

		//
		// Count each rectangle Lower Left X value

		int[] count = new int[max_x + 1];
		for (Rectangle r : input) {
			count[r.x1]++;
		}

		//
		// Counts are accumulated to obtain positions

		for (int i = 1; i <= max_x; ++i)
			count[i] += count[i - 1];

		//
		// Build the output rectangle list

		for (int i = 0; i < n; ++i) {
			Rectangle inputRect = input.get(i);
			output[count[inputRect.x1] - 1] = inputRect;
			--count[inputRect.x1];
		}

		return output;
	}
}
