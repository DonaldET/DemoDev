/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.apple.compounditerator.impl;

import java.util.Iterator;
import java.util.List;

import demo.apple.compounditerator.CompoundIterator;
import demo.apple.compounditerator.UserData;

/**
 * 
 * @author Donald Trummell
 *
 * @param <T>
 *          type of data in lists and iterators
 */
public class UserDataImpl<T> implements UserData<T>
{
  private String name = null;

  private CompoundIterator<T> iterator = null;

  private List<List<T>> data = null;

  private boolean initialized = false;

  /**
   * Construct an incomplete instance
   */
  public UserDataImpl()
  {
  }

  /**
   * @see demo.apple.compounditerator.UserData#initialize()
   */
  @Override
  public void initialize()
  {
    if (initialized)
      throw new IllegalStateException("already initialized");

    if (name == null || name.isEmpty())
      throw new IllegalStateException("null or empty name");

    if (data == null)
      throw new IllegalStateException("no user data");

    if (iterator == null)
      throw new IllegalStateException("no iterator assigned");

    iterator.setListOfIterators(createIterators(data));

    data = null;

    initialized = true;
  }

  public Iterator<T>[] createIterators(final List<List<T>> values)
  {
    if (values == null)
      throw new IllegalArgumentException("null input data");

    final int n = values.size();
    final Iterator<T>[] iterators = createNullIteratorArray(n);

    if (n > 0)
    {
      int i = 0;
      for (List<T> aList : values)
        iterators[i++] = (aList == null) ? null : aList.iterator();
    }

    return iterators;
  }

  /**
   * @see demo.apple.compounditerator.UserData#getName()
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * @see demo.apple.compounditerator.UserData#setName(java.lang.String)
   */
  @Override
  public void setName(final String name)
  {
    if (name == null || name.isEmpty())
      throw new IllegalArgumentException("injecting null or empty name");

    this.name = name;
  }

  /**
   * @see demo.apple.compounditerator.UserData#getIterator()
   */
  @Override
  public CompoundIterator<T> getIterator()
  {
    return iterator;
  }

  /**
   * @see demo.apple.compounditerator.UserData#setIterator(demo.apple.compounditerator.CompoundIterator)
   */
  @Override
  public void setIterator(final CompoundIterator<T> iterator)
  {
    if (iterator == null)
      throw new IllegalArgumentException("injecting null iterator");

    this.iterator = iterator;
  }

  /**
   * @see demo.apple.compounditerator.UserData#getData()
   */
  @Override
  public List<List<T>> getData()
  {
    return data;
  }

  /**
   * @see demo.apple.compounditerator.UserData#setData(java.util.List)
   */
  @Override
  public void setData(final List<List<T>> data)
  {
    if (data == null)
      throw new IllegalArgumentException("injecting null data");

    this.data = data;
  }

  /**
   * @see demo.apple.compounditerator.UserData#isInitialized()
   */
  @Override
  public boolean isInitialized()
  {
    return initialized;
  }

  private final Iterator<T>[] createNullIteratorArray(final int n)
  {
    @SuppressWarnings("unchecked")
    Iterator<T>[] itr = new Iterator[n];

    return itr;
  }
}
