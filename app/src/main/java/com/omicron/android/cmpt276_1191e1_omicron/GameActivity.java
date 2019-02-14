package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

	private float sqrLO; // original left coordinate of where puzzle starts
	private float sqrTO; // original top coordinate of where puzzle starts
	private float txtL; // text coordinates
	private float txtT;

	private drw drawR; // class that draws the squares either highlighted or not, based on touch
	private Pair lastRectColoured = new Pair( -1, -1 ); // stores the last coloured square coordinates
	private Pair currentRectColoured = new Pair( -1, -1 ); // stores the current coloured square

	private Rect[][] rectArr = new Rect[9][9]; // stores all squares in a 2D array
	private Paint paintblack = new Paint();
	private RedrawText textOverlay; // class used to redraw GUI text overlay

	private int[] touchX = { 0 }; // hold user touch (x,y) coordinate
	private int[] touchY = { 0 };

	private Button [] btnArr;
	private ButtonListener listeners; // used to call another function to implement all button listeners, to save space in GameActivity


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
        SudokuGenerator usrSudokuArr = new SudokuGenerator( usrDiffPref );

		//create dictionary button
		Button btnDictionary = (Button) findViewById(R.id.button_dictionary);

		btnDictionary.setOnClickListener(new View.OnClickListener() { // important, SudokuGenerator usrSudokuArr must be initialized before this
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
		int screenH = displayMetrics.heightPixels;
		int screenW = displayMetrics.widthPixels;

		Log.d( "TAG", "--screenH: " + screenH );
		Log.d( "TAG", "--screenW: " + screenW );

		// create canvas and bitmap
		bgMap = Bitmap.createBitmap(1080, 1500, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bgMap);

		//original coordinates of where to draw square
		sqrLO = (float) (screenW / 2.0 - 1005 / 2.0 );
		sqrTO = 50 ;

		Log.d( "TAG", "--sqrLO: " + sqrLO );
		Log.d( "TAG", "--sqrTO: " + sqrTO );

		//duplicate square coordinates
		float sqrL;
		float sqrT;
		float sqrR;
		float sqrB;

		imgView = new ImageView(this);
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
				usrSudokuArr, canvas, paintblack, wordArray );

		drawR = new drw( rectArr, paint, canvas, rectLayout, textOverlay, usrSudokuArr ); // class used to draw/update square matrix

		Log.d( "ERROR-2", "\nbefore call to ButtonListeners" );

		// call function to set all listeners
		listeners = new ButtonListener( currentRectColoured, usrSudokuArr, textOverlay, btnArr,
				drawR, touchX, touchY, lastRectColoured, usrLangPref );

		Log.d( "ERROR-2", "after call to ButtonListeners" );

		if( textOverlay == null )
		{
			Log.d( "NULL-2", "textOverlay initialized null" );
		}



			/** DRAW TEXT BUTTONS **/

		// ON-TOUCH
		handleTouch = new View.OnTouchListener( )
		{
			@Override
			public boolean onTouch( View v, MotionEvent event )
			{
				touchX[0] = (int) event.getX( );
				touchY[0] = (int) event.getY( );

				switch( event.getAction( ) )
				{
					case MotionEvent.ACTION_DOWN:
						//Log.i("TAG", "-- down");

						if( textOverlay == null )
						{
							Log.d( "NULL-2", "textOverlay null in onTouch in GameActivity" );
						}

						// call function to redraw if user touch detected
						drawR.reDraw( touchX, touchY, lastRectColoured, currentRectColoured, true, usrLangPref );

						break;
					case MotionEvent.ACTION_MOVE:
						//Log.i("TAG", "moving: (" + x + ", " + y + ")");
						break;
					case MotionEvent.ACTION_UP:
						//Log.i("TAG", "-- up");
						break;
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


	@Override
	public void onStart( )
	{
		super.onStart( );

		//disable slide in animation
		overridePendingTransition(0, 0);
	}
}
