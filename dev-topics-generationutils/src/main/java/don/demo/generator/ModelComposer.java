package don.demo.generator;

import java.io.Serializable;
import java.util.Properties;

/**
 * Create a model/context for code generation from a template
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
public interface ModelComposer extends Serializable
{

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
    public abstract Properties composeModel(String defaultContext, String overrideContextList[]);
}