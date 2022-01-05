package demo.don.rakuten.merge;

import java.util.Arrays;

/**
 * Merge a smaller sorted array into a larger sorted array. The larger array has
 * sufficient space to hold the additional elements from the smaller array.
 */
public class Solution {

	public static void main(String[] args) {
		int[] nums1 = new int[] { 1, 3, 5, 0, 0 };
		int[] nums2 = new int[] { 2, 4 };
		System.out.println("\n -- nums1: " + Arrays.toString(nums1));
		System.out.println(" -- nums2: " + Arrays.toString(nums2));
		merge(nums1, nums2);
		System.out.println("*-- nums1: " + Arrays.toString(nums1));

		nums1 = new int[] { -1, 2, 33, 75, 100, 0, 0 };
		nums2 = new int[] { 0, 50 };
		System.out.println("\n -- nums1: " + Arrays.toString(nums1));
		System.out.println(" -- nums2: " + Arrays.toString(nums2));
		merge(nums1, nums2);
		System.out.println("*-- nums1: " + Arrays.toString(nums1));

		nums1 = new int[] { -1, 2, 33, 75, 100, 0, 0 };
		nums2 = new int[] { 0, 200 };
		System.out.println("\n -- nums1: " + Arrays.toString(nums1));
		System.out.println(" -- nums2: " + Arrays.toString(nums2));
		merge(nums1, nums2);
		System.out.println("*-- nums1: " + Arrays.toString(nums1));

		nums1 = new int[] { -1, 2, 33, 75, 100, 0, 0 };
		nums2 = new int[] { 150, 200 };
		System.out.println("\n -- nums1: " + Arrays.toString(nums1));
		System.out.println(" -- nums2: " + Arrays.toString(nums2));
		merge(nums1, nums2);
		System.out.println("*-- nums1: " + Arrays.toString(nums1));

		nums1 = new int[] { -10, -9, -8, -5, 1, 0, 0 };
		nums2 = new int[] { 5, 7 };
		System.out.println("\n -- nums1: " + Arrays.toString(nums1));
		System.out.println(" -- nums2: " + Arrays.toString(nums2));
		merge(nums1, nums2);
		System.out.println("*-- nums1: " + Arrays.toString(nums1));

		nums1 = new int[] { 1, 2, 3, 4, 5, 0, 0 };
		nums2 = new int[] { 6, 7 };
		System.out.println("\n -- nums1: " + Arrays.toString(nums1));
		System.out.println(" -- nums2: " + Arrays.toString(nums2));
		merge(nums1, nums2);
		System.out.println("*-- nums1: " + Arrays.toString(nums1));

		nums1 = new int[] { 1, 2, 3, 4, 5, 0, 0 };
		nums2 = new int[] { -1, 0 };
		System.out.println("\n -- nums1: " + Arrays.toString(nums1));
		System.out.println(" -- nums2: " + Arrays.toString(nums2));
		merge(nums1, nums2);
		System.out.println("*-- nums1: " + Arrays.toString(nums1));
	}

	private static void merge(int[] nums1, int[] nums2) {
		int p = 0;
		int freePointer = nums1.length - nums2.length;
		for (int smallPointer = 0; smallPointer < nums2.length; smallPointer++) {
			int smallCandidate = nums2[smallPointer];
			for (int largePointer = p; largePointer < nums1.length; largePointer++) {
				int largerValue = nums1[largePointer];
				if (largePointer >= freePointer) {
					//
					// Free area can be overwritten

					nums1[largePointer] = smallCandidate;
					freePointer++;
					break;
				} else if (smallCandidate < largerValue) {
					//
					// Slider larger to the right by one

					for (int k = nums1.length - 1; k >= Math.max(1, largePointer); k--) {
						nums1[k] = nums1[k - 1];
					}
					nums1[largePointer] = smallCandidate;
					freePointer++;
					break;
				}
				p = largePointer + 1;
			}
		}
	}
}