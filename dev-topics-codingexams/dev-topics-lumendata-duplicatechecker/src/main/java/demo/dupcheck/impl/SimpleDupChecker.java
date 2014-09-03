/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.dupcheck.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import demo.dupcheck.domain.DataMetricsBean;
import demo.dupcheck.domain.DataRow;
import demo.dupcheck.domain.DupChecker;

/**
 * A sample implementation of a duplicate data checker
 * 
 * @author Donald Trummell
 */
public class SimpleDupChecker implements DupChecker
{
  public SimpleDupChecker()
  {
  }

  @Override
  public DataMetricsBean checkData(final List<DataRow> data)
  {
    final Set<String> complete = new TreeSet<String>();
    final Set<String> incomplete = new TreeSet<String>();
    final Set<String> duplicate = new TreeSet<String>();
    final Set<String> nullEntries = new TreeSet<String>();
    List<String> colNames = null;

    int rowRead = -1;
    for (DataRow row : data)
    {
      rowRead++;
      final int index = (int) row.getRowIndex();
      final List<String> values = row.getData();
      if (rowRead < 1)
      {
        colNames = values;
        if (colNames == null || colNames.isEmpty())
          throw new IllegalArgumentException(
              "first row of column names is null or empty");
        for (final String colname : colNames)
          if (colname == null || colname.isEmpty())
            throw new IllegalArgumentException(
                "column name in first row of column names is null or empty");
      }
      else
      {
        final Map<String, Integer> valueCnts = new HashMap<String, Integer>();
        final String idxLead = String.format("%05d.", index);
        int colChecked = 0;
        for (String val : values)
        {
          final String colName = idxLead + colNames.get(colChecked++);
          if (val == null || val.isEmpty())
          {
            nullEntries.add(colName);
            val = "<NULL_ENTRY>";
          }

          if (val.length() > 1)
            complete.add(colName);
          else
            incomplete.add(colName);

          Integer count = valueCnts.get(val);
          count = (count == null) ? 1 : count.intValue() + 1;
          valueCnts.put(val, count);
        }

        if (!valueCnts.isEmpty())
        {
          final Collection<Integer> wordCounts = valueCnts.values();
          for (final Integer count : wordCounts)
          {
            if (count > 1)
            {
              duplicate.add(idxLead + "*");
              break;
            }
          }
        }
      }
    }

    return new DataMetricsBean(complete, incomplete, duplicate, nullEntries);
  }
}
