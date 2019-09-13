package don.demo.generator;

import java.io.Serializable;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * A generated creates text output from templates and model/context properties
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
public interface Generator extends Serializable {

	/**
	 * Returns results of generation
	 *
	 * @author Donald Trummell (dtrummell@gmail.com)
	 */
	public static final class GenStats implements Serializable {
		private static final long serialVersionUID = 1908356674482313616L;
		private final int filesRead;
		private final int filesWritten;

		public GenStats(final int filesRead, final int filesWritten) {
			super();
			this.filesRead = filesRead;
			this.filesWritten = filesWritten;
		}

		public int getFilesRead() {
			return filesRead;
		}

		public int getFilesWritten() {
			return filesWritten;
		}

		@Override
		public String toString() {
			return "GenStats [filesRead=" + filesRead + ", filesWritten=" + filesWritten + "]";
		}

		@Override
		public int hashCode() {
			final int prime = 947;

			int result = 1;
			result = prime * result + filesRead;
			result = prime * result + filesWritten;

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
			final GenStats other = (GenStats) obj;
			if (filesRead != other.filesRead)
				return false;
			if (filesWritten != other.filesWritten)
				return false;
			return true;
		}
	}

	/**
	 * Generate output files from input files at the source directory using the
	 * model/context
	 *
	 * @param composedPropertiesModel the model or context for code generation
	 * @param srcDir                  the common source directory for the templates
	 *                                in the pair list
	 * @param pairs                   the input-output specifications for generation
	 *
	 * @return a <code>GenStats</code> instance summarizing processing
	 */
	public abstract GenStats generate(final Properties composedPropertiesModel, final String srcDir,
			final List<Entry<String, String>> pairs);

	/**
	 * Generate output files from input files using the model/context
	 *
	 * @param composedPropertiesModel the model or context for code generation
	 * @param pairs                   the input-output specifications for generation
	 *
	 * @return a <code>GenStats</code> instance summarizing processing
	 */
	public abstract GenStats generate(final Properties composedPropertiesModel,
			final List<Entry<String, String>> pairs);
}