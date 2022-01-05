/*
 * Copyright (c) 2014. Donald Trummell. All Rights Reserved. Permission to use,
 * copy, modify, and distribute this software and its documentation for
 * educational, research, and not-for-profit purposes, without fee and without a
 * signed licensing agreement, is hereby granted, provided that the above
 * copyright notice, and this paragraph, appear in all copies, modifications,
 * and distributions. Contact dtrummell@gmail.com for commercial licensing
 * opportunities.
 */
package demo.don.dupcheck.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.don.dupcheck.DupCheck;
import demo.don.dupcheck.domain.DataMetricsBean;
import demo.don.dupcheck.impl.StubDataReader;

/**
 * Test against the build-in <code>StubDataReader</code> with artificial data.
 *
 * @author Donald Trummell (dtrummell@gmail.com)
 */
public class DupCheckTest {
	private DupCheck checker = null;

	@Before
	public void setUp() throws Exception {
		checker = new DupCheck();
		checker.setInputFilePath("unit test");
		checker.setPrintHdrTrl(false);
		checker.setPrintInput(false);
		checker.setPrintReport(false);
		checker.setPrintSummary(false);
		checker.setDdr(new StubDataReader());
	}

	@After
	public void tearDown() throws Exception {
		checker = null;
	}

	/**
	 * <pre>
	 *  complete:   (9) [00001.Col1, 00001.Col2, 00003.Col1, 00003.Col3, 00004.Col1, 00004.Col2, 00005.Col1, 00005.Col2, 00005.Col3];
	 *  incomplete: (6) [00001.Col3, 00002.Col1, 00002.Col2, 00002.Col3, 00003.Col2, 00004.Col3];
	 *  duplicate   (2) [00004.*, 00005.*];
	 *  nulls:      (2) [00003.Col1, 00005.Col1]]
	 * </pre>
	 */
	@Test
	public void testProcess() {
		final DataMetricsBean stats = checker.process(null);
		Assert.assertNotNull("no scanning results found", stats);
		Assert.assertEquals("complete differs", 9, stats.getComplete().size());
		Assert.assertEquals("incomplete differs", 6, stats.getIncomplete().size());
		Assert.assertEquals("duplicate differs", 2, stats.getDuplicate().size());
		Assert.assertEquals("nulls differs", 2, stats.getNullEntries().size());
	}
}
