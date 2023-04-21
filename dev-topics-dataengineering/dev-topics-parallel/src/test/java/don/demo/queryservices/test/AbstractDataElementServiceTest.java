package don.demo.queryservices.test;

import java.util.Comparator;

import don.demo.dataservices.GroupsAccessor.GroupIdentification;
import don.demo.dataservices.ItemStringAcccessor.ItemStrings;
import don.demo.mockdata.MockDataGenerator;

public abstract class AbstractDataElementServiceTest {
	public static final int N_TEST_GROUPS = 200;
	public static final int N_TEST_ITEMS = 7000;

	private static final boolean[] initialized = { false };
	public static MockDataGenerator mockData;
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

	class SortByGroup implements Comparator<GroupIdentification> {

		public int compare(GroupIdentification a, GroupIdentification b) {
			int d = a.groupId() - b.groupId();
			if (d == 0) {
				d = a.segmentId() - b.segmentId();
			}
			return d;
		}
	}

	class SortByCompoundKey implements Comparator<ItemStrings> {
		public int compare(ItemStrings a, ItemStrings b) {
			int d = (int) (a.itemStrid() - b.itemStrid());
			return (d == 0) ? (int) (a.recordid() - b.recordid()) : d;
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