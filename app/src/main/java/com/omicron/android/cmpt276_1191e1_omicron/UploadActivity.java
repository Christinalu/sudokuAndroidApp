package com.omicron.android.cmpt276_1191e1_omicron;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UploadActivity extends AppCompatActivity
{
	String fileSelected; //stored the file name user selected to upload	private int READ_REQUEST_CODE = 0;
	final int PERMISSION_REQUEST_CODE = 0;
	int permissionStorage = 0; //PERMISSION_DENIED means permission denied
	int WRITE_REQUEST_CODE = 5;
	int SAVE_REQUEST_CODE = 4;
	private int MAX_WORD_PKG; //maximum number of pkgs the user allowed to upload
	private int READ_REQUEST_CODE = 0;
	private int[] fileSaved = { 0 }; //0 means there was an error - file not loaded
	private int CURRENT_WORD_PKG_COUNT = 0; //stores current number of packages the user has uploaded
	private  FileCSV fileCSV; //object for using CSV functions
	// TODO: check that above fileCSV does not interfere with fileCSV in MainActivity
	private int STATISTIC_MULTIPLE = 2; //double user Hint clicks to make it more likely for word to be shown
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_upload );
		
		//get data from previous activity
		Intent extra = getIntent( );
		MAX_WORD_PKG = (int) extra.getSerializableExtra( "MAX_WORD_PKG" );
		
		Log.d( "upload", "in UploadAct MAX_WORD_PKG: " + MAX_WORD_PKG );
		Log.d( "upload", "in UploadAct CURRENT_WORD_PKG_COUNT[0]: " + CURRENT_WORD_PKG_COUNT );
		
		fileCSV = new FileCSV( MAX_WORD_PKG );
		
		
		Log.d( "upload", "onCreate for UploadACt called" );
		
		final TextView textView = findViewById( R.id.selected_file );
		//final FileCSV validaveCSV = new FileCSV( );
		
		//
		//	IN THIS ACTIVITY ADD INSTRUCTIONS TO USER
		//
		
		// NOTE: xml EditText fields have restricted characters
		// 		notably commas are restricted especially in "Word Package Name" not to interfier with CSV format
		
		// TODO: 	## IMPORTANT ##
		// TODO: dont forget to use STATISTIC_MULTIPLE of 2, to double the chances of word appearing
		// TODO:	- ie if "Hint clicks" was 8 in a game, then double it to 16 when saving to file to make it more likely for word to reappear
		
		
		
		//get text field id
		final EditText editTextPkgName = (EditText) findViewById( R.id.editText_pkg_name );
		//final EditText editTextNative = (EditText) findViewById( R.id.editText_native_name );
		//final EditText editTextTranslation = (EditText) findViewById( R.id.editText_translation_name );
		
		
		
			/* UPLOAD CSV FILE BUTTON */
		Button uploadFileBtn = findViewById( R.id.upload_file );
		uploadFileBtn.setOnClickListener(new View.OnClickListener( )
				{
					@Override
					public void onClick( View v )
					{
						//here check WordPackage name is not empty and <= 35 char
						
						// NOTE: is the above done in readCSVUri( ), so no need to do it here ????
						
						//update how many pkgs so far
						try {
							CURRENT_WORD_PKG_COUNT = fileCSV.findCurrentPackageCount( UploadActivity.this );
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						//NOTE: CURRENT_WORD_PKG has to be array in btn
						if( CURRENT_WORD_PKG_COUNT < MAX_WORD_PKG ) //only allow if user did not reach max file upload
						{
							//check if fields are not empty
							if (TextUtils.isEmpty(editTextPkgName.getText()) ) //if field empty
							{
								Toast.makeText(UploadActivity.this, "All text fields must not be empty", Toast.LENGTH_LONG).show();
								
							} else //fields contain at least 1 char
							{
								String pkgName = (editTextPkgName.getText( )).toString( ); //get the pkg name the user inserted
								
								Log.d( "upload", "pkg_name: " + pkgName );
								
								// check if user inserted pkg name does not already exist
								int unique = 1; // 1 means not unique
								try {
									unique = fileCSV.checkUsrPkgNameForUnique( UploadActivity.this, pkgName, CURRENT_WORD_PKG_COUNT );
									Log.d( "upload", "testing pkg name uniqueness" );
									Log.d( "upload", "if unique pkg_name: " +unique );
								} catch (FileNotFoundException e) {
									e.printStackTrace();
									Log.d( "upload", "ERROR: exception occured in SAVE CSV FILE onClick()" );
								} catch (IOException e) {
									Log.d( "upload", "ERROR: exception occured in SAVE CSV FILE onClick()" );
									e.printStackTrace();
								}
								
								if( unique == 0 ){
									findFileCSV(pkgName);
								}
								else{
									Toast.makeText( UploadActivity.this, "The Package name \"" + pkgName + "\" seems to already exist", Toast.LENGTH_LONG ).show( );
								}
							}
						}
						else
						{
							Toast.makeText( UploadActivity.this, "Maximum Upload Number of " + MAX_WORD_PKG  + " reached. Please delete a Word Package first.", Toast.LENGTH_LONG ).show( );
						}
						// TODO: also check that a internal file does not exist with same name, otherwise overwrite
						// TODO: 	use scheme: package_1.csv package_2.csv... reason being can use loop to increment file name until a non-duplicate name found
					
						// TODO: dont forget to check when user provides Native and Translation Language names, remove all commas AND limit to 35 char + non-empty
						// TODO:	- check if EditText (in xml) fields are filled
						
						// TODO: use "long" instead of "int" to store total 'n' "Hint clicks" ie:  long index = Rand r % (long) n; (here n is total "hint" clicks)
						// TODO: test if when at MAX_PKG, if IN THIS ACTIVITY pressing "upload CSV" btn, should not upload
					}
				}
		);
		
		
	}
	
	
	
	
	
	
	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent resultData )
	{
			/* DEFINE HOW ACTION_GET_CONTENT GETS FILE ON INTENT RETURN */
		
		if( requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK ) //check for correct INTENT and if result OK
		{
			Uri uri = null;
			if (resultData != null)
			{
				uri = resultData.getData( ); //get data from URI (abstract object reference) path
				
				try //URI may throw exception
				{
					// get .csv file content as a String
					String[] contentCSV = fileCSV.readCSVUri( this, uri );
					
					Log.d( "upload", "contentCSV: " + contentCSV );
					
					//if readCSVUri( ) returns empty string, it means the CSV is not properly formatted
					if( contentCSV[0].contentEquals( "" ) ){
						Toast.makeText(UploadActivity.this, "CSV file has incorrect formatting. File not uploaded", Toast.LENGTH_LONG).show( );
						Log.d( "upload", "incorrect CSV format" );
						return;
					}
					
					//String pkgName = (String) resultData.getSerializableExtra( "pkgName" ); //get the pkg name inserted by user
					
					//pkgName = (findViewById( R.id.editText_pkg_name ))
					EditText editTextPkgName = (EditText) findViewById( R.id.editText_pkg_name );
					String pkgName = (editTextPkgName.getText( )).toString( );
					
					Log.d( "upload", "pkg_name-2: " + pkgName );
					
					int res = fileCSV.saveCSVFile( this, contentCSV, pkgName ); //write data to new file in internal storage
					
					if( res == 0 )
					{ Toast.makeText(UploadActivity.this, "File stored in app internal storage", Toast.LENGTH_LONG).show( ); }
					else
					{ Toast.makeText(UploadActivity.this, "Something went wrong. Internal files might be incorrect", Toast.LENGTH_LONG).show( ); }
					
				}
				catch( IOException e ) //in case of error
				{
					e.printStackTrace( );
					Toast.makeText( UploadActivity.this, "ERROR: FILE UPLOAD FAIL", Toast.LENGTH_LONG ).show( );
				}
			}
		}
	}
	
	
	private void findFileCSV( String pkgName )
	{
		/*
		 * Use intent to upload file  from user phone
		 * NOTE: this function must be called from this Activity
		 */
		
		Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); //use intent to ask OS to import a file
		//Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
		
		
		intent.addCategory( Intent.CATEGORY_OPENABLE ); //only include files that are openable
		intent.setType("text/comma-separated-values"); //only allow .csv files; use "*/*" for all files
		intent.putExtra( "pkgName", pkgName ); //add pkg name inserted by user
		//intent.setType( "*/*" );
		
		startActivityForResult( intent, READ_REQUEST_CODE ); //call OS intent to import file from storage framework
		
		// NOTE: the code continues once the intent returns so go to see onActivityResult
	}
	
	
	@Override
	public void onStart( )
	{
		super.onStart( );
		//disable slide in animation
		overridePendingTransition(0, 0);
		Log.d( "TAG", "onStart( ) in GameActivity called with animation" );
		
	}
	
	
	@Override
	public void onPause( )
	{
		super.onPause( );
		//disable slide in animation
		overridePendingTransition(0, 0);
		Log.d( "TAG", "onStart( ) in GameActivity called with animation" );
		
	}
	
	
	
	
	
	
	
	// #################################################################################

	
	
	
	/*@Override
	public void onRequestPermissionsResult( int requestCode, String permissions[], int[] grantResults )
	{
		switch( requestCode )
		{
			case PERMISSION_REQUEST_CODE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d("storage", "storage permission: granted");
				} else {
					Log.d("storage", "storage permission: denied");
				}
				break;
		}
	}*/
	
	
//	private void checkStoragePermission( ) // permission not needed with "storage framework" so far
//	{
//		/* CHECK AND ASK FOR STORAGE PERMISSION */
//
//		permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//		if( permissionStorage == PackageManager.PERMISSION_GRANTED ) //check permission so far
//		{
//			//void
//			//permissionStorage[0] = ; //permission granted
//			Log.d("storage", "storage permission-2: granted");
//		}
//		else
//		{
//			permissionStorage = PackageManager.PERMISSION_DENIED; //permission denied
//			Log.d("storage", "storage permission-2: denied");
//
//		}
//
//		// if using this, also have to uncomment @Override nRequestPermissionsResult( )
//
//		if (permissionStorage == PackageManager.PERMISSION_DENIED) // if permission denied, ask for permission to use storage
//		{
//			if (ActivityCompat.shouldShowRequestPermissionRationale(this, //if user themself disabled storage access
//					Manifest.permission.READ_EXTERNAL_STORAGE))
//			{
//				Toast.makeText(this, "It appears you've denied Storage Access. External Storage needed to import CSV file. Please go to settings and allow for External Storage Access", Toast.LENGTH_LONG ).show();
//			}
//			else
//			{
//				ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
//						PERMISSION_REQUEST_CODE );
//				permissionStorage = PackageManager.PERMISSION_GRANTED;
//			}
//		}
//	}
	
//	private void oldFindFile( )
//	{
//		/*
//
//		  ## OLD FIND FILE FUNCTION ##
//
//		// Find files in Download folder //
//		String path = Environment.getExternalStorageDirectory().toString( ) + "/Download"; //get local phone storage path and go to Downloads folder
//		Log.d( "FILE", "dir path: " + path );
//		File dir = new File( path ) ; //get file and directory path names
//		File[] file = dir.listFiles( ); //save all file names (path) in an array
//		for( int i=0; i<file.length; i++ ) //for debugging
//		{
//			Log.d("FILE", "file-name: " + file[i].getName( ) );
//		}
//
//		Log.d( "FILE", "--pass" );
//
//
//		RadioGroup uploadScrollRadioGroup = (RadioGroup) findViewById( R.id.upload_scroll_RadioGroup );
//		RadioButton[] uploadRadioBtnArr = new RadioButton[file.length]; //array storing buttons corresponding to each file
//
//		//loop and create RadioButtons for all files in Download
//		for( int i=0; i<file.length && i<100; i++ )
//		{
//			uploadRadioBtnArr[i] = new RadioButton( this );
//			uploadScrollRadioGroup.addView( uploadRadioBtnArr[i] ); //add radio button to Scroll View
//			uploadRadioBtnArr[i].setText( file[i] .getName( ) ); //add file name to button
//		}
//
//
//		//test scroll
//		RadioButton[] uploadRadioBtnArr2 = new RadioButton[30]; //array storing buttons corresponding to each file
//		for( int i=0; i<30; i++ )
//		{
//			uploadRadioBtnArr2[i] = new RadioButton( this );
//			uploadScrollRadioGroup.addView( uploadRadioBtnArr2[i] ); //add radio button to Scroll View
//			uploadRadioBtnArr2[i].setText( "-- scroll test --.csv" ); //add file name to button
//		}
//		*/
//
//		/* find which button file user selected */
//		/*if( uploadRadioBtnArr.length >= 1 )
//		{
//			uploadRadioBtnArr[0].setChecked( true ); //by default, mark first file as selected
//			RadioButton checkedRadioButton = (RadioButton) uploadScrollRadioGroup.findViewById( uploadScrollRadioGroup.getCheckedRadioButtonId()) ;
//		}
//
//		// find which file user selected //
//		uploadScrollRadioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener()
//			{
//				@Override
//				public void onCheckedChanged(RadioGroup group, int checkedId) {
//					int selectedId = group.getCheckedRadioButtonId( );
//
//					RadioButton btn = findViewById( selectedId );
//					textView.setText( btn.getText( ) );
//
//					fileSelected = btn.getText( ).toString( ); //store file name
//
//					findFileCSV( );
//
//					//validaveCSV.validate( fileSelected );
//				}
//			}
//		);
//		*/
//
//
//				/*OnCheckedChangeListener() {
//		@Override
//		public void onCheckedChanged(RadioGroup group, int checkedId) {
//			int difficultyId = group.getCheckedRadioButtonId();
//			switch(difficultyId)
//			{
//				case R.id.button_easy:
//					usrDiffPref = 0;
//					break;
//
//				case R.id.button_medium:
//					usrDiffPref = 1;
//					break;
//
//				case R.id.button_hard:
//					usrDiffPref = 2;
//					break;
//			}
//		}
//	}*/
//	}
	
}


