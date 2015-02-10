/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.scanner.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import demo.scanner.api.AscendingComparator;
import demo.scanner.api.Game;

/**
 * This is a simple bean storing game attributes, and including all data defined
 * by the <code>Game</code> interface; additional data may be supplied that,
 * while part of a <code>Game</code> instance, is not part of the unique game
 * identity (e.g., <em>key</em> is part of the identity.)
 * 
 * @author Donald Trummell
 */
public class GameBean implements Game, Cloneable
{
  private static final long serialVersionUID = 632549330886478803L;

  private final String id;
  private final String playerName;
  private final Long score;
  private final Date playedOn;

  private final Comparator<Game> comparator = new AscendingComparator(true);

  /**
   * Construct the game bean using <em>key</em> fields
   * 
   * @param id
   *          a unique identifier across all games (e.g., a <em>GUID</em>
   * @param playerName
   *          field entered at game console
   * @param score
   *          final score associated with a competed game
   * @param playedOn
   *          date and time game was completed
   */
  public GameBean(final String id, final String playerName, final Long score,
      final Date playedOn)
  {
    super();
    this.id = id;
    this.playerName = playerName;
    this.score = score;
    this.playedOn = playedOn;
  }

  /**
   * Alternate constructor for the game bean using <em>key</em> fields
   * 
   * @param id
   *          a unique identifier across all games (e.g., a <em>GUID</em>
   * @param playerName
   *          field entered at game console
   * @param score
   *          final score associated with a competed game
   * @param playedOn
   *          <code>Date</code> game was completed
   */
  public GameBean(final String id, final String playerName, final Long score,
      final Long playedOn)
  {
    this(id, playerName, score, new Date(playedOn));
  }

  /**
   * @see java.lang.Object#clone()
   */
  @Override
  public Object clone()
  {
    GameBean copy = null;
    try
    {
      copy = (GameBean) super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      throw new IllegalArgumentException("clone failed: " + e.getMessage(), e);
    }

    return copy;
  }

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    final StringBuilder info = new StringBuilder();
    info.append("[");
    info.append(getClass().getSimpleName());
    info.append(" - 0x");
    info.append(Integer.toHexString(hashCode()));
    info.append(";  id: ");
    info.append(id);
    info.append(";  score: ");
    info.append(score);
    info.append(";  playerName: ");
    info.append(playerName);
    info.append(";  playedOn: ");
    info.append(playedOn);
    info.append("--[");
    info.append(playedOn == null ? 0l : playedOn.getTime());
    info.append("]");

    return info.toString();
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;

    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((playedOn == null) ? 0 : playedOn.hashCode());
    result = prime * result
        + ((playerName == null) ? 0 : playerName.hashCode());
    result = prime * result + ((score == null) ? 0 : score.hashCode());

    return result;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    GameBean other = (GameBean) obj;
    if (id == null)
    {
      if (other.id != null)
        return false;
    }
    else if (!id.equals(other.id))
      return false;
    if (playedOn == null)
    {
      if (other.playedOn != null)
        return false;
    }
    else if (!playedOn.equals(other.playedOn))
      return false;
    if (playerName == null)
    {
      if (other.playerName != null)
        return false;
    }
    else if (!playerName.equals(other.playerName))
      return false;
    if (score == null)
    {
      if (other.score != null)
        return false;
    }
    else if (!score.equals(other.score))
      return false;
    return true;
  }

  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public String getPlayerName()
  {
    return playerName;
  }

  @Override
  public Long getScore()
  {
    return score;
  }

  @Override
  public Date getPlayedOn()
  {
    return new Date(playedOn.getTime());
  }

  @Override
  public int compareTo(final Game other)
  {
    if (other == null)
      return -1;

    if (this.equals(other))
      return 0;

    return comparator.compare(this, other);
  }

  @Override
  public int compare(final Game lhs, final Game rhs)
  {
    return comparator.compare(lhs, rhs);
  }

  @Override
  public Comparator<Game> reversed()
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Comparator<Game> thenComparing(Comparator<? super Game> other)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <U> Comparator<Game> thenComparing(
      Function<? super Game, ? extends U> keyExtractor,
      Comparator<? super U> keyComparator)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public <U extends Comparable<? super U>> Comparator<Game> thenComparing(
      Function<? super Game, ? extends U> keyExtractor)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Comparator<Game> thenComparingInt(
      ToIntFunction<? super Game> keyExtractor)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Comparator<Game> thenComparingLong(
      ToLongFunction<? super Game> keyExtractor)
  {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Comparator<Game> thenComparingDouble(
      ToDoubleFunction<? super Game> keyExtractor)
  {
    throw new UnsupportedOperationException("Not implemented");
  }
}
