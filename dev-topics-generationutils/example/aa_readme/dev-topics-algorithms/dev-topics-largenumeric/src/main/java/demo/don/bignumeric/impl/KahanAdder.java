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

import demo.don.bignumeric.api.Adder;
import demo.don.bignumeric.api.SequenceGenerator;

/**
 * The Kahan Summation algorithm is an example of an improved summation
 * strategy. On reference for this more accurate approach is the <a
 * href='http://en.wikipedia.org/wiki/Kahan_summation_algorithm'>Kahan</a>
 * algorithm.
 * <p>
 * A psuedo-code example is:
 * 
 * <pre>
 * <code>
 * function KahanSum(input)
 *    var sum = 0.0
 *    var c = 0.0                  // A running compensation for lost low-order bits.
 *    for i = 1 to input.length do
 *        var y = input[i] - c     // So far, so good: c is zero.
 *        var t = sum + y          // Alas, sum is big, y small, so low-order digits of y are lost.
 *        c = (t - sum) - y        // (t - sum) recovers the high-order part of y; subtracting y recovers - (low part of y)
 *        sum = t                 // Algebraically, c should always be zero. Beware overly-aggressive optimizing compilers!
 *        // Next time around, the lost low part will be added to y in a fresh attempt.
 *    return sum
 * </code>
 * </pre>
 * 
 * @author Donald Trummell
 */
public class KahanAdder extends AbstractAdder implements Adder
{
  private float c = (float) 0.0; // A running compensation for lost low-order
                                 // bits.

  public KahanAdder(final SequenceGenerator generator, final long maxIterations)
  {
    super(generator, maxIterations);
    this.name = getClass().getSimpleName();
  }

  @Override
  protected float updateSum(final float sum, final float element)
  {
    float y = element - c; // So far, so good: c is zero.
    float t = sum + y; // Alas, sum is big, y small, so low-order digits of y
                       // are lost.
    float hiOrd = (t - sum); // (t - sum) recovers the high-order part of y.
    c = hiOrd - y; // subtracting y recovers - (low part of y)
    float addjustedSum = t; // Algebraically, c should always be zero. Beware
                            // overly-aggressive optimizing compilers!
                            // Next time around, the lost low part will
                            // be added to y in a fresh attempt.

    return addjustedSum;
  }
}