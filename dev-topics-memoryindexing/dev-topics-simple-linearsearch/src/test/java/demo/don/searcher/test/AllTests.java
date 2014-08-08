package demo.don.searcher.test;

import junit.framework.Test;
import junit.framework.TestSuite;
import demo.don.searcher.databuilder.test.TestNameBuilder;
import demo.don.searcher.index.test.TestIndexHelper;
import demo.don.searcher.match.test.TestStringIndexSearcherImpl;

public class AllTests
{
  public static Test suite()
  {
    TestSuite suite = new TestSuite(AllTests.class.getName());
    // $JUnit-BEGIN$
    suite.addTestSuite(TestIndexHelper.class);
    suite.addTestSuite(TestNameBuilder.class);
    suite.addTestSuite(TestStringIndexSearcherImpl.class);
    // $JUnit-END$
    return suite;
  }
}
