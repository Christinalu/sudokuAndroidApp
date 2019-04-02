package com.omicron.android.cmpt276_1191e1_omicron.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.omicron.android.cmpt276_1191e1_omicron.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class RemoveActivity extends AppCompatActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove);
		
		
		Intent extra = getIntent( );
		final String pkgInternalFileName = (String) extra.getSerializableExtra( "pkgInternalFileName" ); //get internla file name of what to remove
		final int indexOfRadBtnToRemove = (int) extra.getSerializableExtra( "indexOfRadBtnToRemove" );  //get index of line (object in wordPackageFileIndex) to remove
		final String pkgName = (String) extra.getSerializableExtra( "pkgName" ); //name of user defined pkg
		
		
		Log.d( "upload", "radio btn index to remove: " + indexOfRadBtnToRemove );
		Log.d( "upload", "pkg internal name to remove: " + pkgInternalFileName );
		
		
		Button btn_delete = findViewById( R.id.button_delete );
		btn_delete.setOnClickListener( new View.OnClickListener( ) {
										   @Override
										   public void onClick(View v)
										   {
										   		int res = 1; //1 means user trying to delete default pkg - not allowed
											   	
											   res = removePkg( pkgInternalFileName, indexOfRadBtnToRemove );
											   	
											   	if( res == 1 ){ //if user tried to remove default pkg
													Toast.makeText(RemoveActivity.this, "Cannot remove \"" + pkgName + "\" Word Package", Toast.LENGTH_LONG).show();
												}else{
													Toast.makeText(RemoveActivity.this, "Word Package Removed", Toast.LENGTH_LONG).show();
												}
										   }
									   }
		);
		
	}
	
	
	private int removePkg( String pkgInternalFileName, int indexOfRadBtnToRemove )
	{
		/*
		 * This function removes an entire pkg from internal memory and data
		 * Returns 1 on error or if user attempts to remove default pkg (not allowed)
		 */
		
		// NOTE: CAN ONLY REMOVE A PKG AT ONCE (otherwise change onStart( ) in MainActivity)
		//		 to delete multiple files at once, have to delete and re-create all radio buttons (see onStop() in MainActivity)
		
		
			// DELETE LINE FROM word_pkg_file_name //
		
		try {
			// read all content
			FileOutputStream outStream;
			FileInputStream fileInStream = this.openFileInput( "word_pkg_name_and_file_name.csv" ); //open file from internal storage
			InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
			BufferedReader buffRead = new BufferedReader( inStreamRead );
			StringBuilder strBuild = new StringBuilder( );
			String[] strSplit; //holds all attributes from relation instance (ie row)
			
			String line;
			int i = 0; //index to keep track of row index in file
			while( (line = buffRead.readLine()) != null ) //
			{
				if( i == indexOfRadBtnToRemove ) //if on line that the user wants to delete
				{
					strSplit = line.split(","); //get all attribute
					if (strSplit[4].contentEquals("0")) //if removing allowed (usr installed) pkg
					{
						//do not add this line (remove it)
					} else { //cannot remove default pkg
						Log.d("upload", "user attempted to remove default pkg");
						return 1;
					}
				}
				else { //not a line to remove, keep the same
					strBuild.append(line);
					strBuild.append("\n");
				}
				
				i++;
			}
			
			// DELETE pkg_n.csv FILE
			buffRead.close( );
			Log.d( "upload", "file to rm: " + pkgInternalFileName );
			boolean del = deleteFile( pkgInternalFileName );
			if( del == false ){ //check if file deleted
				Log.d( "upload", "ERROR: cannot delete file" );
				return 1;
			}
			
			// re-write content
			outStream = this.openFileOutput( "word_pkg_name_and_file_name.csv", this.MODE_PRIVATE ); //open private output stream for re-write
			outStream.write( strBuild.toString().getBytes( ) ); //convert string to bytes and write to file
			outStream.close( ); //close and save file
			
			
				// DECREASE THE NUMBER OF PKG SO FAR //
			
			FileInputStream fileInStream2 = this.openFileInput( "current_word_pkg_count.txt" ); //open file from internal storage
			InputStreamReader inStreamRead2 = new InputStreamReader( fileInStream2 );
			BufferedReader buffRead2 = new BufferedReader( inStreamRead2 );
			
			String countStr = buffRead2.readLine( ); //get pkg count so far int as string
			
			try {
				OutputStreamWriter outStreamWrite = new OutputStreamWriter( this.openFileOutput( "current_word_pkg_count.txt", Context.MODE_PRIVATE ) );
				int cnt = Integer.parseInt( countStr ) - 1;
				if( cnt <= 0 )
				{ Log.d( "upload", "ERROR: file count <= 0" ); }
				outStreamWrite.write( Integer.toString( cnt ) ); //write new count to file
				outStreamWrite.close( );
			}
			catch( IOException e ){
				Log.d("upload", "ERROR: decrease file count write failed in removePkg( )" );
			}
			
			return 0; //pkg removed
			
		} catch (IOException e) {
			Log.d( "upload", "ERROR: exception in RemoveActivity in function removePkg( )" );
			return 1; //error
		}
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


