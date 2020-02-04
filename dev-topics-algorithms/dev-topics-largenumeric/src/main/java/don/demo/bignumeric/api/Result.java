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
 * Hold the status of the an adder processing a sequence from a generator.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class Result
{
  private boolean completedIterations;
  private boolean newMatchedPrevious;
  private long finalIteration;

  /**
   * Create an immutable instance.
   *
   * @param completedIterations
   *          true if completed
   * @param newMatchedPrevious
   *          true is last sum matches current
   * @param finalIteration
   *          count reached when done
   */
  public Result(boolean completedIterations, boolean newMatchedPrevious,
      long finalIteration)
  {
    super();
    this.completedIterations = completedIterations;
    this.newMatchedPrevious = newMatchedPrevious;
    this.finalIteration = finalIteration;
  }

  public boolean isCompletedIterations()
  {
    return completedIterations;
  }

  public boolean isNewMatchedPrevious()
  {
    return newMatchedPrevious;
  }

  public long getFinalIteration()
  {
    return finalIteration;
  }

  @Override
  public String toString()
  {
    return "[Result - 0x" + Integer.toHexString(hashCode())
        + ";  completedIterations: " + completedIterations
        + ";  newMatchedPrevious: " + newMatchedPrevious
        + ";  finalIteration: " + finalIteration + "]";
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (completedIterations ? 1231 : 1237);
    result = prime * result + (int) (finalIteration ^ (finalIteration >>> 32));
    result = prime * result + (newMatchedPrevious ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Result other = (Result) obj;
    if (completedIterations != other.completedIterations)
      return false;
    if (finalIteration != other.finalIteration)
      return false;
    if (newMatchedPrevious != other.newMatchedPrevious)
      return false;
    return true;
  }
}
