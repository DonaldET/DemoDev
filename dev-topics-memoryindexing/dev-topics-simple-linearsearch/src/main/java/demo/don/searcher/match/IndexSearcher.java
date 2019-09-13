/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.searcher.match;

import demo.don.searcher.SearchIndex;

/**
 * An <code>IndexSearcher</code> implementation uses a <code>SearchIndex</code>
 * implementation (of the same type <code>T</code>) to find user identifier
 * matches that either start with (&quot;<em>like</em>&quot;) or contain a
 * simple string pattern.
 * <p>
 * Note that an <code>IndexSearcher</code> implementation may limit the maximum
 * number of initial matches returned, but &quot;paging&quot; through the
 * matches is not supported. Typically an <code>IndexSearcher</code> of a type
 * <code>T</code> is created using an <code>IndexBuilder</code> of the same type
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 * @param <T>
 *          the internal data type of search index
 */
public interface IndexSearcher<T>
{
  public static final int DEFAULT_MATCH_LIMIT = 20;

  public abstract int getMatchLimit();

  public abstract void setMatchLimit(final int matchLimit);

  public abstract SearchIndex<T> getSearchIndex();

  public abstract void setSearchIndex(final SearchIndex<T> searchIndex);

  public abstract int[] findUsingLike(final String leftMatchPattern,
      final int maxReturned);

  public abstract int[] findUsingContains(final String containsPattern,
      final int maxReturned);
}