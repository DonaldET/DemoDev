/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.amazon.invoice.parser.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import demo.don.amazon.invoice.parser.ExtractStrategy;

/**
 * Shared parsing utilities and error checking on the interface working method,
 * also defines the abstract template method
 * 
 * @author Donald Trummell
 */
public abstract class AbstractExtractStrategy implements ExtractStrategy
{
  public static class ScanResult
  {
    private final String value;
    private final int endPointer;

    public ScanResult(String value, int endPointer)
    {
      this.value = value;
      this.endPointer = endPointer;
    }

    public String getValue()
    {
      return value;
    }

    public int getEndPointer()
    {
      return endPointer;
    }

    @Override
    public String toString()
    {
      return "[" + getClass().getSimpleName() + ";  0x"
          + Integer.toHexString(hashCode()) + ";  value: " + value
          + ";  endPointer: " + endPointer + "]";
    }
  }

  private String name = null;

  protected static final SimpleDateFormat parseFmt = new SimpleDateFormat(
      "MMM dd, yyyy");
  protected static final SimpleDateFormat displayFmt = new SimpleDateFormat(
      "MM/dd/yyyy");

  @Override
  public final void extractDetails(final String fileName,
      final String fileInfo, final StringBuilder data, final int start)
  {
    if (fileName == null || fileName.isEmpty())
      throw new IllegalArgumentException("fileName null or empty");

    if (fileInfo == null || fileInfo.isEmpty())
      throw new IllegalArgumentException("fileInfo null or empty");

    if (data == null)
      throw new IllegalArgumentException("data null");

    if (start < 0)
      throw new IllegalArgumentException("start not in file info");

    extractDetailsImpl(fileName, fileInfo, data, start);
  }

  protected abstract void extractDetailsImpl(final String fileName,
      final String fileInfo, final StringBuilder data, final int start);

  // ---------------------------------------------------------------------------

  protected StringBuilder addWithLimit(final StringBuilder cumulator,
      final String toAdd)
  {
    return addWithLimit(cumulator, toAdd, 25);
  }

  protected StringBuilder addWithLimitBig(final StringBuilder cumulator,
      final String toAdd)
  {
    return addWithLimit(cumulator, toAdd, FIELD_SIZE);
  }

  protected StringBuilder addWithLimit(final StringBuilder cumulator,
      final String toAdd, final int limit)
  {
    if (toAdd == null)
      throw new IllegalArgumentException("toAdd null");

    if (cumulator == null)
      throw new IllegalArgumentException("cumulator null");

    if (limit < 1)
      throw new IllegalArgumentException("limit small, " + limit);

    if (toAdd.length() <= limit)
      cumulator.append(toAdd);
    else
      cumulator.append(toAdd.substring(0, limit) + "+");

    return cumulator;
  }

  protected String dateStringParse(final String src)
  {
    if (src == null)
      throw new IllegalArgumentException("null date");

    final String dte = src.trim();
    if (dte.isEmpty())
      throw new IllegalArgumentException("empty date");

    Date input = null;
    try
    {
      input = parseFmt.parse(dte);
    }
    catch (ParseException pex)
    {
      return pex.getMessage();
    }

    return input == null ? R_BAD_DATE : displayFmt.format(input);
  }

  protected ScanResult parseDate(final String fileInfo, final int start,
      final boolean tryUpper)
  {
    int b = start;
    String dateStr = R_BAD_DATE;

    String beginner = P_DIGITAL_ORDER;
    int p = fileInfo.indexOf(beginner, start);
    if (p > -1)
    {
      int a = p + beginner.length();
      String ender = BOLD_CLOSE_TAG;
      p = fileInfo.indexOf(ender, a);
      if (tryUpper)
      {
        if (p < 0)
          p = fileInfo.indexOf(ender.toUpperCase(), a);
      }
      
      if (p > -1)
      {
        b = p;
        dateStr = fileInfo.substring(a, b).trim();
        if (dateStr.isEmpty())
          dateStr = R_EMPTY_DATE;
        dateStr = dateStringParse(dateStr);
        b = p + ender.length() - 1;
      }
    }

    final ScanResult scanOfDateResult = new ScanResult(dateStr, b);

    return scanOfDateResult;
  }

  protected ScanResult parseOrderType(final String fileInfo, final int start)
  {
    int b = start;
    String type = R_OTHER;

    String beginner = P_ORDER_TYPE_OPEN;
    int p = fileInfo.indexOf(beginner, start);
    if (p > -1)
    {
      int remember = p + beginner.length();
      String ender = P_ORDER_TYPE_CLOSE;
      p = fileInfo.indexOf(ender, remember);
      if (p > -1)
      {
        b = p + ender.length() - 1;
        type = R_KINDLE_EDITION;
      }
    }

    return new ScanResult(type, b);
  }

  protected ScanResult parseGrandTotal(final String fileInfo, final int start)
  {
    int b = start;
    String totalStr = "";
    String beginner = P_GRAND_TOTAL_DOLLAR;
    int p = fileInfo.indexOf(beginner, start);
    if (p < 0)
    {
      beginner = P_GRAND_TOTAL;
      p = fileInfo.indexOf(beginner, start);
    }

    if (p < 0)
      totalStr = "No '" + P_GRAND_TOTAL_DOLLAR + "' or '" + P_GRAND_TOTAL
          + "' entry";
    else
    {
      int a = p + beginner.length();
      String ender = BOLD_CLOSE_TAG;
      b = fileInfo.indexOf(ender, a);
      if (b < 0)
        b = fileInfo.indexOf(ender.toUpperCase(), a);
      if (b < 0)
        throw new IllegalStateException("No '" + P_GRAND_TOTAL_DOLLAR
            + "' entry closer found");

      totalStr = fileInfo.substring(a, b).trim();
      if (totalStr.startsWith("$"))
        totalStr = totalStr.substring(1).trim();
      b = b - 1;
    }

    return new ScanResult(totalStr, b);
  }

  protected String titleFormatter(final String src)
  {
    if (src == null)
      throw new IllegalArgumentException("null title");

    String tit = src.trim();
    if (tit.isEmpty())
      throw new IllegalArgumentException("empty title");

    tit = tit.replaceAll("\\(S3\\)", "[S3]");
    tit = tit.replaceAll("\\(EC2\\)", "[EC2]");

    int p = tit.indexOf("(");
    if (p > 0)
    {
      int q = tit.indexOf(")", p + 1);
      tit = tit.substring(0, q > 0 ? q + 1 : p + 1);
    }

    tit = AbstractExtractStrategy.removeProblems(tit);

    return tit;
  }

  public static String removeProblems(final String src)
  {
    if (src == null)
      throw new IllegalArgumentException("src null");

    String cpy = src.trim();
    if (!cpy.isEmpty())
    {
      cpy = cpy.replaceAll("\\&+", "{amp}");
      cpy = cpy.replaceAll("\\s+", " ");
      cpy = cpy.replaceAll("\\,+", ";");
      cpy = cpy.replaceAll("\\<.*\\>", "");
    }

    return cpy.trim();
  }

  public String getName()
  {
    return name;
  }

  protected void setName(final String name)
  {
    this.name = name;
  }
}
