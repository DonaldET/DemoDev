package don.demo.generator.model.impl;

import java.util.Properties;

import org.springframework.stereotype.Service;

import don.demo.generator.model.PropertyModelBuilder;

/**
 * Accumulates Java properties with augmentation properties, overriding previous
 * values properties
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
@Service("modelbuilder")
public class PropertyModelBuilderImpl implements PropertyModelBuilder, Cloneable {
	private static final long serialVersionUID = -3698564031525804109L;
	private final Properties model = new Properties();

	public PropertyModelBuilderImpl() {
	}

	public PropertyModelBuilderImpl(final Properties initialModel) {
		if (initialModel == null) {
			throw new IllegalArgumentException("initialModel null");
		}
		if (!initialModel.isEmpty()) {
			model.putAll(initialModel);
		}
	}

	/**
	 * Add properties to model and return this builder instance
	 * 
	 * @param properties optionally null or empty augmentation properties
	 * 
	 * @return this instance of the <code>PropertyModelBuilder</code?
	 */
	@Override
	public PropertyModelBuilder augment(final Properties properties) {
		if (properties != null)
			this.model.putAll(properties);

		return this;
	}

	/**
	 * Returns the current properties associated with the model
	 * 
	 * @return a non-null copy of the associated properties
	 */
	@Override
	public Properties build() {
		final Properties copy = new Properties();
		copy.putAll(this.model);

		return copy;
	}

	/**
	 * Clear any associated properties
	 */
	@Override
	public void clear() {
		model.clear();
	}

	// -----------------------------------------------------------------------------------------------------------------

	/**
	 * Provide deep copy
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		final PropertyModelBuilderImpl copy = (PropertyModelBuilderImpl) super.clone();
		final Properties values = copy.build();
		copy.model.clear();
		copy.augment(values);

		return copy;
	}

	@Override
	public int hashCode() {
		final int prime = 2309;
		int result = 1;
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PropertyModelBuilderImpl other = (PropertyModelBuilderImpl) obj;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[PropertyModelBuilderImpl -0x" + Integer.toHexString(hashCode()) + ";  model=" + model + "]";
	}
}
