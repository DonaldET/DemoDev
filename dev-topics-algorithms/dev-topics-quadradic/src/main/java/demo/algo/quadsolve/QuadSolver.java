/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.algo.quadsolve;

import java.util.ArrayList;
import java.util.List;

/**
 * Solve classical quadradic equations, direct and recursive
 * 
 * @author Donald Trummell
 */
public class QuadSolver
{

  private final float a;
  private final float b;
  private final float c;
  private final float discriminent;

  public static final int IMPROVE_LIMIT = 100;
  public static final float ACCURACY_FACTOR = (float) 0.0000005;

  private final int limit = IMPROVE_LIMIT;

  public QuadSolver(final float a, final float b, final float c)
  {
    if (a == 0.0)
      throw new IllegalArgumentException("coefficient a zero");

    this.a = a;
    this.b = b;
    this.c = c;

    final float dsc = b * b - (float) (4.0 * (a * c));
    if (dsc < 0.0)
      throw new IllegalArgumentException("negative discriminent");

    this.discriminent = (float) Math.sqrt(dsc);
  }

  public float getA()
  {
    return a;
  }

  public float getB()
  {
    return b;
  }

  public float getC()
  {
    return c;
  }

  /**
   * For y = a * x *x + b * x + C; the standard solution is:
   * 
   * <pre>
   * DESC = Sqrt( b * b - 4 * a * c)
   * root 1 = (-b + DESC) / (2 * a)
   * root 1 = (-b - DESC) / (2 * a)
   * </pre>
   * 
   * @return
   */
  public List<Float> solve()
  {
    final List<Float> result = new ArrayList<Float>();

    final float twoA = a + a;
    result.add((-b - discriminent) / twoA);
    result.add((-b + discriminent) / twoA);

    return result;
  }

  /**
   * Compute a * x * x + b * x + c at x
   * 
   * @param x
   *          the argument
   * 
   * @return the function value
   */
  public float evaluate(final float x)
  {
    return (a * x + b) * x + c;
  }

  /**
   * Improve both roots, one at a time, using an iterative method
   * 
   * @param r1Inp
   *          root 1
   * @param r2Inp
   *          root 2
   * @param tolerance
   *          minimum accuracy
   * 
   * @return
   */
  public List<Float> improve(final float r1Inp, float r2Inp,
      final float tolerance)
  {
    final List<Float> betterRoots = new ArrayList<Float>();
    betterRoots.add(improveOneRoot(r1Inp, tolerance));
    betterRoots.add(improveOneRoot(r2Inp, tolerance));

    return betterRoots;
  }

  //
  // For y = a*x^2 + b*x + c; use y = 0 at a root, and
  //
  // x = -c / (a*x + b)
  //
  private float improveOneRoot(final float rInp, final float tolerance)
  {
    float r = rInp;
    float y = evaluate(rInp);

    int itr = 1;
    float rNew = -c / (a * r + b);
    float epsR = computeEPS(r, rNew);

    float yNew = evaluate(rNew);
    float epsY = computeEPS(y, yNew);

    while (epsR >= tolerance || epsY >= tolerance)
    {
      if (itr >= limit)
        throw new IllegalStateException("no convergence after " + itr
            + " iterations, EPS: " + epsR);

      r = rNew;
      y = yNew;

      rNew = -c / (a * r + b);
      epsR = computeEPS(r, rNew);

      yNew = evaluate(rNew);
      epsY = computeEPS(y, yNew);

      itr++;
    }

    return rNew;
  }

  // ---------------------------------------------------------------------------

  public static float computeEPS(final float trueValue,
      final float observedValue)
  {
    return (trueValue == 0.0) ? Math.abs(trueValue - observedValue) : Math
        .abs(trueValue - observedValue) / Math.abs(trueValue);
  }

  // -------------------------------------------------------------------------

  @Override
  public String toString()
  {
    return "[" + getClass().getSimpleName() + " - 0x"
        + Integer.toHexString(hashCode()) + ";  a: " + a + ",  b: " + b
        + ",  c: " + c + ",  discriminent: " + discriminent + ",  limit: "
        + limit + "]";
  }
}
