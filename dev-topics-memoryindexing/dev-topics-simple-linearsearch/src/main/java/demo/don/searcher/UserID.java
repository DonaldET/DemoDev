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
 * Adds the Email as part of a Human identification by extending
 * <code>FLName</code>.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class UserID extends FLName implements Comparable<FLName>, Comparator<FLName>, Cloneable {
	private String email = null;

	public UserID(final String firstName, final String lastName, final String email) {
		super(firstName, lastName);

		Validate.notEmpty(firstName, "firstName empty");
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public int compare(final UserID o1, final UserID o2) {
		return o1 != null ? o1.compareTo(o2) : (o2 == null ? 0 : 1);
	}

	public int compareTo(final UserID obj) {
		if (obj == null)
			return -1;

		int cv = mapToOne(super.compareTo(obj));
		if (cv == 0)
			cv = mapToOne(email.compareTo(obj.email));

		return cv;
	}

	@Override
	public int hashCode() {
		int hc = super.hashCode();
		hc = 3343 * hc + email.hashCode();

		return hc;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof UserID)
			if (obj == this)
				return true;
			else if (getClass().equals(obj.getClass()))
				return compareTo((UserID) obj) == 0;

		return false;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public String toString() {
		final StringBuilder msg = new StringBuilder();
		msg.append(super.toString());
		msg.append("[email: ");
		msg.append(email);
		msg.append("]");

		return msg.toString();
	}
}
