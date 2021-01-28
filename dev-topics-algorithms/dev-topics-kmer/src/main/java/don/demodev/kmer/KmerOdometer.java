package don.demodev.kmer;

//D:\GitHub\DemoDev\dev-topics-algorithms\dev-topics-kmer\tests>run_odo.cmd
//Run the Java odometer version with C optimiations
//$ java -cp ..\target\classes\ don.demodev.kmer.KmerC
//Start KMER Computation with Odometer Algorithm and C Optimizations
//Nucleotides: ACGT;  Sequence Length: 13;  K-MERs expected: 67108864
//First: AAAAAAAAAAAAA
//Last : TTTTTTTTTTTTT
//Number of generated k-mers: 67108864
//Elapsed time: 0.2456602 secs
//Finished!

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * KmerOdometer.java
 * 
 * Compute all kmer combinations like an odometer, similar to
 * kmer_article_odometer.py.
 */
public class KmerOdometer {
	public static final byte[] nucleotides = new byte[] { 'A', 'C', 'G', 'T' };
	public static final Map<Character, Character> nucleotidesRotation = new HashMap<>();

	static {
		nucleotidesRotation.put('A', 'C');
		nucleotidesRotation.put('C', 'G');
		nucleotidesRotation.put('G', 'T');
		nucleotidesRotation.put('T', 'A');
	}

	private static class Result {
		public final long count;
		public final double elapsedMs;

		public Result(long count, double elapsedMs) {
			this.count = count;
			this.elapsedMs = elapsedMs;
		}
	}

	public static Result process(int kmerLength) {
		byte firstBase = nucleotides[0];
		byte[] s = new byte[kmerLength];
		for (int i = 0; i < kmerLength; i++) {
			s[i] = firstBase;
		}
		String dsp = new String(s, StandardCharsets.UTF_8);
		System.out.println("First: " + dsp);

		byte lastBase = nucleotides[nucleotides.length - 1];
		byte[] sLast = new byte[kmerLength];
		for (int i = 0; i < kmerLength; i++) {
			sLast[i] = lastBase;
		}
		dsp = new String(sLast, StandardCharsets.UTF_8);
		System.out.println("Last : " + dsp);

		int count = 1;
		dsp = new String(s, StandardCharsets.UTF_8);
		long startimeNs = System.nanoTime();
		while (!Arrays.equals(s, sLast)) {
			count += 1;

			int pos = kmerLength - 1;
			while (pos >= 0) {
				s[pos] = (byte) (nucleotidesRotation.get((char) (s[pos] & 0xFF)) & 0x00FF);
				if (s[pos] != firstBase) {
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
		long totalSequences = (long) Math.rint(Math.pow((double) nucleotides.length, stringLength));
		String dsp = new String(nucleotides, StandardCharsets.UTF_8);
		System.out.print("Nucleotides: " + dsp + ";  Sequence Length: " + stringLength);
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
