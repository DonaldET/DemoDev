/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.apple.compounditerator.impl;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import demo.apple.compounditerator.CompoundIterator;

/**
 * A <code>CompoundIterator</code> implementation
 * 
 * @author Donald Trummell
 *
 * @param <E>
 */
public class CompoundIteratorImpl<E> implements CompoundIterator<E>
{
  private int currentIteratorIndex = -1;
  private Iterator<E> currentIterator = null;
  private boolean traceEnabled = false;
  private PrintStream traceOut = System.out;

  private List<Iterator<E>> listOfIterators = new ArrayList<Iterator<E>>();

  /**
   * Create an uninitialized iterator
   */
  public CompoundIteratorImpl()
  {
  }

  /**
   * Add iterators to internal list of iterators
   * 
   * @param iterators
   *          the iterators to add to internal list
   * 
   * @return true if any added
   */
  private boolean copyIterator(final Iterator<E>[] iterators)
  {
    if (iterators == null)
      throw new IllegalArgumentException("iterators null");

    boolean hasValidIterators = false;
    for (Iterator<E> iter : iterators)
    {
      if ((iter != null) && (iter.hasNext()))
      {
        listOfIterators.add(iter);
        hasValidIterators = true;
      }
      else
      {
        if (traceEnabled)
        {
          traceOut.println("Null or empty Iterator : " + iter);
        }
      }
    }

    if (traceEnabled)
    {
      traceOut.println("Number of Iterators added is : "
          + listOfIterators.size());
    }

    return hasValidIterators;
  }

  /**
   * Make the next iterator in the internal iterator list the current iterator
   * 
   * @return true if there is a next iterator
   */
  private boolean moveToNextIterator()
  {
    if (currentIteratorIndex >= (listOfIterators.size() - 1))
    {
      if (traceEnabled)
      {
        traceOut.println("No next. currentIteratorIndex = "
            + currentIteratorIndex);
      }
      return false;
    }

    currentIteratorIndex++;

    if (traceEnabled)
    {
      traceOut.println("Moving to next iterator, Index = "
          + currentIteratorIndex + " of " + listOfIterators.size()
          + " iterators");
    }

    currentIterator = listOfIterators.get(currentIteratorIndex);
    if (traceEnabled)
    {
      traceOut.println("Moved to next iterator. currentIteratorIndex = "
          + currentIteratorIndex);
    }

    return true;
  }

  /**
   * @see demo.apple.compounditerator.CompoundIterator#hasNext()
   */
  @Override
  public boolean hasNext()
  {
    if (currentIterator == null)
      return false;

    if (currentIterator.hasNext())
      return true;

    if (moveToNextIterator())
    {
      boolean hasNext = currentIterator.hasNext();

      if (traceEnabled)
      {
        traceOut.println("hasNext = " + hasNext);
      }
      return hasNext;
    }
    return false;
  }

  /**
   * @see demo.apple.compounditerator.CompoundIterator#next()
   */
  @Override
  public E next()
  {
    if (hasNext())
    {
      final E nextValue = currentIterator.next();
      if (traceEnabled)
        traceOut.println("Returning next item {" + nextValue + "}");

      return nextValue;
    }

    if (traceEnabled)
      traceOut.println("All iterators exhausted.");

    throw new NoSuchElementException(
        "No more items left. Call hasNext and check before calling next.");
  }

  /**
   * @see demo.apple.compounditerator.CompoundIterator#remove()
   */
  @Override
  public void remove() throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException(getClass().getName()
        + " does not support the remove() operation.");
  }

  // ---------------------------------------------------------------------------

  @Override
  public void setListOfIterators(final Iterator<E>[] iterators)
  {
    if (iterators == null)
      throw new IllegalArgumentException("Iterator array is null.");

    if (iterators.length == 0)
      throw new IllegalArgumentException("Iterator array is empty.");

    currentIteratorIndex = -1;
    currentIterator = null;
    listOfIterators.clear();

    if (copyIterator(iterators))
    {
      currentIteratorIndex = 0;
      currentIterator = listOfIterators.get(currentIteratorIndex);
    }
  }

  /**
   * @see demo.apple.compounditerator.CompoundIterator#getCurrentIteratorIndex()
   */
  @Override
  public int getIteratorCount()
  {
    return listOfIterators.size();
  }

  /**
   * @see demo.apple.compounditerator.CompoundIterator#getCurrentIteratorIndex()
   */
  @Override
  public int getCurrentIteratorIndex()
  {
    return currentIteratorIndex;
  }

  /**
   * @see demo.apple.compounditerator.CompoundIterator#getCurrentIterator()
   */
  @Override
  public Iterator<E> getCurrentIterator()
  {
    return currentIterator;
  }

  public boolean isTraceEnabled()
  {
    return traceEnabled;
  }

  public void setTraceEnabled(final boolean traceEnabled)
  {
    this.traceEnabled = traceEnabled;
  }

  public PrintStream getTraceOut()
  {
    return traceOut;
  }

  public void setTraceOut(final PrintStream traceOut)
  {
    this.traceOut = traceOut;
  }
}
