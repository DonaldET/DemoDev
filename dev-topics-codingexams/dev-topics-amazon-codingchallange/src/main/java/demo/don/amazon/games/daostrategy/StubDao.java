/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.amazon.games.daostrategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import demo.don.amazon.games.CustomerID;
import demo.don.amazon.games.Dao;
import demo.don.amazon.games.ProductID;

/**
 * Stub provides test data with overlapping customer ids and product ids
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class StubDao implements Dao {
	public static final List<CustomerID> testIds = Arrays.asList(new CustomerID("Bob"), new CustomerID("Sam"),
			new CustomerID("Tom"));

	public static final List<CustomerID> customerIds = Arrays.asList(new CustomerID("Bob"), // ***
			new CustomerID("Don"), new CustomerID("Harold"), new CustomerID("Jane"), new CustomerID("Janice"),
			new CustomerID("Jim"), new CustomerID("Michelle"), new CustomerID("Sally"), new CustomerID("Sam"), // ***
			new CustomerID("Tom")); // ***

	private static final List<List<CustomerID>> friendsList = Arrays.asList(
			Arrays.asList(new CustomerID("Tom"), new CustomerID("Harold"), new CustomerID("Sally"),
					new CustomerID("Sam")), // ---Bob
			new ArrayList<CustomerID>(), // -- Don
			new ArrayList<CustomerID>(), // -- Harold
			new ArrayList<CustomerID>(), // -- Jane
			new ArrayList<CustomerID>(), // -- Janice
			new ArrayList<CustomerID>(), // -- Jim
			new ArrayList<CustomerID>(), // -- Michelle
			new ArrayList<CustomerID>(), // -- Sally
			Arrays.asList(new CustomerID("Jane"), new CustomerID("Tom"), new CustomerID("Harold"),
					new CustomerID("Sally"), new CustomerID("Jim")), // ---Sam
			Arrays.asList(new CustomerID("Bob"), new CustomerID("Don"), new CustomerID("Janice"),
					new CustomerID("Michelle"), new CustomerID("Sally"))); // ---Tom

	public final Map<CustomerID, List<CustomerID>> friends = new HashMap<>();

	public final Map<CustomerID, List<ProductID>> libraries = new HashMap<>();

	private void setupData() {
		if (customerIds.size() != friendsList.size())
			throw new IllegalArgumentException("friends list out of sync, customerIds[" + customerIds.size()
					+ "; friendsList[" + friendsList.size() + "]");

		int i = 0;
		for (final CustomerID id : customerIds) {
			final List<CustomerID> friendList = friendsList.get(i++);
			if (friendList == null)
				throw new NullPointerException("null friend list for " + id + " [" + i + "]");
			friends.put(id, friendList);
		}

		int gn = 1;
		for (final CustomerID id : getAllTestCustomerIDs()) {
			final List<ProductID> library = new ArrayList<ProductID>();
			for (int g = 1; g <= gn; g++)
				library.add(new ProductID("G" + g));
			libraries.put(id, library);
			gn++;
		}
	}

	public StubDao() {
		setupData();
	}

	@Override
	public List<CustomerID> getFriendsListForUser(final CustomerID id) {
		return friends.get(id);
	}

	@Override
	public List<ProductID> getLibraryForUser(final CustomerID id) {
		return libraries.get(id);
	}

	/**
	 * Optional data list
	 *
	 * @param argc launch parameters
	 */
	public static void main(final String[] argc) {
		final StubDao dao = new StubDao();

		System.out.println("   ==== User Friend List ====");
		for (final CustomerID id : dao.getAllTestCustomerIDs())
			System.out.println(String.format("%-10s", id) + " ==> " + dao.friends.get(id));

		System.out.println("\n   ==== Game Data List ==== (* marks known game user)");
		for (final CustomerID id : dao.getAllTestCustomerIDs())
			System.out.println(String.format("%-10s", customerIds.contains(id) ? new CustomerID(id.getId() + "*") : id)
					+ " ==> " + dao.libraries.get(id));
	}

	public List<CustomerID> getAllTestCustomerIDs() {
		final Set<CustomerID> names = new TreeSet<CustomerID>();
		names.addAll(customerIds);

		for (List<CustomerID> friendList : friendsList)
			for (final CustomerID id : friendList)
				names.add(id);

		final List<CustomerID> ordered = new ArrayList<CustomerID>();
		for (final Iterator<CustomerID> nameItr = names.iterator(); nameItr.hasNext();)
			ordered.add(nameItr.next());

		return ordered;
	}

	public List<CustomerID> getCustomerIDsWithFriends() {
		final Set<CustomerID> names = new TreeSet<CustomerID>();
		names.addAll(testIds);

		final List<CustomerID> ordered = new ArrayList<CustomerID>();
		for (final Iterator<CustomerID> nameItr = names.iterator(); nameItr.hasNext();)
			ordered.add(nameItr.next());

		return ordered;
	}
}
