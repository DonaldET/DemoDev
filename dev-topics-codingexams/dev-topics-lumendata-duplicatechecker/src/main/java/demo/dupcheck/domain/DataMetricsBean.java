/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.dupcheck.domain;

import java.util.Set;

/**
 * Holds the result of checking a collection of <code>DataRow</code> instances,
 * read from input data; they are checked for completeness and duplication
 * 
 * @author Donald Trummell
 */
public class DataMetricsBean
{
  final Set<String> complete;
  final Set<String> incomplete;
  final Set<String> duplicate;
  final Set<String> nullEntries;

  public DataMetricsBean(final Set<String> complete,
      final Set<String> incomplete, final Set<String> duplicate,
      final Set<String> nullEntries)
  {
    super();
    this.complete = complete;
    this.incomplete = incomplete;
    this.duplicate = duplicate;
    this.nullEntries = nullEntries;
  }

  public Set<String> getComplete()
  {
    return complete;
  }

  public Set<String> getIncomplete()
  {
    return incomplete;
  }

  public Set<String> getDuplicate()
  {
    return duplicate;
  }

  public Set<String> getNullEntries()
  {
    return nullEntries;
  }

  @Override
  public String toString()
  {
    final StringBuilder msg = new StringBuilder();
    msg.append("[");
    msg.append(getClass().getSimpleName());
    msg.append("; 0x");
    msg.append(Integer.toHexString(hashCode()));
    msg.append(";\n  complete:   (" + (complete == null ? -1 : complete.size())
        + ") ");
    msg.append(complete);
    msg.append(";\n  incomplete: ("
        + (incomplete == null ? -1 : incomplete.size()) + ") ");
    msg.append(incomplete);
    msg.append(";\n  duplicate   ("
        + (duplicate == null ? -1 : duplicate.size()) + ") ");
    msg.append(duplicate);
    msg.append(";\n  nulls:      ("
        + (nullEntries == null ? -1 : nullEntries.size()) + ") ");
    msg.append(nullEntries);
    msg.append("]");

    return msg.toString();
  }
}
