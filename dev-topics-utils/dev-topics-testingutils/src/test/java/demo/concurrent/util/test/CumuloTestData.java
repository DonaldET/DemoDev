package demo.concurrent.util.test;

public class CumuloTestData implements Runnable
{
  private final int id;
  private final int[] cumulator;
  private final boolean sync;

  public CumuloTestData(final int id, final int[] cumulator, final boolean sync)
  {
    this.id = id;
    this.cumulator = cumulator;
    this.sync = sync;
  }

  @Override
  public void run()
  {
    try
    {
      Thread.sleep(Math.round(Math.random() * 5.0));
    }
    catch (final InterruptedException ignore)
    {
      System.err.println("\n  **** Sleep interupted for "
          + Thread.currentThread().getName());
    }

    if (sync)
      synchronized (cumulator)
      {
        cumulator[id]++;
      }
    else
      cumulator[id]++;
  }
}