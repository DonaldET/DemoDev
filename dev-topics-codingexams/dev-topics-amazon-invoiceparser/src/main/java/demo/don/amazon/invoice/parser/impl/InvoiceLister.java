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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import demo.don.amazon.invoice.parser.ExtractStrategy;

/**
 * Parse input files, defined by path strings, extract required data depending
 * on the named extraction strategy, and write to extracted fields to output
 * files, also defined by path strings.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class InvoiceLister
{
  public static final String SYSIN = "Sysin";
  public static final String SYSOUT = "Sysout";
  public static final String DEFAULT_STRATEGY = "2012";

  private static final String ERR_FLAG = "ERR: ";

  private String inputFileName = SYSIN;
  private String outputFileName = SYSOUT;

  private boolean echoArgs = false;
  private InputStream inputFile = null;
  private PrintStream outputFile = null;
  private String runStrategy = DEFAULT_STRATEGY;

  private final Map<String, ExtractStrategy> strategies = new HashMap<String, ExtractStrategy>();
  private ExtractStrategy strategy = null;

  public InvoiceLister()
  {
    ExtractStrategy es = new ExtractStrategy2011();
    strategies.put(((AbstractExtractStrategy) es).getName(), es);
    es = new ExtractStrategy2012();
    strategies.put(((AbstractExtractStrategy) es).getName(), es);
  }

  /**
   * Read the input file and extract the desired information using the named run
   * strategy.
   *
   * @param inputFileName
   *          input file path
   * @param outputFileName
   *          output file path
   * @param runStrategy
   *          the named strategy
   *
   * @return {@code true} if successfully processed.
   */
  public int execute(final String inputFileName, final String outputFileName,
      final String runStrategy)
  {
    this.inputFileName = inputFileName;
    this.outputFileName = outputFileName;
    this.runStrategy = runStrategy;

    boolean status = false;
    try
    {
      status = setupRun();
      if (status)
        outputFile.println(processInvoice());
    }
    finally
    {
      cleanup();
    }

    if (echoArgs)
    {
      System.err.flush();
      System.err.println("Exiting with status : " + status);
      System.err.flush();
    }

    return 0;
  }

  /**
   * Reads data from the input stream, cumulating it in the string builder, and
   * returning status.
   *
   * @param data
   *          the cumulated output data.
   * @param srcStream
   *          the input source data.
   * @param inputLabel
   *          the label associated with the input stream
   *
   * @return {@code true} if stream input successfully captured.
   */
  public static boolean cumulateFile(final StringBuilder data,
      final InputStream srcStream, final String inputLabel)
  {
    if (data == null)
      throw new IllegalArgumentException("data null");

    if (srcStream == null)
      throw new IllegalArgumentException("srcStream null");

    if (inputLabel == null)
      throw new IllegalArgumentException("inputLabel null");

    if (inputLabel.isEmpty())
      throw new IllegalArgumentException("inputLabel empty");

    boolean status = true;

    try (InputStreamReader isr = new InputStreamReader(srcStream); BufferedReader br = new BufferedReader(isr, 1024))
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
          final String msg = " **** Input file " + inputLabel
              + " failed during read of line " + count + ",\n      {"
              + ex.getClass().getSimpleName() + "};  Msg: " + ex.getMessage();
          System.err.println(ERR_FLAG + msg);
        }

        if (line != null)
          data.append(line);
      }
      while (line != null);
    }
    catch (IOException ioEx)
    {
      status = false;
      final String msg = " **** Input file " + inputLabel
          + " failed during close \n      {"
          + ioEx.getClass().getSimpleName() + "};  Msg: " + ioEx.getMessage();
      System.err.println(ERR_FLAG + msg);
    }

    return status;
  }

  // ---------------------------------------------------------------------------

  private String processInvoice()
  {
    final StringBuilder extracted = new StringBuilder();

    StringBuilder fileData = new StringBuilder();
    boolean status = cumulateFile(fileData, inputFile, inputFileName);
    if (status)
    {
      if (fileData.length() < 1)
      {
        final String msg = " **** Input file " + inputFileName + " empty";
        System.err.flush();
        System.err.println(ERR_FLAG + msg);
        System.err.flush();
        fileData.append(msg);
      }
      else
        extractInvoiceDetails(inputFileName, fileData.toString(), extracted);
    }

    fileData = null;

    return extracted.toString();
  }

  private void extractInvoiceDetails(final String srcFileName,
      final String info, final StringBuilder data)
  {
    if (srcFileName == null || srcFileName.isEmpty())
      throw new IllegalArgumentException("srcFileName null or empty");

    final String fileName = AbstractExtractStrategy.removeProblems(srcFileName);

    // data.append(ExtractStrategy.QUOTE);

    final int maxNameLength = 25;
    if (fileName.length() < maxNameLength)
      data.append(fileName);
    else
      data.append("..." + fileName.substring(fileName.length() - maxNameLength));

    // data.append(ExtractStrategy.QUOTE);

    data.append(ExtractStrategy.SEPERATOR);
    strategy.extractDetails(fileName, info, data, 0);
  }

  private boolean setupRun()
  {
    if (inputFileName == null || inputFileName.isEmpty())
    {
      System.err.println("\n **** input file name null or empty!");
      return false;
    }

    if (outputFileName == null || outputFileName.isEmpty())
    {
      System.err.println("\n **** Output file name null or empty!");
      return false;
    }

    boolean status = true;

    if (!SYSIN.equalsIgnoreCase(inputFileName.trim()))
    {
      File f = new File(inputFileName);
      try
      {
        inputFile = new FileInputStream(f);
      }
      catch (FileNotFoundException ex)
      {
        System.err.println("\n **** Input file " + f.getPath()
            + " not found,\n      " + ex.getMessage());
        return false;
      }
    }
    else
      inputFile = System.in;

    status &= (inputFile != null);

    if (!SYSOUT.equalsIgnoreCase(outputFileName.trim()))
    {
      try
      {
        outputFile = new PrintStream(new FileOutputStream(outputFileName), true);
      }
      catch (FileNotFoundException ex)
      {
        System.err.println("\n **** Output file " + outputFileName
            + " not created,\n" + ex.getMessage());
      }
    }
    else
      outputFile = System.out;

    status &= (outputFile != null);

    if (runStrategy == null || runStrategy.isEmpty())
    {
      System.err.println("\n **** run strategy null or empty");
      return false;
    }

    strategy = strategies.get(runStrategy);
    if (strategy == null)
    {
      System.err.println("\n **** unrecognized run strategy '" + runStrategy
          + "'");
      return false;
    }

    return status;
  }

  private void cleanup()
  {
    if (inputFile != null)
      try
      {
        inputFile.close();
      }
      catch (IOException ignore)
      {
        // Ignore
      }

    if (outputFile != null)
      outputFile.close();
  }
}
