package demo.liveramp.autoboxbug;

public class ExampleGtr {
	public static void main(String[] args) {
		final int[] testVals = { Integer.MIN_VALUE, (Integer.MIN_VALUE + 1), -128, -127, -2, -1, 0, 1, 2, 127, 128, 128,
				(Integer.MAX_VALUE - 1), Integer.MAX_VALUE, 0, 1, 2 };
		System.out.println("Checking greater-than/less-than comparision for " + testVals.length + " integers.");

		int pgtr = 0;
		int pXgtr = 0;
		int ogtr = 0;
		int oXgtr = 0;
		for (int i = 0; i < testVals.length; i++) {
			final int x = testVals[i];
			final int y = x;
			if (x > y) {
				pgtr++;
			} else {
				pXgtr++;
			}

			final Integer ix = new Integer(x);
			final Integer iy = new Integer(y);
			if (ix > iy) {
				ogtr++;
			} else {
				oXgtr++;
			}
		}

		System.out.println("\tPrimative Greater-Than: " + pgtr + ";  Different: " + pXgtr);
		System.out.println("\tObject Greater-Than: " + ogtr + ";  Different: " + oXgtr);
	}
}
