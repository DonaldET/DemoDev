package demo.rakuten.merge;

import java.util.Arrays;

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
	}

	private static void merge(int[] nums1, int[] nums2) {
		int p = 0;
		int free = nums1.length - nums2.length;
		for (int i = 0; i < nums2.length; i++) {
			int m = nums2[i];
			for (int j = p; j < nums1.length; j++) {
				int v = nums1[j];
				if (j >= free) {
					nums1[j] = m;
					free++;
					break;
				} else if (v >= m) {
					for (int k = nums1.length - 1; k >= j; k--) {
						nums1[k] = nums1[k - 1];
					}
					nums1[j] = m;
					free++;
					break;
				}
				p = j + 1;
			}
		}
	}
}