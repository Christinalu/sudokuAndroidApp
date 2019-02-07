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
	private int usrDiffPref;

	public Paint paint = new Paint( );
	public Bitmap bgMap;
	public Canvas canvas;
	public ImageView imgView;
	public RelativeLayout rectLayout;
	public View.OnTouchListener handleTouch;

	public float sqrLO; // original left coordinate of where puzzle starts
	public float sqrTO; // original top coordinate of where puzzle starts
	public float txtL; // text coordinates
	public float txtT;

	public drw drawR; // class that draws the squares either highlighted or not, based on touch
	public Pair lastRectColoured = new Pair( -1, -1 ); // stores the last coloured square coordinates
	public Pair currentRectColoured = new Pair( -1, -1 ); // stores the current coloured square

	public Rect[][] rectArr = new Rect[9][9]; // stores all squares in a 2D array
	public Paint paintblack = new Paint();
	public RedrawText textOverlay; // class used to redraw GUI text overlay

	private int[] touchX = { 0 };
	private int[] touchY = { 0 };

	// an unique puzzle template input

	///////////
	//
	//	replace with actual array once finished - this array is experimental (may not be unique)
	//
	////////////

	public int [][] seedArr = new int[9][9];

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
			usrLangPref =  (int) wordArraySrc.getSerializableExtra("usrLangPref");
		}

		//set intent to receive Puzzle array from Main Activity
		Intent usrDiffPrefSrc = getIntent( );
		if (usrDiffPrefSrc != null) {
			usrDiffPref =  (int) usrDiffPrefSrc.getSerializableExtra("usrDiffPref");
		}
        if (usrDiffPref==0) {
            seedArr[0][0] = 6;
            seedArr[0][1] = 7;
            seedArr[0][2] = 3;
            seedArr[0][6] = 2;
            seedArr[0][8] = 9;

            seedArr[1][0] = 4;
            seedArr[1][2] = 2;
            seedArr[1][4] = 7;
            seedArr[1][5] = 3;
            seedArr[1][8] = 1;

            seedArr[2][2] = 5;
            seedArr[2][3] = 6;
            seedArr[2][5] = 8;
            seedArr[2][6] = 4;
            seedArr[2][7] = 3;
            seedArr[2][8] = 7;

            seedArr[3][0] = 8;
            seedArr[3][2] = 9;
            seedArr[3][4] = 3;
            seedArr[3][5] = 7;
            seedArr[3][6] = 5;
            seedArr[3][8] = 6;

            seedArr[4][0] = 3;
            seedArr[4][1] = 4;
            seedArr[4][3] = 2;
            seedArr[4][4] = 6;
            seedArr[4][7] = 7;

            seedArr[5][1] = 6;
            seedArr[5][2] = 7;
            seedArr[5][3] = 8;
            seedArr[5][5] = 9;
            seedArr[5][6] = 1;
            seedArr[5][8] = 3;

            seedArr[6][0] = 7;
            seedArr[6][1] = 5;
            seedArr[6][2] = 6;
            seedArr[6][4] = 1;
            seedArr[6][5] = 2;
            seedArr[6][6] = 3;
            seedArr[6][8] = 4;

            seedArr[7][0] = 1;
            seedArr[7][2] = 8;
            seedArr[7][3] = 7;
            seedArr[7][6] = 9;
            seedArr[7][7] = 6;

            seedArr[8][0] = 2;
            seedArr[8][2] = 4;
            seedArr[8][3] = 3;
            seedArr[8][4] = 8;
            seedArr[8][5] = 6;
            seedArr[8][7] = 1;
            seedArr[8][8] = 5;
        }
        if (usrDiffPref==1) {
            seedArr[0][0] = 1;
            seedArr[0][4] = 9;
            seedArr[0][7] = 2;
            seedArr[0][8] = 5;

            seedArr[1][0] = 7;
            seedArr[1][1] = 3;
            seedArr[1][4] = 6;
            seedArr[1][7] = 9;

            seedArr[2][5] = 3;
            seedArr[2][6] = 6;

            seedArr[3][0] = 3;
            seedArr[3][2] = 6;
            seedArr[3][3] = 5;
            seedArr[3][4] = 4;

            seedArr[4][1] = 2;
            seedArr[4][7] = 4;

            seedArr[5][4] = 8;
            seedArr[5][5] = 6;
            seedArr[5][6] = 7;
            seedArr[5][8] = 3;

            seedArr[6][2] = 7;
            seedArr[6][3] = 3;

            seedArr[7][1] = 9;
            seedArr[7][4] = 7;
            seedArr[7][7] = 8;
            seedArr[7][8] = 2;

            seedArr[8][0] = 6;
            seedArr[8][1] = 8;
            seedArr[8][4] = 1;
            seedArr[8][8] = 7;
        }
        if (usrDiffPref==2) {
            seedArr[0][8] = 8;

            seedArr[1][0] = 6;
            seedArr[1][1] = 4;
            seedArr[1][2] = 5;
            seedArr[1][4] = 2;

            seedArr[2][3] = 9;
            seedArr[2][5] = 7;
            seedArr[2][6] = 6;
            seedArr[2][7] = 4;

            seedArr[3][0] = 2;
            seedArr[3][6] = 1;
            seedArr[3][7] = 7;

            seedArr[4][0] = 5;
            seedArr[4][4] = 4;
            seedArr[4][8] = 6;

            seedArr[5][1] = 1;
            seedArr[5][2] = 3;
            seedArr[5][8] = 5;

            seedArr[6][1] = 2;
            seedArr[6][2] = 9;
            seedArr[6][3] = 5;
            seedArr[6][5] = 6;

            seedArr[7][4] = 1;
            seedArr[7][6] = 5;
            seedArr[7][7] = 9;
            seedArr[7][8] = 4;

            seedArr[8][0] = 7;
        }

        // stores the generated puzzle, including arrays of solution and user current puzzle
        SudokuGenerator usrSudokuArr = new SudokuGenerator( seedArr );

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

		Button btn1 = (Button) findViewById(R.id.keypad_1);
		Button btn2 = (Button) findViewById(R.id.keypad_2);
		Button btn3 = (Button) findViewById(R.id.keypad_3);
		Button btn4 = (Button) findViewById(R.id.keypad_4);
		Button btn5 = (Button) findViewById(R.id.keypad_5);
		Button btn6 = (Button) findViewById(R.id.keypad_6);
		Button btn7 = (Button) findViewById(R.id.keypad_7);
		Button btn8 = (Button) findViewById(R.id.keypad_8);
		Button btn9 = (Button) findViewById(R.id.keypad_9);

		// choose button language based on user preference
		if( usrLangPref == 0 )
		{
			btn1.setText( "ONE" );
			btn2.setText( "TWO" );
			btn3.setText( "THREE" );
			btn4.setText( "FOUR" );
			btn5.setText( "FIVE" );
			btn6.setText( "SIX" );
			btn7.setText( "SEVEN" );
			btn8.setText( "EIGHT" );
			btn9.setText( "NINE" );
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
		listeners = new ButtonListener( currentRectColoured, usrSudokuArr, textOverlay, btn1, btn2, btn3,
				btn4, btn5, btn6, btn7, btn8, btn9, drawR, touchX, touchY, lastRectColoured, usrLangPref );

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
