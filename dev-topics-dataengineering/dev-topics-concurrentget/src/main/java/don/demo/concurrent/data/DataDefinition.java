package don.demo.concurrent.data;

import java.util.ArrayList;
import java.util.Random;

/**
 * <code>DummyGetTask</code> test data
 */
public class DataDefinition {
	public static record Remote(String id, int length, int delay) {
	}

	private static final Random gen = new Random(4337);

	public static final ArrayList<DataDefinition.Remote> remotes = new ArrayList<DataDefinition.Remote>();

	static {
		for (int i = 0; i < 30; i++) {
			remotes.add(new Remote("remote" + i, 2000 + gen.nextInt(2500000), 35 + gen.nextInt(2000)));
		}
	}
}
