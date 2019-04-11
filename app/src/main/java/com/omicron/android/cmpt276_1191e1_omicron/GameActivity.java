package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.omicron.android.cmpt276_1191e1_omicron.Model.Block;
import com.omicron.android.cmpt276_1191e1_omicron.Model.Entry;
import com.omicron.android.cmpt276_1191e1_omicron.Model.Pair;
import com.omicron.android.cmpt276_1191e1_omicron.View.ButtonListener;
import com.omicron.android.cmpt276_1191e1_omicron.View.TextMatrix;
import com.omicron.android.cmpt276_1191e1_omicron.View.drw;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity
{
	/*
		This Game Activity is the main window where the Sudoku Puzzle is going to be displayed
		It will be activated from the Main (Menu) Activity
	*/

	private WordArray wordArray;
	private String[] numArray;
	private int[] orderArr;
	private int usrLangPref;
	private int usrDiffPref = 0;
	private int state; //0=new start, 1=resume, 2=completed
	private int usrModePref; //0=standard, 1=speech
	private int usrPuzzSize; //stores puzzle size
	private int usrPuzzleTypePref; //stores puzzle size 4x4 ... 1 = 4x4, 2 = 6x6, 3 = 9x9, 4 = 12x12

	private TextToSpeech mTTS;
	private String theWord;
	private String language;
	private int row;
	private int col;
	private int val;

	private Paint paint = new Paint( );
	//private Bitmap bitMap;
	//private Canvas canvas;
	//private ImageView imgView;
	private RelativeLayout rectLayout;
	private RelativeLayout rectTextLayout;
	private View.OnTouchListener handleTouch;
	private SudokuGenerator usrSudokuArr;
	//private FindSqrCoordToZoomInOn findSqrCoordToZoomInOn;

	private int sqrLO; // original left coordinate of where puzzle starts
	private int sqrTO; // original top coordinate of where puzzle starts

	private drw drawR; // class that draws the squares either highlighted or not, based on touch
	private Pair lastRectColoured = new Pair( -1, -1 ); // stores the last coloured square coordinates
	private Pair currentRectColoured = new Pair( -1, -1 ); // stores the current coloured square
	private int currentSelectedIsCorrect = 0;
	//TODO TESTING

	private Paint paintblack = new Paint();
	private TextMatrix textMatrix; //stores the TextView for drawing the text
	private Block[][] rectArr;
	
	private int[] touchX = { 0 }; // hold user touch (x,y) coordinate
	private int[] touchY = { 0 };

	private int sqrL; //duplicate square coordinates
	private int sqrT;
	private int sqrR;
	private int sqrB;

	private ButtonListener listeners; // used to call another function to implement all button listeners, to save space in GameActivity
	private int[] btnClicked = { 0 }; //flag which is activated when a button is clicked - used to let zoom drw class to update TextView (for efficiency purposes)
	private int[] onStopAlreadyCalled = { 0 }; //stop onStop() from being called twice
	
	private int[] zoomOn = { 0 }; //indicates if user activated zoom 1==true, 0==false
	private int[] dX = { 0 }; //change in X coordinate ie 'drag'
	private int[] dY = { 0 };

	private int[] touchXZ = { 0 }; //stores where user would click in zoom mode - reference to left top corner
	private int[] touchYZ = { 0 };
	private int[] touchXZclick = { 0 }; //stores where user would click in zoom mode, but when there is no drag, only click down and up
	private int[] touchYZclick = { 0 };
	private int[] drag = { 0 }; // 1==user drags on screen

	private static int SQR_INNER_DIVIDER = 5; //space inside block between squares
	private static int SQR_OUTER_DIVIDER = 15; //space between blocks
	
	private static float SQR_OUTER_DIVIDER_RATIO_4x4 = 0.025f; //ratio do determine space depending on screen size and puzzle type
	private static float SQR_INNER_DIVIDER_RATIO_4x4 = 0.010f;
	private static float SQR_OUTER_DIVIDER_RATIO_6x6 = 0.014f;
	private static float SQR_INNER_DIVIDER_RATIO_6x6 = 0.008f;
	private static float SQR_OUTER_DIVIDER_RATIO_9x9 = 0.014f;
	private static float SQR_INNER_DIVIDER_RATIO_9x9 = 0.005f;
	private static float SQR_OUTER_DIVIDER_RATIO_12x12 = 0.011f;
	private static float SQR_INNER_DIVIDER_RATIO_12x12 = 0.005f;
	
	private static int BOUNDARY_OFFSET = 40; //puzzle will have some offset for aesthetic reasons near edges
	private static float BOUNDARY_OFFSET_SCALE = 0.035f;
	private static float[] ZOOM_SCALE = { 1f }; //zoom scale that will be used after initialized from 4x4 or 6x6 or ... zoom scale
	private static float[] ZOOM_SCALE_OLD = { 1f };
	private static boolean[] ZOOM_FIRST_TIME = { true };
	private static float ZOOM_SCALE_ORIGINAL;
	private static float ZOOM_SCALE_4x4 = 1.3f; // zoom factor of how much to zoom in puzzle in "zoom" mode
	private static float ZOOM_SCALE_4x4_ORIGINAL = ZOOM_SCALE_4x4;
	private static float ZOOM_SCALE_6x6 = 1.4f; // zoom factor of how much to zoom in puzzle in "zoom" mode
	private static float ZOOM_SCALE_6x6_ORIGINAL = ZOOM_SCALE_6x6;
	private static float ZOOM_SCALE_9x9 = 1.5f; // zoom factor of how much to zoom in puzzle in "zoom" mode
	private static float ZOOM_SCALE_9x9_ORIGINAL = ZOOM_SCALE_9x9;
	private static float ZOOM_SCALE_12x12 = 2.0f;
	private static float ZOOM_SCALE_12x12_ORIGINAL = ZOOM_SCALE_12x12;
	private int[] zoomLevel = { 0 }; //0== no zoom; 1== default zoom; 2==twice default zoom
	private int zoomLevel_MAX = 3; //max allowed to zoom
	private int PUZZLE_FULL_SIZE_WIDTH; // size of full puzzle from left most column pixel to right most column pixel
	private long STATISTIC_MULTIPLE = 2; //used to multiply by factor the number of "Hint Clicks" a user used, to more likely show these words
	
	private int[] zoomButtonSafe = { 0 }; //used to prevent a 'click coordinate' from being tested if it is in a rect when switching to "zoom" mode because switching to the mode, should not test click
	private int[] zoomClickSafe = { 0 }; //used to block touchX update when user switches mode
	private int[] zoomButtonDisableUpdate = { 0 }; //do not let button update entry right after changing "zoom" mode - because zoomX is incorrect and must be updated first by having user click somewhere first
	private int[] zoomInBtnOn = { 1 }; //enable/disable zoom btn; "on" by default
	private int[] zoomOutBtnOn = { 0 };

	private int screenH;
	private int screenW;
	private int bitmapSizeWidth; //bitmap dimensions where the puzzle will be drawn
	private int bitmapSizeHeight;
	private int sqrSizeWidth; //size of single square in matrix
	private int sqrSizeHeight;
	private int orientation;
	private int barH; //stores size of top bar in pixels
	
	private int HINT_CLICK_TO_MAX_PROB;
	private static final float LANDSCAPE_RATIO = 0.75f; //determines how much of the screen will be dedicated to puzzle in landscape
	private int WORD_COUNT; //stores the number of words in wordArray
	private int COL_PER_BLOCK; //stores how many columns will be inside a block; in 9x9 this would be 3
	private int ROW_PER_BLOCK;
	private int VERTICAL_BLOCK; //stores how many (vertical) blocks are in a puzzle; in 9x9 this would be 3 blocks
	private int HORIZONTAL_BLOCK;
	
	private int[] rotation = { 0 };
	private int[] undoBtnPressed = { 0 };

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);


		ZOOM_FIRST_TIME[0] = true;

		//set intent to receive word array from Main Activity
		if (savedInstanceState != null) {
			//a state had been saved, load it
			savetheInstanceState (0, savedInstanceState, state, wordArray, usrLangPref, usrSudokuArr, usrModePref, language, numArray, orderArr, HINT_CLICK_TO_MAX_PROB, currentRectColoured, currentSelectedIsCorrect);
			rotation[0] = 1;
		}
		else {
			Intent gameSrc = getIntent();
			if (gameSrc != null) {
				state = (int) gameSrc.getSerializableExtra("state");
				//check state: if 1 then we are resuming a previous game, otherwise state == 0 and we are starting a new game
				if (state > 0) {
					//we are resuming a game
					wordArray = (WordArray) gameSrc.getParcelableExtra("wordArray");
					usrLangPref = (int) gameSrc.getSerializableExtra("usrLangPref");
					usrModePref = (int) gameSrc.getSerializableExtra("usrMode");
					language = (String) gameSrc.getSerializableExtra("language");
					HINT_CLICK_TO_MAX_PROB = (int) gameSrc.getSerializableExtra( "HINT_CLICK_TO_MAX_PROB" );
					currentRectColoured = (Pair) gameSrc.getSerializableExtra("currentRectColoured");
					currentSelectedIsCorrect = (int) gameSrc.getSerializableExtra("currentSelectedIsCorrect");
					usrSudokuArr = (SudokuGenerator) gameSrc.getSerializableExtra("SudokuArr");
					rotation[0] = 1;
					if (usrModePref == 1) {
						numArray = (String[]) gameSrc.getStringArrayExtra("numArray");
						orderArr = (int[]) gameSrc.getIntArrayExtra("orderArr");
					}
				}
				else {
					//we are starting a new game
					//state = 1; //set game available to resume
					wordArray = (WordArray) gameSrc.getParcelableExtra("wordArray");
					usrLangPref = (int) gameSrc.getSerializableExtra("usrLangPref");
					usrModePref = (int) gameSrc.getSerializableExtra("usrMode");
					HINT_CLICK_TO_MAX_PROB = (int) gameSrc.getSerializableExtra( "HINT_CLICK_TO_MAX_PROB" );
					usrDiffPref = (int) gameSrc.getSerializableExtra("usrDiffPref");
					usrPuzzSize = (int) gameSrc.getSerializableExtra("usrPuzzSize");
					if (usrModePref == 1) {
						//create separate array to draw from for this mode
						WORD_COUNT = wordArray.getWordCount( );
						numArray = new String[WORD_COUNT];
						orderArr = new int[WORD_COUNT];
						randomizeOrder(orderArr);
						if (usrLangPref == 0) {
							for (int i = 0; i < WORD_COUNT; i++) {
								numArray[i] = wordArray.getWordTranslationAtIndex( i );
								wordArray.setWordTranslationAtIndex( i, Integer.toString(i+1) );
							}
						}
						else {
							for (int i = 0; i < WORD_COUNT; i++) {
								numArray[i] = wordArray.getWordNativeAtIndex( i );
								wordArray.setWordNativeAtIndex( i, Integer.toString(i + 1) );
							}
						}
						language = (String) gameSrc.getSerializableExtra("language");
					}
					usrSudokuArr = new SudokuGenerator(usrDiffPref, usrPuzzSize);
				}

				//debug wordArray
				Log.d( "upload", " @ WORD_ARRAY ON RESUME GAME:" );
				for( int i=0; i<WORD_COUNT; i++ )
				{
					Log.d( "upload","wordArr[" + i + "]: " + wordArray.getWordNativeAtIndex( i ) + "," + wordArray.getWordTranslationAtIndex( i ) + "," + wordArray.getWordInFileLineNumAtIndex( i ) + "," + wordArray.getWordHintClickAtIndex( i ) );
				}
			}
		}

		Log.d( "debug-1", "currentRectColoured: " + currentRectColoured.getRow() + ", " + currentRectColoured.getColumn() );

		if (usrModePref == 1) {
			mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
				@Override
				public void onInit(int status) {
					if (status == TextToSpeech.SUCCESS) {
						Locale locale = new Locale(language);
						int result = mTTS.setLanguage(locale);
						if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
							Log.e("mTTS", "ERROR: language initialization failed. Language = "+language);
						}
						Log.e("mTTS", "SUCCESS: language initialization successful. Language = "+language);
					}
					else {
						Log.e("mTTS", "ERROR: TextToSpeech FAILED");
					}
				}
			});
		}


		TextView Hint=(TextView) findViewById(R.id.hint_content);



			/** INITIALIZE PUZZLE DIMENSIONS **/

		// get display metrics
		DisplayMetrics displayMetrics = new DisplayMetrics( );
		Display screen = getWindowManager( ).getDefaultDisplay( ); //get general display
		screen.getMetrics( displayMetrics );

		screenH = displayMetrics.heightPixels;
		screenW = displayMetrics.widthPixels;


		rectLayout = (RelativeLayout) findViewById( R.id.rect_layout ); //used to detect user touch for matrix drw
		rectTextLayout = (RelativeLayout) findViewById( R.id.rect_txt_layout ); //used to crop TextViews the same size as bitmap
		
		drawR = new drw( this, rectLayout ); //declare only
		
		//initialize Puzzle Matrix parameters including:
		//sqrLO, sqrTO, sqrSizeWidth, sqrSizeHeight, bitMap, rectLayout, rectTextLayout
		initializePuzzleMatrixParameters( );
		
		paint.setColor(Color.parseColor("#c2c2c2"));

		textMatrix = new TextMatrix( this, sqrSizeWidth, sqrSizeHeight, ZOOM_SCALE, WORD_COUNT );


		// set up object to translate to selected square in "zoom" mode
		// note: requires sqrSize be determined before call
		//findSqrCoordToZoomInOn = new FindSqrCoordToZoomInOn( touchXZ, touchYZ, currentRectColoured, bitmapSizeWidth, bitmapSizeHeight,
		//													 sqrSizeWidth, sqrSizeHeight, textMatrix, ZOOM_SCALE );
		

		// PREDEFINE VARIABLES FOR MATRIX OVERLAY
		paintblack.setColor(Color.parseColor("#0000ff"));
		paintblack.setTextSize(30);

		rectArr = new Block[WORD_COUNT][WORD_COUNT]; // stores all squares in a 2D array
		
		drawR.drwInitialize( rectArr, paint, rectTextLayout, textMatrix, usrSudokuArr, zoomOn, drag,
						 dX, dY, touchXZ, touchYZ, zoomButtonSafe, zoomClickSafe, zoomButtonDisableUpdate,
						 bitmapSizeWidth, bitmapSizeHeight, wordArray, btnClicked, ZOOM_SCALE,
						 COL_PER_BLOCK, ROW_PER_BLOCK, VERTICAL_BLOCK, HORIZONTAL_BLOCK, WORD_COUNT,
						 barH, undoBtnPressed ); // class used to draw/update square matrix
		
		TableLayout tableLayout = findViewById( R.id.btn_keypad );
		
		
		// call function to set all listeners - needs drawR
		
		listeners = new ButtonListener(currentRectColoured, usrSudokuArr,
					drawR, touchX, touchY, lastRectColoured, usrLangPref, btnClicked,
					Hint, wordArray, usrModePref, numArray, WORD_COUNT, COL_PER_BLOCK, ROW_PER_BLOCK,
					this, tableLayout, orientation, state, orderArr, rotation, undoBtnPressed);
		


			/** CREATE RECT MATRIX **/
			
		//initialize all rect and text view in RelativeLayout
		//and set coordinates for each rect
		createRectMatrix( );


		//set matrix parameters so far, if the game is not resuming or rotating
		if( rotation[0] == 0 ) { //if not rotating
			Log.d( "highlight", "setting drw parameters..." );
			drawR.setDrawParameters(touchX, touchY, lastRectColoured, currentRectColoured);
		}

		//PRESERVE SELECTED RECTANGLE IN rectArr, FROM drw CLASS, WHEN ROTATING OR RESUMING
		if( (rotation[0] == 1 ) && currentRectColoured.getRow() != -1 ) {
			Log.d( "highlight", "marked rectArr as selected at: " + currentRectColoured.getRow() + ", " + currentRectColoured.getColumn() );

			Log.d("TAG", "duplicate is about to be checked");
			// 0 == nothing selected; 1 == selected and correct; 2 == selected but incorrect
			currentSelectedIsCorrect = 0;
			if (usrSudokuArr.checkDuplicate(currentRectColoured.getRow(), currentRectColoured.getColumn())) {
				Log.d("TAG", "duplicate should have been found");
				currentSelectedIsCorrect = 2;
			} else {
				Log.d("TAG", "duplicate is not found");
				currentSelectedIsCorrect = 1;
			}

			drawR.setRectArrSelectedAtIndex( currentRectColoured.getRow(), currentRectColoured.getColumn() );
		}

		drawR.reDraw( currentRectColoured, usrLangPref, currentSelectedIsCorrect );



			/*UNDO BUTTON*/

		ImageButton btnUndo = findViewById(R.id.button_undo);
		btnUndo.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick( View v)
			{
			    if (!usrSudokuArr.isCorrect) {
                    if (!usrSudokuArr.historyisEmpty()) {
                        usrSudokuArr.printHistory();

                        undoBtnPressed[0] = 1; //set flag not to call setDrawParameter in ButtonListener

                        Entry lastEntry = usrSudokuArr.removeHistory();
                        int tempRow = lastEntry.getCoordinate().getRow();
                        int tempCol = lastEntry.getCoordinate().getColumn();
                        int cSiC;
						if( currentRectColoured.getRow() != -1 ){
							drawR.getRectArr()[currentRectColoured.getRow()][currentRectColoured.getColumn()].deselect(); //deselect current from drw class
						}
						currentRectColoured.update(tempRow, tempCol);
						Log.d( "highlight", "currentRectColoured after undo: " + currentRectColoured.getRow() + ", " + currentRectColoured.getColumn() );
						usrSudokuArr.setPuzzleVal(lastEntry.getValue(), tempRow, tempCol);
                        if (usrSudokuArr.checkDuplicate(tempRow, tempCol)) {
                            //Log.d("TESTI", "Duplicate in given coordinate detected");
                            cSiC = 2;
                        } else {
                            //Log.d("TESTI", "No duplicate detected");
                            cSiC = 1;
                        }
						//deselect current and reselect last in rectArr in drw class
						//deselecting part executed at start of function
						drawR.getRectArr()[tempRow][tempCol].select();
                        drawR.reDraw(currentRectColoured, usrLangPref, cSiC);
						textMatrix.chooseLangAndDraw( currentRectColoured.getRow(), currentRectColoured.getColumn(),
								wordArray, usrSudokuArr, usrLangPref );

						// TODO: see if necessary implement similar undoBtnPressed[] flag for 'reset' btn - ie after resetting, press a btn and see what happens

                    } else {
                        //Toast.makeText(v.getContext(), "There is nothing to undo!", Toast.LENGTH_LONG).show();
                    }
                }
			}
		});

			/*RESET BUTTON*/

		ImageButton btnReset = findViewById(R.id.button_reset);
		btnReset.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick( View v)
			{
			    if (!usrSudokuArr.isCorrect) {
                    usrSudokuArr.resetPuzzle();
                    usrSudokuArr.printCurrent();
                    drawR.reDraw(currentRectColoured, usrLangPref, 0);
					textMatrix.resetAllText( usrSudokuArr );
					drawR.resetRectArrConflict( );
                }
			}
		});

			/* ZOOM IN BUTTON */

		ImageButton btnZoomIn = findViewById( R.id.button_zoom_in );
		btnZoomIn.setOnClickListener(new View.OnClickListener( )
			{
				@Override
				public void onClick( View v )
				{
					if( zoomInBtnOn[0] == 1 ) //only allow btn to be clicked when activated
					{
						// set zoom scale
						zoomLevel[0]++;
						
						if( ZOOM_FIRST_TIME[0] == true ) //detect if game just started; solves bug where ZOOM_OLD was not 1
						{
							ZOOM_SCALE_OLD[0] = 1;
							ZOOM_FIRST_TIME[0] = false;
						}
						else
						{
							ZOOM_SCALE_OLD[0] = ZOOM_SCALE[0];
						}
						
						ZOOM_SCALE[0] = 1f + (ZOOM_SCALE_ORIGINAL - 1) * zoomLevel[0];
						
						zoomOn[0] = 1; //set zoom as activated
						zoomButtonSafe[0] = 1; //zoomSafe needed
						zoomClickSafe[0] = 1;
						zoomButtonDisableUpdate[0] = 1; //do not let button update coordinate when switching modes due to zoomX scaling

						textMatrix.scaleTextZoom( ZOOM_SCALE[0] );
						textMatrix.reDrawTextZoom( touchXZ, touchYZ, dX, dY ); // call this because .scaleTextZoom() only scales Layouts, so call this to place them in correct (drag) position

						/*
						touchXZ[0] = 0;
						touchYZ[0] = 0;
						*/
						
						// CHECK INPUT FOR DUPLICATES
						if( currentRectColoured.getRow() != -1 ) {
							// 0 == nothing selected; 1 == selected and correct; 2 == selected but incorrect
							if (usrSudokuArr.checkDuplicate(currentRectColoured.getRow(), currentRectColoured.getColumn())) {
								currentSelectedIsCorrect = 2;
							}
							else {
								currentSelectedIsCorrect = 1;
							}
						}
						else
						{
							currentSelectedIsCorrect = 0;
						}
						
						// on zoom in, calculate coordinate to zoom on selected square
						//findSqrCoordToZoomInOn.findSqrCoordToZoomInOn( );
						findSqrCoordToZoomInOn( ZOOM_SCALE_OLD );
						
						drawR.setDrawParameters( touchX, touchY, lastRectColoured, currentRectColoured );
						drawR.reDraw( currentRectColoured, usrLangPref, currentSelectedIsCorrect );
						
						zoomButtonSafe[0] = 0; //zoomSafe needed
						
						zoomOutBtnOn[0] = 1; //activate other zoom btn
						if( zoomLevel[0] >= zoomLevel_MAX ){ //do not allow to zoom more, disable zoom in btn
							zoomInBtnOn[0] = 0; //deactivate this btn so text not scaled multiple times
						}
					}
				}
			}
		);
		
		
			/* ZOOM OUT BUTTON */

		ImageButton btnZoomOut = findViewById( R.id.button_zoom_out );
		btnZoomOut.setOnClickListener(new View.OnClickListener( )
					{
						@Override
						public void onClick(View v)
						{
							if( zoomOutBtnOn[0] == 1 )//only allow btn to be clicked when activated
							{
								zoomLevel[0]--; //should not reach -1, because it will be disabled by zoomOutBtnOn[]
								ZOOM_SCALE_OLD[0] = ZOOM_SCALE[0];
								ZOOM_SCALE[0] = 1f + (ZOOM_SCALE_ORIGINAL - 1) * zoomLevel[0];
								
								zoomOn[0] = 1; //set zoom as activated
								zoomButtonSafe[0] = 1; // do not update sqr on button click
								zoomClickSafe[0] = 1;
								zoomButtonDisableUpdate[0] = 1; //do not let button update coordinate when switching modes due to zoomX scaling
								
								textMatrix.scaleTextZoom( ZOOM_SCALE[0] );
								textMatrix.reDrawTextZoom( touchXZ, touchYZ, dX, dY ); // call this because .scaleTextZoom() only scales Layouts, so call this to place them in correct (drag) position
								
								/*
								touchXZ[0] = 0;
								touchYZ[0] = 0;
								*/

								// CHECK INPUT FOR DUPLICATES
								if( currentRectColoured.getRow() != -1 ) {
									// 0 == nothing selected; 1 == selected and correct; 2 == selected but incorrect
									if (usrSudokuArr.checkDuplicate(currentRectColoured.getRow(), currentRectColoured.getColumn())) {
										currentSelectedIsCorrect = 2;
									}
									else {
										currentSelectedIsCorrect = 1;
									}
								}
								else
								{
									currentSelectedIsCorrect = 0;
								}
								
								// on zoom, calculate coordinate to zoom on selected square
								//findSqrCoordToZoomInOn.findSqrCoordToZoomInOn( );
								findSqrCoordToZoomInOn( ZOOM_SCALE_OLD );
								
								drawR.setDrawParameters( touchX, touchY, lastRectColoured, currentRectColoured );
								drawR.reDraw( currentRectColoured, usrLangPref, currentSelectedIsCorrect );

								zoomButtonSafe[0] = 0;
								zoomInBtnOn[0] = 1; //activate other zoom btn
								if( zoomLevel[0] <= 0 ){ //do not allow to zoom more than normal zoom
									zoomOutBtnOn[0] = 0; //deactivate this btn so text not scaled multiple times
								}
							}
						}
					}
		);


			// Legend:
			// dX : pixel drag on screen non-scaled
			// touchXZ : pixel coordinate in scaled image ie a 1000p wide bitmap with 2x scale will be 2000p wide (in ZOOM mode),
			// 			 so touchXZ = 0 will be start top left corner, == 1000 means == 1000 top left corner will start and top right corner
			// 			 would be 1000 + screen_width
			// touchXZclick : initial (stored) regular pixel coordinate of where user first clicked

			/** ON-TOUCH **/

		handleTouch = new View.OnTouchListener( )
		{
			@Override
			public boolean onTouch( View v, MotionEvent event )
			{
				// when user touches the puzzle, perform action accordingly
				
				touchX[0] = (int) event.getX( ); // touch coordinate on actual screen inside RelativeLayout rect_layout - starting from top left corner @ (0,0)
				touchY[0] = (int) event.getY( );

				switch( event.getAction( ) )
				{
					case MotionEvent.ACTION_DOWN:
						
							actionDown( );
						
						break;

					case MotionEvent.ACTION_MOVE:
						
							actionMove( );
						
						break;

					case MotionEvent.ACTION_UP:
						
							actionUp( );
						
						break;
				}

				return true;
			}
		};


		// initialize onTouchListener as defined above
		rectLayout.setOnTouchListener( handleTouch );
	}


	// GET TOP MENU BAR OFFSET
	public int getStatusBarHeight( )
	{
		int result = 0;
		int resourceId = getResources().getIdentifier( "status_bar_height", "dimen", "android" );
		if( resourceId > 0 )
		{
			result = getResources().getDimensionPixelSize( resourceId );
		}
		return result;
	}

	@Override
	public void onSaveInstanceState (Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savetheInstanceState (1, savedInstanceState, state, wordArray, usrLangPref, usrSudokuArr, usrModePref, language, numArray, orderArr, HINT_CLICK_TO_MAX_PROB, currentRectColoured, currentSelectedIsCorrect);
	}

	@Override
	public void onBackPressed() {
		onStop();
		onStopAlreadyCalled[0] = 1; //stop onStop() from being called again
		Log.i("selectW", "back pressed");
		Intent resumeSrc = new Intent( GameActivity.this, MainActivity.class );
		resumeSrc.putExtra( "wordArray", wordArray );
		resumeSrc.putExtra( "usrLangPref", usrLangPref );
		resumeSrc.putExtra("SudokuArr", usrSudokuArr);
		if (usrSudokuArr.isCorrect) {
			state = 2;
		}
		else {
			state = 1;
		}
		resumeSrc.putExtra("state", state);
		resumeSrc.putExtra("usrMode", usrModePref);
		resumeSrc.putExtra("language", language);
		resumeSrc.putExtra("currentRectColoured", currentRectColoured);
		resumeSrc.putExtra("currentSelectedIsCorrect", currentSelectedIsCorrect);
		if (usrModePref == 1) {
			resumeSrc.putExtra("numArray", numArray);
			resumeSrc.putExtra("orderArr", orderArr);
		}
		//resumeSrc.putExtra("countryGA", country);
		Log.i("TAG", "Result about to be stored");
		setResult(RESULT_OK,resumeSrc);
		finish();
		super.onBackPressed();
	}

	@Override
	public void onDestroy() {
		if (mTTS != null) {
			mTTS.stop();
			mTTS.shutdown();
		}
		super.onDestroy();
	}
	
	@Override
	public void onStop( )
	{
		super.onStop( );
	
			/** SAVE ALL DATA **/
			
		//	THESE ARE FOR TESTING
		//wordArray[0].updateHintClick( 10 );
		//wordArray[1].updateHintClick( 1 );
		//wordArray[3].updateHintClick( 1 );
		//wordArray[5].updateHintClick( 10 );
		//wordArray[8].updateHintClick( 3 );
		
		//stop onStop() from being called if already called on "back button"
		if( onStopAlreadyCalled[0] == 1 )
		{
			onStopAlreadyCalled[0] = 0;
			return;
		}
		
		Log.d( "selectW", "onStop() called to save data" );
		
		int HINT_CLICK_ON_DECREASE = 1; //if user had a word in puzzle but did not use HintClick, then it implies the user has less difficulty with word, so decrease HintClick by this
		FileInputStream fileInStream = null; //open file from internal storage
		try {
			fileInStream = this.openFileInput( wordArray.getPackageName() ); //get internal file name, contained in 10th index of wordArray
			
			
			// READ ALL CONTENT
			FileOutputStream outStream;
			InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
			BufferedReader buffRead = new BufferedReader( inStreamRead );
			StringBuilder strBuild = new StringBuilder( );
			String[] strSplit = null; //holds all attributes from relation instance (ie row)
			
			String line;
			long newHintClick = 0;
			int i = 0; // line index to keep track of row index in file
			boolean lineFound;
			
			
				/* READ FILE AND UPDATE HintClick */
			
			// IMPORTANT: 	the idea is that "if inserted correct word once without using HintClick, it implies the user is getting better with that word"
			//				WITHOUT using any HintClick; if HintClick used, then word considered still difficult so increase HintClick
			//				in this case, the user is allowed to place word in incorrect sqr, but if eventually places it in correct square, difficulty is decreased (as long as usr did not use HintClick)
			
			while( (line = buffRead.readLine()) != null ) //
			{
				lineFound = false; //reset
				
					/* FIND LINE OF WORD IN CSV (based on wordArr[i].getInFileLineNum() */
				for( int k=0; k<WORD_COUNT; k++ ) //loop through all 'n' words
				{
					if( i == wordArray.getWordInFileLineNumAtIndex( k ) ) //if updating line with word that was used in wordArray
					{
						strSplit = line.split(","); //get all attribute
						long hintClickSoFar = Long.parseLong(strSplit[2]); //get original click count from file
						
						//to what hintClickSoFar was so far originally in the file, add wordArray.getHintClick() from the current game
						//newHintClick = hintClickSoFar + wordArray[k].getHintClick()*STATISTIC_MULTIPLE;
						
						if( wordArray.getWordHintClickAtIndex( k ) == 0 ) //if no HintClicks for this word, it means user has less difficulty so decrease HintClick to decrease probability
						{
							if( wordArray.getWordAllowToDecreaseDifficultyAtIndex( k ) ) //if user inserted correct word (ONLY ONCE)
							{
								wordArray.setWordDoNotAllowToDecreaseDifficultyAtIndex( k ); //disable so that word cannot have difficulty decreased in this game, until user starts new game
								wordArray.setWordUsedInGameAtIndex( k );
								newHintClick = hintClickSoFar - HINT_CLICK_ON_DECREASE;
								Log.d( "selectW", "word: " + wordArray.getWordNativeAtIndex(k) );
								Log.d( "selectW", "line index: " + i );
								Log.d( "selectW", "HintClick decreased with hintClickSoFar=" + hintClickSoFar + " newHintClick=" + newHintClick );
							}
							else
							{ newHintClick = hintClickSoFar; } //keep the same HintCount (because newHintCount==0 and no need to decrease count)
						}
						else
						{
							Log.d( "selectW", "adding HintCount: " + wordArray.getWordHintClickAtIndex( k ) );
							newHintClick = hintClickSoFar +  wordArray.getWordHintClickAtIndex( k );
							Log.d( "selectW", wordArray.getWordNativeAtIndex(k) + " new hintClick: " + newHintClick );
						} //user used HintClick
						
						//test for bounds
						if( newHintClick < 1 ) //if too low
						{ newHintClick = 1; }
						else if( newHintClick > HINT_CLICK_TO_MAX_PROB )
						{ newHintClick = HINT_CLICK_TO_MAX_PROB; }
						
						
						lineFound = true; //csv file line matches a word in wordArray[]
						
						break; //do not consider the rest
					}
				}
				
				if( lineFound == false ) //not a line to update, keep the same
				{
					strBuild.append( line );
					strBuild.append( "\n" );
				}
				else //line found, update in file
				{
					strBuild.append( strSplit[0] + "," + strSplit[1] +  "," + newHintClick );
					strBuild.append(  "\n");
				}
				
				i++;
			}
			
			buffRead.close( );
			
			// WRITE TO FILE //
			outStream = this.openFileOutput( wordArray.getPackageName( ), this.MODE_PRIVATE ); //open private output stream for re-write
			outStream.write( strBuild.toString().getBytes( ) ); //convert string to bytes and write to file
			outStream.close( ); //close and save file
		
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		Log.d( "upload", "onStop called for GameActivity" );
	}


	@Override
	public void onStart( )
	{
		super.onStart( );
		
		//reset all "HintClick"
		for( int i=0; i<WORD_COUNT; i++ )
		{
			wordArray.wordUpdateHintClickAtIndex( i, 0 );
		}
	}
	
	
	private void setUpPuzzleRowColBlockDividerCountAndSize( )
	{
		/*
		 * Based on puzzle size type, this function sets up the required
		 * Block, Column, Row and Boundary sizes
		 */
		
		if( usrPuzzleTypePref == wordArray.getSize4x4() ) {
			COL_PER_BLOCK = 2;
			ROW_PER_BLOCK = 2;
			VERTICAL_BLOCK = 2;
			HORIZONTAL_BLOCK = 2;
			ZOOM_SCALE[0] = ZOOM_SCALE_4x4;
			ZOOM_SCALE_ORIGINAL = ZOOM_SCALE[0];
			BOUNDARY_OFFSET = (int)( bitmapSizeHeight * BOUNDARY_OFFSET_SCALE );
			SQR_INNER_DIVIDER = (int)( bitmapSizeHeight * SQR_INNER_DIVIDER_RATIO_4x4 );
			SQR_OUTER_DIVIDER = (int)( bitmapSizeHeight * SQR_OUTER_DIVIDER_RATIO_4x4 );
		}
		else if( usrPuzzleTypePref == wordArray.getSize6x6() ){
			COL_PER_BLOCK = 3;
			ROW_PER_BLOCK = 2;
			VERTICAL_BLOCK = 2;
			HORIZONTAL_BLOCK = 3;
			ZOOM_SCALE[0] = ZOOM_SCALE_6x6;
			ZOOM_SCALE_ORIGINAL = ZOOM_SCALE[0];
			BOUNDARY_OFFSET = (int)( bitmapSizeHeight * BOUNDARY_OFFSET_SCALE );
			SQR_INNER_DIVIDER = (int)( bitmapSizeHeight * SQR_INNER_DIVIDER_RATIO_6x6 );
			SQR_OUTER_DIVIDER = (int)( bitmapSizeHeight * SQR_OUTER_DIVIDER_RATIO_6x6 );
			
		}
		else if( usrPuzzleTypePref == wordArray.getSize12x12() ){
			COL_PER_BLOCK = 4;
			ROW_PER_BLOCK = 3;
			VERTICAL_BLOCK = 3;
			HORIZONTAL_BLOCK = 4;
			ZOOM_SCALE[0] = ZOOM_SCALE_12x12; //in 12x12, zoom in more because too small to see
			ZOOM_SCALE_ORIGINAL = ZOOM_SCALE[0];
			BOUNDARY_OFFSET = (int)( bitmapSizeHeight * BOUNDARY_OFFSET_SCALE );
			SQR_INNER_DIVIDER = (int)( bitmapSizeHeight * SQR_INNER_DIVIDER_RATIO_12x12 );
			SQR_OUTER_DIVIDER = (int)( bitmapSizeHeight * SQR_OUTER_DIVIDER_RATIO_12x12 );
		}
		else{
			COL_PER_BLOCK = 3;
			ROW_PER_BLOCK = 3;
			VERTICAL_BLOCK = 3;
			HORIZONTAL_BLOCK = 3;
			ZOOM_SCALE[0] = ZOOM_SCALE_9x9;
			ZOOM_SCALE_ORIGINAL = ZOOM_SCALE[0];
			BOUNDARY_OFFSET = (int)( bitmapSizeHeight * BOUNDARY_OFFSET_SCALE );
			SQR_INNER_DIVIDER = (int)( bitmapSizeHeight * SQR_INNER_DIVIDER_RATIO_9x9 );
			SQR_OUTER_DIVIDER = (int)( bitmapSizeHeight * SQR_OUTER_DIVIDER_RATIO_9x9 );
		}
		
		Log.d( "selectW", "ZOOM_SCALE: " + ZOOM_SCALE );
	}
	
	
	private void initializePuzzleMatrixParameters( )
	{
		/*
		 * This function sets up all Puzzle Parameters including:
		 * 		sqrLO, sqrTO, sqrSizeWidth, sqrSizeHeight, bitMap, rectLayout, rectTextLayout
		 * It also calls setUpPuzzleRowColBlockDividerCountAndSize( ) to set up
		 * 		puzzle size type and initialize row, col, block and divider sizes
		 */
		
		orientation = Configuration.ORIENTATION_UNDEFINED;
		
		//find orientation
		if( screenH > screenW )
		{
			orientation = Configuration.ORIENTATION_PORTRAIT;
		}else {
			orientation = Configuration.ORIENTATION_LANDSCAPE;
		}
		
		//find minimum to create square bitmap
		//note: in Canvas, a bitmap starts from coordinate (0,0), so a bitmap must cover the entire screen size
		//note: this is not optimized for square screens
		if( screenH < screenW ){ //landscape
			bitmapSizeWidth = (int)( screenW * LANDSCAPE_RATIO ); //if in landscape mode, allow puzzle to be rectangle
			bitmapSizeHeight = screenH;
		}
		else {
			bitmapSizeWidth = screenW; //if in portrait mode, the puzzle will be square
			bitmapSizeHeight = screenW;
		}
		
		//status bar height
		//this is needed as offset in landscape mode to alight rect matrix with textOverlay
		barH = getStatusBarHeight();
		
		/* SET PUZZLE ROW AND COL NUMBER PER BLOCK AND DIVIDER SIZE */
		WORD_COUNT = wordArray.getWordCount( );
		usrPuzzleTypePref = wordArray.getUsrPuzzleTypePref( );
		
		//based on puzzle size type, initialize row, col, block and divider sizes
		setUpPuzzleRowColBlockDividerCountAndSize( );
		
		// center bitmap based on orientation
		// original coordinates of where to start to draw square (LO == Left Original)
		// key: sqrLO/TO determines where the drawR class will start drawing from
		if( orientation == Configuration.ORIENTATION_LANDSCAPE ) //if in landscape mode, center bitmap on left side
		{
			sqrLO = BOUNDARY_OFFSET;
			sqrTO = BOUNDARY_OFFSET;
			
			// find matrix single square size based on screen
			// barH needed in landscape mode
			sqrSizeWidth = ( bitmapSizeWidth - 2*BOUNDARY_OFFSET - (VERTICAL_BLOCK*(COL_PER_BLOCK-1))*SQR_INNER_DIVIDER - (VERTICAL_BLOCK-1)*SQR_OUTER_DIVIDER ) / WORD_COUNT;
			sqrSizeHeight = ( bitmapSizeHeight - barH - 2*BOUNDARY_OFFSET - (HORIZONTAL_BLOCK*(ROW_PER_BLOCK-1))*SQR_INNER_DIVIDER - (HORIZONTAL_BLOCK-1)*SQR_OUTER_DIVIDER ) / WORD_COUNT;
			
			drawR.createBitmap( bitmapSizeWidth, bitmapSizeHeight, barH );
			
			//bitMap = Bitmap.createBitmap( bitmapSizeWidth, bitmapSizeHeight-barH, Bitmap.Config.ARGB_8888 );
			
			//set rect_txt_layout the same size as the bitmap
			rectTextLayout.getLayoutParams().width = bitmapSizeWidth;
			rectTextLayout.getLayoutParams().height = bitmapSizeHeight-barH;
			
			//set layout size of where puzzle matrix will be drawn
			rectLayout.getLayoutParams().width = bitmapSizeWidth;
			rectLayout.getLayoutParams().height = bitmapSizeHeight-barH;
		}
		else //in portrait mode
		{
			// find matrix single square size based on screen
			sqrSizeWidth = ( bitmapSizeWidth - 2*BOUNDARY_OFFSET - (VERTICAL_BLOCK*(COL_PER_BLOCK-1))*SQR_INNER_DIVIDER - (VERTICAL_BLOCK-1)*SQR_OUTER_DIVIDER ) / WORD_COUNT;
			sqrSizeHeight = sqrSizeWidth;
			
			// find entire matrix puzzle size
			PUZZLE_FULL_SIZE_WIDTH = WORD_COUNT*sqrSizeWidth + (VERTICAL_BLOCK*(COL_PER_BLOCK-1))*SQR_INNER_DIVIDER + (VERTICAL_BLOCK-1)*SQR_OUTER_DIVIDER;
			
			//if in portrait mode, center puzzle top of screen
			sqrLO = BOUNDARY_OFFSET + (screenW - 2 * BOUNDARY_OFFSET) / 2 - PUZZLE_FULL_SIZE_WIDTH / 2;
			sqrTO = BOUNDARY_OFFSET; // no boundary offset - note: boundary offset is already included in bitmapSize because width == height, so sqrSize calculated for sqrLO width assures the offset is included in height sqrTO
			
			drawR.createBitmap( bitmapSizeWidth, bitmapSizeHeight, 0 );
			//bitMap = Bitmap.createBitmap( bitmapSizeWidth, bitmapSizeHeight, Bitmap.Config.ARGB_8888 );
			
			//set rect_txt_layout the same size as the bitmap
			rectTextLayout.getLayoutParams().width = bitmapSizeWidth;
			rectTextLayout.getLayoutParams().height = bitmapSizeHeight;
			
			//note: limiting rectLayout to bitmap size will force a selected square to be deselected only when clicking inside the bitmap
			//		it wont deselect when clicking outside, say near buttons
			rectLayout.getLayoutParams().width = bitmapSizeWidth;
			rectLayout.getLayoutParams().height = bitmapSizeHeight;
		}
	}
	
	
	private void createRectMatrix( )
	{
		/*
		 * Initialize the rectArr storing Blocks with rect parameters
		 * Initialize TextViews for each rect
		 * Initialize parameters for each sqr
		 */
		
		for( int i=0; i<WORD_COUNT; i++ ) //row
		{
			for( int j=0; j<WORD_COUNT; j++ ) //column
			{
				//key: sqrL, sqrT means sqr coordinate top and left; sqrLO/TO means original coordinate sqrL/T
				//increase square dimensions
				sqrL = sqrLO + j*(sqrSizeWidth + SQR_INNER_DIVIDER); //define top-left corner
				sqrT = sqrTO + i*(sqrSizeHeight + SQR_INNER_DIVIDER);
				sqrR = sqrL + sqrSizeWidth; //define end of bottom-right corner
				sqrB = sqrT + sqrSizeHeight;
				
				//ADD PADDING
				//padding between rows
				sqrT = sqrT + (i/ROW_PER_BLOCK)*SQR_OUTER_DIVIDER;
				sqrB = sqrB + (i/ROW_PER_BLOCK)*SQR_OUTER_DIVIDER;
				
				//padding between columns
				sqrL = sqrL + (j/COL_PER_BLOCK)*SQR_OUTER_DIVIDER;
				sqrR = sqrR + (j/COL_PER_BLOCK)*SQR_OUTER_DIVIDER;
				
				
				rectArr[i][j] = new Block( sqrL, sqrT, sqrR, sqrB ); // create the new square
				//note: this loop has to be here and cannot be replaced by drw class
				
				// add text view to relative layout matrix
				textMatrix.newTextView( sqrL, sqrT, sqrSizeWidth, sqrSizeHeight, i, j, wordArray,
										usrSudokuArr, usrLangPref );
				rectTextLayout.addView( textMatrix.getRelativeTextView( i, j ) );
			}
		}
	}
	
	
	private void actionDown( )
	{
		/*
		 * This function updates all necessary touchX/Y coordinates and flags
		 * when user has clicked the screen
		 */
		
		/////////////// TEST ///////////
//		for( int a=0; a<WORD_COUNT; a++ ) //highlight top 2 rows for conflict
//		{
//			drawR.setConflictAtIndex( 0, a );
//			drawR.setConflictAtIndex( 1, a );
//		}
		////////////////////////////////

		//disable safety because by clicking, user updates to new valid coordinates
		zoomClickSafe[0] = 0;
		zoomButtonDisableUpdate[0] = 0; //once user clicks, the coordinates are updated and become valid, so let button update sqr clicked
		
		
		// SAVE ON CLICK COORDINATE
		if( zoomOn[0] == 1 ) // when zoom mode enabled
		{
			
			touchXZclick[0] = (int) ( touchX[0] ); //where user clicked adjusted for translation for zoom
			touchYZclick[0] = (int) ( touchY[0] ); //ie touchXZ was reference point (where "zoom" matrix was so far), now add extra
			
			Log.d( "TAG", " -- " );
			Log.d( "TAG", "touchXZ start: (" + touchXZ[0] + ", " + touchYZ[0] + ")" );
			Log.d( "TAG", "click down touchXZclick: (" + touchXZclick[0] + ", " + touchYZclick[0] + ")" );
		}
		else
		{
			Log.i("TAG", "normal click: (" + touchX[0] + ", " + touchY[0] + ")");
		}
		
		undoBtnPressed[0] = 0; //once currentRectColured updated from touch, allow to call drw.setDrawParameter()
		rotation[0] = 0; //allow drw.setDrawParameter() to update currentRectColoured

		drawR.setDrawParameters( touchX, touchY, lastRectColoured, currentRectColoured );

		if( currentRectColoured.getRow() != -1 ) {
			Log.d("TAG", "duplicate is about to be checked");
			// 0 == nothing selected; 1 == selected and correct; 2 == selected but incorrect
			if (usrSudokuArr.checkDuplicate(currentRectColoured.getRow(), currentRectColoured.getColumn())) {
				Log.d("TAG", "duplicate should have been found");
				currentSelectedIsCorrect = 2;
			} else {
				Log.d("TAG", "duplicate is not found");
				currentSelectedIsCorrect = 1;
			}
		}
		else
		{
			currentSelectedIsCorrect = 0;
		}
		drawR.reDraw( currentRectColoured, usrLangPref, currentSelectedIsCorrect );

		// TEXT TO SPEECH
		if (usrModePref == 1) {
			row = currentRectColoured.getRow();
			col = currentRectColoured.getColumn();
			
			if (row < WORD_COUNT && col < WORD_COUNT && row > -1 && col > -1) {
				val = usrSudokuArr.PuzzleOriginal[row][col];
				if (val != 0) {
					theWord = numArray[val-1];
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
						mTTS.speak(theWord, TextToSpeech.QUEUE_FLUSH, null, null);
					} else {
						mTTS.speak(theWord, TextToSpeech.QUEUE_FLUSH, null);
					}
				}
			}
		}
		
	}
	
	
	private void actionMove( )
	{
		/*
		 * Whenever the user drags on the screen, this function updates dX dY coordinates
		 * when user drags a click, the Canvas will be moved as well
		 * This function only works when Zoom is enabled
		 */
		
		// Note: touchXZ/XY is used as pixel coordinate in scaled image, to represent top left corner
		//		 coordinate of zoomed Canvas, ie a 1000p wide bitmap in NORMAL_ZOOM with 2x zoom scale will be 2000p wide (in ZOOM),
		//		 so touchXZ == 0 will be start top left corner, == 1000 top left corner will start and top right corner
		//		 would be 1000 + screen_width
		
		if( zoomOn[0] == 1 )
		{
			drag[0] = 1; //enable drag
			
			/* GET 'DRAG' BOUND IN X-AXIS AND BLOCK IF OUT-OF-BOUND */
			dX[0] = touchX[0] - touchXZclick[0]; // change = initial - final
			
			//fix out of bounds: LEFT
			if( touchXZ[0] - dX[0] < 0 ) //detect out of bounds
			{
				touchXZ[0] = 0;
				dX[0] = 0;
			}
			
			//fix out of bounds: RIGHT
			if( touchXZ[0] - dX[0] > bitmapSizeWidth*ZOOM_SCALE[0] - bitmapSizeWidth )
			{
				touchXZ[0] = (int)( bitmapSizeWidth*ZOOM_SCALE[0] - bitmapSizeWidth ); //set to max
				dX[0] = 0;
			}
			
			/* GET 'DRAG' BOUND IN Y-AXIS AND BLOCK IF OUT-OF-BOUND */
			dY[0] = touchY[0] - touchYZclick[0];
			
			//fix out of bounds: TOP
			if( touchYZ[0] - dY[0] < 0 ) //detect out of bounds
			{
				touchYZ[0] = 0; //reset to zero; before, was using "dY[0] = dYTcopy[0]" but using that was not quick enough at updating and resulting in skipping when going out of the border
				dY[0] = 0;
			}
			
			//fix out of bounds: BOTTOM
			if( touchYZ[0] - dY[0] > bitmapSizeHeight*ZOOM_SCALE[0] - bitmapSizeHeight )
			{
				touchYZ[0] = (int)(bitmapSizeHeight*ZOOM_SCALE[0] - bitmapSizeHeight); //set to max
				dY[0] = 0;
			}
			
			drawR.setDrawParameters( touchX, touchY, lastRectColoured, currentRectColoured );
			drawR.reDraw( currentRectColoured, usrLangPref, currentSelectedIsCorrect );
		}
	}
	
	
	private void actionUp( )
	{
		/*
		 * When ever user performs a ACTION_UP, this function resets drag coordinates
		 * and saves the touchZX/ZY
		 */
		
		if( zoomOn[0] == 1 )
		{
			if( drag[0] == 1 )
			{
				touchXZ[0] = touchXZ[0] - dX[0]; //update where new touch coordinate ended up when user stops 'touch'
				touchYZ[0] = touchYZ[0] - dY[0];
			}
			
			// reset 'moved' coordinates
			dX[0] = 0;
			dY[0] = 0;
			
			drag[0] = 0; //disable drag
			
			Log.i( "TAG", "moved dX: (" + dX[0] + ", " + dY[0] + ")" );
		}
	}
	
	private void findSqrCoordToZoomInOn( float[] ZOOM_SCALE_OLD )
	{
		/*
		 * This function adjusts the coordinates touchXZ[] of where to zoom in
		 * Say a user is in "zoom out" mode, then clicks on a square,
		 * then when user switched to "zoom in" mode, this will calculate
		 * coordinates to zoom in onto that selected square
		 *
		 * If no square selected, it will zoom in to center by default
		 */
		
		int topX; //x-coordinate of top left corner of where the view relative to zoomed canvas
		int topY;
		
		// TRANSLATE COORDINATE ONLY IF A SQUARE IS SELECTED
		if( currentRectColoured.getRow() != -1 )
		{
			int i = currentRectColoured.getRow( ); //row coordinate of selected square
			int j = currentRectColoured.getColumn( );
			
			//get coordinates of top left corner of square
			//get position of selected square (then scale)
			topX = (int) (( (textMatrix.getRelativeAndPos())[i][j] ).getSqrL( )*ZOOM_SCALE[0]);
			topY = (int) (( (textMatrix.getRelativeAndPos())[i][j] ).getSqrT( )*ZOOM_SCALE[0]);
			
			//translate from corner of square to corner of zoom view area
			//note: "zoom view area" is the square field of view, ie the only rectangular area that is visible in zoom mode out of whole puzzle
			topX = topX - (int)( (bitmapSizeWidth-sqrSizeWidth*ZOOM_SCALE[0]) / 2 );
			topY = topY - (int)( (bitmapSizeHeight-sqrSizeHeight*ZOOM_SCALE[0]) / 2 );
			
		}
		else
		{
//			if( touchXZ[0] == 0 && touchYZ[0] == 0 ) //start of game
//			{
//				// default: translate to center of matrix
//				topX = (int) (bitmapSizeWidth * ZOOM_SCALE[0] / 2 - bitmapSizeWidth / 2);
//				topY = (int) (bitmapSizeHeight * ZOOM_SCALE[0] / 2 - bitmapSizeHeight / 2);
//			}
//			else
//			{
				/*//default: no square selected
				//assume square in center selected
				//find coordinates of top left square corner
				topX = (int)( ( (touchXZ[0] + bitmapSizeWidth / 2f) - (sqrSizeWidth * ZOOM_SCALE_OLD / 2) ) / ZOOM_SCALE_OLD * ZOOM_SCALE[0] );
				topY = (int)( ( (touchYZ[0] + bitmapSizeHeight / 2f) - (sqrSizeHeight * ZOOM_SCALE_OLD / 2) ) / ZOOM_SCALE_OLD * ZOOM_SCALE[0] );
				
				//translate from corner of square to corner of zoom view area
				//note: "zoom view area" is the square field of view, ie the only rectangular area that is visible in zoom mode out of whole puzzle
				topX = topX - (int) ((bitmapSizeWidth - sqrSizeWidth * ZOOM_SCALE[0]) / 2);
				topY = topY - (int) ((bitmapSizeHeight - sqrSizeHeight * ZOOM_SCALE[0]) / 2);
				*/
			Log.d( "zoom", "touchXZ: " + touchXZ[0] + " bitmapSizeWidth: " + bitmapSizeWidth + " ZOOM_SCALE_OLD: " + ZOOM_SCALE_OLD[0] + " ZOOM_SCALE[]: " + ZOOM_SCALE[0] );
			
			topX = (int)( (touchXZ[0] + bitmapSizeWidth/2)/ZOOM_SCALE_OLD[0]*ZOOM_SCALE[0] - bitmapSizeWidth/2 );
			topY = (int)( (touchYZ[0] + bitmapSizeHeight/2)/ZOOM_SCALE_OLD[0]*ZOOM_SCALE[0] - bitmapSizeHeight/2 );
//			}
			
			Log.d( "zoom", "topX: " + topX + " topY: " + topY );
		}
		
			// FIX OUT-OF-BOUNDS
		//ie: if calculated coordinates of selected square is too close to top left corner, set view at (0,0)
		if( topX < 0 ) //if too close left
		{
			topX = 0;
		}
		else if( topX > bitmapSizeWidth*ZOOM_SCALE[0] - bitmapSizeWidth ) //if too close to right
		{
			topX = (int)( bitmapSizeWidth*ZOOM_SCALE[0] - bitmapSizeWidth );
		}
		
		if( topY < 0 ) //if too close to top
		{
			topY = 0;
		}
		else if( topY > bitmapSizeHeight*ZOOM_SCALE[0] - bitmapSizeHeight ) //if too close to bottom
		{
			topY = (int)( bitmapSizeHeight*ZOOM_SCALE[0] - bitmapSizeHeight );
		}
		
		touchXZ[0] = topX;
		touchYZ[0] = topY;
	}
	private void randomizeOrder(int[] arr)
	{
		//bit map to see what order has been used
		int size = arr.length;
		int[] numUsed = new int[size];
		for (int i=0; i<size; i++) {
			numUsed[i] = 0;
		}
		int randPos;
		Random rand = new Random();
		int i = 0;
		while (i < size) {
			randPos = rand.nextInt(100);
			randPos = randPos%size;
			if (numUsed[randPos] == 0) { // if not used before
				arr[i] = randPos; // put rand num back in arr
				numUsed[randPos] = 1; // mark as used
				i++; // by putting i++ here this only moves on until it find valid num
			}
		}
	}
	public void savetheInstanceState (int RorS, Bundle savedInstanceState, int sis_state, WordArray sis_wordArray, int sis_usrLangPref, SudokuGenerator sis_usrSudokuArr, int sis_usrModePref, String sis_language, String[] sis_numArray, int [] sis_orderArr, int sis_HCTMP, Pair sis_currentRectColoured, int sis_currentSelectedIsCorrect) {
		if (RorS == 0) {
			//we are receiving
			//a state had been saved, load it
			state = (int) savedInstanceState.getSerializable("state");
			wordArray = (WordArray) savedInstanceState.getParcelable("wordArray");
			usrLangPref = savedInstanceState.getInt("usrLangPref");
			usrSudokuArr = (SudokuGenerator) savedInstanceState.get("SudokuArr");
			usrModePref = (int) savedInstanceState.getSerializable("usrMode");
			language = (String) savedInstanceState.getSerializable("language");
			HINT_CLICK_TO_MAX_PROB = savedInstanceState.getInt( "HINT_CLICK_TO_MAX_PROB" );
			//touchX = (int[]) savedInstanceState.getIntArray("touchX");
			//touchY = (int[]) savedInstanceState.getIntArray("touchY");
			currentRectColoured = (Pair) savedInstanceState.getSerializable("currentRectColoured");
			currentSelectedIsCorrect = (int) savedInstanceState.getSerializable("currentSelectedIsCorrect");
			//rectArr = (Block[][]) savedInstanceState.getSerializable("rectArr");
			if (usrModePref == 1) {
				numArray = (String[]) savedInstanceState.getSerializable("numArray");
				orderArr = (int[]) savedInstanceState.getIntArray("orderArr");
			}
		}
		else {
			//we are sending
			if (sis_usrSudokuArr.isCorrect) {
				sis_state = 2;
			}
			else {sis_state = 1;}
			savedInstanceState.putInt("state", sis_state);
			savedInstanceState.putParcelable("wordArray", sis_wordArray);
			savedInstanceState.putInt( "usrLangPref", sis_usrLangPref );
			savedInstanceState.putSerializable("SudokuArr", sis_usrSudokuArr);
			savedInstanceState.putInt("usrMode", sis_usrModePref);
			savedInstanceState.putString("language", sis_language);
			savedInstanceState.putInt("HINT_CLICK_TO_MAX_PROB", sis_HCTMP);
			//savedInstanceState.putIntArray("touchX", touchx);
			//savedInstanceState.putIntArray("touchY", touchy);
			savedInstanceState.putSerializable("currentRectColoured", sis_currentRectColoured);
			savedInstanceState.putSerializable("currentSelectedIsCorrect", sis_currentSelectedIsCorrect);
			//savedInstanceState.putSerializable("rectArr", sis_rectArr);
			if (sis_usrModePref == 1) {
				savedInstanceState.putStringArray("numArray", sis_numArray);
				savedInstanceState.putIntArray("orderArr", sis_orderArr);
			}
		}
	}
}

































