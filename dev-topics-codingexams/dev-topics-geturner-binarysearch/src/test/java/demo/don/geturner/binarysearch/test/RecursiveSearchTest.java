package demo.don.geturner.binarysearch.test;

import org.junit.Before;

import demo.geturner.binarysearch.impl.RecursiveSearchImpl;

public class RecursiveSearchTest extends BinarySearchChecker
{
  @Override
  @Before
  public void setUp() throws Exception
  {
    search = new RecursiveSearchImpl<Integer>();
  }
}
