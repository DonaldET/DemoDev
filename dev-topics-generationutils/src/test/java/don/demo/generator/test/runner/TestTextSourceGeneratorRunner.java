package don.demo.generator.test.runner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.don.file.util.FileUtils;
import don.demo.generator.TextSourceGenerator;
import don.demo.generator.TextSourceGenerator.Result;
import don.demo.generator.TextSourceGeneratorRunner;

/**
 * Tests the runner that, informed by command-line parameters, executes a
 * generation run; this reads templates, uses Freemarker to process, and writes
 * to the target directory.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
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

	@Before
	public void setUp() throws Exception {
		synchronized (initialized) {
			if (!initialized[0]) {
				//
				// Initialize
				final ApplicationContext ctxT = new ClassPathXmlApplicationContext(SPRING_TEST_APP_CONTEXT);
				Assert.assertNotNull(SPRING_TEST_APP_CONTEXT + " null", ctxT);

				ctx = ctxT;

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
	public void testRunnerWithHiveGen() {
		final TextSourceGenerator generator = TextSourceGeneratorRunner.readyApplication();
		Assert.assertNotNull("generator bean null", generator);

		/**
		 * <pre>
		 * <code>
		 * Example:
		 * 		 0. == -defaultContext,
		 * 		 1. == D:\GitHub\DemoDev\dev-topics-generationutils\src\test\resources\app_config\shared_defs.properties,
		 * 		 2. == -overrideContextList,
		 * 		 3. == D:\GitHub\DemoDev\dev-topics-generationutils\src\test\resources\app_config\target_defs.properties,
		 * 		 4. == -srcDir,
		 * 		 5. == D:\GitHub\DemoDev\dev-topics-generationutils\src\test\resources\app_src,
		 * 		 6. == -templateList,
		 * 		 7. == hive_query.tpl,
		 * 		 8. == -dstDir,
		 * 		 9. == D:\GitHub\DemoDev\dev-topics-generationutils\.\target,
		 * 		 10. == -generatedFileList,
		 * 		 11. == hive_query.hql
		 * </code>
		 * </pre>
		 */
		final String[] args = setupRun();
		System.err.println("params: " + Arrays.toString(args));
		final Result result = TextSourceGeneratorRunner.processWithArgs(generator, args);
		Assert.assertNotNull("processing result null", result);
		Assert.assertEquals("read count bad", 1, result.getFilesRead());
		Assert.assertEquals("write count bad", 1, result.getFilesWritten());

		String goldStandard = FileUtils.collectTrimmedFileData(goldStandardPath.toFile());
		String generatedOutput = FileUtils.collectTrimmedFileData(hiveGenerated.toFile());
		Assert.assertEquals("generated file differs", goldStandard, generatedOutput);
	}

	// ------------------------------------------------------------------------------------------

	private static Path workDirPath = null;
	private static Path sharedDef = null;
	private static Path targetDef = null;
	private static Path hiveTemplate = null;
	private static Path hiveGenerated = null;
	private static Path goldStandardPath = null;

	private static String[] setupRun() {
		Path contextDirPath = getVerifiedInputDir("src/test/resources/app_config");
		Path sourceDirPath = getVerifiedInputDir("src/test/resources/app_src");

		sharedDef = getInputFilePath(contextDirPath.toString(), "shared_defs.properties", true);
		targetDef = getInputFilePath(contextDirPath.toString(), "target_defs.properties", true);
		hiveTemplate = getInputFilePath(sourceDirPath.toString(), "hive_query.tpl", true);

		final String workDir = "./target";
		workDirPath = getVerifiedWorkDir(workDir, workDir + "TestTextSourceGeneratorRunner.txt");
		hiveGenerated = getInputFilePath(workDirPath.toString(), "hive_query.hql", false);

		Path goldStandardDirPath = getVerifiedInputDir("src/test/resources/goldStandard");
		goldStandardPath = getInputFilePath(goldStandardDirPath.toString(), "hive_query.hql", true);

		String[] args = { "-defaultContext", sharedDef.toString(), "-overrideContextList", targetDef.toString(),
				"-srcDir", sourceDirPath.toString(), "-templateList", hiveTemplate.getFileName().toString(), "-dstDir",
				workDirPath.toString(), "-generatedFileList", hiveGenerated.getFileName().toString() };

		return args;
	}

	private static Path getInputFilePath(String dir, String fname, boolean checkExists) {
		Path testPath = Paths.get(dir, fname);
		if (checkExists) {
			Assert.assertFalse(testPath + " is a directory", Files.isDirectory(testPath, LinkOption.NOFOLLOW_LINKS));
			Assert.assertTrue(testPath + " isn't readable", Files.isReadable(testPath));
		}

		return testPath;
	}

	private static Path getVerifiedInputDir(String dir) {
		Path readPath = Paths.get(dir);
		Assert.assertTrue(dir + " (" + readPath + ") isn't a directory",
				Files.isDirectory(readPath, LinkOption.NOFOLLOW_LINKS));

		return readPath.toAbsolutePath();
	}

	private static Path getVerifiedWorkDir(String dir, String fname) {
		Path testWriteFilePath = Paths.get(dir, fname);
		Path testPath = null;
		try {
			Files.deleteIfExists(testWriteFilePath);
			testPath = Files.createFile(testWriteFilePath);
			Files.delete(testPath);
			testPath = Paths.get(dir);
		} catch (IOException ex) {
			ex.printStackTrace();
			Assert.fail("unable to write to " + fname + " in " + dir + "; failure: " + ex.getMessage());
		}
		Assert.assertTrue(dir + " (" + testPath + ") isn't a directory",
				Files.isDirectory(testPath, LinkOption.NOFOLLOW_LINKS));

		return testPath.toAbsolutePath();
	}
}
