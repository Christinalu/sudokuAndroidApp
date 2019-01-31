package com.omicron.android.cmpt276_1191e1_omicron;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class GameActivity extends AppCompatActivity
{
	/*
		This Game Activity is the main window where the Sudoku Puzzle is going to be displayed
		It will be activated from the Main (Menu) Activity
	*/

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_game );


	}

}
