package demo.granular.sudoku.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import demo.granular.sudoku.GameChecker;
import demo.granular.sudoku.impl.MultiPassChecker;
import demo.granular.sudoku.impl.SampleData;

public class MultiPassCheckerTest {

	private GameChecker checker = null;

	@Before
	public void setUp() throws Exception {
		checker = new MultiPassChecker();
	}

	@After
	public void tearDown() throws Exception {
		checker = null;
	}

	@Test
	public void test_multipass_checker_on_valid() {
		Assert.assertTrue("Multipass checker on invalid row failed", checker.isSolution(SampleData.validPuzzle));
	}

	@Test
	public void test_multipass_checker_on_invalid_row() {
		Assert.assertFalse("Multipass checker on invalid row failed", checker.isSolution(SampleData.invalidDupRow8));
	}

	@Test
	public void test_multipass_checker_on_invalid_col() {
		Assert.assertFalse("Multipass checker on invalid col failed", checker.isSolution(SampleData.invalidDupCol2));
	}

	@Test
	public void test_multipass_checker_on_invalid_grid() {
		Assert.assertFalse("Multipass checker on invalid grid failed", checker.isSolution(SampleData.invalidSubGrid));
	}

	@Test
	public void test_multipass_checker_on_invalid_digit() {
		Assert.assertFalse("Multipass checker on invalid digit failed", checker.isSolution(SampleData.invalidSubGrid));
	}
}
