package don.demo.generator.wrapper;

import don.demo.generator.ModelComposer;
import don.demo.generator.model.PropertyModelBuilder;
import don.demo.generator.model.PropertyModelLoader;
import java.io.PrintStream;
import java.util.Properties;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * Compose a model by overriding default properties with detail properties.
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
@Service(value = "tsgcomposemodel")
public class TSGComposeModel implements ModelComposer {
	private static final long serialVersionUID = 7524113330107131559L;
	private PropertyModelLoader modelLoader;
	private PropertyModelBuilder modelBuilder;
	private boolean trace = false;

	@Override
	public Properties composeModel(final String defaultContext, final String[] overrideContextList) {
		if (defaultContext == null) {
			throw new IllegalArgumentException("defaultContext null");
		}
		if (defaultContext.isEmpty()) {
			throw new IllegalArgumentException("defaultContext empty");
		}
		Properties subModel = null;
		try {
			if (this.trace) {
				System.err.println("  - Loading default context " + defaultContext);
			}
			subModel = this.modelLoader.loadProperties(defaultContext);
		} catch (RuntimeException rex) {
			throw new IllegalArgumentException("unable to load default context " + defaultContext, rex);
		}
		this.modelBuilder.clear();
		this.modelBuilder.augment(subModel);
		if (overrideContextList != null) {
			int ocIndex = 0;
			final String[] arrstring = overrideContextList;
			int numOvrCtx = arrstring.length;
			int workingOvrCtx = 0;
			while (workingOvrCtx < numOvrCtx) {
				final String ovrCtx = arrstring[workingOvrCtx];
				try {
					if (this.trace) {
						System.err.println("  - Loading override context " + ovrCtx);
					}
					subModel = this.modelLoader.loadProperties(ovrCtx);
				} catch (RuntimeException rex) {
					throw new IllegalArgumentException("unable to load override context " + ovrCtx + " [" + ocIndex
							+ "] of default context " + defaultContext, rex);
				}
				this.modelBuilder.augment(subModel);
				++ocIndex;
				++workingOvrCtx;
			}
		}
		return this.modelBuilder.build();
	}

	public PropertyModelLoader getModelLoader() {
		return this.modelLoader;
	}

	@Resource(name = "modelloader")
	public void setModelLoader(PropertyModelLoader modelLoader) {
		this.modelLoader = modelLoader;
	}

	public PropertyModelBuilder getModelBuilder() {
		return this.modelBuilder;
	}

	@Resource(name = "modelbuilder")
	public void setModelBuilder(PropertyModelBuilder modelBuilder) {
		this.modelBuilder = modelBuilder;
	}

	public boolean isTrace() {
		return this.trace;
	}

	public void setTrace(boolean trace) {
		this.trace = trace;
	}
}
