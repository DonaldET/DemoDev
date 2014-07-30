/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.don.factorial.test;

import java.util.Random;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.factorial.MathHelper;
import demo.don.factorial.impl.PreComputedMathHelper;
import demo.don.factorial.impl.SimpleMathHelper;

/**
 * Test timing of the two implementations
 *
 * @author Donald Trummell
 */
public class TestTimming
{
  private static final int TIMMING_TRIALS = 750000;

  private MathHelper mhSimple = null;
  private MathHelper mhPre = null;

  @Before
  public void setUp() throws Exception
  {
    mhSimple = new SimpleMathHelper();
    mhPre = new PreComputedMathHelper();
  }

  @After
  public void tearDown() throws Exception
  {
    mhSimple = null;
    mhPre = null;
  }

  @Test
  public void testTimming()
  {
    //
    // Warmup JIT
    computeRepresentativeSample(mhSimple, 1, MathHelper.MAX_FACTORIAL_ARGUMENT);

    final long elapsedSimple = computeRepresentativeSample(mhSimple,
        TIMMING_TRIALS, MathHelper.MAX_FACTORIAL_ARGUMENT);
    //
    // Warmup JIT
    computeRepresentativeSample(mhPre, 1, MathHelper.MAX_FACTORIAL_ARGUMENT);

    final long elapsedPre = computeRepresentativeSample(mhPre, TIMMING_TRIALS,
        MathHelper.MAX_FACTORIAL_ARGUMENT);

    Assert.assertTrue("Simple faster than pre-compute",
        elapsedPre < elapsedSimple);
  }

  private long computeRepresentativeSample(final MathHelper mh,
      final int nRuns, final int maxArg)
  {
    if (maxArg < 1)
      throw new IllegalArgumentException("maxArg small, " + maxArg);

    if (maxArg > MathHelper.MAX_FACTORIAL_ARGUMENT)
      throw new IllegalArgumentException("maxArg big, " + maxArg);

    final Random r = new Random();

    //
    // Warm-up JIT
    final int tstArg = MathHelper.MAX_FACTORIAL_ARGUMENT;
    final long fact = mh.factorial(tstArg);
    Assert.assertEquals(mh.getName() + " initial factorial failed for "
        + tstArg + ", maximum arg allowed is "
        + MathHelper.MAX_FACTORIAL_ARGUMENT + ";",
        MathHelper.MAX_FACTORIAL_VALUE, fact);

    final int upperBound = maxArg + 1;
    final long start = System.currentTimeMillis();
    for (int i = 0; i < nRuns; i++)
    {
      final int testArg = r.nextInt(upperBound);
      final long f = mh.factorial(testArg);
      Assert.assertTrue(mh.getName() + " factorial small, arg=" + testArg
          + ";  fact=" + f, ((f > 2) ? (f > testArg) : (f >= testArg)));

      // System.err.println(mh.getName() + "  " + i + ".  fact(" + testArg
      // + ")  == " + f);
    }

    long elapsed = System.currentTimeMillis() - start;

    // System.err.println(mh.getName() + " for " + TIMMING_TRIALS
    // + " trials used " + elapsed + " MS");

    return elapsed;
  }
}
