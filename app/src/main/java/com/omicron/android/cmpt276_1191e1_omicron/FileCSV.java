package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileCSV
{
	/*
	 * This method validates if the provided file is valid CSV file
	 */
	
	private int MAX_CSV_ROW = 150; //allow up to 150 pairs per package
	private int MAX_WORD_LEN = 35; //only allow words with max 35 char
	private int MAX_LANG_LEN = 25; //only allow language names up to 25 char
	
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
	
	
	public int checkIfCurrentWordPkgCountFileExists( Context context, int[] CURRENT_WORD_PKG_COUNT )
	{
		/*
		 * This function attempts to access wordPkgCount file
		 * If file does not exist, it means user just downloaded the app and this function creates the current_WordPkgCount and word_pkg_name_and_file file
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
		
		StringBuilder strBuild = new StringBuilder( ); //used to concatenate all lines into single String Stream
		
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
		// TODO: use naming scheme pkg_n.csv
		// TODO:	- IMPORTANT: here loop through all pkg_ names so far and find a number k that was not used so far and use that as pkg_k.csv
		// TODO:		- create valNumArr array of size PKG_MAX_COUNT = 50 and loop through csv file and extract _n by ignoring "pkg_" and mark that 'n-1' (has to be n-1 because arr start @0) as used
		// TODO:			- then loop through valNumArr and find first 'n' that was no used and use that as pkg name
		// TODO: 			- BUT MAKE SURE when reading from valNumArr which 'n' is available, remember to increase that count +1 back when saving to word_pkg_name_file
		// TODO:			- ie if n=15 was not used, then in numValArr n=14 and then when saving to word_pkg_name_file n=15 again
		// TODO:		- dont forget to add this file name  + user WordPackage name in the word_pkg_name data file
		
		try
		{
			outStream = context.openFileOutput( fileName, context.MODE_PRIVATE ); //open private output stream
			outStream.write( str.getBytes( ) ); //convert string to bytes and write to file
			outStream.close( ); //close and save file
			
			//increase pkg count in file
			increaseCurrentWordPkgCount( context );
		}
		catch( Exception e ) //in case of error
		{
			e.printStackTrace( );
		}
		
	}
	
	
	public String readCSVUri( Context context, Uri uri ) throws IOException //
	{
		/*
		 * Returns a string containing the .csv data
		 * Returns empty string if file does not have proper formatting
		 */
		InputStream inStream = context.getContentResolver().openInputStream( uri ); //create a InputStream by reading bytes
		
		InputStreamReader inStreamRead = new InputStreamReader( inStream ); //convert the bytes from InputStream to encoded char
		BufferedReader buffRead = new BufferedReader( inStreamRead ); //store string in buffer (memory) after bytes were converted to char using InputStreamReader (from InputStream)
		
		String strLine; //store each line from .csv file
		StringBuilder strBuild = new StringBuilder( ); //used to concatinate all lines into single String Stream
		
		// TODO: when implementing feature to allow user to upload audio files, it may not be necessary to change code below; just dont add a 3rd cell in the first line, ie only the word pairs have 3 cells to include audio
		
		// read first line and extract native and translation language //
		// first line must have two cells, so check if only one "comma" is present, other wise the line does not contain both language, or contains too many cells or contains commas, which are no allowed
		// also check if any parsed strings are null, ie if the cell is empty
		// and check if language <= MAX_LANG_LEN
		
		String[] splitLine; //array to store the strings parsed from line by comma
		strLine = buffRead.readLine( ); //get first line
		splitLine = strLine.split( "," ); //split by comma
		
		int commaCount = countChar( strLine, ',' ); //find how many commas there are in string
		
		if( splitLine.length == 2 && splitLine[0].length() > 0 && splitLine[1].length() > 0 && commaCount == 1 && //also check that if 2 attributes then there should be 1 comma
			splitLine[0].length() <= MAX_LANG_LEN && splitLine[1].length() <= MAX_LANG_LEN ) //check for correct formatting; ie if not null, all cells filled, if not enough or too many cells
		{
			strBuild.append( splitLine[0] + "," + splitLine[1] + "\n" ); //if correct format add the lang to file
		}
		else //file has improper formatting
		{ return ""; }
		
		
		while( ( strLine = buffRead.readLine( ) ) != null ) //read lines from buffer until EOF
		{
			// TODO: here have to check if .csv file has correct format (2 attribute); use "String[] splitArr = strLine.split( "," )" and test if splitArr.size == 2, for all string lines
			// TODO:	+ check each of splitArr[i] if each string.size is NOT 0, and <=MAX_WORD_LEN
			// TODO:	+ check if files have at most MAX_CSV_ROW word pairs - when testing, dont forget to change LIMIT back to 150
			// TODO:	+ if test arrived until here, check there should ONLY BE 1 comma per line : test this
			// TODO:	+ fix case where last col is empty ie csv file line is "one,", the second word is missing so fix this case by saying (after parsing the line in lineSplit[]) "if( lineSplit[0].length() > 0 ie not null AND lineSplit.length == 2 ie only 2 columns ){ }else{ say that file has incorrect format ie missing or extra cell/col }
			// TODO:		- check this by giving invalid csv files WITH ONLY ONE INVALID THING, because multipe invalid things in a single file may not catch all errors, so always test if single error is caught
			// NOTE: leave the pkg, lang and trans name in XML because its easier to check valid input
			// TODO: if csv file is improper then return null
			// TODO: add languages from first row to word_pkg_name_file
			
			// here check if line is valid, then if valid add to strBuild
			
			
			strBuild.append( strLine ); //append all lines to builder
			strBuild.append( "\n" ); //"new line" char important to separate rows (because it is discarded when reading line by line
		}
		
		return strBuild.toString( ); //return StringBuilder as String
	}
	
	
	public void increaseCurrentWordPkgCount( Context context ) throws IOException //
	{
		/*
		 * This method is called when a word_pkg was added, so current_word_pkg_count needs to be increased
		 */
		
		// READ THE NUMBER OF PKG SO FAR //
		FileInputStream fileInStream = context.openFileInput( "current_word_pkg_count.txt" ); //open file from internal storage
		InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
		BufferedReader buffRead = new BufferedReader( inStreamRead );
		
		String countStr = buffRead.readLine( ); //get pkg count so far int as string
		
		// INCREASE AND SAVE TO FILE //
		try {
			OutputStreamWriter outStreamWrite = new OutputStreamWriter( context.openFileOutput( "current_word_pkg_count.txt", Context.MODE_PRIVATE ) );
			outStreamWrite.write( Integer.toString( Integer.parseInt( countStr ) + 1 ) ); //write new count to file
			outStreamWrite.close( );
		}
		catch( IOException e ){
			Log.d("upload", "ERROR: increaseCurrentWordPkgCount( ) file write failed" );
		}
	}
	
	
	private int countChar( String str, char c ) //count how many times a char is contained in string
	{
		int count = 0;
		
		for( int i=0; i<str.length(); i++ )
		{
			if( str.charAt(i) == c )
			{ count++; }
		}
		return count;
	}
	
	
	
	/*public boolean validate( String fileSelected )
	{
		// validate that a .csv file has 2 columns and all filled //
		
	}*/
}


