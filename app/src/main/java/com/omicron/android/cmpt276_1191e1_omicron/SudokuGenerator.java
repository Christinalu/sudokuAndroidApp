package com.omicron.android.cmpt276_1191e1_omicron;
import android.util.Log;

import java.io.Serializable;
import java.util.Random;
import static java.lang.Math.sqrt;

public class SudokuGenerator implements Serializable {

    /*
     *    -- IMPORTANT --
     *  This generating function can generate UNIQUE puzzles based on
     *  pre-defined unique puzzles. One puzzle can be used to
     *  generate approximately (3!)^8 = 1679616 new puzzles
    */

    //This class accepts a hardcoded Puzzle and randomizes it to generate a new puzzle which is guaranteed to be unique and the same difficulty and the puzzle taken in as the argument.
    private int size;
    public int[][] PuzzleOriginal;
	public int[][] Puzzle; //stores user input changes
    
    //test array
    public int[][] testSeedSolution = {{1,7,2,5,4,9,6,8,3},{6,4,5,8,7,3,2,1,9},{3,8,9,2,6,1,7,4,5},{4,9,6,3,2,7,8,5,1},{8,1,3,4,5,6,9,7,2},{2,5,7,1,9,8,4,3,6},{9,6,4,7,1,5,3,2,8},{7,3,1,6,8,2,5,9,4},{5,2,8,9,3,4,1,6,7}};
    public int[][] testSeed = {{0,0,0,0,0,0,0,0,0},{6,4,5,8,7,3,2,1,9},{3,8,9,2,6,1,7,4,5},{4,9,6,3,2,7,8,5,1},{8,1,3,4,5,6,9,7,2},{2,5,7,1,9,8,4,3,6},{9,6,4,7,1,5,3,2,8},{7,3,1,6,8,2,5,9,4},{5,2,8,9,3,4,1,6,7}};
    
    
    public SudokuGenerator(int i)
    {
        size = 9;
        PuzzleOriginal = new int[size][size];
		Puzzle = new int[size][size];
		
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
            copyarr( PuzzSeedEasy, Puzzle);
        }
		
        scramble(Puzzle);
        copyarr( Puzzle, PuzzleOriginal );
        
        
        Log.d( "selectW", "puzzle solution:" );
        printArr( "selectW", PuzzleSol );
    }

    // test method to print for debugging
    public void printArr( String tag, int[][] arr )
	{
		for( int i=0; i<size; i++ )
		{
			Log.d(tag, " " + arr[i][0] + " " + arr[i][1] + " " + arr[i][2]+ " " + arr[i][3]+ " " + arr[i][4]+ " " + arr[i][5]+ " " + arr[i][6]+ " " + arr[i][7]+ " " + arr[i][8] );
		}
		Log.d( tag," \n\n");
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

	//zero an array
    private void zero(int[] arr)
    {
        for (int i=0; i<arr.length; i++) arr[i] = 0;
    }

    //copy an array into another
    public void copyarr(int[][] arr1, int[][] arr2)
    {
        int colrow = size;
        for (int i=0; i<colrow; i++) {
            for (int j=0; j<colrow; j++) {
                arr2[i][j] = arr1[i][j];
            }
        }
    }

    //function takes an int array of size a that will be used store a randomized order vector
    private void randomize(int[] arr, int a)
    {
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
                i++; // by putting i++ here this only moves on until it find valid num
            }
        }
    }

    // takes a 2D puzzle array and will shuffle rows/columns to create a new puzzle with unique solution
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
                    Puzzle[puzzleSec][k] = temparr[tempSec][k]; //swap the two rows
                }
            }
        }

        // repeat 3x9 for col
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
            for (int j = 0; j < 3; j++) { // for each of the rows in a 3x9
                //row in the box
                puzzleSec = (i * 3) + j; // inside the ith 3x9
                tempSec = (order[i] * 3) + j; // swap with the chosen 3x9 from arr based on randomize()
                for (int k = 0; k < colrow; k++) {
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
    
    public int[][] getPuzzleOriginalSolution( )
	{ return PuzzleOriginalSolution; }
	
	public int[][] getTestSeedSolution( )
	{ return testSeedSolution; }
}