package don.demo.generator.test.arguments;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demo.generator.ParameterSetup.ParameterBean;
import don.demo.generator.arguments.ArgumentParser.ParseBean;
import don.demo.generator.arguments.tools.ArgumentParserArgs4J;

/**
 * Parses command line parameters, checking for required and converting single
 * string, comma-delimited lists into arrays of string.
 * 
 * @author dtrumme
 */
public class TestArgumentParserArgs4J {

    private ArgumentParserArgs4J parser = null;

    @Before
    public void setUp() throws Exception {
        parser = new ArgumentParserArgs4J();
    }

    @After
    public void tearDown() throws Exception {
        parser = null;
    }

    @Test(expected = RuntimeException.class)
    public void testBadArgs() {
        final String[] args = new String[] { "-wrong" };
        parser.parseArgs(args);
        Assert.fail("bad args not caught");
    }

    @Test(expected = RuntimeException.class)
    public void testNoArgs() {
        final String[] args = new String[0];
        final ParseBean parseBean = parser.parseArgs(args);
        Assert.assertNotNull(Arrays.toString(args) + " parseBean null",
                parseBean);

        final ParameterBean parameterBean = parseBean.getParameterBean();
        Assert.assertNull(Arrays.toString(args) + " parameterBean not null",
                parameterBean);

        final List<String> argList = parseBean.getArguments();
        Assert.assertNotNull(Arrays.toString(args) + " arg list null", argList);

        Assert.assertEquals(Arrays.toString(args) + " arg list not empty", 0,
                argList.size());
    }

    @Test
    public void testAllArgs() {
        final String[] args = new String[] { "-defaultContext", "df_ctx",
                "-dstDir", "dst_dir", "-generatedFileList", "gf_lst",
                "-overrideContextList", "ovr_ctx", "-srcDir", "src_dir",
                "-templateList", "tpl_lst" };
        final ParseBean parseBean = parser.parseArgs(args);
        Assert.assertNotNull(Arrays.toString(args) + " parseBean null",
                parseBean);

        final List<String> argList = parseBean.getArguments();
        Assert.assertNotNull(Arrays.toString(args) + " arg list null", argList);

        Assert.assertEquals(Arrays.toString(args) + " arg list not empty", 0,
                argList.size());

        Assert.assertEquals("DefaultContext", "df_ctx",
                parser.getDefaultContext());
        Assert.assertEquals("DstDir", "dst_dir", parser.getDstDir());
        Assert.assertEquals("GeneratedFileList", "gf_lst",
                parser.getGeneratedFileList());
        Assert.assertEquals("OverrideContextList", "ovr_ctx",
                parser.getOverrideContextList());
        Assert.assertEquals("SrcDir", "src_dir", parser.getSrcDir());
        Assert.assertEquals("TemplateList", "tpl_lst", parser.getTemplateList());

        final ParameterBean parameterBean = parseBean.getParameterBean();
        Assert.assertNotNull("ParameterBean null", parameterBean);

        Assert.assertEquals("all args bean differs", new ParameterBean(
                "src_dir", new String[] { "tpl_lst" }, "df_ctx",
                new String[] { "ovr_ctx" }, "dst_dir",
                new String[] { "gf_lst" }), parameterBean);
    }

    @Test
    public void testRequiredArgs() {
        final String[] args = new String[] { "-defaultContext", "df_ctx",
                "-dstDir", "dst_dir", "-generatedFileList", "gf_lst",
                "-srcDir", "src_dir", "-templateList", "tpl_lst" };
        final ParseBean parseBean = parser.parseArgs(args);
        Assert.assertNotNull(Arrays.toString(args) + " parseBean null",
                parseBean);

        final List<String> argList = parseBean.getArguments();
        Assert.assertNotNull(Arrays.toString(args) + " arg list null", argList);

        Assert.assertEquals(Arrays.toString(args) + " arg list not empty", 0,
                argList.size());

        Assert.assertEquals("DefaultContext", "df_ctx",
                parser.getDefaultContext());
        Assert.assertEquals("DstDir", "dst_dir", parser.getDstDir());
        Assert.assertEquals("GeneratedFileList", "gf_lst",
                parser.getGeneratedFileList());
        Assert.assertNull("OverrideContextList not null",
                parser.getOverrideContextList());
        Assert.assertEquals("SrcDir", "src_dir", parser.getSrcDir());
        Assert.assertEquals("TemplateList", "tpl_lst", parser.getTemplateList());

        final ParameterBean parameterBean = parseBean.getParameterBean();
        Assert.assertNotNull("ParameterBean null", parameterBean);

        Assert.assertEquals("all args bean differs", new ParameterBean(
                "src_dir", new String[] { "tpl_lst" }, "df_ctx", new String[0],
                "dst_dir", new String[] { "gf_lst" }), parameterBean);
    }

    @Test
    public void testSimpleAllValid() {
        final String[] args = new String[] { "-defaultContext",
                "proj/defaults.properties", "-dstDir", "proj/gensrc",
                "-generatedFileList", "a.xml,b.py,c.R,d.properties",
                "-overrideContextList",
                "a_over.properties,,,d_over.properties", "-srcDir",
                "proj/templates", "-templateList", "a.tpl,b.tpl,c.tpl,d.tpl" };
        final ParseBean parseBean = parser.parseArgs(args);
        Assert.assertNotNull(Arrays.toString(args) + " parseBean null",
                parseBean);

        final List<String> argList = parseBean.getArguments();
        Assert.assertNotNull(Arrays.toString(args) + " arg list null", argList);

        Assert.assertEquals(Arrays.toString(args) + " arg list not empty", 0,
                argList.size());

        Assert.assertEquals("DefaultContext", "proj/defaults.properties",
                parser.getDefaultContext());
        Assert.assertEquals("DstDir", "proj/gensrc", parser.getDstDir());
        Assert.assertEquals("GeneratedFileList", "a.xml,b.py,c.R,d.properties",
                parser.getGeneratedFileList());
        Assert.assertEquals("OverrideContextList",
                "a_over.properties,,,d_over.properties",
                parser.getOverrideContextList());
        Assert.assertEquals("SrcDir", "proj/templates", parser.getSrcDir());
        Assert.assertEquals("TemplateList", "a.tpl,b.tpl,c.tpl,d.tpl",
                parser.getTemplateList());

        final ParameterBean parameterBean = parseBean.getParameterBean();
        Assert.assertNotNull(Arrays.toString(args) + " parameterBean null",
                parameterBean);

        Assert.assertEquals("DefaultContext", "proj/defaults.properties",
                parameterBean.getDefaultContext());
        Assert.assertEquals("DstDir", "proj/gensrc", parser.getDstDir());
        strArrayMatch("GeneratedFileList", new String[] { "a.xml", "b.py",
                "c.R", "d.properties" }, parameterBean.getGeneratedFileList());
        strArrayMatch("OverrideContextList", new String[] {
                "a_over.properties", "", "", "d_over.properties" },
                parameterBean.getOverrideContextList());
        Assert.assertEquals("SrcDir", "proj/templates",
                parameterBean.getSrcDir());
        strArrayMatch("TemplateList", new String[] { "a.tpl", "b.tpl", "c.tpl",
                "d.tpl" }, parameterBean.getTemplateList());
    }

    // -----------------------------------------------------------------------------

    private static final void strArrayMatch(final String label,
            final String[] lhs, final String[] rhs) {
        if (lhs == null) {
            if (rhs == null) {
                return;
            } else
                throw new IllegalArgumentException("lhs null, rhs non-null");
        } else if (rhs == null) {
            throw new IllegalArgumentException("lhs non-null, rhs null");
        }

        final int lthl = lhs.length;
        final int lthr = rhs.length;
        if (lthl != lthr) {
            throw new IllegalArgumentException(String.format(
                    "Length Left = %d, differs from Right = %d", lthl, lthr));
        }

        if (lthl < 1)
            return;

        for (int i = 0; i < lthl; i++)
            Assert.assertEquals(label + "[" + i + "]", lhs[i], rhs[i]);
    }
}
