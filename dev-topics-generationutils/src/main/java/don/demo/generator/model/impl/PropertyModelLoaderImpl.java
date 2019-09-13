package don.demo.generator.model.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import don.demo.generator.model.PropertyModelLoader;

/**
 * Load a properties resource from a file path, class path, or URL; the
 * Properties object is a Map and serves as a Freemarker data model. Uses
 * concepts from Spring resource loaders (see <a href=
 * 'http://docs.spring.io/spring/docs/current/spring-framework-reference/html/resources.htm'>Spr
 * i n g Resource Loading</a>.
 *
 * <table style="width:100%" summary="Spring Resource Loader Details">
 * <tr>
 * <th>Prefix</th>
 * <th>Example</th>
 * <th>Explanation</th>
 * </tr>
 * <tr>
 * <td>classpath:</td>
 * <td>classpath:com/myapp/config.xml</td>
 * <td>Loaded from the classpath.</td>
 * </tr>
 * <tr>
 * <td>file:</td>
 * <td>file:///data/config.xml</td>
 * <td>Loaded as a URL, from the filesystem.</td>
 * </tr>
 * <tr>
 * <td>http:</td>
 * <td>http://myserver/logo.png</td>
 * <td>Loaded as a URL.</td>
 * </tr>
 * </table>
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
@Service("modelloader")
public class PropertyModelLoaderImpl implements PropertyModelLoader {
	private static final long serialVersionUID = 1640881080369640241L;

	@Autowired
	private ApplicationContext ctx;

	public PropertyModelLoaderImpl() {
	}

	/**
	 * Load properties using a <code>Spring</code> resource loader; default to file
	 * resource type.
	 */
	@Override
	public Properties loadProperties(final String rawPropertiesSpec) {
		if (rawPropertiesSpec == null)
			throw new IllegalArgumentException("rawPropertiesSpec null");
		if (rawPropertiesSpec.trim().isEmpty())
			throw new IllegalArgumentException("rawPropertiesSpec empty");

		if (ctx == null)
			throw new IllegalStateException("no application context set");

		final String propertiesSpec = rawPropertiesSpec.startsWith("file:")
				|| rawPropertiesSpec.startsWith("classpath:") ? rawPropertiesSpec.trim()
						: "file:" + rawPropertiesSpec.trim();
		Resource resource = null;
		try {
			resource = ctx.getResource(propertiesSpec);
		} catch (RuntimeException rex) {
			throw new IllegalArgumentException("error accessing [" + propertiesSpec + "]; message: " + rex.getMessage(),
					rex);
		}

		if (resource == null)
			return null;

		if (!resource.exists()) {
			throw new IllegalArgumentException("resource " + propertiesSpec + " does not exist");
		}

		if (!resource.isReadable()) {
			throw new IllegalArgumentException("resource " + propertiesSpec + " not readable");
		}

		InputStream inputStream = null;
		final Properties properties = new Properties();
		try {
			inputStream = resource.getInputStream();
			if (inputStream == null) {
				throw new IllegalArgumentException("resource input stream " + propertiesSpec + " null");
			}

			properties.load(inputStream);
		} catch (IOException ex) {
			throw new IllegalArgumentException(
					"resource input stream " + propertiesSpec + " got error: " + ex.getMessage(), ex);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					// IGNORE - Swallow exception
				}
		}

		return properties;
	}

	// --------------------------------------------------------------------------------------------------------

	public ApplicationContext getCtx() {
		return ctx;
	}

	public void setCtx(final ApplicationContext ctx) {
		this.ctx = ctx;
	}
}
