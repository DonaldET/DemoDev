/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.searcher.index;

import org.apache.commons.lang3.Validate;

/**
 * Helper methods that support the creation of an <code>IndexSearcher</code> by
 * an <code>IndexBuilder</code>. Encoding and decoding support for non-negative
 * integer pointers is offered. Coded index values are used to define an
 * associated user identifier with a compact text representation that can be
 * embedded in the <code>SearchIndex</code> data structure.
 * <p>
 * In addition, processing support for a filter pattern is included that lower
 * cases, trims, and removes special characters (except those allowed in Email.)
 * Similar logic is supported in the string index builder to make sure that
 * search fields are processed the same as patterns
 *
 * @author Donald Trummell
 */
public class IndexHelper
{
  /**
   * The maximum size of the output coded string
   */
  public static final int MAX_DIGITS = 3;

  /**
   * The sequence of symbols representing digits of the radix
   */
  public static final String SYMBOL_STRING = "0123456789"
      + "ABCDEFGHIJKLMNOPQRSTUVWZYZ" + "abcdefghijklmnopqrstuvwxyz";

  /**
   * The zero symbol used as the fill character
   */
  public static final char ZERO_CH = SYMBOL_STRING.charAt(0);

  /**
   * The radix is the size of the symbol set
   */
  public static final int RADIX = SYMBOL_STRING.length();

  /**
   * The largest positive value represented in the MAX_DIGITS field
   */
  public static final int MAX_VALUE = (int) (Math.ceil(Math.pow(
      ((double) RADIX), (double) MAX_DIGITS))) - 1;

  /**
   * The character version of the symbol string
   */
  public static char[] symbols = null;

  /**
   * Special characters from Internet Email representations that are allowed in
   * the filter pattern processor
   */
  private static final String ALLOWED_EMAIL_SPECIAL = "*_-.@";

  /**
   * Define the mapping for radix symbols as chars
   */
  static
  {
    symbols = new char[RADIX];
    for (int i = 0; i < RADIX; i++)
      symbols[i] = SYMBOL_STRING.charAt(i);
  }

  private IndexHelper()
  {
  }

  /**
   * Encode a non-negative binary value into a text value using the default
   * radix
   *
   * @param value
   *          a non-negative input value to encode as text
   *
   * @return the non-null and non-empty encoded value in the default radix with
   *         leading zeros
   */
  public static String encode(final int value)
  {
    Validate.isTrue(value > -1, "value negative, ", value);
    Validate.isTrue(value <= MAX_VALUE, "value big, ", value);

    final char[] coded = new char[MAX_DIGITS];
    for (int i = 0; i < MAX_DIGITS; i++)
      coded[i] = ZERO_CH;

    if (value > 0)
    {
      int pos = MAX_DIGITS;
      int val = value;
      do
      {
        final int quot = val / RADIX;
        final int r = val - quot * RADIX;
        coded[--pos] = symbols[r];
        val = quot;
      }
      while (val > 0 && pos > 0);
    }

    return new String(coded);
  }

  /**
   * Decode (<em>text to binary conversion</em>) an input text parameter
   * representing a non-negative binary value used to locate an associated user
   * identifier
   *
   * @param codedInput
   *          the non-null and non-empty encoded text value
   *
   * @return the non-negative binary value corresponding to the parameter
   */
  public static int decode(final String codedInput)
  {
    Validate.notNull(codedInput, "codedInput null");
    Validate.notEmpty(codedInput, "codedInput empty");

    final String coded = codedInput.trim();
    final int codedLth = coded.length();
    int value = 0;

    for (int i = 0; i < codedLth; i++)
    {
      final char digitCH = coded.charAt(i);

      final int digitPos = SYMBOL_STRING.indexOf(digitCH);
      if (digitPos < 0)
        throw new IllegalArgumentException("unrecognized digit, '"
            + new String(new char[] { digitCH }) + "'");

      value = RADIX * value + digitPos;

      if (value > MAX_VALUE)
        throw new IllegalArgumentException("encoded value too large, '" + coded
            + "'");
    }

    return value;
  }

  /**
   * Process a string search pattern or a string based search index so it can be
   * used to find matches with a string based <code>IndexSearcher</code>
   * implementation
   * <p>
   * <strong>Note:</strong> this method implicitly assumes that the processed
   * <code>SearchIndex</code> contains lower case strings, space trimmed, and
   * special characters removed (except Email characters.)
   * </p>
   *
   * @param src
   *          the non-null and non-empty pattern that will be used to locate
   *          matches in the <code>SearchIndex</code>
   *
   * @return the potentially empty cleaned pattern
   */
  public static String filterPattern(final String src)
  {
    Validate.notEmpty(src, "src empty");
    final String copy = src.trim().toLowerCase();
    Validate.notEmpty(copy, "copy (processed source) empty");

    final char[] ccopy = copy.toCharArray();
    final StringBuilder cleaned = new StringBuilder();
    for (char c : ccopy)
      if (Character.isLetterOrDigit(c) || ALLOWED_EMAIL_SPECIAL.indexOf(c) > -1)
        cleaned.append(c);

    return cleaned.toString();
  }
}
