package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity
{
	/*
		This Game Activity is the main window where the Sudoku Puzzle is going to be displayed
		It will be activated from the Main (Menu) Activity
	*/

	public Word[] wordArray;
	private int usrLangPref;
	private int usrDiffPref = 0;

	private Paint paint = new Paint( );
	private Bitmap bgMap;
	private Canvas canvas;
	private ImageView imgView;
	private RelativeLayout rectLayout;
	private RelativeLayout rectTextLayout;
	private View.OnTouchListener handleTouch;
	private SudokuGenerator usrSudokuArr;

	private int sqrLO; // original left coordinate of where puzzle starts
	private int sqrTO; // original top coordinate of where puzzle starts

	private drw drawR; // class that draws the squares either highlighted or not, based on touch
	private Pair lastRectColoured = new Pair( -1, -1 ); // stores the last coloured square coordinates
	private Pair currentRectColoured = new Pair( -1, -1 ); // stores the current coloured square

	private Block[][] rectArr = new Block[9][9]; // stores all squares in a 2D array
	private Paint paintblack = new Paint();
	private Paint paintLight = new Paint( );
	private RedrawText textOverlay; // class used to redraw GUI text overlay
	private TextMatrix textMatrix; //stores the TextView for drawing the text

	private int[] touchX = { 0 }; // hold user touch (x,y) coordinate
	private int[] touchY = { 0 };

	private int sqrL; //duplicate square coordinates
	private int sqrT;
	private int sqrR;
	private int sqrB;

	private Button [] btnArr;
	private ButtonListener listeners; // used to call another function to implement all button listeners, to save space in GameActivity
	private int[] btnClicked = { 0 }; //flag which is activated when a button is clicked - used to let zoom drw class to update TextView (for efficiency purposes)

	private int[] zoomOn = { 0 }; //indicates if user activated zoom 1==true, 0==false
	//private int[] iX = { 0 }; //initial touch coordinate
	//private int[] iY = { 0 };
	private int[] dX = { 0 }; //change in X coordinate ie 'drag'
	private int[] dY = { 0 };

	private int[] touchXZ = { 0 }; //stores where user would click in zoom mode - reference to left top corner
	private int[] touchYZ = { 0 };
	private int[] touchXZclick = { 0 }; //stores where user would click in zoom mode, but when there is no drag, only click down and up
	private int[] touchYZclick = { 0 };
	private int[] drag = { 0 }; // 1==user drags on screen

	//private final static int BIT_MAP_W = 1052; //NOTE: !! this constant is also currently in .xml file - bitmap width (see later in code how to get this number)
	//private final static int BIT_MAP_H = 1055; //bitmap height
	private final static int SQR_INNER_DIVIDER = 5;
	private final static int SQR_OUTER_DIVIDER = 15;
	private final static int BOUNDARY_OFFSET = 40; //puzzle will have some offset for aesthetic reasons
	private int PUZZLE_FULL_SIZE; // size of full puzzle from left most column pixel to right most column pixel

	private int[] zoomButtonSafe = { 0 }; //used to prevent a 'click coordinate' from being tested if it is in a rect when switching to "zoom" mode because switching to the mode, should not test click
	private int[] zoomClickSafe = { 0 }; //used to block touchX update when user switches mode
	private int[] zoomButtonDisableUpdate = { 0 }; //do not let button update entry right after changing "zoom" mode - because zoomX is incorrect and must be updated first by having user click somewhere first
	private int[] zoomInBtnOn = { 1 }; //enable/disable zoom btn; "on" by default
	private int[] zoomOutBtnOn = { 0 };

	private int screenH;
	private int screenW;
	private int bitmapSize; //lowest of screen dimensions - size of square bitmap
	private int sqrSize; //size of single square in matrix


	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		//set intent to receive word array from Main Activity
		Intent wordArraySrc = getIntent( );
		if (wordArraySrc != null) {
			wordArray = (Word[]) wordArraySrc.getSerializableExtra("wordArray");
			usrLangPref =  (int) wordArraySrc.getSerializableExtra("usrLangPref");
		}
		//set intent to receive Puzzle array from Main Activity
		Intent usrDiffPrefSrc = getIntent( );
		if (usrDiffPrefSrc != null) {
			usrDiffPref =  (int) usrDiffPrefSrc.getSerializableExtra("usrDiffPref");
		}

        // stores the generated puzzle, including arrays of solution and user current puzzle
        usrSudokuArr = new SudokuGenerator( usrDiffPref );

		//create dictionary button
		Button btnDictionary = (Button) findViewById(R.id.button_dictionary);

		btnDictionary.setOnClickListener(new View.OnClickListener() { // important, SudokuGenerator Arr must be initialized before this
											 @Override
											 public void onClick(View v) {
												 //create activity window for the dictionary
												 Intent activityDictionary = new Intent(GameActivity.this, DictionaryActivity.class);

												 //save wordArray for Dictionary Activity
												 activityDictionary.putExtra("wordArray", wordArray);
												 startActivity(activityDictionary); //switch to dictionary window
											 }
										 }
		);


			/**  INITIALIZE  **/

		// get display metrics
		DisplayMetrics displayMetrics = new DisplayMetrics( );
		getWindowManager( ).getDefaultDisplay( ).getMetrics( displayMetrics );
		screenH = displayMetrics.heightPixels;
		screenW = displayMetrics.widthPixels;

		// create canvas and bitmap

		// bitmap dimension: width=37+9×(105+5)+15+15−5=1052 height=40+9×(105+5)+15+15−5=1055
		// important to keep this exactly for scaling ie "zoom mode"

		//find minimum to create square bitmap
		//note: in Canvas, a bitmap starts from coordinate (0,0), so a bitmap must cover the entire screen size
		if( screenH < screenW )
		{ bitmapSize = screenH; }
		else{ bitmapSize = screenW; }

		/*//cap size at 1500px (do not consider screens with very small screen sizes, only large displays to limit CPU calculations)
		if( bitmapSize > 1500 )
		{ bitmapSize = 1500; }*/

		bgMap = Bitmap.createBitmap( bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888 );
		canvas = new Canvas( bgMap );

		// find matrix single square size based on screen
		sqrSize = ( bitmapSize - 2*BOUNDARY_OFFSET - 6*SQR_INNER_DIVIDER - 2*SQR_OUTER_DIVIDER ) / 9;

		// find entire matrix puzzle size
		PUZZLE_FULL_SIZE = 9*sqrSize + 6*SQR_INNER_DIVIDER + 2*SQR_OUTER_DIVIDER;

		// center bitmap
		// original coordinates of where to start to draw square (LO == Left Original)
		// key: sqrLO/TO determines where the drawR class will start drawing from
		sqrLO = BOUNDARY_OFFSET + (screenW-2*BOUNDARY_OFFSET)/2 - PUZZLE_FULL_SIZE/2; // if math correct, sqrLO should == BOUNDARY_OFFSET
		sqrTO = BOUNDARY_OFFSET; // no boundary offset - note: boundary offset is already included in bitmapSize because width == height, so sqrSize calculated for sqrLO width assures the offset is included in height sqrTO

		Log.d( "TAG", "--sqrLO: " + sqrLO );
		Log.d( "TAG", "--sqrTO: " + sqrTO );

		imgView = new ImageView(this);
		paint.setColor(Color.parseColor("#c2c2c2"));

		// get the RelativeLayout rect_layout as the main layout to draw on
		imgView.setImageBitmap( bgMap );
		rectLayout = (RelativeLayout) findViewById( R.id.rect_layout ); //used to detect user touch for matrix drw
		rectLayout.addView( imgView );

		rectTextLayout = (RelativeLayout) findViewById( R.id.rect_txt_layout ); //used to crop TextViews the same size as bitmap

		//set rect_txt_layout the same size as the bitmap
		rectTextLayout.getLayoutParams().height = bitmapSize;
		rectTextLayout.getLayoutParams().width = bitmapSize;

		//rectTextLayout.setLayoutParams(  );

		textMatrix = new TextMatrix( this, sqrSize );




			/** SET BUTTON LISTENERS **/

		btnArr = new Button[9]; // set buttons as an array
		btnArr[0] = (Button) findViewById( R.id.keypad_1 );
		btnArr[1] = (Button) findViewById( R.id.keypad_2 );
		btnArr[2] = (Button) findViewById( R.id.keypad_3 );
		btnArr[3] = (Button) findViewById( R.id.keypad_4 );
		btnArr[4] = (Button) findViewById( R.id.keypad_5 );
		btnArr[5] = (Button) findViewById( R.id.keypad_6 );
		btnArr[6] = (Button) findViewById( R.id.keypad_7 );
		btnArr[7] = (Button) findViewById( R.id.keypad_8 );
		btnArr[8] = (Button) findViewById( R.id.keypad_9 );

		// choose button language based on user preference
		if( usrLangPref == 0 )
		{
			btnArr[0].setText( "ONE" );
			btnArr[1].setText( "TWO" );
			btnArr[2].setText( "THREE" );
			btnArr[3].setText( "FOUR" );
			btnArr[4].setText( "FIVE" );
			btnArr[5].setText( "SIX" );
			btnArr[6].setText( "SEVEN" );
			btnArr[7].setText( "EIGHT" );
			btnArr[8].setText( "NINE" );
		}


		// PREDEFINE VARIABLES FOR MATRIX OVERLAY
		paintblack.setColor(Color.parseColor("#0000ff"));
		paintblack.setTextSize(30);

		// initialize text overlay
		//textOverlay = new RedrawText( sqrLO, sqrTO, usrSudokuArr, canvas, paintblack, wordArray,
		//		 					  SQR_INNER_DIVIDER, SQR_OUTER_DIVIDER, sqrSize );

		drawR = new drw( rectArr, paint, canvas, rectLayout, rectTextLayout, textMatrix, usrSudokuArr, zoomOn, drag,
						 dX, dY, touchXZ, touchYZ, zoomButtonSafe, zoomClickSafe, zoomButtonDisableUpdate,
						 bitmapSize, wordArray, btnClicked ); // class used to draw/update square matrix

		// call function to set all listeners - needs drawR, textOverlay
		listeners = new ButtonListener( currentRectColoured, usrSudokuArr, textOverlay, btnArr,
										drawR, touchX, touchY, lastRectColoured, usrLangPref, btnClicked );

		if( textOverlay == null )
		{
			Log.d( "NULL-2", "textOverlay initialized null" );
		}



		//
		//	STATUS: trying to add individual linear layout for each textView to prevent marquee reset
		//


			/** CREATE RECT MATRIX **/


		//canvas.drawColor( Color.parseColor( "#ffff00" ) );

		for( int i=0; i<9; i++ ) //row
		{
			for( int j=0; j<9; j++ ) //column
			{
				//increase square dimensions
				sqrL = sqrLO + j*(sqrSize + SQR_INNER_DIVIDER);
				sqrT = sqrTO + i*(sqrSize + SQR_INNER_DIVIDER);
				sqrR = sqrL + sqrSize;
				sqrB = sqrT + sqrSize;


				//add padding
				if( i>=3 ) //add extra space between rows
				{
					sqrT = sqrT + SQR_OUTER_DIVIDER; //square padding
					sqrB = sqrB + SQR_OUTER_DIVIDER;
				}
				if( i>=6 )
				{
					sqrT = sqrT + SQR_OUTER_DIVIDER; //square padding
					sqrB = sqrB + SQR_OUTER_DIVIDER;
				}

				if( j>=3 ) //add extra space between columns
				{
					sqrL = sqrL + SQR_OUTER_DIVIDER; //square padding
					sqrR = sqrR + SQR_OUTER_DIVIDER;
				}
				if( j>=6 )
				{
					sqrL = sqrL + SQR_OUTER_DIVIDER; //square padding
					sqrR = sqrR + SQR_OUTER_DIVIDER;
				}

				rectArr[i][j] = new Block( sqrL, sqrT, sqrR, sqrB ); // create the new square
				//note: this loop has to be here and cannot be replaced by drw class

				// add text view to relative layout
				textMatrix.newTextView( sqrL, sqrT, sqrSize, i, j, wordArray,
												  usrSudokuArr, usrLangPref );
				rectTextLayout.addView( textMatrix.getTextView( i, j ) );
			}
		}


		// test ////
		/*TextView txtV = new TextView( this );
		rectLayout.addView( txtV );
		txtV.setX( -30f );
		txtV.setText( "TEST" );*/
		//////////


		//draw matrix so far
		drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, usrLangPref );


		/////////////
		//	note: the reason the puzzle is cut ~20px on the right hand side when dragging in 'zoom' mode
		// 			(say when dragging in center of puzzle), the reason is because for nexus the width is 1080p
		// 			but the bitmap is centered @(0,0) and its only 1052px, so there are 28px undrawn
		//	note: the reason it doesnt deselect when pressed outside is because (if) shrinked
		// 			the RelativeLayout to puzzle-size 1052p instead of "match_parent", it doesn't have an "outside" to click on
		//	DO NOT FORGET TO ADJUST HINT POP-UP TO DISPLAY CORRECT LANGUAGES
		///////////////////////

		//////////////////////////
		//
		//	dont forget to also consider boundary offset at the end bounds of puzzle
		//	see if puzzle still adapts correctly after reorienting (tilting) screen
		//	add feature where when zooming in, it zooms to show selected (CHECK FIRST TO SEE IF SQR IS SELECTED to zoom on it, IF NOT, ie currectRect = (-1,-1) then default zoom in center
		//	NOTE: for zoom activity to make more sense and be more effective, increase text size 1.5
		//
		//////////////////////////





			/* ZOOM IN BUTTON */

		Button btnZoomIn =findViewById( R.id.button_zoom_in );
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

						drawR.reDraw(touchX, touchY, lastRectColoured, currentRectColoured, usrLangPref);

						zoomButtonSafe[0] = 0; //zoomSafe needed
						zoomOutBtnOn[0] = 1; //activate other zoom btn
						zoomInBtnOn[0] = 0; //deactivate this btn so text not scaled multiple times
					}
				}
			}
		);



		//////////////////////
		//	here have to disable and re enable zoom buttons - pressing zoom in button twice redraws incorrectly
		/////////////////



			/* ZOOM OUT BUTTON */

		Button btnZoomOut =findViewById( R.id.button_zoom_out );
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

						if( textOverlay == null )
						{
							Log.d( "NULL-2", "textOverlay null in onTouch in GameActivity" );
						}

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
							//iX[0] = touchX[0]; //initial coordinate where user touched
							//iY[0] = touchY[0];

							// call function to redraw if user touch detected
							drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, usrLangPref );
							Log.i("TAG", "normal click: (" + touchX[0] + ", " + touchY[0] + ")");
						}
						break;

					case MotionEvent.ACTION_MOVE:

						if( zoomOn[0] == 1 )
						{
							drag[0] = 1; //enable drag

								/* GET 'DRAG' BOUND IN X-AXIS */
							dX[0] = (touchX[0] ) - touchXZclick[0]; // change = initial - final

							//fix out of bounds: LEFT
							if( touchXZ[0] - dX[0] < 0 ) //detect out of bounds
							{
								touchXZ[0] = 0;
								dX[0] = 0;
							}

							//fix out of bounds: RIGHT
							if( touchXZ[0] - dX[0] > bitmapSize )
							{
								touchXZ[0] = bitmapSize; //set to max
								dX[0] = 0;
							}

							/////////////
							//	here make sure to change out of bounds constants and make them adaptive
							//	so they can adapt to screen ie change "out of bounds" to adapt to change
							//	change to different screen resolutions
							/////////////


								/* GET 'DRAG' BOUND IN Y-AXIS */
							dY[0] = (touchY[0] ) - touchYZclick[0];

							//fix out of bounds: TOP
							if( touchYZ[0] - dY[0] < 0 ) //detect out of bounds
							{
								touchYZ[0] = 0; //reset to zero; before, was using "dY[0] = dYTcopy[0]" but using that was not quick enough at updating and resulting in skipping when going out of the border
								dY[0] = 0;
							}

							//fix out of bounds: BOTTOM
							if( touchYZ[0] - dY[0] > bitmapSize )
							{
								touchYZ[0] = bitmapSize; //set to max
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
							Log.d( "TAG", "click down touchXZclick after drag: (" + touchXZclick[0] + ", " + touchYZclick[0] + ")" );
							Log.d( "TAG", "click up touchXZ: (" + touchXZ[0] + ", " + touchYZ[0] + ")" );
						}
						break;
				}

				return true;
			}
		};

		// initialize onTouchListener as defined above
		rectLayout.setOnTouchListener( handleTouch );
	}


	@Override
	public void onStart( )
	{
		super.onStart( );

		//disable slide in animation
		overridePendingTransition(0, 0);
	}

}
