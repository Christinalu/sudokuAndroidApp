package com.omicron.android.cmpt276_1191e1_omicron;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.omicron.android.cmpt276_1191e1_omicron.Controller.MiniGameActivity;
import com.omicron.android.cmpt276_1191e1_omicron.Controller.EventActivity;
import com.omicron.android.cmpt276_1191e1_omicron.Controller.RemoveActivity;
import com.omicron.android.cmpt276_1191e1_omicron.Controller.UploadActivity;
import com.omicron.android.cmpt276_1191e1_omicron.Model.Pair;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
{
	/*
	 *  This Main Activity is the activity that will act as the Start Menu
	 */

	RadioGroup Difficulty;
	RadioGroup Language;
	RadioGroup Mode;
	RadioGroup Size;
	private ProgressBar Progress;
	private int usrModePref = 0; // 0=standard, 1=speech
	private int usrLangPref = 0; // 0=eng_fr, 1=fr_eng; 0 == native(squares that cannot be modified); 1 == translation(the words that the user inserts)
	private int usrDiffPref; //0=easy,1=medium,2=difficult
	private int state = 0; //0=new start, 1=resume
	private String language = "nomatch";
	private String STTlanguage = "nomatch";
	private boolean canStart = true;
	private int[] usrPuzzleTypePref = {-1}; //determines if it is a 4x4, 6x6, 9x9 or 12x12 sudoku puzzle
	private RadioGroup pkgRadioGroup;
	private Boolean pressOK = false;

	WordArray wordArrayResume;
	String[] numArrayResume;
	int[] orderArrResume;
	private int usrLangPrefResume;
	private SudokuGenerator usrSudokuArrResume;
	int usrModePrefResume;
	String languageResume;
	String STTlanguageResume;
	private Pair currentRectColoured = new Pair( -1, -1 ); // stores the current coloured square
	private int currentSelectedIsCorrect=0;

	//used only for user entering language check//convert to appropriate tag
	private TextToSpeech lTTS;
	Locale[] thelocale;
	private List<String> lTTSlanguage = new ArrayList<String>();
	private List<String> lTTScountry = new ArrayList<String>();
	private List<String> lTTSlangTags = new ArrayList<String>();
	private List<Locale> localeList = new ArrayList<Locale>();

	private WordPackageFileIndex wordPackageFileIndexArr; //stores word packages name and internal file name
	private int MAX_WORD_PKG = 50; //max word packages user is allowed to import
	private int MAX_CSV_ROW = 150; //allow up to 150 pairs per package; IMPORTANT: because of WordArray.selectWord(), too many words may cause an error
	private int MIN_CSV_ROW = 12; //minimum number of words required in file
	private int CURRENT_WORD_PKG_COUNT = 0; //stores current number of packages the user has uploaded
	private int HINT_CLICK_TO_MAX_PROB = 15; //defines how many HintClicks are required for a word to reach MAX_WORD_UNIT_LIMIT
	private FileCSV fileCSV; //object containing CSV functions
	private int[] indexOfRadBtnToRemove = { -1 }; //which radio btn to remove
	private boolean[] removeBtnEnable = { true }; //when false, do not allow "REMOVE PKG" button (required because GameActivity may be using that file to save "Hint Click")

	private static final int MINI_GAME_REQUEST_CODE = 5;
	private boolean resumingMiniGame = false; //flag when returning from MiniGameActivity to resume game

	//data saved for mini game resume
	private int[] viewInvisible;
	//private boolean allowToSelect;
	private int selectedLast;
	private String[] cardArray;
	private int[] cardKey;
	private int gridRowCount;
	private int gridColCount;
	private int size;

	private WordArray wordArray;
	/*private Word[] wordArray =new Word[]
			{
					new Word( "Un", "Un", 1, 1 ),
					new Word( "Two", "Deux", 2, 1 ),
					new Word( "Three", "Trois", 3, 1 ),
					new Word( "Four", "Quatre", 4, 1 ),
					new Word( "Five", "Cinq", 5, 1 ),
					new Word( "Six", "Six", 6, 1 ),
					new Word( "Seven", "Sept", 7, 1 ),
					new Word( "Eight", "Huit", 8, 1 ),
					new Word( "Nine", "Neuf", 9, 1 ),
					new Word( "en-US", "fr-FR", -1, -1 ), //lang
					new Word( "pkg_n.csv", "", -1, -1 ) //pkg name
			};*/

	//data for Calendar event
	public String modeName = "Standard";
	private String packageName = "0-30 Numbers";
	private ImageButton calendar_button;
	private Calendar mCalendar;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
	private Date curDate;
	private Double progress_percentage;

	// TODO: separate all of Intent activity.putExtra( ) outside of MainActivity in different functions

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_main );
		if (savedInstanceState != null) {
			savetheInstanceState(0, savedInstanceState, state, wordArrayResume, usrLangPrefResume, usrSudokuArrResume, usrModePrefResume, languageResume, STTlanguageResume, numArrayResume, orderArrResume, 0, currentRectColoured, currentSelectedIsCorrect, resumingMiniGame, viewInvisible, selectedLast, cardArray, cardKey, gridRowCount, gridColCount, size);
		}

		Progress=(ProgressBar)findViewById(R.id.progressBar) ;
		//Progress.setProgress(10);


		fileCSV = new FileCSV( MAX_WORD_PKG, MAX_CSV_ROW, MIN_CSV_ROW );

		pkgRadioGroup = findViewById( R.id.pkg_radio_group ); //stores all the radio buttons with file names
		int res = checkIfJustInstalledAndSetUpPackagesAlreadyInstalled( );

		if( res != 0 ) //some exception occurred
		{ return; }


		// SET LISTENERS TO WHICH PKG IS SELECTED //

		pkgRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int pkgSelectId = group.getCheckedRadioButtonId();
				RadioButton radBtnSelected = findViewById(pkgRadioGroup.getCheckedRadioButtonId());
                packageName = ((RadioButton)findViewById(checkedId)).getText().toString();
				String fileNameSelected = wordPackageFileIndexArr.getPackageFileAtIndex(pkgRadioGroup.indexOfChild(radBtnSelected)).getInternalFileName(); //get pkg internal file name to find csv
                updateProgressBar(fileNameSelected);

                switch(pkgSelectId)
				{
					//void
				}
			}
		});


		Log.d( "resume", "state: " + state );


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
											  uploadActivityIntent.putExtra( "MIN_CSV_ROW", MIN_CSV_ROW );

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



		lTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.SUCCESS) {
					thelocale = Locale.getAvailableLocales();
					String TTSlanguage;
					String TTScountry;
					for (Locale LO : thelocale) {
						int res = lTTS.isLanguageAvailable(LO);
						if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
							//store all available locales as Locale type
							localeList.add(LO);
							//store all available language tag (String)
							lTTSlangTags.add(LO.toLanguageTag());
							//store all available locales in language - country format (strings)
							TTSlanguage = LO.getDisplayLanguage();
							lTTSlanguage.add(TTSlanguage);
							TTScountry = LO.getDisplayCountry();
							lTTScountry.add(TTScountry);
							//Log.d("lTTS", "Language and Country is: "+lTTSlanguage.get(counter)+" - "+lTTScountry.get(counter));
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
		Button btnStart = findViewById( R.id.button_start );
		btnStart.setOnClickListener( new View.OnClickListener(  ) {
                                         @Override
                                         public void onClick(View v) {
                                             // Select Package from main activity
                                             RadioButton radBtnSelected = findViewById(pkgRadioGroup.getCheckedRadioButtonId());
                                             String fileNameSelected = wordPackageFileIndexArr.getPackageFileAtIndex(pkgRadioGroup.indexOfChild(radBtnSelected)).getInternalFileName(); //get pkg internal file name to find csv
                                             startDialog(fileNameSelected);
                                         }
                                     });


		// SET UP CALENDAR DIALOG

		calendar_button = findViewById(R.id.calendar_button);
		calendar_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				curDate = new Date();
				Intent eventActivityIntent = new Intent(MainActivity.this, EventActivity.class);
				eventActivityIntent.putExtra("ActivityDate", dateFormat.format(curDate));
				eventActivityIntent.putExtra("PackageName", packageName);
				eventActivityIntent.putExtra("ifFinished", state);
				eventActivityIntent.putExtra("modeSelect", modeName);
				eventActivityIntent.putExtra("Progress", progress_percentage);
				startActivity(eventActivityIntent);
			}
		});

	

		//implement STOP btn
		Button btnStop = (Button) findViewById( R.id.button_stop );
		btnStop.setOnClickListener( new View.OnClickListener(  )
									{
										@Override
										public void onClick( View v )
										{
											state = 0;
											resumingMiniGame = false;
											btn_remove.setEnabled( true ); //allow user to remove pkg
											removeBtnEnable[0] = true;
											Button btnResume = findViewById( R.id.button_resume );
											btnResume.setEnabled( false );
										}
									}
		);

		Log.d( "cardArray", "state: "+state+" resumingMiniGame: "+resumingMiniGame );
		
		Button btnResume = (Button) findViewById(R.id.button_resume);
		if (state == 0 && resumingMiniGame == false ) {
			btnResume.setEnabled(false); //block Resume button unless a previous game is saved
			//DISABLE "REMOVE PKG" button when game is started
			removeBtnEnable[0] = true;
			
			Log.d( "cardArray", "disabling btn resume..." );
		}
		else {
			removeBtnEnable[0] = false;
		}


		//if resume button is unblocked and pressed, it will load previous game preferences
		btnResume.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state > 0) {

					Log.d( "resume", "resuming sudoku game..." );

					//load previous game preferences to prepare for export to new Game Activity
					Intent resumeActivity = new Intent(MainActivity.this, GameActivity.class);
					//save preferences for Game Activity to read
					resumeActivity.putExtra("wordArray", wordArrayResume);
					resumeActivity.putExtra("usrLangPref", usrLangPrefResume);
					resumeActivity.putExtra("SudokuArr", usrSudokuArrResume);
					resumeActivity.putExtra("state", state);
					resumeActivity.putExtra("usrMode", usrModePrefResume);
					if (usrModePrefResume == 1) {
						resumeActivity.putExtra("numArray", numArrayResume);
						resumeActivity.putExtra("orderArr", orderArrResume);
					}
					resumeActivity.putExtra("language", languageResume);
					resumeActivity.putExtra("STTlanguage", STTlanguageResume);
					resumeActivity.putExtra( "HINT_CLICK_TO_MAX_PROB", HINT_CLICK_TO_MAX_PROB );
					resumeActivity.putExtra("currentRectColoured", currentRectColoured);
					resumeActivity.putExtra("currentSelectedIsCorrect", currentSelectedIsCorrect);
					startActivityForResult(resumeActivity, 0);
				}
				else if( resumingMiniGame == true )
				{
					Log.d( "resume", "resuming mini game..." );

					// RESUME MINI GAME
					Intent resumeIntent = new Intent( MainActivity.this, MiniGameActivity.class );

					//CardView data
					resumeIntent.putExtra( "viewInvisible", viewInvisible );
					//resumeIntent.putExtra( "allowToSelect", allowToSelect );
					resumeIntent.putExtra( "selectedLast", selectedLast );

					//CardArray data
					resumeIntent.putExtra( "resumeGame", true ); //resume existing game flag
					resumeIntent.putExtra( "cardArray", cardArray );
					resumeIntent.putExtra( "cardKey", cardKey );
					resumeIntent.putExtra( "gridRowCount", gridRowCount );
					resumeIntent.putExtra( "gridColCount", gridColCount );
					resumeIntent.putExtra( "size", size );

					startActivityForResult( resumeIntent,  MINI_GAME_REQUEST_CODE );
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

		FileCSV fileCSV = new FileCSV( MAX_WORD_PKG, MAX_CSV_ROW, MIN_CSV_ROW );
		int CURRENT_WORD_PKG_COUNT_RETURN;
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


		//update scroll pkg view
		updatePkgViewAfterUploadOrRemoval( CURRENT_WORD_PKG_COUNT_RETURN );

		Log.d( "upload", "onStart() called from MainActivity" );
		Log.d( "resume", "mainActivity onStart() state: " + state );

	}

	@Override
	public void onDestroy() {
		if (lTTS != null) {
			lTTS.stop();
			lTTS.shutdown();
		}
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent resumeSrc) {
		if (resumeSrc != null) {

			Log.d( "resume", "resuming data..." );

			//if all necessary game preferences are written in memory, then unblock resume button
			if (resumeSrc.hasExtra("wordArray") && resumeSrc.hasExtra("usrLangPref") && resumeSrc.hasExtra("SudokuArr") && resumeSrc.hasExtra("usrMode") && resumeSrc.hasExtra("language")) {
				Log.i("TAG", "resumeSrc has all elements");
				state = (int) resumeSrc.getSerializableExtra("state");
				wordArrayResume = (WordArray) resumeSrc.getParcelableExtra("wordArray");
				usrLangPrefResume = (int) resumeSrc.getSerializableExtra("usrLangPref");
				usrSudokuArrResume = (SudokuGenerator) resumeSrc.getSerializableExtra("SudokuArr");
				usrModePrefResume = (int) resumeSrc.getSerializableExtra("usrMode");
				if (usrModePrefResume == 1) {
					numArrayResume = (String[]) resumeSrc.getStringArrayExtra("numArray");
					orderArrResume = (int[]) resumeSrc.getIntArrayExtra("orderArr");
				}
				languageResume = (String) resumeSrc.getSerializableExtra("language");
				STTlanguageResume = (String) resumeSrc.getSerializableExtra("STTlanguage");
				currentRectColoured = (Pair) resumeSrc.getSerializableExtra("currentRectColoured");
				currentSelectedIsCorrect = (int) resumeSrc.getSerializableExtra("currentSelectedIsCorrect");
				Button btnResume = (Button) findViewById(R.id.button_resume);
				btnResume.setEnabled(true);
				Button btnRemove = (Button) findViewById(R.id.btn_remove); //block user from deleting pkg while playing game
				btnRemove.setEnabled(false);
				usrLangPref = 0;
				usrModePref = 0;
				usrDiffPref = 0;
				usrPuzzleTypePref[0] = 0;
				removeBtnEnable[0] = false;
			}
			else if( requestCode == MINI_GAME_REQUEST_CODE )
			{
				if( resultCode == Activity.RESULT_OK )
				{
					Log.d( "resume", "resuming from mini game... (saving data)" );

					// TODO: save all intent data when rotating
					// TODO: add data to buttonResume + gameStop button
					// TODO: test resume/stop btn if they are disabled properly

					//set flag to resume game
					resumingMiniGame = (boolean) resumeSrc.getSerializableExtra( "resumeGame" );

					//save data for mini game
					//CardView
					viewInvisible = (int[]) resumeSrc.getSerializableExtra( "viewInvisible" );
					//allowToSelect = (boolean) resumeSrc.getSerializableExtra( "allowToSelect" );
					selectedLast = (int) resumeSrc.getSerializableExtra( "selectedLast" );

					//CardArray
					cardArray = (String[]) resumeSrc.getSerializableExtra( "cardArray" );
					cardKey = (int[]) resumeSrc.getSerializableExtra( "cardKey" );
					gridRowCount = (int) resumeSrc.getSerializableExtra( "gridRowCount" );
					gridColCount = (int) resumeSrc.getSerializableExtra( "gridColCount" );
					size = (int) resumeSrc.getSerializableExtra( "size" );

					//block buttons
					Button btnResume = (Button) findViewById( R.id.button_resume );
					btnResume.setEnabled( true );
					Button btnRemove = (Button) findViewById( R.id.btn_remove ); //block user from deleting pkg while playing game
					btnRemove.setEnabled( false );
					removeBtnEnable[0] = false;
				}
			}
		}
	}

	@Override
	public void onSaveInstanceState (Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savetheInstanceState(1, savedInstanceState, state, wordArrayResume, usrLangPrefResume, usrSudokuArrResume, usrModePrefResume, languageResume, STTlanguageResume, numArrayResume, orderArrResume, HINT_CLICK_TO_MAX_PROB, currentRectColoured, currentSelectedIsCorrect, resumingMiniGame, viewInvisible, selectedLast, cardArray, cardKey,
                gridRowCount, gridColCount, size);

	}


    private void updateProgressBar(String fileNameSelected) {
	    Log.d("updateProgressBar",fileNameSelected);

        int numFalseStatus = 0;
        int numTrueStatus = 0;


        try {
            //Log.d("updateProgressBar","inside try");
            FileInputStream fileInStream = openFileInput( fileNameSelected );
            //Log.d("updateProgressBar","after open");
            // READ ALL CONTENT
            InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
            BufferedReader buffRead = new BufferedReader( inStreamRead );

            //Log.d("updateProgressBar","after string allocation");
            String line;
            while( (line = buffRead.readLine()) != null )

            {
                String[] strSplit = line.split(",");
                if (strSplit.length==4){
                    boolean status = Boolean.parseBoolean(strSplit[3]);
                    Log.d("updateProgressBar:state", Boolean.toString(status));
                    if (!status){ numFalseStatus++; Log.d("updateProgressBar:false", Integer.toString(numFalseStatus)); }
                    else if (status) { numTrueStatus++; Log.d("updateProgressBar:true", Integer.toString(numTrueStatus));}
            }}

            buffRead.close( );
            Progress.setProgress(numTrueStatus);
            Progress.setMax(numFalseStatus+numTrueStatus);
			progress_percentage = (double)numTrueStatus/(numFalseStatus+numTrueStatus);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

	private int checkIfJustInstalledAndSetUpPackagesAlreadyInstalled( )
	{
		//return 0 on success

		/* TEST IF USER JUST INSTALLED APP - IF USER HAS, LOAD DEFAULT FILES */
		/* ELSE, SET UP wordPackageFileIndexArr to store all packages so far, and create Package Scroll */

		int usrNewInstall = fileCSV.checkIfCurrentWordPkgCountFileExists( this ); //0==files already exist

		if( usrNewInstall == 0 ) //if app was already installed and has correct files - get current_word_pkg_count
			try {
				CURRENT_WORD_PKG_COUNT = fileCSV.findCurrentPackageCount( this ); //get current Packages count so far
			} catch (IOException e) {
				e.printStackTrace();
				return 1;
			}
		else //fresh app install
		{
			try
			{
				fileCSV.importDefaultPkg( this ); //load default Word Package
				CURRENT_WORD_PKG_COUNT = fileCSV.findCurrentPackageCount( this ); //get current Packages count so far
			} catch( IOException e ) {
				e.printStackTrace( );
				return 1;
			}
		}

		// READ ALL PACKAGES THE USER HAS UPLOADED SO FAR, and get an array with name and file
		try {
			wordPackageFileIndexArr = new WordPackageFileIndex( this, MAX_WORD_PKG, CURRENT_WORD_PKG_COUNT ); //allow a maximum of X packages
		} catch( IOException e ){
			e.printStackTrace( );
			return 1;
		}

		/* UPDATE THE WORD PKG SCROLL */

		RadioButton radBtn;
		ColorStateList colorStateList = new ColorStateList(
				new int[][]{
						new int[]{android.R.attr.state_enabled}, //enabled
						new int[]{android.R.attr.state_enabled} //disabled

				},
				new int[]{R.color.navy, R.color.white}
		);
		for( int i=0; i<CURRENT_WORD_PKG_COUNT; i++ )
		{
			radBtn = new RadioButton( this );

			radBtn.setText( wordPackageFileIndexArr.getPackageFileAtIndex( i ).getWordPackageName( ) );
			radBtn.setButtonTintList(colorStateList);

			Log.d( "fileCSV", wordPackageFileIndexArr.getPackageFileAtIndex( i ).getWordPackageName( )
					+ " word pair count: " + wordPackageFileIndexArr.getPackageFileAtIndex( i ).getPackageWordPairCount() );

			pkgRadioGroup.addView(radBtn);
		}

		//automatically select first button
		( (RadioButton) (pkgRadioGroup.getChildAt(0)) ).setChecked( true );
		updateProgressBar(wordPackageFileIndexArr.getPackageFileAtIndex( 0 ).getInternalFileName());

		return 0;
	}


	private void updatePkgViewAfterUploadOrRemoval( int CURRENT_WORD_PKG_COUNT_RETURN )
	{
		/*
		 * After user returns from Uploading or Removing Activity
		 * This function updates the user Scroll View with what packages are available after removal/upload
		 */
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
		pkgRadioGroup = findViewById(R.id.pkg_radio_group);
		RadioButton radBtnSelected1 = findViewById(pkgRadioGroup.getCheckedRadioButtonId());
		String fileNameSelected1 = wordPackageFileIndexArr.getPackageFileAtIndex(pkgRadioGroup.indexOfChild(radBtnSelected1)).getInternalFileName(); //get pkg internal file name to find csv
		updateProgressBar(fileNameSelected1);
	}
	public void savetheInstanceState (int RorS, Bundle savedInstanceState, int sis_state, WordArray sis_wordArray, int sis_usrLangPref, SudokuGenerator sis_usrSudokuArr, int sis_usrModePref, String sis_language, String sis_STTlanguage, String[] sis_numArray, int [] sis_orderArr, int sis_HCTMP, Pair sis_currentRectColoured, int sis_currentSelectedIsCorrect, boolean sis_resumingMiniGame,
                                      int[] sis_viewInvisible, int sis_selectedLast, String[] sis_cardArray, int[] sis_cardKey,
                                      int sis_gridRowCount, int sis_gridColCount, int sis_size) {
		
		Log.d( "cardArray", "data for rotation: state "+state+" resumingMiniGame "+resumingMiniGame);
		
		if (RorS == 0) {
			//we are receiving
			state = (int) savedInstanceState.getSerializable("state");
			resumingMiniGame = (boolean) savedInstanceState.getSerializable( "resumeGame" ); //get 'resume mini game' flag

			if (state > 0) {
				wordArrayResume = (WordArray) savedInstanceState.getParcelable("wordArray");
				usrLangPrefResume = savedInstanceState.getInt("usrLangPref");
				usrSudokuArrResume = (SudokuGenerator) savedInstanceState.get("SudokuArr");
				usrModePrefResume = (int) savedInstanceState.getSerializable("usrMode");
				languageResume = (String) savedInstanceState.getSerializable("language");
				STTlanguageResume = (String) savedInstanceState.getSerializable("STTlanguage");
				HINT_CLICK_TO_MAX_PROB = (int) savedInstanceState.getSerializable("HINT_CLICK_TO_MAX_PROB");
				currentRectColoured = (Pair) savedInstanceState.getSerializable("currentRectColoured");
				currentSelectedIsCorrect = (int) savedInstanceState.getSerializable("currentSelectedIsCorrect");
				if (usrModePrefResume == 1) {
					numArrayResume = (String[]) savedInstanceState.getSerializable("numArray");
					orderArrResume = (int[]) savedInstanceState.getSerializable("orderArr");
				}
			}
			else if( resumingMiniGame == true ) //restore data from mini game when rotating
			{
				viewInvisible = (int[]) savedInstanceState.getSerializable( "viewInvisible" );
				selectedLast = (int) savedInstanceState.getSerializable( "selectedLast" );
				cardArray = (String[]) savedInstanceState.getSerializable( "cardArray" );
				cardKey = (int[]) savedInstanceState.getSerializable( "cardKey" );
				gridRowCount = (int) savedInstanceState.getSerializable( "gridRowCount" );
				gridColCount = (int) savedInstanceState.getSerializable( "gridColCount" );
				size = (int) savedInstanceState.getSerializable( "size" );
			}
		}
		else {
			//we are sending
			savedInstanceState.putInt("state", sis_state);
			savedInstanceState.putSerializable( "resumeGame", sis_resumingMiniGame ); //save 'resume mini game' flag

			if (sis_state > 0) {
				//if there was a previous game, load it's contents
				savedInstanceState.putParcelable("wordArray", sis_wordArray);
				savedInstanceState.putInt("usrLangPref", sis_usrLangPref);
				savedInstanceState.putSerializable("SudokuArr", sis_usrSudokuArr);
				savedInstanceState.putInt("usrMode", sis_usrModePref);
				savedInstanceState.putString("language", sis_language);
				savedInstanceState.putString("STTlanguage", sis_STTlanguage);
				savedInstanceState.putInt( "HINT_CLICK_TO_MAX_PROB", sis_HCTMP );
				savedInstanceState.putSerializable("currentRectColoured", sis_currentRectColoured);
				savedInstanceState.putSerializable("currentSelectedIsCorrect", sis_currentSelectedIsCorrect);

				if (sis_usrModePref == 1) {
					savedInstanceState.putStringArray("numArray", sis_numArray);
					savedInstanceState.putIntArray("orderArr", sis_orderArr);
				}
			}
			else if( sis_resumingMiniGame == true ) //save data for mini game when rotating
			{
				savedInstanceState.putSerializable( "viewInvisible", sis_viewInvisible );
				savedInstanceState.putSerializable( "selectedLast", sis_selectedLast );
				savedInstanceState.putSerializable( "cardArray", sis_cardArray );
				savedInstanceState.putSerializable( "cardKey", sis_cardKey );
				savedInstanceState.putSerializable( "gridRowCount", sis_gridRowCount );
				savedInstanceState.putSerializable( "gridColCount", sis_gridColCount );
				savedInstanceState.putSerializable( "size", sis_size );
			}
		}
	}
	public void gameSetup(Intent gA, int gs_state, WordArray gs_wordArray, int gs_usrLangPref, int gs_usrDiffPref, int gs_usrModePref, String gs_language, String gs_STTlanguage, int gs_usrPuzzleTypePref, int HCTMP) {
		gA.putExtra("state", gs_state);
		gA.putExtra( "wordArray", gs_wordArray );
		gA.putExtra( "usrLangPref", gs_usrLangPref );
		gA.putExtra("usrDiffPref",gs_usrDiffPref);
		gA.putExtra("usrMode", gs_usrModePref);
		gA.putExtra("language", gs_language);
		gA.putExtra("STTlanguage", gs_STTlanguage);
		gA.putExtra("usrPuzzSize", gs_usrPuzzleTypePref);
		gA.putExtra( "HINT_CLICK_TO_MAX_PROB", HCTMP );
	}

	private void startDialog(final String fileNameSelected){

		final View view = getLayoutInflater().inflate(R.layout.activity_sub_menu, null);
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this,R.style.Theme_AppCompat_DayNight_Dialog_Alert);
		alertDialog.setView(view);
		
		Log.d( "cardArray", "calling sub menu: state "+state+" resumingMiniGame "+resumingMiniGame);
		
		usrPuzzleTypePref[0] = -1;
		usrModePref = 0;

		// CHOOSE THE DIFFICULTY
		Difficulty = view.findViewById(R.id.button_level);
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
		Language = view.findViewById(R.id.button_language);
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
		Mode = view.findViewById(R.id.button_mode);
		Mode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int ModeId = group.getCheckedRadioButtonId();
				modeName = ((RadioButton)findViewById(checkedId)).getText().toString();
				switch (ModeId)
				{
					case R.id.button_mStandard:
						usrModePref = 0;
						break;

					case R.id.button_mSpeech:
						usrModePref = 1;
						break;

					case R.id.button_mini_game:
						usrModePref = 3;
						break;
				}
			}
		});

		// CHOOSE THE SIZE OF PUZZLE
		Size = view.findViewById(R.id.btn_type);
		Size.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId){
					case R.id.btn_4x4:
						usrPuzzleTypePref[0] = 0;
						break;
					case R.id.btn_6x6:
						usrPuzzleTypePref[0] = 1;
						break;
					case R.id.btn_9x9:
						usrPuzzleTypePref[0] = 2;
						break;
					case R.id.btn_12x12:
						usrPuzzleTypePref[0] = 3;
						break;
				}
			}
		});

		alertDialog.setNegativeButton(android.R.string.cancel, null);

		alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				state = 0;
				wordArray = new WordArray( usrPuzzleTypePref[0], MAX_CSV_ROW, HINT_CLICK_TO_MAX_PROB, usrModePref );

				try {
					//based on pkg, initialize the wordArray (select 'n' words)

					int res = wordArray.initializeWordArray(MainActivity.this, fileNameSelected);
					if (res == 1 && usrModePref != 3) {
						Log.d("upload", "ERROR: initializeWordArray( ) returned an error");
						Toast.makeText(MainActivity.this, "Please select one of the Puzzle Types to start", Toast.LENGTH_SHORT).show();
						return; //error: could not initialize wordArray
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				Intent gameActivity;
				if( usrModePref == 3 ){ //if mini-game mode
					gameActivity = new Intent(MainActivity.this, MiniGameActivity.class);
				}
				else{
					gameActivity = new Intent(MainActivity.this, GameActivity.class);
				}
				//check to see for language format is correct and available
				if (usrModePref == 1 || usrModePref == 0) {
					if (usrLangPref == 0) {
						language = wordArray.getTranslationLang();
                        STTlanguage = wordArray.getNativeLang();
						Log.e("lTTSs", "language is: "+language);
					}
					else {
						language = wordArray.getNativeLang();
                        STTlanguage = wordArray.getTranslationLang();
						Log.e("lTTSs", "language is: "+language);
					}
					canStart = false;
                    for (int i=0; i<lTTSlangTags.size(); i++) {
                        //Log.d("lTTS", "language is: "+language+" langTag is: "+lTTSlangTags.get(i));
                        if (language.equalsIgnoreCase(lTTSlanguage.get(i))) {
                            language = lTTSlangTags.get(i);
                            canStart = true;
                            break;
                        }
                    }
                    //get langtag for other language for STT
                    boolean matchFound = false;
                    for (int i=0; i<lTTSlangTags.size(); i++) {
                        //Log.d("lTTS", "language is: "+language+" langTag is: "+lTTSlangTags.get(i));
                        if (STTlanguage.equalsIgnoreCase(lTTSlanguage.get(i))) {
                            STTlanguage = lTTSlangTags.get(i);
                            matchFound = true;
                            break;
                        }
                    }
                    if (!matchFound) {
                        STTlanguage = "nomatch";
                    }
                    if (usrModePref == 1) {
                        if (canStart) {
                            //save wordArray for Game Activity
                            state = 0;
                            gameSetup(gameActivity, state, wordArray, usrLangPref, usrDiffPref, usrModePref, language, STTlanguage, usrPuzzleTypePref[0], HINT_CLICK_TO_MAX_PROB);
                            startActivityForResult(gameActivity, 0);
                        }
                        else {
                            Toast.makeText(view.getContext(),R.string.no_language, Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        //standard start
                        state = 0;
                        gameSetup(gameActivity, state, wordArray, usrLangPref, usrDiffPref, usrModePref, language, STTlanguage, usrPuzzleTypePref[0], HINT_CLICK_TO_MAX_PROB);
                        startActivityForResult(gameActivity,0);
                    }
				}
                else //usrModPref == 3
				{
					Log.d( "resume", "mini game mode called..." );

					// MINI GAME MODE

					//gameSetup( gameActivity, state, wordArray, usrLangPref, usrDiffPref, usrModePref, language, usrPuzzleTypePref[0], HINT_CLICK_TO_MAX_PROB );
					gameActivity.putExtra( "wordArray", wordArray );
					gameActivity.putExtra( "resumeGame", false ); //resume flag - new game
					startActivityForResult( gameActivity, MINI_GAME_REQUEST_CODE );
				}
			}
		});

		AlertDialog dialog = alertDialog.create();
		dialog.show();
		Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		//positiveButton.setBackground(getResources().getDrawable(R.drawable.buttons));
		positiveButton.setTextColor(getResources().getColor(R.color.glacierblue));
		Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		negativeButton.setTextColor(getResources().getColor(R.color.colorAccent));
		//negativeButton.setBackground(getResources().getDrawable(R.drawable.buttons));
		dialog.getWindow().getDecorView().setBackground(getResources().getDrawable(R.drawable.background));
		//dialog.getWindow().setBackgroundDrawable(R.drawable.background);

	}

}









