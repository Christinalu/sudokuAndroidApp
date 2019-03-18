package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public class GameActivity extends AppCompatActivity
{
	/*
		This Game Activity is the main window where the Sudoku Puzzle is going to be displayed
		It will be activated from the Main (Menu) Activity
	*/

	private WordArray wordArray;
	private String[] numArray;
	private int usrLangPref;
	private int usrDiffPref = 0;
	private int state; //0=new start, 1=resume
	private int usrModePref; //0=standard, 1=speech

	private TextToSpeech mTTS;
	private String theWord;
	private String language;
	private int row;
	private int col;
	private int val;

	private Paint paint = new Paint( );
	private Bitmap bitMap;
	private Canvas canvas;
	private ImageView imgView;
	private RelativeLayout rectLayout;
	private RelativeLayout rectTextLayout;
	private View.OnTouchListener handleTouch;
	private SudokuGenerator usrSudokuArr;
	private FindSqrCoordToZoomInOn findSqrCoordToZoomInOn;

	private int sqrLO; // original left coordinate of where puzzle starts
	private int sqrTO; // original top coordinate of where puzzle starts

	private drw drawR; // class that draws the squares either highlighted or not, based on touch
	private Pair lastRectColoured = new Pair( -1, -1 ); // stores the last coloured square coordinates
	private Pair currentRectColoured = new Pair( -1, -1 ); // stores the current coloured square

	private Block[][] rectArr = new Block[9][9]; // stores all squares in a 2D array
	private Paint paintblack = new Paint();
	private TextMatrix textMatrix; //stores the TextView for drawing the text

	private int[] touchX = { 0 }; // hold user touch (x,y) coordinate
	private int[] touchY = { 0 };

	private int sqrL; //duplicate square coordinates
	private int sqrT;
	private int sqrR;
	private int sqrB;

	private ButtonListener listeners; // used to call another function to implement all button listeners, to save space in GameActivity
	private int[] btnClicked = { 0 }; //flag which is activated when a button is clicked - used to let zoom drw class to update TextView (for efficiency purposes)

	private int[] zoomOn = { 0 }; //indicates if user activated zoom 1==true, 0==false
	private int[] dX = { 0 }; //change in X coordinate ie 'drag'
	private int[] dY = { 0 };

	private int[] touchXZ = { 0 }; //stores where user would click in zoom mode - reference to left top corner
	private int[] touchYZ = { 0 };
	private int[] touchXZclick = { 0 }; //stores where user would click in zoom mode, but when there is no drag, only click down and up
	private int[] touchYZclick = { 0 };
	private int[] drag = { 0 }; // 1==user drags on screen

	private final static int SQR_INNER_DIVIDER = 5;
	private final static int SQR_OUTER_DIVIDER = 15;
	private final static int BOUNDARY_OFFSET = 40; //puzzle will have some offset for aesthetic reasons near edges
	private final static float ZOOM_SCALE = 1.5f; // zoom factor of how much to zoom in puzzle in "zoom" mode
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
	
	private int HINT_CLICK_TO_MAX_PROB;
	private static final float LANDSCAPE_RATIO = 0.75f; //determines how much of the screen will be dedicated to puzzle in landscape
	private int WORD_COUNT; //stores the number of words in wordArray
	private int COL_PER_BLOCK; //stores how many columns will be inside a block; in 9x9 this would be 3
	private int ROW_PER_BLOCK;
	private int VERTICAL_BLOCK; //stores how many (vertical) blocks are in a puzzle; in 9x9 this would be 3 blocks
	private int HORIZONTAL_BLOCK;
	

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		//set intent to receive word array from Main Activity
		if (savedInstanceState != null) {
			//a state had been saved, load it
			state = 1;
			wordArray = (WordArray) savedInstanceState.getSerializable("wordArrayGA");
			usrLangPref = savedInstanceState.getInt("usrLangPrefGA");
			usrSudokuArr = (SudokuGenerator) savedInstanceState.get("SudokuArrGA");
			usrModePref = (int) savedInstanceState.getSerializable("usrModeGA");
			language = (String) savedInstanceState.getSerializable("languageGA");
			HINT_CLICK_TO_MAX_PROB = savedInstanceState.getInt( "HINT_CLICK_TO_MAX_PROB" );
			if (usrModePref == 1) {
				numArray = (String[]) savedInstanceState.getSerializable("numArrayGA");
			}
		}
		else {
			Intent gameSrc = getIntent();
			if (gameSrc != null) {
				state = (int) gameSrc.getSerializableExtra("state");
				//check state: if 1 then we are resuming a previous game, otherwise state == 0 and we are starting a new game
				if (state == 1) {
					wordArray = (WordArray) gameSrc.getSerializableExtra("wordArrayMA");
					usrLangPref = (int) gameSrc.getSerializableExtra("usrLangPrefMA");
					usrSudokuArr = (SudokuGenerator) gameSrc.getSerializableExtra("SudokuArrMA");
					usrModePref = (int) gameSrc.getSerializableExtra("usrModeMA");
					language = (String) gameSrc.getSerializableExtra("languageMA");
					HINT_CLICK_TO_MAX_PROB = (int) gameSrc.getSerializableExtra( "HINT_CLICK_TO_MAX_PROB" );
					if (usrModePref == 1) {
						numArray = (String[]) gameSrc.getStringArrayExtra("numArrayMA");
					}
				} else {
					wordArray = (WordArray) gameSrc.getSerializableExtra("wordArray");
					usrLangPref = (int) gameSrc.getSerializableExtra("usrLangPref");
					usrDiffPref = (int) gameSrc.getSerializableExtra("usrDiffPref");
					usrModePref = (int) gameSrc.getSerializableExtra("usrModeMA");
					HINT_CLICK_TO_MAX_PROB = (int) gameSrc.getSerializableExtra( "HINT_CLICK_TO_MAX_PROB" );
					if (usrModePref == 1) {
						//create separate array to draw from for this mode
						
						
						// TODO: fix the following to adapt to different puzzle types
						
						
						numArray = new String[9];
						if (usrLangPref == 0) {
							for (int i = 0; i < 9; i++) {
								numArray[i] = wordArray.getWordTranslationAtIndex( i );
								wordArray.setWordTranslationAtIndex( i, Integer.toString(i+1) );
							}
						}
						else {
							for (int i = 0; i < 9; i++) {
								numArray[i] = wordArray.getWordNativeAtIndex( i );
								wordArray.setWordNativeAtIndex( i, Integer.toString(i + 1) );
							}
						}
					}
					usrSudokuArr = new SudokuGenerator(usrDiffPref);
					language = (String) gameSrc.getSerializableExtra("languageMA");
				}

				
				//debug wordArray
				Log.d( "upload", " @ WORD_ARRAY ON RESUME GAME:" );
				for( int i=0; i<9; i++ )
				{
					Log.d( "upload","wordArr[" + i + "]: " + wordArray.getWordNativeAtIndex( i ) + "," + wordArray.getWordTranslationAtIndex( i ) + "," + wordArray.getWordInFileLineNumAtIndex( i ) + "," + wordArray.getWordHintClickAtIndex( i ) );
				}
			}
		}

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



			/**  INITIALIZE  **/

		// get display metrics
		int orientation = Configuration.ORIENTATION_UNDEFINED;
		DisplayMetrics displayMetrics = new DisplayMetrics( );
		Display screen = getWindowManager( ).getDefaultDisplay( ); //get general display
		screen.getMetrics( displayMetrics );

		screenH = displayMetrics.heightPixels;
		screenW = displayMetrics.widthPixels;

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


		rectLayout = (RelativeLayout) findViewById( R.id.rect_layout ); //used to detect user touch for matrix drw
		rectTextLayout = (RelativeLayout) findViewById( R.id.rect_txt_layout ); //used to crop TextViews the same size as bitmap
		imgView = new ImageView(this);


		//status bar height
		//this is needed as offset in landscape mode to alight rect matrix with textOverlay
		int barH = getStatusBarHeight();
		
		// SET PUZZLE ROW AND COL NUMBER PER BLOCK
		WORD_COUNT = wordArray.getWordCount( );
		
		if( usrModePref == wordArray.getSize4x4() ) {
			COL_PER_BLOCK = 2;
			ROW_PER_BLOCK = 2;
			VERTICAL_BLOCK = 2;
			HORIZONTAL_BLOCK = 2;
		}
		else if( usrModePref == wordArray.getSize6x6() ){
			COL_PER_BLOCK = 3;
			ROW_PER_BLOCK = 2;
			VERTICAL_BLOCK = 2;
			HORIZONTAL_BLOCK = 3;
		}
		else if( usrModePref == wordArray.getSize12x12() ){
			COL_PER_BLOCK = 4;
			ROW_PER_BLOCK = 3;
			VERTICAL_BLOCK = 3;
			HORIZONTAL_BLOCK = 4;
		}
		else{
			COL_PER_BLOCK = 3;
			ROW_PER_BLOCK = 3;
			VERTICAL_BLOCK = 3;
			HORIZONTAL_BLOCK = 3;
		}

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

			bitMap = Bitmap.createBitmap( bitmapSizeWidth, bitmapSizeHeight-barH, Bitmap.Config.ARGB_8888 );

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

			bitMap = Bitmap.createBitmap( bitmapSizeWidth, bitmapSizeHeight, Bitmap.Config.ARGB_8888 );
			
			//set rect_txt_layout the same size as the bitmap
			rectTextLayout.getLayoutParams().width = bitmapSizeWidth;
			rectTextLayout.getLayoutParams().height = bitmapSizeHeight;

			//note: limiting rectLayout to bitmap size will force a selected square to be deselected only when clicking inside the bitmap
			//		it wont deselect when clicking outside, say near buttons
			rectLayout.getLayoutParams().width = bitmapSizeWidth;
			rectLayout.getLayoutParams().height = bitmapSizeHeight;
		}
		
		canvas = new Canvas( bitMap );
		imgView.setImageBitmap( bitMap );
		paint.setColor(Color.parseColor("#c2c2c2"));

		rectLayout.addView( imgView );

		textMatrix = new TextMatrix( this, sqrSizeWidth, sqrSizeHeight, ZOOM_SCALE, WORD_COUNT );


		// set up object to translate to selected square in "zoom" mode
		// note: requires sqrSize be determined before call
		findSqrCoordToZoomInOn = new FindSqrCoordToZoomInOn( touchXZ, touchYZ, currentRectColoured, bitmapSizeWidth, bitmapSizeHeight,
															 sqrSizeWidth, sqrSizeHeight, textMatrix, ZOOM_SCALE );





		// PREDEFINE VARIABLES FOR MATRIX OVERLAY
		paintblack.setColor(Color.parseColor("#0000ff"));
		paintblack.setTextSize(30);

		drawR = new drw( rectArr, paint, canvas, rectLayout, rectTextLayout, textMatrix, usrSudokuArr, zoomOn, drag,
						 dX, dY, touchXZ, touchYZ, zoomButtonSafe, zoomClickSafe, zoomButtonDisableUpdate,
						 bitmapSizeWidth, bitmapSizeHeight, wordArray, btnClicked, ZOOM_SCALE,
						 COL_PER_BLOCK, ROW_PER_BLOCK, VERTICAL_BLOCK, HORIZONTAL_BLOCK, WORD_COUNT ); // class used to draw/update square matrix
		
		
		// call function to set all listeners - needs drawR
		listeners = new ButtonListener( currentRectColoured, usrSudokuArr,
										drawR, touchX, touchY, lastRectColoured, usrLangPref, btnClicked,
										Hint, wordArray,usrModePref,numArray, WORD_COUNT, COL_PER_BLOCK, ROW_PER_BLOCK,
										this );



			/** CREATE RECT MATRIX **/

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


		//draw matrix so far
		drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, usrLangPref );


			/* ZOOM IN BUTTON */

		ImageButton btnZoomIn = findViewById( R.id.button_zoom_in );
		btnZoomIn.setOnClickListener(new View.OnClickListener( )
			{
				@Override
				public void onClick( View v )
				{
					if( zoomInBtnOn[0] == 1 ) //only allow btn to be clicked when activated
					{
						zoomOn[0] = 1; //set zoom as activated
						zoomButtonSafe[0] = 1; //zoomSafe needed
						zoomClickSafe[0] = 1;
						zoomButtonDisableUpdate[0] = 1; //do not let button update coordinate when switching modes due to zoomX scaling

						textMatrix.scaleTextZoomIn( );
						textMatrix.reDrawTextZoom( touchXZ, touchYZ, dX, dY ); // call this because .scaleTextZoom() only scales Layouts, so call this to place them in correct (drag) position

						touchXZ[0] = 0;
						touchYZ[0] = 0;

						// on zoom in, calculate coordinate to zoom on selected square
						findSqrCoordToZoomInOn.findSqrCoordToZoomInOn( );

						drawR.reDraw(touchX, touchY, lastRectColoured, currentRectColoured, usrLangPref);

						zoomButtonSafe[0] = 0; //zoomSafe needed
						zoomOutBtnOn[0] = 1; //activate other zoom btn
						zoomInBtnOn[0] = 0; //deactivate this btn so text not scaled multiple times
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
								zoomOn[0] = 0; //set zoom as inactivated
								zoomButtonSafe[0] = 1; // do not update sqr on button click
								zoomClickSafe[0] = 1;
								zoomButtonDisableUpdate[0] = 1; //do not let button update coordinate when switching modes due to zoomX scaling

								drawR.reDraw(touchX, touchY, lastRectColoured, currentRectColoured, usrLangPref);
								textMatrix.scaleTextZoomOut( );

								zoomButtonSafe[0] = 0;
								zoomInBtnOn[0] = 1; //activate other zoom btn
								zoomOutBtnOn[0] = 0; //deactivate this btn so text not scaled multiple times
							}
						}
					}
		);


			// Legend:
			// dX : pixel drag on screen non-scaled
			// touchXZ : pixel coordinate in scaled image ie a 1000p wide bitmap with 2x scale will be 2000p wide,
			// 			 so touchXZ = 0 will be start, =1000 means center, =2000 means end
			// touchXZclick : initial (stored) regular pixel coordinate of where user first clicked



			/** ON-TOUCH **/

		handleTouch = new View.OnTouchListener( )
		{
			@Override
			public boolean onTouch( View v, MotionEvent event )
			{
				touchX[0] = (int) event.getX( ); // touch coordinate on actual screen inside RelativeLayout rect_layout - starting from top left corner @ (0,0)
				touchY[0] = (int) event.getY( );

				switch( event.getAction( ) )
				{
					case MotionEvent.ACTION_DOWN:

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

							drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, usrLangPref );
						}
						else
						{
							// call function to redraw if user touch detected
							drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, usrLangPref );
							Log.i("TAG", "normal click: (" + touchX[0] + ", " + touchY[0] + ")");
						}
						if (usrModePref == 1) {
							row = currentRectColoured.getRow();
							col = currentRectColoured.getColumn();
							if (row < 9 && col < 9 && row > -1 && col > -1) {
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
						break;


					case MotionEvent.ACTION_MOVE:

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
							if( touchXZ[0] - dX[0] > bitmapSizeWidth*ZOOM_SCALE - bitmapSizeWidth )
							{
								touchXZ[0] = (int)( bitmapSizeWidth*ZOOM_SCALE - bitmapSizeWidth ); //set to max
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
							if( touchYZ[0] - dY[0]  > bitmapSizeHeight*ZOOM_SCALE - bitmapSizeHeight )
							{
								touchYZ[0] = (int)(bitmapSizeHeight*ZOOM_SCALE - bitmapSizeHeight); //set to max
								dY[0] = 0;
							}

							drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, usrLangPref );
						}
						break;

					case MotionEvent.ACTION_UP:

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
		savedInstanceState.putSerializable("wordArrayGA", wordArray);
		savedInstanceState.putInt( "usrLangPrefGA", usrLangPref );
		savedInstanceState.putSerializable("SudokuArrGA", usrSudokuArr);
		savedInstanceState.putInt("state", state);
		savedInstanceState.putInt("usrModeGA", usrModePref);
		savedInstanceState.putString("languageGA", language);
		if (usrModePref == 1) {
			savedInstanceState.putStringArray("numArrayGA", numArray);
		}
	}

	@Override
	public void onBackPressed() {
		Intent resumeSrc = new Intent( GameActivity.this, MainActivity.class );
		state = 1;
		resumeSrc.putExtra( "wordArrayGA", wordArray );
		resumeSrc.putExtra( "usrLangPrefGA", usrLangPref );
		resumeSrc.putExtra("SudokuArrGA", usrSudokuArr);
		resumeSrc.putExtra("state", state);
		resumeSrc.putExtra("usrModeGA", usrModePref);
		resumeSrc.putExtra("languageGA", language);
		if (usrModePref == 1) {
			resumeSrc.putExtra("numArrayGA", numArray);
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
		
		int HINT_CLICK_ON_DECREASE = 1; //if user had a word in puzzle but did not use HintClick, then it implies the user has less difficulty with word, so decrease HintClick by this
		FileInputStream fileInStream = null; //open file from internal storage
		try {
			fileInStream = this.openFileInput( wordArray[10].getNative( ) ); //get internal file name, contained in 10th index of wordArray
			
			
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
				for( int k=0; k<WORD_COUNT; k++ ) //loop through all '9' words
				{
					if( i == wordArray[k].getInFileLineNum() ) //if updating line with word that was used in wordArray
					{
						strSplit = line.split(","); //get all attribute
						long hintClickSoFar = Long.parseLong(strSplit[2]); //get original click count from file
						
						//to what hintClickSoFar was so far originally in the file, add wordArray.getHintClick() from the current game
						//newHintClick = hintClickSoFar + wordArray[k].getHintClick()*STATISTIC_MULTIPLE;
						
						if( wordArray[k].getHintClick() == 0 ) //if no HintClicks for this word, it means user has less difficulty so decrease HintClick to decrease probability
						{
							if( wordArray[k].getAllowToDecreaseDifficulty() ) //if user inserted correct word (ONLY ONCE)
							{
								wordArray[k].setDoNotAllowToDecreaseDifficulty( ); //disable so that word cannot have difficulty decreased in this game, until user starts new game
								newHintClick = hintClickSoFar - HINT_CLICK_ON_DECREASE;
								Log.d( "selectW", "word: " + wordArray[k].getNative() );
								Log.d( "selectW", "line index: " + i );
								Log.d( "selectW", "HintClick decreased with hintClickSoFar=" + hintClickSoFar + " newHintClick=" + newHintClick );
							}
							else
							{ newHintClick = hintClickSoFar; } //keep the same HintCount (because newHintCount==0 and no need to decrease count)
						}
						else
						{ newHintClick = hintClickSoFar +  wordArray[k].getHintClick(); } //user used HintClick
						
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
			outStream = this.openFileOutput( wordArray[10].getNative( ), this.MODE_PRIVATE ); //open private output stream for re-write
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
			wordArray[i].updateHintClick( 0 );
		}
	}
}

































