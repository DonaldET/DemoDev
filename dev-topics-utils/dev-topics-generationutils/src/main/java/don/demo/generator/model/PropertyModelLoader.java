package don.demo.generator.model;

import java.io.Serializable;
import java.util.Properties;

/**
 * Obtain a properties file that serves as the model, defining substitution
 * variables for interpolation, from a path. The path may be a file, a
 * class-path, or a URL for <code>Spring</code> based implementations.
 * 
 * @author dtrumme
 */
public interface PropertyModelLoader extends Serializable {

    /**
     * Load a properties file from the specification
     * 
     * @param propertiesSpec
     *            a file, class-path, or URL specification.
     * 
     * @return a properties object populated from the parameter
     */
    public abstract Properties loadProperties(final String propertiesSpec);
}