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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import demo.scanner.api.AscendingComparator;
import demo.scanner.api.Game;
import demo.scanner.api.GameDataBuilder;
import demo.scanner.api.GameDataBuilder.ListOrder;

/**
 * Creates artificial <code>Game</code> instances populated with artifical data
 * for testing
 * 
 * @author Donald Trummell
 */
@SuppressWarnings("unused")
public class SimpleGameDataBuilder implements Serializable, GameDataBuilder
{
  private static final long serialVersionUID = -9126122119348644310L;

  public static final long MAX_DATA = 512l * 1024l;

  private static final long SEED_PRIME = 7673l;
  private static String[] fnames = { "Ann", "Barb", "Carl", "Dawn", "Don",
      "Elizabeth", "Jill", "Robert", "Sam", "Ted", "Tom" };
  private static String[] lnames = { "Arnold", "Cruthers", "Douglas",
      "Edwards", "Fox", "Gavin", "Samson", "Tanner", "Wyles", "Zane" };

  private boolean initialized = false;

  private List<Game> playList = new ArrayList<Game>();

  public SimpleGameDataBuilder()
  {
    destroy();
  }

  @Override
  public boolean isInitialized()
  {
    return initialized;
  }

  @Override
  public void initialize(final long nCases, final long baseDate)
  {
    if (isInitialized())
      throw new IllegalStateException("already initialized");

    if (nCases < 0)
      throw new IllegalArgumentException("nCases < 0");

    if (nCases > MAX_DATA)
      throw new IllegalArgumentException("nCases > " + MAX_DATA);

    if (baseDate < 0)
      throw new IllegalArgumentException("baseDate < 0");

    playList.clear();

    final Random rgen = new Random();
    rgen.setSeed(SEED_PRIME);

    long idBase = MAX_DATA;
    for (long i = 0; i < nCases; i++)
    {
      final String id = "ID" + (100000l + idBase - i);

      final double randVal1 = rgen.nextDouble();
      final StringBuilder playerName = new StringBuilder();
      playerName.append(lnames[(int) Math.floor(randVal1 * lnames.length)]);
      playerName.append(", ");
      final double randVal2 = rgen.nextDouble();
      playerName.append(fnames[(int) Math.floor(randVal2 * fnames.length)]);

      final double randVal3 = rgen.nextDouble();
      final long score = 250l + (long) Math.floor(randVal3 * 250000l);

      final Date playedOn = new Date(baseDate
          + (long) Math.ceil(randVal1 * 5000000l));

      final Game g = new GameBean(id, playerName.toString(), score, playedOn);

      playList.add(g);
    }

    initialized = true;
  }

  @Override
  public void destroy()
  {
    playList.clear();
    initialized = false;
  }

  @Override
  public List<Game> getData(final ListOrder sort)
  {
    if (!isInitialized())
      throw new IllegalStateException("uninitialized");

    final int n = playList.size();
    final List<Game> copy = new ArrayList<Game>(n);
    if (n > 0)
    {
      if (sort != ListOrder.unordered)
      {
        Game[] copyAry = new Game[n];
        playList.toArray(copyAry);
        Arrays.sort(copyAry, new AscendingComparator(
            sort == ListOrder.ascending));
        copy.addAll(Arrays.asList(copyAry));
        copyAry = null;
      }
      else
      {
        copy.addAll(playList);
      }
    }

    return copy;
  }
}
