package don.demo.llmexamples.filechecker.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

//import don.demo.llmexamples.filechecker;

/**
 * Unit test for simple App.
 */
public class EratosthenesTest {

	@Test(expected = AssertionError.class)
	public void testSieveBadSize() {
		int n = 0;
		int start = 3;
		assert(n > start);
	}
}
