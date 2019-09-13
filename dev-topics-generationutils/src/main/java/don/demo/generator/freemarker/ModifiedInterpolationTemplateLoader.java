package don.demo.generator.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;

/**
 * Modify the normal Freemarker variable to use specialized opener.
 * Interpolation requests are formed by an opener, followed by a character
 * sequence, and ended with a closer. This implementation permits bypassing the
 * normal interpolation request; that is, requests started by the dollar-sign
 * and curly-brace opener, and replace it with a special form that will be
 * recognized on post-processing template write.
 * <p>
 * An example template loader is found <a href=
 * "https://docs.huihoo.com/freemarker/2.3.14/pgui_config_templateloading.html">here</a>
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
public class ModifiedInterpolationTemplateLoader extends FileTemplateLoader implements TemplateLoader, Serializable {
	private static final long serialVersionUID = 2437833785609374339L;

	public static final String DEFAULT_OPENER = "$";
	public static final String DEFAULT_REPLACMENT = "\u0001";
	public static final String DEFAULT_MAGIC = "@@";

	/**
	 * The normal opener we will replace with a special character
	 */
	private String opener = DEFAULT_OPENER;

	/**
	 * The special character, used to mark where a default opener was, so it is
	 * unrecognized by Freemarker interpolation
	 */
	private String replacement = DEFAULT_REPLACMENT;

	/**
	 * The magic here is the sequence the user codes, in place of the default
	 * opener, and will mark were Freemarker interpolation _should_ be done
	 */
	private String magic = DEFAULT_MAGIC;

	@SuppressWarnings("deprecation")
	public ModifiedInterpolationTemplateLoader() throws IOException {
		throw new UnsupportedOperationException("ModifiedInterpolationTemplateLoader() not implemented");
	}

	public ModifiedInterpolationTemplateLoader(final File baseDir) throws IOException {
		super(baseDir);
	}

	public ModifiedInterpolationTemplateLoader(final File baseDir, final boolean disableCanonicalPathCheck)
			throws IOException {
		super(baseDir, disableCanonicalPathCheck);
	}

	/**
	 * Read a template and optionally interpolate Freemarker expressions.
	 *
	 * @param templateSource the template file to read
	 * @param encoding       Java string encoding
	 *
	 * @return the content of the processed template (just a copy if no
	 *         interpolation needed.)
	 */
	@Override
	public Reader getReader(final Object templateSource, final String encoding) throws IOException {
		if (templateSource == null)
			throw new IllegalArgumentException("templateSource null");
		if (encoding == null)
			throw new IllegalArgumentException("encoding null");

		final Reader reader = super.getReader(templateSource, encoding);
		String rawContent = FreemarkerHelper.cacheFile(reader);

		//
		// If no opener or closer, then do no replacement
		if (getOpener() == null || getReplacement() == null) {
			return new StringReader(rawContent);
		}

		String replacedContent = FreemarkerHelper.replaceOpeners(rawContent, getOpener(), getReplacement());
		rawContent = null; // Just in case it is really big
		replacedContent = FreemarkerHelper.replaceMagic(replacedContent, getMagic(), "$");

		return new StringReader(replacedContent);
	}

	// ------------------------------------------------------------------------------------

	public String getOpener() {
		return opener;
	}

	public void setOpener(final String opener) {
		this.opener = opener;
	}

	public String getReplacement() {
		return replacement;
	}

	public void setReplacement(final String replacement) {
		this.replacement = replacement;
	}

	public String getMagic() {
		return magic;
	}

	public void setMagic(final String magic) {
		this.magic = magic;
	}

	// ------------------------------------------------------------------------------------

	@Override
	public String toString() {
		final StringBuilder msg = new StringBuilder(256);
		msg.append("{");
		msg.append(super.toString());
		msg.append("}[opener: ");
		if (opener == null || opener.length() < 1)
			msg.append("Nothing");
		else {
			msg.append(FreemarkerHelper.dumpAsHex(opener));
			msg.append("; (");
			msg.append(opener);
			msg.append(")");
		}
		msg.append(";  replacement: ");
		if (replacement == null || replacement.length() < 1)
			msg.append("Nothing");
		else {
			msg.append(FreemarkerHelper.dumpAsHex(replacement));
			msg.append("; (");
			msg.append(replacement);
			msg.append(")");
		}
		msg.append(";  magic: ");
		if (magic == null || magic.length() < 1)
			msg.append("Nothing");
		else {
			msg.append(magic);
		}
		msg.append("]");

		return msg.toString();
	}
}
