/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.serialize.hack;

import java.io.Serializable;

public class HackerSample
{
  public static final class Token implements Serializable
  {
    private static final long serialVersionUID = 3250228798267641877L;
    private long key = 0;

    public Token(final long key)
    {
      super();
      setKey(key);
    }

    public long getKey()
    {
      return key;
    }

    private void setKey(final long key)
    {
      this.key = key;
    }

    @Override
    public String toString()
    {
      final StringBuilder msg = new StringBuilder();
      msg.append("[");
      msg.append(getClass().getSimpleName());
      msg.append(" - 0x");
      msg.append(Integer.toHexString(hashCode()));
      msg.append("; key=");
      msg.append(key);
      msg.append(" {0x");
      msg.append(Long.toHexString(key));
      msg.append("}]");

      return msg.toString();
    }
  }

  /**
   * A sample serializable token containing an immutable instance of a known key
   * value
   * 
   * @author Donald Trummell
   */
  public static final class TokenFactory implements Serializable
  {
    private static final long serialVersionUID = 5460702755542613920L;

    private static final int[] primes = new int[] { 6997, 7001, 7013, 7019,
        7027, 7039, 7043, 7057, 7069, 707, 7103, 7109 };

    /**
     * Prevent construction
     */
    private TokenFactory()
    {
      super();
    }

    public static Token createToken(final int seed)
    {
      return new Token(createKey(seed));
    }

    public static long createKey(final int seedIdx)
    {
      if (seedIdx < 0 || seedIdx >= primes.length)
        throw new IllegalArgumentException("initial index bad,  " + seedIdx);

      long key = 1;
      int primeIdx = seedIdx;
      for (int i = 0; i < 3; i++)
      {
        key *= (long) primes[primeIdx];
        primeIdx += 2;
        if (primeIdx >= primes.length)
          primeIdx = primeIdx % primes.length;
      }

      return key;
    }
  }

  /**
   * Prevent Construction
   */
  private HackerSample()
  {
  }
}