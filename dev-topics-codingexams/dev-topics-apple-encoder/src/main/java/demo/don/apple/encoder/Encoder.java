package demo.don.apple.encoder;

public class Encoder {

	private Encoder() {
	}

	public static String compress(String src) {
		StringBuilder encode = new StringBuilder();
		int lth = src.length();
		if (lth < 1) {
			return src;
		}

		int pos = 0;
		char[] firstCh = { '?' };
		int nextPos = findConsecutive(src, pos, firstCh);
		while (pos < lth) {
			String block = src.substring(pos, nextPos);
			int blth = block.length();
			if (blth > 0) {
				encode.append(firstCh);
				if (blth > 1) {
					encode.append(Integer.toString(blth));
				}
			} else {
				break;
			}
			pos = nextPos;
			nextPos = findConsecutive(src, pos, firstCh);
		}

		return encode.toString();
	}

	private static int findConsecutive(String src, int pos, char[] ch) {
		int lth = src.length();
		if (pos >= lth) {
			ch[0] = '^';
			return lth;
		}

		String firstCh = src.substring(pos, pos + 1);
		int i = pos + 1;
		for (; i < lth; i++) {
			String nextCh = src.substring(i, i + 1);
			if (!firstCh.equals(nextCh)) {
				break;
			}
		}

		ch[0] = firstCh.charAt(0);
		return i;
	}
}
