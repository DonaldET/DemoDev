package don.demo.generator.model;

import java.io.Serializable;
import java.util.Properties;

/**
 * Incrementally build a properties model for use by a generator
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
public interface PropertyModelBuilder extends Serializable {

	/**
	 * Augments the model with additional Java properties, optionally overriding
	 * existing properties
	 * 
	 * @param properties optionally null or empty properties to add
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