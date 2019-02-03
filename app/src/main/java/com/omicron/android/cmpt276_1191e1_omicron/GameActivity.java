package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class GameActivity extends AppCompatActivity
{
	/*
		This Game Activity is the main window where the Sudoku Puzzle is going to be displayed
		It will be activated from the Main (Menu) Activity
	*/

	public Word[] wordArray;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_game );

		//set intent to receive word array from Main Activity
		Intent wordArraySrc = getIntent( );
		if( wordArraySrc != null )
		{
			wordArray =  (Word[]) wordArraySrc.getSerializableExtra("wordArray");
		}

		//create dictionary button
		Button btnDictionary = (Button) findViewById( R.id.button_dictionary );

		btnDictionary.setOnClickListener( new View.OnClickListener( )
				{
					@Override
					public void onClick( View v )
					{
						//create activity window for the dictionary
						Intent activityDictionary = new Intent( GameActivity.this, DictionaryActivity.class );

						//save wordArray for Dictionary Activity
						activityDictionary.putExtra( "wordArray", wordArray );

						startActivity( activityDictionary ); //switch to dictionary window
					}
				}
		);

	}

	@Override
	public void onStart() {
		super.onStart();

		//disable slide in animation
		overridePendingTransition(0, 0);
	}
}
