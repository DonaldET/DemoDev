package demo.don.bignumeric.impl;

import demo.don.bignumeric.api.SequenceGenerator;

/**
 * Generate a sequence of ever smaller values, each right-shifted one bit. Let
 * <em>e</em> be the first element, let <em>f</em> be the initial factor, and
 * <em>n</em> be the number of terms to sum.
 * <p>
 * Then the sequence of n generated elements is: <em>e</em>*<em>f</em> +
 * <em>e</em>* <em>f</em>/2 + . . . + <em>e</em>*<em>f</em>/2**(n - 1). The sum
 * is given by the expression: <code>2*<em>e</em>*<em>f</em>(1 - 1/2**n)</code>.
 * 
 * @author Donald Trummell
 */
public class RightShiftSequenceGenerator implements SequenceGenerator
{
  private long iteration = 0;
  private double factorStep = 2.0;
  private double initialFactor = Math.pow(factorStep, 10.0);
  private double factor = initialFactor;
  private double element = 4657.0;

  public RightShiftSequenceGenerator()
  {
  }

  /**
   * Compute the closed form exact mathematical sum of the first
   * <em>iteration</em> terms.
   * 
   * @return the sum of the first n terms
   */
  @Override
  public float correctSum()
  {
    return (float) (2.0 * (double) element * (double) initialFactor * (1.0 - 1.0 / Math
        .pow(2.0, iteration)));
  }

  @Override
  public float getNext()
  {
    iteration++;
    final float value = (float) (element * factor);
    factor /= factorStep;

    return value;
  }

  @Override
  public long getIteration()
  {
    return iteration;
  }

  @Override
  public String toString()
  {
    return "[" + getClass().getSimpleName() + " - 0x"
        + Integer.toHexString(hashCode()) + ";  iteration: " + iteration
        + ";  factor:" + factor + "(step: " + factorStep + ");  element: "
        + element + "]";
  }
}
