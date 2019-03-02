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
	private int[] CURRENT_WORD_PKG_COUNT = { 0 }; //stores current number of packages the user has uploaded
	private FileCSV fileCSV; //object containing CSV functions
	
	
	// SET UP ARRAY TO STORE WORDS
	// a Word (pair) contains the word in native language, and its translation
	// note: this array will remain of size 9, and only changed when modes are switched
	//       this is required in DictionaryActivity.java
	private Word[] wordArray =new Word[]
			{
					new Word( "Un", "Un", 1, 0 ),
					new Word( "Two", "Deux", 2, 0 ),
					new Word( "Threeeeeeeeee", "Troisssssssss", 3, 0 ),
					new Word( "Four", "Quatre", 4, 0 ),
					new Word( "Five", "Cinq", 5, 0 ),
					new Word( "Six", "Six", 6, 0 ),
					new Word( "Seven", "Sept", 7, 0 ),
					new Word( "Eightttttttttttttt", "Huitttttttttttttt", 8, 0 ),
					new Word( "Nine", "Neuf", 9, 0 )
			};
	
	

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		fileCSV = new FileCSV();
		
		//long testLong = Long.parseLong( "123456789123451" );
		//Log.d( "upload", "long: " + testLong );
		
		//TEST IF USER JUST INSTALLED APP - IF USER HAS, LOAD DEFAULT FILES
		int usrNewInstall = fileCSV.checkIfCurrentWordPkgCountFileExists( this, CURRENT_WORD_PKG_COUNT ); //0==files already exist
		
		Log.d( "upload", "-------------------------------" );
		
		if( usrNewInstall == 0 ) //if app was already installed and has correct files - get current_word_pkg_count
			try {
				CURRENT_WORD_PKG_COUNT[0] = fileCSV.findCurrentPackageCount( this ); //get current Packages count so far
				Log.d( "upload", "---- MAX_WORD_PKG: " + MAX_WORD_PKG );
				Log.d( "upload", "CURRENT_WORD_PKG_COUNT: " + CURRENT_WORD_PKG_COUNT[0] );
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
		{
			try
			{
				fileCSV.importDefaultPkg( this ); //load default Word Package
				Log.d( "upload", "---- MAX_WORD_PKG :: FRESH INSTALL :: " + MAX_WORD_PKG );
				Log.d( "upload", "CURRENT_WORD_PKG_COUNT :: FRESH INSTALL :: " + CURRENT_WORD_PKG_COUNT[0] );
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
		
		
		
		// read all packages the user has uploaded so far, and get an array with name and file
		try {
			wordPackageFileIndexArr = new WordPackageFileIndex( this, MAX_WORD_PKG, CURRENT_WORD_PKG_COUNT ); //allow a maximum of X packages
		} catch( IOException e ){
			e.printStackTrace( );
		}
		
		if( wordPackageFileIndexArr.length( ) > 0 ) //boundary check -redundant since if it were empty, by now the default would have been loaded
		{
			Log.d("upload", "[0] filename: " + wordPackageFileIndexArr.getPackageFileAtIndex(0).getInternalFileName());
			Log.d("upload", "[0] pkg name: " + wordPackageFileIndexArr.getPackageFileAtIndex(0).getWordPackageName());
			Log.d("upload", "[0] native: " + wordPackageFileIndexArr.getPackageFileAtIndex(0).getNativeLang());
			Log.d("upload", "[0] trans: " + wordPackageFileIndexArr.getPackageFileAtIndex(0).getTranslateLang());
		}
		else
		{ Log.d( "upload", "wordPackageFileIndexArr is empty" ); }
		
		
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
		
		////////// APPLICATION storage
		//String path = Environment.getExternalStorageDirectory().toString()+"";

		/*PackageManager pkgManager = getPackageManager();
		String pkgDir = getPackageName();
		try
		{
			PackageInfo pkgInfo = pkgManager.getPackageInfo(pkgDir, 0);
			pkgDir = pkgInfo.applicationInfo.dataDir;
		}
		catch (PackageManager.NameNotFoundException e) {
			Log.w("TAG", "ERROR: no pkg found", e);
		}
		Log.d("FILE", "Path-2: " + pkgDir);
		File directory2 = new File(pkgDir);
		File[] files2 = directory2.listFiles();
		for (int i = 0; i < files2.length; i++)
		{
			Log.d("FILE", "FileName-2:" + files2[i].getName());
		}*/
	
		///////// SD storage
		//Log.d("FILE", "Path-1: " + path);
		//File directory = new File(path);
		//[] files = directory.listFiles();
		//Log.d("FILE", "Size: "+ files.length);
		//for (int i = 0; i < files.length; i++)
		//{
		//	Log.d("FILE", "FileName-1:" + files[i].getName());
		//}
	
	
		/////////// ROOT
		/*ListView listView = findViewById( R.id.list_view );
		ArrayList files3 = new ArrayList();

		Log.d( "FILE", "pass" );

		File[] fileRoot = File.listRoots();
		Log.d( "FILE", "pass-3" );
		for (int i = 0; i < fileRoot.length; i++)
		{
			Log.d( "FILE", "pass-4" );
			File[] directory4  = (fileRoot[i]).listFiles();
			Log.d( "FILE", "pass-7" );
			Log.d( "FILE","directory4.length: " + directory4.length );
			for( int j=0; j<directory4.length; j++ )
			{
				Log.d( "FILE", "pass-5" );
				files3.add( directory4[j].getAbsolutePath() );
				Log.d( "FILE", "pass-6" );
			}
			Log.d("FILE", "FileName-4:" + fileRoot[i].getName());
		}
		Log.d( "FILE", "pass2" );
		ArrayAdapter<String> adapter = new ArrayAdapter<String>( this, android.R.layout.simple_expandable_list_item_1, files3 );
		listView.setAdapter( adapter );
		*/
		///////////////////////////////////
	
	
	
	
		//////// SD download
		
		
		
		//create button which will start new UploadActivity to upload and process .csv file
		Button btn_upload = findViewById( R.id.btn_upload );
		btn_upload.setOnClickListener(new View.OnClickListener( )
								{
									@Override
									public void onClick( View v )
									{
										if( CURRENT_WORD_PKG_COUNT[0] < MAX_WORD_PKG ) //only allow if user did not reach max file upload
										{
											Intent uploadActivityIntent = new Intent( MainActivity.this, UploadActivity.class );
											startActivity( uploadActivityIntent );
										}
										else
										{
											Toast.makeText( MainActivity.this, "Maximum Upload Number of " + MAX_WORD_PKG  + " reached. Please delete a file first.", Toast.LENGTH_LONG ).show( );
										}
									}
								}
		);
	
	
	
	
		/* create Scrollable Radio Group View so user can select which Vocabulary package to practice */
	
	
	
	
	
	
	
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
					gameActivity.putExtra("state", state);
					finish( );
					startActivity( gameActivity );
				}
			}
		);
		Button btnResume = (Button) findViewById(R.id.button_resume);
		btnResume.setEnabled(false); //block Resume button unless a previous game is saved

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







































