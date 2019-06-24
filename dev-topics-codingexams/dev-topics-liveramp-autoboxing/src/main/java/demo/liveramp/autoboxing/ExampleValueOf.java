package demo.liveramp.autoboxbug;

public class ExampleValueOf {
	public static void main(String[] args) {
		final int k = 127;
		final Integer x = k;
		final Integer y = Integer.valueOf(k);
		if (x == y) {
			System.out.println(k + " EQUAL");
		} else {
			System.out.println(k + " FAILED!");
		}

		final int l = 128;
		final Integer x2 = l;
		final Integer y2 = Integer.valueOf(l);
		if (x2 == y2) {
			System.out.println(l + " EQUAL");
		} else {
			System.out.println(l + " FAILED!");
		}
	}
}
