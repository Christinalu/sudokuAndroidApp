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

	// will track and increase counter if a new cell was filled
	public void track(SudokuGenerator usrSudokuArr, Pair currentRectColoured )
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
	}

	// check if puzzle is correct, if it is then disable buttons
	public void checkPuzzle( SudokuGenerator usrSudokuArr, PuzzleCheck check, View v, Button [] btnArr)
	{
		check.PuzzleCheckStart(usrSudokuArr.Puzzle);
		isCorrect = check.isTrue;

		if (isCorrect)
		{
			//disable buttons
			Toast.makeText(v.getContext(), "CONGRATULATIONS!", Toast.LENGTH_SHORT).show();
			btnArr[0].setOnClickListener(null);
			btnArr[1].setOnClickListener(null);
			btnArr[2].setOnClickListener(null);
			btnArr[3].setOnClickListener(null);
			btnArr[4].setOnClickListener(null);
			btnArr[5].setOnClickListener(null);
			btnArr[6].setOnClickListener(null);
			btnArr[7].setOnClickListener(null);
			btnArr[8].setOnClickListener(null);
		}
		else
		{
			Toast.makeText(v.getContext(), "INCORRECT", Toast.LENGTH_SHORT).show();
		}
	}
}
