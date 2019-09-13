package don.demo.generator.arguments;

import java.io.Serializable;
import java.util.List;

import don.demo.generator.ParameterSetup.ParameterBean;

/**
 * This is an Arg4j Argument parsing implementation that packages arguments in a
 * bean
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
public interface ArgumentParser extends Serializable {
	/**
	 * RuntimeException message indicating special processing circumstance
	 */
	public static final String UNSPECIFIED_ARGUMENT_ERROR = "unspecified argument error";

	/**
	 * Returns results of argument parsing
	 *
	 * @author Donald Trummell (dtrummell@gmail.com)
	 */
	public static final class ParseBean implements Serializable {
		private static final long serialVersionUID = -9179999212429624505L;

		private final ParameterBean parameterBean;
		private final List<String> arguments;

		public ParseBean(final ParameterBean parameterBean, final List<String> arguments) {
			super();
			this.parameterBean = parameterBean;
			this.arguments = arguments;
		}

		public ParameterBean getParameterBean() {
			return parameterBean;
		}

		public List<String> getArguments() {
			return arguments;
		}

		@Override
		public String toString() {
			return "{" + getClass().getSimpleName() + " - 0x" + Integer.toHexString(hashCode())
					+ "[\n  parameterBean:\n" + parameterBean + ";\n\n  arguments: " + arguments + "]";
		}
	}

	/**
	 * Parse arguments and return the ParameterBean
	 *
	 * @param args command-line arguments passed from a <code>main</code> method
	 *
	 * @return packaged parameter values and a list of aerguments
	 *
	 * @throws IllegalArgumentException if a parsing error occurred
	 */
	public abstract ParseBean parseArgs(final String[] args);

	public abstract boolean isDisplayParameterErrors();

	public abstract void setDisplayParameterErrors(final boolean displayParameterErrors);
}