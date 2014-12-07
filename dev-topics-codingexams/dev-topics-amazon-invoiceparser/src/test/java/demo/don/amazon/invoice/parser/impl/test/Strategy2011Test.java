/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.amazon.invoice.parser.impl.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.amazon.invoice.parser.impl.ExtractStrategy2011;

public class Strategy2011Test extends CheckStrategy
{
  private static final String TEST_SRC_DIR = "src/test/resources/D2011/";
  private static final String TEST_STD_DIR = "src/test/resources/D2011Std/";

  private static String[] testFiles = { "Amazon.com  Digital Order Summary00",
      "Amazon.com  Digital Order Summary03", /*
                                              * 03 and 09 are same data from
                                              * different browser versions
                                              */
      "Amazon.com  Digital Order Summary09",
      "Amazon.com - Order 105-8473708-6521838",
      "Amazon_com Digital Order Summary_grid" };

  private static String[] testFilesExt = { ".html", ".html", ".html", ".html",
      ".htm" };

  @Before
  public void setUp() throws Exception
  {
    es = new ExtractStrategy2011();
  }

  @After
  public void tearDown() throws Exception
  {
    es = null;
  }

  @Test
  public void testGetName()
  {
    Assert.assertEquals("name differs", "2011",
        ((ExtractStrategy2011) es).getName());
  }

  @Override
  protected String getTestSrcDir()
  {
    return TEST_SRC_DIR;
  }

  @Override
  protected String getTestStdDir()
  {
    return TEST_STD_DIR;
  }

  @Override
  protected String[] getTestFiles()
  {
    return testFiles;
  }

  @Override
  protected void setTestFiles(final String[] newTestFiles)
  {
    testFiles = newTestFiles;
  }

  @Override
  protected String[] getTestFilesExt()
  {
    return testFilesExt;
  }

  @Override
  protected void setTestFilesExt(final String[] newTestFilesExt)
  {
    testFilesExt = newTestFilesExt;
  }
}
