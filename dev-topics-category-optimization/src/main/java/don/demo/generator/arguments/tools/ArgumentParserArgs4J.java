package don.demo.generator.arguments.tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.springframework.stereotype.Service;

import don.demo.generator.ParameterSetup.ParameterBean;
import don.demo.generator.arguments.ArgumentParser;

/**
 * An Arg4J parser implementation that does some validation and packages
 * parameters in a bean. See <a href='http://args4j.kohsuke.org/'>project
 * home</a>, <a href='https://github.com/kohsuke/args4j'>source code</a> and
 * <a href='http://args4j.kohsuke.org/sample.html'>for an example</a>.
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
@Service("argumentparser")
public class ArgumentParserArgs4J implements ArgumentParser {
	private static final long serialVersionUID = 4208320039404481623L;

	private static final char mask = '`';

	private boolean displayParameterErrors = false;

	private String srcDir;
	private String templateList;

	private String defaultContext;
	private String overrideContextList;

	private String dstDir;
	private String generatedFileList;

	@Argument
	private List<String> arguments = new ArrayList<String>();

	/**
	 * The last problem encountered
	 */
	private RuntimeException problem = null;

	/**
	 * Construct me
	 */
	public ArgumentParserArgs4J() {
	}

	/**
	 * Return specific arguments for main program
	 */
	public ParseBean parseArgs(final String[] args) {
		if (args == null)
			throw new IllegalArgumentException("args null");

		problem = null;
		arguments.clear();

		final CmdLineParser parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
		} catch (CmdLineException ex) {
			problem = handleError(args, parser, "Command-line", ex);
			if (problem == null) {
				new IllegalStateException("error handler failed");
			}
		}

		if (problem != null)
			throw problem;

		final ParameterBean parameterBean = problem == null
				? new ParameterBean(srcDir, ArgumentUtils.commaToList(templateList, mask), defaultContext,
						(overrideContextList == null) ? new String[0]
								: ArgumentUtils.commaToList(overrideContextList, mask),
						dstDir, ArgumentUtils.commaToList(generatedFileList, mask))
				: null;

		final ParseBean parseBean = new ParseBean(parameterBean, new ArrayList<String>(arguments));
		return parseBean;
	}

	/**
	 * Message formatter for argument error handling
	 * 
	 * @param args   arge to parse
	 * @param parser parser instance
	 * @param label  error message label
	 * @param ex     exception causing the parse problem
	 * 
	 * @return a RuntimeException instance capturing error information
	 */
	public RuntimeException handleError(final String[] args, final CmdLineParser parser, final String label,
			final Throwable ex) {
		if (args == null) {
			throw new IllegalArgumentException("args null");
		}
		if (parser == null) {
			throw new IllegalArgumentException("parser null");
		}
		if (ex == null) {
			throw new IllegalArgumentException("ex null");
		}

		RuntimeException error = null;
		if (displayParameterErrors) {
			error = ArgumentUtils.showParseError(args, parser, label, ex);
			if (error == null) {
				//
				// Pass back to indicate probable no arguments
				error = new RuntimeException(UNSPECIFIED_ARGUMENT_ERROR);
			}
		} else {
			StringBuilder msg = new StringBuilder();
			if (args != null && args.length == 1) {
				final String targ = String.valueOf(args[0]).toLowerCase();
				if ("h".equals(targ) || "help".equals(targ)) {
					msg.append("HELP requested");
				} else {
					msg.append("unrecognized argument error [");
					msg.append(label);
					msg.append("]: ");
					msg.append(Arrays.toString(args));
					msg.append("\n");
				}
			}
			error = new RuntimeException(msg.toString(), ex);
		}

		return error;
	}

	public String getSrcDir() {
		return srcDir;
	}

	@Option(name = "-srcDir", usage = "Defines base directory for all text template (source) files")
	public void setSrcDir(final String srcDir) {
		this.srcDir = srcDir;
	}

	public String getTemplateList() {
		return templateList;
	}

	@Option(name = "-templateList", required = true, usage = "A comma separated list source file names, found under srcDir, of templates to process")
	public void setTemplateList(final String templateList) {
		this.templateList = templateList;
	}

	public String getDefaultContext() {
		return defaultContext;
	}

	@Option(name = "-defaultContext", required = true, usage = "A file with properties definitions to use as the primary context")
	public void setDefaultContext(final String defaultContext) {
		this.defaultContext = defaultContext;
	}

	public String getOverrideContextList() {
		return overrideContextList;
	}

	@Option(name = "-overrideContextList", usage = "A comma separated list file names of properties definitions to augment the primary context")
	public void setOverrideContextList(final String overrideContextList) {
		this.overrideContextList = overrideContextList;
	}

	public String getDstDir() {
		return dstDir;
	}

	@Option(name = "-dstDir", required = true, usage = "Defines base target directory for all generated text files")
	public void setDstDir(final String dstDir) {
		this.dstDir = dstDir;
	}

	public String getGeneratedFileList() {
		return generatedFileList;
	}

	@Option(name = "-generatedFileList", required = true, usage = "A comma separated list file names, found under dstDir, of generated files from processing templates and contexts")
	public void setGeneratedFileList(final String generatedFileList) {
		this.generatedFileList = generatedFileList;
	}

	public boolean isDisplayParameterErrors() {
		return displayParameterErrors;
	}

	public void setDisplayParameterErrors(final boolean displayParameterErrors) {
		this.displayParameterErrors = displayParameterErrors;
	}

	public RuntimeException getProblem() {
		return problem;
	}
}
