/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.apple.compounditerator.impl;

import java.util.ArrayList;
import java.util.List;

import demo.apple.compounditerator.CompoundIterator;
import demo.apple.compounditerator.FilteredUserData;
import demo.apple.compounditerator.UserData;

/**
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 * @param <T>
 *          type of data in lists and iterators
 */
public class FilteredUserDataImpl<T> implements FilteredUserData<T>
{
  private UserData<T> userData = null;

  public FilteredUserDataImpl()
  {

  }

  /**
   * @see demo.apple.compounditerator.FilteredUserData#getName()
   */
  @Override
  public String getName()
  {
    if (userData == null)
      throw new IllegalStateException("userData not initialized");

    return userData.getName();
  }

  /**
   * @see demo.apple.compounditerator.FilteredUserData#getValues(int)
   */
  @Override
  public List<T> getValues(final int maxIndex)
  {
    if (userData == null)
      throw new IllegalStateException("userData not initialized");

    if (maxIndex < 0)
      throw new IllegalArgumentException("maxIndex negative, " + maxIndex);

    final CompoundIterator<T> iterator = userData.getIterator();
    final int n = iterator.getIteratorCount();
    if (maxIndex > n)
      throw new IllegalArgumentException("maxIndex " + maxIndex + " exceeds "
          + n);

    final List<T> visibleData = new ArrayList<T>();
    while (iterator.hasNext())
    {
      visibleData.add(iterator.next());
      if (iterator.getCurrentIteratorIndex() > maxIndex)
        break;
    }

    return visibleData;
  }

  public UserData<T> getUserData()
  {
    return userData;
  }

  public void setUserData(UserData<T> userData)
  {
    this.userData = userData;
  }
}
