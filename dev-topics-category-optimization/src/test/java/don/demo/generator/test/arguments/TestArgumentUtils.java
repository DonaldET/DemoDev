package don.demo.generator.test.arguments;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import don.demo.generator.arguments.tools.ArgumentUtils;

/**
 * Test argument parsing utilities based on Args4J
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
 */
public class TestArgumentUtils
{
    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testEmpty()
    {
        String src = "";
        Assert.assertEquals("Empty", src, ArgumentUtils.maskComma(src, '|'));

        src = "    ";
        Assert.assertEquals("Spaces", src, ArgumentUtils.maskComma(src, '|'));
    }

    @Test
    public void testSimpleNoMask()
    {
        String src = "aaa";
        Assert.assertEquals("Simple", src, ArgumentUtils.maskComma(src, '|'));

        src = "aaa,bbb,ccc";
        final String exp = "aaa,bbb,ccc";

        Assert.assertEquals("Simple", exp, ArgumentUtils.maskComma(src, '|'));
    }

    @Test
    public void testQuotedNoMask()
    {
        String src = "aaa\"bbb\"ccc";
        Assert.assertEquals("Quoted No Mask", src, ArgumentUtils.maskComma(src, '|'));

        src = "aaa'bbb'ccc";
        Assert.assertEquals("Apos No Mask", src, ArgumentUtils.maskComma(src, '|'));
    }

    @Test
    public void testQuotedMask()
    {
        String src = "aaa\"bbb,ddd\"ccc";
        String exp = "aaa\"bbb|ddd\"ccc";
        Assert.assertEquals("Quoted with Mask", exp, ArgumentUtils.maskComma(src, '|'));

        src = "aaa'bbb,ddd'ccc";
        exp = "aaa'bbb|ddd'ccc";
        Assert.assertEquals("Apos with Mask", exp, ArgumentUtils.maskComma(src, '|'));
    }

    @Test
    public void testComplexMask()
    {
        String src = "aaa\"bbb,ddd\"ccc,eee,'fff,ggg',hhh";
        String exp = "aaa\"bbb|ddd\"ccc,eee,'fff|ggg',hhh";
        Assert.assertEquals("Complex quoted with Mask", exp, ArgumentUtils.maskComma(src, '|'));
    }

    // -------------------------------------------------------------------------

    @Test
    public void testArgCommaToListBoundry()
    {
        String src = "";
        String[] expArgs = new String[0];
        String[] actArgs = ArgumentUtils.commaToList(src, '|');
        Assert.assertNotNull("null arg", actArgs);
        int lth = actArgs.length;
        Assert.assertEquals("arg count wrong", expArgs.length, actArgs.length);
        for (int i = 0; i < lth; i++)
        {
            Assert.assertEquals("Bounds arg", expArgs[i], actArgs[i]);
        }

        src = "     ";
        expArgs = new String[]
        { src };
        actArgs = ArgumentUtils.commaToList(src, '|');
        Assert.assertNotNull("null arg", actArgs);
        lth = actArgs.length;
        Assert.assertEquals("arg count wrong", expArgs.length, actArgs.length);
        for (int i = 0; i < lth; i++)
        {
            Assert.assertEquals("Bounds arg", expArgs[i], actArgs[i]);
        }
    }

    @Test
    public void testArgCommaToListSimple()
    {
        String src = "aaa";
        String[] expArgs = new String[]
        { "aaa" };
        String[] actArgs = ArgumentUtils.commaToList(src, '|');
        Assert.assertNotNull("null arg", actArgs);
        int lth = actArgs.length;
        Assert.assertEquals("arg count wrong", expArgs.length, actArgs.length);
        for (int i = 0; i < lth; i++)
        {
            Assert.assertEquals("Simple arg", expArgs[i], actArgs[i]);
        }

        src = "aaa,bbb";
        expArgs = new String[]
        { "aaa", "bbb" };
        actArgs = ArgumentUtils.commaToList(src, '|');
        Assert.assertNotNull("null arg", actArgs);
        lth = actArgs.length;
        Assert.assertEquals("arg count wrong", expArgs.length, actArgs.length);
        for (int i = 0; i < lth; i++)
        {
            Assert.assertEquals("Simple arg", expArgs[i], actArgs[i]);
        }

        src = "\"aaa,bbb\"";
        expArgs = new String[]
        { "\"aaa,bbb\"" };
        actArgs = ArgumentUtils.commaToList(src, '|');
        Assert.assertNotNull("null arg", actArgs);
        lth = actArgs.length;
        Assert.assertEquals("arg count wrong", expArgs.length, actArgs.length);
        for (int i = 0; i < lth; i++)
        {
            Assert.assertEquals("Simple arg", expArgs[i], actArgs[i]);
        }
    }

    @Test
    public void testArgCommaToListComplex()
    {
        String src = "aaa\"bbb,ddd\"ccc,eee,'fff,ggg',hhh";
        String[] expArgs = new String[]
        { "aaa\"bbb,ddd\"ccc", "eee", "'fff,ggg'", "hhh" };
        String[] actArgs = ArgumentUtils.commaToList(src, '|');
        Assert.assertNotNull("null arg", actArgs);
        final int lth = actArgs.length;
        Assert.assertEquals("arg count wrong", expArgs.length, actArgs.length);
        for (int i = 0; i < lth; i++)
        {
            Assert.assertEquals("Complex arg", expArgs[i], actArgs[i]);
        }
    }
}
