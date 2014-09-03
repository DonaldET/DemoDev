/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.dupcheck.impl;

import java.util.List;

import demo.dupcheck.domain.DataRow;

/**
 * A bean implementing <code>DataRow</code> to hold the data from a row as
 * strings
 * 
 * @author Donald Trummell
 */
public class DataRowBean implements DataRow
{
  private final long rowIndex;
  private final List<String> data;

  public DataRowBean(final long rowIndex, final List<String> data)
  {
    super();
    this.rowIndex = rowIndex;
    this.data = data;
  }

  @Override
  public long getRowIndex()
  {
    return rowIndex;
  }

  @Override
  public List<String> getData()
  {
    return data;
  }
}
