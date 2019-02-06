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

	//variables for square listeners
	//public Rect sqr = new Rect( 30, 30, 30, 30 );
	public Paint paint = new Paint( );
	public Bitmap bgMap;
	public Canvas canvas;
	public ImageView imgView;
	public RelativeLayout rectLayout;
	public View.OnTouchListener handleTouch;
	public CheckTouch checkTouch;
	public Pair coordinatePair;
	public Pair sqrSelected = new Pair( -1, -1 ); //contains coordinates of selected square
	public Pair sqrLastSelected = new Pair( -1, -1 ); //used to solve issue when clicking multiple squares colored multiple squares
	public Pair sqrLastActive = new Pair( -1, -1 );
	public Pair lastPair = new Pair( -1, -1 );
	public ColourSqr sqrColour;
	public float sqrLO;
	public float sqrTO;
	public ButtonLayout buttonMatrix;

	public float txtLO;
	public float txtTO;
	public float txtRO;
	public float txtBO;

	public drw drawR;
	public Pair lastRectColoured = new Pair( -1, -1 ); //stores the last coloured square
	public Pair currentRectColoured = new Pair( -1, -1 ); //stores the current coloured square

	public Rect[][] rectArr = new Rect[9][9];
	public Rect[][] textArr = new Rect[3][3];

	//int array storing unput so far
	public int [][] testArr = {{6,7,3,0,0,0,2,0,8},{4,0,2,0,7,3,0,0,1},{0,0,5,6,0,8,4,3,7},{8,0,9,0,3,7,5,0,6},{3,4,0,2,6,0,0,7,0},{0,6,7,8,0,9,1,0,3},{7,5,6,0,1,2,3,0,4},{1,0,8,7,0,0,9,6,0},{2,0,4,3,8,6,0,1,5}};

	public SudokuGenerator usrSudokuArr = new SudokuGenerator( testArr ); //stores the generated puzzle
	public ButtonListener listeners;


	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		//set intent to receive word array from Main Activity
		Intent wordArraySrc = getIntent();
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

												 //canvas.drawRect( 100, 100, 500, 500, paint );

												 startActivity(activityDictionary); //switch to dictionary window
											 }
										 }
		);

		////// test/////

		/*Button btnTest = (Button) findViewById(R.id.button_test);

		btnTest.setOnClickListener(new View.OnClickListener() {
											 @Override
											 public void onClick(View v) {
												 //create activity window for the dictionary
												 //Intent activityDictionary = new Intent(GameActivity.this, DictionaryActivity.class);

												 //save wordArray for Dictionary Activity
												 //activityDictionary.putExtra("wordArray", wordArray);

												 canvas.drawRect( 100, 100, 500, 500, paint );

												 //startActivity(activityDictionary); //switch to dictionary window
											 }
										 }
		);*/


		/////////////



		// SQUARE TEST CODE

		// get display metrics
		DisplayMetrics displayMetrics = new DisplayMetrics( );
		getWindowManager( ).getDefaultDisplay( ).getMetrics( displayMetrics );
		int screenH = displayMetrics.heightPixels;
		int screenW = displayMetrics.widthPixels;

		Log.d( "TAG", "--screenH: " + screenH );
		Log.d( "TAG", "--screenW: " + screenW );

		bgMap = Bitmap.createBitmap(1080, 1500, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bgMap);

		//original coordinates of where to draw square
		sqrLO = (float) (screenW / 2.0 - 1005 / 2.0 );
		sqrTO = 50 ;
		float sqrRO = screenW / 2 - 1005 / 2 + 105;
		float sqrBO = 155;

		Log.d( "TAG", "--sqrLO: " + sqrLO );
		Log.d( "TAG", "--sqrTO: " + sqrTO );

		//duplicate square coordinates
		float sqrL;
		float sqrT;
		float sqrR;
		float sqrB;

		imgView = new ImageView(this);
		paint.setColor(Color.parseColor("#c2c2c2"));



		imgView.setImageBitmap(bgMap);
		rectLayout = (RelativeLayout) findViewById(R.id.rect_layout);
		rectLayout.addView(imgView);

		drawR = new drw(  );


		checkTouch = new CheckTouch( sqrLO, sqrTO );
		sqrColour = new ColourSqr( );


		final Rect rectangle = new Rect( 100, 100, 200, 200);
		//canvas.drawRect( rectangle, paint );

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
				rectArr[i][j] = new Rect( (int)(sqrL), (int)sqrT, (int)sqrR, (int)sqrB );

				canvas.drawRect( rectArr[i][j], paint );
			}
		}
		final Paint paint2 = new Paint();


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


		listeners = new ButtonListener( currentRectColoured, usrSudokuArr, btn1, btn2, btn3,
				btn4, btn5, btn6, btn7, btn8, btn9 );



		//buttonMatrix.create( GameActivity, btn1  );




		/** DRAW TEXT BUTTONS **/

		/*txtLO = (float) (screenW / 2.0 - 900 / 2.0 );
		txtTO = 50;
		float txtRO = screenW / 2 - 900 / 2 + 300;
		float txtBO = 155;

		float txtL;
		float txtT;
		float txtR;
		float txtB;

		for( int i=0; i<3; i++ )
		{
			for( int j=0; j<3; j++ )
			{
				//increase square dimensions
				txtL = txtLO + j*(300+30);
				txtT = sqrTO + i*(70+30);
				txtR = txtL + 300;
				txtB = txtT + 300;


				txtArr[i][j] = new Rect( (int)(txtL), (int)txtT, (int)txtR, (int)txtB );

				canvas.drawRect( rectArr[i][j], paint );
			}
		}*/
		

		//canvas.drawRect( 100, 100, 500, 500, paint );

		//drw = new draw( GameActivity.this );

		//setContentView( drawR );

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


		//rectLayout.
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
