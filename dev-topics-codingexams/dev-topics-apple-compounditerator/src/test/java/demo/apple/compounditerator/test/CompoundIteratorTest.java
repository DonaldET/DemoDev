/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.apple.compounditerator.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.apple.compounditerator.CompoundIterator;

/**
 * Test the compound iterator against different types of user data
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class CompoundIteratorTest extends TestCase
{
  private static final String TEST_ITERATOR_NAME = "iterator1";

  private static final String TEST_CONTEXT_NAME = "test1Context.xml";

  private static final boolean DEBUG_TEST = false;

  private static Random rnd = new Random(System.currentTimeMillis());

  public static final int[][] SIMPLE_TEST_DATA = { { 0, 1, 2, 3, 4 },
      { 5, 6, 7 }, { 8 }, { 9, 10, 11, 12 } };

  public static final int[][] SPARSE_TEST_DATA = { { 0, 1, 2, 3, 4 }, {},
      { 5, 6, 7 }, { 8 }, {}, {}, { 9 }, {}, { 10, 11 }, {}, { 12 } };

  public static final int[][] MULTIPLE_EMPTY_TEST_DATA = { {}, {}, {}, {}, {},
      {} };

  public static final int NUM_TEST_LOOPS = DEBUG_TEST ? 50 : 100;
  public static final int MAX_ITERATORS_FOR_RANDOM_TEST = 100;
  public static final int MAX_ELEMENTS_PER_ITERATOR = 5;

  public static final String EOL = System.getProperty("line.separator");

  private static final boolean[] contextloaded = new boolean[1];
  private static ApplicationContext context = null;

  static
  {
    synchronized (contextloaded)
    {
      if (!contextloaded[0])
      {
        context = new ClassPathXmlApplicationContext(TEST_CONTEXT_NAME);
        contextloaded[0] = true;
      }
    }
  }

  public void testSimpleData() throws Throwable
  {
    try
    {
      int[][] testData = SIMPLE_TEST_DATA;
      doSequentialIntegerTest(testData);
    }
    catch (Throwable e)
    {
      System.err.println("Throwable caught within test");
      e.printStackTrace();
      throw e;
    }
  }

  public void testSparseData() throws Throwable
  {
    try
    {
      int[][] testData = SPARSE_TEST_DATA;
      doSequentialIntegerTest(testData);
    }
    catch (Throwable th)
    {
      System.err.println("Throwable caught within test");
      th.printStackTrace();
      throw th;
    }
  }

  public void testEmptyData() throws Throwable
  {
    try
    {
      int[][] testData = MULTIPLE_EMPTY_TEST_DATA;
      doSequentialIntegerTest(testData);
    }
    catch (Throwable th)
    {
      System.err.println("Throwable caught within test");
      th.printStackTrace();
      throw th;
    }

  }

  public void testRandomNonSparseData() throws Throwable
  {
    try
    {
      for (int i = 0; i < NUM_TEST_LOOPS; i++)
      {
        int[][] testData = constructRandomTestMatrix(
            MAX_ITERATORS_FOR_RANDOM_TEST, MAX_ELEMENTS_PER_ITERATOR, false,
            false);
        doSequentialIntegerTest(testData);
      }
    }
    catch (Throwable th)
    {
      System.err.println("Throwable caught within test");
      th.printStackTrace();
      throw th;
    }
  }

  public void testRandomSparseData() throws Throwable
  {
    try
    {
      for (int i = 0; i < NUM_TEST_LOOPS; i++)
      {
        int[][] testData = constructRandomTestMatrix(
            MAX_ITERATORS_FOR_RANDOM_TEST, MAX_ELEMENTS_PER_ITERATOR, true,
            false);
        doSequentialIntegerTest(testData);
      }
    }
    catch (Throwable th)
    {
      System.err.println("Throwable caught within test");
      th.printStackTrace();
      throw th;
    }
  }

  public void testRandomSparseDataWithNulls() throws Throwable
  {
    try
    {
      for (int i = 0; i < NUM_TEST_LOOPS; i++)
      {
        int[][] testData = constructRandomTestMatrix(
            MAX_ITERATORS_FOR_RANDOM_TEST, MAX_ELEMENTS_PER_ITERATOR, true,
            true);
        doSequentialIntegerTest(testData);
      }
    }
    catch (Throwable th)
    {
      System.err.println("Throwable caught within test");
      th.printStackTrace();
      throw th;
    }
  }

  private void doSequentialIntegerTest(final int[][] testData)
  {
    // Test the mode of use where you call hasNext()
    doSequentialIntegerTest(testData, true);

    // Test the mode of use where you call next() n times without ever calling
    // hasNext()
    doSequentialIntegerTest(testData, false);
  }

  private void doSequentialIntegerTest(final int[][] testData,
      final boolean shouldCallHasNext)
  {
    if (testData == null)
      throw new IllegalArgumentException("testData null");

    final int n = testData.length;
    final Iterator<Integer>[] iterators = makeIteratorArray(n);
    int numInts = 0;
    for (int i = 0; i < n; i++)
    {
      if (testData[i] != null)
      {
        List<Integer> intList = intArrayToList(testData[i]);
        iterators[i] = intList.iterator();
        numInts += intList.size();
      }
    }

    if (DEBUG_TEST)
      System.out.println(intMatrixToString(testData));

    @SuppressWarnings("unchecked")
    final CompoundIterator<Integer> iter = (CompoundIterator<Integer>) context
        .getBean(TEST_ITERATOR_NAME);
    iter.setListOfIterators(iterators);
    int count = 0;
    while (shouldCallHasNext ? iter.hasNext() : (count < numInts))
    {
      final Integer integerFromIterator = iter.next();
      assertEquals("Unexpected value returned from CompoundIterator; "
          + "test data was: " + intMatrixToString(testData) + ". ",
          new Integer(count++), integerFromIterator);
    }
    assertEquals("Expected number of elements in compound iterator "
        + "to be the sum of the number of elements "
        + "in the individual iterators", numInts, count);

    try
    {
      iter.next();
      fail("Expected next() to throw NoSuchElementException after hasNext() is false.");
    }
    catch (NoSuchElementException ignore)
    {
      // Ignore expected exception
    }
    catch (Throwable th)
    {
      assertTrue("Expected next() to throw NoSuchElementException, not: "
          + th.getClass().getName(), th instanceof NoSuchElementException);
    }
  }

  private Iterator<Integer>[] makeIteratorArray(final int n)
  {
    if (n < 0)
      throw new IllegalArgumentException("n negative");

    @SuppressWarnings("unchecked")
    Iterator<Integer>[] itr = new Iterator[n];

    return itr;
  }

  private List<Integer> intArrayToList(final int[] arr)
  {
    if (arr == null)
      throw new IllegalArgumentException("arr null");

    final List<Integer> intList = new ArrayList<Integer>();
    for (int i = 0; i < arr.length; i++)
      intList.add(arr[i]);

    return intList;
  }

  private int[][] constructRandomTestMatrix(final int maxIterators,
      final int maxElementsPerIterator, final boolean isSparse,
      final boolean canContainNullArrays)
  {
    final int numIterators = randomIntBetween(1, maxIterators + 1);
    final int[][] target = new int[numIterators][];

    int count = 0;
    for (int i = 0; i < target.length; i++)
    {
      int minElementsPerIterator = 1;
      if (isSparse)
      {
        if (canContainNullArrays)
        {
          minElementsPerIterator = -1;
        }
        else
        {
          minElementsPerIterator = 0;
        }
      }
      final int numElements = randomIntBetween(minElementsPerIterator,
          maxElementsPerIterator + 1);
      int[] subArray = null;
      if (numElements >= 0)
      {
        subArray = new int[numElements];
        for (int j = 0; j < subArray.length; j++)
        {
          subArray[j] = count++;
        }
      }
      if (subArray == null)
      {
        if (DEBUG_TEST)
          System.out.println("subArray is null.");
      }
      target[i] = subArray;
    }

    if (DEBUG_TEST)
    {
      System.out.println(intMatrixToString(target));
    }

    return target;
  }

  private int randomIntBetween(final int inclusiveLowerBound,
      final int exclusiveUpperBound)
  {
    if (inclusiveLowerBound >= exclusiveUpperBound)
    {
      throw new IllegalArgumentException("Bad range for randomIntBetween();"
          + " expected inclusiveLowerBound of " + inclusiveLowerBound
          + " to be stricly less than exclusiveUpperBound of "
          + exclusiveUpperBound);
    }
    int range = exclusiveUpperBound - inclusiveLowerBound;
    return inclusiveLowerBound + rnd.nextInt(range);
  }

  private String intMatrixToString(final int[][] matrix)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("{").append(EOL);
    for (int i = 0; i < matrix.length; i++)
    {
      buf.append("    ");
      int[] ints = matrix[i];
      if (ints == null)
      {
        buf.append("null");
      }
      else
      {
        buf.append("{ ");
        for (int j = 0; j < ints.length; j++)
        {
          int anInt = ints[j];
          buf.append(anInt);
          if (j < ints.length - 1)
          {
            buf.append(",");
          }
          buf.append(" ");
        }
        buf.append("}");
      }
      if (i < matrix.length - 1)
      {
        buf.append(",");
      }
      buf.append(EOL);
    }
    buf.append("}");

    return buf.toString();
  }
}
