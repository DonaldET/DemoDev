package don.demo.generator.model;

import java.io.Serializable;
import java.util.Properties;

/**
 * Obtain a properties file that serves as the model, defining substitution
 * variables for interpolation, from a path. The path may be a file, a
 * class-path, or a URL for <code>Spring</code> based implementations.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 *         Copyright (c) 2016. Donald Trummell. All Rights Reserved. Permission
 *         to use, copy, modify, and distribute this software and its
 *         documentation for educational, research, and not-for-profit purposes,
 *         without fee and without a signed licensing agreement, is hereby
 *         granted, provided that the above copyright notice, and this
 *         paragraph, appear in all copies, modifications, and distributions.
 *         Contact dtrummell@gmail.com for commercial licensing opportunities.
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