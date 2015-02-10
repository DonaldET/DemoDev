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

import org.junit.Assert;
import org.junit.Test;

import demo.don.amazon.invoice.parser.ExtractStrategy;
import demo.don.amazon.invoice.parser.impl.InvoiceLister;
import demo.file.util.FileUtils;

/**
 * Isolate common elements of test, test data definition in concrete classes
 * 
 * @author Donald Trummell
 */
public abstract class CheckStrategy
{
  protected ExtractStrategy es = null;

  // ---------------------------------------------------------------------------

  protected abstract String getTestSrcDir();

  protected abstract String getTestStdDir();

  protected abstract String[] getTestFiles();

  protected abstract void setTestFiles(final String[] testFiles);

  protected abstract String[] getTestFilesExt();

  protected abstract void setTestFilesExt(final String[] testFilesExt);

  // ---------------------------------------------------------------------------

  @Test
  public void testExtractDetails()
  {
    Assert.assertTrue(true);
  }

  @Test
  public void testAccessDataDirectories()
  {
    checkDirAccess(new File(getTestSrcDir()));
    checkDirAccess(new File(getTestStdDir()));
  }

  @Test
  public void testAccessDataFile()
  {
    int i = 0;
    for (final String fn : getTestFiles())
    {
      String fileName = fn + getTestFilesExt()[i++];
      Assert.assertNotNull(fileName + " not accessable",
          checkFileAccess(getTestSrcDir(), fileName));
      fileName = fn + ".txt";
      Assert.assertNotNull(fileName + " not accessable",
          checkFileAccess(getTestStdDir(), fileName));
    }
  }

  @Test
  public void testParseFileData()
  {
    int i = 0;
    for (final String fn : getTestFiles())
      checkATestFile(getTestFilesExt()[i++], es, fn, getTestSrcDir(),
          getTestStdDir());
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

  public static void checkATestFile(final String ext, final ExtractStrategy es,
      final String testFileBasicName, final String srcDir, final String stdDir)
  {
    final File standardFile = new File(stdDir, testFileBasicName + ".txt");
    final String standard = FileUtils.collectTrimmedFileData(standardFile)
        .trim();
    Assert.assertNotNull("null data from " + standardFile.getAbsolutePath(),
        standard);
    Assert.assertFalse("empty data from " + standardFile.getAbsolutePath(),
        standard.isEmpty());

    final File inputFile = new File(srcDir, testFileBasicName + ext);
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
}