/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.searcher.index;

import demo.don.searcher.SearchIndex;
import demo.don.searcher.UserID;

/**
 * An <code>IndexBuilder</code> implementation creates a
 * <code>SearchIndex</code> (of the raw or internal type <code>T</code>) from a
 * collection of user identifiers. The resulting search index is then used by an
 * <code>IndexSearcher</code> implementation that finds for all matching user
 * identifiers by <em>like</em> or <em>contains</em> semantics.
 * <p>
 * <strong>Note:</strong> It is assumed that the search pattern used by the
 * <code>IndexSearcher</code> is processed to conform to the entries in the
 * search index. Typical processing is to force to lower case, trim, remove
 * special characters, and disallow empty strings
 *
 * @author Donald Trummell
 *
 * @param <T>
 *          the raw or internal data type of the processed search index
 *          constructed by the index builder
 */
public interface IndexBuilder<T>
{
  /**
   * Special character delimiting the search fields
   */
  public static final String NAME_MARK = "|";

  /**
   * Special character marking the coded index of the associated user identifier
   */
  public static final String INDEX_MARK = "{";

  /**
   * Create a search index of user identifiers that an index searcher uses to
   * find matches of a pattern
   *
   * @param userIds
   *          the data used to construct the search index
   *
   * @return the constructed search index
   */
  public abstract SearchIndex<T> buildIndex(final UserID[] userIds);
}