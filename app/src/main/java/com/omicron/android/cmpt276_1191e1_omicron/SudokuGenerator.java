package com.omicron.android.cmpt276_1191e1_omicron;
import android.util.Log;

import java.io.Serializable;
import java.util.Random;
import static java.lang.Math.sqrt;

public class SudokuGenerator implements Serializable {

    /*
     *    -- IMPORTANT --
     *  This generating function can generate UNIQUE puzzles based on
     *  pre-defined unique puzzles.
    */

    //This class accepts a hardcoded Puzzle and randomizes it to generate a new puzzle which is guaranteed to be unique and the same difficulty and the puzzle taken in as the argument.
    private int size;
    public int[][] PuzzleOriginal;
    public int[][] Puzzle; //stores user input changes
    public int[][] PuzzleSol; //stores puzzle solution
    
    private int[][] testSeed9x9 = {{0,0,2,5,4,9,6,8,3},{6,4,5,8,7,3,2,1,9},{3,8,9,2,6,1,7,4,5},{4,9,6,3,2,7,8,5,1},{8,1,3,4,5,6,9,7,2},{2,5,7,1,9,8,4,3,6},{9,6,4,7,1,5,3,2,8},{7,3,1,6,8,2,5,9,4},{5,2,8,9,3,4,1,6,7}};
	private int[][] testSeed9x9Solution = {{1,7,2,5,4,9,6,8,3},{6,4,5,8,7,3,2,1,9},{3,8,9,2,6,1,7,4,5},{4,9,6,3,2,7,8,5,1},{8,1,3,4,5,6,9,7,2},{2,5,7,1,9,8,4,3,6},{9,6,4,7,1,5,3,2,8},{7,3,1,6,8,2,5,9,4},{5,2,8,9,3,4,1,6,7}};
	private int[][] testSeed6x6 = {{0,0,0,0,6,0},{3,0,0,0,0,0},{0,2,0,0,3,0,},{6,0,0,4,0,0,},{0,1,0,3,0,4},{0,0,0,0,0,5}};
	private int[][] testSeed6x6Solution = {{1,5,4,2,6,3},{3,6,2,5,4,1},{4,2,5,1,3,6,},{6,3,1,4,5,2},{5,1,6,3,2,4},{2,4,3,6,1,5}};
	private int[][] testSeed4x4 = {{3,0,4,2},{0,0,0,0},{0,0,0,0},{2,0,0,3}};
	private int[][] testSeed4x4Solution = {{3,1,4,2},{4,2,3,1},{1,3,2,4},{2,4,1,3}};
	private int[][] testSeed12x12 = {{7,0,0,0,0,0,0,6,0,0,0,0},{0,5,6,0,0,0,0,0,4,0,1,0},{0,0,0,10,3,0,0,0,2,6,12,0},{2,0,0,3,0,7,12,0,0,10,0,4},{0,9,1,0,0,8,0,4,0,0,7,5},{4,7,0,0,0,2,0,0,12,0,0,0},{0,0,0,9,0,0,10,0,0,0,5,6},{5,4,0,0,11,0,6,0,0,3,2,0},{10,0,7,0,0,9,5,0,8,0,0,12},{0,3,12,2,0,0,0,11,10,0,0,0},{0,11,0,8,0,0,0,0,0,1,6,0},{0,0,0,0,9,0,0,0,0,0,0,8}};
	private int[][] testSeed12x12Solution = {{7,12,2,4,1,11,9,6,5,8,10,3},{3,5,6,11,8,10,2,12,4,9,1,7},{8,1,9,10,3,4,7,5,2,6,12,11},{2,8,11,3,5,7,12,1,6,10,9,4},{12,9,1,6,10,8,11,4,3,2,7,5},{4,7,10,5,6,2,3,9,12,11,8,1},{11,2,3,9,4,12,10,8,1,7,5,6},{5,4,8,12,11,1,6,7,9,3,2,10},{10,6,7,1,2,9,5,3,8,4,11,12},{1,3,12,2,7,6,8,11,10,5,4,9},{9,11,5,8,12,3,4,10,7,1,6,2},{6,10,4,7,9,5,1,2,11,12,3,8}};
    
    
    public SudokuGenerator(int i)
    {
        

        //test array
        //int[][] testSeed = {{0,3,0,4,9,1,2,5,8},{4,8,2,5,7,3,6,9,1},{9,1,5,6,2,8,4,3,7},{8,2,9,1,3,7,5,4,6},{3,4,1,2,6,5,8,7,9},{5,6,7,8,4,9,1,2,3},{7,5,6,9,1,2,3,8,4},{1,3,8,7,5,4,9,6,2},{2,9,4,3,8,6,7,1,5}};
		
		int[][] PuzzSeedEasy = {{0, 0, 7, 2, 8, 0, 0, 5, 0}, {0, 5, 6, 1, 7, 9, 0, 0, 0}, {1, 2, 0, 0, 0, 6, 0, 4, 0}, {8, 0, 0, 0, 1, 0, 0, 2, 3}, {0, 0, 0, 0, 9, 0, 0, 0, 0}, {6, 4, 0, 0, 3, 0, 0, 0, 7}, {0, 8, 0, 7, 0, 0, 0, 3, 4}, {0, 0, 0, 9, 6, 5, 2, 7, 0}, {0, 6, 0, 0, 4, 3, 9, 0, 0}};
		int[][] PuzzSeedEasySol = {{9, 3, 7, 2, 8, 4, 1, 5, 6}, {4, 5, 6, 1, 7, 9, 3, 8, 2}, {1, 2, 8, 3, 5, 6, 7, 4, 9}, {8, 9, 5, 6, 1, 7, 4, 2, 3}, {2, 7, 3, 4, 9, 8, 5, 6, 1}, {6, 4, 1, 5, 3, 2, 8, 9, 7}, {5, 8, 9, 7, 2, 1, 6, 3, 4}, {3, 1, 4, 9, 6, 5, 2, 7, 8}, {7, 6, 2, 8, 4, 3, 9, 1, 5}};
		int[][] PuzzSeedMed = {{8, 0, 0, 7, 9, 0, 0, 0, 5}, {0, 0, 0, 3, 0, 0, 6, 0, 0}, {9, 0, 0, 0, 6, 5, 1, 0, 8}, {0, 0, 0, 0, 0, 0, 0, 0, 6}, {6, 5, 0, 8, 3, 7, 0, 4, 1}, {2, 0, 0, 0, 0, 0, 0, 0, 0}, {7, 0, 5, 1, 8, 0, 0, 0, 9}, {0, 0, 2, 0, 0, 3, 0, 0, 0}, {3, 0, 0, 0, 5, 6, 0, 0, 2}};
		int[][] PuzzSeedMedSol = {{8, 3, 6, 7, 9, 1, 4, 2, 5}, {5, 2, 1, 3, 4, 8, 6, 9, 7}, {9, 7, 4, 2, 6, 5, 1, 3, 8}, {4, 1, 3, 5, 2, 9, 8, 7, 6}, {6, 5, 9, 8, 3, 7, 2, 4, 1}, {2, 8, 7, 6, 1, 4, 9, 5, 3}, {7, 4, 5, 1, 8, 2, 3, 6, 9}, {1, 6, 2, 9, 7, 3, 5, 8, 4}, {3, 9, 8, 4, 5, 6, 7, 1, 2}};
		int[][] PuzzSeedHard = {{0, 0, 0, 3, 0, 0, 0, 6, 0}, {0, 0, 3, 0, 0, 1, 7, 0, 0}, {0, 8, 0, 0, 7, 0, 0, 0, 2}, {0, 2, 0, 6, 0, 3, 0, 9, 0}, {0, 6, 0, 0, 4, 0, 0, 5, 0}, {0, 1, 0, 7, 0, 9, 0, 3, 0}, {6, 0, 0, 0, 2, 0, 0, 4, 0}, {0, 0, 5, 9, 0, 0, 6, 0, 0}, {0, 3, 0, 0, 0, 6, 0, 0, 0}};
        int[][] PuzzSeedHardSol = {{2, 7, 1, 3, 9, 8, 5, 6, 4}, {4, 5, 3, 2, 6, 1, 7, 8, 9}, {9, 8, 6, 5, 7, 4, 3, 1, 2}, {7, 2, 8, 6, 5, 3, 4, 9, 1}, {3, 6, 9, 1, 4, 2, 8, 5, 7}, {5, 1, 4, 7, 8, 9, 2, 3, 6}, {6, 9, 7, 8, 2, 5, 1, 4, 3}, {1, 4, 5, 9, 3, 7, 6, 2, 8}, {8, 3, 2, 4, 1, 6, 9, 7, 5}};

        
	
		size = 9;
		PuzzleOriginal = new int[size][size];
		Puzzle = new int[size][size];
		PuzzleSol = new int[size][size];
		
		/*if (i == 2) {
            copyarr(PuzzSeedHard, Puzzle);
            copyarr(PuzzSeedHardSol, PuzzleSol);
        }
        else if (i == 1) {
            copyarr(PuzzSeedMed, Puzzle);
            copyarr(PuzzSeedMedSol, PuzzleSol);
        }
        else {
            copyarr(PuzzSeedEasy, Puzzle);
            copyarr(PuzzSeedEasySol, PuzzleSol);
        }*/
        
        // TODO: remove the following once implementation complete
	
		copyarr( PuzzSeedEasy, PuzzleOriginal );
		copyarr( PuzzSeedEasy, Puzzle );
		copyarr( PuzzSeedEasySol, PuzzleSol );
    
        // TODO: enable scramble() and copyarr()
        
        //scramble(Puzzle, PuzzleSol);
        //copyarr( Puzzle, PuzzleOriginal );
        
        Log.d( "selectW", "puzzle solution:" );
        
        // TODO: fix printArr()
        
        //printArr( "selectW", PuzzleSol );
        Log.d( "selectW", "original:" );
		//printArr( "selectW", PuzzleOriginal );
	
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
        for( int i=0; i<size; i++ )
        {
            Log.d("MATRIX", " " + PuzzleSol[i][0] + " " + PuzzleSol[i][1] + " " + PuzzleSol[i][2]+ " " + PuzzleSol[i][3]+ " " + PuzzleSol[i][4]+ " " + PuzzleSol[i][5]+ " " + PuzzleSol[i][6]+ " " + PuzzleSol[i][7]+ " " + PuzzleSol[i][8] );
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
    private void scramble(int[][] Puzzle, int[][] PuzzleSol)
    {
        int colrow = size;
        int tempSec;
        int puzzleSec;
        int[][] temparr = new int[colrow][colrow];
        int[][] tempsolarr = new int[colrow][colrow];
        copyarr(Puzzle, temparr);
        copyarr(PuzzleSol, tempsolarr);
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
                    PuzzleSol[puzzleSec][k] = tempsolarr[tempSec][k]; //swap the two rows
                }
            }
        }

        // repeat 3x9 for col
        copyarr(Puzzle, temparr);
        copyarr(PuzzleSol, tempsolarr);
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
                    PuzzleSol[k][puzzleSec] = tempsolarr[k][tempSec];
                }
            }
        }

        // swap the 3x9 blocks
        copyarr(Puzzle, temparr);
        copyarr(PuzzleSol, tempsolarr);
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
                    PuzzleSol[puzzleSec][k] = tempsolarr[tempSec][k]; // copy the row from randomize()
                }
            }
        }
        copyarr(Puzzle, temparr);
        copyarr(PuzzleSol, tempsolarr);
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
                    PuzzleSol[k][puzzleSec] = tempsolarr[k][tempSec];
                }
            }
        }
    }
	
	public int[][] getSolution( )
	{ return PuzzleSol; }
}