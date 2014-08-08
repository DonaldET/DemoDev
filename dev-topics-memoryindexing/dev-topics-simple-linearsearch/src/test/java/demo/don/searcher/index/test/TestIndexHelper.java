package demo.don.searcher.index.test;

import junit.framework.TestCase;
import demo.don.searcher.index.IndexHelper;

public class TestIndexHelper extends TestCase
{
  private static final int TEST_RADIX = 62;
  private static final int TEST_MAX_DIGITS = 3;
  private static final int TEST_MAX_VALUE = (int) (Math.ceil(Math.pow(
      ((double) IndexHelper.RADIX), (double) IndexHelper.MAX_DIGITS))) - 1;

  public TestIndexHelper(final String name)
  {
    super(name);
  }

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testConstantValues()
  {
    assertEquals("expected radix differs", TEST_RADIX, IndexHelper.RADIX);
    assertEquals("expected digit size differs", TEST_MAX_DIGITS,
        IndexHelper.MAX_DIGITS);
    //
    // Note: this was 238,327 at the time of the test
    //

    assertEquals("expected largest value differs", TEST_MAX_VALUE,
        IndexHelper.MAX_VALUE);
  }

  public void testEncode()
  {
    int value = 0;
    String encoded = IndexHelper.encode(value);
    String expected = "000";
    assertEquals("encoding differs", expected, encoded);

    value = 1;
    encoded = IndexHelper.encode(value);
    expected = "001";
    assertEquals("encoding differs", expected, encoded);

    value = 444;
    encoded = IndexHelper.encode(value);
    expected = "07A";
    assertEquals("encoding differs", expected, encoded);

    value = 7919;
    encoded = IndexHelper.encode(value);
    expected = "23j";
    assertEquals("encoding differs", expected, encoded);

    value = TEST_MAX_VALUE;
    encoded = IndexHelper.encode(value);
    expected = "zzz";
    assertEquals("encoding differs", expected, encoded);
  }

  public void testEncodeFailedBounds()
  {
    checkEncodeFailedBounds(-1);
    checkEncodeFailedBounds(Integer.MIN_VALUE);
    checkEncodeFailedBounds(Integer.MAX_VALUE);
    checkEncodeFailedBounds(TEST_MAX_VALUE + 1);
  }

  private void checkEncodeFailedBounds(final int value)
  {
    boolean caught = false;
    String encoded = null;
    try
    {
      encoded = IndexHelper.encode(value);
    }
    catch (IllegalArgumentException ignore)
    {
      caught = true;
    }

    assertTrue(value + " not caught", caught);
    assertNull(value + " was encoded", encoded);
  }

  public void testDecode()
  {
    String endcodedVal = "0";
    int decoded = IndexHelper.decode(endcodedVal);
    int expected = 0;
    assertEquals("decode failed", expected, decoded);

    endcodedVal = "1";
    decoded = IndexHelper.decode(endcodedVal);
    expected = 1;
    assertEquals("decode failed", expected, decoded);

    endcodedVal = " 1 ";
    decoded = IndexHelper.decode(endcodedVal);
    expected = 1;
    assertEquals("decode failed", expected, decoded);

    endcodedVal = "01";
    decoded = IndexHelper.decode(endcodedVal);
    expected = 1;
    assertEquals("decode failed", expected, decoded);

    endcodedVal = "00000000000000000000123";
    decoded = IndexHelper.decode(endcodedVal);
    expected = 3971;
    assertEquals("decode failed", expected, decoded);

    endcodedVal = "00000000000000000000jjj";
    decoded = IndexHelper.decode(endcodedVal);
    expected = 175815;
    assertEquals("decode failed", expected, decoded);

    endcodedVal = "zzz";
    decoded = IndexHelper.decode(endcodedVal);
    expected = 238327;
    assertEquals("decode failed", expected, decoded);

    endcodedVal = " zzz ";
    decoded = IndexHelper.decode(endcodedVal);
    expected = 238327;
    assertEquals("decode failed", expected, decoded);

    endcodedVal = "0000000zzz";
    decoded = IndexHelper.decode(endcodedVal);
    expected = 238327;
    assertEquals("decode failed", expected, decoded);
  }

  public void testDecodeParamPreconditions()
  {
    checkDecodeParams(null);
    checkDecodeParams("");
    checkDecodeParams(" 0 0 0 ");
    checkDecodeParams("-3");
    checkDecodeParams("12&3");
  }

  public void checkDecodeParams(final String encoded)
  {
    boolean caught = false;
    int decoded = Integer.MIN_VALUE;
    try
    {
      decoded = IndexHelper.decode(encoded);
    }
    catch (IllegalArgumentException | NullPointerException ignore)
    {
      caught = true;
    }

    assertTrue(decoded + " not caught", caught);
    assertTrue(decoded + " was decoded", decoded == Integer.MIN_VALUE);
  }

  public void testRoundTrip()
  {
    int value = 0;
    String encoded = IndexHelper.encode(value);
    int decoded = IndexHelper.decode(encoded);
    assertEquals("round trip failed", value, decoded);

    value = 1;
    encoded = IndexHelper.encode(value);
    decoded = IndexHelper.decode(encoded);
    assertEquals("round trip failed", value, decoded);

    value = 2;
    encoded = IndexHelper.encode(value);
    decoded = IndexHelper.decode(encoded);
    assertEquals("round trip failed", value, decoded);

    value = 3;
    encoded = IndexHelper.encode(value);
    decoded = IndexHelper.decode(encoded);
    assertEquals("round trip failed", value, decoded);

    value = 33;
    encoded = IndexHelper.encode(value);
    decoded = IndexHelper.decode(encoded);
    assertEquals("round trip failed", value, decoded);

    value = 222;
    encoded = IndexHelper.encode(value);
    decoded = IndexHelper.decode(encoded);
    assertEquals("round trip failed", value, decoded);

    value = 1901;
    encoded = IndexHelper.encode(value);
    decoded = IndexHelper.decode(encoded);
    assertEquals("round trip failed", value, decoded);

    value = TEST_MAX_VALUE;
    encoded = IndexHelper.encode(value);
    decoded = IndexHelper.decode(encoded);
    assertEquals("round trip failed", value, decoded);
  }
}
