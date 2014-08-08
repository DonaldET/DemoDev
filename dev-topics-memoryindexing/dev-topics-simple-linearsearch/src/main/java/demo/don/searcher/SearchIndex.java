/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.searcher;

import org.apache.commons.lang3.Validate;

/**
 * A <code>SearchIndex</code> implementation contains a collection of user
 * attributes used to construct a processed search index, and the internal
 * representation of the processed search index itself. The processed search
 * index is used by the <code>IndexSearcher</code> to find matches and
 * references the associated user attribute entry
 *
 * @author Donald Trummell
 *
 * @param <T>
 *          the internal or raw data type of the processed search index
 */
public class SearchIndex<T>
{
  /**
   * The processed search index
   */
  private T searchIndex = null;

  /**
   * The associated user identifiers from which the processed search index was
   * created
   */
  private UserID[] userIds = null;

  /**
   * Construct a search index with a reference to another search index and the
   * user identifiers used to construct the seach index
   *
   * @param searchIndex
   *          the associated search index
   * @param userIds
   *          the user identifiers used to construct the associated search index
   */
  public SearchIndex(final T searchIndex, final UserID[] userIds)
  {
    super();

    setSearchIndex(searchIndex);
    setUserIds(userIds);
  }

  public T getSearchIndex()
  {
    return searchIndex;
  }

  public UserID[] getUserIds()
  {
    return userIds;
  }

  public UserID getUserID(final int index)
  {
    return userIds[index];
  }

  private void setSearchIndex(final T searchIndex)
  {
    Validate.notNull(searchIndex, "searchIndex null");

    this.searchIndex = searchIndex;
  }

  private void setUserIds(final UserID[] userIds)
  {
    Validate.notNull(userIds, "userIds null");
    Validate.notEmpty(userIds, "userIds empty");

    this.userIds = userIds;
  }

  @Override
  public String toString()
  {
    final StringBuilder msg = new StringBuilder();
    msg.append("[");
    msg.append(getClass().getSimpleName());
    msg.append(" - 0x");
    msg.append(Integer.toHexString(hashCode()));
    msg.append(";\n  userIds");

    if (userIds == null)
      msg.append(": null");
    else
    {
      final int nids = userIds.length;
      if (nids < 1)
        msg.append(": empty");
      else
      {
        msg.append("[");
        msg.append(nids);
        msg.append("]: {");
        final int lmt = Math.min(7, nids);
        for (int i = 0; i < lmt; i++)
        {
          msg.append("\n      ");
          msg.append(userIds[i]);
        }

        if (lmt < nids)
          msg.append("\n      . . .");
        msg.append("}");
      }

      msg.append(";\n  searchIndex");
      if (searchIndex == null)
        msg.append(": null");
      else
      {
        if (searchIndex instanceof String)
        {
          final String si = (String) searchIndex;
          final int nsi = si.length();
          if (nsi > 0)
          {
            msg.append("[");
            msg.append(nsi);
            msg.append("]: {");

            final int lmt = Math.min(200, nsi);
            msg.append(si.substring(0, lmt));

            if (lmt < nsi)
            {
              msg.append(" . . .");
              msg.append(si.substring(nsi - 1));
            }
            msg.append("}");
          }
          else
            msg.append(": empty");
        }
        else
          msg.append(searchIndex);
      }
    }

    msg.append("\n]");

    return msg.toString();
  }
}
