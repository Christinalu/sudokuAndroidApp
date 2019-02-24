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
    private int usrLangPref = 0; // 0=eng_fr, 1=fr_eng
    private int usrDiffPref; //0=easy,1=medium,2=difficult
    private int state; //0=new start, 1=resume

    /*
     *  This Main Activity is the activity that will act as the Start Menu
    */

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

        //choose the level of difficulty
        Difficulty = findViewById(R.id.button_level);
        Difficulty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int difficultyId = group.getCheckedRadioButtonId();
                switch(difficultyId) {
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

        //choose the language
        Language=findViewById(R.id.button_language);
        Language.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int LanguageId = group.getCheckedRadioButtonId();
                switch (LanguageId) {
                    case R.id.button_eng_fr:
                        // Your code
                         wordArray = new Word[]
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
                                usrLangPref = 0;
                        break;
                    case R.id.button_fr_eng:
                        // Your code
                         wordArray = new Word[]
                                {
                                        new Word( "Un", "One" ),
                                        new Word( "Deux", "Two" ),
                                        new Word( "Trois", "Three" ),
                                        new Word( "Quatre", "Four" ),
                                        new Word( "Cinq", "Five" ),
                                        new Word( "Six", "Six" ),
                                        new Word( "Sept", "Seven" ),
                                        new Word( "Huit", "Eight" ),
                                        new Word( "Neuf", "Nine" )
                                };
                                usrLangPref = 1;
                        break;
                    default:
                        wordArray = new Word[]
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
                                usrLangPref = 1;
                        break;
                }
            }
        });

        // start and resume game button; used to switch to gameActivity
        Button btnStart = (Button) findViewById( R.id.button_start );
        Button btnResume = (Button) findViewById(R.id.button_resume);
        btnResume.setEnabled(false); //block Resume button unless a previous game is saved

        // set listener to switch to Game Activity
        btnStart.setOnClickListener( new View.OnClickListener(  )
            {
                @Override
                public void onClick( View v )
                {

                	Intent gameActivity = new Intent( MainActivity.this, GameActivity.class );
                	state = 0;

                	//save wordArray for Game Activity

					gameActivity.putExtra( "wordArray", wordArray );
                    gameActivity.putExtra( "usrLangPref", usrLangPref );
                    gameActivity.putExtra("usrDiffPref",usrDiffPref);
                    gameActivity.putExtra("state", state);
                    finish();
                    startActivity( gameActivity );
                }
            }
        );
        //check if a previous game existed. If it did, unblock resume button
        final Intent resumeSrc = getIntent( );
        if (resumeSrc != null) {
            //if all necessary game preferences are written in memory, then unblock resume button
            if (resumeSrc.hasExtra("wordArrayGA") && resumeSrc.hasExtra("usrLangPrefGA") && resumeSrc.hasExtra("SudokuArrGA") && resumeSrc.hasExtra("state")) {
                btnResume.setEnabled(true);
            }
        }
        //if resume button is unblocked and pressed, it will load previous game preferences
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load previous game preferences to prepare for export to new Game Activity
                Word[] wordArrayResume = (Word[]) resumeSrc.getSerializableExtra("wordArrayGA");
                int usrLangPrefResume = (int) resumeSrc.getSerializableExtra("usrLangPrefGA");
                SudokuGenerator usrSudokuArrResume = (SudokuGenerator) resumeSrc.getSerializableExtra("SudokuArrGA");
                state = 1;

                Intent resumeActivity = new Intent( MainActivity.this, GameActivity.class );
                //save preferences for Game Activity to read
                resumeActivity.putExtra( "wordArrayMA", wordArrayResume );
                resumeActivity.putExtra( "usrLangPrefMA", usrLangPrefResume );
                resumeActivity.putExtra("SudokuArrMA", usrSudokuArrResume);
                resumeActivity.putExtra("state", state);
                finish();
                startActivity( resumeActivity );
            }
        });
    }

}
