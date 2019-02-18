package com.omicron.android.cmpt276_1191e1_omicron;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class ButtonListener extends AppCompatActivity
{
	/*
	 *	This class is used to save space from GameActivity
	 *	it initializes all buttons
	 *	this also contains code that will let buttons respond by,
	 *	if selecting valid square, will update PuzzleOriginal
	*/

	private int i;

	public ButtonListener(final Pair currentRectColoured, final SudokuGenerator usrSudokuArr,
						  final RedrawText textOverlay, final Button[] btnArr, final drw drawR,
						  final int[] touchX, final int[] touchY, final Pair lastRectColoured,
						  final int usrLangPref )
	{
		// pulled out of button listeners
		final PuzzleCheck check = new PuzzleCheck(usrSudokuArr.Puzzle);
		final PuzzleTrack track = new PuzzleTrack( usrSudokuArr.Puzzle );

		if( textOverlay == null )
		{
			Log.d( "NULL-2", "textOverlay on initialize call in ButtonListener class" );
		}

		// loop to set up all keypad buttons
		for( i=0; i<9; i++ )
		{
			final int var = i + 1;
			btnArr[i].setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v)
										{
											//if current button selected is valid and is not restricted
											if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()	] == 0 )
											{
												// increase the count of inserted numbers if needed
												track.track( usrSudokuArr, currentRectColoured ); //important, 'track' must occur before 'usrSudokuArr.Puzzle[][] = x'

												// set the cell in the Puzzle to corresponding number based on button user input
												//if( zoomButtonDisableUpdate[0] == 0 ) // do not update entry when switching modes - causes errors
												usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = var;

												// redraw square matrix and text overlay
												drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, usrLangPref );
												//textOverlay.reDrawText( usrLangPref );

												//have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
												if( track.enableCheck )
												{
													track.checkPuzzle( usrSudokuArr, check, v, btnArr );
												}
											}
											usrSudokuArr.printCurrent( );
										}
									}
			);
		}
	}
}
