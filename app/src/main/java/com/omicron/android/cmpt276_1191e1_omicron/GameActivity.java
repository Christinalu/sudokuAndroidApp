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

	public Paint paint = new Paint( );
	public Bitmap bgMap;
	public Canvas canvas;
	public ImageView imgView;
	public RelativeLayout rectLayout;
	public View.OnTouchListener handleTouch;

	public float sqrLO; // original left coordinate of where puzzle starts
	public float sqrTO; // original top coordinate of where puzzle starts

	public drw drawR; // class that draws the squares either highlighted or not, based on touch
	public Pair lastRectColoured = new Pair( -1, -1 ); // stores the last coloured square coordinates
	public Pair currentRectColoured = new Pair( -1, -1 ); // stores the current coloured square

	public Rect[][] rectArr = new Rect[9][9]; // stores all squares in a 2D array
	public Paint paintblack = new Paint();

	// an unique puzzle template input

	///////////
	//
	//	replace with actual array once finished - this array is experimental (may not be unique)
	//
	////////////

	public int [][] testArr = {	{6,7,3,0,0,0,2,0,8}, {4,0,2,0,7,3,0,0,1}, {0,0,5,6,0,8,4,3,7},
								{8,0,9,0,3,7,5,0,6}, {3,4,0,2,6,0,0,7,0}, {0,6,7,8,0,9,1,0,3},
								{7,5,6,0,1,2,3,0,4}, {1,0,8,7,0,0,9,6,0}, {2,0,4,3,8,6,0,1,5}};

	public SudokuGenerator usrSudokuArr = new SudokuGenerator( testArr ); // stores the generated puzzle, including arrays of solution and user current puzzle
	public ButtonListener listeners; // used to call another function to implement all button listeners, to save space in GameActivity


	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		//set intent to receive word array from Main Activity
		Intent wordArraySrc = getIntent( );
		if (wordArraySrc != null) {
			wordArray = (Word[]) wordArraySrc.getSerializableExtra("wordArray");
		}

		//create dictionary button
		Button btnDictionary = (Button) findViewById(R.id.button_dictionary);

		btnDictionary.setOnClickListener(new View.OnClickListener() {
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
		drawR = new drw(  ); // class used to draw/update square matrix

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
				canvas.drawRect( rectArr[i][j], paint ); // draw square on canvas
			}
		}


			/** SET BUTTON LISTENERS **/

		Button btn1 = (Button) findViewById(R.id.keypad_1);
		Button btn2 = (Button) findViewById(R.id.keypad_2);
		Button btn3 = (Button) findViewById(R.id.keypad_3);
		Button btn4 = (Button) findViewById(R.id.keypad_4);
		Button btn5 = (Button) findViewById(R.id.keypad_5);
		Button btn6 = (Button) findViewById(R.id.keypad_6);
		Button btn7 = (Button) findViewById(R.id.keypad_7);
		Button btn8 = (Button) findViewById(R.id.keypad_8);
		Button btn9 = (Button) findViewById(R.id.keypad_9);

		// call function to set all listeners
		listeners = new ButtonListener( currentRectColoured, usrSudokuArr, btn1, btn2, btn3,
				btn4, btn5, btn6, btn7, btn8, btn9 );



			/** DRAW TEXT BUTTONS **/

		// ON-TOUCH
		handleTouch = new View.OnTouchListener( )
		{
			@Override
			public boolean onTouch( View v, MotionEvent event )
			{
				int x = (int) event.getX( );
				int y = (int) event.getY( );

				switch( event.getAction( ) )
				{
					case MotionEvent.ACTION_DOWN:
						//Log.i("TAG", "-- down");

						// call function to redraw if user touch detected
						drawR.reDraw( rectArr, x, y, paint, canvas, rectLayout, lastRectColoured, currentRectColoured );

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

		paintblack.setColor(Color.parseColor("#0000ff"));
		paintblack.setTextSize(30);

		// re-adjust text to fit
		sqrLO = sqrLO + 9;
		sqrTO = sqrTO + 105/2 + 10;

		float txtL; // text coordinates
		float txtT;

		// create 2D array of text coordinates
		PairF[][] puzzleLoc = new PairF[9][9];


		/////// !!!!!!!! //////////
		/////////////


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
