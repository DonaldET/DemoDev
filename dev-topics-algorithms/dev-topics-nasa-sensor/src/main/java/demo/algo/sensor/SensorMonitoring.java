package demo.algo.sensor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * <strong>Problem Definition</strong>
 * <P>
 * Blah
 * <p>
 * <strong>INPUT FORMAT:</strong>
 * <p>
 * The first line of input contains two integer, N and K, such that <em>(1 LE K
 * LE N LE 10**5)</em>. Each of the remaining N lines contains four integers:
 * [x1,y1,x2,y2], describing a rectangular exposed region, with lower-left
 * corner (x1,y1) and upper-right corner (x2,y2). All x and y values are in the
 * range 0..1000, and all rectangles have positive area.
 * <p>
 * <strong>OUTPUT FORMAT:</strong>
 * <p>
 * Please output the area of the Sensor that is exposed by K or more bursts of
 * radiation.
 * <p>
 * <strong>SAMPLE INPUT:</strong>
 * 
 * <pre>
 * <code>
 * 3 2
 * 1 1 5 5
 * 4 4 7 6
 * 3 3 8 7
 * </code>
 * </pre>
 * 
 * SAMPLE OUTPUT:
 * 
 * <pre>
 * <code>
 * 8
 * </code>
 * </pre>
 */
public interface SensorMonitoring {
	public static final int NK_UPPER_BOUND = 100_000;
	public static final int NK_LOWER_BOUND = 1;
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

		public int area() {
			return side(x1, x2) * side(y1, y2);
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
			return getClass().getSimpleName() + "-0x" + Integer.toHexString(hashCode()) + "[(" + x1 + ", " + y1 + "), ("
					+ x2 + ", " + y2 + "); area: " + area() + "]";
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

			if (n < 1) {
				throw new IllegalArgumentException("n small, " + n);
			}
			if (n != sessions.size()) {
				throw new IllegalArgumentException("n (" + n + ") differs from sessions (" + sessions.size());
			}
			if (k < 0 || k > n) {
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
	 * Total ordering over rectangles; up and to the right is further away.
	 * <ol>
	 * <li>lower left X</li>
	 * <li>upper right X</li>
	 * <li>lower left Y</li>
	 * <li>upper right Y</li>
	 * </ol>
	 */
	public static class RectangleComparator<T extends Rectangle> implements Comparator<T> {

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
