package demo.liveramp.autoboxing;

import java.util.Random;

public class ExampleEQLNeg {

	public static void main(String[] args) {
		final int x = 0x80000000;
		final int r = new Random(11).nextInt(32);
		final int y = 0x80000000 + (r - r); // defeat compiler detecting equality
		if (x == y) {
			System.out.println("Equal as expected");
		} else {
			System.out.println("Not equal!");
		}

		final Integer ix = new Integer(x);
		final Integer iy = new Integer(y);
		if (ix == iy) {
			System.out.println("Object Equal as expected");
		} else {
			System.out.println("Object Not equal!");
		}

		final int x2 = ix;
		final int y2 = iy;
		if (x2 == y2) {
			System.out.println("Int from Object Equal as expected");
		} else {
			System.out.println("Int from Object Not Equal!");
		}
	}
}
