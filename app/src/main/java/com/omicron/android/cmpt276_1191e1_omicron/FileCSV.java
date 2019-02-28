package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileCSV
{
	/*
	 * This method validates if the provided file is valid CSV file
	 */
	
	
	
	public FileCSV( )
	{
		//void
	}
	
	public int findCurrentPackageCount( Context context ) throws IOException
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
		
		FileInputStream fileInStream = context.openFileInput( "current_word_pkg_count.txt" ); //open file from internal storage
		InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
		BufferedReader buffRead = new BufferedReader( inStreamRead );
		String countStr = buffRead.readLine( ); //get count int as string
		
		return Integer.parseInt( countStr ); //convert string count to int
	}
	
	
	public int checkForCurrentWordPkgCountFile( Context context, int[] CURRENT_WORD_PKG_COUNT )
	{
		/*
		 * This function attempts to access wordPkgCount file
		 * If file does not exist, it means user just downloaded the app and this function creates the wordPkgCount file
		 * returns 0 if file already existed
		 */
		
		String dirPath = context.getFilesDir().getAbsolutePath( ); //get path to local internal storage
		
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
			outStream = context.openFileOutput( fileName, context.MODE_PRIVATE ); //open private output stream
			outStream.write( ("1").getBytes( ) ); //convert string to bytes and write to file DEFAULT 1
			outStream.close( ); //close and save file
			CURRENT_WORD_PKG_COUNT[0] = 1;
		}
		catch( Exception e ) //in case of error
		{
			e.printStackTrace( );
		}
		
		// NO word_pkg_name_and_file_name.csv DETECTED - CREATE NEW FILE (storing csv database relating user defined WorkPackageName with corresponding internal file name)
		
		fileName = "word_pkg_name_and_file_name.csv";
		
		try
		{
			outStream = context.openFileOutput( fileName, context.MODE_PRIVATE ); //open private output stream
			outStream.write( ("0-30 Numbers,pkg_1.csv,English,French\n").getBytes( ) ); //convert string to bytes and write to file DEFAULT 1
			outStream.close( ); //close and save file
		}
		catch( Exception e ) //in case of error
		{
			e.printStackTrace( );
		}
		
		return 1;
	}
	
	
	public void importDefaultPkg( Context context ) throws IOException
	{
		/*
		 * This function is called when the user first installs the app to import a default package from resource file
		 * Note: this is different from URI in Upload Activity
		 */
		
		/* READ FILE */
		InputStream inStream = context.getResources().openRawResource( R.raw.pkg_1 ); //from RAW resource, get the default pkg_1,csv file
		BufferedReader buffRead = new BufferedReader( new InputStreamReader( inStream ) ); //get bytes of file
		
		String str; //store each line from .csv file
		
		StringBuilder strBuild = new StringBuilder( ); //used to concatinate all lines into single String Stream
		
		while( ( str = buffRead.readLine( ) ) != null ) //read lines from buffer until EOF
		{
			strBuild.append( str ); //append all lines to builder
			strBuild.append( "\n" ); //"new line" char important to separate rows (because it is discarded when reading line by line
		}
		
		String content = strBuild.toString( ); //get all content from file so far in a string
		
		String fileName = "pkg_1.csv"; //this is the default naming scheme
		
		FileOutputStream outStream;
		
		try
		{
			outStream = context.openFileOutput( fileName, context.MODE_PRIVATE ); //open private output stream
			outStream.write( content.getBytes( ) ); //convert string to bytes and write to file
			outStream.close( ); //close and save file
		}
		catch( Exception e ) //in case of error
		{
			e.printStackTrace( );
		}
		
	}
	
	
	
	
	
	public void saveCSVFile( Context context, String str )
	{
		/*
		 * This function saves the content read from .csv file imported from user to local private app storage
		 */
		
		String localPath = Environment.getExternalStorageDirectory().toString( ); //get local phone storage path and go to Downloads folder
		Log.d( "storage", "local dir path: " + localPath );
		
		String fileName = "file-name-TEST";
		
		FileOutputStream outStream;
		
		// TODO: make sure no file with same name is overwritten
		// TODO: update current_word_pkg_count.txt
		
		try
		{
			outStream = context.openFileOutput( fileName, context.MODE_PRIVATE ); //open private output stream
			outStream.write( str.getBytes( ) ); //convert string to bytes and write to file
			outStream.close( ); //close and save file
		}
		catch( Exception e ) //in case of error
		{
			e.printStackTrace( );
		}
		
	}
	
	
	public String readCSVUri( Context context, Uri uri )throws IOException
	{
		/*
		 * Returns a string containing the .csv data
		 */
		InputStream inStream = context.getContentResolver().openInputStream( uri ); //create a InputStream by reading bytes
		
		InputStreamReader inStreamRead = new InputStreamReader( inStream ); //convert the bytes from InputStream to encoded char
		BufferedReader buffRead = new BufferedReader( inStreamRead ); //store string in buffer (memory) after bytes were converted to char using InputStreamReader (from InputStream)
		
		String strLine; //store each line from .csv file
		StringBuilder strBuild = new StringBuilder( ); //used to concatinate all lines into single String Stream
		
		while( ( strLine = buffRead.readLine( ) ) != null ) //read lines from buffer until EOF
		{
			// TODO: here have to scheck if .csv file has correct format (2 attribute); use "String[] splitArr = strLine.split( "," )" and test if splitArr.size == 2, for all string lines
			// TODO:	+ check each of splitArr[i] if each string.size is NOT 0, and <=35
			// TODO:	+ check if files have at most 150 word pairs - when testing, dont forget to change LIMIT back to 150
			// TODO:	+ if test arrived until here, check there should ONLY BE 1 comma per line : test this
			
			strBuild.append( strLine ); //append all lines to builder
			strBuild.append( "\n" ); //"new line" char important to separate rows (because it is discarded when reading line by line
		}
		
		return strBuild.toString( ); //return StringBuilder as String
	}
	
	
	
	/*public boolean validate( String fileSelected )
	{
		// validate that a .csv file has 2 columns and all filled //
		
	}*/
}


