package don.demo.generator.wrappers;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import don.demo.generator.Generator;
import don.demo.generator.InputOutputBuilder;
import don.demo.generator.ModelComposer;
import don.demo.generator.ParameterSetup;
import don.demo.generator.TextSourceGenerator;

/**
 * Incrementally build properties model; this implementation is a Mediator
 * orchestratring the parameters and models construction with invoking the
 * generator.
 * <p>
 * This generator relies on a special implementation of Freemarker interfaces
 * <code>FileTemplateLoader</code> and <code>TemplateLoader</code>, which uses
 * "@at;@at;{" as an interpolation opener instead of the standard "${" (see
 * <code>ModifiedInterpolationTemplateLoader</code>.)
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
 * 
 *         <pre>
 * <code>
 * <strong>Note</strong>: See <a href="http://www.benf.org/other/cfr/faq.html">for decompiling</a>
 * </code>
 *         </pre>
 */
public class TextSourceGeneratorImpl implements Serializable, TextSourceGenerator {
	private static final long serialVersionUID = 1503924918398309281L;
	private static final Logger LOGGER = Logger.getLogger(TextSourceGeneratorImpl.class);
	private ParameterSetup parameterSetup;
	private ModelComposer modelComposer;
	private InputOutputBuilder inputOutputBuilder;
	private Generator generator;

	@Override
	public TextSourceGenerator.Result process(String[] args) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info((Object) "Entering generation process");
		}

		final ParameterSetup.ParameterBean parameterBean = this.parameterSetup.setupArgs(args);
		final Properties composedPropertiesModel = this.modelComposer.composeModel(parameterBean.getDefaultContext(),
				parameterBean.getOverrideContextList());
		final String templateBaseDir = parameterBean.getSrcDir();
		final List<Map.Entry<String, String>> pairs = this.inputOutputBuilder.createPairedList(templateBaseDir,
				parameterBean.getTemplateList(), parameterBean.getDstDir(), parameterBean.getGeneratedFileList(),
				false);
		final Generator.GenStats genstats = this.generator.generate(composedPropertiesModel, templateBaseDir, pairs);
		final TextSourceGenerator.Result result = this.summarize(parameterBean, genstats);
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info((Object) ("Completed generation process: " + String.valueOf(result)));
		}

		return result;
	}

	private TextSourceGenerator.Result summarize(ParameterSetup.ParameterBean parameterBean,
			Generator.GenStats genstats) {
		int filesRead = genstats.getFilesRead();
		int filesWritten = genstats.getFilesWritten();
		String srcDir = parameterBean.getSrcDir();
		String[] templateList = parameterBean.getTemplateList();
		String defaultContext = parameterBean.getDefaultContext();
		String[] overrideContextList = parameterBean.getOverrideContextList();
		String dstDir = parameterBean.getDstDir();
		String[] generatedFileList = parameterBean.getGeneratedFileList();
		return new TextSourceGenerator.Result(srcDir, templateList, defaultContext, overrideContextList, dstDir,
				generatedFileList, filesRead, filesWritten);
	}

	public ParameterSetup getParameterSetup() {
		return this.parameterSetup;
	}

	@Resource(name = "tsgparametersetup")
	public void setParameterSetup(ParameterSetup parameterSetup) {
		this.parameterSetup = parameterSetup;
	}

	public ModelComposer getModelComposer() {
		return this.modelComposer;
	}

	@Resource(name = "tsgcomposemodel")
	public void setModelComposer(ModelComposer modelComposer) {
		this.modelComposer = modelComposer;
	}

	public InputOutputBuilder getInputOutputBuilder() {
		return this.inputOutputBuilder;
	}

	@Resource(name = "tsginputoutputbuilder")
	public void setInputOutputBuilder(InputOutputBuilder inputOutputBuilder) {
		this.inputOutputBuilder = inputOutputBuilder;
	}

	public Generator getGenerator() {
		return this.generator;
	}

	@Resource(name = "tsggenerator")
	public void setGenerator(Generator generator) {
		this.generator = generator;
	}
}
