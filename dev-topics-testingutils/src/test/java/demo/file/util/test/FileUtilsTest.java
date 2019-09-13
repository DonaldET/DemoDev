/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.file.util.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.file.util.FileUtils;

/**
 * Read data from test files, possibly formatting the data for later comparison
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class FileUtilsTest
{
  private static final String TEST_SRC_DIR = "src" + File.separator + "test"
      + File.separator + "resources" + File.separator + "data" + File.separator;
  private static final String TEST_SRC_NAME = "cumulateInput.txt";
  private static final String TEST_SRC_PATH = TEST_SRC_DIR + TEST_SRC_NAME;

  private static final String TEST_STD_DIR = "src" + File.separator + "test"
      + File.separator + "resources" + File.separator + "standard"
      + File.separator;
  private static final String TEST_STD_NAME = "cumulateStandard.txt";
  private static final String TEST_STD__PATH = TEST_STD_DIR + TEST_STD_NAME;

  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public void testInputLocation()
  {
    int data = -1;
    try (FileInputStream fis = new FileInputStream(TEST_SRC_PATH);)
    {
      data = fis.read();
    }
    catch (FileNotFoundException fnfEx)
    {
      Assert.fail("input missing: " + fnfEx.getMessage());
    }
    catch (IOException ioEx)
    {
      Assert.fail("input read error: " + ioEx.getMessage());
    }

    Assert.assertFalse("test file empty", -1 == data);
  }

  @Test
  public void testStandardLocation()
  {
    int data = -1;
    try (FileInputStream fis = new FileInputStream(TEST_STD__PATH);)
    {
      data = fis.read();
    }
    catch (FileNotFoundException fnfEx)
    {
      Assert.fail("input missing: " + fnfEx.getMessage());
    }
    catch (IOException ioEx)
    {
      Assert.fail("input read error: " + ioEx.getMessage());
    }

    Assert.assertFalse("test file empty", -1 == data);
  }

  @Test
  public void testCollectTrimmedFileData()
  {
    final String srcData = FileUtils.collectTrimmedFileData(new File(
        TEST_SRC_PATH));
    Assert.assertNotNull("src null", srcData);
    Assert.assertFalse("src empty", srcData.isEmpty());

    final String stdData = FileUtils.collectTrimmedFileData(new File(
        TEST_STD__PATH));
    Assert.assertNotNull("std null", stdData);
    Assert.assertFalse("std empty", stdData.isEmpty());

    Assert
        .assertEquals("file size differs", stdData.length(), srcData.length());
    Assert.assertEquals("file content differs", stdData, srcData);
  }

  @Test
  public void testCollectRawFileData()
  {
    final String srcData = FileUtils
        .collectRawFileData(new File(TEST_SRC_PATH));
    Assert.assertNotNull("src null", srcData);
    Assert.assertFalse("src empty", srcData.isEmpty());

    final String stdData = FileUtils.collectTrimmedFileData(new File(
        TEST_STD__PATH));
    Assert.assertNotNull("std null", stdData);
    Assert.assertFalse("std empty", stdData.isEmpty());

    Assert.assertFalse("file size same", stdData.length() == srcData.length());
  }
}
