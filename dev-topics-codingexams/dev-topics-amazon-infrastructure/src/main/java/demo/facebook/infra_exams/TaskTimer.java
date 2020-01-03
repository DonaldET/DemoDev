package demo.facebook.infra_exams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//
//	Sample List of Tasks:
//
//	T1 -> T2 T3 | 3
//	T2 -> T3 T4 | 2
//	T3 -> T4 | 2
//	T4 | 5
//
//	We can't start a task before it's dependencies are complete. Here is an example:
//
//	Given task T1, Find the minimum time to complete:
//	 o Order: T4 T3 T2 T1 - 5+2+2+3 = 12
//
public class TaskTimer {

	public static class Task {
		public String id;
		public List<Task> depends;
		public int time;
		public boolean isDone;

		public Task(String id, List<Task> depends, int time) {
			super();
			this.id = id;
			this.depends = depends;
			this.time = time;
			this.isDone = false;
		}

		@Override
		public String toString() {
			return "[Task - @0x" + Integer.toHexString(hashCode()) + "; id=" + id + ", depends=" + depends + ", time="
					+ time + ", isDone=" + isDone + "]";
		}
	}

	public static int minimumTime(Task parent) {
		int sum = 0;
		for (Task t : parent.depends) {
			if (!t.isDone) {
				sum += minimumTime(t);
			}
		}

		int t = ((parent.isDone) ? 0 : parent.time) + sum;
		parent.isDone = true;
		return t;
	}

	public static void main(String[] args) {
		System.out.println("\nTask Timer");
		Task x4 = new Task("T4", new ArrayList<Task>(), 5);
		int minTime = minimumTime(x4);
		System.out.println("\nMinimum Time for " + x4.id + ": " + minTime + " for:");
		System.out.println(x4);

		x4.isDone = false;
		Task x3 = new Task("T3", Arrays.asList(x4), 2);
		minTime = minimumTime(x3);
		System.out.println("\nMinimum Time for " + x3.id + ": " + minTime + " for:");
		System.out.println(x3);

		x3.isDone = false;
		x4.isDone = false;
		Task x2 = new Task("T2", Arrays.asList(x3, x4), 2);
		minTime = minimumTime(x2);
		System.out.println("\nMinimum Time for " + x2.id + ": " + minTime + " for:");
		System.out.println(x2);

		x2.isDone = false;
		x3.isDone = false;
		x4.isDone = false;
		Task x1 = new Task("T1", Arrays.asList(x2, x3), 3);
		minTime = minimumTime(x1);
		System.out.println("\nMinimum Time for " + x1.id + ": " + minTime + " for:");
		System.out.println(x1);
	}
}
