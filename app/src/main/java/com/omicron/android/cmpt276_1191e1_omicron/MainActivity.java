package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Intent;
import android.os.Environment;
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


public class MainActivity extends AppCompatActivity
{
	/*
	 *  This Main Activity is the activity that will act as the Start Menu
	 */

	RadioGroup Difficulty;
	RadioGroup Language;
	private int usrLangPref = 0; // 0=eng_fr, 1=fr_eng; 0 == native(squares that cannot be modified); 1 == translation(the words that the user inserts)
	private int usrDiffPref; //0=easy,1=medium,2=difficult
	private int state; //0=new start, 1=resume
	private WordPackageFileIndex wordPackageFileIndexArr; //stores word packages name and internal file name
	private String wordPackageName; //stores name of all Word Packages the user has so far
	private int MAX_WORD_PKG = 5; //max word packages user is allowed to import
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
					new Word( "English", "French", -1, -1 ), //lang
					new Word( "pkg_n.csv", "", -1, -1 ) //pkg name
			};
	
	
	// TODO: IMPORTANT: when creating wordArr, keep in mind not to analyze last 2 pair as it is the language and file_name
	
	// TODO: when creating wordArray, also include the line on which this word pair was taken (consider to ignore first line as it is the language)
	
	// TODO: IMPORTANT: the 10th pair contains the internal storage file name - used so to know to which file to save
	// TODO:				- then when returning from GameAct to MainAct, loop through pkg_n (get content to a string), then the THIRD
	// TODO:				  ARGUMENT in a Word() has the line number, so for each line in pkg_n get line, check if the line # mathces
	// TODO:				  one of the 3rd arguments in wordArr.word(), if it does then replace the line from pkg_n with the one from
	// TODO: 				  wordArray, else keep the line the same

	// TODO: in GameAct in onStop() save data from wordArr to internal storage file
	
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		fileCSV = new FileCSV( MAX_WORD_PKG );
		
		
		
		//long testLong = Long.parseLong( "123456789123451" );
		//Log.d( "upload", "long: " + testLong );
		
		//TEST IF USER JUST INSTALLED APP - IF USER HAS, LOAD DEFAULT FILES
		int usrNewInstall = fileCSV.checkIfCurrentWordPkgCountFileExists( this ); //0==files already exist
		
		Log.d( "upload", "-------------------------------" );
		
		if( usrNewInstall == 0 ) //if app was already installed and has correct files - get current_word_pkg_count
			try {
				CURRENT_WORD_PKG_COUNT = fileCSV.findCurrentPackageCount( this ); //get current Packages count so far
				Log.d( "upload", "---- MAX_WORD_PKG: " + MAX_WORD_PKG );
				Log.d( "upload", "Default CURRENT_WORD_PKG_COUNT: " + CURRENT_WORD_PKG_COUNT );
			} catch (IOException e) {
				e.printStackTrace();
			}
		else //fresh app install
		{
			try
			{
				fileCSV.importDefaultPkg( this ); //load default Word Package
				CURRENT_WORD_PKG_COUNT = fileCSV.findCurrentPackageCount( this ); //get current Packages count so far
				Log.d( "upload", "---- MAX_WORD_PKG :: FRESH INSTALL :: " + MAX_WORD_PKG );
				Log.d( "upload", "Default CURRENT_WORD_PKG_COUNT :: FRESH INSTALL :: " + CURRENT_WORD_PKG_COUNT );
			} catch( IOException e ) {
				e.printStackTrace( );
			}
		}
		
		///////////
		//
		//	## NEXT: work on readCSVFile( ) to check if csv file is valid (format)
		//
		//	## NEXT: finish WordPackageFileIndex( ) creating array that stores WordPkgName and internal_storage_name
		//   		 	+ add these as a .csv file in internal storage (see already created "word_pkg_name_and_file_name.csv")
		//				+ if user just installed app, create
		//
		///////////
		
		
		//////////////////
		//	THIS IS A TEST CALL to test if internal files have empty last line
		/*try {
			fileCSV.importDefaultPkg2( this );
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		///////////////////
		
		
		// read all packages the user has uploaded so far, and get an array with name and file
		try {
			wordPackageFileIndexArr = new WordPackageFileIndex( this, MAX_WORD_PKG, CURRENT_WORD_PKG_COUNT ); //allow a maximum of X packages
		} catch( IOException e ){
			e.printStackTrace( );
		}
		
		// TODO: implement so all pkg are shown in scroll
		
		
		
		
		if( wordPackageFileIndexArr.length( ) > 0 ) //boundary check - redundant since if it were empty, by now the default would have been loaded
		{
			Log.d("upload", "[0] filename: " + wordPackageFileIndexArr.getPackageFileAtIndex(0).getInternalFileName());
			Log.d("upload", "[0] pkg name: " + wordPackageFileIndexArr.getPackageFileAtIndex(0).getWordPackageName());
			Log.d("upload", "[0] native: " + wordPackageFileIndexArr.getPackageFileAtIndex(0).getNativeLang());
			Log.d("upload", "[0] trans: " + wordPackageFileIndexArr.getPackageFileAtIndex(0).getTranslateLang());
		}
		else
		{ Log.d( "upload", "wordPackageFileIndexArr is empty" ); }
		
		
		
			// UPDATE THE WORD PKG SCROLL //
		
		//get string[] with all pkg names
		/*String[] allPkgName;
		try {
			allPkgName = fileCSV.findAllPkgName( this, CURRENT_WORD_PKG_COUNT );
		} catch (FileNotFoundException e) {
			allPkgName = null;
			e.printStackTrace();
		} catch (IOException e) {
			allPkgName = null;
			e.printStackTrace();
		}
		
		////////// debug
		Log.d( "upload", "allPkgName :: ::" );
		for( int i=0; i<CURRENT_WORD_PKG_COUNT; i++ )
		{
			Log.d( "upload", allPkgName[i] );
		}
		Log.d( "upload", "CURRENT_WORD_PKG_COUNT: " + CURRENT_WORD_PKG_COUNT );
		//////////
		*/
		
		RadioButton radBtn;
		final RadioGroup pkgRadioGroup = findViewById( R.id.pkg_radio_group );
		//if( allPkgName != null ) //consider allPkgName failed to initialize
		//{
			for( int i=0; i<CURRENT_WORD_PKG_COUNT; i++ )
			{
				radBtn = new RadioButton( this );
				
				//radBtn.setText( allPkgName[i] );
				radBtn.setText( wordPackageFileIndexArr.getPackageFileAtIndex( i ).getWordPackageName( ) );
				
				pkgRadioGroup.addView(radBtn);
				
				if (i == 0) //automatically select first button
				{
					( (RadioButton) (pkgRadioGroup.getChildAt(0)) ).setChecked( true );
				}
			}
		//}
		
		
		// TODO: test if pkgSelected works, it it detects correct selected pkg
		
		
			// SET LISTENERS TO WHICH PKG IS SELECTED //
		
		pkgRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int pkgSelectId = group.getCheckedRadioButtonId();
				switch(pkgSelectId)
				{
					
					///
					
					
				}
			}
		});
		
		
		
		// TODO: dont forget to check is wordPackageFileIndexArr is full
		// TODO: dont forget to update raw file "current_word_pgk_count.txt" when user uploads new file
		
		// TODO: dont forget to update raw file "current_word_pgk_count.txt" when user REMOVES file
		// TODO: test if MAX_WORD_PKG limit is reached, that user cannot upload file
		
		// TODO: test user uploading multiple files in the same game
		// TODO: test when user comes back from UploadActivity that the file was added in ScrollView ie pass intent back
		// TODO: when user already has reached MAX_WORD_PKG, block user from pressing to import new file
		
		// TODO: dont forget that whenever user switches packages, also switch the langauges in the mode "radio group" ie change "English to French" to "English to Spanish"
		// TODO: capitalize the first letter of the Langauge input
		
		// TODO: implement feature to allow user to delete a package
		// TODO: however, DO NOT allow user to delete default packages
		// TODO: change MAX_WORD_PKG back to 50
		
		// TODO: also mention that first row of CSV file must contain languages
		// TODO: FIX LANDCAPE MODE IN MAIN ACTIVITY
		
		// TODO: when allowing user delete a package, also delete from word_pkg_name_file_name
		// TODO:	- also decrease current_word_pkg_count
		// TODO: 	- also do not allow user to delete default pkgs -check if pkg default, dont delete
		
		// TODO: on REMOVE btn, ask user to confirm selected package
		
		// TODO: dont forget to finish "user deleted pkg" part in MainAct in function onStart( )
		
	
		
		
		
		//create button which will start new UploadActivity to upload and process .csv file
		Button btn_upload = findViewById( R.id.btn_upload );
		btn_upload.setOnClickListener(new View.OnClickListener( )
								{
									@Override
									public void onClick( View v )
									{
										
										Intent uploadActivityIntent = new Intent( MainActivity.this, UploadActivity.class );
										uploadActivityIntent.putExtra( "MAX_WORD_PKG", MAX_WORD_PKG );
										
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
											
											  // TODO: when deleting pkg; take the pkg_name and file_name to delete from wordPackageFileIndex
											  // TODO:	+ also implement onStart() to update add data so far
											  // TODO: 	+ check if data is actually deleted in internal files
											  // TODO: also START AN ACTIVITY that will remove pkg (this is easier because the data will be updates in onStart() )
											  // TODO: 	+ modify onStart()
											  // TODO: remove pkg
											
											  // TODO: test case where user just removed a pkg and starts a new game
											
											  // TODO: add some TextView message as saying to confirm if user wanting to remove pkg
										  	
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
		
		
	
	
	
	
	
		//////////////
		//	if using SD, check if SD is mounted:
		//////////////
	
		////////////////////
		//
		//	limit the number of vocab lists to 50
		//	check if app has read access and add toast if permission not given
		//	check validity if csv file (only once before importing to internal storage)
		//		- check if no cell is empty
		//		- check is there is a minimum of 9 words
		//		- check for maximum of 100 word pairs per csv
		//		- check if each row in csv file has 2 attributes
		//	ask user to type Language of Col1 and Col2 (which is native vs translated)
		//	add a default csv file with numbers up to 50 in eng-fr
		//	display message to let user know that csv file must be stored in Download folder - can either from web of move file to folder via USB
		//		+additionally user can download .csv file from the web, which will be placed in the Download
		//		+mention that all these files can be seen in the "File" application, so as long as the .csv file is on phone its ok, ie can be downloaded from web, imported via usb,
		//	ask for file name
		//		- check if has extension .csv
		//		- check if such file exists in /Downloads folder
		//
		////////////////////
	
		//////////
		//	idea, to csv file also add "difficulty" column so when choosing 9 words, to choose based on difficulty
		//////////
	
		////////////
		//
		//	TODO: import file from external /Downloads storage, VALIDATE CSV file, and store in internal storage (which is private)
		//	TODO: have a sql database that stores "Title of these pairs (ie Chapter 1)" and all file names (ie eng_fr.csv, eng_de.csv) and also the languages and word count
		//	TODO: first deal with .csv file already inside app internal storage until prof answers questions
		//
		//	TODO: create array that adds RadioButtons inside Radio group using for loop + add name of that file
		//  TODO: 	+ have the first option automatically selected in for loop
		//
		//	TODO: move all "Canvas" stuff to drw.class but leave all "new declarations" in GameActivity
		//	TODO: change api to lower in manifest to test on physical phone (not just emulator) to see if file load works
		//
		////////////
	
	
		////////////
		//
		//	maybe move canvas from GameActivity and store in drw.class to make it a Model Object
		//	mention that user can drag on "zoom" mode
		////////////
	
		//////////
		//	add in TDD example, that user must give permission for app to access storage
		//////////////
	
		//////////////
		//	try to see if storage access is given on phone, if XML doesnt work, then create function to ask for permission
		//////////////
	
		// TODO: dont forget to add user stories + TDD examples to cover all new features + features given as requirement
		// TODO: in user story say that "used  algorithm to automatically detect and more often select which words the user has difficulty with based on the number of times the user had to reveal a translation in Hint Pop-Up"
		
		// TODO: pull first +save copy, then merge
	
		// TODO: change SDK version in gradle back to 21
		
		// TODO: make sure to use onStop( ) instead of onDestroy( )
	
		//////////////////
		//
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
		////////////////////////
	
		// TODO: when user inputing name of csv package ie "Chapter 1 Vocab", make sure to check such a package doesnt exist already
		// TODO: add instruction to tell user to insert what to call the Word Package ie "Vocab 1"
		
		// TODO: test app by uploading a file, selecting it, then playing game a little, then going back to main menu, then resume game - see if imported wordArray works
		// TODO: test if CURRENT_WORD_PKG_COUNT is preserved when going to GameAct and back to MainAct
	
		// TODO: IMPORTANT: (already implemented in XML EditText) forbid commas when user give WordPackageName, ie analyse and remove user's input commas using str.replace(",", "");, this is necessary so that when putting data to word_pkg_name_file_name.csv, the comma does not interfere when using comma to separate pkg_name and internal_file_name;
	
		// TODO: make sure the "adapt text size according to sqrSize and zoom" exists in TextMatrix file
	
		// TODO:  (already implemented in XML EditText) limit to 35 char + non-empty (using string.replace( ",", "" );)
		// TODO: tell user that the .csv file has to have Native langauge in 1st col and translation in 2nd column
	
		// TODO: make sure each time the GameActivity onStop is called, save all data to file
		// TODO: test if switching between Main and Game Activity preserves filled puzzle after implementing import
		
		// TODO: to test if "select words the user has difficulty with" works, use Package of ~20 words, and in hint pop-up spam ONLY a single word, check if the pkg_n.csv was correctly updated AND
		// TODO:	+ after that restat couple of games and see if that word appears almost all the time; ie in 20 words if user used Hint on it 10 times, user should aprox see that word 50% of time, (with STATISTIC_MULTIPLE of 2)
		
		// TODO: use STATISTIC_MULTIPLE of two to actually double the likelyhood of word appearing (see paper note)
		
		// TODO: make sure when choosing words from .csv, make sure not to have Rand r choose the same word, ie loop to find word until a unused word was found + also limit Rand loop to ~10k for safety else, say and error occured and end activity
		// TODO: check when user inserts package name if such a package already exists, if it does, exit and show msg
		
		// # # # NOTE: switched language named from xml to inside file so user does not have to know which column is which, ie language will have corresponding word under it
		
		// TODO: make sure to let user know that first row in CSV has to contain language
		// TODO: deal with case when importing an empty file
		
		// TODO: test case where user given csv file under 9 words
		// TODO: test file with not utf-8 char
		
		// TODO: it may be the case that whenever returning to main menu and pressing the "RESUME" btn, the wordArr will be regenerated
		// TODO:	- ie in line "if (resumeSrc != null)", botton of code in this activity, consider resetting
		// TODO		IMPORTANT :: actually maybe save the data to file in GameAct as it is stopped()
		
		// TODO: test if "hint click" are preserved when resuming game
		
		// TODO: also explain what csv file is: no empty line, only separated by comma, no comma within a cell, no string delimiter (")
		// TODO:	+ show an example
		
		// TODO: in tdd example also mention that user can upload / remove pkg
		
		// TODO: when deleting pkg; take the pkg_name and file_name to delete from wordPackageFileIndex
		// TODO:	+ also implement onStart() to update add data so far
		// TODO: 	+ check if data is actually deleted in internal files
		
		// TODO: test app by randomly starting game, then go back to main menu, then upload a pkg, then resume game , then remove pkg and resume game ...
		// TODO: test: user uploads a package, starts a new game with that package, then comes back to main menu
		// TODO:		then removes that package AND them RESUME the game AND then go back to main menu (previous error was if file deleted then GameActivity cannot open file to save "Hint Click")
		
		// TODO: see if when merging entire project with everybody, see if "REMOVE PKG" button is still disabled
		// TODO: 	- idea: ctr+f to find all "btnResume.setEnabled()" and when RESUME is set to false, then set REMOVE_BTN to TRUE, and other way around as well
		// TODO: test upload pkg, start game with default pkg, the get back to main menu, (REMOVE should be disabled) then click STOP GAME, then press to remove a file BUT DO NOT REMOVE A FILE, go back and see if RESUME is disabled, it should be
		
		// TODO: shrink buttons in MainActivity to fit everything
		// TODO: mention that maybe instead add an icon where usr can click "this is a hard word" to increment "Hint Click"
		
		// TODO: once Hint Click is implemented by other team member, try it to see if csv file is actually updating
		
		
		
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
					
					Log.d( "upload", "  ## current pkg name selected: " + pkgNameSelected );
					Log.d( "upload", "  ## current pkg internal file selected: " + fileNameSelected );
					
					try {
						initializeWordArray( fileNameSelected ); //based on pkg, initialize the array (select 9 words)
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					Intent gameActivity = new Intent( MainActivity.this, GameActivity.class );

					//save wordArray for Game Activity
					gameActivity.putExtra( "wordArray", wordArray );
					gameActivity.putExtra( "usrLangPref", usrLangPref );
					gameActivity.putExtra("usrDiffPref",usrDiffPref);
					gameActivity.putExtra("state", state);
					finish( );
					startActivity( gameActivity );
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
		
		
		final Button btnResume = (Button) findViewById(R.id.button_resume);
		btnResume.setEnabled(false); //block Resume button unless a previous game is saved
		//DISABLE "REMOVE PKG" button when game is started
		removeBtnEnable[0] = true;
		
		//check if a previous game existed. If it did, unblock resume button
		final Intent resumeSrc = getIntent( );
		if (resumeSrc != null) {
			//if all necessary game preferences are written in memory, then unblock resume button
			if (resumeSrc.hasExtra("wordArrayGA") && resumeSrc.hasExtra("usrLangPrefGA") && resumeSrc.hasExtra("SudokuArrGA") && resumeSrc.hasExtra("state")) {
				btnResume.setEnabled(true);
				
				//DISABLE "REMOVE PKG" button when game is started
				removeBtnEnable[0] = false;
				
				// TODO: maybe here have to restore wordArray if returning from GameAct
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
	
	
	@Override
	public void onStart( )
	{
		super.onStart( );
		
			/** WHEN USER RETURNING FROM UPLOAD ACTIVITY, UPDATE WORD PKG LIST **/
			
		FileCSV fileCSV = new FileCSV( MAX_WORD_PKG );
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
			
			// TODO: finish this
			
			//for( int i=pkgRadioGroup.getChildCount(); i>CURRENT_WORD_PKG_COUNT; i-- )
			//{
				
				// TODO: delete radio btn
				
				//pkgRadioGroup.getChildAt( pkgRadioGroup.getChildCount() - 1 ); //get last child
				
				
			//}
			
			
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
	
	
	private void initializeWordArray( String fileNameSelected ) throws IOException //
	{
		/*
		 * This function initializes the  word array to 9 words based on pkg selected for user for GameActivity
		 */
		
		// TODO: this currently only selects first 9 words of pkg
		// TODO:	- later implement difficulty statistics
		
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
		
		
		// -- for now only read first 9 words -- -- --
		
		String line;
		String[] strSplit;
		
		int i = 0;
		
		Log.d( "upload", " @ WORD_ARRAY ON START GAME BTN CLICK:" );
		
		//set pkg name
		wordArray[10] = new Word( fileNameSelected, "", -1, -1 );
		
		while( (line = buffRead.readLine( )) != null && i < 10 ) //loop and get all lines
		{
			if( i == 0 ) //get language from file
			{
				strSplit = line.split( "," );
				wordArray[9] = new Word( strSplit[0], strSplit[1], -1, -1 );
			}
			else
			{
				strSplit = line.split( "," );
				//note: hintClick in wordArray is set to 0, because hintClick only stores how many times user had difficulty in a specific game
				wordArray[i-1] = new Word( strSplit[0], strSplit[1], i, 0 ); //set new word based on csv file
				Log.d( "upload","wordArr[" + (i-1) + "]: " + strSplit[0] + "," + strSplit[1] + "," + i + "," + 0 );
			}
			i++;
		}
		
		// -- -- -- -- -- -- -- -- -- -- --
		
	}
	
	
}







































