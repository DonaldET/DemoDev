/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.dupcheck.domain;

import java.util.List;

/**
 * A <code>DupDataReader</code> implementation reads data and creates a row
 * representation (<code>DataRow</code>) with column data values and the row
 * index where obtained
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface DupDataReader
{
  /**
   * Load data from the source file according to the file format known to the
   * implementation and create a collection of <code>DataRow</code> instances
   *
   * @return <code>true</code> if load was successful
   */
  public abstract boolean loadData();

  public abstract String getSource();

  public abstract void setSource(final String source);

  public abstract long getLinesRead();

  public abstract List<DataRow> getData();

  public abstract boolean isList();

  public abstract void setList(final boolean list);

}