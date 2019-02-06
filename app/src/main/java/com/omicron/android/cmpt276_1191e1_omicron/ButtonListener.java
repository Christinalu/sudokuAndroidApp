package com.omicron.android.cmpt276_1191e1_omicron;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class ButtonListener extends AppCompatActivity
{
	//public Pair currentRectColoured;
	//public SudokuGenerator usrSudokuArr;

	public ButtonListener(final Pair currentRectColoured2, final SudokuGenerator usrSudokuArr2,
						  Button btn1, Button btn2, Button btn3, Button btn4, Button btn5,
						  Button btn6, Button btn7, Button btn8, Button btn9 )
	{
		final Pair currentRectColoured = currentRectColoured2;
		final SudokuGenerator usrSudokuArr = usrSudokuArr2;

		//btn1 = (Button) findViewById(R.id.keypad_1);
		btn1.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v)
									{
										//Toast.makeText(GameActivity.this, "BTN-1", Toast.LENGTH_SHORT).show();

										//if current button selected is valid and is not restricted
										if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()	] == 0 )
										{
											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 1;
											Log.d( "MATRIX", " changed entry" );
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
											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 2;
											Log.d( "MATRIX", " changed entry" );
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
											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 3;
											Log.d( "MATRIX", " changed entry" );
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
											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 4;
											Log.d( "MATRIX", " changed entry" );
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
											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 5;
											Log.d( "MATRIX", " changed entry" );
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
											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 6;
											Log.d( "MATRIX", " changed entry" );
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
											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 7;
											Log.d( "MATRIX", " changed entry" );
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
											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 8;
											Log.d( "MATRIX", " changed entry" );
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
											usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] = 9;
											Log.d( "MATRIX", " changed entry" );
										}
										usrSudokuArr.printCurrent( );
									}
								}
		);
	}
}
