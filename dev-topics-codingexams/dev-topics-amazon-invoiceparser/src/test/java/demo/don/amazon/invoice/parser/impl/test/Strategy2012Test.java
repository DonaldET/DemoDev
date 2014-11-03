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

import demo.don.amazon.invoice.parser.impl.ExtractStrategy2012;

public class Strategy2012Test extends CheckStrategy
{
  private static final String TEST_SRC_DIR = "src/test/resources/D2012/";
  private static final String TEST_STD_DIR = "src/test/resources/D2012Std/";

  private static String[] testFiles = { "Amazon.com  Digital Order Summary",
      "Amazon.com  Digital Order Summary10",
      "Amazon.com  Digital Order Summary13",
      "Amazon.com - Order 103-4039722-7521802" };

  protected static String[] testFilesExt = { ".htm", ".htm", ".htm", ".htm" };

  @Before
  public void setUp() throws Exception
  {
    es = new ExtractStrategy2012();
  }

  @After
  public void tearDown() throws Exception
  {
    es = null;
  }

  @Test
  public void testGetName()
  {
    Assert.assertEquals("name differs", "2012",
        ((ExtractStrategy2012) es).getName());
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
