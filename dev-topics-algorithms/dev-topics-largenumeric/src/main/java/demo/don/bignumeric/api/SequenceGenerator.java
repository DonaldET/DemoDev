/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.bignumeric.api;

/**
 * Generate a sequence of floating point numbers, tracking invocations, and
 * returning the next generated element. Offer an accurate current sum
 * operation.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface SequenceGenerator
{
  public abstract float getNext();

  /**
   * Compute an accurate, hopefully exact, mathematical sum of the current
   * <em>iteration</em> terms.
   *
   * @return the sum of the first <em>iteration</em> terms
   */
  public abstract float correctSum();

  public abstract long getIteration();
}