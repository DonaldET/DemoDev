package don.demo.generator.wrappers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import don.demo.generator.Generator;
import don.demo.generator.freemarker.FreemarkerHelper;
import don.demo.generator.freemarker.ModifiedInterpolationTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * A generator uses a model and list of source-template pairs to produce generated results.
 * 
 * @author Donald Trummell
 * 
 *         Copyright (c) 2016. Donald Trummell. All Rights Reserved. Permission
 *         to use, copy, modify, and distribute this software and its
 *         documentation for educational, research, and not-for-profit purposes,
 *         without fee and without a signed licensing agreement, is hereby
 *         granted, provided that the above copyright notice, and this
 *         paragraph, appear in all copies, modifications, and distributions.
 *         Contact dtrummell@gmail.com for commercial licensing opportunities.
 * 
 * <pre>
 * <code>
 * <strong>Note</strong>: See <a href="http://www.benf.org/other/cfr/faq.html for decompiling">decompiling</a></strong>
 * </code>
 * </pre>
 */
@Service(value = "tsggenerator")
public class TSGGenerator implements Generator {
	private static final long serialVersionUID = 5003949308482754668L;
	// private static final int TEMPLATE_BUFFER_SIZE = 1048576;

	@Override
	public Generator.GenStats generate(Properties composedPropertiesModel, List<Map.Entry<String, String>> pairs) {
		throw new UnsupportedOperationException("requires template (source) base directory");
	}

	@Override
	public Generator.GenStats generate(Properties composedPropertiesModel, String srcDir,
			List<Map.Entry<String, String>> pairs) {
		this.checkPreConditions(composedPropertiesModel, srcDir, pairs);
		int read = 0;
		int written = 0;
		if (pairs.size() > 0) {
			File templateBaseDir = this.processBaseDir(srcDir);
			String baseDir = templateBaseDir.getPath();
			FileTemplateLoader fileTemplateLoader = FreemarkerHelper.createFileTemplateLoader(templateBaseDir);
			assert (fileTemplateLoader != null) : "createFileTemplateLoader returned null";
			String replacement = ((ModifiedInterpolationTemplateLoader) fileTemplateLoader).getReplacement();
			FreemarkerHelper.ConfigAndLoader cfgLoader = FreemarkerHelper.configureFreemarker(fileTemplateLoader);
			Configuration cfg = cfgLoader.getConfiguration();
			for (Map.Entry<String, String> pair : pairs) {
				String templateFileName = pair.getKey();
				Template template = FreemarkerHelper.retrieveTemplate(cfg, baseDir, templateFileName);
				++read;
				StringWriter generatedSrc = new StringWriter(1048576);
				try {
					template.process((Object) composedPropertiesModel, (Writer) generatedSrc);
					generatedSrc.flush();
				} catch (TemplateException | IOException ex) {
					throw new IllegalArgumentException("unable to process template " + templateFileName + " at "
							+ baseDir + "; message: " + ex.getMessage(), (Throwable) ex);
				}
				String actualVal = generatedSrc.toString();
				generatedSrc = null;
				actualVal = FreemarkerHelper.replaceOpeners(actualVal, replacement, "$");
				this.writeOutputFile(actualVal, pair.getValue());
				++written;
			}
		}
		return new Generator.GenStats(read, written);
	}

	private void checkPreConditions(Properties composedPropertiesModel, String srcDir,
			List<Map.Entry<String, String>> pairs) {
		if (composedPropertiesModel == null) {
			throw new IllegalArgumentException("composedPropertiesModel null");
		}
		if (srcDir == null) {
			throw new IllegalArgumentException("srcDir null");
		}
		if (srcDir.isEmpty()) {
			throw new IllegalArgumentException("srcDir empty");
		}
		if (pairs == null) {
			throw new IllegalArgumentException("pairs null");
		}
	}

	/*
	 * Enabled force condition propagation Lifted jumps to return sites
	 */
	private File processBaseDir(String srcDir) {
		File baseDir = new File(srcDir);
		if (!baseDir.exists())
			throw new IllegalArgumentException("base directory " + baseDir.getPath() + " does not exist");
		if (!baseDir.isDirectory())
			throw new IllegalArgumentException("base directory " + baseDir.getPath() + " does not a directory");
		if (!baseDir.canRead())
			throw new IllegalArgumentException("base directory " + baseDir.getPath() + " not readable");
		try {
			return baseDir.getCanonicalFile();
		} catch (IOException ex) {
			throw new IllegalArgumentException(String.valueOf(srcDir) + " got error: " + ex.getMessage(), ex);
		}
	}

	private void writeOutputFile(String actualVal, String outputFileName) {
		PrintWriter generatedPW = null;
		FileOutputStream generatedFile = null;
		try {
			try {
				generatedFile = new FileOutputStream(outputFileName);
				generatedPW = new PrintWriter(generatedFile);
				generatedPW.write(actualVal);
				generatedPW.flush();
			} catch (FileNotFoundException fnfEx) {
				throw new IllegalArgumentException("unable to write out generated file " + outputFileName);
			}
		} finally {
			if (generatedPW != null) {
				generatedPW.close();
				generatedPW = null;
			} else if (generatedFile != null) {
				try {
					generatedFile.close();
				} catch (IOException iOException) {
				}
				generatedFile = null;
			}
		}
	}
}
