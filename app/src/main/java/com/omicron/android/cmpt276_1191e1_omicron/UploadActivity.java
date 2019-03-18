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
	private int MAX_WORD_PKG; //maximum number of pkgs the user allowed to upload
	private int MAX_CSV_ROW;
	private int MIN_CSV_ROW;
	private int READ_REQUEST_CODE = 0;
	private int CURRENT_WORD_PKG_COUNT = 0; //stores current number of packages the user has uploaded
	private  FileCSV fileCSV; //object for using CSV functions
	private int STATISTIC_MULTIPLE = 2; //double user Hint clicks to make it more likely for word to be shown
	
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_upload );
		
		//get data from previous activity
		Intent extra = getIntent( );
		MAX_WORD_PKG = (int) extra.getSerializableExtra( "MAX_WORD_PKG" );
		MAX_CSV_ROW = (int) extra.getSerializableExtra( "MAX_CSV_ROW" );
		MIN_CSV_ROW = (int) extra.getSerializableExtra( "MIN_CSV_ROW" );
		
		Log.d( "upload", "in UploadAct MAX_WORD_PKG: " + MAX_WORD_PKG );
		Log.d( "upload", "in UploadAct CURRENT_WORD_PKG_COUNT[0]: " + CURRENT_WORD_PKG_COUNT );
		
		fileCSV = new FileCSV( MAX_WORD_PKG, MAX_CSV_ROW, MIN_CSV_ROW );
		
		
		Log.d( "upload", "onCreate for UploadACt called" );
		
		final TextView textView = findViewById( R.id.selected_file );
		
		
		//get text field id
		final EditText editTextPkgName = (EditText) findViewById( R.id.editText_pkg_name );
		
		
			/* UPLOAD CSV FILE BUTTON */
		Button uploadFileBtn = findViewById( R.id.upload_file );
		uploadFileBtn.setOnClickListener(new View.OnClickListener( )
				{
					@Override
					public void onClick( View v )
					{
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
		
		intent.addCategory( Intent.CATEGORY_OPENABLE ); //only include files that are openable
		intent.setType("*/*"); //only allow .csv files; use "*/*" for all files; "text/comma-separated-values" for CSV
		intent.putExtra( "pkgName", pkgName ); //add pkg name inserted by user
		
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
}


