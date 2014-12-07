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
    // Invoice
    InvoiceType inv = InvoiceType.DET;
    String fileType = ExtractStrategy.DETAILS_FOR_ORDER;
    int p = fileInfo.indexOf(fileType, start);
    if (p < 0)
    {
      inv = InvoiceType.FINAL_DET;
      fileType = ExtractStrategy.FINAL_DETAILS_FOR_ORDER;
      p = fileInfo.indexOf(fileType, start);
      if (p < 0)
      {
        data.append("\"**** No entry for '" + ExtractStrategy.DETAILS_FOR_ORDER
            + "'\" or '" + ExtractStrategy.FINAL_DETAILS_FOR_ORDER + "'");
        return;
      }
    }

    // addWithLimit(data, QUOTE);

    int a = p + fileType.length();
    int b = fileInfo.indexOf(SPACE, a);
    final String invoice = AbstractExtractStrategy.removeProblems(fileInfo
        .substring(a, b));
    addWithLimitBig(data, invoice);

    // addWithLimit(data, QUOTE);

    if (InvoiceType.FINAL_DET == inv)
    {
      addWithLimit(data, SEPERATOR);
      // addWithLimit(data, QUOTE);
      data.append("**** No entry info for '"
          + ExtractStrategy.FINAL_DETAILS_FOR_ORDER + "'");
      // addWithLimit(data, QUOTE);
      return;
    }

    // Date
    addWithLimit(data, SEPERATOR);
    p = fileInfo.indexOf(DIGITAL_ORDER, b + 1);
    a = p + DIGITAL_ORDER.length();
    b = fileInfo.indexOf(BOLD_CLOSE_TAG, a);
    String dateStr = fileInfo.substring(a, b).trim();
    if (dateStr.isEmpty())
      dateStr = EMPTY_DATE;
    dateStr = dateParse(dateStr);
    addWithLimitBig(data, dateStr);

    // Title
    addWithLimit(data, SEPERATOR);
    // addWithLimit(data, QUOTE);
    int remember = b + 1;
    p = fileInfo.indexOf(A_TAG_BEGIN, remember);
    a = fileInfo.indexOf(A_TAG_CLOSE, p + A_TAG_BEGIN.length());
    a += A_TAG_CLOSE.length();
    b = fileInfo.indexOf(A_TAG_END, a);

    String title = fileInfo.substring(a, b).trim();
    if (ExtractStrategy.ORDER_SUMMARY.equals(title))
    {
      b = remember;
      p = fileInfo.indexOf(NOT_ORDER_SUMMARY, remember);
      a = p + NOT_ORDER_SUMMARY.length();
      b = fileInfo.indexOf(BOLD_CLOSE_TAG, a);
      title = fileInfo.substring(a, b).trim();
    }

    if (title.isEmpty())
      title = "Untitled";
    title = this.titleFormatter(title);

    addWithLimitBig(data, title);
    // addWithLimit(data, QUOTE);

    // Order type (e.g. Kindle)
    addWithLimit(data, SEPERATOR);
    // addWithLimit(data, QUOTE);
    p = fileInfo.indexOf(KINDLE_EDITION, b + 1);
    if (p > 0)
    {
      a = p;
      b = a + KINDLE_EDITION.length();
      addWithLimitBig(data, fileInfo.substring(a, b).trim());
    }
    else
      addWithLimit(data, "[OTHER]");
    // addWithLimit(data, QUOTE);

    // Total
    addWithLimit(data, SEPERATOR);
    p = fileInfo.indexOf(GRAND_TOTAL, b + 1);
    if (p < 0)
      addWithLimitBig(data, "No '" + GRAND_TOTAL + "' entry");
    else
    {
      a = p + GRAND_TOTAL.length();
      b = fileInfo.indexOf(BOLD_CLOSE_TAG, a);
      addWithLimitBig(data, fileInfo.substring(a, b).trim());
    }
  }
}
