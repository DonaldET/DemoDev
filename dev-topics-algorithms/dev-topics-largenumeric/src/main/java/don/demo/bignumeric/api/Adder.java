/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package don.demo.bignumeric.api;

/**
 * An adder sums sequences of values. We are primarily interested in addition
 * and the influence of long sequences. For background, <a href=
 * 'http://en.wikipedia.org/wiki/Numerical_stability'>Numerical_stability</a>.
 * <p>
 * <strong>Note:</strong>
 * <p>
 * This approach to floating point addition is based on work done at the
 * <em>University of California</em>, Berkeley, in the early 70's, and originally
 * coded in <code>C++</code>.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface Adder
{
  /**
   * Add the sequence provided by the included generator.
   *
   * @return the status of processing the sequence
   */
  public abstract Result runSequence();

  public abstract long getMaxIterations();

  /**
   * The value of the sum over the processed sequence
   *
   * @return the sum of the generated sequence
   */
  public abstract float getSum();

  /**
   * The penultimate sum, generated just prior to the final or last sum, used to
   * determine if the accumulator overflowed.
   *
   * @return the penultimate sum of the generated sequence
   */
  public abstract float getLastSum();

  public abstract String getName();
}