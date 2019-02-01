package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity
{
    /*
     *  This Main Activity is the activity that will act as the Start Menu
    */

    // SET UP ARRAY TO STORE WORDS
    // a Word (pair) contains the word in native language, and its translation
    private Word[] wordArray = new Word[]
            {
                    new Word( "One", "Un" ),
                    new Word( "Two", "Deux" ),
                    new Word( "Three", "Trois" ),
                    new Word( "Four", "Quatre" ),
                    new Word( "Five", "Cinq" ),
                    new Word( "Six", "Six" ),
                    new Word( "Seven", "Sept" ),
                    new Word( "Eight", "Huit" ),
                    new Word( "Nine", "Neuf" )
            };



    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // start game button; used to switch to gameActivity
        Button btn1 = (Button) findViewById( R.id.button_start );

        // set listener to switch to Game Activity
        btn1.setOnClickListener( new View.OnClickListener(  )
            {
                @Override
                public void onClick( View v )
                {
                	Intent gameActivity = new Intent( MainActivity.this, GameActivity.class );

                	//save wordArray for Game Activity
					gameActivity.putExtra( "wordArray", wordArray );

                    startActivity( gameActivity );
                }
            }
        );

    }
}
