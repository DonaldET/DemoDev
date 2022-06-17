package don.demo.concurrent;

import java.util.concurrent.ForkJoinPool;

import don.demo.concurrent.abstractions.ProcessState;
import don.demo.concurrent.data.DataDefinition;

/**
 * Use a parallel stream to process the 30 test cases.
 */
public class ConcurrentRunner {

	public static void main(String[] args) {
		System.out.println("\nConcurrentRunner - Run tasks concurrently");
		final boolean isHeavy = Boolean.valueOf(System.getenv("IS_HEAVY"));

		System.out.println("  -- CPU Cores                    : " + Runtime.getRuntime().availableProcessors());
		System.out.println("  -- CommonPool Parallelism       : " + ForkJoinPool.commonPool().getParallelism());
		System.out.println("  -- CommonPool shared Parallelism: " + ForkJoinPool.getCommonPoolParallelism());
		System.out.println("  -- IS HEAVY                     : " + isHeavy);

		int nTasks = DataDefinition.remotes.size();
		System.out.println("  -- Processing " + nTasks + " tasks");
		ProcessState processState = new ProcessState();
		double elapsed = SequentialRunner.doWork(true, isHeavy, processState);
		System.out.println(" -- Elapsed: " + Math.round(elapsed / 1000000.0) / 1000.0 + " seconds");
		System.out.println(processState.toString());
	}
}
