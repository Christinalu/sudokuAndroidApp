package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.TextView;

public class DictionaryActivity extends AppCompatActivity
{
	/*
	 *	This Class implements the Hint button, that is, the Dictionary
	*/

	public Word[] wordArray;
	public TextView tvToChange;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dictionary);

		//set intent to receive word array from Main Activity
		Intent wordArraySrc = getIntent( );
		if( wordArraySrc != null )
		{
			wordArray =  (Word[]) wordArraySrc.getSerializableExtra("wordArray");
		}

		//get display size (width, height in pixels)
		DisplayMetrics dMetric = new DisplayMetrics( );
		getWindowManager( ).getDefaultDisplay( ).getMetrics( dMetric );

		//store pixel dimensions
		int width = dMetric.widthPixels;
		int height = dMetric.heightPixels;

		getWindow( ).setLayout( (int) (width*.7), (int) (height*.5) );

		// UPDATE WORDS IN TABLE
		// Language 1
		tvToChange = (TextView) findViewById( R.id.textViewLeft1 );
		tvToChange.setText( wordArray[0].getNative( ) );
		tvToChange = (TextView) findViewById( R.id.textViewLeft2 );
		tvToChange.setText( wordArray[1].getNative( ) );
		tvToChange = (TextView) findViewById( R.id.textViewLeft3 );
		tvToChange.setText( wordArray[2].getNative( ) );
		tvToChange = (TextView) findViewById( R.id.textViewLeft4 );
		tvToChange.setText( wordArray[3].getNative( ) );
		tvToChange = (TextView) findViewById( R.id.textViewLeft5 );
		tvToChange.setText( wordArray[4].getNative( ) );
		tvToChange = (TextView) findViewById( R.id.textViewLeft6 );
		tvToChange.setText( wordArray[5].getNative( ) );
		tvToChange = (TextView) findViewById( R.id.textViewLeft7 );
		tvToChange.setText( wordArray[6].getNative( ) );
		tvToChange = (TextView) findViewById( R.id.textViewLeft8 );
		tvToChange.setText( wordArray[7].getNative( ) );
		tvToChange = (TextView) findViewById( R.id.textViewLeft9 );
		tvToChange.setText( wordArray[8].getNative( ) );

		// Language 2
		tvToChange = (TextView) findViewById( R.id.textViewRight1 );
		tvToChange.setText( wordArray[0].getTranslation( ) );
		tvToChange = (TextView) findViewById( R.id.textViewRight2 );
		tvToChange.setText( wordArray[1].getTranslation( ) );
		tvToChange = (TextView) findViewById( R.id.textViewRight3 );
		tvToChange.setText( wordArray[2].getTranslation( ) );
		tvToChange = (TextView) findViewById( R.id.textViewRight4 );
		tvToChange.setText( wordArray[3].getTranslation( ) );
		tvToChange = (TextView) findViewById( R.id.textViewRight5 );
		tvToChange.setText( wordArray[4].getTranslation( ) );
		tvToChange = (TextView) findViewById( R.id.textViewRight6 );
		tvToChange.setText( wordArray[5].getTranslation( ) );
		tvToChange = (TextView) findViewById( R.id.textViewRight7 );
		tvToChange.setText( wordArray[6].getTranslation( ) );
		tvToChange = (TextView) findViewById( R.id.textViewRight8 );
		tvToChange.setText( wordArray[7].getTranslation( ) );
		tvToChange = (TextView) findViewById( R.id.textViewRight9 );
		tvToChange.setText( wordArray[8].getTranslation( ) );
	}
}