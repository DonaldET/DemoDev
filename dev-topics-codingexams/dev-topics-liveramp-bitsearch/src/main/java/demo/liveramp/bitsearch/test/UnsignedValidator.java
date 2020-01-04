package demo.liveramp.bitsearch.test;

/**
 * Validate that Java unsigned compare works as expected.
 * 
 * @author Donald Trummell
 */
public class UnsignedValidator {

	public static int donCompareUnsigned(int x, int y) {
		if (x >= 0) {
			if (y >= 0) {
				return x - y;
			} else {
				return -1;
			}
		} else {	// x < 0
			if (y >= 0) {
				return 1;
			} else {
				return x - y;
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("Don's Version:");
		int x = 2, y = 2;
		System.out.println("compare(" + x + ", " + y + ") = " + donCompareUnsigned(x, y) + ";  compare(" + y + ", " + x
				+ ") = " + donCompareUnsigned(y, x));
		x = 2;
		y = 1;
		System.out.println("compare(" + x + ", " + y + ") = " + donCompareUnsigned(x, y) + ";  compare(" + y + ", " + x
				+ ") = " + donCompareUnsigned(y, x));
		x = -2;
		y = 3;
		System.out.println("compare(" + x + ", " + y + ") = " + donCompareUnsigned(x, y) + ";  compare(" + y + ", " + x
				+ ") = " + donCompareUnsigned(y, x));
		x = 2;
		y = -3;
		System.out.println("compare(" + x + ", " + y + ") = " + donCompareUnsigned(x, y) + ";  compare(" + y + ", " + x
				+ ") = " + donCompareUnsigned(y, x));
		x = -2;
		y = -3;
		System.out.println("compare(" + x + ", " + y + ") = " + donCompareUnsigned(x, y) + ";  compare(" + y + ", " + x
				+ ") = " + donCompareUnsigned(y, x));

		// -----------------------------------------------------------------------------------------

		System.out.println("\nJava's Version:");
		x = 2;
		y = 2;
		System.out.println("compare(" + x + ", " + y + ") = " + Integer.compareUnsigned(x, y) + ";  compare(" + y + ", "
				+ x + ") = " + Integer.compareUnsigned(y, x));
		x = 2;
		y = 1;
		System.out.println("compare(" + x + ", " + y + ") = " + Integer.compareUnsigned(x, y) + ";  compare(" + y + ", "
				+ x + ") = " + Integer.compareUnsigned(y, x));
		x = -2;
		y = 3;
		System.out.println("compare(" + x + ", " + y + ") = " + Integer.compareUnsigned(x, y) + ";  compare(" + y + ", "
				+ x + ") = " + Integer.compareUnsigned(y, x));
		x = 2;
		y = -3;
		System.out.println("compare(" + x + ", " + y + ") = " + Integer.compareUnsigned(x, y) + ";  compare(" + y + ", "
				+ x + ") = " + Integer.compareUnsigned(y, x));
		x = -2;
		y = -3;
		System.out.println("compare(" + x + ", " + y + ") = " + Integer.compareUnsigned(x, y) + ";  compare(" + y + ", "
				+ x + ") = " + Integer.compareUnsigned(y, x));
	}
}
