package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
	private int CURRENT_WORD_PKG_COUNT; //stores current number of packages the user has uploaded
	
	
	// SET UP ARRAY TO STORE WORDS
	// a Word (pair) contains the word in native language, and its translation
	// note: this array will remain of size 9, and only changed when modes are switched
	//       this is required in DictionaryActivity.java
	private Word[] wordArray =new Word[]
			{
					new Word( "Un", "Un" ),
					new Word( "Two", "Deux" ),
					new Word( "Threeeeeeeeee", "Troisssssssss" ),
					new Word( "Four", "Quatre" ),
					new Word( "Five", "Cinq" ),
					new Word( "Six", "Six" ),
					new Word( "Seven", "Sept" ),
					new Word( "Eightttttttttttttt", "Huitttttttttttttt" ),
					new Word( "Nine", "Neuf" )
			};
	
	

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		
		//TEST IF USER JUST INSTALLED APP - IF USER HAS, LOAD DEFAULT FILES
		int usrNewInstall = checkForCurrentWordPkgCountFile( ); //0==files already exist
		
		
		if( usrNewInstall == 0 ) //if app was already installed and has correct files - get current_word_pkg_count
			try {
				CURRENT_WORD_PKG_COUNT = findCurrentPackageCount( ); //get current Packages count so far
				Log.d( "upload", "---- MAX_WORD_PKG: " + MAX_WORD_PKG );
				Log.d( "upload", "CURRENT_WORD_PKG_COUNT: " + CURRENT_WORD_PKG_COUNT );
			} catch (IOException e) {
				e.printStackTrace();
			}
		else
		{
			try
			{
				importDefaultPkg( ); //load default Word Package
				Log.d( "upload", "---- MAX_WORD_PKG :: FRESH INSTALL :: " + MAX_WORD_PKG );
				Log.d( "upload", "CURRENT_WORD_PKG_COUNT :: FRESH INSTALL :: " + CURRENT_WORD_PKG_COUNT );
			} catch( IOException e ) {
				e.printStackTrace( );
			}
		}
		
		///////////
		//
		//	## NEXT: finish WordPackageFileIndex( ) creating array that stores WordPkgName and internal_storage_name
		//   		 	+ add these as a .csv file in internal storage (see already created "word_pkg_name_and_file_name.csv")
		//				+ if user just installed app, create
		//
		///////////
		
		
		
		
		wordPackageFileIndexArr = new WordPackageFileIndex( MAX_WORD_PKG, CURRENT_WORD_PKG_COUNT ); //allow a maximum of X packages
		
		
		// TODO: dont forget to check is wordPackageFileIndexArr is full
		// TODO: dont forget to update raw file "current_word_pgk_count.txt" when user uploads new file
		
		// TODO: dont forget to update raw file "current_word_pgk_count.txt" when user REMOVES file
		// TODO: test if MAX_WORD_PKG limit is reached, that user cannot upload file
		
		// TODO: test user uploading multiple files in the same game
		// TODO: test when user comes back from UploadActivity that the file was added in ScrollView ie pass intent back
		// TODO: when user already has reached MAX_WORD_PKG, block user from pressing to import new file
		
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
										if( CURRENT_WORD_PKG_COUNT < MAX_WORD_PKG ) //only allow if user did not reach max file upload
										{
											Intent uploadActivityIntent = new Intent(MainActivity.this, UploadActivity.class);
											startActivity(uploadActivityIntent);
										}
										else
										{
											Toast.makeText(MainActivity.this, "Maximum Upload Number of " + MAX_WORD_PKG  + " reached. Please delete a file first.", Toast.LENGTH_LONG).show( );
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
	
		// TODO: pull first +save copy, then merge
	
		// TODO: change SDK version in gradle back to 21
	
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
		////////////////////////
	
		// TODO: when user inputing name of csv package ie "Chapter 1 Vocab", make sure to check such a package doesnt exist already
		// TODO: add instruction to tell user to insert what to call the Word Package ie "Vocab 1"
	
		
		// TODO: test app by uploading a file, selecting it, then playing game a little, then going back to main menu, then resume game - see if imported wordArray works
		// TODO: test if CURRENT_WORD_PKG_COUNT is preserved when going to GameAct and back to MainAct
	
		// TODO: IMPORTANT: forbid commas when user give WordPackageName, ie analyse and remove user's input commas using str.replace(",", "");, this is necessary so that when putting data to word_pkg_name_file_name.csv, the comma does not interfere when using comma to separate pkg_name and internal_file_name;
	
	
	
	
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
					finish();
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
	
	
	
	private int findCurrentPackageCount( ) throws IOException
	{
		/*
		 * This reads raw file resource, finds and returns an int representing how many WordPackages the user has uploaded so far
		 */

		/*InputStream inStream = getResources().openRawResource( R.raw.current_word_pkg_count ); //from RAW resource, get the current_count file
		BufferedReader buffRead = new BufferedReader( new InputStreamReader( inStream ) ); //get bytes of file

		String count = ""; //stores the count as from file as string

		count = buffRead.readLine( );

		return Integer.parseInt( count ); //convert string count to int
		*/
		
		FileInputStream fileInStream = this.openFileInput( "current_word_pkg_count.txt" ); //open file from internal storage
		InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
		BufferedReader buffRead = new BufferedReader( inStreamRead );
		String countStr = buffRead.readLine( ); //get count int as string
		
		return Integer.parseInt( countStr ); //convert string count to int
	}
	
	
	private void importDefaultPkg( ) throws IOException
	{
		/*
		 * This function is called when the user first installs the app to import a default package from resource file
		 * Note: this is different from URI in Upload Activity
		 */
		
		/* READ FILE */
		InputStream inStream = getResources().openRawResource( R.raw.pkg_1 ); //from RAW resource, get the default pkg_1,csv file
		BufferedReader buffRead = new BufferedReader( new InputStreamReader( inStream ) ); //get bytes of file
		
		String str; //store each line from .csv file
		
		StringBuilder strBuild = new StringBuilder( ); //used to concatinate all lines into single String Stream
		
		while( ( str = buffRead.readLine( ) ) != null ) //read lines from buffer until EOF
		{
			strBuild.append( str ); //append all lines to builder
			strBuild.append( "\n" ); //"new line" char important to separate rows (because it is discarded when reading line by line
		}
		
		String content = strBuild.toString( ); //get all content from file so far in a string
		
		//String localPath = Environment.getExternalStorageDirectory().toString( ); //get local phone storage path and go to Downloads folder
		
		String fileName = "pkg_1.csv";
		
		FileOutputStream outStream;
		
		try
		{
			outStream = openFileOutput( fileName, this.MODE_PRIVATE ); //open private output stream
			outStream.write( content.getBytes( ) ); //convert string to bytes and write to file
			outStream.close( ); //close and save file
		}
		catch( Exception e ) //in case of error
		{
			e.printStackTrace( );
		}
		
	}
	
	
	private int checkForCurrentWordPkgCountFile( )
	{
		/*
		 * This function attempts to access wordPkgCount file
		 * If file does not exist, it means user just downloaded the app and this function creates the wordPkgCount file
		 * returns 0 if file already existed
		 */
		
		String dirPath = getFilesDir().getAbsolutePath( ); //get path to local internal storage
		
		File dir = new File( dirPath ); //get File object storing all file names
		File[] file = dir.listFiles( ); //save all file names (path) in an array
		
		
		for( int i=0; i<file.length; i++ ) //for debugging
		{
			Log.d("upload", "file-name: " + file[i].getName( ) );
			
			if( file[i].getName().contentEquals( "current_word_pkg_count.txt" ) ) //check if such a file name already exists
			{
				//if it does exist - do nothing
				return 0;
			}
		}
		
		// NO CURRENT_WORD_PKG DETECTED - CREATE NEW FILE (storing how many packages user has uploaded)
		
		String fileName = "current_word_pkg_count.txt";
		
		FileOutputStream outStream;
		
		try
		{
			outStream = openFileOutput( fileName, this.MODE_PRIVATE ); //open private output stream
			outStream.write( ("1").getBytes( ) ); //convert string to bytes and write to file DEFAULT 1
			outStream.close( ); //close and save file
			CURRENT_WORD_PKG_COUNT = 1;
		}
		catch( Exception e ) //in case of error
		{
			e.printStackTrace( );
		}
		
		// NO word_pkg_name_and_file_name.csv DETECTED - CREATE NEW FILE (storing csv database relating user defined WorkPackageName with corresponding internal file name)
		
		fileName = "word_pkg_name_and_file_name.csv";
		
		try
		{
			outStream = openFileOutput( fileName, this.MODE_PRIVATE ); //open private output stream
			outStream.write( ("0-30 Numbers,pkg_1.csv\n").getBytes( ) ); //convert string to bytes and write to file DEFAULT 1
			outStream.close( ); //close and save file
		}
		catch( Exception e ) //in case of error
		{
			e.printStackTrace( );
		}
		
		return 1;
	}
}





































