/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.bignumeric.impl;

import java.util.Random;

import demo.don.bignumeric.api.SequenceGenerator;

/**
 * Generate a sequence of scaled, bounded randomized values within a range.
 * Maintain the correct sum to the current iteration.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class ShuffleSequenceGenerator implements SequenceGenerator
{
  private long iteration = 0;
  private final int origin = 100;
  private final int bound = 50000;
  private final double scale = 53.0;

  private final long seed = 7841;
  private final Random rnd = new Random(seed);

  private long longSum = 0;

  public ShuffleSequenceGenerator()
  {
  }

  /**
   * Compute the double precision sum of the first <em>n</em> terms as return
   * single precision.
   *
   * @return the sum of the first <em>iteration</em> terms
   */
  @Override
  public float correctSum()
  {
    return (float) (longSum / scale);
  }

  @Override
  public float getNext()
  {
    iteration++;

    final int k = rnd.nextInt(bound - origin);
    longSum += k;

    return (float) (k / scale);
  }

  @Override
  public long getIteration()
  {
    return iteration;
  }

  // ---------------------------------------------------------------------------

  @Override
  public String toString()
  {
    return "[" + getClass().getSimpleName() + " - 0x"
        + Integer.toHexString(hashCode()) + ";  iteration:" + iteration
        + ";  origin: " + origin + ";  bound: " + bound + ";  scale: " + scale
        + ";  seed: " + seed + ";  longSum: " + longSum + "]";
  }
}
