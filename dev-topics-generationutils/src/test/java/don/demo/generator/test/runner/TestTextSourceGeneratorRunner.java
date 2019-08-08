package don.demo.generator.test.runner;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import don.demo.generator.TextSourceGenerator;
import don.demo.generator.TextSourceGeneratorRunner;

/**
 * Tests the runner that, informed by command-line parameters, executes a
 * generation run; this reads templates, uses Freemarker to process, and writes
 * to the target directory.
 * 
 * @author Donald Trummell
 *
 *         Copyright (c) 2019. Donald E. Trummell. All Rights Reserved.
 *         Permission to use, copy, modify, and distribute this software and its
 *         documentation for educational, research, and not-for-profit purposes,
 *         without fee and without a signed licensing agreement, is hereby
 *         granted, provided that the above copyright notice, and this
 *         paragraph, appear in all copies, modifications, and distributions.
 *         Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
public class TestTextSourceGeneratorRunner {
	private static final String SPRING_TEST_APP_CONTEXT = "/META-INF/main/spring/test-app-context.xml";

	private static boolean[] initialized = new boolean[] { false };

	private static ApplicationContext ctx = null;

	private static Logger LOGGER = null;

	@Before
	public void setUp() throws Exception {
		synchronized (initialized) {
			if (!initialized[0]) {
				//
				// Initialize
				final ApplicationContext ctxT = new ClassPathXmlApplicationContext(SPRING_TEST_APP_CONTEXT);
				Assert.assertNotNull(SPRING_TEST_APP_CONTEXT + " null", ctxT);

				ctx = ctxT;

				LOGGER = Logger.getLogger(TextSourceGeneratorRunner.class);

				initialized[0] = true;
			}
		}
	}

	private String version = null;
	private String testVersion = null;

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAllNeededTestBeansPresent() {
		String name = "version.test";
		testVersion = (String) ctx.getBean(name);
		Assert.assertEquals("bean " + name + " differs", "TEST-1.0.0-SNAPSHOT", testVersion);
	}

	@Test
	public void testAllNeededAppBeansPresent() {
		String name = "version";
		version = (String) ctx.getBean(name);
		Assert.assertEquals("bean " + name + " differs", "1.0.0-SNAPSHOT", version);
	}

	@Test
	public void testHiveGen() {
		final TextSourceGenerator generator = TextSourceGeneratorRunner.readyApplication();
		Assert.assertNotNull("generator bean null", generator);
//		// -defaultContext VAL -dstDir VAL -generatedFileList VAL -overrideContextList
//		// VAL -srcDir VAL -templateList VAL
//
//		final String[] args = { "a", "b", "c" };
//		final Result result = TextSourceGeneratorRunner.processWithArgs(generator, args);
//		Assert.assertNotNull("processing result null", result);
	}
}
