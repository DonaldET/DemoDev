/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.amazon.games;

/**
 * Customers are unique so ids should be unique as well.
 * 
 * @author Donald Trummell
 */
public class CustomerID extends AbstractID implements Comparable<AbstractID>
{
  public CustomerID(final String id)
  {
    super(id);
  }
}