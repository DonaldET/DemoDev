package demo.don.amazon.invoice.parser.impl;

/**
 * 
 * @author Donald Trummell
 */
public class InvoiceListerRunner
{
  private InvoiceLister lister = new InvoiceLister();

  public InvoiceListerRunner()
  {
  }

  public static void main(final String[] args)
  {
    final InvoiceListerRunner runner = new InvoiceListerRunner();
    final int status = runner.lister.execute(args);
    if (status != 0)
      System.exit(status);
  }
}
