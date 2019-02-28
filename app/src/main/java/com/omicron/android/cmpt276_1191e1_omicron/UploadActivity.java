package com.omicron.android.cmpt276_1191e1_omicron;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UploadActivity extends AppCompatActivity
{
	String fileSelected; //stored the file name user selected to upload	private int READ_REQUEST_CODE = 0;
	private int READ_REQUEST_CODE = 0;
	final int PERMISSION_REQUEST_CODE = 0;
	int permissionStorage = 0; //PERMISSION_DENIED means permission denied
	int WRITE_REQUEST_CODE = 5;
	int SAVE_REQUEST_CODE = 4;
	Uri uri2;
	private int[] fileSaved = { 0 }; //0 means there was an error - file not loaded
	private int CURRENT_WORD_PKG_COUNT; //stores current number of packages the user has uploaded
	
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_upload );
		
		final TextView textView = findViewById( R.id.selected_file );
		//final FileCSV validaveCSV = new FileCSV( );
		
		//
		//	IN THIS ACTIVITY ADD INSTRUCTIONS TO USER
		//
		
		
			/* UPLOAD CSV FILE BUTTON */
		Button uploadFileBtn = findViewById( R.id.upload_file );
		uploadFileBtn.setOnClickListener(new View.OnClickListener( )
				{
					@Override
					public void onClick( View v )
					{
						//here check WordPackage name is not empty and <= 35 char
						
						findFileCSV( );
						
						
						// TODO: also check that a internal file does not exist with same name, otherwise overwrite
						// TODO: 	use scheme: package_1.csv package_2.csv... reason being can use loop to increment file name until a non-duplicate name found
					}
				}
		);
		
		
	}
	
	
	
	private void findFileCSV(  )
	{
		// TODO: check if this function can be placed separatelly
		
		final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
		
		//checkStoragePermission( );
		
		
		//if( permissionStorage ==  PackageManager.PERMISSION_GRANTED ) // import only if permission granted
		//{
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT); //use intent to ask OS to import a file
			//Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
			
			
			intent.addCategory( Intent.CATEGORY_OPENABLE ); //only include files that are openable
			intent.setType("text/comma-separated-values"); //only allow .csv files; use "*/*" for all files
			//intent.setType( "*/*" );
			
			startActivityForResult( intent, READ_REQUEST_CODE ); //call OS intent to import file from storage framework
		
		
			/*
			if( fileSaved[0] == 1 ) //if file successfully loaded
			{
				Toast.makeText(UploadActivity.this, "File stored in app internal storage", Toast.LENGTH_LONG).show( );
				fileSaved[0] = 0;
			}
			else {
				Toast.makeText( UploadActivity.this, "ERROR: FILE UPLOAD FAIL", Toast.LENGTH_LONG ).show( );
			}*/
			
		//}
		
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
					String contentCSV = readCSVUri( uri );
					
					saveCSVFile( contentCSV ); //write data to new file in internal storage
					Toast.makeText(UploadActivity.this, "File stored in app internal storage", Toast.LENGTH_LONG).show( );
				}
				catch( IOException e ) //in case of error
				{
					e.printStackTrace( );
					Toast.makeText( UploadActivity.this, "ERROR: FILE UPLOAD FAIL", Toast.LENGTH_LONG ).show( );
				}
			}
		}
	}
	
	
	private String readCSVUri( Uri uri )throws IOException
	{
		/*
		 * Returns a string containing the .csv data
		 */
		InputStream inStream = getContentResolver().openInputStream( uri ); //create a InputStream by reading bytes
		
		InputStreamReader inStreamRead = new InputStreamReader( inStream ); //convert the bytes from InputStream to encoded char
		BufferedReader buffRead = new BufferedReader( inStreamRead ); //store string in buffer (memory) after bytes were converted to char using InputStreamReader (from InputStream)
		
		String str; //store each line from .csv file
		StringBuilder strBuild = new StringBuilder( ); //used to concatinate all lines into single String Stream
		
		while( ( str = buffRead.readLine( ) ) != null ) //read lines from buffer until EOF
		{
			strBuild.append( str ); //append all lines to builder
			strBuild.append( "\n" ); //"new line" char important to separate rows (because it is discarded when reading line by line
		}
		
		return strBuild.toString( ); //return StringBuilder as String
	}
	
	
	private void saveCSVFile( String str )
	{
		/*
		 * This function saves the content read from .csv file imported from user to local private app storage
		 */
		
		String localPath = Environment.getExternalStorageDirectory().toString( ); //get local phone storage path and go to Downloads folder
		Log.d( "storage", "local dir path: " + localPath );
		
		String fileName = "myfileTEST6";
		
		FileOutputStream outStream;
		
		try
		{
			outStream = openFileOutput( fileName, this.MODE_PRIVATE ); //open private output stream
			outStream.write( str.getBytes( ) ); //convert string to bytes and write to file
			outStream.close( ); //close and save file
		}
		catch( Exception e ) //in case of error
		{
			e.printStackTrace( );
		}
		
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
	
	
	private void checkStoragePermission( ) // permission not needed with "storage framework" so far
	{
		/* CHECK AND ASK FOR STORAGE PERMISSION */
		
		permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
		
		if( permissionStorage == PackageManager.PERMISSION_GRANTED ) //check permission so far
		{
			//void
			//permissionStorage[0] = ; //permission granted
			Log.d("storage", "storage permission-2: granted");
		}
		else
		{
			permissionStorage = PackageManager.PERMISSION_DENIED; //permission denied
			Log.d("storage", "storage permission-2: denied");
			
		}
		
		// if using this, also have to uncomment @Override nRequestPermissionsResult( )
		
		if (permissionStorage == PackageManager.PERMISSION_DENIED) // if permission denied, ask for permission to use storage
		{
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, //if user themself disabled storage access
					Manifest.permission.READ_EXTERNAL_STORAGE))
			{
				Toast.makeText(this, "It appears you've denied Storage Access. External Storage needed to import CSV file. Please go to settings and allow for External Storage Access", Toast.LENGTH_LONG ).show();
			}
			else
			{
				ActivityCompat.requestPermissions( this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE },
						PERMISSION_REQUEST_CODE );
				permissionStorage = PackageManager.PERMISSION_GRANTED;
			}
		}
	}
	
	private void oldFindFile( )
	{
		/*
		
		  ## OLD FIND FILE FUNCTION ##
		  
		// Find files in Download folder //
		String path = Environment.getExternalStorageDirectory().toString( ) + "/Download"; //get local phone storage path and go to Downloads folder
		Log.d( "FILE", "dir path: " + path );
		File dir = new File( path ) ; //get file and directory path names
		File[] file = dir.listFiles( ); //save all file names (path) in an array
		for( int i=0; i<file.length; i++ ) //for debugging
		{
			Log.d("FILE", "file-name: " + file[i].getName( ) );
		}
		
		Log.d( "FILE", "--pass" );
		
		
		RadioGroup uploadScrollRadioGroup = (RadioGroup) findViewById( R.id.upload_scroll_RadioGroup );
		RadioButton[] uploadRadioBtnArr = new RadioButton[file.length]; //array storing buttons corresponding to each file
		
		//loop and create RadioButtons for all files in Download
		for( int i=0; i<file.length && i<100; i++ )
		{
			uploadRadioBtnArr[i] = new RadioButton( this );
			uploadScrollRadioGroup.addView( uploadRadioBtnArr[i] ); //add radio button to Scroll View
			uploadRadioBtnArr[i].setText( file[i] .getName( ) ); //add file name to button
		}
		
		
		//test scroll
		RadioButton[] uploadRadioBtnArr2 = new RadioButton[30]; //array storing buttons corresponding to each file
		for( int i=0; i<30; i++ )
		{
			uploadRadioBtnArr2[i] = new RadioButton( this );
			uploadScrollRadioGroup.addView( uploadRadioBtnArr2[i] ); //add radio button to Scroll View
			uploadRadioBtnArr2[i].setText( "-- scroll test --.csv" ); //add file name to button
		}
		*/
		
		/* find which button file user selected */
		/*if( uploadRadioBtnArr.length >= 1 )
		{
			uploadRadioBtnArr[0].setChecked( true ); //by default, mark first file as selected
			RadioButton checkedRadioButton = (RadioButton) uploadScrollRadioGroup.findViewById( uploadScrollRadioGroup.getCheckedRadioButtonId()) ;
		}
		
		// find which file user selected //
		uploadScrollRadioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					int selectedId = group.getCheckedRadioButtonId( );
					
					RadioButton btn = findViewById( selectedId );
					textView.setText( btn.getText( ) );
					
					fileSelected = btn.getText( ).toString( ); //store file name
					
					findFileCSV( );
					
					//validaveCSV.validate( fileSelected );
				}
			}
		);
		*/
		
		
				/*OnCheckedChangeListener() {
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
	}*/
	}
	
}


