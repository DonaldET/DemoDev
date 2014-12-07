/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.amazon.invoice.parser;

public interface ExtractStrategy
{
  public static final int FIELD_SIZE = 75;

  // Shared return values
  public static final String R_UNTITLED = "Untitled";
  public static final String R_UNTITLED_LOOK_FAILED = R_UNTITLED + "?";
  public static final String R_EMPTY_TITLE = R_UNTITLED + "!";
  public static final String R_EMPTY_FMT_TITLE = "Empty Formatted Title";
  public static final String R_EMPTY_DATE = "01/01/1901";
  public static final String R_BAD_DATE = "12/31/1900";
  public static final String R_OTHER = "[OTHER]";
  public static final String R_KINDLE_EDITION = "[Kindle Edition]";

  //
  // Shared HTML constructs
  public static final String SPACE = " ";
  public static final String SEPERATOR = ", ";
  public static final String QUOTE = "\"";
  public static final String BOLD_OPEN_TAG = "<b>";
  public static final String BOLD_CLOSE_TAG = "</b>";
  public static final String LINE_BREAK = "<br>";
  public static final String A_TAG_CLOSE = "</a>";
  public static final String QUOTED_TAG_CLOSE = "\">";

  // Anchor points for parse
  public static final String P_A_TAG_BEGIN = "<a href=\"";
  public static final String P_A_TAG_BEGIN2 = "<A href=\"";

  public static final String P_ORD_DET = "Details for Order # ";
  public static final String P_ORD_DET_FINAL = "Final Details for Order #";
  public static final String P_DIGITAL_ORDER = "Digital Order: ";
  public static final String P_GRAND_TOTAL_DOLLAR = "<b>Grand Total: $";
  public static final String P_ORDER_TYPE_OPEN = "[Kindle";
  public static final String P_ORDER_TYPE_CLOSE = "Edition]";

  // D2012 Strategy
  public static final String P_ORD_SMRY = "Order Summary";
  public static final String P_BAD_ORD_SMRY = "<tr><td valign=\"top\" align=\"left\"><b>";

  // D2011 Strategy
  public static final String P_ORD_SMRY_HASH = "Order Summary #";
  public static final String P_INTRODUCER = "<a href=\"http://www.amazon.com/gp/product";
  public static final String P_NEXT_STEP = "<td valign=\"top\" align=\"left\" style=\"padding:10px\">";
  public static final String P_NEXT_STEP2 = "<td align=\"left\" valign=\"top\">";
  public static final String P_NEXT_STEP3 = "<TD align=\"left\" valign=\"top\">";
  public static final String P_KE_SPACER = "                   ";
  public static final String P_KE2 = "[Kindle" + P_KE_SPACER + "Edition]";
  public static final String P_GRAND_TOTAL = "Grand Total:";

  public enum InvoiceType
  {
    DET, FINAL_DET, SUMMARY
  };

  public abstract void extractDetails(final String fileName,
      final String fileInfo, final StringBuilder data, final int start);
}