/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.don.apple.compounditerator;

import java.util.List;

/**
 * A <code>FilteredUserData</code> instance provides access to a named
 * collection of values, presented as a <code>List</code>
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 * @param <T>
 *          type of data stored in the named list
 */
public interface FilteredUserData<T>
{
  public abstract String getName();

  public abstract List<T> getValues(final int maxIndex);
}