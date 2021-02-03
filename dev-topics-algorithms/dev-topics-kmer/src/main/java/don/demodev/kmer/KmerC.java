package don.demodev.kmer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * KmerC.java
 * 
 * Compute all kmer combinations like an odometer, similar to
 * kmer_article_odometer.py, but incorporate C optimizations.
 */
public class KmerC {
	public static final byte[] nucleotideLabels = new byte[] { 'A', 'C', 'G', 'T' };
	public static final byte[] nucleotides = new byte[] { 0, 1, 2, 3 };
	public static final byte[] nucleotidesRotation = new byte[] { 1, 2, 3, 0 };

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
		final byte[] btemp = new byte[s.length];
		for (int i = 0; i < s.length; i++) {
			btemp[i] = nucleotideLabels[s[i]];
		}
		String dsp = new String(btemp, StandardCharsets.UTF_8);
		System.out.println("First: " + dsp);

		byte lastBase = nucleotides[nucleotides.length - 1];
		byte[] sLast = new byte[kmerLength];
		for (int i = 0; i < kmerLength; i++) {
			sLast[i] = lastBase;
		}
		for (int i = 0; i < sLast.length; i++) {
			btemp[i] = nucleotideLabels[sLast[i]];
		}
		dsp = new String(btemp, StandardCharsets.UTF_8);
		System.out.println("Last : " + dsp);

		int count = 1;
		long startimeNs = System.nanoTime();
		while (!Arrays.equals(s, sLast)) {
			count += 1;
			int pos = kmerLength - 1;
			while (pos >= 0) {
				s[pos] = nucleotidesRotation[s[pos] & 0xFF];
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
		System.out.println("Start KMER Computation with Odometer Algorithm and C Optimizations");
		int stringLength = 13;
		long totalSequences = (long) Math.rint(Math.pow((double) nucleotides.length, stringLength));
		String dsp = new String(nucleotideLabels, StandardCharsets.UTF_8);
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
