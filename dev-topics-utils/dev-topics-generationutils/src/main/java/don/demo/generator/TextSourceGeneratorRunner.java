package don.demo.generator;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import don.demo.generator.TextSourceGenerator.Result;
import don.demo.generator.arguments.ArgumentParser;

/**
 * Run the generator passing in command-line arguments
 * 
 * @author dtrumme
 */
public class TextSourceGeneratorRunner implements Serializable {
    private static final long serialVersionUID = -6173219947683706066L;

    private static final String APP_CONTEXT_PATH = "classpath:META-INF/main/spring/app-main-context.xml";

    private static final Logger LOGGER = Logger
            .getLogger(TextSourceGeneratorRunner.class);

    /**
     * Read and parse template(s) and produce generated output based on model;
     * all required inputs are passed in using command-line arguments
     * 
     * @param args
     *            command-line arguments
     */
    public static void main(final String[] args) {
        System.out.println("\nTextSourceGeneratorRunner");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("\n========================================================="
                    + "\n                                                         "
                    + "\n          TextSourceGenerator Runner                     "
                    + "\n                                                         "
                    + "\n=========================================================");
        }

        final TextSourceGenerator generator = readyApplication();
        final Result result = processWithArgs(generator, args);

        final boolean failed = result == null;
        if (failed) {
            System.err.println("Generator run failed!\n");
        } else {
            System.out.println("\nGeneration Information:\n"
                    + String.valueOf(result));
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
     * @param generator
     *            source code generator to use
     * @param args
     *            specification of files and models to process
     * 
     * @return a <code>Result</code> instance or <code>null</code> if failed
     */
    private static Result processWithArgs(final TextSourceGenerator generator,
            final String[] args) {

        Result result = null;
        try {
            result = generator.process(args);
        } catch (RuntimeException rex) {
            if (!ArgumentParser.UNSPECIFIED_ARGUMENT_ERROR.equals(rex
                    .getMessage())) {
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
        System.err.println("  Loading context " + APP_CONTEXT_PATH);

        return new ClassPathXmlApplicationContext(APP_CONTEXT_PATH);
    }
}
