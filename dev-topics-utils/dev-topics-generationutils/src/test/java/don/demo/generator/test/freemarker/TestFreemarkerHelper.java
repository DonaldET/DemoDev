package don.demo.generator.test.freemarker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demo.generator.freemarker.FreemarkerHelper;
import don.demo.generator.freemarker.FreemarkerHelper.ConfigAndLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreemarkerHelper {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testValidateDirPath() {
        final File basePath = FreemarkerHelper
                .validateDirPath(TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL_DIR);
        Assert.assertNotNull("null for validated dir path", basePath);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValidateDirPathBad() {
        FreemarkerHelper.validateDirPath("xyzzy_pqrst_123");
        Assert.fail("validated bad dir path");
    }

    @Test
    public void testDumpAsHexNOE() {
        String param = null;
        Assert.assertEquals("null param failed", "null", FreemarkerHelper.dumpAsHex(param));
        param = "";
        Assert.assertEquals("empty param failed", "empty", FreemarkerHelper.dumpAsHex(param));
    }

    @Test
    public void testDumpAsHex() {
        String param = "ABC";
        Assert.assertEquals("null param failed", "(  3;   3): 41 42 43", FreemarkerHelper.dumpAsHex(param));
        param = "\u0000ABC\u0001";
        Assert.assertEquals("null param failed", "(  5;   5): 00 41 42 43 01", FreemarkerHelper.dumpAsHex(param));
    }
    
    @Test
    public void testConfigureFreemarker() {
        final File baseDir = new File(
                TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL_DIR);
        final ConfigAndLoader cfgLoader = FreemarkerHelper
                .configureFreemarker(FreemarkerHelper
                        .createFileTemplateLoader(baseDir));
        final Configuration cfg = cfgLoader.getConfiguration();
        Assert.assertNotNull("configuration null", cfg);
        Assert.assertEquals("encoding differs", "UTF-8",
                cfg.getDefaultEncoding());
        Assert.assertEquals("locale differs", "UTF-8",
                cfg.getEncoding(Locale.US));
    }

    @Test
    public void testRetrieveTemplate() {
        final File baseDir = new File(
                TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL_DIR);
        final ConfigAndLoader cfgLdr = FreemarkerHelper
                .configureFreemarker(FreemarkerHelper
                        .createFileTemplateLoader(baseDir));
        final Configuration cfg = cfgLdr.getConfiguration();
        final Template template = FreemarkerHelper
                .retrieveTemplate(
                        cfg,
                        TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL_DIR,
                        TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL);
        Assert.assertNotNull(
                "null template for "
                        + TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL
                        + " from "
                        + TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL_DIR,
                template);

        final StringWriter tpl_value = new StringWriter(1024);
        try {
            template.dump(tpl_value);
        } catch (IOException ex) {
            throw new IllegalArgumentException(
                    "unable to dump template "
                            + TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL
                            + "; message: " + ex.getMessage(), ex);
        }
    }

    @Test
    public void testCacheFile() {
        final File tplFile = new File(
                TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL_DIR,
                TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL);
        FileReader reader = null;
        try {
            reader = new FileReader(tplFile);
        } catch (FileNotFoundException ex) {
            Assert.fail("I/O error creating reader for "
                    + TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL
                    + " from "
                    + TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL_DIR
                    + "; message: " + ex.getMessage());
        }

        final String cachedFile = FreemarkerHelper.cacheFile(reader);
        Assert.assertNotNull(
                "NULL cached file for "
                        + TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL
                        + " from "
                        + TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL_DIR,
                cachedFile);
        Assert.assertFalse(
                "empty cached file for "
                        + TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL
                        + " from "
                        + TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_EVALUATION_TPL_DIR,
                cachedFile.isEmpty());

        final String exp_val = TestSimpleModifiedInterpolationTemplateLoader.SIMPLE_TEMPLATE_READ_VAL
                .trim();
        final String act_val = cachedFile.trim();
        Assert.assertEquals("Read of template differs", exp_val, act_val);
    }

    @Test
    public void testReplaceOpenersNone() {
        String raw = "abcDef123Pqr &*(^%";
        String replaced = FreemarkerHelper.replaceOpeners(raw, "$", "|");
        Assert.assertEquals("none no replacement", raw, replaced);

        raw = "";
        replaced = FreemarkerHelper.replaceOpeners(raw, "$", "|");
        Assert.assertEquals("empty no replacement", raw, replaced);

        raw = "abcDef123Pqr &*(^%$";
        replaced = FreemarkerHelper.replaceOpeners(raw, "$", "|");
        Assert.assertEquals("end no replacement", raw, replaced);

        raw = "$abcDef123Pqr &*(^%";
        replaced = FreemarkerHelper.replaceOpeners(raw, "$", "|");
        Assert.assertEquals("begin no replacement", raw, replaced);

        raw = "abcDef$123Pqr &*(^%";
        replaced = FreemarkerHelper.replaceOpeners(raw, "$", "|");
        Assert.assertEquals("middle no replacement", raw, replaced);

        raw = "abcDef123Pqr &*(^$%";
        replaced = FreemarkerHelper.replaceOpeners(raw, "$", "|");
        Assert.assertEquals("near end no replacement", raw, replaced);

        raw = "$";
        replaced = FreemarkerHelper.replaceOpeners(raw, "$", "|");
        Assert.assertEquals("only no replacement", raw, replaced);
    }

    @Test
    public void testReplaceOpeners() {
        String raw = "@{me}";
        String replaced = FreemarkerHelper.replaceOpeners(raw, "@", "|");
        String expected = raw.replaceAll("@", "|");
        Assert.assertEquals("only with replacement", expected, replaced);

        raw = "abcDef123Pqr &*(^%@{";
        replaced = FreemarkerHelper.replaceOpeners(raw, "@", "|");
        expected = raw.replaceAll("@", "|");
        Assert.assertEquals("end with replacement", expected, replaced);

        raw = "@{abcDef123Pqr &*(^%";
        replaced = FreemarkerHelper.replaceOpeners(raw, "@", "|");
        expected = raw.replaceAll("@", "|");
        Assert.assertEquals("begin with replacement", expected, replaced);

        raw = "abcDef@{123Pqr &*(^%";
        replaced = FreemarkerHelper.replaceOpeners(raw, "@", "|");
        expected = raw.replaceAll("@", "|");
        Assert.assertEquals("middle with replacement", expected, replaced);

        raw = "abcDef123Pqr &*(^@{%";
        replaced = FreemarkerHelper.replaceOpeners(raw, "@", "|");
        expected = raw.replaceAll("@", "|");
        Assert.assertEquals("near end with replacement", expected, replaced);
    }

    @Test
    public void testReplaceMagic() {
        String raw = "abc\u0001";
        String replaced = FreemarkerHelper.replaceMagic(raw, "\u0001", "$");
        String expected = "abc$";
        Assert.assertEquals("end with replacement", expected, replaced);

        raw = "\u0001abc";
        replaced = FreemarkerHelper.replaceMagic(raw, "\u0001", "$");
        expected = "$abc";
        Assert.assertEquals("begin with replacement", expected, replaced);

        raw = "ab\u0001cd";
        replaced = FreemarkerHelper.replaceMagic(raw, "\u0001", "$");
        expected = "ab$cd";
        Assert.assertEquals("middle with replacement", expected, replaced);
    }
}
