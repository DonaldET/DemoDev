package don.demo.generator;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import don.demo.generator.TextSourceGenerator.Result;
import don.demo.generator.arguments.ArgumentParser;

/**
 * Run the generator passing in command-line arguments
 * 
 * @author Donald Trummell
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
	private static final long serialVersionUID = -6173219947683706066L;

	private static final String APP_BEANS_CONTEXT_PATH_FILE = "classpath:META-INF/main/spring/app-context.xml";
	private static final String APP_BEANS_CONTEXT_PATH_JAR = "classpath:/resources/META-INF/main/spring/app-context.xml";
	private static final String MAIN_APP_CONTEXT_PATH_FILE = "classpath:META-INF/main/spring/app-main-context.xml";
	private static final String MAIN_APP_CONTEXT_PATH_JAR = "classpath:/resources/META-INF/main/spring/app-main-context.xml";

	private static final Logger LOGGER = Logger.getLogger(TextSourceGeneratorRunner.class);

	/**
	 * Read and parse template(s) and produce generated output based on model; all
	 * required inputs are passed in using command-line arguments
	 * 
	 * @param args command-line arguments
	 */
	public static void main(final String[] args) {
		System.out.println(
				"\nTextSourceGeneratorRunner - Copyright (c) 2019. Donald Trummell. All Rights Reserved" + "\n");
		System.out.flush();
		Path basePath = Paths.get("./");
		String baseDirPath = basePath.toAbsolutePath().toString();

		System.out.println("\n");
		System.out.flush();
		System.out.println("\n  Working in path      : " + baseDirPath + "\n");
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
	private static TextSourceGenerator readyApplication() {
		myLoadSource();
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
	private static Result processWithArgs(final TextSourceGenerator generator, final String[] args) {

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
		final String loadedFrom = myLoadSource();
		final String beansAppContextPath = loadedFrom == null ? "null"
				: loadedFrom.toLowerCase().startsWith("file:") ? APP_BEANS_CONTEXT_PATH_FILE
						: APP_BEANS_CONTEXT_PATH_JAR;
		// System.err.println(" Loading beans application context " +
		// beansAppContextPath + " from URL " + loadedFrom);

		final String mainAppContextPath = loadedFrom == null ? "null"
				: loadedFrom.toLowerCase().startsWith("file:") ? MAIN_APP_CONTEXT_PATH_FILE : MAIN_APP_CONTEXT_PATH_JAR;
		// System.err.println(" Loading main applicaiton context " + mainAppContextPath
		// + " from URL " + loadedFrom);
		return new ClassPathXmlApplicationContext(new String[] { beansAppContextPath, mainAppContextPath });
	}

	private static String myLoadSource() {
		final ClassLoader cl = TextSourceGeneratorRunner.class.getClassLoader();
		return String.valueOf(cl.getResource("don/demo/generator/TextSourceGeneratorRunner.class"));
	}
}
