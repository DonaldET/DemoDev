package demo.easypost.pick;

/**
 * In our fulfillment warehouses, workers use an app which records actions
 * performed. The three types of actions are: Picking, Receiving, and Shipping.
 * We need to aggregate these events to understand worker efficiency and
 * throughput.
 * <p>
 * There are a few different types of actions, but let"s focus on picking. When
 * a worker starts a pick, an event with type <strong>Pick::Create</strong> is
 * emitted. When a worker picks an item, an event with type
 * <strong>Inventory::Pick</strong> is emitted.
 * <p>
 * events have the following fields:
 * <ul>
 * <li>a pickId field which uniquely identifies a pick.</li>
 * <li>a ts field which is seconds since the unix epoch.</li>
 * <li>a workerId field which uniquely identifies a worker.</li>
 * <li>a warehouse_id field which uniquely identifies a warehouse.</li>
 * </ul>
 * <strong>Inventory::Pick</strong> also has an inventory_id field which
 * uniquely identifies an inventory item. Here is an example of a
 * <strong>Pick::Create</strong> event, expressed in JSON:
 * 
 * <pre>
 * <code>
 * {
 *   "type": "<strong>Pick::Create</strong>",
 *   "ts": 1524593594, # epoch time in seconds
 *   "workerId": 5,
 *   "warehouse_id": 1,
 *   "pickId": 1
 * }
 * </code>
 * </pre>
 * 
 * You are given an array, representing a sample of the event stream containing
 * these events. Your goal is to write a function which calculates how long each
 * worker spent doing picking for a specified day and warehouse so that the
 * supervisor of that warehouse can keep track of his workers' performance.
 * <p>
 * Input: array of event objects
 * <p>
 * Output: dictionary from worker ID to seconds spent picking
 * <p>
 * { "4": 360, "5": 720 }
 * <p>
 * Notes:
 * <ul>
 * <li>See input in <codemain</code> below for test data.</li>
 * <li>Problem originally given in Python, extensive work required to convert
 * problem and test data to Java.</li>
 * <li>Claim was that time-stamp integers were in GMT, but in Java, they were
 * "America/Los_Angeles" time zone.</li>
 * <li>Proposed solution was a sort input followed by break logic; actual
 * solution used a map to record time stamps.</li>
 * </ul>
 */

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Solution {

	static class Event {
		public String type;
		public int warehouseId;
		public int ts;
		public int workerId;
		public int pickId;
		public int inventoryId;

		public Event(String type, int ts, int workerId, int warehouseId, int pickId, int inventoryId) {
			this.type = type;
			this.warehouseId = warehouseId;
			this.ts = ts;
			this.workerId = workerId;
			this.pickId = pickId;
			this.inventoryId = inventoryId;
		}

		public Event(String type, int ts, int workerId, int warehouseId, int pickId) {
			this(type, ts, workerId, warehouseId, pickId, -1);
		}

		@Override
		public String toString() {
			Date tsDate = new Date(1000L * ts);
			return "Event [type=" + type + ", warehouseId=" + warehouseId + ", ts=" + ts + " sec. (" + tsDate
					+ "), workerId=" + workerId + ", pickId=" + pickId + ", inventoryId=" + inventoryId + "]";
		}
	}

	static class FilledOrder {
		int workerId;
		int begin;
		int end;

		public FilledOrder(int workerId, int begin, int end) {
			super();
			this.workerId = workerId;
			this.begin = begin;
			this.end = end;
		}

		@Override
		public String toString() {
			return "FilledOrder [workerId=" + workerId + ", begin=" + begin + ", end=" + end + "]";
		}
	}

	static interface DayInterval {
		int getStartTS();

		int getEndTS();
	}

	static class DayInterval7 implements DayInterval {
		private final int startTS;
		private final int endTS;

		@SuppressWarnings("deprecation")
		public DayInterval7(int bgnYear, int bgnMonth, int bgnDay, int endYear, int endMonth, int endDay) {
			super();
			Date d1 = new Date(bgnYear - 1900, bgnMonth - 1, bgnDay);
			startTS = (int) (d1.getTime() / 1000L);
			Date d2 = new Date(endYear - 1900, endMonth - 1, endDay);
			endTS = (int) (d2.getTime() / 1000L) - 1;
		}

		public int getStartTS() {
			return startTS;
		}

		public int getEndTS() {
			return endTS;
		}

		@Override
		public String toString() {
			return "DayInterval7 [startTS=" + startTS + ", endTS=" + endTS + "]";
		}
	}

	static class DayInterval8 implements DayInterval {
		private final int startTS;
		private final int endTS;

		public DayInterval8(int bgnYear, int bgnMonth, int bgnDay, int days) {
			super();
			// ZonedDateTime utcBegin = ZonedDateTime.of(bgnYear, bgnMonth, bgnDay, 0, 0, 0,
			// 0, ZoneOffset.UTC);
			// System.out.println("\t -- Zone: " +
			// ZonedDateTime.now().getZone().toString());
			// Returns: America/Los_Angeles
			ZonedDateTime utcBegin = ZonedDateTime.of(bgnYear, bgnMonth, bgnDay, 0, 0, 0, 0,
					ZoneId.of("America/Los_Angeles"));
			ZonedDateTime utcEnd = utcBegin.plus(days, ChronoUnit.DAYS);
			startTS = (int) (utcBegin.toEpochSecond());
			endTS = (int) (utcEnd.toEpochSecond() - 1);
		}

		public int getStartTS() {
			return startTS;
		}

		public int getEndTS() {
			return endTS;
		}

		@Override
		public String toString() {
			return "DayInterval8 [startTS=" + startTS + ", endTS=" + endTS + "]";
		}
	}

	static DayInterval getTestDateRange() {
		@SuppressWarnings("deprecation")
		Date d1 = new Date(2019 - 1900, 3 - 1, 25);
		System.out.println("\nDate of Interest: " + d1.toString());
		DayInterval7 dintr7 = new DayInterval7(2019, 3, 25, 2019, 3, 26);
		System.out.println("   Prior to Java 8: " + dintr7.toString());
		DayInterval8 dintr8 = new DayInterval8(2019, 3, 25, 1);
		System.out.println("   Post Java 8    : " + dintr8.toString());

		// return dintr7;
		return dintr8;
	}

	private static Map<Integer, FilledOrder> getOrders(List<Event> events, int warehouseId, int tsStart, int tsEnd) {
		Map<Integer, FilledOrder> orderHistory = new HashMap<Integer, FilledOrder>();

		for (Event event : events) {
			if (event.ts < tsStart || event.ts > tsEnd || event.warehouseId != warehouseId) {
				continue;
			}

			FilledOrder entry = orderHistory.get(event.pickId);
			if (event.type == "Pick::Create") {
				if (entry == null) {
					entry = new FilledOrder(event.workerId, event.ts, Integer.MIN_VALUE);
				} else {
					entry.begin = Math.min(entry.begin, event.ts);
				}
			} else if (event.type == "Inventory::Pick") {
				if (entry == null) {
					entry = new FilledOrder(event.workerId, Integer.MAX_VALUE, event.ts);
				} else {
					entry.end = Math.max(entry.end, event.ts);
				}
			} else {
				continue; // Ignore other types
			}
			orderHistory.put(event.pickId, entry);
		}

		return orderHistory;
	}

	public Map<Integer, Integer> getSummary(List<Event> events, int warehouseId, int tsStart, int tsEnd) {
		Map<Integer, FilledOrder> orderTimes = getOrders(events, warehouseId, tsStart, tsEnd);

		Map<Integer, Integer> summary = new HashMap<Integer, Integer>();
		for (Entry<Integer, FilledOrder> e : orderTimes.entrySet()) {
			summary.put(e.getValue().workerId, e.getValue().end - e.getValue().begin);
		}
		return summary;
	}

	public static void main(String[] args) {
		List<Event> data = Arrays.asList(new Event("Pick::Create", 1553564811, 4, 5, 10),
				new Event("Other", 1553579995, 2, 5, 12), new Event("Inventory::Pick", 1553565151, 4, 5, 10, 2),
				new Event("Inventory::Pick", 1553559899, 1, 5, 11), new Event("Inventory::Pick", 1553557899, 1, 5, 11),
				new Event("Pick::Create", 1553557502, 1, 5, 11));
		System.out.println("Data:");
		for (Event d : data) {
			System.out.println("  " + d);
		}

		Solution solution = new Solution();
		DayInterval dintr = getTestDateRange();

		Map<Integer, FilledOrder> orderSummary = getOrders(data, 5, dintr.getStartTS(), dintr.getEndTS());
		System.out.println("\nOrders:\n  " + orderSummary.toString());

		Map<Integer, Integer> result = solution.getSummary(data, 5, dintr.getStartTS(), dintr.getEndTS());
		System.out.println("\nResult:\n  " + String.valueOf(result));
	}
}