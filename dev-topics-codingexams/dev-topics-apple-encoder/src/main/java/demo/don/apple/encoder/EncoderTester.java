package demo.don.apple.encoder;

/**
 * String Encoding - Implement algorithm that performs basic compression of a
 * string using counts of repeating characters. Examples are:
 * <p>
 * <code><pre>
 * �aabccccaaa�  -> �a2bc4a3�
 * �wwwwaaadexxxxxx� -> �w4a3dex6�
 * </pre></code>
 */
class EncoderTester {

	public static void main(String[] args) {
		String test = "a";
		runTest("no encoding", test, "a");
		test = "aa";
		runTest("simple encoding", test, "a2");
		test = "";
		runTest("empty", test, "");
		test = "aabccccaaa";
		runTest("example1", test, "a2bc4a3");
		test = "wwwwaaadexxxxxx";
		runTest("example2", test, "w4a3dex6");
		test = "wwwwjaaadexxxxxxk";
		runTest("example2A - Mixed", test, "w4ja3dex6k");
	}

	private static void runTest(String label, String test, String exp) {
		String act = Encoder.compress(test);
		if (exp.equals(act)) {
			System.out.println(label + " passed");
		} else {
			System.out.println(label + " failed!");
			System.out.println("ACT: " + act);
			System.out.println("EXP: " + exp);
		}
	}
}
