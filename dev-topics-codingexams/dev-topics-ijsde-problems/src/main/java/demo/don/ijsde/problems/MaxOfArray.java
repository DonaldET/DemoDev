package demo.don.ijsde.problems;

import java.util.Arrays;

public class MaxOfArray {

	public static long solution(long[] numbers) {
		if (numbers.length < 1) {
			return 0l;
		}
		return Arrays.stream(numbers).max().getAsLong();
	}

	public static long solution2(long[] numbers) {
		if (numbers.length < 1) {
			return 0l;
		}
		long maxVal = Long.MIN_VALUE;
		for (int i = 0; i < numbers.length; i++) {
			maxVal = Math.max(maxVal, numbers[i]);
		}
		return maxVal;
	}

	public static void main(String[] args) {
		long[] arr = { 5L, 0L, -1L, 17L, 8L };
		System.out.println("Max of " + Arrays.toString(arr) + " is " + solution(arr) + " by stream");
		System.out.println("Max of " + Arrays.toString(arr) + " is " + solution2(arr) + " by for loop");
	}
}
