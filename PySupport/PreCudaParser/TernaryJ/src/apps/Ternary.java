//Java Test ternary IF operator NO side-effects
//
//initial:: chooser:  true  x: 0  y: 0  z: 0
//post::    chooser:  true  x: 1  y: 0  z: 1
//
//initial:: chooser:  false  x: 1  y: 0  z: 1
//post::    chooser:  false  x: 1  y: 1  z: 1

package apps;

class Ternary {

	public static void main(String[] args) {
		int x = 1, y = 5, z = 0;

		System.out.println("\nJava Test ternary IF operator NO side-effects");
		boolean chooser = true;
		System.out.println("\ninitial:: chooser:  " + chooser + "  x: " + x + "  y: " + y + "  z: " + z);
		z = chooser ? x + 1 : y + 1;
		System.out.println("post::    chooser:  " + chooser + "  x: " + x + "  y: " + y + "  z: " + z);
		chooser = false;
		System.out.println("\ninitial:: chooser:  " + chooser + "  x: " + x + "  y: " + y + "  z: " + z);
		z = chooser ? x + 1 : y + 1;
		System.out.println("post::    chooser:  " + chooser + "  x: " + x + "  y: " + y + "  z: " + z);
	}
}