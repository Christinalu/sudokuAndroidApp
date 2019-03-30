package com.omicron.android.cmpt276_1191e1_omicron;

import static org.junit.Assert.*;

public class SudokuGeneratorTest {
    public void sudokuGenerator() {
        //test when Puzzle is completed correctly
        /*
        int[][] sudokuArray = {{6, 7, 3, 4, 9, 1, 2, 5, 8}, {4, 8, 2, 5, 7, 3, 6, 9, 1}, {9, 1, 5, 6, 2, 8, 4, 3, 7}, {8, 2, 9, 1, 3, 7, 5, 4, 6}, {3, 4, 1, 2, 6, 5, 8, 7, 9}, {5, 6, 7, 8, 4, 9, 1, 2, 3}, {7, 5, 6, 9, 1, 2, 3, 8, 4}, {1, 3, 8, 7, 5, 4, 9, 6, 2}, {2, 9, 4, 3, 8, 6, 7, 1, 5}};
        PuzzleCheck check = new PuzzleCheck(sudokuArray);
        check.PuzzleCheckStart(sudokuArray);
        boolean output = check.isTrue;
        boolean expected = true;
        assertEquals(expected, output);
        //test when Puzzle is completed incorrectly
        int[][] sudokuArray2 = {{1, 7, 3, 4, 9, 1, 2, 5, 8}, {4, 8, 2, 5, 7, 3, 6, 9, 1}, {9, 1, 5, 6, 2, 8, 4, 3, 7}, {8, 2, 9, 1, 3, 7, 5, 4, 6}, {3, 4, 1, 2, 6, 5, 8, 7, 9}, {5, 6, 7, 8, 4, 9, 1, 2, 3}, {7, 5, 6, 9, 1, 2, 3, 8, 4}, {1, 3, 8, 7, 5, 4, 9, 6, 2}, {2, 9, 4, 3, 8, 6, 7, 1, 5}};
        PuzzleCheck check2 = new PuzzleCheck(sudokuArray2);
        check.PuzzleCheckStart(sudokuArray2);
        boolean output2 = check.isTrue;
        boolean expected2 = false;
        assertEquals(expected2, output2);
        */
    }
}