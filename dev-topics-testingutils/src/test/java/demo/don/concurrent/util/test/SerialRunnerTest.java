/*
 Copyright (c) 2014. Donald Trummell. All Rights Reserved.
 Permission to use, copy, modify, and distribute this software and its documentation
 for educational, research, and not-for-profit purposes, without fee and without
 a signed licensing agreement, is hereby granted, provided that the above
 copyright notice, and this paragraph, appear in all copies, modifications, and
 distributions. Contact dtrummell@gmail.com for commercial licensing opportunities.
 */
package demo.don.concurrent.util.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.concurrent.util.SerialRunner;

public class SerialRunnerTest {
	private static final boolean TRACE_STATE = false;
	private SerialRunner runner;

	private static final int NUM_TEST_IDS = 10;
	private int[] cumulator = new int[NUM_TEST_IDS];

	@Before
	public void setUp() throws Exception {
		runner = new SerialRunner();
		runner.setTrace(TRACE_STATE);
		for (int i = 0; i < cumulator.length; i++)
			cumulator[i] = 0;
	}

	@After
	public void tearDown() throws Exception {
		runner = null;
	}

	@Test
	public void testInitialState() {
		Assert.assertTrue("invalid default trace", runner.isTrace() == TRACE_STATE);
	}

	@Test
	public void testCumulate() {
		// runner.setTrace(true);

		final List<Runnable> runnables = new ArrayList<Runnable>();
		for (int id = 0; id < NUM_TEST_IDS; id++)
			runnables.add(new CumuloTestData(id, cumulator, false));

		final String label = "Cumulate Test of " + NUM_TEST_IDS + " instances";
		setupCheckRun(label, runnables);

		final int[] expectedCounts = new int[NUM_TEST_IDS];
		for (int id = 0; id < NUM_TEST_IDS; id++)
			expectedCounts[id] = 1;
		checkCounts(expectedCounts);
	}

	@Test
	public void testMultiCumulate() {
		// runner.setTrace(true);

		final List<Integer> ids = new ArrayList<Integer>();
		final int totalTestInstances = (NUM_TEST_IDS * (NUM_TEST_IDS + 1)) / 2;
		for (int id = 0; id < NUM_TEST_IDS; id++)
			for (int i = 1; i <= (id + 1); i++)
				ids.add(id);
		Assert.assertEquals("cumulation counts differ", totalTestInstances, ids.size());
		Collections.shuffle(ids);

		final List<Runnable> runnables = new ArrayList<Runnable>();
		for (final Integer id : ids)
			runnables.add(new CumuloTestData(id, cumulator, false));

		final String label = "Cumulate Test of " + totalTestInstances + " instances";
		setupCheckRun(label, runnables);

		final int[] expectedCounts = new int[NUM_TEST_IDS];
		for (int id = 0; id < NUM_TEST_IDS; id++)
			expectedCounts[id] = (id + 1);
		checkCounts(expectedCounts);
	}

	// ---------------------------------------------------------------------------

	private void setupCheckRun(final String label, final List<Runnable> runnables) {
		final int maxTimeoutSeconds = 1;
		List<Throwable> errors = null;
		errors = runner.launchRunnables(label, runnables, maxTimeoutSeconds);

		Assert.assertNotNull("errors null", errors);
		if (!errors.isEmpty()) {
			System.err.println("Run failed with " + errors.size() + " errors:");
			int i = 0;
			for (final Throwable th : errors)
				System.err.println("  " + (i++) + ". " + th.getMessage());
			Assert.assertTrue("errors not empty", errors.isEmpty());
		}
	}

	private void checkCounts(final int[] expectedCounts) {
		int diffCount = 0;
		for (int id = 0; id < NUM_TEST_IDS; id++) {
			final int expectedCount = expectedCounts[id];
			final int actualCount = cumulator[id];
			if (actualCount != expectedCount) {
				System.err.println(
						"Instance " + id + " count differs -- expected: " + expectedCount + ";  had: " + actualCount);
				diffCount++;
			}
		}
		Assert.assertEquals("Instance cumulation counts differ", 0, diffCount);
	}
}
