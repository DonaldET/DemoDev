/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.gesail.twowriters.impl;

import demo.gesail.twowriters.Conductor;

/**
 * A <code>Conductor</code> using an array based counter for incrementing, and
 * providing a main method so it can be run as a Java application
 * 
 * @author Donald Trummell
 */
public class ArrayConductor extends AbstractConductor<long[]> implements
    Conductor<long[]>
{
  public static void main(final String[] args)
  {
    new ArrayConductor().runme(true);
  }

  public ArrayConductor()
  {
    super(new long[1]);
  }

  @Override
  protected void setupAdders(final boolean display)
  {
    t1Addr = new ArrayAddOne(counter, display);
    t2Addr = new ArrayAddOne(counter, display);
  }
}