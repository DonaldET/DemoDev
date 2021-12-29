package demo.don.marketingpush.orgchart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * We initially reviewed a JSON file ORG chart file and abstracted that to a
 * recursive Java data structure as if read into memory by a JSON library. The
 * restated problem is:
 * <p>
 * <strong>Problem:</strong>
 * <p>
 * Given a list of employees, optionally followed by reports, nested
 * recursively, print the entries in a breadth-first walk down down the
 * organization chart. It is OK to ignore the reporting relationships, as we
 * wish to display each level in order and completed before printing the next
 * level.
 * <p>
 * An ORG chart is represented as Lists of type Object containing String or
 * additional embedded lists of type Object.
 * 
 * @author Donald Trummell
 */
public class OrgChart {

	/**
	 * 
	 * @param data  Data contains a list of either strings or a list-of-strings
	 *              recursively embedded
	 * @param level the employee level in the ORG chart
	 */
	@SuppressWarnings("unchecked")
	public static void printEntries(Object data, int level) {
		List<List<Object>> nextLevel = new ArrayList<>();
		if (data instanceof List) {
			for (Object member : (List<Object>) data) {
				if (member instanceof String) {
					printString((String) member, level);
				} else {
					nextLevel.add((List<Object>) member);
				}
			}
		} else {
			printString(String.valueOf(data), level);
		}

		for (List<Object> newLevel : nextLevel) {
			printEntries(newLevel, level + 1);
		}
	}

	private static void printString(String data, int level) {
		System.out.print(" ".repeat(2 * level));
		System.out.println(data);
	}

	// -------------- Test Support ------------------

	private static List<Object> buildTestData() {
		List<String> lowLevel4A = Arrays.asList("Robert the Bruce, King of Scotland", "Samuel Johnson, Playwright",
				"Richard Petty, NASCAR");
		List<String> lowLevel4B = Arrays.asList("George Patton, General", "Chester W. Nimitz, Admiral",
				"The Duke, Actor");

		List<Object> level3 = new ArrayList<>();
		level3.add("Leonardo Da Vinci, Artist");
		level3.add("Karl der Gro�e, Holy Roman Emperor");
		level3.add(lowLevel4A);
		level3.add("Dwight D. Eisenhower, 34th Pesident");
		level3.add(lowLevel4B);
		level3.add("Attila the Hun, barbarian");

		List<Object> level2 = new ArrayList<>();
		level2.add("Isidore of Seville, Patron Saint of Madrid");
		level2.add("Nero Claudius Caesar Augustus Germanicus, Emperor #5");
		level2.add("Julius Caesar, Emperor #1");
		level2.add(level3);

		List<Object> level1 = new ArrayList<>();
		level1.add("Thor, God of Thunder");
		level1.add(level2);

		System.out.println("\n-- The Organization Chart Data --");
		System.out.println("Top Level 1 : " + level1);
		System.out.println("Mid Level 2 : " + level2);
		System.out.println("Mid Level 3 : " + level3);
		System.out.println("Low Level 4A: " + lowLevel4A);
		System.out.println("Low Level 4B: " + lowLevel4B);

		return level1;
	}

	/**
	 * Test entry point
	 * 
	 * @param args command-line parameters
	 */
	public static void main(String[] args) {
		System.out.println("ORG Chart Printer");
		List<Object> orgChart = buildTestData();
		System.out.println("\n-- Structured Org Chart--");
		printEntries(orgChart, 0);
		System.out.println("Done.");
	}
}
