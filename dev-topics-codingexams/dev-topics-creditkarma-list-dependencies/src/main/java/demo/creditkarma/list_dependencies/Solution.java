package demo.creditkarma.list_dependencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/*

1. Create a data structure or object to represent an "Item" which has
   two properties, "name" and "dependancies".
     - "name" is a string
     - "dependancies" is multiple string (names of other items this one depends on)

Example:

Item {
   "name": String
   "dependancies": List[String]
}


2. Create a function that given a list of items will print out their
   names ordered so that an item appears after it's dependencies

[
	{ "name": "A", "dependancies": [ "B" ] },
	{ "name": "B", "dependancies": [ "X" ] },
	{ "name": "C", "dependancies": [ "D" ] },
	{ "name": "X", "dependancies": [ "Y" ] },
	{ "name": "D", "dependancies": [ "A" ] },
	{ "name": "E", "dependancies": [ "D" ] },
	{ "name": "F", "dependancies": [ "A", "C" ] },
	{ "name": "Y", "dependancies": [ ] },
]

Example output: Y X B A D C E F

*/

public class Solution {

	private static Map<String, List<String>> loadModuleList(String[] rawDependencyList) {
		LinkedHashMap<String, List<String>> modules = new LinkedHashMap<String, List<String>>(); // Preserve input order
		int ln = 0;
		for (String line : rawDependencyList) {
			ln++;
			String[] tokens = line.trim().split(",");
			int nTokens = tokens.length;
			if (nTokens < 1) {
				throw new IllegalArgumentException("no tokens on line");
			}
			String name = tokens[0].trim();
			List<String> dependenciesForName = new ArrayList<String>(nTokens - 1);
			if (nTokens > 0) {
				Set<String> known = new LinkedHashSet<String>();
				known.add(name);
				for (int i = 1; i < nTokens; i++) {
					String depName = tokens[i].trim();
					if (!known.add(depName)) {
						throw new IllegalArgumentException("Attempting to add duplicate dependency:\n" + depName
								+ " to " + known + " at\n" + ln + ". " + line);
					}
					dependenciesForName.add(depName);
				}
			}
			if (modules.put(name, dependenciesForName) != null) {
				throw new IllegalArgumentException(
						"Attempting to add module " + name + " to\n" + modules + " with\n" + ln + ". " + line);
			}
		}

		// Check dependency not defined
		for (Map.Entry<String, List<String>> e : modules.entrySet()) {
			List<String> dependencies = e.getValue();
			if (!dependencies.isEmpty()) {
				for (String depName : dependencies) {
					if (!modules.containsKey(depName)) {
						throw new IllegalArgumentException("Dependency " + depName + " not in modules");
					}
				}
			}
		}
		return modules;
	}

	private static TreeMap<String, List<String>> sortDependencies(Map<String, List<String>> unorderedModules) {
		TreeMap<String, List<String>> orderedModules = new TreeMap<String, List<String>>(); // Order ascending by key
		orderedModules.putAll(unorderedModules);
		for (Entry<String, List<String>> e : orderedModules.entrySet()) {
			Collections.sort(e.getValue()); // sort dependencies
		}
		return orderedModules;
	}

	private static Entry<String, List<String>> getFirstEntry(Map<String, List<String>> modules) {
		if (modules.isEmpty()) {
			return null;
		}
		Entry<String, List<String>> first = modules.entrySet().iterator().next();
		modules.remove(first.getKey());
		return first;
	}

	private static boolean addInput(Set<String> published, Map<String, List<String>> modules,
			LinkedList<String> dependencies, int[] nextPos) {

		if (nextPos[0] != dependencies.size()) {
			throw new IllegalArgumentException(
					"next [" + nextPos[0] + "] not at end of dependencies [" + dependencies.size() + "]");
		}

		while (true) {
			Entry<String, List<String>> first = getFirstEntry(modules);
			if (first == null) {
				return false;
			}

			String name = first.getKey();
			if (published.add(name)) {
				dependencies.add(name);
				List<String> firstDependencies = first.getValue();
				if (!firstDependencies.isEmpty()) {
					for (String dependencyName : firstDependencies) {
						if (published.add(dependencyName)) {
							dependencies.add(dependencyName);
						}
					}
				}
				nextPos[0]++;
				return true;
			}
		}
	}

	private static boolean augmentDependencies(Set<String> published, Map<String, List<String>> modules,
			LinkedList<String> dependencies, int startPos, int[] nextPos) {

		if (startPos < 0 || startPos > dependencies.size()) {
			throw new IllegalArgumentException(
					"unable to augment, startPos [" + startPos + "] exceeds " + dependencies.size());
		}

		int p = startPos;
		while (p < dependencies.size()) {
			String name = dependencies.get(p);
			List<String> moduleDependencies = modules.get(name);
			if (moduleDependencies != null) {
				for (String md : moduleDependencies) {
					if (published.add(md)) {
						dependencies.add(++p, md);
					}
				}
				modules.remove(name);
				nextPos[0] = ++p;
				return true;
			}
		}
		nextPos[0] = p;
		return false;
	}

	private static LinkedList<String> listModules(Map<String, List<String>> modules) {
		LinkedList<String> dependencies = new LinkedList<String>();
		if (modules.isEmpty()) {
			return dependencies;
		}

		Set<String> published = new LinkedHashSet<String>();
		int startPos = 0;
		int[] nextPos = { startPos };

		// Add first dependency
		boolean added = addInput(published, modules, dependencies, nextPos);
		if (!added) {
			throw new IllegalArgumentException("unable to add any dependencies");
		}
		startPos = nextPos[0];

		do {
			// Add dependencies following the added names from the modules list
			boolean augmented = true;
			while (augmented) {
				augmented = augmentDependencies(published, modules, dependencies, startPos, nextPos);
				startPos = nextPos[0];
			}

			// Add another entry to the dependencies
			added = addInput(published, modules, dependencies, nextPos);
			startPos = nextPos[0];
		} while (added);

		System.out.println("   ---- published dependencies: " + published);

		return dependencies;
	}

	private static List<String> reverse(List<String> modList) {
		ListIterator<String> iter = modList.listIterator(modList.size());
		List<String> reversedMods = new ArrayList<String>(modList.size());
		while (iter.hasPrevious()) {
			reversedMods.add(iter.previous());
		}
		return reversedMods;
	}

	public static void processDependencies(String label, String[] testMods) {
		boolean doOrdered = true;
		System.out.println("\n------ " + label + ", " + ((doOrdered) ? "Ordered" : "Unordered"));
		Map<String, List<String>> modDependencies = loadModuleList(testMods);
		System.out.println(String.format("%-11s         : ", label) + modDependencies);

		LinkedList<String> modList = null;
		if (doOrdered) {
			TreeMap<String, List<String>> modules = sortDependencies(modDependencies);
			System.out.println("Ordered dependencies: " + modules);
			modList = listModules(modules);
		} else {
			modList = listModules(modDependencies);
		}
		System.out.println("Generated Modules   : " + modList);
		System.out.println("Reversed Modules    : " + reverse(modList));
	}

	// -------------------------------------------------------------------------

	// Ordered Output:
	//
	// *** Display Modules following their dependencies ***
	//
	// ------ Zero Length, Ordered
	// Zero Length : {}
	// Ordered dependencies: {}
	// Generated Modules : []
	// Reversed Modules : []
	//
	// ------ simple look, Ordered
	// simple look : {"D"=["E", "F"], "A"=["G"], "B"=["C"], "C"=["E"], "E"=["F"],
	// "F"=[], "G"=["B"]}
	// Ordered dependencies: {"A"=["G"], "B"=["C"], "C"=["E"], "D"=["E", "F"],
	// "E"=["F"], "F"=[], "G"=["B"]}
	// ---- published dependencies: ["A", "G", "B", "C", "E", "F", "D"]
	// Generated Modules : ["A", "G", "B", "C", "E", "F", "D"]
	// Reversed Modules : ["D", "F", "E", "C", "B", "G", "A"]
	//
	// ------ many looks, Ordered
	// many looks : {"D"=[], "B"=["C", "D", "A", "E"], "A"=["E"], "C"=["A"],
	// "E"=["F"], "F"=[]}
	// Ordered dependencies: {"A"=["E"], "B"=["A", "C", "D", "E"], "C"=["A"],
	// "D"=[], "E"=["F"], "F"=[]}
	// ---- published dependencies: ["A", "E", "F", "B", "C", "D"]
	// Generated Modules : ["A", "E", "F", "B", "C", "D"]
	// Reversed Modules : ["D", "C", "B", "F", "E", "A"]
	//
	// ------ Acceptance Test, Ordered
	// Accept Test : {"A"=["B"], "B"=["X"], "C"=["D"], "X"=["Y"], "D"=["A"],
	// "E"=["D"], "F"=["A", "C"], "Y"=[]}
	// Ordered dependencies: {"A"=["B"], "B"=["X"], "C"=["D"], "D"=["A"], "E"=["D"],
	// "F"=["A", "C"], "X"=["Y"], "Y"=[]}
	// ---- published dependencies: ["A", "B", "X", "C", "D", "E", "F", "Y"]
	// Generated Modules : ["A", "B", "X", "C", "D", "E", "F", "Y"]
	// Reversed Modules : ["Y", "F", "E", "D", "C", "X", "B", "A"]
	//

	public static void main(String[] args) {
		System.out.println("\n*** Display Modules following their dependencies ***");

//		String[] error1 = { "\"A\", \"A\"" };
//		processDependencies("dup", error1);

//		String[] error2 = { "\"A\", \"B\", \"B\"" };
//		processDependencies("dupdep line", error2);

//		String[] error3 = { "\"A\", \"B\"" };
//		processDependencies("undefined dep", error3);

		String[] zeroLength = {};
		processDependencies("Zero Length", zeroLength);

		// [a=[b, c], d=[a, e], e=[], b=[], c=[]]
		//
		// A <---- D ----> E  B  C
		// |                  ^  ^
		// -------------------|--|
		//
		// satisfies      : a b c d *a* e ==> e d c b a
		// depends/recurse: b c a e d
		//
		//
		String[] simplestLook = { "\"a\", \"b\", \"c\"", "\"d\", \"a\", \"e\"", "\"e\"", "\"b\"", "\"c\"" };
		processDependencies("simpest look", simplestLook);

		String[] simpleLook = { "\"D\", \"E\", \"F\"", "\"A\", \"G\"", "\"B\", \"C\"", "\"C\", \"E\"", "\"E\", \"F\"",
				"\"F\"", "\"G\", \"B\"" };
		processDependencies("simple look", simpleLook);

		String[] multiLook = { "\"D\"", "\"B\", \"C\", \"D\", \"A\", \"E\"", "\"A\", \"E\"", "\"C\", \"A\"",
				"\"E\", \"F\"", "\"F\"" };
		processDependencies("many looks", multiLook);

		// [a=[z], b=[w], w=[x, y], x=[a], y=[], z=[]]
		String[] front2back = { "\"a\", \"z\"", "\"b\", \"w\"", "\"w\", \"x\", \"y\"", "\"x\", \"a\"", "\"y\"",
				"\"z\"" };
		processDependencies("front-back", front2back);

		// [z=[], y=[], x=[a], w=[y, x], b=[w], a=[z]]
		String[] back2front = { "\"z\"", "\"y\"", "\"x\", \"a\"", "\"w\", \"y\", \"x\"", "\"b\", \"w\"",
				"\"a\", \"z\"" };
		processDependencies("back-front", back2front);

		String[] acceptanceTest = { "\"A\", \"B\"", "\"B\", \"X\"", "\"C\", \"D\"", "\"X\", \"Y\"", "\"D\", \"A\"",
				"\"E\", \"D\"", "\"F\", \"A\", \"C\"", "\"Y\"" };
		processDependencies("Accept Test", acceptanceTest);
	}
}
