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

	private int[] touchX = { 0 };
	private int[] touchY = { 0 };

	public ButtonListener(final Pair currentRectColoured2, final SudokuGenerator usrSudokuArr2, final RedrawText textOverlay2,
						  final Button btn1, final Button btn2, final Button btn3, final Button btn4, final Button btn5,
						  final Button btn6, final Button btn7, final Button btn8, final Button btn9, final drw drawR,
						  int[] touchX2, int[] touchY2, Pair lastRectColoured2, final int usrLangPref )
	{
		final Pair currentRectColoured = currentRectColoured2;
		final SudokuGenerator usrSudokuArr = usrSudokuArr2;
		final RedrawText textOverlay = textOverlay2;
		final Pair lastRectColoured = lastRectColoured2;

		// pulled out of button listeners
		final PuzzleCheck check = new PuzzleCheck(usrSudokuArr.Puzzle);
		final PuzzleTrack track = new PuzzleTrack( usrSudokuArr.Puzzle );

		Log.d( "ERROR-2", "before touch[0] inside buttonListener" );

		touchX[0] = touchX2[0];
		touchY[0] = touchY2[0];

		if( textOverlay == null )
		{
			Log.d( "NULL-2", "textOverlay on initialize call in ButtonListener class" );
		}

		Log.d( "ERROR-2", "start on inside of Button Listener" );

		//btn1 = (Button) findViewById(R.id.keypad_1);
		btn1.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v)
									{
										//Toast.makeText(GameActivity.this, "BTN-1", Toast.LENGTH_SHORT).show();

										//if current button selected is valid and is not restricted
										if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()	] == 0 )
										{
											// increase the count of inserted numbers
											track.track( usrSudokuArr, currentRectColoured, check ); //important, track must occur before usrSudokuArr.Puzzle[][] = 1

											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 1;

											// redraw square matrix and text overlay
											drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, false, usrLangPref );
											textOverlay.reDrawText( usrLangPref );
											Log.d( "MATRIX", " changed entry" );

											//removed test check is puzzle was correct
											//improved -will only check for puzzle only on last user input, instead of every time the user clicker a button (which was pointless since the puzzleCheck by default will be false because its incomplete)

											//have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
											if( track.enableCheck )
											{
												track.checkPuzzle(usrSudokuArr, check, v, btn1, btn2, btn3,
														btn4, btn5, btn6, btn7, btn8, btn9);
											}
										}
										usrSudokuArr.printCurrent( );
									}
								}
		);

		//Button btn2 = (Button) findViewById(R.id.keypad_2);
		btn2.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										//Toast.makeText(GameActivity.this, "BTN-2", Toast.LENGTH_SHORT).show();
										//if current button selected is valid and is not restricted
										if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()	] == 0 )
										{
											// increase the count of inserted numbers
											track.track( usrSudokuArr, currentRectColoured, check ); //important, track must occur before usrSudokuArr.Puzzle[][] = 1

											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 2;
											// redraw text overlay
											drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, false, usrLangPref );
											textOverlay.reDrawText( usrLangPref );
											Log.d( "MATRIX", " changed entry" );

											//have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
											if( track.enableCheck )
											{
												track.checkPuzzle(usrSudokuArr, check, v, btn1, btn2, btn3,
														btn4, btn5, btn6, btn7, btn8, btn9);
											}
										}
										usrSudokuArr.printCurrent( );
									}
								}
		);

		//Button btn3 = (Button) findViewById(R.id.keypad_3);
		btn3.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										//Toast.makeText(GameActivity.this, "BTN-3", Toast.LENGTH_SHORT).show();
										//if current button selected is valid and is not restricted
										if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()	] == 0 )
										{
											// increase the count of inserted numbers
											track.track( usrSudokuArr, currentRectColoured, check ); //important, track must occur before usrSudokuArr.Puzzle[][] = 1

											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 3;
											// redraw text overlay
											drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, false, usrLangPref );
											textOverlay.reDrawText( usrLangPref );
											Log.d( "MATRIX", " changed entry" );

											//have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
											if( track.enableCheck )
											{
												track.checkPuzzle(usrSudokuArr, check, v, btn1, btn2, btn3,
														btn4, btn5, btn6, btn7, btn8, btn9);
											}
										}
										usrSudokuArr.printCurrent( );
									}
								}
		);

		//Button btn4 = (Button) findViewById(R.id.keypad_4);
		btn4.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										//Toast.makeText(GameActivity.this, "BTN-4", Toast.LENGTH_SHORT).show();
										//if current button selected is valid and is not restricted
										if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()	] == 0 )
										{
											// increase the count of inserted numbers
											track.track( usrSudokuArr, currentRectColoured, check ); //important, track must occur before usrSudokuArr.Puzzle[][] = 1

											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 4;
											// redraw text overlay
											drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, false, usrLangPref );
											textOverlay.reDrawText( usrLangPref );
											Log.d( "MATRIX", " changed entry" );

											//have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
											if( track.enableCheck )
											{
												track.checkPuzzle(usrSudokuArr, check, v, btn1, btn2, btn3,
														btn4, btn5, btn6, btn7, btn8, btn9);
											}
										}
										usrSudokuArr.printCurrent( );
									}
								}
		);

		//Button btn5 = (Button) findViewById(R.id.keypad_5);
		btn5.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										//Toast.makeText(GameActivity.this, "BTN-5", Toast.LENGTH_SHORT).show();
										//if current button selected is valid and is not restricted
										if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()	] == 0 )
										{
											// increase the count of inserted numbers
											track.track( usrSudokuArr, currentRectColoured, check ); //important, track must occur before usrSudokuArr.Puzzle[][] = 1

											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 5;
											// redraw text overlay
											drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, false, usrLangPref );
											textOverlay.reDrawText( usrLangPref );
											Log.d( "MATRIX", " changed entry" );

											//have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
											if( track.enableCheck )
											{
												track.checkPuzzle(usrSudokuArr, check, v, btn1, btn2, btn3,
														btn4, btn5, btn6, btn7, btn8, btn9);
											}
										}
										usrSudokuArr.printCurrent( );
									}
								}
		);

		//Button btn6 = (Button) findViewById(R.id.keypad_6);
		btn6.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										//Toast.makeText(GameActivity.this, "BTN-6", Toast.LENGTH_SHORT).show();
										//if current button selected is valid and is not restricted
										if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()	] == 0 )
										{
											// increase the count of inserted numbers
											track.track( usrSudokuArr, currentRectColoured, check ); //important, track must occur before usrSudokuArr.Puzzle[][] = 1

											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 6;
											// redraw text overlay
											drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, false, usrLangPref );
											textOverlay.reDrawText( usrLangPref );
											Log.d( "MATRIX", " changed entry" );

											//have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
											if( track.enableCheck )
											{
												track.checkPuzzle(usrSudokuArr, check, v, btn1, btn2, btn3,
														btn4, btn5, btn6, btn7, btn8, btn9);
											}
										}
										usrSudokuArr.printCurrent( );

									}
								}
		);

		//Button btn7 = (Button) findViewById(R.id.keypad_7);
		btn7.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										//Toast.makeText(GameActivity.this, "BTN-7", Toast.LENGTH_SHORT).show();
										//if current button selected is valid and is not restricted
										if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()	] == 0 )
										{
											// increase the count of inserted numbers
											track.track( usrSudokuArr, currentRectColoured, check ); //important, track must occur before usrSudokuArr.Puzzle[][] = 1

											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 7;
											// redraw text overlay
											drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, false, usrLangPref );
											textOverlay.reDrawText( usrLangPref );
											Log.d( "MATRIX", " changed entry" );

											//have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
											if( track.enableCheck )
											{
												track.checkPuzzle(usrSudokuArr, check, v, btn1, btn2, btn3,
														btn4, btn5, btn6, btn7, btn8, btn9);
											}
										}
										usrSudokuArr.printCurrent( );
									}
								}
		);

		//Button btn8 = (Button) findViewById(R.id.keypad_8);
		btn8.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										//Toast.makeText(GameActivity.this, "BTN-8", Toast.LENGTH_SHORT).show();
										//if current button selected is valid and is not restricted
										if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()	] == 0 )
										{
											// increase the count of inserted numbers
											track.track( usrSudokuArr, currentRectColoured, check ); //important, track must occur before usrSudokuArr.Puzzle[][] = 1

											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 8;
											// redraw text overlay
											drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, false, usrLangPref );
											textOverlay.reDrawText( usrLangPref );
											Log.d( "MATRIX", " changed entry" );

											//have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
											if( track.enableCheck )
											{
												track.checkPuzzle(usrSudokuArr, check, v, btn1, btn2, btn3,
														btn4, btn5, btn6, btn7, btn8, btn9);
											}
										}
										usrSudokuArr.printCurrent( );
									}
								}
		);

		//Button btn9 = (Button) findViewById(R.id.keypad_9);
		btn9.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										//Toast.makeText(GameActivity.this, "BTN-9", Toast.LENGTH_SHORT).show();
										//if current button selected is valid and is not restricted
										if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()	] == 0 )
										{
											// increase the count of inserted numbers
											track.track( usrSudokuArr, currentRectColoured, check ); //important, track must occur before usrSudokuArr.Puzzle[][] = 1

											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 9;
											// redraw text overlay
											drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, false, usrLangPref );
											textOverlay.reDrawText( usrLangPref );;
											Log.d( "MATRIX", " changed entry" );

											//have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
											if( track.enableCheck )
											{
												track.checkPuzzle(usrSudokuArr, check, v, btn1, btn2, btn3,
														btn4, btn5, btn6, btn7, btn8, btn9);
											}
										}
										usrSudokuArr.printCurrent( );
									}
								}
		);
	}
}
