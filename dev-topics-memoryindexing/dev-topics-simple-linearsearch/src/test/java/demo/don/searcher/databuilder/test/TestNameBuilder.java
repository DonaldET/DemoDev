package demo.don.searcher.databuilder.test;

import java.io.InputStream;
import java.util.Arrays;

import junit.framework.TestCase;
import demo.don.searcher.FLName;
import demo.don.searcher.UserID;
import demo.don.searcher.databuilder.NameBuilder;
import demo.don.searcher.test.DataBuilderTestConstants;

public class TestNameBuilder extends TestCase
{
  private static final int FULL_NAME_LENGTH = DataBuilderTestConstants.TEST_FULL_NAME_COUNT;
  private static final int FIRST_NAME_LENGTH = DataBuilderTestConstants.TEST_FIRST_NAME_COUNT;

  private NameBuilder builder = null;
  private boolean echoFake = false;

  public TestNameBuilder(final String name)
  {
    super(name);
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    builder = new NameBuilder();
  }

  @Override
  protected void tearDown() throws Exception
  {
    builder = null;

    super.tearDown();
  }

  public void testFLNameEql()
  {
    assertEquals("idential failed", DataBuilderTestConstants.o1,
        DataBuilderTestConstants.o1);
    assertEquals("hc: idential failed", DataBuilderTestConstants.o1.hashCode(),
        DataBuilderTestConstants.o1.hashCode());

    assertEquals("equal content failed", DataBuilderTestConstants.o4,
        DataBuilderTestConstants.o4a);
    assertEquals("hc: equal content failed",
        DataBuilderTestConstants.o4.hashCode(),
        DataBuilderTestConstants.o4a.hashCode());

    assertFalse("different equal",
        DataBuilderTestConstants.o1.equals(DataBuilderTestConstants.o2));
    assertFalse("hc: different equal",
        DataBuilderTestConstants.o1.hashCode() == DataBuilderTestConstants.o2
            .hashCode());
  }

  public void testFLNameCompareTo()
  {
    assertEquals("idential failed", 0,
        DataBuilderTestConstants.o1.compareTo(DataBuilderTestConstants.o1));
    assertEquals("equal content failed", 0,
        DataBuilderTestConstants.o4.compareTo(DataBuilderTestConstants.o4a));
    assertEquals("less failed", -1,
        DataBuilderTestConstants.o1.compareTo(DataBuilderTestConstants.o2));
    assertEquals("greater failed", 1,
        DataBuilderTestConstants.o4.compareTo(DataBuilderTestConstants.o3));
  }

  public void testFLNameCompare()
  {
    assertEquals("idential failed", 0, DataBuilderTestConstants.o1.compare(
        DataBuilderTestConstants.o1, DataBuilderTestConstants.o1));
    assertEquals("equal content failed", 0,
        DataBuilderTestConstants.o4.compare(DataBuilderTestConstants.o4,
            DataBuilderTestConstants.o4a));
    assertEquals("less failed", -1, DataBuilderTestConstants.o1.compare(
        DataBuilderTestConstants.o1, DataBuilderTestConstants.o2));
    assertEquals("greater failed", 1, DataBuilderTestConstants.o4.compare(
        DataBuilderTestConstants.o4, DataBuilderTestConstants.o3));
  }

  public void testFLNameCloneCompare()
  {
    final FLName[] unorderedCopy = DataBuilderTestConstants.unordered.clone();
    final int n = unorderedCopy.length;
    assertEquals("size differs", DataBuilderTestConstants.unordered.length, n);
    for (int i = 0; i < n; i++)
      assertEquals("entry[" + i + "] differs",
          DataBuilderTestConstants.unordered[i], unorderedCopy[i]);
    assertTrue("array comparision failed",
        Arrays.equals(DataBuilderTestConstants.unordered, unorderedCopy));
  }

  public void testFLNameSort()
  {
    final FLName[] unorderedCopy = DataBuilderTestConstants.unordered.clone();
    assertFalse("array comparision wrongly succeeded",
        Arrays.equals(DataBuilderTestConstants.ordered, unorderedCopy));
    Arrays.sort(unorderedCopy);
    assertTrue("sort failed",
        Arrays.equals(DataBuilderTestConstants.ordered, unorderedCopy));
  }

  public void testUserIDEql()
  {
    assertEquals("idential failed", DataBuilderTestConstants.uo1,
        DataBuilderTestConstants.uo1);
    assertEquals("hc: idential failed",
        DataBuilderTestConstants.uo1.hashCode(),
        DataBuilderTestConstants.uo1.hashCode());

    assertEquals("equal content failed", DataBuilderTestConstants.uo4,
        DataBuilderTestConstants.uo4a);
    assertEquals("hc: equal content failed",
        DataBuilderTestConstants.uo4.hashCode(),
        DataBuilderTestConstants.uo4a.hashCode());

    assertFalse("different equal",
        DataBuilderTestConstants.uo1.equals(DataBuilderTestConstants.uo2));
    assertFalse("hc: different equal",
        DataBuilderTestConstants.uo1.hashCode() == DataBuilderTestConstants.uo2
            .hashCode());
  }

  public void testUserIDCompareTo()
  {
    assertEquals("idential failed", 0,
        DataBuilderTestConstants.uo1.compareTo(DataBuilderTestConstants.uo1));
    assertEquals("equal content failed", 0,
        DataBuilderTestConstants.uo4.compareTo(DataBuilderTestConstants.uo4a));
    assertEquals("less failed", -1,
        DataBuilderTestConstants.uo1.compareTo(DataBuilderTestConstants.uo2));
    assertEquals("greater failed", 1,
        DataBuilderTestConstants.uo4.compareTo(DataBuilderTestConstants.uo3));
  }

  public void testUserIDCompare()
  {
    assertEquals("idential failed", 0, DataBuilderTestConstants.uo1.compare(
        DataBuilderTestConstants.uo1, DataBuilderTestConstants.uo1));
    assertEquals("equal content failed", 0,
        DataBuilderTestConstants.uo4.compare(DataBuilderTestConstants.uo4,
            DataBuilderTestConstants.uo4a));
    assertEquals("less failed", -1, DataBuilderTestConstants.uo1.compare(
        DataBuilderTestConstants.uo1, DataBuilderTestConstants.uo2));
    assertEquals("greater failed", 1, DataBuilderTestConstants.uo4.compare(
        DataBuilderTestConstants.uo4, DataBuilderTestConstants.uo3));
  }

  public void testUserIDCloneCompare()
  {
    final UserID[] unorderedCopy = DataBuilderTestConstants.uidUnordered
        .clone();
    final int n = unorderedCopy.length;
    assertEquals("size differs", DataBuilderTestConstants.uidUnordered.length,
        n);
    for (int i = 0; i < n; i++)
      assertEquals("entry[" + i + "] differs",
          DataBuilderTestConstants.uidUnordered[i], unorderedCopy[i]);
    assertTrue("array comparision failed",
        Arrays.equals(DataBuilderTestConstants.uidUnordered, unorderedCopy));
  }

  public void testUserIDSort()
  {
    final UserID[] unorderedCopy = DataBuilderTestConstants.uidUnordered
        .clone();
    assertFalse("array comparision wrongly succeeded",
        Arrays.equals(DataBuilderTestConstants.uidOrdered, unorderedCopy));
    Arrays.sort(unorderedCopy);
  }

  public void testStaticReadFirstNames()
  {
    final String testFirstNamesFile = DataBuilderTestConstants.FIRST_NAME_FILE;
    final InputStream stream = NameBuilder.class
        .getResourceAsStream(testFirstNamesFile);
    assertNotNull("no stream for " + testFirstNamesFile, stream);

    final String[] firstNames = NameBuilder.readFirstNames(stream);
    assertNotNull("null firstNames for " + testFirstNamesFile, firstNames);
    final int n = firstNames.length;
    assertTrue("empty firstNames for " + testFirstNamesFile, n > 0);
    final int expected = FIRST_NAME_LENGTH;
    assertEquals("unexpected length", expected, n);
  }

  public void testStaticReadFLNames()
  {
    final String testFLNamesFile = DataBuilderTestConstants.FULL_NAME_FILE;
    final InputStream stream = NameBuilder.class
        .getResourceAsStream(testFLNamesFile);
    assertNotNull("no stream for " + testFLNamesFile, stream);

    final FLName[] flNames = NameBuilder.readFLNames(stream);
    assertNotNull("null firstNames for " + testFLNamesFile, flNames);
    final int n = flNames.length;
    assertTrue("empty firstNames for " + testFLNamesFile, n > 0);
    final int expected = FULL_NAME_LENGTH;
    assertEquals("unexpected length", expected, n);
  }

  public void testInitialize()
  {
    assertFalse("builder falsely initialized", builder.isInitialized());

    final String firstNameFile = builder.getFirstNameFile();
    assertNotNull("firstNameFile null", firstNameFile);
    assertFalse("firstNameFile empty", firstNameFile.isEmpty());

    final String fullNameFile = builder.getFullNameFile();
    assertNotNull("fullNameFile null", fullNameFile);
    assertFalse("fullNameFile empty", fullNameFile.isEmpty());

    builder.initialize();
    assertTrue("builder not initialized", builder.isInitialized());

    final String[] firstNames = builder.getFirstNames();
    assertNotNull("null firstNames for " + firstNameFile, firstNames);
    int n = firstNames.length;
    assertTrue("empty firstNames for " + firstNameFile, n > 0);
    int expected = DataBuilderTestConstants.TEST_FIRST_NAME_COUNT;
    assertEquals("unexpected length", expected, n);

    // System.err.println("First Names=" + firstNames[0]);

    final FLName[] fullNames = builder.getFullNames();
    assertNotNull("null fullNames for " + fullNameFile, fullNames);
    n = fullNames.length;
    assertTrue("empty fullNames for " + fullNameFile, n > 0);
    expected = DataBuilderTestConstants.TEST_FULL_NAME_COUNT;
    assertEquals("unexpected length", expected, n);

    // System.err.println("Full Names=" + fullNames[0]);
  }

  public void testCreateFakeNames()
  {
    builder.initialize();

    final int firstNameCount = 6;
    final int fullNameCount = 3;
    final UserID[] fakes = builder.createFakeNames(firstNameCount,
        fullNameCount);
    assertNotNull("null fakes", fakes);
    final int n = fakes.length;
    assertTrue("empty fakes for", n > 0);
    final int expected = (firstNameCount + 1) * fullNameCount;
    assertEquals("unexpected length", expected, n);

    if (echoFake)
    {
      int i = 0;
      for (UserID id : fakes)
      {
        System.err.println("UserID[" + i + "] " + id);
        if (++i > 9)
          break;
      }
    }
  }
}
