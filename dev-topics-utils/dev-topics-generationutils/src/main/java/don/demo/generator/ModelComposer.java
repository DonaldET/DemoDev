package don.demo.generator;

import java.io.Serializable;
import java.util.Properties;

/**
 * Create a model/context for code generation from a template
 * 
 * @author dtrumme
 */
public interface ModelComposer extends Serializable {

    /**
     * Compose model/context from a default and override version of the required
     * model/context
     * 
     * @param defaultContext
     *            the basic model with replaceable parameters
     * @param overrideContextList
     *            the optional context which may also override the default
     *            model/context
     * 
     * @return the fully composed model/context created from the inputs
     */
    public abstract Properties composeModel(String defaultContext,
            String overrideContextList[]);
}