package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.util.Log;

import com.omicron.android.cmpt276_1191e1_omicron.PackageFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WordPackageFileIndex
{
	/*
	 * This class stores the Word Packages the user has uploaded so far in an array,
	 * along with the internal file name for reference
	 * This array will be used to display all uploaded packages so far in MainActivity for user to select
	 */
	
	private int MAX_WORD_PKG;
	private int CURRENT_WORD_PKG_COUNT;
	private int WORD_PKG_ATTR_NUM = 4;
	private PackageFile[] packageFileArr; //stores pairs of Word Package name and internal file name
	
	public WordPackageFileIndex( Context context, int MAX_WORD_PKG2, int CURRENT_WORD_PKG_COUNT2 ) throws IOException //construct
	{
		String pkgName; //used to store wordPackageName
		String fileName; //used to store internalFileName
		String nativeLang; //stores the native lang
		String translateLang; //stores the lang name of translation
		String[] linePart = new String[WORD_PKG_ATTR_NUM]; //array parsing all data in line in individual strings
		
		MAX_WORD_PKG = MAX_WORD_PKG2;
		CURRENT_WORD_PKG_COUNT = CURRENT_WORD_PKG_COUNT2;
		
		packageFileArr = new PackageFile[CURRENT_WORD_PKG_COUNT]; //as many as user has now; this array need not be initialized max size because each time the user uploads a file and returns back to MainActivity, this will be re-created
		
		//warning: test if PackageFile line above should have size MAX_PKG_COUNT instead if activity nor re-created
		
		//open and read from word_pkg_name
		String line; //stores entire line from file
		FileInputStream fileInStream = context.openFileInput( "word_pkg_name_and_file_name.csv" ); //open file from internal storage
		InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
		BufferedReader buffRead = new BufferedReader( inStreamRead );
		
		for( int i=0; i<CURRENT_WORD_PKG_COUNT; i++ )
		{
			// READ THE DATA FROM THE DATA FILE //
			line = buffRead.readLine( ); //get one line at a time
			linePart = line.split( "," ); //parse all line attributes with respect to comma
			
			pkgName = linePart[0];
			fileName = linePart[1];
			nativeLang = linePart[2];
			translateLang = linePart[3];
			
			// TODO: finished? -yes
			
			//add all data into a PackageFile object
			packageFileArr[i] = new PackageFile( pkgName, fileName, nativeLang, translateLang );
		
		}
	}
	
	public PackageFile getPackageFileAtIndex( int index )
	{
		//get the PackageFile at a certain index
		
		PackageFile pf = null;
		if( index < CURRENT_WORD_PKG_COUNT ) //check for out of bounds
		{
			pf = packageFileArr[index];
		}
		else
		{ Log.d( "upload", " -- ERROR: out of bounds index in getPackageFileAtIndex( )" ); }
		return pf;
	}
	
	public int length( )
	{
		//get the length of the PackageFile array
		return packageFileArr.length;
	}
}
