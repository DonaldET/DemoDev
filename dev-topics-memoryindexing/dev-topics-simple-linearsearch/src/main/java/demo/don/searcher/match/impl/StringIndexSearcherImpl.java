/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.searcher.match.impl;

import org.apache.commons.lang3.Validate;

import demo.don.searcher.SearchIndex;
import demo.don.searcher.index.IndexBuilder;
import demo.don.searcher.index.IndexHelper;
import demo.don.searcher.match.IndexSearcher;

/**
 * This <code>IndexSearcher</code> implementation manages a <code>String</code>
 * based search index to support queries that locate entries matching a string
 * value in the index with <em>like</em> or <em>contains</em> semantics.
 * <p>
 * The structure of the string based search index is:
 *
 * <pre>
 * <strong>NAME_MARK</strong> <em>firstName</em>
 * <strong>NAME_MARK</strong> <em>lastName</em>
 * <strong>NAME_MARK</strong> <em>Email</em>
 * <strong>INDEX_MARK</strong> <em>encodedIndex</em>
 * </pre>
 *
 * The <code>encodedIndex</code> is a text field that defines the user
 * identifier entry from which this index entry was created.
 * <p>
 * <strong>Note:</strong> The characters <code>NAME_MARK</code> and
 * <code>INDEX_MARK</code> are not allowed in the text to search or the pattern
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 */
public class StringIndexSearcherImpl implements IndexSearcher<String>
{
  /**
   * Stores the associated index from a match and the next search index position
   * to test
   *
   * @author Donald Trummell (dtrummell@gmail.com)
   */
  public static final class ExtractIndex
  {
    final int associatedIndex;
    final int nextPosition;

    /**
     * Save the associated user identifier index and the next scan position
     *
     * @param associatedIndex
     * @param nextPosition
     */
    public ExtractIndex(final int associatedIndex, final int nextPosition)
    {
      super();

      this.associatedIndex = associatedIndex;
      this.nextPosition = nextPosition;
    }

    public int getAssociatedIndex()
    {
      return associatedIndex;
    }

    public int getNextPosition()
    {
      return nextPosition;
    }
  }

  private static final int[] ZERO_LTH_RESULT = new int[0];
  private int matchLimit = DEFAULT_MATCH_LIMIT;
  private SearchIndex<String> searchIndex = null;

  /**
   * Construct me
   */
  public StringIndexSearcherImpl()
  {
  }

  /**
   * @see demo.lookahead.match.IndexSearcher#getMatchLimit()
   */
  @Override
  public int getMatchLimit()
  {
    return matchLimit;
  }

  /**
   * @see demo.lookahead.match.IndexSearcher#setMatchLimit(int)
   */
  @Override
  public void setMatchLimit(final int matchLimit)
  {
    Validate.isTrue(matchLimit > 0, "matchLimit small");

    this.matchLimit = matchLimit;
  }

  /**
   * @see demo.lookahead.match.IndexSearcher#getSearchIndex()
   */
  @Override
  public SearchIndex<String> getSearchIndex()
  {
    return searchIndex;
  }

  /**
   * @see demo.lookahead.match.IndexSearcher#setSearchIndex(demo.lookahead.SearchIndex)
   */
  @Override
  public void setSearchIndex(final SearchIndex<String> searchIndex)
  {
    Validate.notNull(searchIndex, "searchIndex null");

    Validate.isTrue(
        searchIndex.getSearchIndex().endsWith(IndexBuilder.NAME_MARK),
        "no end mark");

    this.searchIndex = searchIndex;
  }

  /**
   * Left match
   */
  @Override
  public int[] findUsingLike(final String leftMatchPattern,
      final int maxReturned)
  {
    Validate.notEmpty(leftMatchPattern, "leftMatchPattern empty");
    Validate.isTrue(maxReturned > 0, "maxReturned small, ", maxReturned);
    validateInvariant();

    final String cleanedPattern = IndexHelper.filterPattern(leftMatchPattern);
    if (cleanedPattern.isEmpty())
      return ZERO_LTH_RESULT;
    final String pattern = IndexBuilder.NAME_MARK + cleanedPattern;

    final String index = searchIndex.getSearchIndex();
    int matchPos = 0;
    int matchCount = 0;
    final int[] matches = new int[matchLimit];
    while ((matchPos = index.indexOf(pattern, matchPos)) > -1
        && matchCount < matchLimit)
    {
      final int beyondPatternEnd = matchPos + pattern.length();
      final ExtractIndex extract = getAssociatedIndex(index, beyondPatternEnd);
      matches[matchCount++] = extract.getAssociatedIndex();

      matchPos = extract.getNextPosition();
      if (matchPos >= index.length())
        break;
    }

    if (matchCount < 1)
      return ZERO_LTH_RESULT;

    final int[] result = new int[matchCount];
    System.arraycopy(matches, 0, result, 0, matchCount);

    return result;
  }

  /**
   * Contains match
   */
  @Override
  public int[] findUsingContains(final String containsPattern,
      final int maxReturned)
  {
    Validate.notEmpty(containsPattern, "containsPattern empty");
    Validate.isTrue(maxReturned > 0, "maxReturned small, ", maxReturned);

    final String cleanedPattern = IndexHelper.filterPattern(containsPattern);
    if (cleanedPattern.isEmpty())
      return new int[0];

    //
    // pat = ...............................YYY---
    // idx = |fn1|ln1{0|fn2|ln2{1| ... |fnx|lnx{x|
    //

    final String index = searchIndex.getSearchIndex();
    final int idxLth = index.length();
    final int patLth = cleanedPattern.length();
    final int boundry = idxLth - patLth - 3;
    if (boundry < 0)
      return new int[0];

    int beginIndex = 1;
    int matchCount = 0;
    final int[] matches = new int[maxReturned];
    while (matchCount < maxReturned && beginIndex <= boundry)
    {
      final int associatedIndexPos = index.indexOf(IndexBuilder.INDEX_MARK,
          beginIndex);
      if (associatedIndexPos < 0)
        throw new IllegalStateException("no '" + IndexBuilder.INDEX_MARK
            + "' found beyond " + beginIndex);

      final int nextEntryPos = index.indexOf(IndexBuilder.NAME_MARK,
          associatedIndexPos + 1);
      if (nextEntryPos < 0)
        throw new IllegalStateException("no '" + IndexBuilder.INDEX_MARK
            + "' found beyond " + associatedIndexPos);

      final String indexEntry = index.substring(beginIndex, associatedIndexPos);
      final int matchPos = indexEntry.indexOf(cleanedPattern);
      if (matchPos > -1)
        matches[matchCount++] = IndexHelper.decode(index.substring(
            associatedIndexPos + 1, nextEntryPos));

      beginIndex = nextEntryPos + 1;
    }

    if (matchCount < 1)
      return new int[0];

    final int[] result = new int[matchCount];
    System.arraycopy(matches, 0, result, 0, matchCount);

    return result;
  }

  // ---------------------------------------------------------------------------

  private ExtractIndex getAssociatedIndex(final String index,
      final int beyondPatternEnd)
  {
    int associatedIndexPos = index.indexOf(IndexBuilder.INDEX_MARK,
        beyondPatternEnd);
    if (associatedIndexPos < 0)
      throw new IllegalStateException("malformed string search index, missing "
          + IndexBuilder.INDEX_MARK + " beyond " + beyondPatternEnd);

    int nextEntryPos = index.indexOf(IndexBuilder.NAME_MARK,
        associatedIndexPos + 1);
    if (nextEntryPos < 0)
      throw new IllegalStateException("malformed string search index, missing "
          + IndexBuilder.NAME_MARK + " at end beyond " + associatedIndexPos);

    final int associatedIndex = IndexHelper.decode(index.substring(
        associatedIndexPos + 1, nextEntryPos));

    return new ExtractIndex(associatedIndex, nextEntryPos + 1);
  }

  /**
   * Insure class has valid state
   */
  private void validateInvariant()
  {
    Validate.isTrue(matchLimit > 0, "matchLimit small, " + matchLimit);
    Validate.notNull(searchIndex, "searchIndex null");

    //
    // Note: we except the index to end with a name mark to guarantee that the
    // index of the associated UserID is ended
    //

    Validate.isTrue(
        searchIndex.getSearchIndex().endsWith(IndexBuilder.NAME_MARK),
        "no end mark");
  }
}
