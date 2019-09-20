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
import java.util.Collections;
import java.util.List;

/**
 * Solve classical quadradic equations, using both direct and recursive approaches.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
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
   * root 2 = (-b - DESC) / (2 * a)
   * </pre>
   *
   * Roots returned are first sorted low to high.
   *
   * @return
   */
  public List<Float> solve()
  {
    final List<Float> result = new ArrayList<Float>();

    final float twoA = a + a;
    result.add((-b - discriminent) / twoA);
    result.add((-b + discriminent) / twoA);

    Collections.sort(result);

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

    final float r1 = improveOneRoot(r1Inp, tolerance);
    float r2 = improveOneRoot(r2Inp, tolerance);
    if (computeEPS(r1, r2) < tolerance && r1 != 0.0)
    {
      float r2Est = (c / a) / r1;
      float yR2 = evaluate(r2Est);
      if (computeEPS((float) 0.0, yR2) < tolerance)
        r2 = r2Est;
      else
      {
        final float bound = (float) 1.0;
        r2 = bisectionImprove(r2Est, tolerance / (float) 16.0, r2Inp - bound,
            r2Inp + bound);
      }
    }

    betterRoots.add(r1);
    betterRoots.add(r2);
    Collections.sort(betterRoots);

    return betterRoots;
  }

  private float bisectionImprove(final float rInp, final float tolerance,
      final float lowBound, final float highBound)
  {
    float lft = Math.min(lowBound, highBound);
    float rgt = Math.max(lowBound, highBound);
    float mid = rInp;
    float eps = rgt - lft;

    final int biLimit = limit;
    int itr = 1;
    int toLowerHalf = 0;
    int toUpperHalf = 0;

    while (eps >= tolerance)
    {
      if (itr >= biLimit)
      {
        System.err.println("\nWARNING: no bisection convergence after "
            + biLimit + " iterations");
        System.err.println("Input interval : [" + lowBound + ", " + highBound
            + "]");
        System.err.println("Output interval: [" + lft + ", " + rgt + "]");
        System.err.println("To Upper: " + toUpperHalf + ";  To Lower: "
            + toLowerHalf);

        return rInp;
      }

      mid = (lft + rgt) / (float) 2.0;
      float midY = evaluate(mid);
      float rgtY = evaluate(rgt);

      if (midY * rgtY >= 0.0)
      {
        rgt = mid;
        toUpperHalf++;
      }
      else
      {
        lft = mid;
        toLowerHalf++;
      }

      eps = rgt - lft;

      itr++;
    }

    mid = (lft + rgt) / (float) 2.0;

    // System.err
    // .println("\nBinary Est -- Input: " + rInp + ";  ITR: " + itr
    // + ";  eps: " + eps + " or [" + lft + ", : " + rgt + "];  value: "
    // + mid);

    return mid;
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
            + " iterations, EPS R: " + epsR);

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
    final float delta = Math.abs(trueValue - observedValue);
    return (trueValue == 0.0) ? delta : (delta / Math.abs(trueValue));
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
