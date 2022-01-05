/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.dupcheck.domain;

import java.util.List;

/**
 * A <code>DataRow</code> implementation holds the processed data extracted from
 * the text file, including the associated index (zero based)
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public interface DataRow {
	public abstract long getRowIndex();

	public abstract List<String> getData();
}