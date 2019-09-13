package don.demo.generator.test.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demo.generator.freemarker.FreemarkerHelper;
import don.demo.generator.freemarker.FreemarkerHelper.ConfigAndLoader;
import don.demo.generator.freemarker.ModifiedInterpolationTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * Test the template loader that substitutes interpolation opener characters to
 * hide from Freemarker. Examples inspired by blog (See
 * <a href='http://www.vogella.com/tutorials/FreeMarker/article.html'>this 2013
 * Vogella Freemarker example</a>.)
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
public class TestSimpleModifiedInterpolationTemplateLoader
{

    public static final String SIMPLE_TEMPLATE_EVALUATION_TPL_DIR = "./src/test/resources/tpl_read_tests";
    public static final String SIMPLE_TEMPLATE_EVALUATION_TPL = "MyTestApp.tpl";
    public static final String SIMPLE_TEMPLATE_REPLACE_TPL = "MyReplacementTestApp.tpl";

    // Note: template and read and template as dumped differ
    public static final String SIMPLE_TEMPLATE_READ_VAL = "##\n" + "# A sample test application configuration\n"
            + "##\n" + "app_title=${title}\n" + "an_object_id=${exampleObject.name}\n"
            + "an_object_value=${exampleObject.value}\n" + "# List deployable systems\n" + "<#list systems as system>\n"
            + "sys_num.${system_index + 1}=${system.name} from ${system.value}\n" + "</#list>\n"
            + "a_configuration=<%= @environment_prefix %>${exampleObject.name}.${exampleObject.value}\n";

    // Note: template and read and template as dumped differ
    public static final String SIMPLE_TEMPLATE_DUMP_VAL = "##\n" + "# A sample test application configuration\n"
            + "##\n" + "app_title=${title}\n" + "an_object_id=${exampleObject.name}\n"
            + "an_object_value=${exampleObject.value}\n" + "# List deployable systems\n"
            + "<#list systems as system>sys_num.${system_index + 1}=${system.name} from ${system.value}\n"
            + "</#list>\n" + "a_configuration=<%= @environment_prefix %>${exampleObject.name}.${exampleObject.value}";

    public static final String SIMPLE_TEMPLATE_OUTPUT_VAL = "##\n" + "# A sample test application configuration\n"
            + "##\n" + "app_title=Extended Vogella Example\n" + "an_object_id=myId\n" + "an_object_value=MyValue\n"
            + "# List deployable systems\n" + "sys_num.1=Android from Google\n" + "sys_num.2=iOS States from Apple\n"
            + "sys_num.3=Ubuntu from Canonical\n" + "sys_num.4=Windows10 from Microsoft\n"
            + "a_configuration=<%= @environment_prefix %>myId.MyValue\n";

    public static final String SIMPLE_TEMPLATE_REPLACE_VAL = "##\n"
            + "# A sample test application configuration with Replacement\n" + "##\n"
            + "app_title=Extended Vogella Example\n" + "an_object_id=myId\n" + "an_object_value=MyValue\n"
            + "# List deployable systems\n" + "sys_num.1=Android from Google\n" + "sys_num.2=iOS States from Apple\n"
            + "sys_num.3=Ubuntu from Canonical\n" + "sys_num.4=Windows10 from Microsoft\n"
            + "a_configuration=<%= @environment_prefix %>myId.MyValue\n" + "b_configuration=${sombody_else}\n";

    /**
     * Example object used in model
     *
     * @author Donald Trummell (dtrummell@gmail.com)
     */
    public static final class NVPair
    {
        private final String name;
        private final String value;

        public NVPair(final String name, final String value) {
            super();
            this.name = name;
            this.value = value;
        }

        public String getName()
        {
            return name;
        }

        public String getValue()
        {
            return value;
        }
    }

    /**
     * Return test setup info
     *
     * @author Donald Trummell (dtrummell@gmail.com)
     */
    public static final class TplCfgFtl
    {
        private final Template template;
        private final Configuration configuration;
        private final FileTemplateLoader fileTemplateLoader;

        public TplCfgFtl(final Template template, final Configuration cfg, final FileTemplateLoader ftl) {
            this.template = template;
            this.configuration = cfg;
            this.fileTemplateLoader = ftl;
        }

        public Template getTemplate()
        {
            return template;
        }

        public Configuration getConfiguration()
        {
            return configuration;
        }

        public FileTemplateLoader getFileTemplateLoader()
        {
            return fileTemplateLoader;
        }
    }

    // ---------------------------------------------------------------------------------------

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testToString()
    {
        final ModifiedInterpolationTemplateLoader mtl = (ModifiedInterpolationTemplateLoader) FreemarkerHelper
                .createFileTemplateLoader(new File(SIMPLE_TEMPLATE_EVALUATION_TPL_DIR));
        Assert.assertNotNull("NULL, unable to create loader using " + SIMPLE_TEMPLATE_EVALUATION_TPL_DIR, mtl);

        final String msg = mtl.toString();
        Assert.assertNotNull("NULL toString", msg);
        Assert.assertTrue("toString empty", msg.length() > 0);
        Assert.assertTrue("no class name", msg.contains(mtl.getClass().getSimpleName()));

        Assert.assertEquals("opener wrong", ModifiedInterpolationTemplateLoader.DEFAULT_OPENER, mtl.getOpener());

        Assert.assertEquals("replacement wrong", ModifiedInterpolationTemplateLoader.DEFAULT_REPLACMENT,
                mtl.getReplacement());
    }

    @Test
    public void testGetReader()
    {
        final ModifiedInterpolationTemplateLoader mtl = (ModifiedInterpolationTemplateLoader) FreemarkerHelper
                .createFileTemplateLoader(new File(SIMPLE_TEMPLATE_EVALUATION_TPL_DIR));
        Assert.assertNotNull("NULL, unable to create loader using " + SIMPLE_TEMPLATE_EVALUATION_TPL_DIR, mtl);
        mtl.setOpener("$");
        mtl.setReplacement("##");

        final File tplFile = new File(SIMPLE_TEMPLATE_EVALUATION_TPL_DIR, SIMPLE_TEMPLATE_EVALUATION_TPL);

        Reader reader = null;
        try
        {
            reader = mtl.getReader(tplFile, "UTF8");
        }
        catch (IOException ex)
        {
            Assert.fail("I/O error creating reader for " + SIMPLE_TEMPLATE_EVALUATION_TPL + " from "
                    + SIMPLE_TEMPLATE_EVALUATION_TPL_DIR + "; message: " + ex.getMessage());
        }
        Assert.assertNotNull("NULL creating reader for " + SIMPLE_TEMPLATE_EVALUATION_TPL + " from "
                + SIMPLE_TEMPLATE_EVALUATION_TPL_DIR, reader);

        final String cachedFile = FreemarkerHelper.cacheFile(reader);
        Assert.assertNotNull("NULL cached file for " + SIMPLE_TEMPLATE_EVALUATION_TPL + " from "
                + SIMPLE_TEMPLATE_EVALUATION_TPL_DIR, cachedFile);
        Assert.assertFalse("empty cached file for " + SIMPLE_TEMPLATE_EVALUATION_TPL + " from "
                + SIMPLE_TEMPLATE_EVALUATION_TPL_DIR, cachedFile.isEmpty());

        String exp_val = SIMPLE_TEMPLATE_READ_VAL.trim();
        exp_val = Pattern.compile(Matcher.quoteReplacement("$")).matcher(exp_val).replaceAll(mtl.getReplacement());

        final String act_val = cachedFile.trim();
        Assert.assertEquals("Read template differs", exp_val, act_val);
    }

    @Test
    /**
     * See
     * <a href='http://www.vogella.com/tutorials/FreeMarker/article.html'>this
     * 2013 Vogella example</a>
     */
    public void testSimpleTemplateEvaluationNoInterpolation()
    {
        //
        // Get template
        final TplCfgFtl tcf = getTemplateNoReplace(SIMPLE_TEMPLATE_EVALUATION_TPL_DIR, SIMPLE_TEMPLATE_EVALUATION_TPL);
        final Template template = tcf.getTemplate();

        //
        // Create the model
        final Map<String, Object> model = createSimpleTemplateEvaluationModel();

        //
        // Apply the model values to the template
        final StringWriter gen_src = new StringWriter(1024);
        try
        {
            template.process(model, gen_src);
        }
        catch (TemplateException | IOException ex)
        {
            throw new IllegalArgumentException(
                    "unable to process template " + SIMPLE_TEMPLATE_EVALUATION_TPL + "; message: " + ex.getMessage(),
                    ex);
        }

        //
        // Check the substituted results
        final String act_val = gen_src.toString();
        Assert.assertEquals("invalid generated value", SIMPLE_TEMPLATE_OUTPUT_VAL.trim(), act_val.trim());
    }

    @Test
    /**
     * Replace normal openers and use "magic" openers (@@{)
     */
    public void testSimpleTemplateEvaluationInterpolation()
    {
        //
        // Get template
        final TplCfgFtl tcf = getTemplate(SIMPLE_TEMPLATE_EVALUATION_TPL_DIR, SIMPLE_TEMPLATE_REPLACE_TPL);
        final Template template = tcf.getTemplate();
        final ModifiedInterpolationTemplateLoader ftl = (ModifiedInterpolationTemplateLoader) tcf
                .getFileTemplateLoader();

        //
        // Create the model
        final Map<String, Object> model = createSimpleTemplateEvaluationModel();

        //
        // Apply the model values to the template
        final StringWriter gen_src = new StringWriter(1024);
        try
        {
            template.process(model, gen_src);
        }
        catch (TemplateException | IOException ex)
        {
            throw new IllegalArgumentException(
                    "unable to process template " + SIMPLE_TEMPLATE_REPLACE_TPL + "; message: " + ex.getMessage(), ex);
        }

        //
        // Check the substituted results
        String act_val = gen_src.toString();
        act_val = FreemarkerHelper.replaceOpeners(act_val, ftl.getReplacement(), "$");
        Assert.assertEquals("invalid generated value", SIMPLE_TEMPLATE_REPLACE_VAL.trim(), act_val.trim());
    }

    // ----------------------------------------------------------------------------------------------

    private Map<String, Object> createSimpleTemplateEvaluationModel()
    {
        final Map<String, Object> model = new HashMap<String, Object>();
        model.put("title", "Extended Vogella Example");
        model.put("exampleObject", new NVPair("myId", "MyValue"));

        final List<NVPair> systems = new ArrayList<NVPair>();
        systems.add(new NVPair("Android", "Google"));
        systems.add(new NVPair("iOS States", "Apple"));
        systems.add(new NVPair("Ubuntu", "Canonical"));
        systems.add(new NVPair("Windows10", "Microsoft"));
        model.put("systems", systems);

        return model;
    }

    private TplCfgFtl getTemplate(final String templateBaseDir, final String templateFileName)
    {
        final File baseDir = new File(templateBaseDir);
        final FileTemplateLoader ftl = FreemarkerHelper.createFileTemplateLoader(baseDir);
        final ConfigAndLoader cfgLoader = FreemarkerHelper.configureFreemarker(ftl);
        final Configuration cfg = cfgLoader.getConfiguration();
        final Template template = FreemarkerHelper.retrieveTemplate(cfg, templateBaseDir, templateFileName);

        return new TplCfgFtl(template, cfg, ftl);
    }

    private TplCfgFtl getTemplateNoReplace(final String templateBaseDir, final String templateFileName)
    {
        final File baseDir = new File(templateBaseDir);

        final FileTemplateLoader ftl = FreemarkerHelper.createFileTemplateLoader(baseDir);
        ((ModifiedInterpolationTemplateLoader) ftl).setOpener(null);
        ((ModifiedInterpolationTemplateLoader) ftl).setReplacement(null);

        final ConfigAndLoader cfgLoader = FreemarkerHelper.configureFreemarker(ftl);
        final Configuration cfg = cfgLoader.getConfiguration();
        final Template template = FreemarkerHelper.retrieveTemplate(cfg, templateBaseDir, templateFileName);

        return new TplCfgFtl(template, cfg, ftl);
    }

    // ----------------------------------------------------------------------------------------------
}
