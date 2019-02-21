package demo.cruisecontrol.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Process tasks sequentially in a realistic manner with additional features of:
 * <ul>
 * <li>task memory now finite, allowing streaming</li>
 * <li>execution time and cooling time are parameterizable and generalized to
 * <em>required time</em></li>
 * <li>task executor provides runtime state if needed</li>
 * <li>Increased speed of task lookup from O(n) to O(1)</li>
 * </ul>
 * <p>
 * <strong>Sample Output:</strong>
 *
 * <pre>
 * <code>
 * Realistic: Process tasks with 1 TU for execution and 2 TU for cooling off period
 * [T1]: T1[0,1] ==: 1
 * [T1, T2, T3, T4]: T1[0,1] T2[1,2] T3[2,3] T4[3,4] ==: 4
 * [T1, T2, T3, T1]: T1[0,1] T2[1,2] T3[2,3] T1[3,4] ==: 4
 * [T1, T2, T1, T4]: T1[0,1] T2[1,2] _ T1[2,4] T4[3,5] ==: 5
 * [T1, T2, T2, T4]: T1[0,1] T2[1,2] _ _ T2[2,5] T4[3,6] ==: 6
 * [T2, T2, T2]: T2[0,1] _ _ T2[1,4] _ _ T2[2,7] ==: 7
 * </code>
 * </pre>
 */
public class RealisticSolution {
	private static final int COOL_TIME = 2;
	private static final int EXECUTE_TIME = 1;

	public static class Task {
		public int position;
		public String taskType;
		public int requiredTime;
		public int started;

		public Task(int position, String taskType, int requiredTime, int started) {
			super();
			this.position = position;
			this.taskType = taskType;
			this.requiredTime = requiredTime;
			this.started = started;
		}

		public void startTask(int clock) {
			System.out.print(" " + taskType + "[" + position + "," + clock + "]");
		}
	}

	public static class TaskExecutor {
		private final Map<String, Task> processor = new LinkedHashMap<String, Task>();
		private final int requiredTime;
		private int clock = 0;

		public TaskExecutor(int requiredTime) {
			this.requiredTime = requiredTime;
		}

		public boolean addNewTask(String taskType, int position) {
			boolean empty = workCycle();
			if (!empty && processor.containsKey(taskType)) {
				return false;
			}
			Task newTask = new Task(position, taskType, requiredTime, clock);
			newTask.startTask(clock);
			Task oldVale = processor.put(taskType, newTask);
			assert oldVale == null;
			return true;
		}

		public boolean workCycle() {
			clock++;
			removeCompletedTasks();
			return processor.isEmpty();
		}

		private int removeCompletedTasks() {
			List<String> completedTasks = new ArrayList<String>();
			// Find completed task
			for (String taskType : processor.keySet()) {
				Task currentTask = processor.get(taskType);
				if (clock - currentTask.started >= currentTask.requiredTime) {
					completedTasks.add(taskType);
				}
			}
			// Remove completed tasks
			for (String taskType : completedTasks) {
				processor.remove(taskType);
			}
			return completedTasks.size();
		}
	}

	public static void main(String[] args) {
		System.out.println("\nRealistic: Process tasks with 1 TU for execution and 2 TU for cooling off period");

		runTest(Arrays.asList("T1"));
		runTest(Arrays.asList("T1", "T2", "T3", "T4"));
		runTest(Arrays.asList("T1", "T2", "T3", "T1"));
		runTest(Arrays.asList("T1", "T2", "T1", "T4"));
		runTest(Arrays.asList("T1", "T2", "T2", "T4"));
		runTest(Arrays.asList("T2", "T2", "T2"));
	}

	private static void runTest(List<String> tasks) {
		System.out.print("\n" + tasks + ":");
		TaskExecutor te = new TaskExecutor(COOL_TIME + EXECUTE_TIME);
		int p = 0;
		int attempts = 0;
		while (p < tasks.size()) {
			String taskType = tasks.get(p);
			boolean added = te.addNewTask(taskType, p);
			attempts++;
			while (!added) {
				System.out.print(" _");
				added = te.addNewTask(taskType, p);
				attempts++;
			}
			p++;
		}

		System.out.print(" ==: " + attempts);
	}
}
