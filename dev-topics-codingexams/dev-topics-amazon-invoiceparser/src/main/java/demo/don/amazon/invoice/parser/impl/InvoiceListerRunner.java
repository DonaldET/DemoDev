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

/**
 * Parse command-line parameters and delegate parsing and extraction to the
 * named invoice lister.
 * 
 * @author Donald Trummell
 */
public class InvoiceListerRunner
{
  private boolean echoArgs = false;
  private String inputFileName = InvoiceLister.SYSIN;
  private String outputFileName = InvoiceLister.SYSOUT;
  private String runStrategy = InvoiceLister.DEFAULT_STRATEGY;

  private InvoiceLister lister = new InvoiceLister();

  public InvoiceListerRunner()
  {
  }

  public static void main(final String[] args)
  {
    final InvoiceListerRunner runner = new InvoiceListerRunner();
    if (!runner.parseArgs(args))
      runner.usage_err_exit("**** Argument parse failed!");

    final int status = runner.lister.execute(runner.inputFileName,
        runner.outputFileName, runner.runStrategy);

    if (status != 0)
      System.exit(status);
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
            break;

          case "o":
            outputFileName = argv;
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
