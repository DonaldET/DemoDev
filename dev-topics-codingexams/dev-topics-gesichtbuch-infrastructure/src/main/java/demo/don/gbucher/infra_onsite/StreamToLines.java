package demo.don.gbucher.infra_onsite;

/**
 * Read text lines, delimited by new lines, from a stream of large chunks of
 * text. Zero length and varying length chunks are allowed, as well as a totally
 * empty stream.
 */
public class StreamToLines {
	/**
	 * Chunk data source; supplies chunks to line extractor.
	 */
	public static class Chunk {
		private final String[] chunks;
		private int n = 0;

		public Chunk(String[] chuncks) {
			this.chunks = chuncks;
		}

		public String readChunck() {
			if (chunks == null || n >= chunks.length) {
				return null;
			}

			return chunks[n++];
		}
	}

	/**
	 * Extract newline delimited lines from chunks, including the new line.
	 */
	public static class LineBuilder {
		private final Chunk chunkReader;

		//
		// State used between getNextLine calls

		private String chunk = "";
		private int nxtChrIndex = 1;
		boolean sawEof = false;

		public LineBuilder(Chunk chunk) {
			this.chunkReader = chunk;
		}

		/**
		 * Return next newline delimited line or null for EOF.
		 * 
		 * @return next read line plus new line as result
		 */
		public String getNextLine() {
			if (sawEof) {
				return null;
			}
			final StringBuilder sb = new StringBuilder();

			boolean sawEol = false;
			while (!sawEol) {
				//
				// Handle multiple lines per chunk vs multiple chunks per line
				if (nxtChrIndex >= chunk.length()) {
					if (getNextNonEmptyChunk() == null) {
						return (sb.length() > 0) ? sb.toString() : null;
					}
				}
				sawEol = copyToEol(sb);
			}

			return sb.toString();
		}

		private String getNextNonEmptyChunk() {
			String chunkin = chunkReader.readChunck();
			while (chunkin != null) {
				if (!chunkin.isEmpty()) {
					chunk = chunkin;
					nxtChrIndex = 0;
					return chunkin;
				}
				chunkin = chunkReader.readChunck();
			}

			sawEof = true;
			chunk = "";
			nxtChrIndex = 1;
			return chunkin;
		}

		private boolean copyToEol(StringBuilder sb) {
			while (nxtChrIndex < chunk.length()) {
				char c = chunk.charAt(nxtChrIndex);
				sb.append(c);
				nxtChrIndex++;

				if (c == '\n') {
					return true;
				}
			}
			return false;
		}
	}

	public static void main(String[] args) {
		System.out.println("\nTest of Line Builder");
		String[] inputs = null;
		LineBuilder lb = new LineBuilder(new Chunk(inputs));
		if (lb.getNextLine() != null)
			throw new IllegalStateException("Did not get null for null");
		inputs = new String[] {};
		lb = new LineBuilder(new Chunk(inputs));
		if (lb.getNextLine() != null)
			throw new IllegalStateException("Did not get null for empty");
		System.out.println("   --- Null/Empty OK");

		System.out.print("\n   --- Small lines");
		inputs = new String[] { "", "", "One and two empty, now third chunk[", "", "]Fith chunk-no new line" };
		listLines(new LineBuilder(new Chunk(inputs)));

		inputs = new String[] { "first line\nSecond line\nThi", "rd spanned line\nFourth line\nnumber5\n" };
		listLines(new LineBuilder(new Chunk(inputs)));

		System.out.print("\n   --- Large lines");
		inputs = new String[] { "This really long line goes on and on across many ch",
				"unks and ends, yes ends, in the middle with a new ",
				"line\nHere is the last line without a new line!" };
		listLines(new LineBuilder(new Chunk(inputs)));
	}

	private static void listLines(LineBuilder lb) {
		System.out.println("\n----------");
		String line = lb.getNextLine();
		int i = 0;
		while (line != null) {
			System.out.print(i + ". " + line);
			if (!line.endsWith("\n")) {
				System.out.println();
			}
			i++;
			line = lb.getNextLine();
		}
		System.out.println(i + ". EOF");
	}
}
