package don.demo.concurrent.data;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/**
 * <code>DummyGetTask</code> test data
 */
public class DataDefinition {
//  Java 14+
//	public static record Remote(String id, int length, int delay) {
//	}

	/**
	 * Defines a report source - Prior to Java 14
	 */
	public static class Remote {
		private final String id;
		private final int length;
		private final int delay;

		public Remote(String id, int length, int delay) {
			super();
			this.id = id;
			this.length = length;
			this.delay = delay;
		}

		@Override
		public int hashCode() {
			return Objects.hash(delay, id, length);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Remote other = (Remote) obj;
			return delay == other.delay && Objects.equals(id, other.id) && length == other.length;
		}

		@Override
		public String toString() {
			return "Remote [id=" + id + ", length=" + length + ", delay=" + delay + "]";
		}

		public String id() {
			return id;
		}

		public int length() {
			return length;
		}

		public int delay() {
			return delay;
		}
	}

	private static final Random gen = new Random(4337);

	public static final ArrayList<DataDefinition.Remote> remotes = new ArrayList<DataDefinition.Remote>();

	static {
		for (int i = 0; i < 30; i++) {
			remotes.add(new Remote("remote" + i, 2000 + gen.nextInt(2500000), 35 + gen.nextInt(2000)));
		}
	}
}
