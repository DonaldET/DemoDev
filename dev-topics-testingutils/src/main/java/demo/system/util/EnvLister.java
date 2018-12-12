/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.system.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Obtain environment and property values for debugging
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class EnvLister
{
  /**
   * Prevent construction
   */
  private EnvLister()
  {
  }

  public static String getEnvVars()
  {
    final StringBuilder varList = new StringBuilder();

    Map<String, String> vars = System.getenv();

    Set<String> keySet = vars.keySet();
    final List<String> keys = new ArrayList<String>();
    for (final String key : keySet)
      keys.add(key);
    keySet = null;
    Collections.sort(keys);

    for (final String key : keys)
    {
      if (key == null || key.isEmpty())
        continue;

      if (varList.length() > 0)
        varList.append("\n");

      varList.append(key);
      varList.append("=\"");

      String value = String.valueOf(vars.get(key));
      if (value.endsWith("\n"))
      {
        final int lth = value.length();
        value = lth > 1 ? value.substring(0, lth - 2) : "";
      }

      varList.append(value);
      varList.append("\"");
    }

    vars = null;

    return varList.toString();
  }

  public static String getProperties()
  {
    final StringBuilder varList = new StringBuilder();

    Properties props = System.getProperties();

    Set<Object> keySet = props.keySet();
    final List<String> keys = new ArrayList<String>();
    for (final Object key : keySet)
      keys.add((String) key);
    keySet = null;
    Collections.sort(keys);

    for (final String key : keys)
    {
      if (key == null || key.isEmpty())
        continue;

      if (varList.length() > 0)
        varList.append("\n");

      varList.append(key);
      varList.append("=\"");

      String value = String.valueOf(props.getProperty(key));
      if (value.endsWith("\n"))
      {
        final int lth = value.length();
        value = lth > 1 ? value.substring(0, lth - 2) : "";
      }

      varList.append(value);
      varList.append("\"");
    }

    props = null;

    return varList.toString();
  }
}
