package don.demo.mockdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import don.demo.dataservices.GroupsAccessor.Groups;
import don.demo.dataservices.ItemStringAcccessor.ItemStrings;
import don.demo.dataservices.ItemsAccessor.Items;

/**
 * Generate the mock data for testing that meets the data model and data access
 * criteria.
 * <p>
 * This class is a static class implementation accessed by source code
 * dependency, and is expected to be initialized prior to use and treated as
 * immutable.
 * <p>
 * Proper use of an initialized version of this class is thread safe for reading
 * data.
 */
public class MockDataGenerator {
	public static final String MILLI_SECONDS = " milli-seconds.";

	public static final double NANO_TO_MILLI = 1000000.0;

	private static Random r = null;

	public static final Map<Integer, Groups> groups = new HashMap<>();
	public static int maxGroupId = 0;
	public static int maxGroupSegmentId = 0;
	public static long minGroupStartDate = Long.MAX_VALUE;
	public static long maxGroupStartDate = Long.MIN_VALUE;

	public static final Map<Integer, Items> items = new HashMap<>();
	public static long maxItemId = 0;
	public static long minItmDate = Long.MAX_VALUE;
	public static long maxItmDate = Long.MIN_VALUE;

	public static final Map<Long, List<ItemStrings>> itemStrings = new HashMap<>();
	public static long maxItemStrId = 0;

	public static void main(String[] args) {
		initialize(true, true, Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}

	public static long initialize(boolean showSummaries, boolean showContent, int ngroups, int nItems) {
		r = new Random(3373);
		if (showSummaries) {
			System.out.println("== Generate Mock Data");
		}

		// Groups
		if (showSummaries) {
			System.out.println("\nGROUPS table with " + ngroups + " basic loops");
		}
		long elapsed = MockDataGenerator.buildGroups(ngroups);
		long totalElapsed = elapsed;
		if (showSummaries) {
			System.out.println("  -- Created " + MockDataGenerator.groups.size() + " records in "
					+ elapsed / MockDataGenerator.NANO_TO_MILLI + MockDataGenerator.MILLI_SECONDS);
			System.out.println("  -- Max GroupId   : " + MockDataGenerator.maxGroupId);
			System.out.println("  -- Max SegmentId : " + MockDataGenerator.maxGroupSegmentId);
			System.out.println("  -- Min Start Date: " + MockDataGenerator.minGroupStartDate);
			System.out.println("  -- Max Start Date: " + MockDataGenerator.maxGroupStartDate);
			System.out.println("--------------------------");
		}
		if (showContent) {
			groups.keySet().stream().filter(s -> groups.get(s).groupId() % 25 == 1)
					.forEach(g -> System.out.println(g + " .. " + groups.get(g)));
			System.out.println(maxGroupId + " .. " + groups.get(maxGroupId));
		}

		// Items
		if (showSummaries) {
			System.out.println("\nItems table with " + nItems + " basic loops");
		}
		elapsed = MockDataGenerator.buildItems(nItems);
		totalElapsed += elapsed;
		if (showSummaries) {
			System.out.println("  -- Created " + MockDataGenerator.items.size() + " ITEMS records in "
					+ elapsed / MockDataGenerator.NANO_TO_MILLI + MockDataGenerator.MILLI_SECONDS);
			System.out.println("  -- Max ItemId   : " + MockDataGenerator.maxItemId);
			System.out.println("  -- Min Item Date: " + MockDataGenerator.minItmDate);
			System.out.println("  -- Max Item Date: " + MockDataGenerator.maxItmDate);
			System.out.println("--------------------------");
		}
		if (showContent) {
			items.keySet().stream().filter(s -> items.get(s).itemId() % 250 == 1)
					.forEach(g -> System.out.println(g + " .. " + items.get(g)));
			System.out.println(maxItemId + " .. " + items.get((int) maxItemId));

		}

		// Item Strings
		long nItemStrings = MockDataGenerator.maxItemId;
		if (showSummaries) {
			System.out.println("\nItem Strings table with " + nItemStrings + " basic loops");
		}
		elapsed = MockDataGenerator.buildItemStrings(nItemStrings);
		totalElapsed += elapsed;
		if (showSummaries) {
			System.out.println("  -- Created " + MockDataGenerator.itemStrings.size()
					+ " ITEM STRINGS as blocks of 'related' records in " + elapsed / MockDataGenerator.NANO_TO_MILLI
					+ MockDataGenerator.MILLI_SECONDS);
			System.out.println("  -- Max Itm StrID: " + MockDataGenerator.maxItemId);
			long totalRecs = MockDataGenerator.itemStrings.entrySet().stream().map(s -> s.getValue().size()).reduce(0,
					Integer::sum);
			System.out.println("  -- Total Strings: " + totalRecs);
			System.out.println("--------------------------");
		}
		if (showContent) {
			itemStrings.entrySet().stream().filter(s -> s.getValue().get(0).itemStrid() % 100 == 1)
					.forEach(g -> System.out
							.println(g.getKey() + " .. " + g.getValue().get(0) + " (" + g.getValue().size() + ")"));
			System.out.println(maxItemId + " .. " + itemStrings.get(maxItemId).get(0) + " ("
					+ itemStrings.get(maxItemId).size() + ")");
		}

		return totalElapsed;
	}

	public static long buildGroups(int ngroups) {
		groups.clear();
		maxGroupId = 0;
		maxGroupSegmentId = 0;
		minGroupStartDate = Long.MAX_VALUE;
		maxGroupStartDate = Long.MIN_VALUE;
		long startTm = System.nanoTime();
		for (int i = 0; i < ngroups; i++) {
			int nsegs = r.nextInt(1, 7);
			for (int segmentId = 1; segmentId <= nsegs; segmentId++) {
				maxGroupSegmentId = Math.max(maxGroupSegmentId, nsegs);
				long businessStartDate = r.nextLong(33000L, 479156L);
				minGroupStartDate = Math.min(minGroupStartDate, businessStartDate);
				maxGroupStartDate = Math.max(maxGroupStartDate, businessStartDate);
				long businessEndDate = businessStartDate + r.nextLong(30000L, 100000L);
				++maxGroupId;
				groups.put(maxGroupId, new Groups(maxGroupId, segmentId, businessStartDate, businessEndDate));
			}
		}
		return System.nanoTime() - startTm;
	}

	public static long buildItems(int nitems) {
		items.clear();
		maxItemId = 0;
		minItmDate = Long.MAX_VALUE;
		maxItmDate = Long.MIN_VALUE;
		long startTm = System.nanoTime();
		for (int i = 0; i < nitems; i++) {
			maxItemId++;
			int groupId = r.nextInt(1, maxGroupId + 1);
			long itmDate = r.nextLong(Math.max(1, minGroupStartDate - 20), maxGroupStartDate + 51);
			minItmDate = Math.min(minItmDate, itmDate);
			maxItmDate = Math.max(maxItmDate, itmDate);
			int recId = (int) Math.min(itmDate / 10, 1);
			long reftime = r.nextLong(500000L);
			boolean deleted = r.nextDouble() < 0.35;
			long dateDeleted = deleted ? itmDate + 10 : 0;
			int itemId = i + 1;
			// int itemId, int groupId, long itmDate, int recId, long reftime, boolean
			// deleted, long dateDeleted
			items.put(itemId, new Items(itemId, groupId, itmDate, recId, reftime, deleted, dateDeleted));
		}

		return System.nanoTime() - startTm;
	}

	public static long buildItemStrings(long nItemStrings) {
		itemStrings.clear();
		maxItemStrId = 0;
		long startTm = System.nanoTime();
		for (long i = 0; i < nItemStrings; i++) {
			maxItemStrId++;
			List<ItemStrings> relatedStrings = new ArrayList<>();
			final int numEntries = r.nextInt(3, 10);
			for (int j = 1; j <= numEntries; j++) {
				final String content = "[" + j + "," + "ABCDEFGHIJKLMNOPQRETUVWXYZ".substring(j - 1, j) + "]";
				relatedStrings.add(new ItemStrings(maxItemStrId, j, "val->" + content, "err->" + content));
			}
			itemStrings.put(maxItemStrId, relatedStrings);
		}
		return System.nanoTime() - startTm;
	}
}
