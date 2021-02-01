package don.demodev.kmer;

import java.util.HashMap;
import java.util.Map;

/**
 * KmerLists.java
 * 
 * Compute all kmer combinations like an odometer, but concatenating string
 * elements of the KMER together.
 */
public class KmerLists {
	public static final String nucleotides = "ACGT";
	public static final Map<String, String> nucleotidesRotation = new HashMap<>();

	static {
		nucleotidesRotation.put("A", "C");
		nucleotidesRotation.put("C", "G");
		nucleotidesRotation.put("G", "T");
		nucleotidesRotation.put("T", "A");
	}

	private static class Result {
		public final long count;
		public final double elapsedMs;

		public Result(long count, double elapsedMs) {
			this.count = count;
			this.elapsedMs = elapsedMs;
		}
	}

	private static String build_by_append(String s, int pos, String newBase, StringBuilder sb) {
		sb.delete(0, sb.length());
		if (pos > 0) {
			sb.append(s.substring(0, pos));
			sb.append(newBase);
			if (pos < s.length() - 1) {
				sb.append(s.substring(pos + 1));
			}
		} else {
			sb.append(newBase);
			sb.append(s.substring(1));
		}

		return sb.toString();
	}

	public static Result process(int kmerLength) {
		final StringBuilder sb = new StringBuilder();

		final String firstBase = nucleotides.substring(0, 1);
		for (int i = 0; i < kmerLength; i++) {
			sb.append(firstBase);
		}
		String s = sb.toString();
		System.out.println("First: " + s);

		sb.delete(0, sb.length());
		final String lastBase = nucleotides.substring(nucleotides.length() - 1);
		for (int i = 0; i < kmerLength; i++) {
			sb.append(lastBase);
		}
		final String slast = sb.toString();
		System.out.println("Last : " + slast);

		int count = 1;
		long startimeNs = System.nanoTime();
		while (!s.equals(slast)) {
			count += 1;
			int pos = kmerLength - 1;
			while (pos >= 0) {
				String newBase = nucleotidesRotation.get(s.substring(pos, pos + 1));
				s = build_by_append(s, pos, newBase, sb);
				if (!newBase.equals(firstBase)) {
					break;
				}
				pos -= 1;
			}
		}
		long endTimeNs = System.nanoTime();
		return new Result(count, (endTimeNs - startimeNs) / 1000000.0D);
	}

	public static void main(String[] args) {
		System.out.println("Start KMER Computation using Odometer Algorithm");
		int stringLength = 13;
		long totalSequences = (long) Math.rint(Math.pow((double) nucleotides.length(), stringLength));
		System.out.print("Nucleotides: " + nucleotides + ";  Sequence Length: " + stringLength);
		System.out.println(";  K-MERs expected: " + totalSequences);
		Result r = process(stringLength);
		System.out.println("Number of generated k-mers: " + r.count);
		if (r.count != totalSequences) {
			System.err.println("--- WARNING: possible error, expected " + totalSequences + " KMERs but got " + r.count);
		}
		System.out.println("Elapsed time: " + (r.elapsedMs / 1000.0) + " secs");
		System.out.println("Finished!");
	}
}
