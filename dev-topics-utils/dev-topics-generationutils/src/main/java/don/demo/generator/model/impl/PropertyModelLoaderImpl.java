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
 * <p>
 * <table style="width:100%">
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
 * @author dtrumme
 */
@Service("modelloader")
public class PropertyModelLoaderImpl implements PropertyModelLoader {
    private static final long serialVersionUID = 1640881080369640241L;

    @Autowired
    private ApplicationContext ctx;

    public PropertyModelLoaderImpl() {
    }

    /**
     * Load properties using a <code>Spring</code> resource loader
     */
    @Override
    public Properties loadProperties(final String propertiesSpec) {
        if (propertiesSpec == null)
            throw new IllegalArgumentException("propertiesSpec null");
        if (propertiesSpec.isEmpty())
            throw new IllegalArgumentException("propertiesSpec empty");

        if (ctx == null)
            throw new IllegalStateException("no application context set");

        Resource resource = ctx.getResource(propertiesSpec);
        try {
            resource = ctx.getResource(propertiesSpec);
        } catch (RuntimeException rex) {
            throw new IllegalArgumentException("error accessing "
                    + propertiesSpec + "; message: " + rex.getMessage(), rex);
        }

        if (resource == null)
            return null;

        if (!resource.exists()) {
            throw new IllegalArgumentException("resource " + propertiesSpec
                    + " does not exist");
        }

        if (!resource.isReadable()) {
            throw new IllegalArgumentException("resource " + propertiesSpec
                    + " not readable");
        }

        InputStream inputStream = null;
        final Properties properties = new Properties();
        try {
            inputStream = resource.getInputStream();
            if (inputStream == null) {
                throw new IllegalArgumentException("resource input stream "
                        + propertiesSpec + " null");
            }

            properties.load(inputStream);
        } catch (IOException ex) {
            throw new IllegalArgumentException("resource input stream "
                    + propertiesSpec + " got error: " + ex.getMessage(), ex);
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
