package demo.don.liveramp.autoboxing;

/**
 * Demonstrate how identity and value are identical for integers -127 to +128.
 * 
 * @author Donald Trummell
 */
public class ExampleEQL {
	public static void main(String[] args) {
		final int[] testVals = { Integer.MIN_VALUE, (Integer.MIN_VALUE + 1), -128, -127, -2, -1, 0, 1, 2, 127, 128,
				(Integer.MAX_VALUE - 1), Integer.MAX_VALUE };
		System.out.println("Checking equal comparision for " + testVals.length + " integers.");

		int peql = 0;
		int oeql = 0;
		int ooeql = 0;
		int pdif = 0;
		int odif = 0;
		int oodif = 0;
		for (int i = 0; i < testVals.length; i++) {
			final int x = testVals[i];
			final int y = x;
			if (x == y) {
				peql++;
			} else {
				pdif++;
			}
			final Integer ix = x;
			final Integer iy = y;
			if (ix == iy) {
				oeql++;
			} else {
				odif++;
			}
			if (ix.equals(iy)) {
				ooeql++;
			} else {
				oodif++;
			}
		}

		System.out.println("\tPrimative Equal: " + peql + ";  Not Equal: " + pdif);
		System.out.println("\tObject Equal: " + oeql + ";  Not Equal: " + odif);
		System.out.println("\tObject .equals(): " + ooeql + ";  Not Equal: " + oodif);
	}
}
