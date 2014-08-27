package demo.don.geturner.binarysearch.test;

import org.junit.Before;

import demo.geturner.binarysearch.impl.IterativeSearchImpl;

public class IterativeSearchTest extends BinarySearchChecker
{
  @Override
  @Before
  public void setUp() throws Exception
  {
    search = new IterativeSearchImpl<Integer>();
  }
}
