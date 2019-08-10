/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.algo.quadsolve.test;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.algo.quadsolve.QuadSolver;

/**
 * Verify solution accuracy
 * 
 * @author Donald Trummell
 */
public class QuadTest
{
  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegDiscriminent()
  {
    final float a = (float) 2.0;
    final float b = (float) 4.0;
    final float c = (float) 2.001;

    final QuadSolver qs = new QuadSolver(a, b, c);
    qs.solve();
    Assert.fail("should have thrown exception");
  }

  @Test
  public void testSimple1And3Roots()
  {
    final float expectedR1 = (float) 3.0;
    final float expectedR2 = (float) 1.0;

    final float a = (float) 1.0;
    final float b = -(expectedR1 + expectedR2);
    final float c = expectedR1 * expectedR2;

    final QuadSolver qs = new QuadSolver(a, b, c);
    final List<Float> roots = getRoots(qs);
    Assert.assertNotNull("null root array returned", roots);
    Assert.assertEquals("wrong size", 2, roots.size());

    final float r1 = roots.get(1).floatValue();
    Assert.assertEquals("first root bad", expectedR1, r1,
        QuadSolver.ACCURACY_FACTOR);
    final float r2 = roots.get(0).floatValue();
    Assert.assertEquals("second root bad", expectedR2, r2,
        QuadSolver.ACCURACY_FACTOR);

    verifyRootsAreZeros("Simple1&3", qs, r1, r2, QuadSolver.ACCURACY_FACTOR);
  }

  @Test
  public void testChallangingRoot()
  {
    final float expectedR1 = (float) -0.25;
    final float expectedR2 = (float) -400000.0;

    final float a = (float) 1.0;
    final float b = -(expectedR1 + expectedR2);
    final float c = expectedR1 * expectedR2;

    final QuadSolver qs = new QuadSolver(a, b, c);
    final List<Float> roots = getRoots(qs);

    final float r1 = roots.get(1).floatValue();
    Assert.assertEquals("first root bad", expectedR1, r1,
        QuadSolver.ACCURACY_FACTOR);
    final float r2 = roots.get(0).floatValue();
    Assert.assertEquals("second root bad", expectedR2, r2,
        QuadSolver.ACCURACY_FACTOR);

    verifyRootsAreZeros("ChallangingRoot", qs, r1, r2,
        QuadSolver.ACCURACY_FACTOR);
  }

  @Test
  public void testCatastrophicCancellation()
  {
    final QuadSolver qs = getPerturbedQS();

    // Verifies roots are inaccurate
    final List<Float> roots = getRoots(qs);
    final StringBuilder msg = verifyRootsAreZerosUnchecked(false,
        "CatastrophicCancellation", qs, roots.get(0).floatValue(), roots.get(1)
            .floatValue(), QuadSolver.ACCURACY_FACTOR);
    if (msg.length() < 1)
      Assert.fail("roots unexpectedly worked");
  }

  @Test
  public void testIteration()
  {
    //
    // Accuracy for iterative solution

    final float accuracyFactor = QuadSolver.ACCURACY_FACTOR;

    final QuadSolver qs = getPerturbedQS();
    final List<Float> roots = getRoots(qs);
    // Both roots bad, differs by 0.0390625
    verifyRootsAreZeros("normal quad root", qs, roots.get(0), roots.get(1),
        (float) 0.04);

    final List<Float> improvedRoots = getIterativeRoots(qs,
        (float) (accuracyFactor / 8.0));

    //
    // Verify improved roots differ from original roots
    checkRootsDiffer(accuracyFactor, roots, improvedRoots);

    // Root 2 still does not work well, using 0.0390625
    // verifyRootsAreZeros("Iteration", qs, improvedRoots.get(0),
    // improvedRoots.get(1), QuadSolver.ACCURACY_FACTOR);
    verifyRootsAreZeros("Iteration", qs, improvedRoots.get(0),
        improvedRoots.get(1), (float) 0.04);
  }

  // -------------------------------------------------------------------------

  /**
   * We expect one or more roots to be different
   * 
   * @param accuracyFactor
   * @param roots
   * @param improvedRoots
   */
  private void checkRootsDiffer(final float accuracyFactor,
      final List<Float> roots, final List<Float> improvedRoots)
  {
    boolean echo = false;
    final StringBuilder msg = new StringBuilder();
    for (int i = 0; i < roots.size(); i++)
    {
      final float root = roots.get(i);
      final float improvedRoot = improvedRoots.get(i);
      final float eps = QuadSolver.computeEPS(root, improvedRoot);

      if (eps > accuracyFactor)
      {
        if (msg.length() > 0)
          msg.append("\n");

        msg.append("Comparison of root[" + i + "] -- eps: ");
        msg.append(eps);
        msg.append(";  rq: ");
        msg.append(root);
        msg.append(";  ri: ");
        msg.append(improvedRoot);

        if (echo)
          System.err.println(msg.toString());
      }
      else
      {
        Assert.assertEquals("perturbed vs iterative root[" + i + "] differs",
            roots.get(i), improvedRoots.get(i), accuracyFactor);
      }
    }
    Assert.assertTrue("no change in roots", msg.length() > 0);
  }

  /**
   * Get equation roots using standard solution
   * 
   * @param qs
   * 
   * @return the roots for the stored equation
   */
  private List<Float> getRoots(final QuadSolver qs)
  {
    final List<Float> roots = qs.solve();
    Assert.assertNotNull("null root array returned", roots);
    Assert.assertEquals("wrong size", 2, roots.size());
    Assert.assertNotNull("r1 null", roots.get(0));
    Assert.assertNotNull("r2 null", roots.get(1));

    Collections.sort(roots);

    return roots;
  }

  /**
   * Calculate iterative roots
   * 
   * @param qs
   * @param tolerance
   * 
   * @return the alternative iterative roots
   */
  private List<Float> getIterativeRoots(final QuadSolver qs,
      final float tolerance)
  {
    final List<Float> roots = getRoots(qs);

    final float r1 = roots.get(0).floatValue();
    final float r2 = roots.get(1).floatValue();

    //
    // Finds differing roots and converges
    final List<Float> iterativeRoots = qs.improve(r1, r2, tolerance);

    //
    // Does not converge
    // final List<Float> iterativeRoots = qs.improve(r2, r1, tolerance);

    Assert.assertNotNull("iterative null", iterativeRoots);
    Assert.assertEquals("iterative root count bad", roots.size(),
        iterativeRoots.size());

    Collections.sort(iterativeRoots);

    return iterativeRoots;
  }

  private StringBuilder verifyRootsAreZeros(final String label,
      final QuadSolver qs, final float r1, final float r2, final float tolerance)
  {
    final StringBuilder msg = verifyRootsAreZerosUnchecked(true, label, qs, r1,
        r2, tolerance);
    if (msg.length() > 0)
      Assert.fail("Solution failed :: " + msg.toString());

    return msg;
  }

  private StringBuilder verifyRootsAreZerosUnchecked(final boolean echo,
      final String label, final QuadSolver qs, final float r1, final float r2,
      final float tolerance)
  {
    StringBuilder msg = new StringBuilder();
    float yR1 = qs.evaluate(r1);
    float epsR1 = QuadSolver.computeEPS((float) 0.0, yR1);
    if (epsR1 > tolerance)
      msg.append(" IMP_R1[" + r1 + "]: " + yR1 + " differs from 0.0 by "
          + epsR1 + ";  tolerance: " + tolerance);
    float yR2 = qs.evaluate(r2);
    float epsR2 = QuadSolver.computeEPS((float) 0.0, yR2);
    if (epsR2 > tolerance)
    {
      msg.append(" IMP_R2[" + r2 + "]: " + yR2 + " differs from 0.0 by "
          + epsR2 + ";  tolerance: " + tolerance);

      if (echo)
        System.err.println("\n" + label + " -- a: " + qs.getA() + "; b: "
            + qs.getB() + "; c: " + qs.getC() + ";    R1: " + r1 + " [" + epsR1
            + "],    R2: " + r2 + " [" + epsR2 + "]");
    }

    return msg;
  }

  /**
   * Generate an equation close to the original, but with different coefficients
   * and therefore different roots
   * 
   * @return the perturbed quad solver with the altered coefficients
   */
  private QuadSolver getPerturbedQS()
  {
    final float accuracyFactor = QuadSolver.ACCURACY_FACTOR;
    final float qR1 = (float) -0.25;
    final float qR2 = (float) -400000.0;
    final float bq = -(qR1 + qR2);
    final float cq = qR1 * qR2;

    final float expectedR1 = (float) (qR1 + Math.signum(cq) * 0.0000001); // Perturbed
    final float expectedR2 = qR2;

    final float a = (float) 1.0;
    final float b = -(expectedR1 + expectedR2);
    Assert.assertEquals("coeff b too different", bq, b, accuracyFactor);
    final float c = expectedR1 * expectedR2;
    float eps = QuadSolver.computeEPS(cq, c);
    if (eps >= QuadSolver.ACCURACY_FACTOR)
      Assert.fail("coeff c too different; expected: " + cq + ",  actual: " + c
          + ", EPS: " + eps);

    final QuadSolver qs = new QuadSolver(a, b, c);
    // verifyRootsAreZeros("PerturbedQS", qs, expectedR1, expectedR1,
    // tolerance);

    return qs;
  }
}
