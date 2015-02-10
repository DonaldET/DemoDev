package demo.don.searcher.match.test;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.commons.lang3.Validate;

import demo.don.searcher.SearchIndex;
import demo.don.searcher.UserID;
import demo.don.searcher.index.IndexBuilder;
import demo.don.searcher.index.impl.StringIndexBuilderImpl;
import demo.don.searcher.match.IndexSearcher;
import demo.don.searcher.match.impl.StringIndexSearcherImpl;
import demo.don.searcher.test.DataBuilderTestConstants;

public class TestStringIndexSearcherImpl extends TestCase
{
  private static SearchIndex<String> searchIndex = null;
  private static IndexSearcher<String> searcher = null;
  private static final IndexBuilder<String> builder = new StringIndexBuilderImpl();

  static
  {
    final IndexBuilder<String> builder = new StringIndexBuilderImpl();
    searchIndex = builder.buildIndex(DataBuilderTestConstants.uidOrdered);
    searcher = new StringIndexSearcherImpl();
    searcher.setSearchIndex(searchIndex);
  }

  public TestStringIndexSearcherImpl(final String name)
  {
    super(name);
  }

  @Override
  protected void setUp() throws Exception
  {
    super.setUp();

    searcher = new StringIndexSearcherImpl();
    searcher.setSearchIndex(searchIndex);
  }

  @Override
  protected void tearDown() throws Exception
  {
    searcher = null;

    super.tearDown();
  }

  public void testGetMatchLimit()
  {
    assertEquals("default match limit differs",
        IndexSearcher.DEFAULT_MATCH_LIMIT, searcher.getMatchLimit());
  }

  public void testSetup()
  {
    final String si = searchIndex.getSearchIndex();
    assertNotNull("null index", si);
    assertFalse("empty index", si.isEmpty());
    assertEquals("wrong user id list", DataBuilderTestConstants.uidOrdered,
        searchIndex.getUserIds());
  }

  public void testFindUsingLikeBadPreconditions()
  {
    String pattern = "";
    int[] expected = new int[] {};
    checkBadPreconditionLike(pattern, expected);

    pattern = null;
    checkBadPreconditionLike(pattern, expected);
  }

  public void testFindUsingLike()
  {
    String pattern = "?";
    int[] expected = new int[] {};
    checkFindUsingLike(pattern, expected);

    pattern = "z";
    expected = new int[] {};
    checkFindUsingLike(pattern, expected);

    pattern = "m";
    expected = new int[] { 2 };
    checkFindUsingLike(pattern, expected);

    pattern = "c";
    expected = new int[] {};
    checkFindUsingLike(pattern, expected);

    pattern = "d";
    expected = new int[] { 0, 1 };
    checkFindUsingLike(pattern, expected);

    pattern = "r";
    expected = new int[] { 3, 4, 5 };
    checkFindUsingLike(pattern, expected);

    pattern = "detdk";
    expected = new int[] { 1 };
    checkFindUsingLike(pattern, expected);

    pattern = "don";
    expected = new int[] { 1 };
    checkFindUsingLike(pattern, expected);

    pattern = "rabbit";
    expected = new int[] { 3, 4, 5 };
    checkFindUsingLike(pattern, expected);

    pattern = "com";
    expected = new int[] {};
    checkFindUsingLike(pattern, expected);
  }

  public void testFindUsingContainsBadPreconditions()
  {
    String pattern = "";
    int[] expected = new int[] {};
    checkBadPreconditionContains(pattern, expected);

    pattern = null;
    checkBadPreconditionContains(pattern, expected);
  }

  public void testFindUsingContains()
  {
    String pattern = "?";
    int[] expected = new int[] {};
    checkFindUsingContains(pattern, expected);

    pattern = "z";
    expected = new int[] {};
    checkFindUsingContains(pattern, expected);

    pattern = "m";
    expected = new int[] { 0, 1, 2 };
    checkFindUsingContains(pattern, expected);

    pattern = "c";
    expected = new int[] { 0, 1, 5 };
    checkFindUsingContains(pattern, expected);

    pattern = "d";
    expected = new int[] { 0, 1 };
    checkFindUsingContains(pattern, expected);

    pattern = "r";
    expected = new int[] { 1, 3, 4, 5 };
    checkFindUsingContains(pattern, expected);

    pattern = "detdk";
    expected = new int[] { 1 };
    checkFindUsingContains(pattern, expected);

    pattern = "don";
    expected = new int[] { 1 };
    checkFindUsingContains(pattern, expected);

    pattern = "rabbit";
    expected = new int[] { 3, 4, 5 };
    checkFindUsingContains(pattern, expected);

    pattern = "com";
    expected = new int[] { 0, 1 };
    checkFindUsingContains(pattern, expected);
  }

  // ---------------------------------------------------------------------------

  private void checkBadPreconditionLike(final String pattern,
      final int[] expected)
  {
    int[] matches = null;
    boolean caught = false;
    try
    {
      matches = checkFindUsingLike(pattern, expected);
    }
    catch (IllegalArgumentException | NullPointerException ignore)
    {
      caught = true;
    }
    catch (Throwable th)
    {
      fail("unexpected error: " + th.getMessage());
    }

    assertTrue("not caught", caught);
    assertNull("matches not null", matches);
  }

  private void checkBadPreconditionContains(final String pattern,
      final int[] expected)
  {
    int[] matches = null;
    boolean caught = false;
    try
    {
      matches = checkFindUsingContains(pattern, expected);
    }
    catch (NullPointerException | IllegalArgumentException ignore)
    {
      caught = true;
    }
    catch (Throwable th)
    {
      fail("unexpected error: " + th.getMessage() + " from "
          + th.getClass().getName() + ";  for pattern ["
          + String.valueOf(pattern) + "];  expected: ["
          + String.valueOf(expected) + "]");
    }

    assertTrue("not caught", caught);
    assertNull("matches not null", matches);
  }

  private int[] checkFindUsingLike(final String pattern, final int[] expected)
  {
    int[] matches = searcher.findUsingLike(pattern,
        IndexSearcher.DEFAULT_MATCH_LIMIT);
    assertNotNull("null return for " + pattern, matches);
    int nmatch = matches.length;
    if (expected.length < 1)
    {
      assertEquals("unexpected match found for " + pattern, 0, nmatch);
      return expected;
    }

    assertEquals("match size for " + pattern + " differs", expected.length,
        nmatch);

    final Set<Integer> found = new HashSet<Integer>();
    checkLikeMockMatch(pattern, matches, expected, found);
    checkAllMatches(pattern, found);

    return matches;
  }

  private int[] checkFindUsingContains(final String pattern,
      final int[] expected)
  {
    Validate.notNull(expected, "expected null");

    int[] matches = searcher.findUsingContains(pattern,
        IndexSearcher.DEFAULT_MATCH_LIMIT);
    assertNotNull("null return for " + pattern, matches);
    int nmatch = matches.length;
    if (expected.length < 1)
    {
      assertEquals("unexpected match found for " + pattern, 0, nmatch);
      return expected;
    }

    assertEquals("match size for " + pattern + " differs", expected.length,
        nmatch);

    final Set<Integer> found = new HashSet<Integer>();
    checkContainsMockMatch(pattern, matches, expected, found);
    // checkAllMatches(pattern, found);

    return matches;
  }

  private void checkLikeMockMatch(final String pattern, final int[] matches,
      final int[] expected, final Set<Integer> found)
  {
    final String si = searchIndex.getSearchIndex();
    final int nmatch = matches.length;
    final String searchPattern = buildMockLikeSearchPattern(pattern);
    for (int i = 0; i < nmatch; i++)
    {
      final int m = matches[i];
      assertEquals(pattern + " differs at " + i, expected[i], m);
      assertTrue("unable to find " + searchPattern + " in [" + m + "]",
          si.indexOf(searchPattern) > -1);
      found.add(m);
    }

    assertFalse("found empty", found.isEmpty());
  }

  private void checkContainsMockMatch(final String pattern,
      final int[] matches, final int[] expected, final Set<Integer> found)
  {
    final int nmatch = matches.length;
    final Pattern searchPattern = buildMockContainsSearchPattern(pattern);
    for (int i = 0; i < nmatch; i++)
    {
      final int m = matches[i];
      assertEquals(pattern + " differs at " + i, expected[i], m);

      final UserID userID = searchIndex.getUserID(m);
      final SearchIndex<String> indexBuilder = builder
          .buildIndex(new UserID[] { userID });
      final String si = indexBuilder.getSearchIndex();

      final Matcher matcher = searchPattern.matcher(si);
      assertTrue("unable to find " + searchPattern + " in [" + m + "]",
          matcher.find());
      found.add(m);
    }

    assertFalse("found empty", found.isEmpty());
  }

  private void checkAllMatches(final String pattern, final Set<Integer> found)
  {
    assertFalse("found empty", found.isEmpty());

    int i = -1;
    for (UserID id : searchIndex.getUserIds())
    {
      i++;

      boolean fnd = id.getFirstName().toLowerCase().startsWith(pattern)
          || id.getLastName().toLowerCase().startsWith(pattern)
          || id.getEmail().toLowerCase().startsWith(pattern);
      if (found.contains(i))
        assertTrue("unable to find " + pattern + " in [" + i + "]", fnd);
      else
        assertFalse("erronously able to find " + pattern + " in [" + i + "]",
            fnd);
    }
  }

  // ---------------------------------------------------------------------------

  private static String buildMockLikeSearchPattern(final String pattern)
  {
    return IndexBuilder.NAME_MARK + pattern.trim().toLowerCase();
  }

  private static Pattern buildMockContainsSearchPattern(final String pattern)
  {
    final String regex = "\\" + IndexBuilder.NAME_MARK + ".*"
        + pattern.trim().toLowerCase();

    return Pattern.compile(regex);
  }
}
