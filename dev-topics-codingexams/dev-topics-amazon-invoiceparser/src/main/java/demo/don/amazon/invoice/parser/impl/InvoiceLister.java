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
 * Development data files in resources for 2012:
 * 
 * <pre>
 * <code>
 * Amazon.com  Digital Order Summary.htm
 * Amazon.com  Digital Order Summary10.htm
 * Amazon.com  Digital Order Summary13.htm
 * Amazon.com - Order 103-4039722-7521802.htm
 * </code>
 * </pre>
 * 
 * @author Donald Trummell
 */
public class InvoiceLister
{
  private static final String DEFAULT_STRATEGY = "2012";
  private static final String ERR_FLAG = "ERR: ";

  private String inputFileName = "Sysin";
  private String outputFileName = "Sysout";

  private boolean echoArgs = false;
  private InputStream inputFile = System.in;
  private PrintStream outputFile = System.out;
  private String runStrategy = DEFAULT_STRATEGY;

  private final Map<String, ExtractStrategy> strategies = new HashMap<String, ExtractStrategy>();
  private ExtractStrategy strategy = null;

  public InvoiceLister()
  {
    strategies.put(DEFAULT_STRATEGY, new ExtractStrategy2012());
  }

  public int execute(final String[] args)
  {
    if (!parseArgs(args))
    {
      System.err.println(" **** FAILED: arguments incorrect!");
      return 1;
    }

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

  private String processInvoice()
  {
    final StringBuilder extracted = new StringBuilder();

    StringBuilder fileData = new StringBuilder();
    boolean status = cumulateFile(fileData);
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

  private boolean cumulateFile(final StringBuilder data)
  {
    boolean status = true;

    InputStreamReader isr = null;
    BufferedReader br = null;
    try
    {
      isr = new InputStreamReader(inputFile);
      br = new BufferedReader(isr, 1024);
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
          final String msg = " **** Input file " + inputFileName
              + " failed during read of line " + count + ",\n      {"
              + ex.getClass().getSimpleName() + "};  Msg: " + ex.getMessage();
          System.err.println(ERR_FLAG + msg);
        }

        if (line != null)
          data.append(line);
      }
      while (line != null);
    }
    finally
    {
      if (br != null)
        try
        {
          br.close();
        }
        catch (IOException ignore)
        {
          // Ignore
        }
      else if (isr != null)
        try
        {
          isr.close();
        }
        catch (IOException ignore)
        {
          // Ignore
        }
    }

    return status;
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

    if (inputFile == null)
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

      status &= (inputFile != null);
    }

    if (outputFileName == null)
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

      status &= (outputFile != null);
    }

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

  private boolean parseArgs(final String[] args)
  {
    boolean parsed = parseArgList(args);

    if (echoArgs)
      echoParsedArgs();

    return parsed;
  }

  private boolean parseArgList(final String[] args)
  {
    int nargs = args.length;
    if (nargs > 0)
    {
      if (2 * (nargs / 2) != nargs)
        usage_err_exit("  odd arg count: " + nargs);

      for (int i = 0; i < nargs; i += 2)
      {
        String argId = args[i];
        if (argId == null || argId.isEmpty())
          usage_err_exit("  arg[" + i + "] null or empty");
        if (!argId.startsWith("-"))
          usage_err_exit("  arg[" + i + "] has no dash, " + argId);
        argId = argId.substring(1).trim();
        if (argId.isEmpty())
          usage_err_exit("  arg[" + i + "] has no key, " + argId);

        String argv = args[i + 1];
        if (argv == null || argv.isEmpty())
          usage_err_exit("  value[" + i + "] null or empty");
        argv = argv.trim();

        switch (argId)
        {
          case "e":
            echoArgs = Boolean.parseBoolean(argv);
            break;

          case "i":
            inputFileName = argv;
            inputFile = null;
            break;

          case "o":
            outputFileName = argv;
            outputFile = null;
            break;

          case "s":
            runStrategy = argv;
            break;

          default:
            usage_err_exit("  arg[" + i + "] is not a recognized identifier, "
                + argId);
            break;
        }
      }
    }

    return true;
  }

  private void echoParsedArgs()
  {
    System.err.flush();
    System.err.println("\nInvoice Lister");
    System.err.println("  - echo arguments     : " + echoArgs);
    System.err.println("  - input file name    : " + inputFileName);
    System.err.println("  - output file name   : " + outputFileName);
    System.err.println("  - extraction strategy: " + runStrategy);
    System.err.flush();
  }

  private void usage_err_exit(final String msg)
  {
    System.err.flush();
    System.err.println("\n **** USAGE ERROR: " + msg);
    System.err.println("\nCorrest usage is:\n");
    System.err.println("  InvoiceLister");
    System.err.println("\t-e<echo>    : if true, echo arguments");
    System.err.println("\t-i<input>   : input file name");
    System.err.println("\t-o<output>  : output file name");
    System.err.println("\t-s<strategy>: detail extraction strategy");
    System.err.flush();

    System.exit(1);
  }
}
