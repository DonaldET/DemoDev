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

/**
 * A <code>CompoundIterator</code> provides multiple concurrent access paths to
 * user data
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 *
 * @param <E>
 *          Type iterator delivers
 */
public interface CompoundIterator<E> extends Iterator<E>
{
  public abstract void setListOfIterators(final Iterator<E>[] iterators);

  public abstract int getIteratorCount();

  public abstract int getCurrentIteratorIndex();

  public abstract Iterator<E> getCurrentIterator();
}