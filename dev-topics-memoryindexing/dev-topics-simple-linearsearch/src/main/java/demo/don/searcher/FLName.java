/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.searcher;

import java.util.Comparator;

import org.apache.commons.lang3.Validate;

/**
 * This class manages the simple Western European notion of a person's name
 * consisting of two parts: a first or given name, and last or family name. It
 * supports three sets of responsibilities:
 * <ul>
 * <li>Supplying an ordering by implementing <code>Comparable</code> and
 * <code>Comparator</code></li>
 * <li>Handle <code>equals</code>, <code>hashCode</code>, and
 * <code>toString</code></li>
 * <li>Provides <code>clone</code></li>
 * </ul>
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class FLName implements Comparable<FLName>, Comparator<FLName>, Cloneable {
	private String firstName;
	private String lastName;

	public FLName(final String firstName, final String lastName) {
		super();

		Validate.notEmpty(firstName, "firstName empty");
		this.firstName = firstName.trim().toLowerCase();

		Validate.notEmpty(lastName, "lastName empty");
		this.lastName = lastName.trim().toLowerCase();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public int compare(final FLName o1, final FLName o2) {
		return o1 != null ? o1.compareTo(o2) : (o2 == null ? 0 : 1);
	}

	@Override
	public int compareTo(final FLName obj) {
		if (obj == null)
			return -1;

		int cv = mapToOne(lastName.compareTo(obj.lastName));
		if (cv == 0)
			cv = mapToOne(firstName.compareTo(obj.firstName));

		return cv;
	}

	@Override
	public int hashCode() {
		int hc = 7757;
		hc = 3343 * hc + firstName.hashCode();
		hc = 3343 * hc + lastName.hashCode();

		return hc;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof FLName)
			if (obj == this)
				return true;
			else if (getClass().equals(obj.getClass()))
				return compareTo((FLName) obj) == 0;

		return false;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		final StringBuilder msg = new StringBuilder();
		msg.append("[");
		msg.append(getClass().getSimpleName());
		msg.append(" - 0x");
		msg.append(Integer.toHexString(hashCode()));
		msg.append(";  firstName: ");
		msg.append(firstName);
		msg.append(";  lastName: ");
		msg.append(lastName);
		msg.append("]");

		return msg.toString();
	}

	protected int mapToOne(final int cv) {
		return cv < 0 ? -1 : (cv > 0 ? 1 : 0);
	}
}
