/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.serialize.hack.test;

import java.io.Serializable;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.serialize.hack.HackUtil;
import demo.serialize.hack.HackerSample.Token;
import demo.serialize.hack.HackerSample.TokenFactory;

public class HackerTest
{
  private static final int INITIAL_SEED = 1;
  private static final long INJECTED_KEY = 16969691;

  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public void testPrimeInjection()
  {
    final Token token = TokenFactory.createToken(INITIAL_SEED);
    final long markerKey = token.getKey();
    Assert.assertEquals("unexpected marker key value",
        TokenFactory.createKey(INITIAL_SEED), markerKey);

    final long newKey = INJECTED_KEY;
    Assert.assertTrue("injected key not different", newKey != markerKey);

    final Serializable hackedToken = HackUtil.injectLongKey(markerKey, newKey,
        token);
    Assert.assertNotNull("injection null", hackedToken);
    Assert.assertTrue("unexpected class for hackedToken, "
        + hackedToken.getClass().getName(), hackedToken instanceof Token);
    final long injectedKey = ((Token) hackedToken).getKey();
    Assert.assertEquals("unexpected injected key", newKey, injectedKey);
  }
}
