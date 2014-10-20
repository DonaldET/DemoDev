package demo.don.amazon.invoice.parser.impl.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.amazon.invoice.parser.ExtractStrategy;
import demo.don.amazon.invoice.parser.impl.AbstractExtractStrategy;
import demo.don.amazon.invoice.parser.impl.ExtractStrategy2012;

public class ParserHelperTest
{
  public static final String TEMP_DIR_PROP_NAME = "java.io.tmpdir";

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
  public void testRemoveProblems()
  {
    String src = "make&change";
    Assert.assertEquals("Ampersand bad", "make{amp}change",
        AbstractExtractStrategy.removeProblems(src));
    src = "make,change";
    Assert.assertEquals("Comma bad", "make;change",
        AbstractExtractStrategy.removeProblems(src));
    src = "make  ws-change,including new lines\n\nand tabs\t\t\tor returns\r";
    Assert.assertEquals("White-space bad",
        "make ws-change;including new lines and tabs or returns",
        AbstractExtractStrategy.removeProblems(src));
  }

  @Test
  public void testGetName()
  {
    Assert.assertEquals("name wrong", "2012",
        ((AbstractExtractStrategy) es).getName());
  }

  @Test
  public void testTempDirAccess()
  {
    final File td = getJavaTempDir();
    Assert.assertNotNull("File for " + TEMP_DIR_PROP_NAME + " null", td);
    Assert.assertTrue(td + " does not exist", td.exists());
    Assert.assertTrue(td + " does not directory", td.isDirectory());
    Assert.assertTrue(td + " does not writeable", td.canWrite());

    final String testFileName = "XX_TEST_YY.xyz";

    //
    // E.g.: C:\Users\Don\AppData\Local\Temp\XX_TEST_YY.xyz
    //

    final File testFile = new File(td, testFileName);
    if (testFile.exists())
      testFile.delete();

    if (!write2File(testFile, "Hi!\n"))
      Assert.fail("unable to write " + testFileName);
    Assert.assertTrue(testFileName + " not created", testFile.exists());

    testFile.delete();
    Assert.assertFalse(testFileName + " not deleted", testFile.exists());
  }

  // ---------------------------------------------------------------------------

  public static File getJavaTempDir()
  {
    final String tempDirPath = System.getProperty(TEMP_DIR_PROP_NAME);
    if (tempDirPath == null)
      throw new IllegalStateException(
          "null property value associated with key " + TEMP_DIR_PROP_NAME);

    if (tempDirPath.isEmpty())
      throw new IllegalStateException(
          "empty property value associated with key " + TEMP_DIR_PROP_NAME);

    return new File(tempDirPath);
  }

  public static boolean write2File(final File testFile, final String data)
  {
    if (testFile == null)
      throw new IllegalArgumentException("testFile null");

    if (data == null)
      throw new IllegalArgumentException("data null");

    boolean status = false;
    FileOutputStream fos = null;
    try
    {
      fos = new FileOutputStream(testFile);
      final byte[] bytes = data.getBytes();
      if (bytes != null && bytes.length > 0)
        fos.write(bytes);
      fos.flush();
      status = true;
    }
    catch (FileNotFoundException fnfEx)
    {
      Assert.fail("error creating " + testFile.getAbsolutePath() + "; "
          + fnfEx.getMessage());
    }
    catch (IOException ioEx)
    {
      Assert.fail("error writing " + testFile.getAbsolutePath() + "; "
          + ioEx.getMessage());
    }
    finally
    {
      if (fos != null)
        try
        {
          fos.close();
        }
        catch (IOException ignore)
        {
          // Ignore
        }
    }

    return status;
  }
}
