//Java Test NO ternary operator NO side-effects
//
//Setup:   x: 1  y: 5  Alternatives: [2, 6]
//post::   chooser:  0  z: 2
//post::   chooser:  1  z: 6

package apps;

class Ternary_NB {

	public static void main(String[] args) {
		int x = 1, y = 5, z = 0;

		System.out.println("\nJava Test NO ternary operator NO side-effects");
		int[] alternatives = { x + 1, y + 1 };
		System.out.println("\nSetup:   x: " + x + "  y: " + y + "  Alternatives: [" + alternatives[0] + ", "
				+ alternatives[1] + "]");
		int chooser = 0;
		z = alternatives[chooser];
		System.out.println("post::   chooser:  " + chooser + "  z: " + z);
		chooser = 1;
		z = alternatives[chooser];
		System.out.println("post::   chooser:  " + chooser + "  z: " + z);
	}
}