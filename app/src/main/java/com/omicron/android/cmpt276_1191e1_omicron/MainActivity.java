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
    /*
     *  This Main Activity is the activity that will act as the Start Menu
     */

    RadioGroup Difficulty;
    RadioGroup Language;
    private int usrLangPref = 0; // 0=eng_fr, 1=fr_eng; 0 == native(squares that cannot be modified); 1 == translation(the words that the user inserts)
    private int usrDiffPref; //0=easy,1=medium,2=difficult


	// SET UP ARRAY TO STORE WORDS
    // a Word (pair) contains the word in native language, and its translation
    // note: this array will remain of size 9, and only changed when modes are switched
    //       this is required in DictionaryActivity.java
    private Word[] wordArray =new Word[]
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


        	// CHOOSE THE LEVEL OF DIFFICULTY
        Difficulty = findViewById(R.id.button_level);
        Difficulty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int difficultyId = group.getCheckedRadioButtonId();
                switch(difficultyId)
				{
                    case R.id.button_easy:
                        usrDiffPref = 0;
                        break;

                    case R.id.button_medium:
                        usrDiffPref = 1;
                        break;

                    case R.id.button_hard:
                        usrDiffPref = 2;
                        break;
                }
            }
        });


        	// CHOOSE THE LANGUAGE
        Language=findViewById(R.id.button_language);
        Language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int LanguageId = group.getCheckedRadioButtonId();
                switch (LanguageId)
				{
                    case R.id.button_eng_fr:
                    	usrLangPref = 0;
                        break;

                    case R.id.button_fr_eng:
                    	usrLangPref = 1;
                        break;
                }
            }
        });

        // start game button; used to switch to gameActivity
        Button btnStart = (Button) findViewById( R.id.button_start );
        btnStart.setOnClickListener( new View.OnClickListener(  )
            {
                @Override
                public void onClick( View v )
                {
                	Intent gameActivity = new Intent( MainActivity.this, GameActivity.class );

                	//save wordArray for Game Activity
					gameActivity.putExtra( "wordArray", wordArray );
                    gameActivity.putExtra( "usrLangPref", usrLangPref );
                    gameActivity.putExtra("usrDiffPref",usrDiffPref);

                    startActivity( gameActivity );
                }
            }
        );
    }
}
