package com.omicron.android.cmpt276_1191e1_omicron;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PuzzleTrack
{
	private int [][] matrix = new int[9][9]; //stores 2d array of which elements where filled so far
	private int sqrFilled;
	public boolean isCorrect;
	public boolean enableCheck; //flag to indicate if to allow to check puzzle for correctess

	public PuzzleTrack( int [][] matrix2 )
	{
		sqrFilled = 0;
		isCorrect = false;
		enableCheck = false;

		// based on original Puzzle matrix, make reference matrix
		for( int i=0; i<9; i++ )
		{
			for( int j=0; j<9; j++ )
			{
				matrix[i][j] = matrix2[i][j];
				//update how many squares are filled
				if( matrix2[i][j] != 0 )
				{
					sqrFilled ++;
				}
			}
		}

	}
	public void track(SudokuGenerator usrSudokuArr, Pair currentRectColoured, PuzzleCheck check ) // returns true if user
	{
		if( usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] == 0 ) //if sqr selected was not selected so far
		{
			sqrFilled ++;
		}

		//if puzzle filled, check puzzle
		if( sqrFilled == 81 )
		{
			enableCheck = true; //allow for puzzle to be checked
		}

		//// /////  make sure to deal with case where in last square being filled, it wont automatically stop the game if iputting wrong input
		// ie check for correct puzzle only when sqrFilled == sqrFilled - 1
	}

	public void checkPuzzle( SudokuGenerator usrSudokuArr, PuzzleCheck check, View v,
							 Button btn1, Button btn2, Button btn3, Button btn4, Button btn5,
							 Button btn6, Button btn7, Button btn8, Button btn9 )
	{
		check.PuzzleCheckStart(usrSudokuArr.Puzzle);
		isCorrect = check.isTrue;

		if (isCorrect)
		{
			//disable buttons
			Toast.makeText(v.getContext(), "CONGRATULATIONS!", Toast.LENGTH_SHORT).show();
			btn1.setOnClickListener(null);
			btn2.setOnClickListener(null);
			btn3.setOnClickListener(null);
			btn4.setOnClickListener(null);
			btn5.setOnClickListener(null);
			btn6.setOnClickListener(null);
			btn7.setOnClickListener(null);
			btn8.setOnClickListener(null);
			btn9.setOnClickListener(null);
		}
		else
		{
			Toast.makeText(v.getContext(), "INCORRECT", Toast.LENGTH_SHORT).show();
		}
	}
}
