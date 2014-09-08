/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.amazon.games;

import java.util.List;

/**
 * A Data Access Object that provides these two library functions to help you
 * recommend games:
 * <ul>
 * <li>getFriendsListForUser - returns a list of customer IDs (strings that
 * uniquely identify an Amazon user) representing the friends of an Amazon user.
 * </li>
 * <li>getLibraryForUser - returns a list of product IDs (strings that uniquely
 * identify a game) for an Amazon user</li>
 * </ul>
 *
 * @author Donald Trummell
 */
public interface Dao
{
  public abstract List<CustomerID> getFriendsListForUser(final CustomerID id);

  public abstract List<ProductID> getLibraryForUser(final CustomerID id);
}
