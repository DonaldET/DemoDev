package don.demo.runner.timer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Predicate;

import don.demo.dataservices.GroupsAccessor.BusinessDateHolder;
import don.demo.dataservices.GroupsAccessor.SegmentHolder;
import don.demo.dataservices.ItemStringAcccessor.ItemStrings;
import don.demo.mockdata.MockDataGenerator;
import don.demo.queryservices.UIDataRetrieval;
import don.demo.queryservices.UIDataRetrieval.UIDataElement;
import don.demo.queryservices.servicesimpl.StreamDataElementService;

public class QueryRunner {
	public static final int N_TEST_GROUPS = 200;
	public static final int N_TEST_ITEMS = 7000;

	private UIDataRetrieval retriever = null;

	public UIDataRetrieval getRetriever() {
		return retriever;
	}

	public void setRetriever(UIDataRetrieval retriever) {
		this.retriever = retriever;
	}

	public QueryRunner() {
	}

	public void initialize(int ngroups, int nItems) {
		long totalTime = MockDataGenerator.initialize(false, false, ngroups, nItems);
		System.out.println(
				"Data generated in " + totalTime / MockDataGenerator.NANO_TO_MILLI + MockDataGenerator.MILLI_SECONDS);
	}

	public void runQuery() {
		System.out.println("  ... Running Query");

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
		System.out.println("  ... Query Complete");
	}

	private static void usageErrorAndExit(String message) {
		System.err.print("\nUsage Error: " + message + "\n  Usage: ");
		System.err.println("runner.timer.QueryRunner <is_parallel> <delay>");
		System.err.println("\t    is_parallel: true or false");
		System.err.println("\t    delay      : integer service delay amount in milli-seconds\n");
		System.exit(1);
	}

	private static Map<String, Object> parseArge(String[] args) {
		Map<String, Object> parsedArgs = new HashMap<String, Object>();

		if (args == null || args.length < 1) {
			usageErrorAndExit("No arguments");
		}

		if (args.length != 2) {
			usageErrorAndExit("incorrect argument count, expected 2 but got " + args.length);
		}

		parsedArgs.put("parallel", Boolean.parseBoolean(args[0]));

		try {
			long delay = Long.parseLong(args[1]);
			if (delay < 0) {
				usageErrorAndExit("service delay is negative: " + delay);
			}
			parsedArgs.put("service_delay", delay);
		} catch (NumberFormatException ex) {
			usageErrorAndExit("Second parameter '" + String.valueOf(args[1]) + "' is not an integer");
		}

		return parsedArgs;
	}

	public static void main(String[] args) {
		System.out.println("\nTimed Query Runner");
		Map<String, Object> argvals = parseArge(args);
		System.out.println("  -- " + argvals);
		System.out.println("  -- parallelism: " + ForkJoinPool.commonPool().getParallelism());

		QueryRunner runner = new QueryRunner();
		runner.initialize(N_TEST_GROUPS, N_TEST_ITEMS);
		StreamDataElementService retriever = new StreamDataElementService(
				((Boolean) argvals.get("parallel")).booleanValue(), ((Long) argvals.get("service_delay")).longValue());
		runner.setRetriever(retriever);
		runner.runQuery();
		System.out.println("Timed Query Runner Done");
	}
}
