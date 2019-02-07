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
	private float sqrLO;
	private float sqrTO;
	private PairF[][] puzzleLoc;
	private SudokuGenerator usrSudokuArr;
	private Canvas canvas;
	private Paint paintblack;
	private Word[] wordArray;

	public RedrawText( float txtL2, float txtT2, float sqrLO2, float sqrTO2, PairF[][] puzzleLoc2,
					   SudokuGenerator usrSudokuArr2, Canvas canvas2, Paint paintblack2, Word[] wordArray2 )
	{
		// import data on initialize
		txtL = txtL2;
		txtT = txtT2;
		sqrLO = sqrLO2;
		sqrTO = sqrTO2;
		puzzleLoc = puzzleLoc2;
		usrSudokuArr = usrSudokuArr2;
		canvas = canvas2;
		paintblack = paintblack2;
		wordArray = wordArray2;
	}
	public void reDrawText(  )
	{
		Log.d( "MATRIX", "--PASS" );
		for( int i=0; i<9; i++ ) //row
		{
			for( int j=0; j<9; j++ ) //column
			{
				//increase square dimensions
				txtL = sqrLO + j*(105+5);
				txtT = sqrTO + i*(105+5);


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
				puzzleLoc[i][j] = new PairF(txtT,txtL);

				if (usrSudokuArr.Puzzle[i][j]!=0) { // draw only if the original puzzle contains a number
					canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j]-1].getTranslation(), txtL, txtT, paintblack);
				}
			}
		}
	}
}
