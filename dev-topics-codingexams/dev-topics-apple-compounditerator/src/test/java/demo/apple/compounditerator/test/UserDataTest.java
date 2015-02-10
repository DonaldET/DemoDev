package demo.apple.compounditerator.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.apple.compounditerator.CompoundIterator;
import demo.apple.compounditerator.UserData;

public class UserDataTest extends TestCase
{
  private static final String USER_DATA_ASSIGNED_NAME = "Don";

  private static final String TEST_UDATA_NAME = "userData1";

  private static final String TEST_CONTEXT_NAME = "test2Context.xml";

  private UserData<Integer> userData = null;

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

  public UserDataTest()
  {
  }

  @Override
  @SuppressWarnings("unchecked")
  @Before
  public void setUp() throws Exception
  {
    userData = (UserData<Integer>) context.getBean(TEST_UDATA_NAME);
  }

  @Override
  @After
  public void tearDown() throws Exception
  {
    userData = null;
  }

  @Test
  public void testCreateIterators()
  {
    final List<Integer> one = new ArrayList<Integer>();
    one.add(1);
    one.add(2);

    final List<Integer> two = new ArrayList<Integer>();
    one.add(3);
    one.add(4);
    one.add(5);

    final List<List<Integer>> both = new ArrayList<List<Integer>>();
    both.add(one);
    both.add(two);

    final int n = both.size();

    final Iterator<Integer>[] iterators = userData.createIterators(both);
    assertNotNull("no iterators for " + TEST_UDATA_NAME, iterators);
    assertEquals("wrong iterator count for " + TEST_UDATA_NAME, n,
        iterators.length);

    for (int i = 0; i < n; i++)
    {
      int k = 0;
      while (iterators[i].hasNext())
      {
        assertEquals("iterator[" + i + "]{" + k + "} incorrect", k + 1,
            iterators[i].next().intValue());
        k++;
      }
    }
  }

  @Test
  public void testInitialize()
  {
    assertTrue(TEST_UDATA_NAME + " not inialized", userData.isInitialized());
  }

  @Test
  public void testGetName()
  {
    assertEquals(TEST_UDATA_NAME + " has invalid name",
        USER_DATA_ASSIGNED_NAME, userData.getName());
  }

  @Test
  public void testGetData()
  {
    assertNull(TEST_UDATA_NAME + " has non-null data", userData.getData());
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testGetIterator()
  {
    final int expectedIteratorFirstIdx = 0;
    final int expectedIteratorCount = 2;
    final String DON_ITR_DATA1 = "raw_primes";
    final String DON_ITR_DATA2 = "raw_fib";
    final int expectedCount = 22;

    final CompoundIterator<Integer> cpIter = userData.getIterator();
    assertNotNull(TEST_UDATA_NAME + " has null iterator", cpIter);

    assertEquals("current iterator for " + TEST_UDATA_NAME
        + " has incorrect index", expectedIteratorFirstIdx,
        cpIter.getCurrentIteratorIndex());

    assertEquals("iterator for " + TEST_UDATA_NAME + " has wrong count",
        expectedIteratorCount, cpIter.getIteratorCount());

    assertNotNull(TEST_UDATA_NAME + " has null current currentIterator",
        cpIter.getCurrentIterator());

    final List<List<Integer>> expectedIt = new ArrayList<List<Integer>>();
    expectedIt.add((List<Integer>) context.getBean(DON_ITR_DATA1));
    expectedIt.add((List<Integer>) context.getBean(DON_ITR_DATA2));

    // ((CompoundIteratorImpl<Integer>) cpIter).setTraceEnabled(true);

    List<Integer> itx = null;
    int i = -1;
    int k = -1;
    int count = 0;
    while (cpIter.hasNext())
    {
      final int ci = cpIter.getCurrentIteratorIndex();
      if (ci > i)
      {
        i = ci;
        k = 0;
        itx = expectedIt.get(i);
      }

      final Integer nextItrValue = cpIter.next();
      final Integer expectedItrValue = itx.get(k++);
      count++;

      assertEquals(TEST_UDATA_NAME + "--compoundItr[" + i + "]{" + k + "} at "
          + count + " differs", expectedItrValue.intValue(),
          nextItrValue.intValue());
    }

    assertEquals(TEST_UDATA_NAME + " iterator total element count incorrect",
        expectedIteratorCount - 1, i);
    assertEquals(TEST_UDATA_NAME + " iterator total element count incorrect",
        count, expectedCount);
  }
}
