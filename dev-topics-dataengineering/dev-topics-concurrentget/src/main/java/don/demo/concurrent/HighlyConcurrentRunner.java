package don.demo.concurrent;

import java.util.concurrent.ForkJoinPool;

import don.demo.concurrent.abstractions.ProcessState;
import don.demo.concurrent.data.DataDefinition;
import don.demo.concurrent.impl.DummyGetTask;

/**
 * Use a parallel stream to process the 30 test cases, but allow increased
 * parallelization. Output looks like this
 * 
 * <pre>
 * <code>
 *  -- Elapsed: 2.759 seconds
 * ProcessState [taskCount      : 30,
 *               failedTaskCount: 1,
 *               byteCount      : 33294202,
 *               checkSum       : 4244683985]
 * </code>
 * </pre>
 * 
 * Compare to default parallelism:
 * 
 * <pre>
 * <code>
 *  -- Elapsed: 5.252 seconds
 * ProcessState [taskCount      : 30,
 *               failedTaskCount: 1,
 *               byteCount      : 33294202,
 *               checkSum       : 4244685264]
 * </code>
 * </pre>
 * 
 * Compare to Sequential processing:
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
public class HighlyConcurrentRunner {

	public static void main(String[] args) {
		System.out.println("\nConcurrentRunner - Run tasks concurrently with more threads");

		System.out.println("  -- CPU Core                     : " + Runtime.getRuntime().availableProcessors());
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
		System.out.println("  -- CommonPool Parallelism update: " + ForkJoinPool.commonPool().getParallelism());
		System.out.println("  -- CommonPool shared Parallelism: " + ForkJoinPool.getCommonPoolParallelism());

		int nTasks = DataDefinition.remotes.size();
		System.out.println("  -- Processing " + nTasks + " tasks");
		ProcessState processState = new ProcessState();

		long start = System.nanoTime();
		DataDefinition.remotes.stream().parallel()
				.forEach(remote -> new DummyGetTask(processState, remote).accept(remote.id()));
		double elapsed = System.nanoTime() - start;

		System.out.println(" -- Elapsed: " + Math.round(elapsed / 1000000.0) / 1000.0 + " seconds");
		System.out.println(processState.toString());
	}
}
