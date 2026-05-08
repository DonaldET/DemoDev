//Test ternary IF
//
//initial:: chooser:  true  x: 0  y: 0
//post::    chooser:  true  x: 1  y: 1
//
//initial:: chooser:  false  x: 1  y: 1
//post::    chooser:  false  x: 1  y: 2
//
//Incremented x: 4

package apps;

class Ternary {

    private static int x = 0, y = 0;
    private static boolean chooser = true;

    public static void main(String[] args) {
        System.out.println("\nTest ternary IF");
        chooser = true;
        System.out.println("\ninitial:: chooser:  " + chooser + "  x: " + x + "  y: " + y);
        y = chooser ? ++x : ++y;
        System.out.println("post::    chooser:  " + chooser + "  x: " + x + "  y: " + y);
        chooser = false;
        System.out.println("\ninitial:: chooser:  " + chooser + "  x: " + x + "  y: " + y);
        y = chooser ? ++x : ++y;
        System.out.println("post::    chooser:  " + chooser + "  x: " + x + "  y: " + y);
        ++x;
        ++x;
        ++x;
        System.out.println("\nIncremented x: " + x);
    }
}