package com.omicron.android.cmpt276_1191e1_omicron;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class RedrawText
{
	/*
	 * This function is called to redraw the text overlay whenever the GUI is redrawn
	*/

	private float txtL;
	private float txtT;
	private float txtLO;
	private float txtTO;
	private SudokuGenerator usrSudokuArr;
	private Canvas canvas;
	private Paint paintblack;
	private Word[] wordArray;

	public RedrawText( float sqrLO2, float sqrTO2, SudokuGenerator usrSudokuArr2, Canvas canvas2,
					   Paint paintblack2, Word[] wordArray2 )
	{
		// import data on initialize
		txtLO = sqrLO2 + 9;
		txtTO = sqrTO2 + 105/2 + 10;
		usrSudokuArr = usrSudokuArr2;
		canvas = canvas2;
		paintblack = paintblack2;
		wordArray = wordArray2;
	}

	public void reDrawText( int usrLangPref )
	{
		for( int i=0; i<9; i++ ) //row
		{
			for( int j=0; j<9; j++ ) //column
			{
				//increase square dimensions
				txtL = txtLO + j*(105+5);
				txtT = txtTO + i*(105+5);

				//add padding
				if( i>=3 ) //add extra space between rows
				{
					txtT = txtT + 15;
				}
				if( i>=6 )
				{
					txtT = txtT + 15;
				}

				if( j>=3 ) //add extra space between columns
				{
					txtL = txtL + 15;
				}
				if( j>=6 )
				{
					txtL = txtL + 15;
				}


				// CHOOSE WHAT LANGUAGE TO DRAW
				// note: usrLangPref refers to text that the user is typing in the puzzle (ie if usrLangPref = 0 = native, then the (unchangeable) text inside puzzle is translation)
				// matrix text: text inside matrix that cannot be modified
				// user text: the text that the user inputs
				if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]!=0 && usrLangPref == 0 ) // draw only if the puzzle contains a number; and draw the native translated word
				{
					// matrix text - draw translation (because user chose = 0 = native, the matrix is =1=translation)
					canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j] - 1].getTranslation(), txtL, txtT, paintblack);
				}
				else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]!=0 && usrLangPref == 1)
				{
					// matrix text - draw native (because user chose = 1 = translation, the matrix is =0=native)
					canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j] - 1].getNative(), txtL, txtT, paintblack);
				}
				else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]==0 && usrLangPref == 0)
				{
					// user text - draw native (because user chose = 0 = native, draw user square native)
					canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j] - 1].getNative(), txtL, txtT, paintblack);
				}
				else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]==0 && usrLangPref == 1)
				{
					// user text - draw translation (because user chose = 1 = translation, draw user square translation)
					canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j] - 1].getTranslation(), txtL, txtT, paintblack);
				}
			}
		}
	}
}
