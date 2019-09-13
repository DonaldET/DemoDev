/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.scanner.api.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Utility methods for testing
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class VerificationUtils
{
  /**
   * Prevent construction
   */
  private VerificationUtils()
  {
  }

  /**
   * Useful to locate a file on the class path and peek at the file; helps with
   * Spring resources.
   * <p>
   * Create the desired file with contents describing its location, and place in
   * the candidate class path locations. This method lists the contents,
   * allowing you to determine the location found on the class path
   *
   * @param resourceLocator
   *          the class path reference
   * @param maxBufSize
   *          the largest part of the file listed
   */
  public static void listResource(final String resourceLocator,
      final int maxBufSize)
  {
    if (resourceLocator == null || resourceLocator.isEmpty())
      throw new IllegalArgumentException("resourceLocator null or empty");

    if (maxBufSize < 1)
      throw new IllegalArgumentException("buffer allocation size small, "
          + maxBufSize);

    System.err.println("Locating " + resourceLocator);
    try (InputStream ctx = VerificationUtils.class.getClassLoader().getResourceAsStream(
            resourceLocator);)
    {
      if (ctx == null)
        System.err.println("     Resource not found at " + resourceLocator);
      else
      {
        System.err.println("     Resource FOUND at " + resourceLocator);
        byte[] buf = new byte[maxBufSize];
        int lth = -1;
        try
        {
          lth = ctx.read(buf);
        }
        catch (IOException ex)
        {
          ex.printStackTrace();
        }

        final String s = new String(Arrays.copyOfRange(buf, 0, lth));
        System.err.println("Saw:\n" + s + "\n---");
      }
    }
    catch (IOException ioEx)
    {
    	ioEx.printStackTrace();
	  }
  }
}
