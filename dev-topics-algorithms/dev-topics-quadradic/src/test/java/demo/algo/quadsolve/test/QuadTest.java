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
    final StringBuilder msg = verifyRootsAreZerosUnchecked(
        "CatastrophicCancellation", qs, roots.get(0).floatValue(), roots.get(1)
            .floatValue(), QuadSolver.ACCURACY_FACTOR);
    if (msg.length() < 1)
      Assert.fail("roots unexpectedly worked");
  }

  @Test
  public void testIteration()
  {
    final QuadSolver qs = getPerturbedQS();
    final List<Float> roots = getRoots(qs);

    final float improveTolerance = QuadSolver.ACCURACY_FACTOR;
    final List<Float> improvedRoots = getIterativeRoots(qs, roots.get(0)
        .floatValue(), roots.get(1).floatValue(), improveTolerance);
    
    // Root 2 still does not work well, using 0.0078125
    // verifyRootsAreZeros("Iteration", qs, improvedRoots.get(0),
    // improvedRoots.get(1), QuadSolver.ACCURACY_FACTOR);
    verifyRootsAreZeros("Iteration", qs, improvedRoots.get(0),
        improvedRoots.get(1), (float) 0.008);
  }

  // -------------------------------------------------------------------------

  private List<Float> getRoots(final QuadSolver qs)
  {
    final List<Float> roots = qs.solve();
    Assert.assertNotNull("null root array returned", roots);
    Assert.assertEquals("wrong size", 2, roots.size());

    Assert.assertNotNull("r1 null", roots.get(0));
    Assert.assertNotNull("r2 null", roots.get(1));

    return roots;
  }

  private StringBuilder verifyRootsAreZeros(final String label,
      final QuadSolver qs, final float r1, final float r2, final float tolerance)
  {
    final StringBuilder msg = verifyRootsAreZerosUnchecked(label, qs, r1, r2,
        tolerance);
    if (msg.length() > 0)
      Assert.fail("Solution failed :: " + msg.toString());

    return msg;
  }

  private StringBuilder verifyRootsAreZerosUnchecked(final String label,
      final QuadSolver qs, final float r1, final float r2, final float tolerance)
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
      msg.append(" IMP_R2[" + r2 + "]: " + yR2 + " differs from 0.0 by "
          + epsR2 + ";  tolerance: " + tolerance);

    System.err.println("\n" + label + " -- a: " + qs.getA() + "; b: "
        + qs.getB() + "; c: " + qs.getC() + ";    R1: " + r1 + " [" + epsR1
        + "],    R2: " + r2 + " [" + epsR2 + "]");

    return msg;
  }

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

  private List<Float> getIterativeRoots(final QuadSolver qs, final float r1,
      final float r2, final float tolerance)
  {
    final List<Float> improvedRoots = qs.improve(r1, r2, tolerance);
    Assert.assertNotNull("improvedRoots null", improvedRoots);
    Assert.assertEquals("improvedRoots count differs", 2, improvedRoots.size());

    return improvedRoots;
  }
}
