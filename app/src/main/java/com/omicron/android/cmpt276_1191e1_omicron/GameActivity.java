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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
	private View.OnTouchListener handleTouch;
	private SudokuGenerator usrSudokuArr;

	private int sqrLO; // original left coordinate of where puzzle starts
	private int sqrTO; // original top coordinate of where puzzle starts
	private int txtL; // text coordinates
	private int txtT;

	private drw drawR; // class that draws the squares either highlighted or not, based on touch
	private Pair lastRectColoured = new Pair( -1, -1 ); // stores the last coloured square coordinates
	private Pair currentRectColoured = new Pair( -1, -1 ); // stores the current coloured square

	private Rect[][] rectArr = new Rect[9][9]; // stores all squares in a 2D array
	private Rect[][] rectArrZoom = new Rect[9][9]; // stores all ZOOM MODE squares in a 2D array
	private Paint paintblack = new Paint();
	private RedrawText textOverlay; // class used to redraw GUI text overlay

	private int[] touchX = { 0 }; // hold user touch (x,y) coordinate
	private int[] touchY = { 0 };

	private int sqrL; //duplicate square coordinates
	private int sqrT;
	private int sqrR;
	private int sqrB;

	private Button [] btnArr;
	private ButtonListener listeners; // used to call another function to implement all button listeners, to save space in GameActivity

	private float scale = 1f;
	private Matrix matrix =  new Matrix( );
	private ScaleGestureDetector scaleGD;

	private int[] zoomOn = { 0 }; //indicates if user activated zoom 1==true, 0==false
	private int[] iX = { 0 }; //initial touch coordinate
	private int[] iY = { 0 };
	private int[] dX = { 0 }; //change in X coordinate
	private int[] dY = { 0 };
	private int[] touchXZ = { 0 }; //stores where user would click in zoom mode
	private int[] touchYZ = { 0 };
	private int[] touchXZclick = { 0 }; //stores where user would click in zoom mode, but when there is no drag, only click down and up
	private int[] touchYZclick = { 0 };
	private int drag = 0; // 1==user drags on screen
	private final static int BIT_MAP_W = 1052;
	private final static int BIT_MAP_H = 1055;


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

		//initialize ScaleGestureDetector
		//scaleGD = new ScaleGestureDetector( this, new ScaleListener( ) );

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
		final int screenH = displayMetrics.heightPixels;
		final int screenW = displayMetrics.widthPixels;

		Log.d( "TAG", "--screenH: " + screenH );
		Log.d( "TAG", "--screenW: " + screenW );

		// create canvas and bitmap

		///////////////
		//
		//	here adapt bitmap width and height from 1100 to something else that matches screens
		//	also note 1100 is present in other functions
		//
		///////////////

		// bitmap dimension: width=37+9×(105+5)+15+15−5=1052 height=40+9×(105+5)+15+15−5=1055
		// important to keep this exactly for scaling ie "zoom mode"
		bgMap = Bitmap.createBitmap(BIT_MAP_W, BIT_MAP_H, Bitmap.Config.ARGB_8888); // important to keep bitmap aligned!, width=37+9×(105+5)+15+15−5=1052 height=40+9×(105+5)+15+15−5=1055
		canvas = new Canvas(bgMap);

		//original coordinates of where to draw square
		sqrLO = (int) (1052 / 2.0 - 1005 / 2.0 );  //(screenW / 2.0 - 1005 / 2.0 );
		sqrTO = 40;

		Log.d( "TAG", "--sqrLO: " + sqrLO );
		Log.d( "TAG", "--sqrTO: " + sqrTO );

		// initialize 'zoom' touch coordinate in center of screen
		//touchXZclick[0] =


		imgView = new ImageView(this);
		//imgView = (ImageView) findViewById( R.id.img_view );
		paint.setColor(Color.parseColor("#c2c2c2"));

		// get the RelativeLayout rect_layout as the main layout to draw on
		imgView.setImageBitmap(bgMap);
		rectLayout = (RelativeLayout) findViewById(R.id.rect_layout);
		rectLayout.addView(imgView);



			/** CREATE RECT MATRIX **/

		for( int i=0; i<9; i++ ) //row
		{
			for( int j=0; j<9; j++ ) //column
			{
				//increase square dimensions
				sqrL = sqrLO + j*(105+5);
				sqrT = sqrTO + i*(105+5);
				sqrR = sqrL + 105;
				sqrB = sqrT + 105;

				//add padding
				if( i>=3 ) //add extra space between rows
				{
					sqrT = sqrT + 15;
					sqrB = sqrB + 15;
				}
				if( i>=6 )
				{
					sqrT = sqrT + 15;
					sqrB = sqrB + 15;
				}

				if( j>=3 ) //add extra space between columns
				{
					sqrL = sqrL + 15;
					sqrR = sqrR + 15;
				}
				if( j>=6 )
				{
					sqrL = sqrL + 15;
					sqrR = sqrR + 15;
				}

				rectArr[i][j] = new Rect( (int)(sqrL), (int)sqrT, (int)sqrR, (int)sqrB ); // create the new square

				//set proper colour for changeable vs fixed squares
				if( usrSudokuArr.PuzzleOriginal[i][j] != 0 )
				{
					paint.setColor(Color.parseColor("#a2a2a2")); // set darker colour for fixed numbers
				}
				else
				{
					paint.setColor(Color.parseColor("#c2c2c2")); // set lighter colour for fixed numbers
				}

				///////////////
				//
				//	later add so that when user touches restricted square that cannot be changed, it wont be coloured
				//
				//////////////

				canvas.drawRect( rectArr[i][j], paint ); // draw square on canvas
			}
		}


			/** SET BUTTON LISTENERS **/

		btnArr = new Button[9]; // set buttons as an array
		btnArr[0] = (Button) findViewById(R.id.keypad_1);
		btnArr[1] = (Button) findViewById(R.id.keypad_2);
		btnArr[2] = (Button) findViewById(R.id.keypad_3);
		btnArr[3] = (Button) findViewById(R.id.keypad_4);
		btnArr[4] = (Button) findViewById(R.id.keypad_5);
		btnArr[5] = (Button) findViewById(R.id.keypad_6);
		btnArr[6] = (Button) findViewById(R.id.keypad_7);
		btnArr[7] = (Button) findViewById(R.id.keypad_8);
		btnArr[8] = (Button) findViewById(R.id.keypad_9);

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


		/* predefine variables to text overlay */

		paintblack.setColor(Color.parseColor("#0000ff"));
		paintblack.setTextSize(30);
		// re-adjust text to fit
		sqrLO = sqrLO + 9;
		sqrTO = sqrTO + 105/2 + 10;

		// create 2D array of text coordinates
		final PairF[][] puzzleLoc = new PairF[9][9];

		// initialize text overlay
		textOverlay = new RedrawText( txtL, txtT, sqrLO, sqrTO, puzzleLoc,
				usrSudokuArr, canvas, paintblack, wordArray, zoomOn );

		drawR = new drw( rectArr, paint, canvas, rectLayout, textOverlay, usrSudokuArr, zoomOn, touchXZclick, touchYZclick ); // class used to draw/update square matrix

		Log.d( "ERROR-2", "\nbefore call to ButtonListeners" );

		// call function to set all listeners
		listeners = new ButtonListener( currentRectColoured, usrSudokuArr, textOverlay, btnArr,
				drawR, touchX, touchY, lastRectColoured, usrLangPref );

		Log.d( "ERROR-2", "after call to ButtonListeners" );

		if( textOverlay == null )
		{
			Log.d( "NULL-2", "textOverlay initialized null" );
		}


		/////////////
		//
		//	dont forget to set zoomOn[0] to 0 when zooming out
		//
		///////////



		/* ZOOM BUTTON */ ///// ---------------------------------------------------------------------------------------------------------------------------------------------

		Button btnZoom =findViewById( R.id.button_zoom );
		btnZoom.setOnClickListener(new View.OnClickListener( )
			{
				@Override
				public void onClick( View v )
				{
					zoomOn[0] = 1; //set zoom as activated
					canvas.save( );
					canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //clear screen
					//canvas.translate(50, 50);
					canvas.scale( 2.0f, 2.0f );
					//canvas.drawRect( 0f, 0f, 10, 10, paint );
					//canvas.drawRect( 0.0f, 0.0f, 150.0f, 150.0f, paint );

					//reset scale corner coordinates
					sqrLO = (int) (BIT_MAP_W / 2 - 1005 / 2 );
					sqrTO = 40 ;

					//////////
					/** CREATE RECT MATRIX **/

					for( int i=0; i<9; i++ ) //row
					{
						for( int j=0; j<9; j++ ) //column
						{
							//increase square dimensions
							sqrL = sqrLO + j*(105+5);
							sqrT = sqrTO + i*(105+5);
							sqrR = sqrL + 105;
							sqrB = sqrT + 105;

							//add padding
							if( i>=3 ) //add extra space between rows
							{
								sqrT = sqrT + 15;
								sqrB = sqrB + 15;
							}
							if( i>=6 )
							{
								sqrT = sqrT + 15;
								sqrB = sqrB + 15;
							}

							if( j>=3 ) //add extra space between columns
							{
								sqrL = sqrL + 15;
								sqrR = sqrR + 15;
							}
							if( j>=6 )
							{
								sqrL = sqrL + 15;
								sqrR = sqrR + 15;
							}

							// overwrite array
							rectArr[i][j] = new Rect( (int)(sqrL), (int)sqrT, (int)sqrR, (int)sqrB ); // create the new square

							//set proper colour for changeable vs fixed squares
							if( usrSudokuArr.PuzzleOriginal[i][j] != 0 )
							{
								paint.setColor(Color.parseColor("#a2a2a2")); // set darker colour for fixed numbers
							}
							else
							{
								paint.setColor(Color.parseColor("#c2c2c2")); // set lighter colour for fixed numbers
							}

							///////////////
							//
							//	later add so that when user touches restricted square that cannot be changed, it wont be coloured
							//
							//////////////

							canvas.drawRect( rectArr[i][j], paint ); // draw square on canvas
						}
					}

					canvas.restore( );

					//////////

					// call function to redraw if user touch detected
					drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, true, usrLangPref );

					rectLayout.invalidate( );


					Log.d( "ZOOM", "--btnZoom clicked" );
				}
			}
		);


			/** DRAW TEXT BUTTONS **/

		// ON-TOUCH																	-----------------------------------------------------------------------------------------
		handleTouch = new View.OnTouchListener( )
		{
			@Override
			public boolean onTouch( View v, MotionEvent event )
			{
				touchX[0] = (int) event.getX( ); // touch coordinate on actual screen
				touchY[0] = (int) event.getY( );

				switch( event.getAction( ) )
				{
					case MotionEvent.ACTION_DOWN:
						//Log.i("TAG", "-- down");

						if( textOverlay == null )
						{
							Log.d( "NULL-2", "textOverlay null in onTouch in GameActivity" );
						}

						//save click down coordinate
						if( zoomOn[0] == 1 ) { // when zoom mode enabled
							//change "zoomed in" coordinate
							touchXZclick[0] = (int) (touchXZ[0] + touchX[0] / 2f);
							touchYZclick[0] = (int) (touchYZ[0] + touchY[0] / 2f);

							drawR.reDraw( touchXZclick, touchYZclick, lastRectColoured, currentRectColoured, true, usrLangPref );
							Log.d( "TAG", "click down: (" + touchXZclick[0] + ", " + touchYZclick[0] + ")" );
						} else {
							iX[0] = touchX[0]; //initial coordinate
							iY[0] = touchY[0];
							// call function to redraw if user touch detected
							drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, true, usrLangPref );
							Log.i("TAG", "normal click: (" + touchX[0] + ", " + touchY[0] + ")");
						}

						break;
					case MotionEvent.ACTION_MOVE:
						Log.i("TAG", "moving: (" + touchX[0] + ", " + touchY[0] + ")");

						drag = 1;

						//get on click up distance travelled
						dX[0] = ( touchX[0] - iX[0] ) / 2; // final - initial
						dY[0] = ( touchY[0] - iY[0] ) / 2; // divide by 2 to scale move to zoom

						Log.i("TAG", "moved: (" + dX[0] + ", " + dY[0] + ")");

						break;
					case MotionEvent.ACTION_UP:
						//Log.i("TAG", "-- up");

						//update where "zoomed in" coordinates landed
						if( zoomOn[0] == 1 )
						{
							if( drag == 1 ) {
								touchXZ[0] = touchXZ[0] + dX[0];
								touchYZ[0] = touchYZ[0] + dY[0];
							}
							//fix out of bounds
							if (touchXZ[0] > screenW){ touchXZ[0] = screenW; }
							else if (touchXZ[0] < 0){ touchXZ[0] = 0; }
							if (touchYZ[0] > screenH){ touchYZ[0] = screenH; }
							else if (touchYZ[0] < 0){ touchYZ[0] = 0; }

							drag = 0; //disable drag
							Log.d( "TAG", "click up: (" + touchXZ[0] + ", " + touchYZ[0] + ")" );
						}

						break;

						//////////////
						//
						//	deal with case when user keeps swiping in a direction out of bounds - limit touchXZ
						//
						/////////////////

				}

				return true;
			}
		};

		// initialize onTouchListener
		rectLayout.setOnTouchListener( handleTouch );



			/** CREATE TEXT PUZZLE OVERLAY **/

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

				if (usrSudokuArr.Puzzle[i][j]!=0) { // draw only if the original puzzle contains a number
					canvas.drawText(wordArray[usrSudokuArr.Puzzle[i][j]-1].getTranslation(), txtL, txtT, paintblack);
				}
			}
		}
	}

	/*
	//function that scales canvas view
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
	{
		@Override
		public boolean onScale( ScaleGestureDetector detector )
		{
			scale = scale * detector.getScaleFactor( );

			//set limit to scale
			scale = Math.max( 1f, Math.min( scale, 2f ) );
			matrix.setScale( scale, scale );
			imgView.setImageMatrix( matrix );

			return true;
		}
	}

	@Override
	public boolean onTouchEvent( MotionEvent event )
	{
		scaleGD.onTouchEvent( event );
		return true;
	}
*/


	@Override
	public void onStart( )
	{
		super.onStart( );

		//disable slide in animation
		overridePendingTransition(0, 0);
	}


}
