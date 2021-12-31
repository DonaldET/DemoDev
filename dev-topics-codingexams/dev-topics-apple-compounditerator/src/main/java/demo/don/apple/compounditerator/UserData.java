/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.don.apple.compounditerator;

import java.util.Iterator;
import java.util.List;

/**
 * A <code>UserData</code> instance creates iterators from a list of data values
 * and an associated name
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 * @param <T>
 *          type of data managed as a list associated with an iterator
 */
public interface UserData<T>
{
  public abstract Iterator<T>[] createIterators(final List<List<T>> values);

  public abstract void initialize();

  public abstract String getName();

  public abstract void setName(final String name);

  public abstract CompoundIterator<T> getIterator();

  public abstract void setIterator(final CompoundIterator<T> iterator);

  public abstract List<List<T>> getData();

  public abstract void setData(final List<List<T>> data);

  public abstract boolean isInitialized();
}