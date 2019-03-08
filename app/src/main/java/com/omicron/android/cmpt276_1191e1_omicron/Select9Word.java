package com.omicron.android.cmpt276_1191e1_omicron;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

public class Select9Word
{
	/*
	 * This function selects based on statistics the 9 most difficult words
	 * and places them in wordArray
	 * returns 1 on failure
	 */
	
	private int MAX_CSV_ROW;
	
	public Select9Word( int MAX_CSV_ROW2 )
	{
		MAX_CSV_ROW = MAX_CSV_ROW2;
	}
	
	
	public int select(Word[] wordArray, String fileNameSelected, BufferedReader buffRead ) throws IOException
	{
		
			/** STATISTICALLY CHOOSE 9 MOST DIFFICULT WORD BASED ON HINT CLICK**/
		
		String line;
		String[] strSplit;
		
		int lineCount = 0;
		int i = 0;
		long total = 0; //stores total number of Click Count
		long totalBk = 0; //back up for "total"
		Range[] rangeArr = new Range[MAX_CSV_ROW]; //array for each word pair, storing the range of "hint clicks"
		
		Log.d("upload", " @ WORD_ARRAY ON START GAME BTN CLICK:");
		
		//set pkg name
		wordArray[10] = new Word(fileNameSelected, "", -1, -1);
		
		while( (line = buffRead.readLine( )) != null ) //loop and get all lines
		{
			if( i == 0 ) //get language from file
			{
				strSplit = line.split( "," );
				wordArray[9] = new Word( strSplit[0], strSplit[1], -1, -1 );
				i = 1;
			}
			else
			{
				strSplit = line.split( "," );
				totalBk = total;
				total = total + Long.parseLong( strSplit[2] ); //add Click Count for each word
				
				//note: in Range( 0,0 ), it means only 0th index; Range( 1,5 ) means from 1 to 5 inclusive
				rangeArr[lineCount] = new Range( totalBk, total-1, strSplit[0], strSplit[1], Integer.parseInt( strSplit[2] ) );
				
				lineCount++;
			}
		}
		
		//// debug /////////////
		Log.d( "upload", "# of line: " + lineCount );
		Log.d( "upload", "# TOTAL: " + total );
		for( int j=0; j<lineCount; j++ ) //print rangeArr[]
		{
			Log.d( "upload", "RANGE :: line " + (j+1) + ": ( " + rangeArr[j].getNumLeft() + ", " + rangeArr[j].getNumRight() + " )" );
		}
		///////////////////////
		
		
		// RANDOMLY CHOOSE 9 WORDS (based on difficulty) //
		
		// NOTE: Random Number generator does not return all range for "long"
		Random rand = new Random( );
		long randPos; //random position to choose
		int n = 0; //used to prevent run-on random generator
		boolean breakOut = false;
		int[] wordUsed = new int[lineCount]; //array for all words used to mark if a word was selected for wordArray
		
		for( int k=0; k<9; k++ ) //loop to find 9 words
		{
			n = 0; //reset
			breakOut = false; //reset
			while( n < 500000 )
			{
				randPos = rand.nextLong( ) % total; //random position to choose
				
				//loop through rangeArr and find which word range has this value
				for( int c=0; c<lineCount; c++ )
				{
					if( rangeArr[c].getNumLeft() <= randPos && randPos <= rangeArr[c].getNumRight() ) //if within range of word
					{
						if( wordUsed[c] == 1 ) //if word already used
						{
							continue;
						}
						else //word not previously selected for wordArray
						{
							//USE THIS WORD AS NEW wordArr[k]
							wordArray[k] = new Word( rangeArr[c].getStrNative(), rangeArr[c].getStrTranslation(), c+1, 0 );
							wordUsed[c] = 1; //mark word as used
							//break out of loop
							breakOut = true;
							break; //do not look for more words
						}
					}
				}
				
				if( breakOut == true )//breaked after found valid word
				{ break; } //so break out of while loop
				
				n++;
			}
			
			if( n >= 500000 ) //exhausted all tries
			{ return 1; }
		}
		
		
		//// debug //////
		for( int j=0; j<lineCount; j++ ) //print rangeArr[]
		{
			Log.d( "upload", "wordUsed[" + (j+1) + "] :: " + wordUsed[j] );
		}
		/////////////////
		
		return 0;
	}
}
