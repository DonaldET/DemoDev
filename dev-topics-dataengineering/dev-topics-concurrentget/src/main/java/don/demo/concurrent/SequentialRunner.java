package don.demo.concurrent;

import java.util.stream.Stream;

import don.demo.concurrent.abstractions.GetTask;
import don.demo.concurrent.abstractions.ProcessState;
import don.demo.concurrent.data.DataDefinition;
import don.demo.concurrent.impl.DummyGetTask;
import don.demo.concurrent.impl.HeavyComputeGetTask;

/**
 * Sequentially process the 30 test cases. Output looks like this:
 */
public class SequentialRunner {

	public static void main(String[] args) {
		System.out.println("\nSequentialRunner - Run tasks one-at-a-time");
		final boolean isHeavy = Boolean.valueOf(System.getenv("IS_HEAVY"));

		System.out.println("  -- Java Version: " + System.getProperty("java.version", "unknown"));
		System.out.println("  -- Java VM     : " + System.getProperty("java.vm.name", "unknown"));
		System.out.println("  -- IS HEAVY                     : " + isHeavy);

		int nTasks = DataDefinition.remotes.size();
		System.out.println("  -- Processing " + nTasks + " tasks");
		ProcessState processState = new ProcessState();
		double elapsed = doWork(false, isHeavy, processState);
		System.out.println(" -- Elapsed: " + Math.round(elapsed / 1000000.0) / 1000.0 + " seconds");
		System.out.println(processState.toString());
	}

	public static double doWork(boolean isParallel, boolean isHeavy, ProcessState processState) {
		long start = System.nanoTime();
		Stream<DataDefinition.Remote> workStream = DataDefinition.remotes.stream();
		if (isParallel) {
			workStream = workStream.parallel();
		}
		workStream.forEach(remote -> {
			final GetTask task = (isHeavy) ? new HeavyComputeGetTask(processState, remote)
					: new DummyGetTask(processState, remote);
			task.accept(remote.id());
		});
		return System.nanoTime() - start;
	}
}
