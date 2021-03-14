package don.demo.listenv.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import don.demo.listenv.Listenv;

public class SimpleTestSuccessful {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSimple() throws IOException {
		OutputStream os = new ByteArrayOutputStream(2048);
		PrintStream ps = new PrintStream(os, true);
		Listenv.setOutputDest(ps);
		Listenv.main(new String[] { "A", "B", "C" });
		ps.flush();
		String result = os.toString();
		Assert.assertNotNull("output string null", result);
		result = result.trim();
		Assert.assertFalse("output string empty", result.isEmpty());
		Assert.assertTrue("output header differs", result.startsWith("Listenv - show parameters"));
		Assert.assertTrue("Needs 3 arguments", result.contains("-- Arguments [3]"));
		String ender = "Listenv Done";
		Assert.assertTrue("'" + ender + "' missing", result.endsWith(ender));
	}
}
