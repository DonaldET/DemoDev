package demo.javascript.intersect;

/**
 * <strong>Question</strong>:
 * <p>
 * Write a function, <code>findIntersection</code>, that reads an array of
 * strings which will contain two elements: the first element will represent a
 * list of comma-separated numbers sorted in ascending order, the second element
 * will represent a second list of comma-separated numbers (also sorted). Your
 * goal is to return a string of numbers that occur in both elements of the
 * input array in sorted order. If there is no intersection, return the string
 * <code>FALSE</code>.
 * <p>
 * For example: if the input array is ["1, 3, 4, 7, 15", "1, 2, 4, 15, 21"] the
 * output string should be "1, 4, 15" because those numbers appear in both
 * strings (they are the common elements). The array given will not be empty,
 * and each string inside the array will be of numbers sorted in ascending order
 * and may contain negative numbers.
 * <p>
 * Another example: if the input array is ["1, 3, 9, 10, 17, 18", "1, 4, 9, 10"]
 * the output string should be "1, 9, 10" because those numbers appear in both
 * of the strings.
 * <p>
 * Problem taken from <a href=
 * "https://javascript.plainenglish.io/a-common-javascript-phone-screen-question-asked-by-facebook-357a0139c458">a
 * JavaSript site</a>.
 * 
 * @author Donald Trummell
 */
public class Intersect {

	public static void main(String[] args) {
		System.out.println("Intersect two sorted lists of integers");
		System.out.println("Done.");
	}
}
