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

	public Rect[][] rectArr = new Rect[9][9];


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

		Log.d( "TAG", "--screenH: " + screenH );
		Log.d( "TAG", "--screenW: " + screenW );

		bgMap = Bitmap.createBitmap(1080, 1000, Bitmap.Config.ARGB_8888);
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
		RelativeLayout rectLayout = (RelativeLayout) findViewById(R.id.rect_layout);
		rectLayout.addView(imgView);


		checkTouch = new CheckTouch( sqrLO, sqrTO );
		sqrColour = new ColourSqr( );

		final Rect rectangle = new Rect( 100, 100, 200, 200);
		canvas.drawRect( rectangle, paint );

		//create rect matrix-2
		


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


						if( rectangle.contains( x, y ) )
						{
							Toast.makeText(GameActivity.this, "RECT CLICKED", Toast.LENGTH_SHORT).show( );
						}


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






	}


	@Override
	public void onStart( )
	{
		super.onStart( );

		//disable slide in animation
		overridePendingTransition(0, 0);
	}
}
