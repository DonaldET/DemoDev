/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.amazon.games;

/**
 * A basic customer identifier implementation providing <code>Object</code>
 * functions.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public abstract class AbstractID implements Comparable<AbstractID> {
	private final String id;

	protected AbstractID(final String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public int compareTo(final AbstractID other) {
		return id.compareTo(other.getId());
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;

		if (!(other instanceof AbstractID))
			return false;

		return id.equals(((AbstractID) other).getId());
	}

	@Override
	public String toString() {
		return id.toString();
	}
}
