package don.demo.dataservices.test;

import don.demo.mockdata.MockDataGenerator;

public class AbstractAccessorTest {
	public static final int N_TEST_GROUPS = 200;
	public static final int N_TEST_ITEMS = 7000;

	private static final boolean[] initialized = { false };
	static {
		synchronized (initialized) {
			if (!initialized[0]) {
				initialized[0] = true;
				long elapsed = MockDataGenerator.initialize(false, false, N_TEST_GROUPS, N_TEST_ITEMS);
				System.out.println("Mock data generated in " + elapsed / MockDataGenerator.NANO_TO_MILLI
						+ MockDataGenerator.MILLI_SECONDS);
			}
		}
	}

	protected long checkSum() {
		int csGroups = MockDataGenerator.groups.values().stream().map(s -> s.hashCode()).reduce(0, Integer::sum);
		int csItems = MockDataGenerator.items.values().stream().map(s -> s.hashCode()).reduce(0, Integer::sum);
		int csItemStrings = MockDataGenerator.itemStrings.values().stream().map(s -> s.hashCode()).reduce(0,
				Integer::sum);

		return (long) csGroups + (long) csItems + (long) csItemStrings;
	}
}
