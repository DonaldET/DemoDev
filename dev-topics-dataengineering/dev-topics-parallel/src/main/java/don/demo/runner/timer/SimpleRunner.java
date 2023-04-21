package don.demo.runner.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import don.demo.dataservices.GroupsAccessor.BusinessDateHolder;
import don.demo.dataservices.GroupsAccessor.SegmentHolder;
import don.demo.dataservices.ItemStringAcccessor.ItemStrings;
import don.demo.mockdata.MockDataGenerator;
import don.demo.queryservices.UIDataRetrieval;
import don.demo.queryservices.UIDataRetrieval.UIDataElement;
import don.demo.queryservices.servicesimpl.SimpleDataElementService;

public class SimpleRunner {
	public static final int N_TEST_GROUPS = 200;
	public static final int N_TEST_ITEMS = 7000;

	private UIDataRetrieval retriever = null;

	public UIDataRetrieval getRetriever() {
		return retriever;
	}

	public void setRetriever(UIDataRetrieval retriever) {
		this.retriever = retriever;
	}

	public SimpleRunner() {
	}

	public void initialize(int ngroups, int nItems) {
		long totalTime = MockDataGenerator.initialize(false, false, ngroups, nItems);
		System.out.println(
				"Data generated in " + totalTime / MockDataGenerator.NANO_TO_MILLI + MockDataGenerator.MILLI_SECONDS);
	}

	public void runQuery() {
		System.out.println("  ... Running Simple Query");

		long testDate = 329000L;
		int testSegmnt = 1;

		Predicate<ItemStrings> selectAll = (s) -> true;

		long startTM = System.nanoTime();
		List<UIDataElement> elements = retriever.getDataElements(new BusinessDateHolder(testDate),
				new SegmentHolder(testSegmnt), selectAll);
		long elapsed = System.nanoTime() - startTM;

		if (elements == null) {
			throw new IllegalStateException("query result is null");
		}

		int expSize = 1254;
		if (elements.size() != expSize) {
			throw new IllegalStateException("expected " + expSize + " elements, but got " + elements.size());
		}

		final double NANO_FACTOR = 1.0E+6d;
		System.out.println(
				"        warmed-up " + elements.size() + " elements in " + (elapsed / NANO_FACTOR) + " milli-seconds.");

		final int REPEAT_COUNT = 100;
		startTM = System.nanoTime();
		for (int i = 0; i < REPEAT_COUNT; i++) {
			elements = retriever.getDataElements(new BusinessDateHolder(testDate), new SegmentHolder(testSegmnt),
					selectAll);
		}
		long totalTime = System.nanoTime() - startTM;

		System.out.println("        retrieved " + elements.size() + " elements in "
				+ (totalTime / NANO_FACTOR) / (double) REPEAT_COUNT + " milli-seconds average for " + REPEAT_COUNT
				+ " trials.");
		System.out.println("  ... Simple Query Complete");
	}

	private static void usageErrorAndExit(String message) {
		System.err.print("\nUsage Error: " + message + "\n  Usage: ");
		System.err.println("runner.timer.SimpleRunner <delay>");
		System.err.println("\t    delay      : integer service delay amount in milli-seconds\n");
		System.exit(1);
	}

	private static Map<String, Object> parseArge(String[] args) {
		Map<String, Object> parsedArgs = new HashMap<String, Object>();

		if (args == null || args.length < 1) {
			usageErrorAndExit("No arguments");
		}

		if (args.length != 1) {
			usageErrorAndExit("incorrect argument count, expected 1 but got " + args.length);
		}

		try {
			long delay = Long.parseLong(args[0]);
			if (delay < 0) {
				usageErrorAndExit("service delay is negative: " + delay);
			}
			parsedArgs.put("service_delay", delay);
		} catch (NumberFormatException ex) {
			usageErrorAndExit("First parameter '" + String.valueOf(args[0]) + "' is not an integer");
		}

		return parsedArgs;
	}

	public static void main(String[] args) {
		System.out.println("\nTimed Simple Runner");
		Map<String, Object> argvals = parseArge(args);
		System.out.println("  -- " + argvals);

		SimpleRunner runner = new SimpleRunner();
		runner.initialize(N_TEST_GROUPS, N_TEST_ITEMS);
		SimpleDataElementService retriever = new SimpleDataElementService(false,
				((Long) argvals.get("service_delay")).longValue());
		runner.setRetriever(retriever);
		runner.runQuery();
		System.out.println("Timed Simple Runner Done");
	}
}
