package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class MainActivity extends AppCompatActivity
{
    RadioGroup Difficulty;
    RadioButton btnDifficulty;

    RadioGroup Language;
    RadioButton btnLanguage;

    /*
     *  This Main Activity is the activity that will act as the Start Menu
    */

    // SET UP ARRAY TO STORE WORDS
    // a Word (pair) contains the word in native language, and its translation
    // note: this array will remain of size 9, and only changed when modes are switched
    //       this is required in DictionaryActivity.java
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

        //choose the level of difficulty
        Difficulty = findViewById(R.id.button_level);
        int difficultyId=Difficulty.getCheckedRadioButtonId();
        btnDifficulty=findViewById(difficultyId);

        //choose the language
        Language=findViewById(R.id.button_language);
        int LanguageId=Language.getCheckedRadioButtonId();
        btnLanguage=findViewById(LanguageId);


        // start game button; used to switch to gameActivity
        Button btnStart = (Button) findViewById( R.id.button_start );

        // set listener to switch to Game Activity
        btnStart.setOnClickListener( new View.OnClickListener(  )
            {
                @Override
                public void onClick( View v )
                {
                   // int levelId=Difficulty.getCheckedRadioButtonId();
                    //btnDifficulty=findViewById(levelId);
                	Intent gameActivity = new Intent( MainActivity.this, GameActivity.class );

                	//save wordArray for Game Activity
					gameActivity.putExtra( "wordArray", wordArray );

                    startActivity( gameActivity );
                }
            }
        );

    }

}
