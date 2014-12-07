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
 * Defines the concrete extract method for 2012 invoices
 * 
 * @author Donald Trummell
 */
public class ExtractStrategy2012 extends AbstractExtractStrategy implements
    ExtractStrategy
{
  public ExtractStrategy2012()
  {
    setName("2012");
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
        data.append("\"**** No entry for '" + ExtractStrategy.P_ORD_DET
            + "' or '" + ExtractStrategy.P_ORD_DET_FINAL + "'");
        return;
      }
    }

    a = p + fileType.length();
    b = fileInfo.indexOf(SPACE, a);
    if (b < 0)
      throw new IllegalStateException("no invoice close tag");
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
    final ScanResult dateScanResult = parseDate(fileInfo, remember, false);
    final String dateStr = dateScanResult.getValue();
    b = dateScanResult.getEndPointer();

    addWithLimit(data, SEPERATOR);
    addWithLimitBig(data, dateStr);

    // Title
    remember = b + 1;
    final ScanResult titleScanResult = findTitle1(fileInfo, remember);
    String title = titleScanResult.getValue();
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

    String beginner = P_A_TAG_BEGIN;
    int p = fileInfo.indexOf(beginner, start);
    if (p > -1)
    {
      beginner = QUOTED_TAG_CLOSE;
      int a = fileInfo.indexOf(beginner, p + beginner.length());
      if (a > -1)
      {
        a += beginner.length();
        String ender = A_TAG_CLOSE;
        p = fileInfo.indexOf(ender, a);
        if (p > -1)
        {
          b = p;
          title = fileInfo.substring(a, b).trim();
          if (ExtractStrategy.P_ORD_SMRY.equals(title))
          {
            final ScanResult result = findTitle2(fileInfo, start);
            title = result.getValue();
            b = result.getEndPointer();
          }
          b = b - 1;
        }
      }
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

  private ScanResult findTitle2(final String fileInfo, final int start)
  {
    String title = "";
    int b = start;
    String beginner = P_BAD_ORD_SMRY;
    int p = fileInfo.indexOf(beginner, b);
    if (p > -1)
    {
      int a = p + beginner.length();
      String ender = BOLD_CLOSE_TAG;
      b = fileInfo.indexOf(ender, a);
      title = fileInfo.substring(a, b).trim();
      b = b - 1;
    }

    return new ScanResult(title, b);
  }
}
