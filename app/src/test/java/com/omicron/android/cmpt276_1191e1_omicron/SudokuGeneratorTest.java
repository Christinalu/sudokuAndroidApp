package com.omicron.android.cmpt276_1191e1_omicron;

import org.junit.Test;

import static org.junit.Assert.*;

public class SudokuGeneratorTest {
    public void sudokuGenerator() {
        //test when Puzzle is completed correctly
        //inputs for usrDiffPref range may be 0 (easy), 1 (medium), 2 (hard) in reference to the difficulty level
        //inputs for psize may be 0 (4x4), 1 (6x6), 2 (9x9), 3 (12x12) in reference to the puzzle size
        //generate puzzle medium, 6x6
        int usrDiffPref = 1;
        int psize = 1;
        SudokuGenerator usrSudokuArr = new SudokuGenerator (usrDiffPref,psize);
        int expected_size = 0;
        int output_size = -1;
        if (psize == 0) {
            expected_size = 16;
            output_size = usrSudokuArr.getPuzzle().length;
        }
        else if (psize == 1) {
            expected_size = 36;
            output_size = usrSudokuArr.getPuzzle().length;
        }
        else if (psize == 1) {
            expected_size = 81;
            output_size = usrSudokuArr.getPuzzle().length;
        }
        else if (psize == 1) {
            expected_size = 144;
            output_size = usrSudokuArr.getPuzzle().length;
        }
        else {
            //entered in a input that is invalid and would never be ran
            expected_size = 0;
            output_size = 0;
        }
        assertEquals(expected_size, output_size);

    }
}