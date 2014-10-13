package demo.don.amazon.invoice.parser.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import demo.don.amazon.invoice.parser.ExtractStrategy;

public abstract class AbstractExtractStrategy implements ExtractStrategy
{
  protected static final String EMPTY_DATE = "01/01/1901";
  protected static final String BAD_DATE = "12/31/1900";
  protected static final SimpleDateFormat parseFmt = new SimpleDateFormat(
      "MMM dd, yyyy");
  protected static final SimpleDateFormat displayFmt = new SimpleDateFormat(
      "MM/dd/yyyy");

  protected AbstractExtractStrategy()
  {
  }

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

  protected String dateParse(final String src)
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

    return input == null ? BAD_DATE : displayFmt.format(input);
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
      cpy = cpy.replaceAll("\\,+", ";");
      cpy = cpy.replaceAll("\\s+", " ");
    }

    return cpy;
  }
}
