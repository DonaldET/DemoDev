package demo.don.system.util.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.system.util.EnvLister;

/*
 * Copyright (c) 2018. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
/**
 * Verify some of the extracted property and environment values
 * 
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class EnvListerTest
{
  @Before
  public void setUp() throws Exception
  {
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public void testGetEnvVars()
  {
		final String vars = EnvLister.getEnvVars();
    Assert.assertNotNull("env vars null", vars);
    Assert.assertFalse("env vars empty", vars.isEmpty());
    final String needed = "JAVA_HOME";
    Assert.assertTrue(needed + " not defined", vars.contains(needed));
  }

  @Test
  public void testGetProperties()
  {
    final String props = EnvLister.getProperties();
    Assert.assertNotNull("properties null", props);
    Assert.assertFalse("properties empty", props.isEmpty());
    final String needed = "user.home";
    Assert.assertTrue(needed + " not defined", props.contains(needed));
  }
}
