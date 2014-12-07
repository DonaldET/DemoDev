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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.amazon.invoice.parser.ExtractStrategy;
import demo.don.amazon.invoice.parser.impl.ExtractStrategy2012;
import demo.don.amazon.invoice.parser.impl.InvoiceLister;
import demo.file.util.FileUtils;

public class Strategy2012Test
{
  private static final String TEST_SRC_DIR = "src/test/resources/D2012/";
  private static final String TEST_STD_DIR = "src/test/resources/D2012Std/";

  private static final String[] testFiles = {
      "Amazon.com  Digital Order Summary",
      "Amazon.com  Digital Order Summary10",
      "Amazon.com  Digital Order Summary13",
      "Amazon.com - Order 103-4039722-7521802" };

  private ExtractStrategy es = null;

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
  public void testExtractDetails()
  {
    Assert.assertTrue(true);
  }

  @Test
  public void testGetName()
  {
    Assert.assertEquals("name differs", "2012",
        ((ExtractStrategy2012) es).getName());
  }

  @Test
  public void testAccessDataDirectories()
  {
    checkDirAccess(new File(TEST_SRC_DIR));
    checkDirAccess(new File(TEST_STD_DIR));
  }

  @Test
  public void testAccessDataFile()
  {
    for (final String fn : testFiles)
    {
      String fileName = fn + ".htm";
      Assert.assertNotNull(fileName + " not accessable",
          checkFileAccess(TEST_SRC_DIR, fileName));
      fileName = fn + ".txt";
      Assert.assertNotNull(fileName + " not accessable",
          checkFileAccess(TEST_STD_DIR, fileName));
    }
  }

  @Test
  public void testParseFileData()
  {
    for (final String fn : testFiles)
      checkATestFile(es, fn, ParserHelperTest.getJavaTempDir(), TEST_SRC_DIR,
          TEST_STD_DIR);
  }

  // ---------------------------------------------------------------------------

  public static File checkFileAccess(final String testDir, final String fileName)
  {
    final File dir = new File(testDir);
    final File file = new File(dir, fileName);
    Assert
        .assertTrue(file.getAbsolutePath() + " does not exist", file.exists());
    Assert.assertTrue(file.getAbsolutePath() + " not a file", file.isFile());
    Assert.assertTrue(file.getAbsolutePath() + " not readable", file.canRead());

    return file;
  }

  public static void checkDirAccess(final File dirFile)
  {
    Assert.assertTrue(dirFile.getAbsolutePath() + " does not exist",
        dirFile.exists());
    Assert.assertTrue(dirFile.getAbsolutePath() + " not directory",
        dirFile.isDirectory());
    Assert.assertTrue(dirFile.getAbsolutePath() + " not readable",
        dirFile.canRead());
  }

  public static void checkATestFile(final ExtractStrategy es,
      final String testFileBasicName, final File tempDir, final String srcDir,
      final String stdDir)
  {
    final File standardFile = new File(stdDir, testFileBasicName + ".txt");
    final String standard = FileUtils.collectTrimmedFileData(standardFile)
        .trim();
    Assert.assertNotNull("null data from " + standardFile.getAbsolutePath(),
        standard);
    Assert.assertFalse("empty data from " + standardFile.getAbsolutePath(),
        standard.isEmpty());

    final File inputFile = new File(srcDir, testFileBasicName + ".htm");
    final String inputData = readInputFile(inputFile);
    Assert.assertNotNull("null data from " + inputFile.getAbsolutePath(),
        standard);
    Assert.assertFalse("empty data from " + inputFile.getAbsolutePath(),
        standard.isEmpty());

    final StringBuilder data = new StringBuilder();
    es.extractDetails(inputFile.getAbsolutePath(), inputData, data, 0);
    Assert.assertTrue(
        "empty data from parse of " + inputFile.getAbsolutePath(),
        data.length() > 0);
    Assert.assertEquals("parse of " + inputFile.getAbsolutePath()
        + " differs from " + standardFile.getAbsolutePath(), standard,
        data.toString());
  }

  public static String readInputFile(final File inputFile)
  {
    boolean status = false;
    final StringBuilder srcData = new StringBuilder();
    InputStream srcStream = null;
    try
    {
      srcStream = new FileInputStream(inputFile);
      status = InvoiceLister.cumulateFile(srcData, srcStream,
          inputFile.getAbsolutePath());
    }
    catch (FileNotFoundException ex)
    {
      Assert.fail("unable to find " + inputFile.getAbsolutePath() + ": "
          + ex.getMessage());
    }

    if (!status)
      Assert.fail("unable to red " + inputFile.getAbsolutePath());

    return srcData.toString();
  }
}
