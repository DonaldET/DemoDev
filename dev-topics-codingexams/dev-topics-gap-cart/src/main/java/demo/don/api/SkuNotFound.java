/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.api;

/**
 * Application error for failed SKU lookup
 * 
 * @author Donald Trummell
 */
public class SkuNotFound extends RuntimeException
{
  private static final long serialVersionUID = -2400927512056318274L;

  public SkuNotFound()
  {
  }

  public SkuNotFound(final String message)
  {
    super(message);
  }

  public SkuNotFound(final Throwable cause)
  {
    super(cause);
  }

  public SkuNotFound(final String message, final Throwable cause)
  {
    super(message, cause);
  }

  public SkuNotFound(final String message, final Throwable cause,
      final boolean enableSuppression, final boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
