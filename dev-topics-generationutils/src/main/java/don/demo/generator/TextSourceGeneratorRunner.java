package don.demo.generator;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import don.demo.generator.TextSourceGenerator.Result;
import don.demo.generator.arguments.ArgumentParser;

/**
 * Run the Freemarker-based generator by passing in command-line arguments and
 * executing the generator implementation. Freemarker resources are:
 * <ul>
 * <li><a href="https://freemarker.apache.org/">Freemarker site</a></li>
 * <li><a href="http://zetcode.com/java/freemarker/">Freemarker
 * tutorial-1</a></li>
 * <li><a href=
 * "https://www.vogella.com/tutorials/FreeMarker/article.html">Freemarker
 * tutorial-2</a></li>
 * <li><a href="https://freemarker.apache.org/docs/index.html">Freemarker
 * manual</a></li>
 * </ul>
 * Sample Eclipse test run parameters:
 *
 * <pre>
 * <code>
 * -defaultContext D:\GitHub\DemoDev\dev-topics-generationutils\src\test\resources\app_config\shared_defs.properties -overrideContextList D:\GitHub\DemoDev\dev-topics-generationutils\src\test\resources\app_config\target_defs.properties -srcDir D:\GitHub\DemoDev\dev-topics-generationutils\src\test\resources\app_src -templateList hive_query.tpl -dstDir D:\GitHub\DemoDev\dev-topics-generationutils\.\target -generatedFileList hive_query.hql
 * </code>
 * </pre>
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 *         Copyright (c) 2019. Donald Trummell. All Rights Reserved. Permission
 *         to use, copy, modify, and distribute this software and its
 *         documentation for educational, research, and not-for-profit purposes,
 *         without fee and without a signed licensing agreement, is hereby
 *         granted, provided that the above copyright notice, and this
 *         paragraph, appear in all copies, modifications, and distributions.
 *         Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
public class TextSourceGeneratorRunner implements Serializable {
	private static final long serialVersionUID = 2679304604204119379L;

	private static final String RESOURCE_BASE = "don/demo/generator/TextSourceGeneratorRunner.class";
	private static final String[] CONTEXT_PATHS = { "classpath:%s/META-INF/main/spring/app-context.xml",
			"classpath:%s/META-INF/main/spring/app-main-context.xml" };

	private static final Logger LOGGER = Logger.getLogger(TextSourceGeneratorRunner.class);

	/**
	 * Read and parse template(s) and produce generated output based on model; all
	 * required inputs are passed in using command-line arguments
	 *
	 * @param args command-line arguments
	 */
	public static void main(final String[] args) {
		System.out.println("\n\nTextSourceGeneratorRunner - Copyright (c) 2019. Donald Trummell. All Rights Reserved");
		System.out.flush();

		Path basePath = Paths.get("./");
		String baseDirPath = basePath.toAbsolutePath().toString();

		System.out.println("\n");
		System.out.println("\n  Working in path      : " + baseDirPath);
		System.out.flush();

		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\n========================================================="
					+ "\n                                                         "
					+ "\n          TextSourceGenerator Runner                     "
					+ "\n             Copyright (c) 2019. Donald Trummell         "
					+ "\n=========================================================" + "\nWorking in " + baseDirPath);
		}

		final TextSourceGenerator generator = readyApplication();
		final Result result = processWithArgs(generator, args);

		final boolean failed = result == null;
		if (failed) {
			final String msg = "Generator run failed!\n";
			System.err.println(msg);
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(msg);
			}
		} else {
			final String msg = "\nGeneration Information:\n" + String.valueOf(result);
			System.out.println(msg);
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(msg);
			}
		}

		System.out.println("\nTextSourceGeneratorRunner - done");
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("\n========================================================="
					+ "\n                                                         "
					+ "\n          TextSourceGenerator Done                       "
					+ "\n                                                         "
					+ "\n=========================================================");
		}

		if (failed) {
			System.exit(1);
		}
	}

	// -----------------------------------------------------------------------------------------------------------------

	/**
	 * Load spring and get bean with processing implementation
	 */
	public static TextSourceGenerator readyApplication() {
		runnerLoadSource();
		final ClassPathXmlApplicationContext springContext = getSpringAppContext();
		final String configVersion = (String) springContext.getBean("version");
		final String msg = "  Configuration version: " + configVersion;
		System.out.println(msg);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(msg);
		}

		return (TextSourceGenerator) springContext.getBean("generator");
	}

	/**
	 * Process files defined by arguments using the generator. Parser validates
	 * correct arguments but does not check file existence and other details
	 *
	 * @param generator source code generator to use
	 * @param args      specification of files and models to process
	 *
	 * @return a <code>Result</code> instance or <code>null</code> if failed
	 */
	public static Result processWithArgs(final TextSourceGenerator generator, final String[] args) {

		Result result = null;
		try {
			result = generator.process(args);
		} catch (RuntimeException rex) {
			if (!ArgumentParser.UNSPECIFIED_ARGUMENT_ERROR.equals(rex.getMessage())) {
				throw rex;
			}
		}

		return result;
	}

	/**
	 * Load the application context using streams so they can reside in a jar
	 *
	 * @return the populated context
	 */
	private static ClassPathXmlApplicationContext getSpringAppContext() {
		final String loadedFrom = runnerLoadSource();
		if (loadedFrom == null) {
			throw new IllegalStateException("unable to load resource " + RESOURCE_BASE);
		}
		final boolean jarPath = loadedFrom.toLowerCase().startsWith("jar:");
		String[] paths = Arrays.stream(CONTEXT_PATHS).map((String s) -> String.format(s, jarPath ? "" : "\resources"))
				.toArray(String[]::new);
		ClassPathXmlApplicationContext ctx = loadSpringContextFiles(paths);
		if (ctx == null) {
			paths = Arrays.stream(CONTEXT_PATHS).map((String s) -> String.format(s, !jarPath ? "" : "\resources"))
					.toArray(String[]::new);
			ctx = loadSpringContextFiles(paths);
			if (ctx == null) {
				throw new IllegalStateException("unable to load " + loadedFrom);
			}
		}

		return ctx;
	}

	private static ClassPathXmlApplicationContext loadSpringContextFiles(final String[] contextPaths) {
		ClassPathXmlApplicationContext ctx = null;
		try {
			ctx = new ClassPathXmlApplicationContext(contextPaths);
		} catch (final Exception ex) {
			if (ex instanceof BeanDefinitionStoreException) {
				ctx = null;
			} else {
				throw new IllegalStateException(
						"unable to load context paths: (" + Arrays.deepToString(contextPaths) + ")");
			}
		}

		return ctx;
	}

	private static String runnerLoadSource() {
		final ClassLoader cl = TextSourceGeneratorRunner.class.getClassLoader();
		return String.valueOf(cl.getResource(RESOURCE_BASE));
	}
}
