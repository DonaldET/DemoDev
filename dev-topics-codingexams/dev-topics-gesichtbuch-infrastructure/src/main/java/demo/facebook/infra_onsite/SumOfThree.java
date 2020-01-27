package demo.facebook.infra_onsite;

/**
 * Compute sum of three values in an array of unique integers such that it is
 * closest to a target t.
 */
public class SumOfThree {

	public static int closestSumOfThree(final int[] a, int t) {
		if (a == null || a.length < 3) {
			return -1;
		}

		int n = a.length;
		if (n < 3) {
			return -1;
		}

		int minDelSum = Integer.MAX_VALUE;
		int minDel = Integer.MAX_VALUE;

		for (int i = 0; i < n - 2; i++) {
			int s1 = a[i];
			for (int j = i + 1; j < n - 1; j++) {
				int s2 = s1 + a[j];
				for (int k = j + 1; k < n; k++) {
					int s3 = s2 + a[k];
					int delta = Math.abs(s3 - t);

					if (delta == 0) {
						return s3;
					}

					if (delta < minDel) {
						minDel = delta;
						minDelSum = s3;
					}
				}
			}
		}

		return minDelSum;
	}

	public static void main(String[] args) {
		// c(4, 3) = 4! / ((4 - 3)! * 3!) = 4!/3! = 4
		// {1, 2, 3, 4} => (1,2,3), (1,2,4); (1,3,4); (2,3,4)
		// SUMS => 6 7 8 9

		System.out.println("\n --- Testing Sum of Three");

		final int x[] = { 1, 2, 3, 4 };
		if (closestSumOfThree(x, 1) != 6)
			throw new IllegalStateException("failed != 1/6");
		if (closestSumOfThree(x, 5) != 6)
			throw new IllegalStateException("failed != 5/6");
		if (closestSumOfThree(x, 8) != 8)
			throw new IllegalStateException("failed != 8/8");
		if (closestSumOfThree(x, 22) != 9)
			throw new IllegalStateException("failed != 22/8");

		System.out.println("\n --- Testing Sum of Three passed");
	}
}
