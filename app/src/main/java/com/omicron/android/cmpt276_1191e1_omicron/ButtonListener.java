package com.omicron.android.cmpt276_1191e1_omicron;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class ButtonListener extends AppCompatActivity
{
	/*
	 *	This class is used to set up keypad buttons for GameActivity
	 *	This also contains code that will let buttons respond
	 *	by updating Puzzle
	 */

	private int i;
	private Button [] btnArr;
	
	
	public ButtonListener(final Pair currentRectColoured, final SudokuGenerator usrSudokuArr, final drw drawR,
						  final int[] touchX, final int[] touchY, final Pair lastRectColoured,
						  final int usrLangPref, final int[] btnClicked, final TextView Hint, final WordArray wordArray,
						  final int usrModePref, final String[] numArray, int WORD_COUNT, int COL_PER_BLOCK,
						  int ROW_PER_BLOCK, Context context, TableLayout tableLayout, int orientation)
	{
		// pulled out of button listeners
		final PuzzleCheck check = new PuzzleCheck(usrSudokuArr.Puzzle);
		final PuzzleTrack track = new PuzzleTrack( usrSudokuArr.Puzzle, WORD_COUNT );
		final Handler handler = new Handler();
		int rowCount;
		int btnCount;
		int indexArr = 0;
		
			/** SET BUTTON LISTENERS **/
		
		btnArr = new Button[WORD_COUNT]; // set buttons as an array
		
		Log.d( "listener", "WORD_COUNT: " + WORD_COUNT );
		if( btnArr == null){ Log.d( "listener", "ERROR: null Button[]" ); }
		
			/* CREATE TABLE LAYOUT */
		if( COL_PER_BLOCK > ROW_PER_BLOCK ) //set row count as biggest of ROW/COL_PER_BLOCK, so that less buttons per row
		{ rowCount = COL_PER_BLOCK; btnCount = ROW_PER_BLOCK; }		// but more rows allows to fit bigger words
		else{ rowCount = ROW_PER_BLOCK; btnCount = COL_PER_BLOCK; }
		
		
		TableRow tableRow;
		Button button;
		
		Log.d( "listener", "rowCount: " + rowCount );
		Log.d( "listener", "btnCount: " + btnCount );

		// if it's in PORTRAIT mode
		if(orientation == 1) {
			for( int j=0; j<rowCount; j++ ) //add button rows to table
			{
				tableRow = new TableRow(context);
				for (int k = 0; k < btnCount; k++) {
					button = new Button(context);
					if (button == null) {
						Log.d("listener", "ERROR: null button");
					}
					button.setTextColor(Color.WHITE);
					button.setBackgroundResource(R.drawable.buttons);
					button.setSingleLine();

					Log.d("listener", "sample word from array: " + wordArray.getWordNativeAtIndex(indexArr));

					// choose button text in language based on user preference
					if (usrLangPref == 0) {
						button.setText(wordArray.getWordNativeAtIndex(indexArr));
					} else {
						button.setText(wordArray.getWordTranslationAtIndex(indexArr));
					}

					btnArr[indexArr] = button;
					tableRow.addView(button);
					indexArr++;
				}
				tableLayout.addView(tableRow);
			}
		}
		// if it's in LANDSCAPE mode
		else if (orientation == 2){
			TableLayout.LayoutParams params = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
			for (int k = 0; k < WORD_COUNT; k++) {
				button = new Button(context);
				tableLayout.setOrientation(TableLayout.VERTICAL);

				if (button == null) {
					Log.d("listener", "ERROR: null button");
				}

				if (usrLangPref == 0) {
					button.setText(wordArray.getWordNativeAtIndex(indexArr));
				} else {
					button.setText(wordArray.getWordTranslationAtIndex(indexArr));
				}

				button.setTextColor(Color.WHITE);
				button.setBackgroundResource(R.drawable.buttons);
				button.setLayoutParams(params);
				button.setGravity(Gravity.CENTER);
				button.setPadding(20,0,20,0);
				button.setSingleLine();

				Log.d("listener", "sample word from array: " + wordArray.getWordNativeAtIndex(indexArr));

				btnArr[indexArr] = button;
				tableLayout.addView(button);
				indexArr++;
			}

		}

		// loop to set up all keypad buttons
		for( i=0; i<WORD_COUNT; i++ )
		{
			final int var;
			var = i + 1;
			final int index;
			index=i;
			btnArr[i].setOnLongClickListener(new View.OnLongClickListener() {
												 @SuppressLint("SetTextI18n")
												 @Override
												 public boolean onLongClick(View v) {
												 	 Log.d( "selectW", "long button press" );
												     Hint.setBackgroundColor(R.drawable.buttons);
                                                     final Drawable buttonBackground = btnArr[index].getBackground();
                                                     btnArr[index].setBackgroundColor(R.drawable.buttons);
													 if(usrLangPref==0){
													     if(usrModePref==1){
                                                             Hint.setText(wordArray.getWordNativeAtIndex( index ) + " : " + numArray[index]);
                                                         }
                                                         else {
                                                             Hint.setText(wordArray.getWordNativeAtIndex(index) + " : " + wordArray.getWordTranslationAtIndex(index));
                                                         }
													 }
													 else {
                                                         if (usrModePref == 1) {
                                                             Hint.setText(wordArray.getWordTranslationAtIndex(index) + " : " + numArray[index]);
                                                         } else {
                                                             Hint.setText(wordArray.getWordTranslationAtIndex(index) + " : " + wordArray.getWordNativeAtIndex(index));
                                                         }
                                                     }
													 wordArray.wordIncrementHintClickAtIndex(index);
													
													 wordArray.setWordUsedInGameAtIndex(index); //mark as used
													 wordArray.setWordDoNotAllowToDecreaseDifficultyAtIndex(index); //allow for difficulty to be decreased
													 
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
												 if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()] == 0 )
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
													 
													 Log.d( "selectW", "btn clicked: " + var );
													 
													 //check if word inserted is correct (used to decrease probability of word being selected in WordArray.selectWord() )
													 if( var == usrSudokuArr.getSolution()[currentRectColoured.getRow()][currentRectColoured.getColumn()] ) //if input matches solution
													 {
													 	Log.d( "selectW", "btn listener: user sqr input correct" );
													 	if( wordArray.getWordAlreadyUsedInGameAtIndex(var-1) == false ) //if correctly using this word for the first time in game
														{
															Log.d( "selectW", "btn listener: decrease difficulty" );
															
															wordArray.setWordUsedInGameAtIndex(var-1); //mark as used
															wordArray.setWordToAllowToDecreaseDifficultyAtIndex(var-1); //allow for difficulty to be decreased
														}
													 }
													 // do not include "else if inserted wrong input, do not allow to be decreased" because user is likely to make mistakes
													 // SO FAR keep the idea that "if inserted correct word once without using HintClick, it implies the user is getting better with that word"
													 
													 //have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
													 if( track.enableCheck )
													 {
														 track.checkPuzzle( usrSudokuArr, check, v, btnArr );
													 }
												 }
												 //usrSudokuArr.printCurrent( );
											 }
										 }

			);
		}
	}
}
