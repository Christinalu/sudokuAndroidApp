package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
	
	private int MAX_CSV_ROW; //allow up to 150 pairs per package
	private int MIN_CSV_ROW; //minimum number of words required in file
	private int MAX_WORD_LEN = 35; //only allow words with max 35 char
	private int MAX_LANG_LEN = 25; //only allow language names up to 25 char
	private int MAX_WORD_PKG; //max word packages user is allowed to import
	private int MIN_FILE_NAME_LENGTH = 9; //naming scheme "pkg_n.csv" must have at least 9 characters
	private int LIMIT_TITLE = 1000;
	private int LIMIT_LOOP = 10000;
	
	public FileCSV( int MAX_WORD_PKG2, int MAX_CSV_ROW2, int MIN_CSV_ROW2 )
	{
		MAX_WORD_PKG = MAX_WORD_PKG2;
		MAX_CSV_ROW = MAX_CSV_ROW2;
		MIN_CSV_ROW = MIN_CSV_ROW2;
	}
	
	public int findCurrentPackageCount( Context context ) throws IOException
	{
		/*
		 * This reads raw file resource, finds and returns an int representing how many WordPackages the user has uploaded so far
		 */
		
		FileInputStream fileInStream = context.openFileInput( "current_word_pkg_count.txt" ); //open file from internal storage
		InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
		BufferedReader buffRead = new BufferedReader( inStreamRead );
		String countStr = buffRead.readLine( ); //get count int as string
		
		return Integer.parseInt( countStr ); //convert string count to int
	}
	
	
	public int checkIfCurrentWordPkgCountFileExists( Context context )
	{
		/*
		 * This function attempts to access wordPkgCount file
		 * If file does not exist, it means user just downloaded the app and this function creates the current_WordPkgCount and word_pkg_name_and_file file
		 * returns 0 if file already existed
		 */
		
		// NOTE: when adding more default pkgs, also increase current_word_pkg_count from 1 to n AND modify word_pkg_name_file_name
		// NOTE: when allowing user to delete pkgs, if adding any pkgs here as default continue pattern pkg_n, pkg_n+1... AND change in DELETE function not to delete these files
		
		
		String dirPath = context.getFilesDir().getAbsolutePath( ); //get path to local internal storage
		
		File dir = new File( dirPath ); //get File object storing all file names
		File[] file = dir.listFiles( ); //save all file names (path) in an array
		
		//check if file already exists
		for( int i=0; i<file.length; i++ )
		{
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
			outStream.write( ("0-30 Numbers,pkg_0.csv,English,French,31,1\n").getBytes( ) ); //convert string to bytes and write to file DEFAULT 1
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
		
		/** NOTE: default CSV files should not contain any empty lines **/
		
			/* READ FILE */
		
		InputStream inStream = context.getResources().openRawResource( R.raw.pkg_0 ); //from RAW resource, get the default pkg_1,csv file
		BufferedReader buffRead = new BufferedReader( new InputStreamReader( inStream ) ); //get bytes of file
		
		String str; //store each line from .csv file
		
		StringBuilder strBuild = new StringBuilder( ); //used to concatenate all lines into single String Stream
		
		//read language
		str = buffRead.readLine( );
		strBuild.append( str );
		strBuild.append( "\n" );
		
		while( ( str = buffRead.readLine( ) ) != null ) //read lines from buffer until EOF
		{
			strBuild.append( str ); //append all lines to builder
			strBuild.append( ",1\n" ); //"new line" char important to separate rows
									   // (because it is discarded when reading line by line from InputStream)
									   // 1 represents hint click count
		}
		
		String content = strBuild.toString( ); //get all content from file so far in a string
		
		String fileName = "pkg_0.csv"; //this is the default naming scheme
		
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
	
	
	public int saveCSVFile( Context context, String[] strContent, String pkgName )
	{
		/*
		 * This function saves the content read from .csv file imported from user to local private app storage
		 * Returns 0 if everything ok, -1 if error
		 */
		
		// NOTE: file naming system "pkg_n.csv" where 'n' is an integer >= 0
		// NOTE: this function is called after the CSV file  has been checked for validity, so this function assumes CSV file is valid
		
		FileOutputStream outStream;
		
			/* FIND WHAT "FILE NAME" SHOULD BE (to not overwrite files with the same same) */
		
		
		//get all file name
		String dirPath = context.getFilesDir().getAbsolutePath( ); //get path to local internal storage
		File dir = new File( dirPath ); //get File object storing all file names
		File[] file = dir.listFiles( ); //save all file names (path) in an array
		
		
		//FileCSVAnalyze analyzeCSV = new FileCSVAnalyze( MAX_WORD_PKG );
		
		//call function to find available file number
		int validIndex = analyseSaveFileNameNum( file );
		
		if( validIndex == -1 )
		{ return -1; } //error occured
		
		String fileNameFinal = "pkg_" + Integer.toString( validIndex ) + ".csv"; //set new valid name
		
		
			/* WRITE TO pkg_n FILE */
		try
		{
			outStream = context.openFileOutput( fileNameFinal, context.MODE_PRIVATE ); //open private output stream
			outStream.write( strContent[0].getBytes( ) ); //convert string to bytes and write to file
			outStream.close( ); //close and save file
			
			//increase pkg count in file
			increaseCurrentWordPkgCount( context );
		}
		catch( Exception e ) //in case of error
		{
			e.printStackTrace( );
		}
		
		
		
			/* APPEND TO word_pkg_name_file_name.csv FILE THE NEW ADDED FILE */
		
		try {
			// read all content
			FileInputStream fileInStream = context.openFileInput( "word_pkg_name_and_file_name.csv" ); //open file from internal storage
			InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
			BufferedReader buffRead = new BufferedReader( inStreamRead );
			StringBuilder strBuild = new StringBuilder( );
			
			String line;
			while( (line = buffRead.readLine()) != null )
			{
				strBuild.append( line );
				strBuild.append( "\n" );
			}
			
			// re-write content
			outStream = context.openFileOutput( "word_pkg_name_and_file_name.csv", context.MODE_PRIVATE ); //open private output stream for re-write
			outStream.write( (strBuild.toString() + pkgName + "," + fileNameFinal + ","
					+ strContent[1].replace( "\n", "") +","+ strContent[2] + ",0\n" ).getBytes( ) ); //convert string to bytes and write to file
			outStream.close( ); //close and save file
			
			
			
		} catch (IOException e) {
			Log.d( "upload", "ERROR: exception in FileCSV in function saveCSVFile( )" );
		}
		
		return 0; //no errors
	}
	
	
	
	
	public String[] readCSVUri( Context context, Uri uri ) throws IOException //
	{
		/*
		 * Returns a string[] containing the [0] .csv data and [1] line with language
		 * Returns empty string in [0] if file does not have proper formatting
		 */
		
		InputStream inStream = context.getContentResolver().openInputStream( uri ); //create a InputStream by reading bytes
		
		InputStreamReader inStreamRead = new InputStreamReader( inStream ); //convert the bytes from InputStream to encoded char
		BufferedReader buffRead = new BufferedReader( inStreamRead ); //store string in buffer (memory) after bytes were converted to char using InputStreamReader (from InputStream)
		
		
		//FileCSVAnalyze fileAnalyseReadCSV = new FileCSVAnalyze( MAX_WORD_PKG );
		
		//analyse .csv data and get string[] with [0] .csv data and [1] with line
		String [] res = analyseReadCSVFile( buffRead, MAX_LANG_LEN, MAX_CSV_ROW, MAX_WORD_LEN, MIN_CSV_ROW );
		
		return res;
	}
	
	
	public int checkUsrPkgNameForUnique( Context context, String pkgName, int CURRENT_WORD_PKG_COUNT ) throws IOException //
	{
		/*
		 * This function checks if the user inserted pkg name does not already exist
		 * If the pkg name already exists, it returns 1
		 */
		
		FileInputStream fileInStream = context.openFileInput( "word_pkg_name_and_file_name.csv" ); //open file from internal storage
		InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
		BufferedReader buffRead = new BufferedReader( inStreamRead );
		
		String line;
		String[] split;
		while( (line = buffRead.readLine()) != null )
		{
			split = line.split( ",", 2 );
			
			//first param is user defined pkg name
			if( pkgName.contentEquals( split[0] ) )
			{
				return 1;
			}

		}
		
		return 0;
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
	
	
	public int analyseSaveFileNameNum( File[] file )
	{
		
		/*
		 * This class takes a File[] which stores all files names, and returns a int 'n' for
		 * 		which file name is available for format pkg_n.csv, that is, it finds which
		 * 		pkg_n.csv name is available;
		 * 		if had pkg_0.csv, pkg_2.csv, pkg_3.csv already, then 'n' would be 1
		 * Returns -1 on failure
		 */
		
		String fileName;
		String fileNoStart; //stores file name without the "pkg_"
		String fileNoExtension; //stores file name without the ".csv"
		int invalidPkgNameSoFar = 0; //each time fileNameUsedArr has something set to "1" this is increased to represent how many "1" are in array
		int arrIndex;
		int[] fileNameUsedArr = new int[MAX_WORD_PKG]; //create array to be used to determine which number represents that a file already exists; ie pkg_5 would have fileNameUsedArr[4] == 1
		
		
		
			/* FIND WHAT "FILE NAME" SHOULD BE (to not overwrite files with the same same) */
		
		// loop through all pkg_n files and mark which names already exist
		for( int i=0; i<file.length; i++)
		{
			Log.d("upload", "file-name-CSV: " + file[i].getName( ) );
			
			fileName = file[i].getName( ); //get file name
			
			// VALIDATE FILE NAME OF FORM "pkg_n.csv" //
			
			if( fileName.length() < MIN_FILE_NAME_LENGTH ) //if too short to be valid file name
			{ continue; }
			
			if( (fileName.substring( 0, 4 )).contentEquals( "pkg_" ) ) //check if file starts with "pkg_"
			{
				fileNoStart = fileName.substring( 4 ); //remove "pkg_"
				
				Log.d( "extension","extension: " + fileNoStart.substring( fileNoStart.length()-4, fileNoStart.length() ) );
				
				//test for ".csv" extension
				if( fileNoStart.substring( fileNoStart.length()-4, fileNoStart.length() ).contentEquals( ".csv" ) == false )
				{ return -1; }
				
				fileNoExtension = fileNoStart.substring(0, fileNoStart.length()-4 ); //remove ".csv" extension
				
				try{
					arrIndex = Integer.parseInt( fileNoExtension ); //get 'n' from "pkg_n.csv"
				}
				catch(Exception e){
					Log.d( "upload", "ERROR: could not convert to int :file: " + fileName );
					continue;
				}
				
				if( 0 <= arrIndex && arrIndex < MAX_WORD_PKG ){
					fileNameUsedArr[arrIndex] = 1; //mark file name as taken
				}
				else
				{ Log.d( "upload", "ERROR: integer but out of bound" ); }
			}
			
		}
		
		for( int i=0; i<MAX_WORD_PKG; i++ ) //loop and find how many file names are invalid
		{
			if( fileNameUsedArr[i] == 1 )
			{ invalidPkgNameSoFar++; }
		}
		
		if( invalidPkgNameSoFar >= MAX_WORD_PKG ) //if array full with 1
		{
			return -1; //error: no available name to use, this should not occur unless incorrect extra internal files were added
		}
		
		// FIND WHICH FILE NAME IS AVAILABLE
		
		int validIndex = -1;
		for( int i=0; i<MAX_WORD_PKG; i++ ) //loop and find an index == 0, meaning that this number was not used yet
		{
			if( fileNameUsedArr[i] == 0 )
			{ validIndex = i; break; }
		}
		
		return validIndex; //return available file number
	}
	
	
	public String[] analyseReadCSVFile( BufferedReader buffRead, int MAX_LANG_LEN, int MAX_CSV_ROW,
										int MAX_WORD_LEN, int MIN_CSV_ROW ) throws IOException
	{
		/*
		 * Returns a string[] containing the string[0]=='.csv data', string[1]=='line with language',
		 * string[2]=='word pair count in package'
		 * Returns empty string in string[0] if file does not have proper formatting
		 */
		
		String strLine; //store each line from .csv file
		StringBuilder strBuild = new StringBuilder( ); //used to concatenate all lines into single String Stream
		
		String[] splitLine; //array to store the strings parsed from line by comma
		String lang;
		String[] res = { "", "", "" };
		
		try {
			strLine = buffRead.readLine(); //required if file empty to prevent crash on accessing .split() on null pointer
		} catch (IOException e) {
			strLine = null;
			e.printStackTrace();
		}
		
		if( strLine == null ) //test if file is empty by testing first line
		{ return res; }
		
		//get first valid line containing the language
		splitLine = strLine.split( "," ); //split by comma
		
		int commaCount = countChar( strLine, ',' ); //find how many commas there are in string
		
		
		// TEST FORMAT (for title)
		if( splitLine.length == 2 && splitLine[0].length() > 0 && splitLine[1].length() > 0 && commaCount == 1 && //also check that if 2 attributes then there should be 1 comma
				splitLine[0].length() <= MAX_LANG_LEN && splitLine[1].length() <= MAX_LANG_LEN ) //check for correct formatting; ie if not null, all cells filled, if not enough or too many cells
		{
			strBuild.append( splitLine[0] + "," + splitLine[1] + "\n" ); //if correct format add the lang to file
			lang = splitLine[0] + "," + splitLine[1] + "\n"; //this will be sent to saveCSVFile
		}
		else //file has improper formatting
		{ return res; }
		
		
		// TEST FORMAT (for word pair)
		
		int totalLineCnt = 0; //stores the number of word pairs in the file
		
		int loopLimit = 0; //only loop to MAX, looping any more is irrelevant since file does not meet size requirement
		while( ( strLine = buffRead.readLine( ) ) != null && loopLimit <= MAX_CSV_ROW ) //read lines from buffer until EOF
		{
			// here check if line is valid, then if valid add to strBuild
			splitLine = strLine.split( "," ); //split by comma
			commaCount = countChar( strLine, ',' ); //find how many commas there are in string
			
			if( splitLine.length == 2 && splitLine[0].length() > 0 && splitLine[1].length() > 0 && commaCount == 1 && //also check that if 2 attributes then there should be 1 comma
					splitLine[0].length() <= MAX_WORD_LEN && splitLine[1].length() <= MAX_WORD_LEN ) //check for correct formatting; ie if not null, all cells filled, if not enough or too many cells
			{
				strBuild.append(strLine); //append all lines to builder
				strBuild.append( ",1" ); //add a third "hint click" attribute to represent how many times a user has clicked in Dictionary to reveal translation (used to find which words the user is having difficulty with); "1" must be default, NOT "0" because later in code "1" is needed
				strBuild.append("\n"); //"new line" char important to separate rows (because it is discarded when reading line by line)
				totalLineCnt++; //increase count of valid word pair
			}
			else
			{ return res; } //invalid word pair
			
			loopLimit ++;
		}
		if( loopLimit > MAX_CSV_ROW ){ return res; }
		
		if( totalLineCnt < MIN_CSV_ROW || totalLineCnt > MAX_CSV_ROW ){ //'n' word pairs is a minimum and has to be  <= MAX_CSV_ROW as requirement
			Log.d( "upload", "ERROR: incorrect number of word pairs" );
			return res;
		}
		
		//return valid String[]
		res[0] = strBuild.toString( );
		res[1] = lang;
		res[2] = Integer.toString( totalLineCnt );
		
		return res;
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
	
}


