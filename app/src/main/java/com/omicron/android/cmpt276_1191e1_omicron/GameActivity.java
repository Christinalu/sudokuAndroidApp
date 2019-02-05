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
	public Rect sqr = new Rect( 30, 30, 30, 30 );
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

												 startActivity(activityDictionary); //switch to dictionary window
											 }
										 }
		);



		// SQUARE TEST CODE

		// get display metrics
		DisplayMetrics displayMetrics = new DisplayMetrics( );
		getWindowManager( ).getDefaultDisplay( ).getMetrics( displayMetrics );
		int screenH = displayMetrics.heightPixels;
		int screenW = displayMetrics.widthPixels;

		bgMap = Bitmap.createBitmap(screenW, screenH, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(bgMap);

		//original coordinates of where to draw square
		sqrLO = (float) (screenW / 2.0 - 1005 / 2.0 );
		sqrTO = 50;
		float sqrRO = screenW / 2 - 1005 / 2 + 105;
		float sqrBO = 155;

		//duplicate square coordinates
		float sqrL;
		float sqrT;
		float sqrR;
		float sqrB;

		imgView = new ImageView(this);
		paint.setColor(Color.parseColor("#c2c2c2"));

		//draw squares
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
				canvas.drawRect( sqrL, sqrT, sqrR, sqrB, paint );
			}
		}

		imgView.setImageBitmap(bgMap);
		RelativeLayout rectLayout = (RelativeLayout) findViewById(R.id.rect_layout);
		rectLayout.addView(imgView);


		checkTouch = new CheckTouch( sqrLO, sqrTO );
		sqrColour = new ColourSqr( );

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
						Log.i("TAG", "-- down");

						coordinatePair = checkTouch.check( x, y );


						Log.d( "TAG", "-- row: " + coordinatePair.getRow() + " col: " + coordinatePair.getColumn() );

						if( checkTouch.touch( ) == true )
						{
							sqrSelected.update( coordinatePair.getRow(), coordinatePair.getColumn() ); //update which square is selected


							paint.setColor(Color.parseColor("#ff0000"));
							//canvas.drawRect(300, 300, 500, 500, paint);
							sqrColour.colour( coordinatePair, sqrSelected, sqrLastSelected, sqrLastActive, lastPair, canvas, paint, sqrLO, sqrTO );
							imgView.setImageBitmap(bgMap);

							sqrLastSelected.update( sqrSelected.getRow(), sqrSelected.getColumn() );
							//save copy of last active square
							if( sqrSelected.getRow() != -1 )
							{
								sqrLastActive.update( coordinatePair.getRow(), coordinatePair.getColumn() );
							}

						}
						else //colour back to grey
						{
							paint.setColor(Color.parseColor("#c2c2c2"));
							sqrColour.colour( coordinatePair, sqrSelected, sqrLastSelected, sqrLastActive, lastPair, canvas, paint, sqrLO, sqrTO );
							imgView.setImageBitmap(bgMap);
						}
						lastPair.update( coordinatePair.getRow(), coordinatePair.getColumn() ); //last coordinate pair touched

						break;
					case MotionEvent.ACTION_MOVE:
						Log.i("TAG", "moving: (" + x + ", " + y + ")");
						break;
					case MotionEvent.ACTION_UP:
						Log.i("TAG", "-- up");
						break;
				}

				return true;
			}
		};



		rectLayout.setOnTouchListener( handleTouch );



		/*  ON-CLICK

		rectLayout.setOnClickListener( new View.OnClickListener( )
			{
				@Override
				public void onClick( View view )
				{
					//toast message.
					Toast.makeText(GameActivity.this, "You clicked the Linear Layout", Toast.LENGTH_SHORT).show( );

					if( true )
					{
						paint.setColor(Color.parseColor("#ff0000"));
						canvas.drawRect(300, 300, 500, 500, paint);
						imgView.setImageBitmap(bgMap);
					}
				}


			}
		);
		*/


	}


	@Override
	public void onStart( )
	{
		super.onStart( );

		//disable slide in animation
		overridePendingTransition(0, 0);
	}
}
