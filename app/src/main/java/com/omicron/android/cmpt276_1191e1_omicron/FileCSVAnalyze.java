package com.omicron.android.cmpt276_1191e1_omicron;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class FileCSVAnalyze
{
	/*
	 * This class in an extension of the FileCSV but focuses on the Model and Control part of MVC
	 */
	
	private int MAX_WORD_PKG;
	
	public FileCSVAnalyze( int MAX_WORD_PKG2 )
	{
		MAX_WORD_PKG = MAX_WORD_PKG2;
	}
	
	public int analyseSaveFileNameNum( File[] file )
	{
		
		/*
		 * This class takes a File[] which stores all files names, and returns a int 'n' for
	 	 * 		which file name is available for format pkg_n.csv
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
			
			if( fileName.length() < 9 ) //if name <9 means its too short to be valid file name
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
	
	
	public String[] analyseReadCSVFile( BufferedReader buffRead, int MAX_LANG_LEN, int MAX_CSV_ROW, int MAX_WORD_LEN, int MIN_CSV_ROW ) throws IOException {
		/*
		 * Returns a string[] containing the [0] .csv data and [1] line with language
		 * Returns empty string in [0] if file does not have proper formatting
		 */
		
		String strLine; //store each line from .csv file
		StringBuilder strBuild = new StringBuilder( ); //used to concatenate all lines into single String Stream
		
		String[] splitLine; //array to store the strings parsed from line by comma
		String lang;
		String[] res = { "", "" };
		
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
		
		
			// TEST FORMAT
	
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
				strBuild.append("\n"); //"new line" char important to separate rows (because it is discarded when reading line by line
				totalLineCnt++; //increase count of valid word pair
			}
			else
			{ return res; } //invalid word pair
			
			loopLimit ++;
		}
		if( loopLimit > MAX_CSV_ROW ){ return res; }
		
		if( totalLineCnt < MIN_CSV_ROW || totalLineCnt > MAX_CSV_ROW ){ //9 word pairs is a minimum and has to be  <= MAX_CSV_ROW as requirement
			Log.d( "upload", "ERROR: incorrect number of word pairs" );
			return res;
		}
		
		//return valid String[]
		res[0] = strBuild.toString( );
		res[1] = lang;
		
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
