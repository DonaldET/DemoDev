/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.don.factorial;

/**
 * A factorial implementation of <code>MathHelper</code> provides different
 * mechanisms to compute the factorial
 * 
 * @author Donald Trummell
 */
public interface MathHelper
{
  /**
   * Largest computable input with a representable factorial value for a long
   * 
   * Note: maximum factorial(20) == 2432902008176640000
   */
  public static final int MAX_FACTORIAL_ARGUMENT = 20;

  public static final long MAX_FACTORIAL_VALUE = 2432902008176640000L;

  /**
   * @return returns associated name
   */
  public abstract String getName();

  /**
   * Implement factorial on non-negative integer inputs
   * 
   * @param n
   *          the factorial argument on which to compute the factorial
   * 
   * @return the factorial of n
   */
  public abstract long factorial(int n);
}