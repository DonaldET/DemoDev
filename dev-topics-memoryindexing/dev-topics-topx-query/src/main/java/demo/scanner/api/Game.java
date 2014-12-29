/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.scanner.api;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

/**
 * Defines key field data associated with a Game, along with the
 * &quot;score&quot; defining the capture.
 * 
 * @author Donald Trummell
 */
public interface Game extends Comparable<Game>, Comparator<Game>, Serializable,
    Cloneable
{
  public abstract String getId();

  public abstract String getPlayerName();

  public abstract Long getScore();

  public abstract Date getPlayedOn();

  public Object clone();
}