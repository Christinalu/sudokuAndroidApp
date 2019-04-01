package com.omicron.android.cmpt276_1191e1_omicron;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
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

import java.util.List;
import java.util.Random;


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
						  int ROW_PER_BLOCK, Context context, TableLayout tableLayout, int orientation, int state,
						  int[] orderArr, final int[] rotation, final int[] undoBtnPressed ) {
		// pulled out of button listeners
		final Handler handler = new Handler();
		int rowCount;
		int btnCount;
		int indexArr = 0;


		/** SET BUTTON LAYOUT **/

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
					button.setTextColor(Color.parseColor("#00293C"));
					button.setBackgroundResource(R.drawable.keypad_button);
					button.setSingleLine();

					Log.d("listener", "sample word from array: " + wordArray.getWordNativeAtIndex(indexArr));
					//choose button text based on user mode
					if (usrModePref == 0) {
						//user is doing standard mode
						// choose button text in language based on user preference
						if (usrLangPref == 0) {
							button.setText(wordArray.getWordNativeAtIndex(indexArr));
						} else {
							button.setText(wordArray.getWordTranslationAtIndex(indexArr));;
						}
					}
					else {
						//user is doing listening comprehension
						if (usrLangPref == 0) {
							button.setText(wordArray.getWordNativeAtIndex(orderArr[indexArr]));
						} else {
							button.setText(wordArray.getWordTranslationAtIndex(orderArr[indexArr]));
						}
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

				if (usrModePref == 0) {
					//user is doing standard mode
					// choose button text in language based on user preference
					if (usrLangPref == 0) {
						button.setText(wordArray.getWordNativeAtIndex(indexArr));
					} else {
						button.setText(wordArray.getWordTranslationAtIndex(indexArr));;
					}
				}
				else {
					//user is doing listening comprehension
					if (usrLangPref == 0) {
						button.setText(wordArray.getWordNativeAtIndex(orderArr[indexArr]));
					} else {
						button.setText(wordArray.getWordTranslationAtIndex(orderArr[indexArr]));
					}
				}

				button.setTextColor(Color.parseColor("#00293C"));
				button.setBackgroundResource(R.drawable.keypad_button);
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

			/** SET BUTTON LISTENERS **/

		// loop to set up all keypad buttons
		if (state != 2) {
			for (i = 0; i < WORD_COUNT; i++) {
				final int var;
				if (usrModePref == 0) {
					var = i + 1;
				}
				else {
					var = orderArr[i] + 1;
				}
				final int index;
				index = i;
				btnArr[i].setOnLongClickListener(new View.OnLongClickListener() {
													 @SuppressLint("SetTextI18n")
													 @Override
													 public boolean onLongClick(View v) {
														 Log.d("selectW", "long button press");
														 Hint.setBackgroundColor(R.drawable.buttons);
														 final Drawable buttonBackground = btnArr[index].getBackground();
														 btnArr[index].setBackgroundColor(R.drawable.buttons);
														 if (usrLangPref == 0) {
															 if (usrModePref == 1) {
																 Hint.setText(wordArray.getWordNativeAtIndex(index) + " : " + numArray[index]);
															 } else {
																 Hint.setText(wordArray.getWordNativeAtIndex(index) + " : " + wordArray.getWordTranslationAtIndex(index));
															 }
														 } else {
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
												 public void onClick(View v) {
													 //if current button selected is valid and is not restricted
													 int row = currentRectColoured.getRow();
													 int col = currentRectColoured.getColumn();

													 Log.d( "highlight", "btn pressed..." );

													 if (row != -1 && usrSudokuArr.PuzzleOriginal[row][col] == 0) {
														 // increase the count of inserted numbers if needed
														 usrSudokuArr.track(currentRectColoured); //important, 'track' must occur before 'usrSudokuArr.Puzzle[][] = x'

														 //remove duplicates from (x,y) in preparation of new input and new duplication check
														 usrSudokuArr.removeDuplicates(row,col);

														 //if new value is not equal to the old one add last value and its coordinate to history before changing it
														 if (usrSudokuArr.getPuzzle()[row][col] != var) {
															 usrSudokuArr.addHistroy(row, col);
															 usrSudokuArr.printHistory();
														 }

														 if( rotation[0] == 0 && undoBtnPressed[0] == 0 ){ //if rotate or pressed 'undo', skip setDrawParameter
															Log.d( "highlight", "setDrawParameters called from ButtonListener" );
															drawR.setDrawParameters(touchX, touchY, lastRectColoured, currentRectColoured);
														 }

														 // set the cell in the Puzzle to corresponding number based on button user input
														 //if( zoomButtonDisableUpdate[0] == 0 ) // do not update entry when switching modes - causes errors
														 usrSudokuArr.Puzzle[row][col] = var;

														 // redraw square matrix and text overlay
														 btnClicked[0] = 1; //this flag allows (for efficiency) class drw to update TextView as well in zoom mode

														 if( undoBtnPressed[0] == 1 ){ //
															//if user has 'undone' some some moves, when inserting a new entry, do not call drw.setDrawParameters because it resets currentRectColoured
														 }


														 //if( rotation[0] == 1 )
														 //{ rotation[0] = 0; } //disable rotation flag after user inserted cell

														 // check if there is a duplicate in row/col/section. MAKE SURE TO HAVE AFTER PUZZLE INPUT IS SET
														 int currentSelectedisCorrect = 0;
														 if (usrSudokuArr.checkDuplicate(row, col)) {
															 Log.d("TESTI", "Duplicate in given coordinate detected");
															 currentSelectedisCorrect = 2;
														 }
														 else {
															 Log.d("TESTI", "No duplicate detected");
															 currentSelectedisCorrect = 1;
														 }
														 drawR.reDraw(currentRectColoured, usrLangPref, currentSelectedisCorrect);

														 btnClicked[0] = 0;
														 //textOverlay.reDrawText( usrLangPref );

														 Log.d("highlight", "btn clicked: " + var);

														 //check if word inserted is correct (used to decrease probability of word being selected in WordArray.selectWord() )
														 if (var == usrSudokuArr.getSolution()[row][col]) //if input matches solution
														 {
															 Log.d("selectW", "btn listener: user sqr input correct");
															 if (wordArray.getWordAlreadyUsedInGameAtIndex(var - 1) == false) //if correctly using this word for the first time in game
															 {
																 Log.d("selectW", "btn listener: decrease difficulty");

																 wordArray.setWordUsedInGameAtIndex(var - 1); //mark as used
																 wordArray.setWordToAllowToDecreaseDifficultyAtIndex(var - 1); //allow for difficulty to be decreased
															 }
														 }
														 // do not include "else if inserted wrong input, do not allow to be decreased" because user is likely to make mistakes
														 // SO FAR keep the idea that "if inserted correct word once without using HintClick, it implies the user is getting better with that word"

														 //have to check if puzzle is correct (only when allowed by efficiency) and if true, disable buttonListener
														 if (usrSudokuArr.canCheck()) {
															 usrSudokuArr.checkPuzzle(v, btnArr);
														 }

														 Log.d( "highlight", "marked rectArr (in ButtonListener) as selected at: " + currentRectColoured.getRow() + ", " + currentRectColoured.getColumn() );

													 }

													 //debug
													 usrSudokuArr.printCurrent( );
												 }
											 }

				);
			}
		}
	}
}
