package demo.don.cruisecontrol.processor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Process tasks sequentially in a simplistic manner with drawbacks of:
 * <ul>
 * <li>task memory is as long as the input list</li>
 * <li>cooling time is parameterizable and execution time is not</li>
 * <li>Data structure requires all execution times to be fixed and a cooling
 * time a multiple of execution time</li>
 * <li>Task search is O(n)</li>
 * </ul>
 * <p>
 * <strong>Sample Output:</strong>
 * <pre>
 * <code>
 * Simple: Process tasks with 1 TU for execution and 2 TU for cooling off period
 * [T1]: T1 ==: 1
 * [T1, T2, T3, T4]: T1 T2 T3 T4 ==: 4
 * [T1, T2, T3, T1]: T1 T2 T3 T1 ==: 4
 * [T1, T2, T1, T4]: T1 T2 _ T1 T4 ==: 5
 * [T1, T2, T2, T4]: T1 T2 _ _ T2 T4 ==: 6
 * [T2, T2, T2]: T2 _ _ T2 _ _ T2 ==: 7
 * </code>
 * </pre>
 */
public class SimpleSolution {
	private static final int COOL_TIME = 2;

	public static class SimpleTask {
		private List<String> taskID = new LinkedList<String>();
		private int coolingTime;

		public SimpleTask(List<String> taskIDList, int coolingTime) {
			this.taskID.addAll(taskIDList);
			this.coolingTime = coolingTime;
		}

		public int process() {
			int nattempts = 0;
			int idx2Process = 0;
			while (idx2Process < taskID.size()) {
				String taskType = taskID.get(idx2Process);
				if (lookBack(taskType, idx2Process) >= 0) {
					// Have to wait, add cooling task to list to keep lookBack in sync
					taskType = "_";
					taskID.add(idx2Process, taskType);
				}
				System.out.print(" " + taskType);
				nattempts++;
				idx2Process++;
			}

			return nattempts;
		}

		private int lookBack(String taskType, int idx2Process) {
			for (int i = 0; i < coolingTime; i++) {
				int p = idx2Process - (coolingTime - i);
				if (p >= 0) {
					if (taskID.get(p).equals(taskType)) {
						return p;
					}
				}
			}
			return -1;
		}
	}

	public static void main(String[] args) {
		System.out.println("\nSimple: Process tasks with 1 TU for execution and 2 TU for cooling off period");

		runTest(Arrays.asList("T1"));
		runTest(Arrays.asList("T1", "T2", "T3", "T4"));
		runTest(Arrays.asList("T1", "T2", "T3", "T1"));
		runTest(Arrays.asList("T1", "T2", "T1", "T4"));
		runTest(Arrays.asList("T1", "T2", "T2", "T4"));
		runTest(Arrays.asList("T2", "T2", "T2"));
	}

	private static void runTest(List<String> tasks) {
		System.out.print("\n" + tasks + ":");
		SimpleTask st = new SimpleTask(tasks, COOL_TIME);
		System.out.print(" ==: " + st.process());
	}
}
