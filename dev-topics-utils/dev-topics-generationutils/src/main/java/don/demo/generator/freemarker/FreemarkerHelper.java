package don.demo.generator.freemarker;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.Locale;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

/**
 * Freemarker utilities to manage interpolation control; hide unwanted
 * interpolation requests in the template.
 * 
 * @author dtrumme
 */
public final class FreemarkerHelper implements Serializable {
    private static final long serialVersionUID = -869478639649790967L;
    private static final String EMPTY_CLASS_PREFIX = "";

    /**
     * Prevent construction
     */
    private FreemarkerHelper() {
    }

    /**
     * Return results of configuring Freemarker
     * 
     * @author dtrumme
     */
    public static final class ConfigAndLoader {
        private final Configuration configuration;
        private final FileTemplateLoader loader;

        public ConfigAndLoader(final Configuration configuration,
                final FileTemplateLoader loader) {
            this.configuration = configuration;
            this.loader = loader;
        }

        public Configuration getConfiguration() {
            return configuration;
        }

        public FileTemplateLoader getLoader() {
            return loader;
        }
    }

    /**
     * Validate that the base path is a readable directory
     * 
     * @param baseTemplatePath
     *            the existing readable directory to validate
     * 
     * @return the valid directory path as a file
     */
    public static File validateDirPath(final String baseTemplatePath) {
        if (baseTemplatePath == null)
            throw new IllegalArgumentException("baseTemplatePath null");
        final String basePath = baseTemplatePath.trim();
        if (baseTemplatePath.length() > 0 && basePath.length() < 1)
            throw new IllegalArgumentException("baseTemplatePath all spaces");

        final File baseFilePath = new File(basePath);
        if (!baseFilePath.exists())
            throw new IllegalArgumentException("basePath " + basePath
                    + " does not exist");
        if (!baseFilePath.isDirectory())
            throw new IllegalArgumentException("basePath " + basePath
                    + " not a directory");
        if (!baseFilePath.canRead())
            throw new IllegalArgumentException("basePath " + basePath
                    + " not readable");

        return baseFilePath;
    }

    /**
     * Create a Freemarker template loader for a base path
     * 
     * @param baseTemplatePath
     *            the base directory from which templates will be loaded
     * 
     * @return the loader
     */
    public static FileTemplateLoader createFileTemplateLoader(
            final File baseTemplatePath) {
        if (baseTemplatePath == null)
            throw new IllegalArgumentException("baseTemplatePath null");

        FileTemplateLoader ftl = null;
        try {
            ftl = new ModifiedInterpolationTemplateLoader(baseTemplatePath);
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    "error creating template loader using base path "
                            + baseTemplatePath.getPath() + "; message: "
                            + ex.getMessage(), ex);
        }

        return ftl;
    }

    /**
     * Create a Freemarker template loader for a base path with optional opener
     * and replacement
     * 
     * @param baseTemplatePath
     *            the base directory from which templates will be loaded
     * @param opener
     *            the opener string to replace
     * @param replacement
     *            the string to use in place of the opener
     * @return the loader
     */
    public static FileTemplateLoader createFileTemplateLoader(
            final File baseTemplatePath, final String opener,
            final String replacement) {
        if (baseTemplatePath == null)
            throw new IllegalArgumentException("baseTemplatePath null");

        ModifiedInterpolationTemplateLoader ftl = null;
        try {
            ftl = new ModifiedInterpolationTemplateLoader(baseTemplatePath);
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    "error creating template loader using base path "
                            + baseTemplatePath.getPath() + "; message: "
                            + ex.getMessage(), ex);
        }

        ftl.setOpener(opener);
        ftl.setReplacement(replacement);

        return ftl;
    }

    /**
     * Configure a Freemarker context to use a template base path
     * 
     * @param fileTemplateLoader
     *            the file template loader instance
     * 
     * @return a path-configured "configuration" and associated loader
     * 
     * @throws Illegal
     *             argument exception of base template path is unusable
     */
    public static ConfigAndLoader configureFreemarker(
            final FileTemplateLoader fileTemplateLoader) {
        if (fileTemplateLoader == null)
            throw new IllegalArgumentException("ftl null");

        final Configuration cfg = new Configuration(
                Configuration.VERSION_2_3_23);
        cfg.setIncompatibleImprovements(new Version(2, 3, 20));
        cfg.setClassForTemplateLoading(FreemarkerHelper.class,
                EMPTY_CLASS_PREFIX);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.US);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setTemplateLoader(fileTemplateLoader);
        final DefaultObjectWrapperBuilder dowb = new DefaultObjectWrapperBuilder(
                Configuration.getVersion());
        final DefaultObjectWrapper dow = dowb.build();
        cfg.setObjectWrapper(dow);

        return new ConfigAndLoader(cfg, fileTemplateLoader);
    }

    public static Template retrieveTemplate(final Configuration cfg,
            final String templateBaseDir, final String templateFileName) {
        if (cfg == null)
            throw new IllegalArgumentException("cfg null");

        if (templateBaseDir == null)
            throw new IllegalArgumentException("templateBaseDir null");

        if (templateFileName == null)
            throw new IllegalArgumentException("templateFileName null");
        if (templateFileName.isEmpty())
            throw new IllegalArgumentException("templateFileName empty");

        Template template = null;
        try {
            template = cfg.getTemplate(templateFileName);
        } catch (IOException ex) {
            throw new IllegalArgumentException("unable to retrieve template "
                    + templateFileName + " at " + templateBaseDir
                    + "; message: " + ex.getMessage(), ex);
        }

        if (template == null)
            throw new IllegalArgumentException(
                    "null attempting to retrieve template " + templateFileName);

        return template;
    }

    /**
     * Read file wrapped by the reader and return as a string
     * 
     * @param reader
     *            the reader to cache
     * 
     * @return string value of reader content
     */
    public static String cacheFile(final Reader reader) {
        if (reader == null)
            throw new IllegalArgumentException("reader null");

        final StringBuilder buf = new StringBuilder(2048);
        final BufferedReader br = new BufferedReader(reader, 2048);
        for (int i = 1;; i++) {
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException ex) {
                throw new IllegalArgumentException("I/O error on line " + i
                        + ", message: " + ex.getMessage(), ex);
            }
            if (line == null)
                break;
            buf.append(line);
            buf.append("\n");
        }

        return buf.toString();
    }

    /**
     * All occurrences of an <code>opener</code> token, the sequence "
     * <em>(marker characters){</em>", in the <code>tplTex</code> are replaced
     * by a <code>replacement</code> string. Note: if either <code>opener</code>
     * or replacement parameters are null, <code>tmplText</code> are copied and
     * no replacement is performed.
     * 
     * @param tplText
     *            the template text to process
     * @param opener
     *            the sequence of marker characters, followed by "{", that are
     *            to be replaced
     * @param replacement
     *            the character sequence to replace the openers
     * 
     * @return the substituted text
     */
    public static String replaceOpeners(final String tplText,
            final String opener, final String replacement) {
        if (tplText == null)
            throw new IllegalArgumentException("tplText null");
        if (opener == null)
            throw new IllegalArgumentException("opener null");
        final int olth = opener.length();
        if (olth < 1)
            throw new IllegalArgumentException("opener empty");

        final int lth = tplText.length();
        if (lth < olth + 3)
            return tplText;
        if (replacement == null)
            throw new IllegalArgumentException("replacement null");

        final StringBuilder fixedTpl = new StringBuilder();
        int last = 0;
        do {
            //
            // If no more opener found, then copy and quit
            int p = tplText.indexOf(opener, last);
            if (p == -1) {
                fixedTpl.append(tplText.substring(last));
                break;
            }

            //
            // Move stuff before the opener
            if (p > last)
                fixedTpl.append(tplText.substring(last, p));

            //
            // If at end, then just add lone opener
            final int behind = p + olth;
            if (behind >= lth) {
                fixedTpl.append(opener);
                break;
            }

            //
            // Check if followed by open brace
            final int beyond = behind + 1;
            final String peek = tplText.substring(behind, beyond);
            fixedTpl.append("{".equals(peek) ? replacement : opener);
            fixedTpl.append(peek);
            last = beyond;
        } while (last < lth);

        return fixedTpl.toString();
    }

    /**
     * A short string (<code>magic</code>), expected to be non-printable, is
     * replaced by the <code>replacement</code> string in <code>tplText</code>
     * content.
     * 
     * @param tplText
     *            the template text in which replacement occurs
     * @param magic
     *            the special string to be replaced
     * @param replacement
     *            the replacement string used in place of the magic
     * @return the substituted text
     */
    public static String replaceMagic(final String tplText, final String magic,
            final String replacement) {
        if (tplText == null)
            throw new IllegalArgumentException("tplText null");
        if (magic == null)
            throw new IllegalArgumentException("magic null");
        final int mlth = magic.length();
        if (mlth < 1)
            throw new IllegalArgumentException("magic empty");

        final int lth = tplText.length();
        if (lth < mlth)
            return tplText;
        if (replacement == null)
            throw new IllegalArgumentException("replacement null");

        final StringBuilder fixedTpl = new StringBuilder();
        int last = 0;
        do {
            //
            // If no more magic found, then copy and quit
            int p = tplText.indexOf(magic, last);
            if (p == -1) {
                fixedTpl.append(tplText.substring(last));
                break;
            }

            //
            // Move stuff before the magic and add replacement
            if (p > last)
                fixedTpl.append(tplText.substring(last, p));
            fixedTpl.append(replacement);

            //
            // If at end, then just add lone magic
            last = p + mlth;
        } while (last < lth);

        return fixedTpl.toString();
    }

    /**
     * Dump a string as a sequence of hex characters
     * 
     * @param param
     *            the string to convert to hex
     * 
     * @return a displayable hex formatted string
     */
    public static String dumpAsHex(final String param) {
        final StringBuilder disp = new StringBuilder();
        if (param == null) {
            disp.append("null");
        } else {
            final int lth = param.length();
            if (lth < 1) {
                disp.append("empty");
            } else {
                final char[] cparam = param.toCharArray();
                final int clth = cparam.length;
                disp.append(String.format("(%3d; %3d):", lth, clth));
                for (int i = 0; i < clth; i++) {
                    final int c = 0xFFFF & cparam[i];
                    disp.append(String.format(" %02x", c));
                }
            }
        }

        return disp.toString();
    }
}
