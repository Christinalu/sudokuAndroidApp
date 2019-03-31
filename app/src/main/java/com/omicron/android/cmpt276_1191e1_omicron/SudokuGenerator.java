package com.omicron.android.cmpt276_1191e1_omicron;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import static java.lang.Math.sqrt;

public class SudokuGenerator implements Serializable {

    /*
     *    -- IMPORTANT --
     *  This generating function can generate UNIQUE puzzles based on
     *  pre-defined unique puzzles.
    */

    //This class accepts a hardcoded Puzzle and randomizes it to generate a new puzzle which is guaranteed to be unique and the same difficulty and the puzzle taken in as the argument.
    //size of puzzle by index
    private int pindex;
    //number of rows (or cols) of the puzzle (always perfect square, so only one number is needed)
    private int size;
    //number of sections on the horizontal axis
    private int puzz_numHsec;
    //number of sections on the vertical axis
    private int puzz_numVsec;
    //number of rows in a section of the puzzle
    private int sec_numRows;
    //number of cols in a section of the puzzle
    private int sec_numCols;
    public int[][] PuzzleOriginal;
    public int[][] Puzzle; //stores user input changes
    public int[][] PuzzleSol; //stores puzzle solution
    private int[][] conflictArr;

    public boolean isCorrect;
    public boolean enableCheck;
    private int sqrFilled;

    private List<Entry> History;
    private int hsize;

	//
    //TODO: Remove the first 3D array (the test array) before submitting for iteration 3
	//TEST Puzzle Seed array. USE THIS ONE TO RUN TESTS AND DISABLE THE puzz_seeds ARRAY DIRECTLY BELOW, feel free to edit values. Currently the puzz_seeds == puzz_seedSols, except first 2 values are set to empty
    /*
    private int[][][] puzz_seeds = {
            {
                    //4x4 easy difficulty
                    {0, 0, 4, 3}, {4, 3, 2, 1}, {3, 2, 1, 4}, {1, 4, 3, 2}
            },
            {
                    //4x4 medium difficulty
                    {0, 0, 3, 4}, {4, 3, 1, 2}, {2, 1, 4, 3}, {3, 4, 2, 1}
            },
            {
                    //4x4 hard difficulty
                    {0, 0, 4, 1}, {1, 4, 3, 2}, {2, 3, 1, 4}, {4, 1, 2, 3}
            },
            {
                    //6x6 easy difficulty
                    {0, 0, 2, 4, 3, 1}, {3, 1, 4, 6, 2, 5}, {4, 3, 6, 1, 5, 2}, {1, 2, 5, 3, 6, 4}, {2, 4, 3, 5, 1, 6}, {5, 6, 1, 2, 4, 3}
            },
            {
                    //6x6 medium difficulty
                    {0, 0, 1, 6, 4, 5}, {6, 4, 5, 2, 1, 3}, {3, 2, 6, 1, 5, 4}, {1, 5, 4, 3, 6, 2}, {5, 1, 2, 4, 3, 6}, {4, 6, 3, 5, 2, 1}
            },
            {
                    //6x6 hard difficulty
                    {0, 0, 6, 3, 1, 4}, {4, 3, 1, 6, 2, 5}, {1, 6, 5, 4, 3, 2}, {2, 4, 3, 5, 6, 1}, {3, 1, 4, 2, 5, 6}, {6, 5, 2, 1, 4, 3}
            },
            {
                    //9x9 easy solution
                    {0, 0, 7, 2, 8, 4, 1, 5, 6}, {4, 5, 6, 1, 7, 9, 3, 8, 2}, {1, 2, 8, 3, 5, 6, 7, 4, 9}, {8, 9, 5, 6, 1, 7, 4, 2, 3}, {2, 7, 3, 4, 9, 8, 5, 6, 1}, {6, 4, 1, 5, 3, 2, 8, 9, 7}, {5, 8, 9, 7, 2, 1, 6, 3, 4}, {3, 1, 4, 9, 6, 5, 2, 7, 8}, {7, 6, 2, 8, 4, 3, 9, 1, 5}
            },
            {
                    //9x9 medium solution
                    {0, 0, 6, 7, 9, 1, 4, 2, 5}, {5, 2, 1, 3, 4, 8, 6, 9, 7}, {9, 7, 4, 2, 6, 5, 1, 3, 8}, {4, 1, 3, 5, 2, 9, 8, 7, 6}, {6, 5, 9, 8, 3, 7, 2, 4, 1}, {2, 8, 7, 6, 1, 4, 9, 5, 3}, {7, 4, 5, 1, 8, 2, 3, 6, 9}, {1, 6, 2, 9, 7, 3, 5, 8, 4}, {3, 9, 8, 4, 5, 6, 7, 1, 2}
            },
            {
                    //9x9 hard solution
                    {0, 0, 1, 3, 9, 8, 5, 6, 4}, {4, 5, 3, 2, 6, 1, 7, 8, 9}, {9, 8, 6, 5, 7, 4, 3, 1, 2}, {7, 2, 8, 6, 5, 3, 4, 9, 1}, {3, 6, 9, 1, 4, 2, 8, 5, 7}, {5, 1, 4, 7, 8, 9, 2, 3, 6}, {6, 9, 7, 8, 2, 5, 1, 4, 3}, {1, 4, 5, 9, 3, 7, 6, 2, 8}, {8, 3, 2, 4, 1, 6, 9, 7, 5}
            },
            {
                    //12x12 easy solution
                    {0, 0, 6, 7, 11, 8, 5, 4, 2, 10, 12, 3}, {5, 2, 10, 11, 6, 12, 1, 3, 7, 8, 4, 9}, {12, 8, 3, 4, 2, 7, 10, 9, 11, 1, 6, 5}, {2, 7, 9, 1, 4, 10, 8, 5, 12, 6, 3, 11}, {11, 12, 8, 10, 3, 1, 6, 2, 5, 9, 7, 4}, {3, 5, 4, 6, 9, 11, 7, 12, 8, 2, 10, 1}, {9, 10, 2, 12, 8, 3, 4, 11, 6, 5, 1, 7}, {6, 3, 1, 8, 12, 5, 9, 7, 10, 4, 11, 2}, {4, 11, 7, 5, 1, 6, 2, 10, 3, 12, 9, 8}, {10, 1, 12, 2, 7, 4, 11, 8, 9, 3, 5, 6}, {8, 4, 11, 3, 5, 9, 12, 6, 1, 7, 2, 10}, {7, 6, 5, 9, 10, 2, 3, 1, 4, 11, 8, 12}
            },
            {
                    //12x12 medium solution
                    {0, 0, 12, 2, 7, 8, 4, 11, 6, 3, 1, 5}, {3 ,5 ,4, 6, 1, 9, 2, 12, 8, 11, 10, 7}, {11, 7, 8, 1, 5, 6, 10, 3, 2, 9, 4, 12}, {4, 9, 1, 3, 8, 2, 6, 7, 12, 5, 11, 10}, {7, 8, 11, 10, 4, 5, 12, 9, 1, 2, 6, 3}, {6, 2, 5, 12, 3, 11, 1, 10, 9, 8, 7, 4}, {1, 4, 10, 11, 9, 7, 3, 2, 5, 12, 8, 6}, {2, 3, 6, 5, 10, 12, 8, 4, 7, 1, 9, 11}, {8, 12, 9, 7, 11, 1, 5, 6, 10, 4, 3, 2}, {12, 6, 7, 4, 2, 3, 9, 1, 11, 10, 5, 8}, {10, 1, 2, 8, 6, 4, 11, 5, 3, 7, 12, 9}, {5, 11, 3, 9, 12, 10, 7, 8, 4, 6, 2, 1}
            },
            {
                    //12x12 hard solution
                    {0, 0, 7, 11, 5, 2, 6, 4, 12, 1, 3, 9}, {6, 4, 3, 2, 10, 9, 1, 12, 11, 5, 7, 8}, {9, 1, 5, 12, 3, 11, 7, 8, 6, 4, 2, 10}, {7, 3, 6, 8, 12, 10, 2, 11, 5, 9, 1, 4}, {4, 10, 2, 5, 6, 1, 3, 9, 8, 11, 12, 7}, {1, 11, 12, 9, 7, 8, 4, 5, 3, 2, 10, 6}, {5, 2, 10, 7, 9, 12, 8, 6, 1, 3, 4, 11}, {11, 9, 4, 3, 2, 5, 10, 1, 7, 6, 8, 12}, {8, 12, 1, 6, 4, 7, 11, 3, 2, 10, 9, 5}, {3, 7, 8, 1, 11, 4, 5, 10, 9, 12, 6, 2}, {2, 5, 9, 4, 1, 6, 12, 7, 10, 8, 11, 3}, {12, 6, 11, 10, 8, 3, 9, 2, 4, 7, 5, 1}
            }
    };
    */
    private int[][][] puzz_seeds = {
            {
                    //4x4 easy difficulty seed
                    {2, 1, 0, 0}, {0, 3, 2, 0}, {3, 0, 1, 4}, {1, 0, 0, 2}
            },
            {
                    //4x4 medium difficulty seed
                    {1, 0, 3, 4}, {0, 3, 0, 0}, {2, 0, 0, 0}, {0, 0, 0, 1}
            },
            {
                    //4x4 hard difficulty seed
                    {3, 0, 0, 1}, {1, 0, 0, 0}, {0, 0, 0, 0}, {0, 0, 2, 0}
            },
            {
                    //6x6 easy difficulty seed
                    {0, 5, 0, 4, 3, 1}, {3, 0, 4, 6, 0, 5}, {4, 3, 0, 0, 5, 2}, {1, 0, 5, 3, 6, 4}, {0, 4, 3, 5, 1, 0}, {0, 6, 0, 2, 4, 3}
            },
            {
                    //6x6 medium difficulty seed
                    {0, 3, 1, 6, 0, 5}, {0, 4, 0, 0, 0, 0}, {3, 0, 0, 0, 0, 0}, {0, 5, 0, 3, 6, 0}, {5, 0, 2, 0, 0, 6}, {4, 0, 0, 5, 0, 1}
            },
            {
                    //6x6 hard difficulty seed
                    {5, 0, 6, 0, 0, 0}, {0, 0, 0, 0, 2, 0}, {0, 6, 0, 0, 0, 0}, {0, 0, 0, 5, 0, 1}, {0, 0, 4, 0, 0, 0}, {0, 0, 0, 1, 0, 3}
            },
            {
                    //9x9 easy difficulty seed
                    {9, 0, 7, 2, 8, 0, 0, 5, 6}, {0, 5, 6, 1, 7, 9, 0, 8, 2}, {1, 2, 8, 3, 0, 6, 0, 4, 9}, {8, 0, 5, 6, 1, 7, 0, 2, 3}, {2, 0, 3, 0, 9, 8, 0, 6, 1}, {6, 4, 0, 5, 3, 2, 8, 0, 7}, {0, 8, 0, 7, 2, 0, 6, 3, 4}, {0, 0, 0, 9, 6, 5, 2, 7, 0}, {0, 6, 2, 0, 4, 3, 9, 0, 0}
            },
            {
                    //9x9 medium difficulty seed
                    {8, 0, 6, 7, 9, 0, 0, 0, 5}, {0, 2, 0, 3, 4, 0, 6, 0, 0}, {9, 0, 4, 0, 6, 5, 1, 0, 8}, {4, 0, 3, 5, 0, 0, 0, 0, 6}, {6, 5, 0, 8, 3, 7, 0, 4, 1}, {2, 0, 0, 0, 0, 0, 0, 0, 0}, {7, 0, 5, 1, 8, 0, 0, 0, 9}, {0, 0, 2, 9, 0, 3, 0, 0, 0}, {3, 0, 8, 0, 5, 6, 0, 0, 2}
            },
            {
                    //9x9 hard difficulty seed
                    {0, 0, 0, 3, 0, 0, 0, 6, 0}, {0, 0, 3, 0, 0, 1, 7, 0, 0}, {0, 8, 0, 0, 7, 0, 0, 0, 2}, {0, 2, 0, 6, 0, 3, 0, 9, 0}, {0, 6, 0, 0, 4, 0, 0, 5, 0}, {0, 1, 0, 7, 0, 9, 0, 3, 0}, {6, 0, 0, 0, 2, 0, 0, 4, 0}, {0, 0, 5, 9, 0, 0, 6, 0, 0}, {0, 3, 0, 0, 0, 6, 0, 0, 0}
            },
            {
                    //12x12 easy difficulty seed
                    {1, 9, 0, 0, 11, 8, 0, 4, 2, 10, 12, 0}, {5, 2, 10, 0, 6, 12, 1, 0, 7, 8, 4, 0}, {0, 8, 3, 4, 2, 7, 0, 9, 0, 1, 6, 5}, {2, 7, 9, 0, 4, 10, 0, 5, 12, 6, 3, 0}, {11, 12, 8, 0, 3, 1, 6, 0, 5, 9, 7, 0}, {3, 5, 4, 6, 9, 11, 0, 0, 8, 2, 10, 1}, {9, 0, 2, 12, 0, 3, 0, 11, 6, 0, 1, 7}, {6, 3, 0, 8, 12, 5, 0, 7, 10, 0, 11, 2}, {0, 11, 7, 5, 1, 6, 0, 10, 3, 0, 9, 0}, {10, 1, 0, 2, 0, 4, 11, 8, 9, 3, 0, 0}, {8, 0, 11, 3, 0, 9, 12, 6, 0, 0, 2, 10}, {0, 6, 5, 0, 10, 2, 3, 1, 4, 0, 0, 12}
            },
            {
                    //12x12 medium difficulty seed
                    {9, 0, 12, 0, 7, 0, 4, 11, 6, 0, 0, 0}, {3, 5, 4, 0, 1, 0, 0, 12, 8, 0, 10, 7}, {11, 0, 8, 0, 5, 0, 0, 3, 0, 9, 0, 12}, {0, 9, 0, 3, 0, 2, 0, 7, 12, 5, 11, 10}, {0, 8, 0, 10, 4, 5, 0, 9, 0, 2, 6, 3}, {6, 2, 0, 12, 0, 0, 1, 0, 0, 8, 7, 0}, {1, 4, 10, 11, 0, 0, 3, 2, 0, 0, 8, 0}, {0, 3, 0, 0, 0, 0, 0, 0, 7, 1, 9, 11}, {8, 12, 0, 0, 0, 1, 5, 6, 0, 4, 3, 0}, {12, 0, 7, 0, 0, 3, 0, 0, 11, 0, 0, 0}, {10, 1, 2, 8, 0, 0, 0, 0, 3, 0, 12, 0}, {5, 11, 3, 0, 12, 10, 7, 0, 4, 6, 0, 1}
            },
            {
                    //12x12 hard difficulty seed
                    {0, 0, 0, 11, 0, 2, 6, 4, 12, 0, 0, 0}, {0, 0, 3, 0, 0, 9, 0, 12, 11, 5, 0, 0}, {9, 1, 5, 0, 0, 11, 0, 8, 6, 0, 0, 0}, {7, 3, 0, 0, 0, 0, 0, 0, 0, 9, 1, 0}, {0, 10, 0, 5, 0, 0, 3, 9, 0, 11, 0, 7}, {0, 0, 0, 0, 0, 8, 4, 0, 0, 2, 0, 0}, {5, 0, 0, 0, 0, 12, 8, 6, 1, 3, 4, 0}, {0, 9, 4, 3, 0, 0, 10, 1, 0, 0, 8, 12}, {0, 0, 1, 6, 4, 7, 11, 0, 2, 0, 9, 5}, {0, 7, 8, 0, 11, 0, 0, 0, 0, 0, 0, 2}, {2, 0, 0, 4, 1, 0, 0, 0, 0, 0, 0, 3}, {0, 0, 0, 0, 8, 3, 0, 0, 0, 7, 5, 1}
            },
    };
    private int[][][] puzz_seedSols = {
            {
                    //4x4 easy difficulty
                    {2, 1, 4, 3}, {4, 3, 2, 1}, {3, 2, 1, 4}, {1, 4, 3, 2}
            },
            {
                    //4x4 medium difficulty
                    {1, 2, 3, 4}, {4, 3, 1, 2}, {2, 1, 4, 3}, {3, 4, 2, 1}
            },
            {
                    //4x4 hard difficulty
                    {3, 2, 4, 1}, {1, 4, 3, 2}, {2, 3, 1, 4}, {4, 1, 2, 3}
            },
            {
                    //6x6 easy difficulty
                    {6, 5, 2, 4, 3, 1}, {3, 1, 4, 6, 2, 5}, {4, 3, 6, 1, 5, 2}, {1, 2, 5, 3, 6, 4}, {2, 4, 3, 5, 1, 6}, {5, 6, 1, 2, 4, 3}
            },
            {
                    //6x6 medium difficulty
                    {2, 3, 1, 6, 4, 5}, {6, 4, 5, 2, 1, 3}, {3, 2, 6, 1, 5, 4}, {1, 5, 4, 3, 6, 2}, {5, 1, 2, 4, 3, 6}, {4, 6, 3, 5, 2, 1}
            },
            {
                    //6x6 hard difficulty
                    {5, 2, 6, 3, 1, 4}, {4, 3, 1, 6, 2, 5}, {1, 6, 5, 4, 3, 2}, {2, 4, 3, 5, 6, 1}, {3, 1, 4, 2, 5, 6}, {6, 5, 2, 1, 4, 3}
            },
            {
                    //9x9 easy solution
                    {9, 3, 7, 2, 8, 4, 1, 5, 6}, {4, 5, 6, 1, 7, 9, 3, 8, 2}, {1, 2, 8, 3, 5, 6, 7, 4, 9}, {8, 9, 5, 6, 1, 7, 4, 2, 3}, {2, 7, 3, 4, 9, 8, 5, 6, 1}, {6, 4, 1, 5, 3, 2, 8, 9, 7}, {5, 8, 9, 7, 2, 1, 6, 3, 4}, {3, 1, 4, 9, 6, 5, 2, 7, 8}, {7, 6, 2, 8, 4, 3, 9, 1, 5}
            },
            {
                    //9x9 medium solution
                    {8, 3, 6, 7, 9, 1, 4, 2, 5}, {5, 2, 1, 3, 4, 8, 6, 9, 7}, {9, 7, 4, 2, 6, 5, 1, 3, 8}, {4, 1, 3, 5, 2, 9, 8, 7, 6}, {6, 5, 9, 8, 3, 7, 2, 4, 1}, {2, 8, 7, 6, 1, 4, 9, 5, 3}, {7, 4, 5, 1, 8, 2, 3, 6, 9}, {1, 6, 2, 9, 7, 3, 5, 8, 4}, {3, 9, 8, 4, 5, 6, 7, 1, 2}
            },
            {
                    //9x9 hard solution
                    {2, 7, 1, 3, 9, 8, 5, 6, 4}, {4, 5, 3, 2, 6, 1, 7, 8, 9}, {9, 8, 6, 5, 7, 4, 3, 1, 2}, {7, 2, 8, 6, 5, 3, 4, 9, 1}, {3, 6, 9, 1, 4, 2, 8, 5, 7}, {5, 1, 4, 7, 8, 9, 2, 3, 6}, {6, 9, 7, 8, 2, 5, 1, 4, 3}, {1, 4, 5, 9, 3, 7, 6, 2, 8}, {8, 3, 2, 4, 1, 6, 9, 7, 5}
            },
            {
                    //12x12 easy solution
                    {1, 9, 6, 7, 11, 8, 5, 4, 2, 10, 12, 3}, {5, 2, 10, 11, 6, 12, 1, 3, 7, 8, 4, 9}, {12, 8, 3, 4, 2, 7, 10, 9, 11, 1, 6, 5}, {2, 7, 9, 1, 4, 10, 8, 5, 12, 6, 3, 11}, {11, 12, 8, 10, 3, 1, 6, 2, 5, 9, 7, 4}, {3, 5, 4, 6, 9, 11, 7, 12, 8, 2, 10, 1}, {9, 10, 2, 12, 8, 3, 4, 11, 6, 5, 1, 7}, {6, 3, 1, 8, 12, 5, 9, 7, 10, 4, 11, 2}, {4, 11, 7, 5, 1, 6, 2, 10, 3, 12, 9, 8}, {10, 1, 12, 2, 7, 4, 11, 8, 9, 3, 5, 6}, {8, 4, 11, 3, 5, 9, 12, 6, 1, 7, 2, 10}, {7, 6, 5, 9, 10, 2, 3, 1, 4, 11, 8, 12}
            },
            {
                    //12x12 medium solution
                    {9, 10, 12, 2, 7, 8, 4, 11, 6, 3, 1, 5}, {3 ,5 ,4, 6, 1, 9, 2, 12, 8, 11, 10, 7}, {11, 7, 8, 1, 5, 6, 10, 3, 2, 9, 4, 12}, {4, 9, 1, 3, 8, 2, 6, 7, 12, 5, 11, 10}, {7, 8, 11, 10, 4, 5, 12, 9, 1, 2, 6, 3}, {6, 2, 5, 12, 3, 11, 1, 10, 9, 8, 7, 4}, {1, 4, 10, 11, 9, 7, 3, 2, 5, 12, 8, 6}, {2, 3, 6, 5, 10, 12, 8, 4, 7, 1, 9, 11}, {8, 12, 9, 7, 11, 1, 5, 6, 10, 4, 3, 2}, {12, 6, 7, 4, 2, 3, 9, 1, 11, 10, 5, 8}, {10, 1, 2, 8, 6, 4, 11, 5, 3, 7, 12, 9}, {5, 11, 3, 9, 12, 10, 7, 8, 4, 6, 2, 1}
            },
            {
                    //12x12 hard solution
                    {10, 8, 7, 11, 5, 2, 6, 4, 12, 1, 3, 9}, {6, 4, 3, 2, 10, 9, 1, 12, 11, 5, 7, 8}, {9, 1, 5, 12, 3, 11, 7, 8, 6, 4, 2, 10}, {7, 3, 6, 8, 12, 10, 2, 11, 5, 9, 1, 4}, {4, 10, 2, 5, 6, 1, 3, 9, 8, 11, 12, 7}, {1, 11, 12, 9, 7, 8, 4, 5, 3, 2, 10, 6}, {5, 2, 10, 7, 9, 12, 8, 6, 1, 3, 4, 11}, {11, 9, 4, 3, 2, 5, 10, 1, 7, 6, 8, 12}, {8, 12, 1, 6, 4, 7, 11, 3, 2, 10, 9, 5}, {3, 7, 8, 1, 11, 4, 5, 10, 9, 12, 6, 2}, {2, 5, 9, 4, 1, 6, 12, 7, 10, 8, 11, 3}, {12, 6, 11, 10, 8, 3, 9, 2, 4, 7, 5, 1}
            }
    };

    public SudokuGenerator(int usrDiffPref, int psize)
    {
        pindex = psize*3 + usrDiffPref;
        size = puzz_seeds[pindex][0].length;
		puzz_numHsec = (int) sqrt(size);
		puzz_numVsec = size/puzz_numHsec;
		sec_numRows = puzz_numHsec;
		sec_numCols = puzz_numVsec;
		Puzzle = new int[size][size];
		PuzzleSol = new int[size][size];
		PuzzleOriginal = new int[size][size];
		conflictArr = new int[size][size];

		isCorrect = false;
		enableCheck = false;
		sqrFilled = 0;

        History = new ArrayList<Entry>();
        hsize = 0;

        copyarr(puzz_seeds[pindex], Puzzle);
        copyarr(puzz_seedSols[pindex], PuzzleSol);
        //printCurrent();
        scramble(Puzzle, PuzzleSol);
        copyarr(Puzzle, PuzzleOriginal );
        zero(conflictArr);
        printCurrent();

        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (PuzzleOriginal[i][j] != 0) {
                    sqrFilled++;
                }
            }
        }
	}

    // test method to print for debugging
    private void printArr( String tag, int[][] arr ) {
        if (size == 4) {
            //4x4
            for( int i=0; i<size; i++ )
            {
                Log.d(tag, " " + arr[i][0] + " " + arr[i][1] + " " + arr[i][2]+ " " + arr[i][3] );
            }
            Log.d( tag," \n\n");
        }
        else if (size == 6) {
            //6x6
            for( int i=0; i<size; i++ )
            {
                Log.d(tag, " " + arr[i][0] + " " + arr[i][1] + " " + arr[i][2]+ " " + arr[i][3]+ " " + arr[i][4]+ " " + arr[i][5] );
            }
            Log.d( tag," \n\n");
        }
        else if (size == 9) {
            //9x9
            for( int i=0; i<size; i++ )
            {
                Log.d(tag, " " + arr[i][0] + " " + arr[i][1] + " " + arr[i][2]+ " " + arr[i][3]+ " " + arr[i][4]+ " " + arr[i][5]+ " " + arr[i][6]+ " " + arr[i][7]+ " " + arr[i][8] );
            }
            Log.d( tag," \n\n");
        }
        else {
            //12x12
            for( int i=0; i<size; i++ )
            {
                Log.d(tag, " " + arr[i][0] + " " + arr[i][1] + " " + arr[i][2]+ " " + arr[i][3]+ " " + arr[i][4]+ " " + arr[i][5]+ " " + arr[i][6]+ " " + arr[i][7]+ " " + arr[i][8] + " " + arr[i][9] + " " + arr[i][10] + " " + arr[i][11] );
            }
            Log.d( tag," \n\n");
        }
        //Log.d(tag,"Arr " + tag + " is:" + Arrays.deepToString(arr));
    }

    public void printCurrent( ) {
        printArr("puzzle", Puzzle);
        printArr("puzzleSol", PuzzleSol);
    }

	//zero an 1D array
    private void zero(int[] arr)
    {
        for (int i=0; i<arr.length; i++) arr[i] = 0;
    }

    //zero a 2D array
    private void zero(int[][] arr)
    {
        int arrSize = arr.length;
        for (int i=0; i<arrSize; i++) {
            for (int j=0; j<arrSize; j++) {
                arr[i][j] = 0;
            }
        }
    }

    //copy a 2D array into another
    public void copyarr(int[][] arr1, int[][] arr2)
    {
        for (int i = 0; i<size; i++) {
            for (int j = 0; j<size; j++) {
                //traverse by columns first, then down one row
                arr2[i][j] = arr1[i][j];
            }
        }
    }
    //function takes an int array of row/col size that will be used store a randomized order vector
    private void randomizeOrder(int[] arr)
    {
        //bit map to see what order has been used
        int rowcol = arr.length;
        int[] numUsed = new int[rowcol];
        zero(numUsed);
        int randPos;
        Random rand = new Random();
        int i = 0;
        while (i < rowcol) {
            randPos = rand.nextInt(100);
            randPos = randPos%rowcol;
            if (numUsed[randPos] == 0) { // if not used before
                arr[i] = randPos; // put rand num back in arr
                numUsed[randPos] = 1; // mark as used
                i++; // by putting i++ here this only moves on until it find valid num
            }
        }
    }

    // takes a 2D puzzle array and will shuffle rows/columns to create a new puzzle with unique solution
    private void scramble(int[][] Puzzle, int[][] PuzzleSol)
    {
        int tempSec;
        int puzzleSec;
        int[][] temparr = new int[size][size];
        int[][] tempsolarr = new int[size][size];
        copyarr(Puzzle, temparr);
        copyarr(PuzzleSol, tempsolarr);
        int[] orderR = new int[sec_numRows];
        int[] orderC = new int[sec_numCols];
        int[] orderV = new int[puzz_numVsec];
        int[] orderH = new int[puzz_numHsec];
        //swap rows within section i
        for (int i = 0; i < puzz_numVsec; i++) {
            //ith section down
            randomizeOrder(orderR);
            //Log.d( "RAND", "S_ROWS Random Generated: "+ Arrays.toString(orderR));
            for (int j = 0; j < sec_numRows; j++) { //each row within a 3x9
                //row in the section
                puzzleSec = (i * sec_numRows) + j; //go to a specific row
                tempSec = (i * sec_numRows) + orderR[j]; //go to a specific row based on the randomizer
                for (int k = 0; k < size; k++) {
                    //col
                    Puzzle[puzzleSec][k] = temparr[tempSec][k]; //swap the two rows
                    PuzzleSol[puzzleSec][k] = tempsolarr[tempSec][k]; //swap the two rows
                }
            }
        }
        //swap columns within section i
        copyarr(Puzzle, temparr);
        copyarr(PuzzleSol, tempsolarr);
        for (int i = 0; i < puzz_numHsec; i++) {
            //ith box to the right
            randomizeOrder(orderC);
            //Log.d( "RAND", "S_COLS Random Generated: "+ Arrays.toString(orderC));
            for (int j = 0; j < sec_numCols; j++) {
                //col in the section
                puzzleSec = (i * sec_numCols) + j;
                tempSec = (i * sec_numCols) + orderC[j];
                for (int k = 0; k < size; k++) {
                    //row
                    Puzzle[k][puzzleSec] = temparr[k][tempSec];
                    PuzzleSol[k][puzzleSec] = tempsolarr[k][tempSec];
                }
            }
        }
        //swap the sections
        copyarr(Puzzle, temparr);
        copyarr(PuzzleSol, tempsolarr);
        //swap vertical sections
        randomizeOrder(orderV);
        //Log.d( "RAND", "S_ROWSsec Random Generated: "+ Arrays.toString(orderV));
        for (int i = 0; i < puzz_numVsec; i++) { // for each 3x9
            //ith section down
            for (int j = 0; j < sec_numRows; j++) { // for each of the rows in a 3x9
                //row in section
                puzzleSec = (i * sec_numRows) + j; // inside the ith 3x9
                tempSec = (orderV[i] * sec_numRows) + j; // swap with the chosen 3x9 from arr based on randomize()
                for (int k = 0; k < size; k++) {
                    //col
                    Puzzle[puzzleSec][k] = temparr[tempSec][k]; // copy the row from randomize()
                    PuzzleSol[puzzleSec][k] = tempsolarr[tempSec][k]; // copy the row from randomize()
                }
            }
        }
        copyarr(Puzzle, temparr);
        copyarr(PuzzleSol, tempsolarr);
        //swap horizontal sections
        randomizeOrder(orderH);
        //Log.d( "RAND", "S_COLSsec Random Generated: "+ Arrays.toString(orderH));
        for (int i = 0; i < puzz_numHsec; i++) {
            //ith box to the right
            for (int j = 0; j < sec_numCols; j++) {
                //col in the section
                puzzleSec = (i * sec_numCols) + j;
                tempSec = (orderH[i] * sec_numCols) + j;
                for (int k = 0; k < size; k++) {
                    //row
                    Puzzle[k][puzzleSec] = temparr[k][tempSec];
                    PuzzleSol[k][puzzleSec] = tempsolarr[k][tempSec];
                }
            }
        }
    }

	public int[][] getSolution( )
	{ return PuzzleSol; }

	public int[][] getPuzzle() { return Puzzle;}

	public void setPuzzleVal(int val, int x, int y) {
        Puzzle[x][y] = val;
    }

	public int[][] getconflictArr() {return conflictArr;}

	public void resetPuzzle() {
        copyarr(PuzzleOriginal,Puzzle);
        sqrFilled = 0;
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                if (PuzzleOriginal[i][j] != 0) {
                    sqrFilled++;
                }
            }
        }
        zero(conflictArr);
        clearHistory();
    }

    //add entry to history
    public void addHistroy(int x, int y) {
        Pair p = new Pair(x,y);
        Entry e = new Entry(Puzzle[x][y], p);
        History.add(e);
        hsize++;
    }

    //remove entry from history
    public Entry removeHistory() {
        //caller's responsibility to check if empty
        Entry e = new Entry(History.get(hsize - 1));
        removeDuplicates(e.getCoordinate().getRow(),e.getCoordinate().getColumn());
        History.remove(hsize - 1);
        hsize--;
        return e;
    }

    private void clearHistory() {
        History.clear();
        hsize = 0;
    }

    public boolean historyisEmpty() {return History.isEmpty();}

    public void printHistory() {
        if (!History.isEmpty()) {
            for (int i = 0; i < hsize; i++) {
                History.get(i).print();
            }
        }
        else {
            Log.e ("TESTI", "Histroy list is empty");
        }
    }

    public void track(Pair currentRectColoured) {
        if( Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] == 0 ) //if sqr selected was not selected so far
        {
            sqrFilled ++;
        }
        //if puzzle filled, check puzzle
        if( sqrFilled == size*size )
        {
            enableCheck = true; //allow for puzzle to be checked
        }
    }

	private boolean check() {
        for (int i = 0; i<size; i++) {
            for (int j = 0; j<size; j++) {
                if (Puzzle[i][j] != PuzzleSol[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // check if puzzle is correct, if it is then disable buttons
    public void checkPuzzle(View v, Button[] btnArr)
    {
        isCorrect = check();
        if( isCorrect )
        {
            //disable buttons
            for( int i=0; i<size; i++ )
            {
                btnArr[i].setOnClickListener( null );
            }
            Toast.makeText(v.getContext(), "CONGRATULATIONS!", Toast.LENGTH_LONG).show( );
        }
        else
        {
            Toast.makeText(v.getContext(), "INCORRECT", Toast.LENGTH_SHORT).show();
        }
    }

    //check row, col, section for duplicate entries. return true if duplicate detected, false if no duplicates
    public boolean checkDuplicate (int x, int y) {
        int input = Puzzle[x][y];
        if (input > 0 && PuzzleOriginal[x][y] == 0) {
            int duplicateFound = 0;
            //find (x,y) of the section currently selected
            int sec_x = x / sec_numRows;
            sec_x = sec_x * sec_numRows;
            //Log.d("TESTI", "x coordinate of section is: "+sec_x);
            int sec_y = y / sec_numCols;
            sec_y = sec_y * sec_numCols;
            //temporarily set Puzzle[x][y] to 0 so we can check with loops
            Puzzle[x][y] = 0;

            int loopCount = (sec_y + sec_numCols)%size;
            while (loopCount != sec_y) {
                //check row for duplicates
                if (input == Puzzle[x][loopCount]) {
                    Log.d("TAG", "Duplicate found at: ("+x+","+loopCount+")");
                    duplicateFound = 1;
                    conflictArr[x][loopCount] = 1;
                }
                loopCount = (loopCount + 1)%size;
            }
            loopCount = (sec_x + sec_numRows)%size;
            while (loopCount != sec_x) {
                //check col for duplicates
                if (input == Puzzle[loopCount][y]) {
                    Log.d("TAG", "Duplicate found at: ("+loopCount+","+y+")");
                    duplicateFound = 1;
                    conflictArr[loopCount][y] = 1;
                }
                loopCount = (loopCount + 1)%size;
            }
            //Log.d("TESTI", "y coordinate of section is: "+sec_y);
            for (int i = sec_x; i < sec_numRows + sec_x; i++) {
                for (int j = sec_y; j < sec_numCols + sec_y; j++) {
                    //check section for duplicates
                    if (input == Puzzle[i][j]) {
                        Log.d("TAG", "Duplicate found at: ("+i+","+j+")");
                        duplicateFound = 1;
                        conflictArr[i][j] = 1;
                    }
                }
            }
            Puzzle[x][y] = input;
            if (duplicateFound == 1) {
                conflictArr[x][y] = 1;
                return true;
            }
        }
        return false;
    }

    public void removeDuplicates(int x, int y) {
        //function removes conflicts from conflictArr in preparation for a new input at (x,y)
        int current = Puzzle[x][y];
        if (current > 0 && PuzzleOriginal[x][y] == 0) {
            //find (x,y) of the section currently selected
            int sec_x = x / sec_numRows;
            sec_x = sec_x * sec_numRows;
            //Log.d("TESTI", "x coordinate of section is: "+sec_x);
            int sec_y = y / sec_numCols;
            sec_y = sec_y * sec_numCols;
            //temporarily set Puzzle[x][y] to 0 so we can check with loops
            Puzzle[x][y] = 0;
            int loopCount = (sec_y + sec_numCols)%size;
            while (loopCount != sec_y) {
                //check row for duplicates
                if (current == Puzzle[x][loopCount]) {
                    conflictArr[x][loopCount] = 0;
                    checkDuplicate(x,loopCount);
                }
                loopCount = (loopCount + 1)%size;
            }
            loopCount = (sec_x + sec_numRows)%size;
            while (loopCount != sec_x) {
                //check col for duplicates
                if (current == Puzzle[loopCount][y]) {
                    conflictArr[loopCount][y] = 0;
                    checkDuplicate(loopCount,y);
                }
                loopCount = (loopCount + 1)%size;
            }
            //Log.d("TESTI", "y coordinate of section is: "+sec_y);
            for (int i = sec_x; i < sec_numRows + sec_x; i++) {
                for (int j = sec_y; j < sec_numCols + sec_y; j++) {
                    //check section for duplicates
                    if (current == Puzzle[i][j]) {
                        conflictArr[i][j] = 0;
                        checkDuplicate(i,j);
                    }
                }
            }
            Puzzle[x][y] = current;
            conflictArr[x][y] = 0;
        }
    }
}