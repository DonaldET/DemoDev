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
 * Provide a customer recommendation for games to purchase based on their
 * friends library of games. We know the current customer's library and
 * associated friends libraries.
 * 
 * @author Donald Trummell
 */
public interface Recommend
{
  /**
   * Return the low-to-high ranked products recommended for customer based on
   * friends owning a game customer does not own, rank by popularity with
   * friends.
   * 
   * @param id
   *          the customer for which a recommendation is generated
   * @return zero or more currently unowned product ids ranked by popularity
   *         with friends
   */
  public abstract List<ProductID> getRankedRecommendations(final CustomerID id);
}