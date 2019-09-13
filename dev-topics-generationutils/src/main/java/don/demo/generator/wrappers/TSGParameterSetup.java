package don.demo.generator.wrappers;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import don.demo.generator.ParameterSetup;
import don.demo.generator.arguments.ArgumentParser;

/**
 * Capture parameters for generation process.
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
 *
 *         <pre>
 * <code>
 * <strong>Note</strong>:
 * See <a href=
"http://www.benf.org/other/cfr/faq.html"> for decompiling</a>
 * </code>
 *         </pre>
 */
@Service(value = "tsgparametersetup")
public class TSGParameterSetup implements ParameterSetup {
	private static final long serialVersionUID = 2205886572230294731L;

	private ArgumentParser argParser;

	@Override
	public ParameterSetup.ParameterBean setupArgs(final String[] args) {
		assert (args != null) : "args null";
		assert (this.argParser != null) : "argParser null";
		final ArgumentParser.ParseBean parsedArgs = this.argParser.parseArgs(args);
		assert (parsedArgs != null) : "parsedArgs null";
		final ParameterSetup.ParameterBean parameterBean = parsedArgs.getParameterBean();
		assert (parameterBean != null) : "parameterBean arguments null";
		return parameterBean;
	}

	public ArgumentParser getArgParser() {
		return this.argParser;
	}

	@Resource(name = "argumentparser")
	public void setArgParser(final ArgumentParser argParser) {
		this.argParser = argParser;
	}
}
