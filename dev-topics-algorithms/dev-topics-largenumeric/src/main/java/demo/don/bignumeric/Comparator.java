/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.bignumeric;

import java.util.Arrays;
import java.util.List;

import demo.don.bignumeric.api.Adder;
import demo.don.bignumeric.api.Result;
import demo.don.bignumeric.api.SequenceGenerator;
import demo.don.bignumeric.impl.KahanAdder;
import demo.don.bignumeric.impl.NaiveAdder;
import demo.don.bignumeric.impl.ShuffleSequenceGenerator;

/**
 * Compare two addition algorithms for accuracy
 * 
 * @author Donald Trummell
 */
public class Comparator
{
  private Comparator()
  {
  }

  /**
   * Adds a sequence of bounded, positive, randomly computed floating point
   * values using simple addition and the Kahan summation algorithm; the errors
   * of both approaches are compared.
     * <strong>Note:</strong>
   * <p>
   * This approach to floating point addition is based on work done at the
   * <em>Univesity of California</em>, Berkeley, in the early 70's, and
   * originally coded in <code>C++</code>.
 * <p>
   * 
   * @param args
   *          runtime argument; ignored
   */
  public static void main(final String[] args)
  {
    final List<Integer> testSizes = Arrays.asList(10, 25, 50, 75, 100, 125,
        150, 200, 250, 300, 500, 1000, 1500, 2000, 2500, 3000);

    System.out
        .println("Single Precision Floating Point Addition Algorithm Comparison");
    System.out.format("\n%3s.%1s%4s %12s %12s %12s %8s %8s %8s", "Set", " ",
        "  N", "Naive", "Kahan", "True", "N ERR", "K ERR", "%REL");

    int tnum = 0;
    for (final int size : testSizes)
    {
      tnum++;

      final SequenceGenerator ngenerator = new ShuffleSequenceGenerator();
      final Adder naive = new NaiveAdder(ngenerator, size);
      final Result naiveResult = naive.runSequence();

      final SequenceGenerator kgenerator = new ShuffleSequenceGenerator();
      final Adder kahan = new KahanAdder(kgenerator, size);
      final Result kahanResult = kahan.runSequence();

      System.out.format("\n%3d.%1s%4d", tnum,
          naiveResult.equals(kahanResult) ? " " : "*", size);

      final float nsum = naive.getSum();
      System.out.format(" %12.4f", nsum);
      final float ksum = kahan.getSum();
      System.out.format(" %12.4f", ksum);

      final float correctSum = kgenerator.correctSum();
      if (ngenerator.correctSum() != correctSum)
        throw new IllegalArgumentException("Correct sums don't match");
      System.out.format(" %12.4f", correctSum);

      final float nerror = nsum - correctSum; // o = t + e // e = o - t
      System.out.format(" %8.4f", nerror);
      final float kerror = ksum - correctSum;
      System.out.format(" %8.4f", kerror);

      final float relError = (float) (100.0 * kerror / correctSum);
      System.out.format(" %8.4f%%", relError);
    }
  }
}
