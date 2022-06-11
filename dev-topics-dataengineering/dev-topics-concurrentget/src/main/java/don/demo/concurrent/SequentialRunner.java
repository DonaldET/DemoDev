package don.demo.concurrent;

import don.demo.concurrent.abstractions.ProcessState;
import don.demo.concurrent.data.DataDefinition;
import don.demo.concurrent.impl.DummyGetTask;

/**
 * Sequentually process the 30 test cases. Output looks like this:
 * 
 * <pre>
 * <code>
 *  -- Elapsed: 37.757 seconds
 * ProcessState [taskCount      : 30,
 *               failedTaskCount: 1,
 *               byteCount      : 33294202,
 *               checkSum       : 4244683377]
 * </code>
 * </pre>
 */
public class SequentialRunner {

	public static void main(String[] args) {
		System.out.println("\nSequentialRunner - Run tasks one-at-a-time");
		int nTasks = DataDefinition.remotes.size();
		System.out.println("  -- Processing " + nTasks + " tasks");
		ProcessState processState = new ProcessState();

		long start = System.nanoTime();
		DataDefinition.remotes.stream().forEach(remote -> new DummyGetTask(processState, remote).accept(remote.id()));
		double elapsed = System.nanoTime() - start;

		System.out.println(" -- Elapsed: " + Math.round(elapsed / 1000000.0) / 1000.0 + " seconds");
		System.out.println(processState.toString());
	}
}
