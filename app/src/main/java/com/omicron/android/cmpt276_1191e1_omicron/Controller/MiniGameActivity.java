package com.omicron.android.cmpt276_1191e1_omicron.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.Toast;

import com.omicron.android.cmpt276_1191e1_omicron.Model.CardArray;
import com.omicron.android.cmpt276_1191e1_omicron.R;
import com.omicron.android.cmpt276_1191e1_omicron.WordArray;

public class MiniGameActivity extends AppCompatActivity
{
	private WordArray wordArray;
	private GridLayout gridLayout;
	private int gridColCount; //how many columns depending in screen orientation
	private int gridRowCount; //rows in card gridlayout based on word count
	CardArray cardStringArray; //stores the strings to be displayed for each card
	
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_mini_game );
		
		// TODO: add resume option
		// TODO: save data on 'back' btn press
		// TODO: implement saveInstanceState for state saving when rotating
		// TODO: dont include SudokuArray in intent
		// TODO: allow constant word card count ie always 9 words == 18 cards
		// TODO: when initializing word array, make it to always initialize 9 words
		// TODO: make sure to also save on rotation in MainActivity
		// TODO: once game finished, make sure it stays finished even when resuming
		// TODO: also do unit tests for Card and CardArray
		// TODO: remove Card object if not necassary
		
		Intent intentSrc = getIntent( );
		if( intentSrc == null )
		{ return; }
		
		wordArray = (WordArray) intentSrc.getParcelableExtra("wordArray");
		
		int orientation = getResources().getConfiguration().orientation;
		if( orientation == Configuration.ORIENTATION_LANDSCAPE ){ //based on orientation, create grid (column) size
			gridColCount = 3; //allow 3 words per row in landscape
			gridRowCount = 6;
		} else {
			gridColCount = 2;
			gridRowCount = 9;
		}
		
		cardStringArray = new CardArray( gridRowCount, gridColCount );
		cardStringArray.initializeStringArray( wordArray );
		
//		int res = cardStringArray.initializeStringArray( wordArray );
//		if( res != 0 ){ //initialization failed
//			Toast.makeText( this, "Unable to start game", Toast.LENGTH_LONG ).show( );
//			return;
//		}
		
		gridLayout = new GridLayout( this ); //layout to store the cards
		viewCardArraySetUp( gridLayout );
		
		
		
		
		
		
		
		
	}
	
	
	private void viewCardArraySetUp( GridLayout gridLayout )
	{
		/*
		 * This function sets up the card array View to display the card words
		 */
		
		
		
		gridLayout.setColumnCount( gridColCount );
		gridLayout.setRowCount( gridRowCount );
	}
}






































