/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.dupcheck;

import java.util.List;

import demo.dupcheck.domain.DataMetricsBean;
import demo.dupcheck.domain.DataRow;
import demo.dupcheck.domain.DupChecker;
import demo.dupcheck.domain.DupDataReader;
import demo.dupcheck.impl.SimpleDupChecker;
import demo.dupcheck.impl.StubDataReader;

/**
 * This program reports on data quality in a text file representing a logical
 * table. It reads a text file with rows of data, where each line of the file is
 * a logical row and multiple column values are separated from each other as
 * required by the file format. One supported file format is CSV. It then
 * computes the required reports.
 * <p>
 * The program operates in memory and executes these three phases:
 * <ol>
 * <li>Read in column data, first row are labels</li>
 * <li>Process data to accumulate counts for reports (uses a
 * <code>DupChecker</code> implementation)</li>
 * <li>print reports as required from the accumulated data</li>
 * </ol>
 * The program is packaged as a runnable Jar and contains supporting libraries.
 * Reports are triggered by program parameters and are optional. The report
 * definitions and program (command line) options to trigger them are:
 * <ul>
 * <li><strong>-f</strong> Defines the input file path to check.</li>
 * <li><strong>-l</strong> Display a formatted listing of the input data.</li>
 * <li><strong>-s</strong> Display a Summary Report that prints the null,
 * complete, incomplete, and duplicate row-column combinations.
 * </ul>
 * <strong>Note these conceptual definitions:</strong>
 * <ul>
 * <li>A <em>null</em> column is actually null or zero length</li>
 * <li>A <em>complete</em> column has 2 or more characters, fewer than 2 are
 * incomplete.</li>
 * <li>
 * Any two non-null columns are <em>duplicates</em> if their values are the same
 * in the same row.</li>
 * </ul>
 * 
 * @author Donald Trummell
 */
public class DupCheck
{
  private static final String hdr = "************************";
  private String inputFilePath = "dummy";
  private boolean printHdrTrl = true;
  private boolean printInput = true;
  private boolean printReport = true;
  private boolean printSummary = true;

  private DupDataReader ddr = new StubDataReader();
  private DupChecker ddc = new SimpleDupChecker();

  public DupCheck()
  {
  }

  /**
   * Read data, process quality, and print reports. The report definition
   * parameters are above, and the file input may be defined by an additional
   * parameter
   * 
   * @param args
   *          the command line arguments
   */
  public static void main(final String[] args)
  {
    final DupCheck dupcheck = new DupCheck();
    dupcheck.process(args);
  }

  public DataMetricsBean process(final String[] args)
  {
    if (printHdrTrl)
      showHdr();

    if (args != null)
      parseParams(args);

    if (printHdrTrl)
      showArgs();

    loadInput();

    final DataMetricsBean metrics = checkInput();

    if (printReport)
      report(metrics);

    if (printHdrTrl)
      showTrailer();

    return metrics;
  }

  private void report(final DataMetricsBean metrics)
  {
    System.out.println("\n-------------------------");
    System.out.println("-- Data Quality Report --");
    System.out.println("-------------------------");

    System.out.println("  Values:\n" + String.valueOf(metrics));
    System.out.println("-------------------------");
  }

  private DataMetricsBean checkInput()
  {
    return ddc.checkData(ddr.getData());
  }

  private void loadInput()
  {
    if (!ddr.loadData())
      throw new IllegalStateException("error loading data");

    if (printInput)
      listInput();
  }

  private void listInput()
  {
    final String lead = "*** ";
    System.out.println("\n" + lead + ddr.getLinesRead() + " lines read from "
        + inputFilePath);
    final List<DataRow> data = ddr.getData();
    System.out.println(lead + "List " + data.size() + " rows");
    for (final DataRow dr : data)
    {
      System.out.print(dr.getRowIndex() + ". ");
      System.out.println(dr.getData());
    }
  }

  private void showArgs()
  {
    System.out.println("  Input file path    : " + inputFilePath);
    System.out.println("  List Header/Trailer: " + printHdrTrl);
    System.out.println("  List input         : " + printInput);
    System.out.println("  List Report        : " + printReport);
    System.out.println("  List Summary       : " + printSummary);
  }

  /**
   * Parse the command line arguments.
   * 
   * <ul>
   * <li>-f {input-file-path} - File path of the input data.</li>
   * <li>-l Display a formatted listing of the input data.</li>
   * <li>-s Display a Summary Report that prints the null, complete, incomplete,
   * and duplicate row-column combinations.
   * </ul>
   * 
   * @param args
   *          the command-line parameters
   */
  private void parseParams(final String[] args)
  {
    boolean onCmd = true;
    String cmd = null;
    String value = null;
    for (int i = 0; i < args.length; i++)
    {
      if (onCmd)
      {
        //
        // Process the command specification part

        cmd = args[i].trim();
        if (cmd.isEmpty())
          throw new IllegalArgumentException("empty command part");
        if (!cmd.startsWith("-"))
          throw new IllegalArgumentException("missing -");
        if (cmd.length() < 2)
          throw new IllegalArgumentException("empty command part");
        cmd = cmd.substring(1);

        if (!(cmd.startsWith("f") || cmd.startsWith("l") || cmd.startsWith("s")))
          throw new IllegalArgumentException("unrecognized command " + cmd);

        if (cmd.startsWith("f"))
        {
          onCmd = false;
        }
        else if (cmd.startsWith("l"))
        {
          printInput = true;
        }
        else if (cmd.startsWith("s"))
        {
          printSummary = true;
        }
        else
          onCmd = false;
      }
      else
      {
        //
        // Process the value part

        value = args[i].trim();
        if (value.isEmpty())
          throw new IllegalArgumentException("empty command value for " + cmd);

        if (cmd.equals("f"))
        {
          inputFilePath = value;
        }
        else
          throw new IllegalArgumentException("unrecognized command " + cmd);

        onCmd = true;
      }
    }

  }

  private static void showHdr()
  {
    System.out.println();
    System.out.println(hdr);
    System.out.println("Summarize Data Quality");
    System.out.println(hdr);
  }

  private static void showTrailer()
  {
    System.out.println();
    System.out.println(hdr);
    System.out.println("Data Quality Summarized");
    System.out.println(hdr);
  }

  // ---------------------------------------------------------------------------

  public String getInputFilePath()
  {
    return inputFilePath;
  }

  public boolean isPrintHdrTrl()
  {
    return printHdrTrl;
  }

  public boolean isPrintInput()
  {
    return printInput;
  }

  public boolean isPrintReport()
  {
    return printReport;
  }

  public boolean isPrintSummary()
  {
    return printSummary;
  }

  public DupDataReader getDdr()
  {
    return ddr;
  }

  public void setDdr(DupDataReader ddr)
  {
    this.ddr = ddr;
  }

  public DupChecker getDdc()
  {
    return ddc;
  }

  public void setDdc(final DupChecker ddc)
  {
    this.ddc = ddc;
  }

  public void setInputFilePath(final String inputFilePath)
  {
    this.inputFilePath = inputFilePath;
  }

  public void setPrintHdrTrl(boolean printHdrTrl)
  {
    this.printHdrTrl = printHdrTrl;
  }

  public void setPrintInput(final boolean printInput)
  {
    this.printInput = printInput;
  }

  public void setPrintReport(boolean printReport)
  {
    this.printReport = printReport;
  }

  public void setPrintSummary(final boolean printSummary)
  {
    this.printSummary = printSummary;
  }
}
