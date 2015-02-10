package demo.scanner;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.scanner.api.Game;
import demo.scanner.api.GameDataBuilder;
import demo.scanner.api.GameDataBuilder.ListOrder;
import demo.scanner.api.TopXScanner;
import demo.scanner.impl.QueueScanner;
import demo.scanner.impl.SortedArrayScanner;

/**
 * Compares the performance of <em>priority queue</em> and <em>sorted array</em>
 * based scanners on "<em>top X</em>" queries across a random collection of
 * integer instances. Integer instances have an ascending comparator that allows
 * the instances to be ordered on value.
 * <p>
 * This project compares several techniques to capture the <em>top x</em> values
 * from a large list of integer elements. Four implementations are compared for
 * performance in an application; a binary search approach; a niave searcher, a
 * radix searcher, and a <code>TreeMap</code> searcher. This application is
 * related to the TopX application in that both compare methods of capturing the
 * <em>top or bottom x</em> elements of a list, but the TopX implementation
 * concentrates on the comparison with the binary search approaches and this
 * application compares several techniques in less detail.
 * <p>
 * This example uses the motivation of finding the (say) top 10 scores of a
 * complex <em>game</em> where several game attributes allow for unique
 * ordering. We vary the number of inputs to scan and the size of the
 * <em>top</em> set. Since 100 elements is a large list, that is our upper limit
 * for this performance test.
 *
 * @author Donald Trummell
 */
public class ScannerTimer
{
  public static final String BEAN_TEST_CONTEXT = "queueTimingTestContext.xml";

  private static final long[] blockSizes = { 2l, 5l, 10l, 25l, 50l, 64l, 128l,
      256l, 512l };

  private static final String QUEUE_SCANNER_BEAN_NAME = "test.queue.scanner";
  private static final String ARRAY_SCANNER_BEAN_NAME = "test.array.scanner";
  private static final String TEST_BASE_TIME = "test.base.time";
  private static final String GAME_DATA_BUILDER = "test.game.data.builder";

  private static ApplicationContext queueContext;

  private GameDataBuilder dataBuilder;
  private Long baseDate;
  private TopXScanner arrayScanner, queueScanner;

  public ScannerTimer()
  {
    queueContext = new ClassPathXmlApplicationContext(BEAN_TEST_CONTEXT);
  }

  public static void main(final String[] args)
  {
    System.out.println("\n==== Test and Compare Scanner Performance ====\n");

    final ScannerTimer st = new ScannerTimer();
    st.runTimming(true, 10);
    st.runTimming(true, 25);
    st.runTimming(true, 50);
    st.runTimming(true, 75);
    st.runTimming(true, 100);

    System.out.println("\n==== Test COMPLETE                        ====");
  }

  public void runTimming(final boolean display, final int x)
  {
    if (x < 1)
      throw new IllegalArgumentException("x too small, " + x);

    if (display)
      System.out.println("\n-+-+-+- Find top " + x);

    setup();

    warmup(x);

    int arrayWins = 0;
    int queueWins = 0;
    int ties = 0;
    for (final long bs : blockSizes)
    {
      final long elapsedArray = performTest(display, bs * 1024l, x,
          arrayScanner);
      final long elapsedQueue = performTest(display, bs * 1024l, x,
          queueScanner);
      if (elapsedArray > elapsedQueue)
        queueWins++;
      else if (elapsedQueue > elapsedArray)
        arrayWins++;
      else
        ties++;
    }

    if (display)
      System.out.println("---- [Array Wins: " + arrayWins + ";  Queue Wins: "
          + queueWins + ";  Ties : " + ties + "] ----");

    cleanup();
  }

  private void setup()
  {
    dataBuilder = queueContext
        .getBean(GAME_DATA_BUILDER, GameDataBuilder.class);
    baseDate = queueContext.getBean(TEST_BASE_TIME, java.lang.Long.class);
    queueScanner = queueContext.getBean(QUEUE_SCANNER_BEAN_NAME,
        QueueScanner.class);
    arrayScanner = queueContext.getBean(ARRAY_SCANNER_BEAN_NAME,
        SortedArrayScanner.class);
  }

  private void warmup(final int x)
  {
    // System.out.println(" *** Warming up run " + x + "!");
    performTest(false, 1024l, x, arrayScanner);
    performTest(false, 1024l, x, queueScanner);
  }

  private void cleanup()
  {
    dataBuilder = null;
    baseDate = null;
    queueScanner = null;
  }

  // ---------------------------------------------------------------------------

  private long performTest(final boolean display, final long nCases,
      final int x, final TopXScanner scanner2Test)
  {
    final String label = String.format("%15.15s", scanner2Test.getClass()
        .getSimpleName());

    if (display)
      System.out.print("---- " + label + " > Starting,  N: "
          + String.format("%5d", nCases) + " . . . ");

    dataBuilder.initialize(nCases, baseDate);
    final List<Game> generatedData = dataBuilder.getData(ListOrder.unordered);

    final long start = System.currentTimeMillis();
    final List<Game> topList = scanner2Test.topX(x, generatedData.iterator());
    final long elapsed = System.currentTimeMillis() - start;

    dataBuilder.destroy();

    if (display)
      System.out.println("Elapsed: " + String.format("%3d", elapsed)
          + " msec;  Per: "
          + String.format("%4.3f", scale(1000.0 * elapsed / nCases, 6))
          + " usec");

    checkTest(label, x, topList);

    return elapsed;
  }

  private void checkTest(final String label, final int x,
      final List<Game> topList)
  {
    if (topList == null)
      throw new IllegalStateException(label + " topList null");

    if (x != topList.size())
      throw new IllegalStateException(label
          + " topList size differs, expected " + x + ", got " + topList.size());
  }

  // ---------------------------------------------------------------------------

  private static double scale(final double x, final int d)
  {
    final double f = Math.pow(10.0, d);
    final double v = Math.abs(x);
    final double vs = Math.rint(v * f + 0.5) / f;

    return x < 0 ? -vs : vs;
  }
}
