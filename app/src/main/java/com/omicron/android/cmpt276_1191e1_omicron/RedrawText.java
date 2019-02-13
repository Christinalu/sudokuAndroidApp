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
	private float sqrTO3; //backup
	private float sqrLO3; //backup
	private PairF[][] puzzleLoc;
	private SudokuGenerator usrSudokuArr;
	private Canvas canvas;
	private Paint paintblack;
	private Word[] wordArray;
	private int[] zoomOn;
	//private int usrLangPref;

	public RedrawText( float txtL2, float txtT2, float sqrLO2, float sqrTO2, PairF[][] puzzleLoc2,
					   SudokuGenerator usrSudokuArr2, Canvas canvas2, Paint paintblack2,
					   Word[] wordArray2, int[] zoomOn2 )
	{
		// import data on initialize
		txtL = txtL2;
		txtT = txtT2;
		sqrLO = sqrLO2;
		sqrTO = sqrTO2;
		sqrTO3 = sqrTO2; //set backup
		sqrLO3 = sqrLO2; //set backup
		puzzleLoc = puzzleLoc2;
		usrSudokuArr = usrSudokuArr2;
		canvas = canvas2;
		paintblack = paintblack2;
		wordArray = wordArray2;
		zoomOn = zoomOn2;
		//usrLangPref = usrLangPref2;
	}
	public void reDrawText( int usrLangPref )
	{
		/*if( zoomOn[0] == 1 ) //if zoom on, set offset
		{ sqrTO = 80; sqrLO = 27; }
		else{ sqrTO = sqrTO3; sqrLO = sqrLO3; }*/

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

				// choose what language to draw
				if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]!=0 && usrLangPref == 0 ) { // draw only if the puzzle contains a number; and draw the native translated word
					//draw translation
					if( zoomOn[0] == 1 )//colour on zoom mode
					{
						canvas.save( );
						canvas.scale( 2.0f, 2.0f );
						canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j]-1].getTranslation(), txtL, txtT, paintblack);
						canvas.restore( );
					}
					else {
						canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j] - 1].getTranslation(), txtL, txtT, paintblack);
					}
				}
				else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]!=0 && usrLangPref == 1)
				{
					//draw native
					if( zoomOn[0] == 1 )//colour on zoom mode
					{
						canvas.save( );
						canvas.scale( 2.0f, 2.0f );
						canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j]-1].getTranslation(), txtL, txtT, paintblack);
						canvas.restore( );
					}
					else {
						canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j] - 1].getTranslation(), txtL, txtT, paintblack);
					}
				}
				else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]==0 && usrLangPref == 0)
				{
					//draw native
					if( zoomOn[0] == 1 )//colour on zoom mode
					{
						canvas.save( );
						canvas.scale( 2.0f, 2.0f );
						canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j]-1].getNative(), txtL, txtT, paintblack);
						canvas.restore( );
					}
					else {
						canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j] - 1].getNative(), txtL, txtT, paintblack);
					}
				}
				else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]==0 && usrLangPref == 1)
				{
					//draw native
					if( zoomOn[0] == 1 )//colour on zoom mode
					{
						canvas.save( );
						canvas.scale( 2.0f, 2.0f );
						canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j]-1].getNative(), txtL, txtT, paintblack);
						canvas.restore( );
					}
					else {
						canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j] - 1].getNative(), txtL, txtT, paintblack);
					}
				}
			}
		}
	}
}
