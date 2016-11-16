package don.demo.generator.model;

import java.io.Serializable;
import java.util.Properties;

/**
 * Incrementally build properties model
 * 
 * @author dtrumme
 */
public interface PropertyModelBuilder extends Serializable {

    /**
     * Augments the model with additional properties, optionally overriding
     * existing properties
     * 
     * @param properties
     *            optionally null or empty properties to add
     * 
     * @return this instance of the builder
     */
    public abstract PropertyModelBuilder augment(final Properties properties);

    /**
     * Computes current state of model
     * 
     * @return the internal model
     */
    public abstract Properties build();
    
    /**
     * Clear any associated properties
     */
    public abstract void clear();
}