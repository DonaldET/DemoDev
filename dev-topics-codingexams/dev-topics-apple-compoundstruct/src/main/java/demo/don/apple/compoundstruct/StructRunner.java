package demo.don.apple.compoundstruct;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StructRunner {

	public StructRunner() {
	}

	public static void main(String[] args) {
		System.out.println("Test the compound store data structure");

		Struct cs = new Struct();
		if (cs.size() != 0) {
			System.err.println("CS not empty");
		} else {
			System.out.println("Empty size passed");
		}

		int gn = cs.getRandom();
		if (gn != Integer.MIN_VALUE) {
			System.err.println("get random failed on empty, got " + gn);
		} else {
			System.out.println("Empty getRandom passed");
		}

		cs.insert(2);
		if (cs.size() != 1) {
			System.err.println("CS not 1");
		} else {
			System.out.println("Single entry size passed");
		}

		gn = cs.getRandom();
		if (gn != 2) {
			System.err.println("get random failed on single entry, got " + gn);
		} else {
			System.out.println("Single getRandom passed");
		}

		cs.insert(3);
		cs.insert(4);
		cs.insert(5);
		if (cs.size() != 4) {
			System.err.println("CS not 4");
		} else {
			System.out.println("Four entries size passed");
		}

		cs.remove(4);
		if (cs.size() != 3) {
			System.err.println("CS not 3");
		} else {
			System.out.println("Remove one size passed");
		}

		Map<Integer, Integer> content = new HashMap<>();
		int testSize = 10000;
		for (int i = 0; i < testSize; i++) {
			Integer val = cs.getRandom();
			Integer count = content.get(val);
			count = (count == null) ? 1 : count + 1;
			content.put(val, count);
		}

		if (content.size() != 3) {
			System.out.println("random outputs for 3 entries was " + content.size() + ", not 3");
			System.exit(1);
		} else {
			System.out.println("random outputs for 3 entries passed");
			System.out.println("  -- Counts: " + content);

		}

		int[] diffs = new int[3];
		diffs[0] = Math.abs(content.get(2) - content.get(3));
		diffs[1] = Math.abs(content.get(2) - content.get(5));
		diffs[2] = Math.abs(content.get(3) - content.get(5));
		Arrays.sort(diffs);
		System.out.println("  -- Diffs : " + Arrays.toString(diffs));
		if ((diffs[2] - diffs[0]) / (double) testSize > 0.01) {
			System.out.println("random outputs for 3 entries NOT approximately normal!");
		} else {
			System.out.println("random outputs for 3 entries approximately normal");
		}
	}
}
