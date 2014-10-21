/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.amazon.games.recommenders;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import demo.amazon.games.CustomerID;
import demo.amazon.games.Dao;
import demo.amazon.games.ProductID;
import demo.amazon.games.Recommend;

/**
 * <strong>Recommendation Engine</strong>
 * <p>
 * Create a recommendation feature called &quot;Games Your Friends Play&quot;.
 * The recommendation logic is based on the following rules: <br>
 * <ul>
 * <li>A customer should only be recommended games that their friends own but
 * they don&apos;t.</li>
 * <li>The recommendations priority is driven by how many friends own a game -
 * if multiple friends own a particular game, it should be higher in the
 * recommendations than a game that only one friend owns.</li>
 * </ul>
 * <br>
 * You are provided two library functions to help you: <br>
 * <ul>
 * <li><code>getFriendsListForUser</code> - returns a list of customer IDs
 * (strings that uniquely identify an Amazon user) representing the friends of
 * an Amazon user.</li>
 * <li><code>getLibraryForUser</code> - returns a list of product IDs (strings
 * that uniquely identify a game) for an Amazon user</li>
 * </ul>
 * <br>
 * We supply an example of the following: <br>
 * <ol>
 * <li>A function that provides a ranked (high to low) list of recommendations
 * (product IDs) for a provided user.
 * <li>Key unit tests.</li>
 * <li>The space and time complexity of our solution.</li>
 * </ol>
 * <br>
 * For a space-time complexity analysis applied to customer and friends, we use
 * the following notations: <br>
 * <ul>
 * <li><em>L</em>: average number of products in a customer library</li>
 * <li><em>P</em>: fraction of games unique to each customer library</li>
 * <li><em>F</em>: average number of friends of a customer</li>
 * <li><em>N</em>: total number of products in catalog</li>
 * <li><em>R</em>: expected number-of-recommendations</li>
 * </ul>
 * <br>
 * The expected number of recommendations is <em>R = F * (P * L)</em>;
 * constrained to be less than <em>N</em>. The performance analysis is: <br>
 * <ol>
 * <li>Count - O(<em>F</em> * <em>L</em>)</li>
 * <li>Remove - O(<em>L</em>)</li>
 * <li>Extract - O(log(<em>R</em>))
 * </ol>
 * <br>
 * The dominant performance from the steps above is O(<em>F</em> * <em>L</em>).
 *
 * @author Donald Trummell
 */
public class SimpleRecommender implements Recommend
{
  private final Dao dao;

  public SimpleRecommender(final Dao dao)
  {
    this.dao = dao;
  }

  /**
   * Recommend games based on popularity with friends
   *
   * @param id
   *          the customer for which recommendations are created
   * @return a list of product recommendations ordered from most popular to
   *         least popular with friends of the customer
   */
  @Override
  public List<ProductID> getRankedRecommendations(final CustomerID id)
  {
    final List<ProductID> recommendations = new ArrayList<ProductID>();
    if (id == null)
      return recommendations;

    final Map<ProductID, Integer> counts = getGamePopularity(id);
    if (counts == null || counts.isEmpty())
      return recommendations;

    removeOwnedGames(id, counts);

    recommendations.addAll(extractOrderedRecommendations(counts));

    return recommendations;
  }

  /**
   * Count all games in all friends libraries. Performance is based on count
   * step.
   * <p>
   * <ul>
   * <li>Space: O(F * L)</li>
   * <li>Time: O(F * L)</li>
   * </ul>
   *
   * @param id
   *          id of customer with friends
   * @return fiend usage count associated with a product
   */
  private Map<ProductID, Integer> getGamePopularity(final CustomerID id)
  {
    final List<CustomerID> friends = dao.getFriendsListForUser(id);
    if (friends == null)
      throw new IllegalArgumentException("unknown friend list id " + id);

    if (friends.isEmpty())
      return new HashMap<ProductID, Integer>();

    return countGameUse(friends);
  }

  /**
   * Count how often a game appears in a library.
   * <p>
   * <ul>
   * <li>Space: O(F * L)</li>
   * <li>Time: O(F * L)</li>
   * </ul>
   *
   * @param friends
   * @return
   */
  private Map<ProductID, Integer> countGameUse(final List<CustomerID> friends)
  {
    final Map<ProductID, Integer> counts = new HashMap<ProductID, Integer>();
    for (final CustomerID friend : friends)
    {
      final List<ProductID> libraryForUser = dao.getLibraryForUser(friend);
      if (libraryForUser != null && !libraryForUser.isEmpty())
      {
        for (final ProductID pid : libraryForUser)
        {
          Integer count = counts.get(pid);
          if (count == null)
            count = 1;
          else
            count = count.intValue() + 1;
          counts.put(pid, count);
        }
      }
    }

    return counts;
  }

  /**
   * Remove games owned by customer.
   * <p>
   * <ul>
   * <li>Space: -N/A-</li>
   * <li>Time: O(L)</li>
   * </ul>
   *
   * @param id
   *          the customer
   * @param counts
   *          the game usage counts to clean
   */
  private void removeOwnedGames(final CustomerID id,
      final Map<ProductID, Integer> counts)
  {
    final List<ProductID> libraryForUser = dao.getLibraryForUser(id);
    if (libraryForUser != null && !libraryForUser.isEmpty())
    {
      for (final ProductID pid : libraryForUser)
        counts.remove(pid);
    }
  }

  /**
   * Order recommendations by usage count.
   * <p>
   * <ul>
   * <li>Space: O(R)</li>
   * <li>Time: O(log(R))</li>
   * </ul>
   * <p>
   * Copied reference information. <code>
   * PriorityQueuemplementation note: this implementation provides O(log(n))
   * time for the enqueuing and dequeuing methods (offer, poll, remove() and add);
   * linear time for the remove(Object) and contains(Object) methods; and constant
   * time for the retrieval methods (peek, element, and size)
   * </code>
   *
   * @param counts
   *          counts of usage by product id
   * @return list of product ids ordered by counts
   */
  private List<ProductID> extractOrderedRecommendations(
      final Map<ProductID, Integer> counts)
  {
    final List<ProductID> recommendations = new ArrayList<ProductID>();
    if (counts == null || counts.isEmpty())
      return recommendations;

    final PriorityQueue<WeightedGame> ordered = new PriorityQueue<WeightedGame>(
        new WeightedGameReverseComparitor());
    for (final Entry<ProductID, Integer> e : counts.entrySet())
      ordered.offer(new WeightedGame(e.getKey(), e.getValue()));

    final List<ProductID> results = new ArrayList<ProductID>();
    for (WeightedGame recommendation = ordered.poll(); recommendation != null; recommendation = ordered
        .poll())
    {
      results.add(recommendation.getId());
    }

    return results;
  }
}

class WeightedGame implements Comparator<WeightedGame>
{
  private final ProductID id;
  private final Integer weight;

  private final Comparator<WeightedGame> comp = new WeightedGameComparitor();

  public WeightedGame(final ProductID id, final Integer weight)
  {
    this.id = id;
    this.weight = weight;
  }

  public ProductID getId()
  {
    return id;
  }

  public Integer getWeight()
  {
    return weight;
  }

  @Override
  public int compare(final WeightedGame lhs, final WeightedGame rhs)
  {
    return comp.compare(lhs, rhs);
  }
}

class WeightedGameComparitor implements Comparator<WeightedGame>
{
  @Override
  public int compare(final WeightedGame lhs, final WeightedGame rhs)
  {
    if (lhs == null)
      return rhs == null ? 0 : -1;

    if (rhs == null)
      return 1;

    final Integer lhsWeight = lhs.getWeight();
    final Integer rhsWeight = rhs.getWeight();

    if (lhsWeight == null)
      return rhsWeight == null ? 0 : -1;

    if (rhsWeight == null)
      return 1;

    int d = lhsWeight.intValue() - rhsWeight.intValue();
    if (d == 0)
    {
      final ProductID lhsId = lhs.getId();
      final ProductID rhsId = rhs.getId();

      if (lhsId == null)
        d = rhsId == null ? 0 : -1;
      else
      {
        if (rhsId == null)
          d = 1;
        else
          d = lhsId.compareTo(rhsId);
      }
    }

    return d;
  }
}

class WeightedGameReverseComparitor implements Comparator<WeightedGame>
{
  @Override
  public int compare(final WeightedGame lhs, final WeightedGame rhs)
  {
    int d = 0;
    if (lhs == null)
      d = rhs == null ? 0 : -1;
    else if (rhs == null)
      d = 1;

    if (d != 0)
      return -d;

    return lhs.compare(lhs, rhs);
  }
}
