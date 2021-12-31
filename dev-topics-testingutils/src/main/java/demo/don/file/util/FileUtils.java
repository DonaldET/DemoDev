/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.file.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Function;

/**
 * File utilities to aid testing
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class FileUtils
{
  public static final String ERR_FLAG = " **** ";

  /**
   * Prevent construction
   */
  private FileUtils()
  {
  }

  public static String collectRawFileData(final File inputFile)
  {
    if (inputFile == null)
      throw new IllegalArgumentException("inputFile null");

    return collectFileData(inputFile, (String s) -> s, true);
  }

  public static String collectTrimmedFileData(final File inputFile)
  {
    if (inputFile == null)
      throw new IllegalArgumentException("inputFile null");

    return collectFileData(inputFile,
        (String s) -> s == null ? "null" : s.trim(), true);
  }

  public static String collectFileData(final File inputFile,
      final Function<String, String> formatter, final boolean addNewLine)
  {
    if (inputFile == null)
      throw new IllegalArgumentException("inputFile null");

    if (formatter == null)
      throw new IllegalArgumentException("formatter null");

    final StringBuilder data = new StringBuilder();

    if (!cumulateFile(data, inputFile, formatter, addNewLine))
      data.append(ERR_FLAG + "Cumulation of " + inputFile.getAbsolutePath()
          + " failed");

    return data.toString();
  }

  public static boolean cumulateFile(final StringBuilder data,
      final File inputFile, final Function<String, String> formatter,
      final boolean addNewLine)
  {
    if (data == null)
      throw new IllegalArgumentException("data null");

    if (inputFile == null)
      throw new IllegalArgumentException("inputFile null");

    if (formatter == null)
      throw new IllegalArgumentException("formatter null");

    boolean status = true;

    try (InputStream istr = new FileInputStream(inputFile);
    	   InputStreamReader isr = new InputStreamReader(istr);		 
    		 BufferedReader br = new BufferedReader(isr, 1024);)
    {
      int count = 0;
      String line = null;
      do
      {
        count++;
        try
        {
          line = br.readLine();
        }
        catch (IOException ex)
        {
          status = false;
          final String msg = ERR_FLAG + "Input file "
              + inputFile.getAbsolutePath() + " read failed at line " + count
              + ",\n      {" + ex.getClass().getSimpleName() + "};  Msg: "
              + ex.getMessage();
          System.err.println(ERR_FLAG + msg);
        }

        if (line != null)
        {
          final String fixedLine = formatter.apply(line);
          data.append(String.valueOf(fixedLine));
          if (addNewLine)
            data.append("\n");
        }
      }
      while (line != null);
    }
    catch (FileNotFoundException fnfEx)
    {
      System.err.println(ERR_FLAG + "Unable to find file "
          + inputFile.getAbsolutePath() + "\n" + ERR_FLAG + fnfEx.getMessage());
    }
    catch (IOException ioEx)
    {
      System.err.println(ERR_FLAG + "Unable to find file "
          + inputFile.getAbsolutePath() + "\n" + ERR_FLAG + ioEx.getMessage());
	  }

    return status;
  }
}
