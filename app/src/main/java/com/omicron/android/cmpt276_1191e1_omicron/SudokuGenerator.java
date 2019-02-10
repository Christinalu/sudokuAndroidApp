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

        //test array
        //int[][] testSeed = {{0,3,0,4,9,1,2,5,8},{4,8,2,5,7,3,6,9,1},{9,1,5,6,2,8,4,3,7},{8,2,9,1,3,7,5,4,6},{3,4,1,2,6,5,8,7,9},{5,6,7,8,4,9,1,2,3},{7,5,6,9,1,2,3,8,4},{1,3,8,7,5,4,9,6,2},{2,9,4,3,8,6,7,1,5}};
		int[][] testSeed = {{0,0,2,5,4,9,6,8,3},{6,4,5,8,7,3,2,1,9},{3,8,9,2,6,1,7,4,5},{4,9,6,3,2,7,8,5,1},{8,1,3,4,5,6,9,7,2},{2,5,7,1,9,8,4,3,6},{9,6,4,7,1,5,3,2,8},{7,3,1,6,8,2,5,9,4},{5,2,8,9,3,4,1,6,7}};

		int[][] PuzzSeedEasy = {{0, 0, 7, 2, 8, 0, 0, 5, 0}, {0, 5, 6, 1, 7, 9, 0, 0, 0}, {1, 2, 0, 0, 0, 6, 0, 4, 0}, {8, 0, 0, 0, 1, 0, 0, 2, 3}, {0, 0, 0, 0, 9, 0, 0, 0, 0}, {6, 4, 0, 0, 3, 0, 0, 0, 7}, {0, 8, 0, 7, 0, 0, 0, 3, 4}, {0, 0, 0, 9, 6, 5, 2, 7, 0}, {0, 6, 0, 0, 4, 3, 9, 0, 0}};
        int[][] PuzzSeedMed = {{8, 0, 0, 7, 9, 0, 0, 0, 5}, {0, 0, 0, 3, 0, 0, 6, 0, 0}, {9, 0, 0, 0, 6, 5, 1, 0, 8}, {0, 0, 0, 0, 0, 0, 0, 0, 6}, {6, 5, 0, 8, 3, 7, 0, 4, 1}, {2, 0, 0, 0, 0, 0, 0, 0, 0}, {7, 0, 5, 1, 8, 0, 0, 0, 9}, {0, 0, 2, 0, 0, 3, 0, 0, 0}, {3, 0, 0, 0, 5, 6, 0, 0, 2}};
        int[][] PuzzSeedHard = {{0, 0, 0, 3, 0, 0, 0, 6, 0}, {0, 0, 3, 0, 0, 1, 7, 0, 0}, {0, 8, 0, 0, 7, 0, 0, 0, 2}, {0, 2, 0, 6, 0, 3, 0, 9, 0}, {0, 6, 0, 0, 4, 0, 0, 5, 0}, {0, 1, 0, 7, 0, 9, 0, 3, 0}, {6, 0, 0, 0, 2, 0, 0, 4, 0}, {0, 0, 5, 9, 0, 0, 6, 0, 0}, {0, 3, 0, 0, 0, 6, 0, 0, 0}};
        if (i == 2) {
            copyarr(PuzzSeedHard, Puzzle);
        }
        else if (i == 1) {
            copyarr(PuzzSeedMed, Puzzle);
        }
        else {
            //// change testArr back to easy puzzle /////
            copyarr(testSeed, Puzzle);
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
    public void copyarr(int[][] arr1, int[][] arr2) {
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
        int[] numUsed = new int[a];
        zero(numUsed);
        int randPos;
        Random rand = new Random();
        int i = 0;
        while (i < a) {
            randPos = rand.nextInt(100);
            randPos = randPos%a;
            if (numUsed[randPos] == 0) { // if not used before
                arr[i] = randPos; // put rand num back in arr
                numUsed[randPos] = 1; // mark as used
                i++; // by putting i++ here this only moves on until it find valid num -- ie it keeps looping until find a num that was not used to far -- if pseudorand is good, should not cause infinite loop
            }
            //  // what if the same number is generated all over, the if statement doesnt cover that

        }
    }

    private void scramble(int[][] Puzzle)
    {
        int colrow = size;
        int tempSec;
        int puzzleSec;
        int[][] temparr = new int[colrow][colrow];
        copyarr(Puzzle, temparr);
        int[] order = new int[3];

        //swap rows in 3x9 until 9x9 fully swapped
        for (int i = 0; i < 3; i++) { //swap the 3 3x9 rows
            //ith box down
            randomize(order, 3);
            Log.d( "RAND", " -- " + order[0] + ", " + order[1] + ", " + order[2] );
            for (int j = 0; j < 3; j++) { //each row within a 3x9
                //row in box
                puzzleSec = (i * 3) + j; //go to a specific 1x9 row
                tempSec = (i * 3) + order[j]; //go to a specific row based on the randomizer
                for (int k = 0; k < colrow; k++) {
                    //col
                    Puzzle[puzzleSec][k] = temparr[tempSec][k]; //swap kth sqr in a 1x9 row of the original row with the sqr from the randomized row -- ie swap the two rows
                }
            }
        }

        //repeat 3x9 for col
        copyarr(Puzzle, temparr);
        //swap cols 9x3 until 9x9 fully swapped
        for (int i = 0; i < 3; i++) {
            //ith box to the right
            randomize(order, 3);
            Log.d( "RAND", " -- " + order[0] + ", " + order[1] + ", " + order[2] );
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

        // swap the 3x9 blocks
        copyarr(Puzzle, temparr);
        //swap row boxes
        randomize(order, 3);
        Log.d( "RAND", " -- " + order[0] + ", " + order[1] + ", " + order[2] );
        for (int i = 0; i < 3; i++) { // for each 3x9
            //ith box
            for (int j = 0; j < 3; j++) { // for each of the rows in a 3x9 - ie copy the 3 rows to another 3x9
                //row in the box
                puzzleSec = (i * 3) + j; // inside the ith 3x9
                tempSec = (order[i] * 3) + j; // swap with the chosen 3x9 from arr based on randomize() - ie this determines the row to copy from the chosen 3x9 based on randomize()
                for (int k = 0; k < colrow; k++) {
                    //col
                    Puzzle[puzzleSec][k] = temparr[tempSec][k]; // copy the row from randomize()
                }
            }
        }
        copyarr(Puzzle, temparr);
        //swap col boxes
        randomize(order, 3);
        Log.d( "RAND", " -- " + order[0] + ", " + order[1] + ", " + order[2] );
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

    /* private void randomizer2( int[][] Puzzle )
    {
        int colA;
        int colB;
        Random rand = new Random();
        int [][] tempArr = new int[9][9];

        copyarr(Puzzle, tempArr );


        //randomize a single 3x3 column
        for( int i=0; i<5; i++ ) //swap any random columns 5 times
        {
            colA =  rand.nextInt( ) % 3;  //caution, later add maxRandomCalls to prevent freeze
            colB =  rand.nextInt( ) % 3;  //caution, later add maxRandomCalls to prevent freeze
        }

        //to check if algo works, use a complete puzzle then scrambl then use PuzzleCheck to see if still valid
    }*/
}