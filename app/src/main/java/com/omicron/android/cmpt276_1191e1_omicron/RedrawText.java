package com.omicron.android.cmpt276_1191e1_omicron;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.widget.TextView;

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
	private int SQR_INNER_DIVIDER;
	private int SQR_OUTER_DIVIDER;
	private int sqrSize;

	public RedrawText( float sqrLO2, float sqrTO2, SudokuGenerator usrSudokuArr2, Canvas canvas2,
					   Paint paintblack2, Word[] wordArray2, int SQR_INNER_DIVIDER_2, int SQR_OUTER_DIVIDER_2,
					   int sqrSize2 )
	{
		// import data on initialize
		sqrSize = sqrSize2;
		txtLO = sqrLO2 + 9;
		txtTO = sqrTO2 + ((float) sqrSize)/2 + 10;
		usrSudokuArr = usrSudokuArr2;
		canvas = canvas2;
		paintblack = paintblack2;
		wordArray = wordArray2;
		SQR_INNER_DIVIDER = SQR_INNER_DIVIDER_2;
		SQR_OUTER_DIVIDER = SQR_OUTER_DIVIDER_2;
	}

	public void chooseLangAndDraw(int i, int j, int usrLangPref, TextView textView )
	{
		// CHOOSE WHAT LANGUAGE TO DRAW IN  TEXTVIEW MARQUEE
		// note: usrLangPref refers to text that the user is typing in the puzzle (ie if usrLangPref = 0 = native, then the (unchangeable) text inside puzzle is translation)
		// matrix text: text inside matrix that cannot be modified
		// user text: the text that the user inputs
		if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]!=0 && usrLangPref == 0 ) // draw only if the puzzle contains a number; and draw the native translated word
		{
			// matrix text - draw translation (because user chose = 0 = native, the matrix is =1=translation)
			textView.setText( wordArray[usrSudokuArr.Puzzle[i][j] - 1].getTranslation() );
		}
		else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]!=0 && usrLangPref == 1)
		{
			// matrix text - draw native (because user chose = 1 = translation, the matrix is =0=native)
			textView.setText( wordArray[usrSudokuArr.Puzzle[i][j] - 1].getNative() );
		}
		else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]==0 && usrLangPref == 0)
		{
			// user text - draw native (because user chose = 0 = native, draw user square native)
			textView.setText( wordArray[usrSudokuArr.Puzzle[i][j] - 1].getNative() );
		}
		else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]==0 && usrLangPref == 1)
		{
			// user text - draw translation (because user chose = 1 = translation, draw user square translation)
			textView.setText( wordArray[usrSudokuArr.Puzzle[i][j] - 1].getTranslation() );
		}
	}

	public void reDrawText( int usrLangPref )
	{
		/*
		 * NOTE: This method is depreciated when using TextView marquee to draw
		 */

		for( int i=0; i<9; i++ ) //row
		{
			for( int j=0; j<9; j++ ) //column
			{
				//increase square dimensions
				txtL = txtLO + j*(sqrSize+SQR_INNER_DIVIDER);
				txtT = txtTO + i*(sqrSize+SQR_INNER_DIVIDER);

				//add padding
				if( i>=3 ) //add extra space between rows
				{
					txtT = txtT + SQR_OUTER_DIVIDER;
				}
				if( i>=6 )
				{
					txtT = txtT + SQR_OUTER_DIVIDER;
				}

				if( j>=3 ) //add extra space between columns
				{
					txtL = txtL + SQR_OUTER_DIVIDER;
				}
				if( j>=6 )
				{
					txtL = txtL + SQR_OUTER_DIVIDER;
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
