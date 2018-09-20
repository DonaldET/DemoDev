package demo.granular.sudoku.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.granular.sudoku.GameChecker;
import demo.granular.sudoku.impl.SampleData;
import demo.granular.sudoku.impl.SinglePassChecker;

public class SinglePassCheckerTest {

	private GameChecker checker = null;

	@Before
	public void setUp() throws Exception {
		checker = new SinglePassChecker();
	}

	@After
	public void tearDown() throws Exception {
		checker = null;
	}

	@Test
	public void test_singlepass_checker_on_valid() {
		Assert.assertTrue("Singlepass checker on invalid row failed", checker.isSolution(SampleData.validPuzzle));
	}

	@Test
	public void test_singlepass_checker_on_invalid_row() {
		Assert.assertFalse("Singlepass checker on invalid row failed", checker.isSolution(SampleData.invalidDupRow8));
	}

	@Test
	public void test_singlepass_checker_on_invalid_col() {
		Assert.assertFalse("Singlepass checker on invalid col failed", checker.isSolution(SampleData.invalidDupCol2));
	}

	@Test
	public void test_singlepass_checker_on_invalid_grid() {
		Assert.assertFalse("Singlepass checker on invalid grid failed", checker.isSolution(SampleData.invalidSubGrid));
	}

	@Test
	public void test_singlepass_checker_on_invalid_digit() {
		Assert.assertFalse("Singlepass checker on invalid digit failed", checker.isSolution(SampleData.invalidSubGrid));
	}
}
