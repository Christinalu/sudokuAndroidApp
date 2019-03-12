package com.omicron.android.cmpt276_1191e1_omicron;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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


	public ButtonListener(final Pair currentRectColoured, final SudokuGenerator usrSudokuArr, final Button[] btnArr, final drw drawR,
						  final int[] touchX, final int[] touchY, final Pair lastRectColoured,
						  final int usrLangPref, final int[] btnClicked, final TextView Hint, final Word[] wordArray,final int usrModePref,final String[] numArray )
	{
		// pulled out of button listeners
		final PuzzleCheck check = new PuzzleCheck(usrSudokuArr.Puzzle);
		final PuzzleTrack track = new PuzzleTrack( usrSudokuArr.Puzzle );
		final Handler handler = new Handler();


		// loop to set up all keypad buttons
		for( i=0; i<9; i++ )
		{
			final int var;
			var = i + 1;
			final int index;
			index=i;
			btnArr[i].setOnLongClickListener(new View.OnLongClickListener() {
												 @SuppressLint("SetTextI18n")
												 @Override
												 public boolean onLongClick(View v) {
												     Hint.setBackgroundColor(R.drawable.buttons);
                                                     final Drawable buttonBackground = btnArr[index].getBackground();
                                                     btnArr[index].setBackgroundColor(R.drawable.buttons);
													 if(usrLangPref==0){
													     if(usrModePref==1){
                                                             Hint.setText(wordArray[index].getNative() + " : " + numArray[index]);
                                                             Log.d( "Counter","wordArr[" + index + "]: " + wordArray[index].getNative() + "," + wordArray[index].getTranslation() + "," + wordArray[index].getInFileLineNum() + "," + wordArray[index].getHintClick()+"," + numArray[index] );
                                                         }
                                                         else {
                                                             Hint.setText(wordArray[index].getNative() + " : " + wordArray[index].getTranslation());
                                                             Log.d( "Counter","wordArr[" + index + "]: " + wordArray[index].getNative() + "," + wordArray[index].getTranslation() + "," + wordArray[index].getInFileLineNum() + "," + wordArray[index].getHintClick() );
                                                         }
													 }
													 else {
                                                         if (usrModePref == 1) {
                                                             Hint.setText(wordArray[index].getTranslation() + " : " + numArray[index]);
                                                             Log.d("Counter", "wordArr[" + index + "]: " + wordArray[index].getNative() + "," + wordArray[index].getTranslation() + "," + wordArray[index].getInFileLineNum() + "," + wordArray[index].getHintClick() + "," + numArray[index]);
                                                         } else {
                                                             Hint.setText(wordArray[index].getTranslation() + " : " + wordArray[index].getNative());
                                                             Log.d("Counter", "wordArr[" + index + "]: " + wordArray[index].getNative() + "," + wordArray[index].getTranslation() + "," + wordArray[index].getInFileLineNum() + "," + wordArray[index].getHintClick());
                                                         }
                                                     }
													 wordArray[index].incrementHintClick();


													 handler.postDelayed(new Runnable() {
														 @Override
														 public void run() {
															 // Do something after 5s = 5000ms
                                                             Hint.setBackgroundColor(Color.TRANSPARENT);
															 Hint.setText("");
                                                             btnArr[index].setBackground(buttonBackground);
                                                     }
													 }, 4000);


													 return true;
												 }
											 }


			);
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
													 btnClicked[0] = 1; //this flag allows (for efficiency) class drw to update TextView as well in zoom mode
													 drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, usrLangPref );
													 btnClicked[0] = 0;
													 //textOverlay.reDrawText( usrLangPref );

													 int a = currentRectColoured.getRow();
													 int b = currentRectColoured.getColumn();
													 
													 //check if word inserted is correct (used to decrease probability of word being selected in Select9Word() )
													 if( var == usrSudokuArr.getPuzzleOriginalSolution()[a][b] ) //if input matches solution
													 {
													 	Log.d( "selectW", "user sqr input correct" );
													 	if( wordArray[var].getAlreadyUsedInGame() == false ) //if correctly using this word for the first time in game
														{
															wordArray[var].setUsedInGame(); //mark as used
															wordArray[var].setToAllowToDecreaseDifficulty( ); //allow for difficulty to be decreased
														}
													 }
													 
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
