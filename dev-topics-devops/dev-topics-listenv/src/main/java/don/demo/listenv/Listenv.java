package don.demo.listenv;

import java.io.PrintStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * List operating system environment variables and Java properties, as well as
 * any parameteters on the command line.
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class Listenv {

	/**
	 * Isolate output for unit testing
	 */
	private static PrintStream outputDest = System.out;

	public static void main(String[] args) {
		outputDest.println("\nListenv - show parameters");
		listParams(args);
		listEnv();
		listProperties();
		outputDest.println("Listenv Done");
	}

	private static int listParams(final String[] args) {
		final int na = args.length;
		outputDest.println("\n  -- Arguments [" + na + "]");
		if (na > 0) {
			for (int i = 0; i < na; i++) {
				outputDest.println("     " + i + ". " + String.valueOf(args[i]));
			}
		} else {
			outputDest.println("     [None]");
		}

		return na;
	}

	private static int listEnv() {
		Set<String> envKeys = new TreeSet<String>(System.getenv().keySet());
		final int ne = envKeys.size();
		outputDest.println("\n\n  -- Environment [" + ne + "]");
		int i = 0;
		for (String key : envKeys) {
			if (i < 5 || i > ne - 6) {
				outputDest.println(String.format("%26s: %s", key, System.getenv(key)));
			} else if (i == 5) {
				outputDest.println(String.format("%26s", ". . ."));
			}
			i++;
		}
		return ne;
	}

	private static int listProperties() {
		Set<Object> keySet = System.getProperties().keySet();
		Set<String> strKeys = keySet.stream().map(String::valueOf).collect(Collectors.toSet());
		keySet = null;
		Set<String> propKey = new TreeSet<String>(strKeys);
		strKeys = null;

		final int ne = propKey.size();
		outputDest.println("\n\n  -- System Properties [" + ne + "]");
		int i = 0;
		for (String key : propKey) {
			if (i < 5 || i > ne - 6) {
				outputDest.println(String.format("%26s: %s", key, System.getProperty(key)));
			} else if (i == 5) {
				outputDest.println(String.format("%26s", ". . ."));
			}
			i++;
		}
		return ne;
	}

	public static PrintStream getOutputDest() {
		return outputDest;
	}

	public static void setOutputDest(PrintStream outputDest) {
		Listenv.outputDest = outputDest;
	}
}
