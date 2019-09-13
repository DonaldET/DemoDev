/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.amazon.games.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.amazon.games.CustomerID;
import demo.amazon.games.ProductID;
import demo.amazon.games.daostrategy.StubDao;

/**
 * Validate test data integrity
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class StubDaoTest
{
  private StubDao dao;

  @Before
  public void setUp() throws Exception
  {
    dao = new StubDao();
  }

  @After
  public void tearDown() throws Exception
  {
    dao = null;
  }

  @Test
  public void testCustomerIDs()
  {
    final List<CustomerID> testCustomerIDs = dao.getAllTestCustomerIDs();
    Assert.assertNotNull("testCustomerIDs null", testCustomerIDs);
    Assert.assertFalse("testCustomerIDs empty", testCustomerIDs.isEmpty());
    final int keyListSize = testCustomerIDs.size();
    Assert.assertTrue("test customer id list size small, " + keyListSize,
        keyListSize > 5);
  }

  @Test
  public void testLibraryCustomerIDs()
  {
    final List<CustomerID> testLibCustomerIDs = checkLibraryKeys();
    Assert.assertNotNull("testLibCustomerIDs null", testLibCustomerIDs);
    Assert
        .assertFalse("testLibCustomerIDs empty", testLibCustomerIDs.isEmpty());
    final int keyListSize = testLibCustomerIDs.size();
    Assert.assertTrue("test library customer id list size small, "
        + keyListSize, keyListSize > 5);
  }

  @Test
  public void testNonEmptyLibraries()
  {
    final List<CustomerID> libraryCustomerIDs = checkLibraryKeys();
    for (final CustomerID id : libraryCustomerIDs)
    {
      final List<ProductID> library = dao.libraries.get(id);
      Assert.assertNotNull("library null for " + id, library);
      Assert.assertFalse("library empty for " + id, library.isEmpty());
      Assert.assertEquals("library differs for " + id, library,
          dao.getLibraryForUser(id));
    }
  }

  @Test
  public void testNonEmptyFriends()
  {
    final Set<CustomerID> testable = new HashSet<CustomerID>(StubDao.testIds);

    final List<CustomerID> friendsCustomerIDs = checkFriendsKeys();
    for (final CustomerID id : friendsCustomerIDs)
    {
      final List<CustomerID> friends = dao.friends.get(id);
      Assert.assertNotNull("friends null for " + id, friends);
      if (testable.contains(id))
        Assert.assertFalse("friends empty for " + id, friends.isEmpty());
      Assert.assertEquals("friends differs for " + id, friends,
          dao.getFriendsListForUser(id));
    }
  }

  @Test
  public void testLibraryAndTestHaveIdenticalCustomerIDs()
  {
    final List<CustomerID> testCustomerIDs = dao.getAllTestCustomerIDs();
    final List<CustomerID> libraryCustomerIDs = checkLibraryKeys();

    final int testCustomerSize = testCustomerIDs.size();
    Assert.assertEquals("library list sizes differ", testCustomerSize,
        libraryCustomerIDs.size());

    int dcount = 0;
    for (int i = 0; i < testCustomerSize; i++)
    {
      final CustomerID customerID = testCustomerIDs.get(i);
      final CustomerID libCustomerID = libraryCustomerIDs.get(i);
      if (!customerID.equals(libCustomerID))
      {
        dcount++;
        System.err.println(dcount + ". name entry[" + i + "] differs; "
            + customerID + " <> " + libCustomerID);
      }
    }
    Assert.assertEquals("non-matching entries", 0, dcount);
  }

  // ---------------------------------------------------------------------------

  @Test
  public void testGetFriendsListForUser()
  {
    final Set<CustomerID> testable = new HashSet<CustomerID>(StubDao.testIds);

    final List<CustomerID> friendedCustomerIDs = checkFriendsKeys();
    for (final CustomerID id : friendedCustomerIDs)
    {
      final List<CustomerID> friendsListForUser = dao.getFriendsListForUser(id);
      Assert.assertNotNull("friendsListForUser null for " + id,
          friendsListForUser);
      if (testable.contains(id))
        Assert.assertFalse("friendsListForUser empty for " + id,
            friendsListForUser.isEmpty());
    }
  }

  @Test
  public void testGetLibraryForUser()
  {
    final List<CustomerID> testCustomerIDs = dao.getAllTestCustomerIDs();
    int lastSize = 0;
    for (final CustomerID id : testCustomerIDs)
    {
      final List<ProductID> libraryForUser = dao.getLibraryForUser(id);
      Assert.assertNotNull("libraryForUser null for " + id, libraryForUser);
      Assert.assertFalse("libraryForUser empty for " + id,
          libraryForUser.isEmpty());

      final int sz = libraryForUser.size();
      Assert.assertTrue("bad lib size (" + sz + " <= " + lastSize + ") for "
          + id, sz > lastSize);
      lastSize = sz;
    }
  }

  // ---------------------------------------------------------------------------

  private List<CustomerID> checkLibraryKeys()
  {
    final Set<CustomerID> keySet = dao.libraries.keySet();
    final CustomerID[] keys = new CustomerID[keySet.size()];
    keySet.toArray(keys);
    Arrays.sort(keys);

    return Arrays.asList(keys);
  }

  private List<CustomerID> checkFriendsKeys()
  {
    final Set<CustomerID> keySet = dao.friends.keySet();
    final CustomerID[] keys = new CustomerID[keySet.size()];
    keySet.toArray(keys);
    Arrays.sort(keys);

    return Arrays.asList(keys);
  }
}
