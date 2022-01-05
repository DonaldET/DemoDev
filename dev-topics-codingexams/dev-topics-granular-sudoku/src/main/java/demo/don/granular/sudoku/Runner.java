package demo.don.granular.sudoku;

import demo.don.granular.sudoku.impl.MultiPassChecker;
import demo.don.granular.sudoku.impl.SampleData;
import demo.don.granular.sudoku.impl.SinglePassChecker;

public class Runner {

	public static void main(String[] args) {
		System.out.println("\nSudoku checker (" + GameChecker.SD_ROWS + " rows by " + GameChecker.SD_COLS
				+ " cols with " + GameChecker.SD_SUBGRIDS + " subgrids)");

		GameChecker checker = new MultiPassChecker();
		System.out.println("\nMultipass checker on valid        : " + checker.isSolution(SampleData.validPuzzle));
		System.out.println("Multipass checker on invalid row  : " + checker.isSolution(SampleData.invalidDupRow8));
		System.out.println("Multipass checker on invalid col  : " + checker.isSolution(SampleData.invalidDupCol2));
		System.out.println("Multipass checker on invalid grid : " + checker.isSolution(SampleData.invalidSubGrid));
		System.out.println("Multipass checker on invalid digit: " + checker.isSolution(SampleData.invalidSubGrid));

		checker = new SinglePassChecker();
		System.out.println("\nSinglepass checker on valid        : " + checker.isSolution(SampleData.validPuzzle));
		System.out.println("Singlepass checker on invalid row  : " + checker.isSolution(SampleData.invalidDupRow8));
		System.out.println("Singlepass checker on invalid col  : " + checker.isSolution(SampleData.invalidDupCol2));
		System.out.println("Singlepass checker on invalid grid : " + checker.isSolution(SampleData.invalidSubGrid));
		System.out.println("Singlepass checker on invalid digit: " + checker.isSolution(SampleData.invalidSubGrid));
	}
}
