/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.searcher.index.impl;

import demo.don.searcher.SearchIndex;
import demo.don.searcher.UserID;
import demo.don.searcher.index.IndexBuilder;
import demo.don.searcher.index.IndexHelper;

/**
 * Create a string based search index of user identifiers with this structure:
 * <p>
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
 * @author Donald Trummell
 *
 */
public class StringIndexBuilderImpl implements IndexBuilder<String>
{
  /**
   * Construct me
   */
  public StringIndexBuilderImpl()
  {
  }

  /**
   * @see demo.lookahead.index.IndexBuilder#buildIndex(demo.lookahead.UserID[])
   */
  @Override
  public SearchIndex<String> buildIndex(final UserID[] userIds)
  {
    final StringBuilder idx = new StringBuilder();

    int ndx = -1;
    for (UserID uid : userIds)
    {
      ndx++;

      idx.append(IndexBuilder.NAME_MARK);
      idx.append(IndexHelper.filterPattern(uid.getFirstName()));

      idx.append(IndexBuilder.NAME_MARK);
      idx.append(IndexHelper.filterPattern(uid.getLastName()));

      idx.append(IndexBuilder.NAME_MARK);
      idx.append(IndexHelper.filterPattern(uid.getEmail()));

      idx.append(IndexBuilder.INDEX_MARK);
      idx.append(IndexHelper.encode(ndx));
    }

    idx.append(IndexBuilder.NAME_MARK);

    return new SearchIndex<String>(idx.toString(), userIds);
  }
}
