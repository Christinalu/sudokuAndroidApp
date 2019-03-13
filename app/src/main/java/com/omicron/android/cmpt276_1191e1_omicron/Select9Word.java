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
	
	
	public int select(Word[] wordArray, String fileNameSelected, BufferedReader buffRead, int HINT_CLICK_TO_MAX_PROB ) throws IOException
	{
		/*
		 * Modifies existing wordArray with the statistically selected words
		 * Returns 1 on failure
		 */
		//discrete units are units assigned to words to mimin discrete probability
		//for example, starting out, you could have 5 words with 10 units each == 50 total units
		//hence the probability of choosing one word would be units_of_word / total_units == 10/50 == 0.2
		
		int hintCountTotal = 0;
		int SINGLE_WORD_BLOCK_UNIT_SZ_CONST = 10; //constant stores how many discrete units are assigned to a single word HintClick
		int SINGLE_WORD_BLOCK_UNIT_SZ; //depending on SINGL_WORD_BLOCK_UNIT_SZ_CONST and HintClick, this will represent units of single word OF 1 HINT-COUNT
		int WORD_INCREASE_UNIT_MULTIP = 10; //multiplier whch increases word_unit depending on how many words there are, ie if == 10; then 10 words == 100 total units and 100 words == 1000 total units
		float MAX_PERCENTAGE_OF_TOTAL = 0.1f; //stores a percentage of how much probability a word is allowed to gain; ie if == 0.2 and total_units == 1000 (ie 10 words with 100 units), then the max units a word can have would be 0.2*1000 = 200 units max per block
		int MAX_WORD_UNIT_LIMIT_DEFAULT; //stores the maximum units that a single word can have at any time
		int MAX_WORD_UNIT_LIMIT; //same as MAX_WORD_UNIT_LIMIT_DEFAULT but adjusted for HintClick
		long TOTAL_UNITS = 0; //stores all the units from all the HintCount blocks //needs to be long ( > int)
		int WORD_UNIT_INCREASE_PER_HINT_CLICK; //defines how many units are added per HintClick (when HintClick > 1)
		
		
			/** STATISTICALLY CHOOSE 9 MOST DIFFICULT WORD BASED ON HINT CLICK**/
		
		String line;
		String[] strSplit;
		
		int lineCount = 0; //also how many word pairs in file
		int i = 0;
		int total = 0; //stores total number of Click Count
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
				total = total + Integer.parseInt( strSplit[2] ); //add Click Count for each word
				
				//note: in Range( 0,0 ), it means only 0th index; Range( 1,5 ) means from 1 to 5 inclusive
				int hintCount = Integer.parseInt( strSplit[2] );
				hintCountTotal = hintCountTotal + hintCount;
				rangeArr[lineCount] = new Range( 0, 0, strSplit[0], strSplit[1], hintCount );
				
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
		
		
		SINGLE_WORD_BLOCK_UNIT_SZ = SINGLE_WORD_BLOCK_UNIT_SZ_CONST * lineCount; //find how many units a word with HintCount == 1 will have
		int totalUnitsDefault = SINGLE_WORD_BLOCK_UNIT_SZ * lineCount; //get total units based on new, equal, probability word file where each word has HintClick == 1
		
		//// NOTE /////
		// units in a word with MAX_PERCENTAGE_OF_TOTAL, at max: MAX_UNIT == MAX_PERCENTAGE_OF_TOTAL*(lineCount * (lineCount*SINGL_WORD_BLOCK_UNIT_SZ_CONST) )
		// probability of choosing one word with max_probability == MAX_UNIT / ( (lineCount-1) * lineCount * SINGL_WORD_BLOCK_UNIT_SZ_CONST )
		
		MAX_WORD_UNIT_LIMIT_DEFAULT = (int)(MAX_PERCENTAGE_OF_TOTAL * totalUnitsDefault); //find how many units a word can contain at any time
		
		if( MAX_WORD_UNIT_LIMIT_DEFAULT < SINGLE_WORD_BLOCK_UNIT_SZ  ) //needs a minimum word count for probability to increase, so that taking MAX_PERCENTAGE_OF_TOTAL wont decreases probability
		{ MAX_WORD_UNIT_LIMIT_DEFAULT = SINGLE_WORD_BLOCK_UNIT_SZ; }
		
		WORD_UNIT_INCREASE_PER_HINT_CLICK = ( MAX_WORD_UNIT_LIMIT_DEFAULT - SINGLE_WORD_BLOCK_UNIT_SZ ) / HINT_CLICK_TO_MAX_PROB; //find how many units will be added per additional click
		
		//find MAX_UNIT_LIMIT based on MAX HintClick (should be <= MAX_WORD_UNIT_LIMIT_DEFAULT)
		MAX_WORD_UNIT_LIMIT = SINGLE_WORD_BLOCK_UNIT_SZ + (HINT_CLICK_TO_MAX_PROB-1)*WORD_UNIT_INCREASE_PER_HINT_CLICK;
		
		
			/* CREATE RANGE BASED ON HintClick */
		
		// NOTE: 1 HintClick = SINGLE_WORD_BLOCK_UNIT_SZ
		// set up first word
		int num = SINGLE_WORD_BLOCK_UNIT_SZ;
		rangeArr[0].setNumLeft(1);
		num = num + (rangeArr[0].getHintClick() - 1 ) * WORD_UNIT_INCREASE_PER_HINT_CLICK;
		rangeArr[0].setNumRight(  num );
		TOTAL_UNITS = num;
		
		//create UNIT based range
		for( int j=1; j<lineCount; j++ )
		{
			rangeArr[j].setNumLeft( rangeArr[j-1].getNumRight()+1 ); //left to prev word range.right but +1
			num = SINGLE_WORD_BLOCK_UNIT_SZ + (rangeArr[j].getHintClick() - 1 )*WORD_UNIT_INCREASE_PER_HINT_CLICK; //calculate how many units in this word
			rangeArr[j].setNumRight( TOTAL_UNITS + num );
			TOTAL_UNITS = TOTAL_UNITS + num;
		}
		
		
		// OVERFLOW TEST
		if( TOTAL_UNITS >= Integer.MAX_VALUE )
		{ return -1; } //error overflow
		
		
		//// debug /////////////
		Log.d( "upload", "# of line: " + lineCount );
		Log.d( "upload", "# TOTAL: " + total );
		for( int j=0; j<lineCount; j++ ) //print rangeArr[]
		{
			Log.d( "selectW", "RANGE :: unit block " + (j+1) + ": ( " + rangeArr[j].getNumLeft() + ", " + rangeArr[j].getNumRight() + " )" );
		}
		///////////////////////
		
		
		Log.d( "selectW", "lineCount: " + lineCount );
		Log.d( "selectW", "WORD_UNIT_INCREASE_PER_HINT_CLICK: " + WORD_UNIT_INCREASE_PER_HINT_CLICK );
		Log.d( "selectW", "totalUnitsDefault: " + totalUnitsDefault );
		Log.d( "selectW", "SINGLE_WORD_BLOCK_UNIT_SZ: " + SINGLE_WORD_BLOCK_UNIT_SZ );
		Log.d( "selectW", "MAX_WORD_UNIT_LIMIT_DEFAULT: " + MAX_WORD_UNIT_LIMIT_DEFAULT );
		Log.d( "selectW", "MAX_WORD_UNIT_LIMIT: " + MAX_WORD_UNIT_LIMIT + " (should not exceed " + MAX_WORD_UNIT_LIMIT_DEFAULT + " )" );
		Log.d( "selectW", "TOTAL_UNITS: " + TOTAL_UNITS );
		
		
			// RANDOMLY CHOOSE 9 WORDS (based on difficulty) //
		
		// NOTE: Random Number generator does not return all range for "long"
		Random rand = new Random( );
		int randPos; //random position to choose
		int n = 0; //used to prevent run-on random generator
		boolean breakOut = false;
		int[] wordUsed = new int[lineCount]; //array for all words used to mark if a word was selected for wordArray
		int wordsUsedSoFar = 0; //stores number of words used so far for wordArr[]
		
		for( int k=0; k<9; k++ ) //loop to find 9 words
		{
			n = 0; //reset
			breakOut = false; //reset
			while( n < 500000 )
			{
				//randPos = rand.nextInt( ) % total; //random position to choose
				randPos = rand.nextInt( ) % (int)TOTAL_UNITS; //random position to choose
				
				
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
							wordsUsedSoFar++;
							break; //do not look for more words
						}
					}
				}
				
				if( breakOut == true )//break after found valid word
				{ break; }
				
				n++;
			}
			
			if( n >= 500000 ) //if exhausted all tries, linearly choose 9 words
			{
				Log.d( "selectW", "WARNING: exhauster all tries in Select9Word" );
				int r = rand.nextInt( ) % lineCount; //get a random position to start
				//loop linearly and choose 9 words
				int c = 0;
				while( c<lineCount && wordsUsedSoFar < 9 )
				{
					
					if( wordUsed[r] == 1 ) //if word already used
					{
						//void
					}
					else //word not previously selected for wordArray
					{
						//USE THIS WORD AS NEW wordArr[k]
						wordArray[wordsUsedSoFar] = new Word( rangeArr[r].getStrNative(), rangeArr[r].getStrTranslation(), r+1, 0 );
						wordsUsedSoFar++;
						wordUsed[r] = 1; //mark word as used
					}
					r = (r+1) % lineCount; //move to next word in rangeArr[]
					c++;
				}
				break; //already found 9 words
			}
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
