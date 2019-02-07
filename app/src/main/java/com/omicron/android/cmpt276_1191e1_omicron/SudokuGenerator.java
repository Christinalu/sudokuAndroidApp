package com.omicron.android.cmpt276_1191e1_omicron;
import android.util.Log;

import java.util.Random;
import static java.lang.Math.sqrt;

public class SudokuGenerator {

    /*
     *    -- IMPORTANT --
     *  This generating function can generate UNIQUE puzzles based on
     *  pre-defined unique puzzles. One puzzle can be used to
     *  generate approximately (3!)^8 = 1679616 new unique solutions
    */

    //This class accepts a hardcoded Puzzle and randomizes it to generate a new puzzle which is guaranteed to be unique and the same difficulty and the puzzle taken in as the argument.
    private int size;
    public int[][] PuzzleOriginal;
    public int[][] Puzzle; //stores user input changes
    private int[][] PuzzSeedEasy;
    private int[][] PuzzSeedMed;
    private int[][] PuzzSeedHard;

    public SudokuGenerator(int i) {
        size = 9;
        PuzzleOriginal = new int[size][size];
        Puzzle = new int[size][size];
        int[][] PuzzSeedEasy = {{6, 7, 3, 0, 0, 0, 2, 0, 9}, {4, 0, 2, 0, 7, 3, 0, 0, 1}, {0, 0, 5, 6, 0, 8, 4, 3, 7}, {8, 0, 9, 0, 3, 7, 5, 0, 8}, {3, 4, 0, 2, 6, 0, 0, 7, 0}, {0, 6, 7, 8, 0, 9, 1, 0, 3}, {7, 5, 6, 0, 1, 2, 3, 0, 4}, {1, 0, 8, 7, 0, 0, 9, 6, 0}, {1, 0, 8, 7, 0, 0, 9, 6, 0}, {2, 0, 4, 3, 8, 6, 0, 1, 5}};
        int[][] PuzzSeedMed = {{1, 0, 0, 0, 9, 0, 0, 2, 5}, {7, 3, 0, 0, 6, 0, 0, 9, 0}, {0, 0, 0, 0, 0, 3, 6, 0, 0}, {3, 0, 6, 5, 4, 0, 0, 0, 0}, {0, 2, 0, 0, 0, 0, 0, 7, 0}, {0, 0, 0, 0, 8, 6, 7, 0, 3}, {0, 0, 7, 3, 0, 0, 0, 0, 0}, {1, 0, 8, 7, 0, 0, 9, 6, 0}, {0, 9, 0, 0, 7, 0, 0, 8, 2}, {6, 8, 0, 0, 1, 0, 0, 0, 7}};
        int[][] PuzzSeedHard = {{0, 0, 0, 0, 0, 0, 0, 0, 8}, {6, 4, 5, 0, 2, 0, 0, 0, 0}, {0, 0, 0, 9, 0, 7, 6, 4, 0}, {2, 0, 0, 0, 0, 0, 1, 7, 0}, {5, 0, 0, 0, 4, 0, 0, 0, 6}, {0, 1, 3, 0, 0, 0, 0, 0, 5}, {0, 2, 9, 5, 0, 6, 0, 0, 0}, {1, 0, 8, 7, 0, 0, 9, 6, 0}, {0, 0, 0, 0, 1, 0, 5, 9, 4}, {7, 0, 0, 0, 0, 0, 0, 0, 0}};
        if (i == 2) {
            copyarr(PuzzSeedHard, Puzzle);
        }
        else if (i == 1) {
            copyarr(PuzzSeedMed, Puzzle);
        }
        else {
            copyarr(PuzzSeedEasy, Puzzle);
        }
        scramble(Puzzle);
        copyarr( Puzzle, PuzzleOriginal );
    }

    // test method to print for debugging
    public void printOriginal( )
	{
		for( int i=0; i<size; i++ )
		{
			Log.d("MATRIX", " " + PuzzleOriginal[i][0] + " " + PuzzleOriginal[i][1] + " " + PuzzleOriginal[i][2]+ " " + PuzzleOriginal[i][3]+ " " + PuzzleOriginal[i][4]+ " " + PuzzleOriginal[i][5]+ " " + PuzzleOriginal[i][6]+ " " + PuzzleOriginal[i][7]+ " " + PuzzleOriginal[i][8] );
		}
		Log.d( "MATRIX"," \n\n");
	}

    // test method to print for debugging
	public void printCurrent( )
	{
		for( int i=0; i<size; i++ )
		{
			Log.d("MATRIX", " " + Puzzle[i][0] + " " + Puzzle[i][1] + " " + Puzzle[i][2]+ " " + Puzzle[i][3]+ " " + Puzzle[i][4]+ " " + Puzzle[i][5]+ " " + Puzzle[i][6]+ " " + Puzzle[i][7]+ " " + Puzzle[i][8] );
		}
        Log.d( "MATRIX"," \n\n");
	}

    private void zero(int[] arr) {
        for (int i=0; i<arr.length; i++) arr[i] = 0;
    }
    private void copyarr(int[][] arr1, int[][] arr2) {
        //copy arr1 to arr2
        int colrow = size;
        for (int i=0; i<colrow; i++) {
            for (int j=0; j<colrow; j++) {
                arr2[i][j] = arr1[i][j];
            }
        }
    }
    private void randomize(int[] arr, int a) {
        //function takes an int array of size a that will be used store a randomized order vector
        int[] numused = new int[a];
        zero(numused);
        int randpos;
        Random rand = new Random();
        int i = 0;
        while (i < a) {
            randpos = rand.nextInt(100);
            randpos = randpos%a;
            if (numused[randpos] == 0) {
                arr[i] = randpos;
                numused[randpos] = 1;
                i++;
            }
        }
    }
    private void scramble(int[][] Puzzle) {
        int colrow = size;
        int tempSec;
        int puzzleSec;
        int[][] temparr = new int[colrow][colrow];
        copyarr(Puzzle, temparr);
        int[] order = new int[3];
        //swap rows in 3x9 until 9x9 fully swapped
        for (int i = 0; i < 3; i++) {
            //ith box down
            randomize(order, 3);
            for (int j = 0; j < 3; j++) {
                //row in box
                puzzleSec = (i * 3) + j;
                tempSec = (i * 3) + order[j];
                for (int k = 0; k < colrow; k++) {
                    //col
                    Puzzle[puzzleSec][k] = temparr[tempSec][k];
                }
            }
        }
        copyarr(Puzzle, temparr);
        //swap cols 9x3 until 9x9 fully swapped
        for (int i = 0; i < 3; i++) {
            //ith box to the right
            randomize(order, 3);
            for (int j = 0; j < 3; j++) {
                //col in the box
                puzzleSec = (i * 3) + j;
                tempSec = (i * 3) + order[j];
                for (int k = 0; k < colrow; k++) {
                    //row
                    Puzzle[k][puzzleSec] = temparr[k][tempSec];
                }
            }
        }
        copyarr(Puzzle, temparr);
        //swap row boxes
        randomize(order, 3);
        for (int i = 0; i < 3; i++) {
            //ith box
            for (int j = 0; j < 3; j++) {
                //row in the box
                puzzleSec = (i * 3) + j;
                tempSec = (order[i] * 3) + j;
                for (int k = 0; k < colrow; k++) {
                    //col
                    Puzzle[puzzleSec][k] = temparr[tempSec][k];
                }
            }
        }
        copyarr(Puzzle, temparr);
        //swap col boxes
        randomize(order, 3);
        for (int i = 0; i < 3; i++) {
            //ith box
            for (int j = 0; j < 3; j++) {
                //col in the box
                puzzleSec = (i * 3) + j;
                tempSec = (order[i] * 3) + j;
                for (int k = 0; k < colrow; k++) {
                    //row
                    Puzzle[k][puzzleSec] = temparr[k][tempSec];
                }
            }
        }
    }
}