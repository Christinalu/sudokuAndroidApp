package com.omicron.android.cmpt276_1191e1_omicron;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PuzzleTrack
{
	private int [][] matrix; //stores 2d array of which elements where filled so far
	private int sqrFilled;
	public boolean isCorrect;
	public boolean enableCheck; //flag to indicate if to allow to check puzzle for correctess
	private int WORD_COUNT;
	
	public PuzzleTrack( int [][] matrix2, int WORD_COUNT2 )
	{
		sqrFilled = 0;
		isCorrect = false;
		enableCheck = false;
		WORD_COUNT = WORD_COUNT2;
		matrix = new int[WORD_COUNT][WORD_COUNT];

		// based on original Puzzle matrix, make reference matrix
		for( int i=0; i<WORD_COUNT; i++ )
		{
			for( int j=0; j<WORD_COUNT; j++ )
			{
				matrix[i][j] = matrix2[i][j];
				//update how many squares are already filled
				if( matrix2[i][j] != 0 )
				{
					sqrFilled ++;
				}
			}
		}

	}

	// will track and increase counter if a new cell was filled
	public void track(SudokuGenerator usrSudokuArr, Pair currentRectColoured )
	{
		if( usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] == 0 ) //if sqr selected was not selected so far
		{
			sqrFilled ++;
		}

		//if puzzle filled, check puzzle
		if( sqrFilled == WORD_COUNT*WORD_COUNT )
		{
			enableCheck = true; //allow for puzzle to be checked
		}
	}

	// check if puzzle is correct, if it is then disable buttons
	public void checkPuzzle( SudokuGenerator usrSudokuArr, PuzzleCheck check, View v, Button [] btnArr)
	{
		check.PuzzleCheckStart(usrSudokuArr.Puzzle);
		isCorrect = check.isTrue;

		if( isCorrect )
		{
			//disable buttons
			for( int i=0; i<WORD_COUNT; i++ )
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
}
