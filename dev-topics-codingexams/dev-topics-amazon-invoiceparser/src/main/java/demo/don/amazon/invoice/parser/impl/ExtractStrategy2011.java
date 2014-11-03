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

import demo.don.amazon.invoice.parser.ExtractStrategy;

/**
 * Defines the concrete extract method for 2011 invoices
 * 
 * @author Donald Trummell
 */
public class ExtractStrategy2011 extends AbstractExtractStrategy implements
    ExtractStrategy
{
  public ExtractStrategy2011()
  {
    setName("2011");
  }

  @Override
  protected void extractDetailsImpl(final String fileName,
      final String fileInfo, final StringBuilder data, final int start)
  {
    int a = start;
    int b = start;
    int p = start;

    // Invoice
    InvoiceType inv = InvoiceType.DET;
    String fileType = ExtractStrategy.P_ORD_DET;
    p = fileInfo.indexOf(fileType, start);
    if (p < 0)
    {
      inv = InvoiceType.FINAL_DET;
      fileType = ExtractStrategy.P_ORD_DET_FINAL;
      p = fileInfo.indexOf(fileType, start);
      if (p < 0)
      {
        inv = InvoiceType.SUMMARY;
        fileType = ExtractStrategy.P_ORD_SMRY_HASH;
        p = fileInfo.indexOf(fileType, start);
        if (p < 0)
        {
          data.append("\"**** No entry for '" + ExtractStrategy.P_ORD_DET
              + "' or '" + ExtractStrategy.P_ORD_DET_FINAL + "' or '"
              + ExtractStrategy.P_ORD_SMRY_HASH + "'");
          return;
        }
      }
    }

    a = p + fileType.length();
    b = fileInfo.indexOf(BOLD_CLOSE_TAG, a);
    if (b < 0)
      b = fileInfo.indexOf(LINE_BREAK, a);
    if (b < 0)
      b = fileInfo.indexOf(LINE_BREAK.toUpperCase(), a);
    if (b < 0)
      throw new IllegalArgumentException("no invoice close tag");

    final String invoice = AbstractExtractStrategy.removeProblems(fileInfo
        .substring(a, b));
    addWithLimitBig(data, invoice);

    if (InvoiceType.FINAL_DET == inv)
    {
      addWithLimit(data, SEPERATOR);
      data.append("**** No entry info for '" + ExtractStrategy.P_ORD_DET_FINAL
          + "'");
      return;
    }

    // Date
    int remember = b + 1;
    final ScanResult dateScanResult = parseDate(fileInfo, remember, true);
    final String dateStr = dateScanResult.getValue();
    b = dateScanResult.getEndPointer();

    addWithLimit(data, SEPERATOR);
    addWithLimitBig(data, dateStr);

    // Title
    remember = b + 1;
    ScanResult titleScanResult = findTitle1(fileInfo, remember);
    String title = titleScanResult.getValue();
    if (title == null || title.isEmpty() || title.startsWith(R_EMPTY_FMT_TITLE)
        || title.startsWith(R_EMPTY_TITLE)
        || title.startsWith(R_UNTITLED_LOOK_FAILED))
    {
      titleScanResult = findTitle2(fileInfo, remember);
      title = titleScanResult.getValue();
      if (title == null || title.isEmpty()
          || title.startsWith(R_EMPTY_FMT_TITLE)
          || title.startsWith(R_EMPTY_TITLE)
          || title.startsWith(R_UNTITLED_LOOK_FAILED))
      {
        titleScanResult = findTitle3(fileInfo, remember);
        title = titleScanResult.getValue();
      }
    }
    b = titleScanResult.getEndPointer();

    title = titleFormatter(title.isEmpty() ? R_EMPTY_FMT_TITLE : title);

    addWithLimit(data, SEPERATOR);
    addWithLimitBig(data, title);

    // Order type (e.g. Kindle)
    remember = b;
    final ScanResult orderScanResult = parseOrderType(fileInfo, remember);
    final String type = orderScanResult.getValue();
    b = orderScanResult.getEndPointer();

    addWithLimit(data, SEPERATOR);
    addWithLimitBig(data, type);

    // Total
    remember = b + 1;
    final ScanResult totalScanResult = parseGrandTotal(fileInfo, remember);
    final String totalStr = totalScanResult.getValue();
    b = totalScanResult.getEndPointer();

    addWithLimit(data, SEPERATOR);
    addWithLimitBig(data, totalStr);
  }

  // ---------------------------------------------------------------------------

  private ScanResult findTitle1(final String fileInfo, final int start)
  {
    int b = start;
    String title = "";

    String beginner = P_INTRODUCER;
    int p = fileInfo.indexOf(beginner, start);
    if (p < 0)
      title = R_UNTITLED_LOOK_FAILED;
    else
    {
      int a = p + beginner.length();
      String ender = QUOTED_TAG_CLOSE;
      p = fileInfo.indexOf(ender, a);
      if (p < 0)
        title = R_UNTITLED_LOOK_FAILED + "-" + ender;
      else
      {
        a = p + ender.length();
        ender = A_TAG_CLOSE;
        b = fileInfo.indexOf(ender, a);
        if (b < 0)
        {
          title = R_UNTITLED_LOOK_FAILED + "-" + ender;
          b = start;
        }
        else
        {
          title = fileInfo.substring(a, b).trim();
          b = b - 1;
        }
      }
    }

    if (!title.startsWith(R_UNTITLED_LOOK_FAILED))
    {
      if (title.isEmpty())
        title = R_EMPTY_TITLE;
      else
      {
        title = titleFormatter(title);
        if (title.isEmpty())
          title = R_EMPTY_FMT_TITLE;
      }
    }

    return new ScanResult(title, b);
  }

  private ScanResult findTitle2(final String fileInfo, final int start)
  {
    int b = start;
    String title = "";

    String beginner = P_NEXT_STEP;
    int p = fileInfo.indexOf(beginner, start);
    if (p < 0)
    {
      beginner = P_NEXT_STEP2;
      p = fileInfo.indexOf(beginner, start);
      if (p < 0)
      {
        beginner = P_NEXT_STEP3;
        p = fileInfo.indexOf(beginner, start);
      }
    }

    if (p < 0)
      title = R_UNTITLED_LOOK_FAILED + beginner;
    else
    {
      int a = p + beginner.length();
      beginner = BOLD_OPEN_TAG;
      p = fileInfo.indexOf(beginner, a);
      if (p < 0)
      {
        beginner = BOLD_OPEN_TAG.toUpperCase();
        p = fileInfo.indexOf(beginner, a);
        if (p < 0)
          throw new IllegalStateException("no title " + beginner + " opener");
      }
      a = p + beginner.length();

      String ender = BOLD_CLOSE_TAG;
      p = fileInfo.indexOf(ender, a);
      if (p < 0)
      {
        ender = BOLD_CLOSE_TAG.toUpperCase();
        p = fileInfo.indexOf(ender, a);
        if (p < 0)
          throw new IllegalStateException("no title " + ender + " closer");
      }

      b = p;
      title = fileInfo.substring(a, b).trim();
      b = b - 1;
    }

    if (title.isEmpty())
      title = R_EMPTY_TITLE;
    else
    {
      title = titleFormatter(title);
      if (title.isEmpty())
        title = R_EMPTY_FMT_TITLE;
    }

    return new ScanResult(title, b);
  }

  private ScanResult findTitle3(final String fileInfo, final int start)
  {
    int b = start;
    String title = "";

    String beginner = "<A href=\"http://www.amazon.com/gp/product";
    int p = fileInfo.indexOf(beginner, start);
    if (p < 0)
      title = R_UNTITLED_LOOK_FAILED + beginner;
    else
    {
      int a = p + beginner.length();
      beginner = QUOTED_TAG_CLOSE;
      p = fileInfo.indexOf(beginner, a);
      if (p < 0)
        throw new IllegalStateException("no title " + beginner + " opener");
      a = p + beginner.length();

      String ender = A_TAG_CLOSE.toUpperCase();
      p = fileInfo.indexOf(ender, a);
      if (p < 0)
        throw new IllegalStateException("no title " + ender + " closer");

      b = p;
      title = fileInfo.substring(a, b).trim();
      b = b - 1;
    }

    if (title.isEmpty())
      title = R_EMPTY_TITLE;
    else
    {
      title = titleFormatter(title);
      if (title.isEmpty())
        title = R_EMPTY_FMT_TITLE;
    }

    return new ScanResult(title, b);
  }
}
