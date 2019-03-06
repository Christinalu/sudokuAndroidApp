package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Intent;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;


public class MainActivity extends AppCompatActivity
{
	/*
	 *  This Main Activity is the activity that will act as the Start Menu
	 */

	RadioGroup Difficulty;
	RadioGroup Language;
	RadioGroup Mode;
	private int usrModePref = 0; // 0=standard, 1=speech
	private int usrLangPref = 0; // 0=eng_fr, 1=fr_eng; 0 == native(squares that cannot be modified); 1 == translation(the words that the user inserts)
	private int usrDiffPref; //0=easy,1=medium,2=difficult
	private int state; //0=new start, 1=resume
	private String language;
	private boolean canStart = true;

	Word[] wordArrayResume;
	private int usrLangPrefResume;
	private SudokuGenerator usrSudokuArrResume;
	int usrModePrefResume;
	String languageResume;

	//used only for user entering language check//convert to appropriate tag
	private TextToSpeech lTTS;
	private List<String> langCountries = new ArrayList<String>();
	private List<String> langTags = new ArrayList<String>();
	private List<Locale> localeList = new ArrayList<Locale>();
	Locale usrlangchoice;

	private WordPackageFileIndex wordPackageFileIndexArr; //stores word packages name and internal file name
	private String wordPackageName; //stores name of all Word Packages the user has so far
	private int MAX_WORD_PKG = 50; //max word packages user is allowed to import
	private int MAX_CSV_ROW = 150; //allow up to 150 pairs per package
	private int CURRENT_WORD_PKG_COUNT = 0; //stores current number of packages the user has uploaded
	private FileCSV fileCSV; //object containing CSV functions
	private int[] indexOfRadBtnToRemove = { -1 }; //which radio btn to remove
	private boolean[] removeBtnEnable = { true }; //when false, do not allow "REMOVE PKG" button (required because GameActivity may be using that file to save "Hint Click")
	
	
	// SET UP ARRAY TO STORE WORDS
	// a Word (pair) contains the word in native language, and its translation
	// note: this array will remain of size 9, and only changed when modes are switched
	//       this is required in DictionaryActivity.java
	//		 the 9th index contains the corresponding language
	//		 the 10th index contains the internal storage file name
	private Word[] wordArray =new Word[]
			{
					new Word( "Un", "Un", 1, 1 ),
					new Word( "Two", "Deux", 2, 1 ),
					new Word( "Threeeeeeeeee", "Troisssssssss", 3, 1 ),
					new Word( "Four", "Quatre", 4, 1 ),
					new Word( "Five", "Cinq", 5, 1 ),
					new Word( "Six", "Six", 6, 1 ),
					new Word( "Seven", "Sept", 7, 1 ),
					new Word( "Eightttttttttttttt", "Huitttttttttttttt", 8, 1 ),
					new Word( "Nine", "Neuf", 9, 1 ),
					new Word( "en-US", "fr-FR", -1, -1 ), //lang
					new Word( "pkg_n.csv", "", -1, -1 ) //pkg name
			};
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		fileCSV = new FileCSV( MAX_WORD_PKG, MAX_CSV_ROW );
		
		//TEST IF USER JUST INSTALLED APP - IF USER HAS, LOAD DEFAULT FILES
		int usrNewInstall = fileCSV.checkIfCurrentWordPkgCountFileExists( this ); //0==files already exist
		
		if( usrNewInstall == 0 ) //if app was already installed and has correct files - get current_word_pkg_count
			try {
				CURRENT_WORD_PKG_COUNT = fileCSV.findCurrentPackageCount( this ); //get current Packages count so far
			} catch (IOException e) {
				e.printStackTrace();
			}
		else //fresh app install
		{
			try
			{
				fileCSV.importDefaultPkg( this ); //load default Word Package
				CURRENT_WORD_PKG_COUNT = fileCSV.findCurrentPackageCount( this ); //get current Packages count so far
			} catch( IOException e ) {
				e.printStackTrace( );
			}
		}
		
		
		// read all packages the user has uploaded so far, and get an array with name and file
		try {
			wordPackageFileIndexArr = new WordPackageFileIndex( this, MAX_WORD_PKG, CURRENT_WORD_PKG_COUNT ); //allow a maximum of X packages
		} catch( IOException e ){
			e.printStackTrace( );
		}
		
		
		// TODO: FIX LANDCAPE MODE IN MAIN ACTIVITY
		// TODO: move all "Canvas" stuff to drw.class but leave all "new declarations" in GameActivity
		// TODO: FIX so that btn REMOVE is disabled when RESUME is active
		// TODO: dont forget to add user stories + TDD examples to cover all new features + features given as requirement
		// TODO: in user story say that "used  algorithm to automatically detect and more often select which words the user has difficulty with based on the number of times the user had to reveal a translation in Hint Pop-Up"
		
		// TODO: add note in app that the files are shown directly if they were downloaded
		// TODO:	 from a website (on the phone) they would automatically be displayed
		// TODO		 however for testing purposes, these "Word Pair" files are not downloadable for our class
		// TODO:	 so it may be faster to import file from storage (via USB). Sometimes this feature is not
		// TODO:	 enabled by default, so user may need to click on top right corner on "settings/options"
		// TODO:	 in android 5.0 this looks like 3 vertical dots, and then click "show SD card" or "show internal storage"
		// TODO:	 or similar depending on SDK version. If google drive is set up, user may also import .csv
		// TODO: 	 files from Google Drive. However, if one may want to test by downloading from a website
		// TODO:	 a easy way is to find a free temporary hosting website which provides a "upload" then "download" feature for a file
		// TODO:	 One may additionally post a file on Google Drive, then upload it from there
		// TODO:		+ let  user know the rules about csv file and other "text field" input ie Native Name <= 35 char
		
		// TODO: make sure the "adapt text size according to sqrSize and zoom" exists in TextMatrix file
		// TODO: make sure to let user know that first row in CSV has to contain language
		// TODO: test file with not utf-8 char
		
		// TODO: FINISH INSTRUCTIONS IN UPLOAD CSV
		
		// TODO: test if "hint click" are preserved when resuming game
		// TODO: also explain what csv file is: no empty line, only separated by comma, no comma within a cell, no string delimiter (")
		// TODO:	+ show an example
		// TODO: in tdd example also mention that user can upload / remove pkg
		
		// TODO: test app by randomly starting game, then go back to main menu, then upload a pkg, then resume game , then remove pkg and resume game ...
		// TODO: test: user uploads a package, starts a new game with that package, then comes back to main menu
		// TODO:		then removes that package AND them RESUME the game AND then go back to main menu (previous error was if file deleted then GameActivity cannot open file to save "Hint Click")
		
		// TODO: see if when merging entire project with everybody, see if "REMOVE PKG" button is still disabled
		// TODO: 	- idea: ctr+f to find all "btnResume.setEnabled()" and when RESUME is set to false, then set REMOVE_BTN to TRUE, and other way around as well
		// TODO: test upload pkg, start game with default pkg, the get back to main menu, (REMOVE should be disabled) then click STOP GAME, then press to remove a file BUT DO NOT REMOVE A FILE, go back and see if RESUME is disabled, it should be
		
		// TODO: shrink buttons in MainActivity to fit everything
		// TODO: once "Hint Click" is implemented by other team member, try it to see if csv file is actually updating
		// TODO: check if switching modes switches languages
		// TODO: in mode_btn actually show language instead of "Native-Translation"
		
		
		
		// UPDATE THE WORD PKG SCROLL //
		
		RadioButton radBtn;
		final RadioGroup pkgRadioGroup = findViewById( R.id.pkg_radio_group );
		
		for( int i=0; i<CURRENT_WORD_PKG_COUNT; i++ )
		{
			radBtn = new RadioButton( this );
				
			radBtn.setText( wordPackageFileIndexArr.getPackageFileAtIndex( i ).getWordPackageName( ) );
				
			pkgRadioGroup.addView(radBtn);
				
			if (i == 0) //automatically select first button
			{
				( (RadioButton) (pkgRadioGroup.getChildAt(0)) ).setChecked( true );
				
			}
		}
		
		
		
			// SET LISTENERS TO WHICH PKG IS SELECTED //
		
		pkgRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int pkgSelectId = group.getCheckedRadioButtonId();
				switch(pkgSelectId)
				{
					//void
				}
			}
		});
		
		
		
		
		
		
		//create button which will start new UploadActivity to upload and process .csv file
		Button btn_upload = findViewById( R.id.btn_upload );
		btn_upload.setOnClickListener(new View.OnClickListener( )
								{
									@Override
									public void onClick( View v )
									{
										
										Intent uploadActivityIntent = new Intent( MainActivity.this, UploadActivity.class );
										uploadActivityIntent.putExtra( "MAX_WORD_PKG", MAX_WORD_PKG );
										uploadActivityIntent.putExtra( "MAX_CSV_ROW", MAX_CSV_ROW );
										
										startActivity( uploadActivityIntent );
									}
								}
		);
		
		
		//create button which will remove file
		final Button btn_remove = findViewById( R.id.btn_remove );
		btn_remove.setOnClickListener(new View.OnClickListener( )
									  {
										  @Override
										  public void onClick( View v )
										  {
											  //FIND WHICH BUTTON IS SELECTED
											  int indexOfRadBtnToRemove2 = pkgRadioGroup.indexOfChild( findViewById( (pkgRadioGroup.getCheckedRadioButtonId()) ) );
											  indexOfRadBtnToRemove[0] = indexOfRadBtnToRemove2; //save a local copy needed for onStop() to remove radio btn
											  
											  String pkgInternalFileName = wordPackageFileIndexArr.getPackageFileAtIndex( indexOfRadBtnToRemove2 ).getInternalFileName( ); //find internal file name of pkg to remove
											  String pkgName = wordPackageFileIndexArr.getPackageFileAtIndex( indexOfRadBtnToRemove2 ).getWordPackageName( ); //user defined pkg name
											  
											  Intent removeActivityIntent = new Intent( MainActivity.this, RemoveActivity.class );
											  removeActivityIntent.putExtra( "indexOfRadBtnToRemove", indexOfRadBtnToRemove2 );
											  removeActivityIntent.putExtra( "pkgInternalFileName", pkgInternalFileName );
											  removeActivityIntent.putExtra( "pkgName", pkgName );
											
											  startActivity( removeActivityIntent );
										  }
									  }
		);
		
		
		
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

		// CHOOSE THE MODE
		Mode=findViewById(R.id.button_mode);
		Mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int ModeId = group.getCheckedRadioButtonId();
				switch (ModeId)
				{
					case R.id.button_mStandard:
						usrModePref = 0;
						break;

					case R.id.button_mSpeech:
						usrModePref = 1;
						break;
				}
			}
		});

		lTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					Locale[] thelocale = Locale.getAvailableLocales();
					//For optional implementations
					String langCountry;
					int counter = 0;
					for (Locale LO : thelocale) {
						int res = lTTS.isLanguageAvailable(LO);
						if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
							//store all available locales as Locale type
							localeList.add(LO);
							//store all available language tag (String)
							langTags.add(LO.toLanguageTag());
							Log.e("lTTS", langTags.get(counter));
							//counter++;
                            /*
                            //For optional implementations
                            langCountry = langTags.get(counter);
                            Log.e("lTTS", langCountry);
                            langCountry = LO.getDisplayLanguage() + " - " + LO.getDisplayCountry();
                            Log.e("lTTS", langCountry);
                            //store all available locales in language - country format (strings)
                            langCountries.add(langCountry);
                            */
						}
					}
				}
				else {
					Log.e("lTTS", "TTS failed to initiate");
				}
			}
		});

		
			/** START GAME BUTTON **/
		
		// used to switch to gameActivity
		Button btnStart = (Button) findViewById( R.id.button_start );
		btnStart.setOnClickListener( new View.OnClickListener(  )
			{
				@Override
				public void onClick( View v )
				{
					// TODO: in here, one START btn is pressed, create word array with respect to pkg selected
					
					RadioButton radBtnSelected = findViewById( pkgRadioGroup.getCheckedRadioButtonId() );
					String pkgNameSelected = radBtnSelected.getText().toString( ); //get pkg name inside
					String fileNameSelected = wordPackageFileIndexArr.getPackageFileAtIndex( pkgRadioGroup.indexOfChild( radBtnSelected ) ).getInternalFileName( ); //get pkg internal file name to find csv
					
					try {
						int res = initializeWordArray( fileNameSelected ); //based on pkg, initialize the array (select 9 words)
						if( res == 1 ){
							Log.d( "upload", "ERROR: initializeWordArray( ) returned an error" );
							return;
						} //error: could not initialize wordArray
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					Intent gameActivity = new Intent( MainActivity.this, GameActivity.class );
					state = 0;
					//check to see for language format is correct and available
					if (usrModePref == 1) {
						if (usrLangPref == 0) {
							language = "en-US";
							//language = wordArray[9].getTranslation();
							Log.e("lTTSs", "language is: "+language);
						}
						else {
							language = "fr-FR";
							//language = wordArray[9].getNative();
							Log.e("lTTSs", "language is: "+language);
						}
						canStart = false;
						for (int i=0; i<langTags.size(); i++) {
							//Log.e("lTTS", "language is: "+language+" langTag is: "+langTags.get(i));
							if (Objects.equals(language,langTags.get(i))) {
								canStart = true;
								if (canStart) {
									break;
								}
							}
						}
						if (canStart) {
							//save wordArray for Game Activity
							gameActivity.putExtra( "wordArray", wordArray );
							gameActivity.putExtra( "usrLangPref", usrLangPref );
							gameActivity.putExtra("usrDiffPref",usrDiffPref);
							gameActivity.putExtra("state", state);
							gameActivity.putExtra("usrModeMA", usrModePref);
							gameActivity.putExtra("languageMA", language);
							startActivityForResult(gameActivity,0);
						}
						else {
							Toast.makeText(v.getContext(),R.string.no_language, Toast.LENGTH_LONG).show();
						}
					}
					else {
						//standard start
						gameActivity.putExtra( "wordArray", wordArray );
						gameActivity.putExtra( "usrLangPref", usrLangPref );
						gameActivity.putExtra("usrDiffPref",usrDiffPref);
						gameActivity.putExtra("state", state);
						gameActivity.putExtra("usrModeMA", usrModePref);
						gameActivity.putExtra("languageMA", language);
						startActivityForResult(gameActivity,0);
					}
                                             /*
                                             //Assume proper language formatting in CSV file
                                             if (usrModePref == 1) {
                                                 if (usrLangPref == 0) {
                                                     language = wordArray[9].getTranslation();
                                                 } else {
                                                     language = wordArray[9].getNative();
                                                 }
                                             }
                                             //unconditional game start: save wordArray for Game Activity
                                             gameActivity.putExtra("wordArray", wordArray);
                                             gameActivity.putExtra("usrLangPref", usrLangPref);
                                             gameActivity.putExtra("usrDiffPref", usrDiffPref);
                                             gameActivity.putExtra("state", state);
                                             gameActivity.putExtra("usrModeMA", usrModePref);
                                             gameActivity.putExtra("languageMA", language);
                                             startActivityForResult(gameActivity, 0);
                                             */
				}
			}
		);
		
		
		//implement STOP btn
		Button btnStop = (Button) findViewById( R.id.button_stop );
		btnStop.setOnClickListener( new View.OnClickListener(  )
									{
										@Override
										public void onClick( View v )
										{
											btn_remove.setEnabled( true ); //allow user to remove pkg
											removeBtnEnable[0] = true;
											Button btnResume = findViewById( R.id.button_resume );
											btnResume.setEnabled( false );
											//remove all content from wordArray
											for( int i=0; i<wordArray.length; i++ )
											{ wordArray[0] = null; }
										}
									}
		);
		
		
		Button btnResume = (Button) findViewById(R.id.button_resume);
		btnResume.setEnabled(false); //block Resume button unless a previous game is saved
		//DISABLE "REMOVE PKG" button when game is started
		removeBtnEnable[0] = true;
		/*
		
		//check if a previous game existed. If it did, unblock resume button
		final Intent resumeSrc = getIntent( );
		if (resumeSrc != null) {
			//if all necessary game preferences are written in memory, then unblock resume button
			if (resumeSrc.hasExtra("wordArrayGA") && resumeSrc.hasExtra("usrLangPrefGA") && resumeSrc.hasExtra("SudokuArrGA") && resumeSrc.hasExtra("state")) {
				btnResume.setEnabled(true);
				
				//DISABLE "REMOVE PKG" button when game is started
				removeBtnEnable[0] = false;
				
			}
		}
		*/
		
		//if resume button is unblocked and pressed, it will load previous game preferences
		btnResume.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state==1) {
					//load previous game preferences to prepare for export to new Game Activity
					Intent resumeActivity = new Intent(MainActivity.this, GameActivity.class);
					//save preferences for Game Activity to read
					resumeActivity.putExtra("wordArrayMA", wordArrayResume);
					resumeActivity.putExtra("usrLangPrefMA", usrLangPrefResume);
					resumeActivity.putExtra("SudokuArrMA", usrSudokuArrResume);
					resumeActivity.putExtra("state", state);
					resumeActivity.putExtra("usrModeMA", usrModePrefResume);
					resumeActivity.putExtra("languageMA", language);
					startActivityForResult(resumeActivity, 0);
				}
				else {
					Toast.makeText(v.getContext(),R.string.no_resume, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	
	@Override
	public void onStart( )
	{
		super.onStart( );
		
			/** WHEN USER RETURNING FROM UPLOAD ACTIVITY, UPDATE WORD PKG LIST **/
			
		FileCSV fileCSV = new FileCSV( MAX_WORD_PKG, MAX_CSV_ROW );
		int CURRENT_WORD_PKG_COUNT_RETURN = CURRENT_WORD_PKG_COUNT;
		try {
			CURRENT_WORD_PKG_COUNT_RETURN = fileCSV.findCurrentPackageCount( this ); //get current Packages count so far
		} catch (IOException e) {
			CURRENT_WORD_PKG_COUNT_RETURN = CURRENT_WORD_PKG_COUNT;
			e.printStackTrace();
		}
		
		// ENABLE OR DISABLE REMOVE BTN
		if( removeBtnEnable[0] == true ) //enable btn
		{
			Button btn = findViewById( R.id.btn_remove );
			btn.setEnabled( true );
		} else {
			Button btn = findViewById( R.id.btn_remove );
			btn.setEnabled( false );
		}
		
		
		// read all packages the user has uploaded so far, and get an array with name and file
		try {
			wordPackageFileIndexArr = new WordPackageFileIndex( this, MAX_WORD_PKG, CURRENT_WORD_PKG_COUNT_RETURN ); //allow a maximum of X packages
		} catch( IOException e ){
			e.printStackTrace( );
		}
		
		RadioGroup pkgRadioGroup = findViewById(R.id.pkg_radio_group);
		RadioButton radBtn;
		
		if( CURRENT_WORD_PKG_COUNT_RETURN > CURRENT_WORD_PKG_COUNT ) //usr uploaded a pkg
		{
				/* USER UPLOADED A PKG */
			
			CURRENT_WORD_PKG_COUNT = CURRENT_WORD_PKG_COUNT_RETURN; //update pkg count because it increased
			Log.d("upload", "USER UPLOADED A PKG");
			//find how many pkg are available and if user user
			for( int i=pkgRadioGroup.getChildCount(); i<CURRENT_WORD_PKG_COUNT; i++ )
			{
				radBtn = new RadioButton(this);
				
				//radBtn.setText( allPkgName[i] );
				radBtn.setText(wordPackageFileIndexArr.getPackageFileAtIndex(i).getWordPackageName());
				
				pkgRadioGroup.addView(radBtn);
				
			}
		}
		else if( CURRENT_WORD_PKG_COUNT_RETURN < CURRENT_WORD_PKG_COUNT )
		{
				/* USER DELETED A PKG */
			
			Log.d("upload", "USER DELETED A PKG");
			CURRENT_WORD_PKG_COUNT = CURRENT_WORD_PKG_COUNT_RETURN; //update pkg count because it decreased
			
			
			// IMPORTANT: the following procedure limits to deleting only 1 file at a time
			//			  to delete multiple files at one, have to delete and re-create all radio buttons in RadioGroup
			
			pkgRadioGroup.removeViewAt( indexOfRadBtnToRemove[0] );
			
			//reselect top radio btn
			( (RadioButton) pkgRadioGroup.getChildAt(0)).setChecked( true );
			
			
		}
		else
		{
			Log.d("upload", "USER DID NOT MODIFY A PKG");
		}
		
		Log.d( "upload", "onStart() called from MainActivity" );
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent resumeSrc) {
		if (resumeSrc == null) {
			Log.i("TAG", "resumeSrc == null");
			return;
		}
		//if all necessary game preferences are written in memory, then unblock resume button
		Log.i("TAG", "resumeSrc != null");
		if (resumeSrc.hasExtra("wordArrayGA") && resumeSrc.hasExtra("usrLangPrefGA") && resumeSrc.hasExtra("SudokuArrGA") && resumeSrc.hasExtra("state") && resumeSrc.hasExtra("languageGA")) {
			Log.i("TAG", "resumeSrc has all elements");
			wordArrayResume = (Word[]) resumeSrc.getSerializableExtra("wordArrayGA");
			usrLangPrefResume = (int) resumeSrc.getSerializableExtra("usrLangPrefGA");
			usrSudokuArrResume = (SudokuGenerator) resumeSrc.getSerializableExtra("SudokuArrGA");
			usrModePrefResume = (int) resumeSrc.getSerializableExtra("usrModeGA");
			languageResume = (String) resumeSrc.getSerializableExtra("languageGA");
			state = 1;
			Button btnResume = (Button) findViewById(R.id.button_resume);
			btnResume.setEnabled(true);
			Button btnRemove = (Button) findViewById(R.id.button_delete); //block user from deleting pkg while playing game
			btnRemove.setEnabled(false);
			removeBtnEnable[0] = false;
		}
	}
	
	private int initializeWordArray( String fileNameSelected ) throws IOException //
	{
		/*
		 * This function initializes the  word array to 9 words based on pkg selected for user for GameActivity
		 * Returns 1 if could not generate an array
		 */
		
		
			/* OPEN PKG FILE TO READ */
		
		FileInputStream fileInStream = null; //open file from internal storage
		try {
			fileInStream = this.openFileInput( fileNameSelected ); //get internal file name, contained in 10th index of wordArray
		} catch (FileNotFoundException e) {
			Log.d( "upload", "ERROR: exception int initializeWordArr( )" );
			e.printStackTrace();
		}
		InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
		BufferedReader buffRead = new BufferedReader( inStreamRead );
		
		
			/** STATISTICALLY CHOOSE 9 MOST DIFFICULT WORD BASED ON HINT CLICK**/
		
		String line;
		String[] strSplit;
		
		int lineCount = 0;
		int i = 0;
		long total = 0; //stores total number of Click Count
		long totalBk = 0; //back up for "total"
		Range[] rangeArr = new Range[MAX_CSV_ROW]; //array for each word pair, storing the range of "hint clicks"
		
		Log.d( "upload", " @ WORD_ARRAY ON START GAME BTN CLICK:" );
		
		//set pkg name
		wordArray[10] = new Word( fileNameSelected, "", -1, -1 );
		
		
		while( (line = buffRead.readLine( )) != null ) //loop and get all lines
		{
			if( i == 0 ) //get language from file
			{
				strSplit = line.split( "," );
				wordArray[9] = new Word( strSplit[0], strSplit[1], -1, -1 );
				i = 1;
			}
			else
			{
				strSplit = line.split( "," );
				totalBk = total;
				total = total + Long.parseLong( strSplit[2] ); //add Click Count for each word
				
				//note: in Range( 0,0 ), it means only 0th index; Range( 1,5 ) means from 1 to 5 inclusive
				rangeArr[lineCount] = new Range( totalBk, total-1, strSplit[0], strSplit[1], Integer.parseInt( strSplit[2] ) );
				
				lineCount++;
			}
		}
		
		
		//// debug /////////////
		Log.d( "upload", "# of line: " + lineCount );
		Log.d( "upload", "# TOTAL: " + total );
		for( int j=0; j<lineCount; j++ ) //print rangeArr[]
		{
			Log.d( "upload", "RANGE :: line " + (j+1) + ": ( " + rangeArr[j].getNumLeft() + ", " + rangeArr[j].getNumRight() + " )" );
		}
		///////////////////////
		
		
			// RANDOMLY CHOOSE 9 WORDS (based on difficulty) //
		
		// NOTE: Random Number generator does not return all range for "long"
		Random rand = new Random( );
		long randPos; //random position to choose
		int n = 0; //used to prevent run-on random generator
		boolean breakOut = false;
		int[] wordUsed = new int[lineCount]; //array for all words used to mark if a word was selected for wordArray
		
		for( int k=0; k<9; k++ ) //loop to find 9 words
		{
			n = 0; //reset
			breakOut = false; //reset
			while( n < 500000 )
			{
				randPos = rand.nextLong( ) % total; //random position to choose
				
				//loop through rangeArr and find which word range has this value
				for( int c=0; c<lineCount; c++ )
				{
					if( rangeArr[c].getNumLeft() <= randPos && randPos <= rangeArr[c].getNumRight() ) //if within range of word
					{
						if( wordUsed[c] == 1 ) //if word already used
						{
							continue;
						}
						else //word not previously selected for wordArray
						{
							//USE THIS WORD AS NEW wordArr[k]
							wordArray[k] = new Word( rangeArr[c].getStrNative(), rangeArr[c].getStrTranslation(), c+1, 0 );
							wordUsed[c] = 1; //mark word as used
							//break out of loop
							breakOut = true;
							break; //do not look for more words
						}
					}
				}
				
				if( breakOut == true )//breaked after found valid word
				{ break; } //so break out of while loop
				
				n++;
			}
			
			if( n >= 500000 ) //exhausted all tries
			{ return 1; }
		}
		
		
		
		//// debug //////
		for( int j=0; j<lineCount; j++ ) //print rangeArr[]
		{
			Log.d( "upload", "wordUsed[" + (j+1) + "] :: " + wordUsed[j] );
		}
		/////////////////
		
		
		return 0;
	}
}







































