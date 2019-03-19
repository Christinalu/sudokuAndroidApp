package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Random;

public class WordArray implements Parcelable
{
	/*
	 * This class stores the wordArray initialized according to user preference
	 * It also adapts wordArray depending on user Puzzle Type preference
	 */
	
	private Word[] wordArray; //stores the array with words
	private int wordCount; //stores how many words in wordArray
	//private RadioGroup radGroup;
	private int usrPuzzleTypePref = -1;
	private static int MAX_CSV_ROW;
	private static int HINT_CLICK_TO_MAX_PROB;
	
	//stores the puzzle sizes
	private static int size4x4 = 0;
	private static int size6x6 = 1;
	private static int size9x9 = 2;
	private static int size12x12 = 3;
	
	
		/* Override Parcelable */
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable( wordArray );
		dest.writeInt( wordCount );
		dest.writeInt( usrPuzzleTypePref );
		dest.writeInt( MAX_CSV_ROW );
		dest.writeInt( HINT_CLICK_TO_MAX_PROB );
		
		dest.writeInt( size4x4 );
		dest.writeInt( size6x6 );
		dest.writeInt( size9x9 );
		dest.writeInt( size12x12 );
	}
	
	public WordArray( Parcel in )
	{
		//this.id = in.readString();
		
		wordArray = (Word[]) in.readSerializable( );
		wordCount = in.readInt( );
		usrPuzzleTypePref = in.readInt( );
		MAX_CSV_ROW = in.readInt( );
		HINT_CLICK_TO_MAX_PROB = in.readInt( );
		
		size4x4 = in.readInt( );
		size6x6 = in.readInt( );
		size9x9 = in.readInt( );
		size12x12 = in.readInt( );
	}
	
	public static final Parcelable.Creator<WordArray> CREATOR = new Parcelable.Creator<WordArray>() {
		
		public WordArray createFromParcel(Parcel in) {
			return new WordArray(in);
		}
		
		public WordArray[] newArray(int size) {
			return new WordArray[size];
		}
	};
	
	
	public WordArray( int usrPuzzleTypePref2, int MAX_CSV_ROW2, int HINT_CLICK_TO_MAX_PROB2 )
	{
		// SET UP ARRAY TO STORE WORDS
		// a Word (pair) contains the word in native language, and its translation
		//	the n-2 th index contains the corresponding language
		//	the n-1 th index contains the internal storage file name
		
		usrPuzzleTypePref = usrPuzzleTypePref2;
		MAX_CSV_ROW = MAX_CSV_ROW2;
		HINT_CLICK_TO_MAX_PROB = HINT_CLICK_TO_MAX_PROB2;
	}
	
	
	public int setUpBasedOnUserTypePreference( )
	{
		// returns -1 if user preference not initialized
		if( usrPuzzleTypePref == size4x4 )
		{ wordCount = 4; }
		else if( usrPuzzleTypePref == size6x6 )
		{ wordCount = 6; }
		else if( usrPuzzleTypePref == size9x9 )
		{ wordCount = 9; }
		else if( usrPuzzleTypePref == size12x12 )
		{ wordCount = 12; }
		else
		{ return -1; }
		
		wordArray = new Word[wordCount + 2]; //+2 for language and pkg name
		Log.d( "selectW", "usrPuzzleTypePref: " + usrPuzzleTypePref );
		Log.d( "selectW", "wordArray wordCount: " + wordCount );
		return 0;
	}
	

	public String getNativeLang( )
	{ return wordArray[(wordArray.length-2)].getNative(); }
	
	public String getTranslationLang( )
	{ return wordArray[(wordArray.length-2)].getTranslation(); }
	
	public String getPackageName( )
	{ return wordArray[(wordArray.length-1)].getNative(); }
	
	
	public int initializeWordArray( Context context, String fileNameSelected ) throws IOException //
	{
		/*
		 * This function initializes the  word array to 'n' words based on pkg selected for user for GameActivity
		 * Returns 1 if could not generate an array
		 */
		
		int res = setUpBasedOnUserTypePreference( );
		if( res == -1 ){ return 1; } //failed to initialize
		
		/* OPEN PKG FILE TO READ */
		
		FileInputStream fileInStream = null; //open file from internal storage
		try {
			fileInStream = context.openFileInput( fileNameSelected ); //get internal file name, contained in 10th index of wordArray
		} catch (FileNotFoundException e) {
			Log.d( "upload", "ERROR: exception int initializeWordArr( )" );
			e.printStackTrace();
		}
		InputStreamReader inStreamRead = new InputStreamReader( fileInStream );
		BufferedReader buffRead = new BufferedReader( inStreamRead );
		
		//call function to modify wordArray[] and select the words
		res = selectWord( wordArray, fileNameSelected, buffRead, HINT_CLICK_TO_MAX_PROB );
		
		//// debug ////////
		for( int i=0; i<wordCount; i++ )
		{
			Log.d( "selectW", "wordArr[] " + i + " file line: " + wordArray[i].getInFileLineNum() );
		}
		///////////////////
		
		return res;
	}
	
	
	public int getUsrPuzzleTypePref( )
	{ return usrPuzzleTypePref; }
	
	public Word[] getWordArray( )
	{ return wordArray; }
	
	public int getWordCount( )
	{ return wordCount; } //returns -1 on error
	
	public int getSize4x4( )
	{ return size4x4; }
	
	public int getSize6x6( )
	{ return size6x6; }
	
	public int getSize9x9( )
	{ return size9x9; }
	
	public int getSize12x12( )
	{ return size12x12; }
	
	
	
	
	
	public String getWordNativeAtIndex( int i )
	{
		//returns empty string on error
		if( i < 0 || i >= wordCount )
		{ return ""; } //invalid index
		
		return wordArray[i].getNative( );
	}
	
	public String getWordTranslationAtIndex( int i )
	{
		//returns empty string on error
		if( i < 0 || i >= wordCount )
		{ return ""; } //invalid index
		
		return wordArray[i].getTranslation( );
	}
	
	public int setWordNativeAtIndex( int i, String str )
	{
		//returns -1 on error
		if( i < 0 || i >= wordCount )
		{ return -1; } //invalid index
		wordArray[i].setNative( str );
		return 0;
	}
	
	public int setWordTranslationAtIndex( int i, String str )
	{
		//returns -1 on error
		if( i < 0 || i >= wordCount )
		{ return -1; } //invalid index
		wordArray[i].setTranslation( str );
		return 0;
	}
	
	public int getWordInFileLineNumAtIndex( int i )
	{
		//returns -1 on error
		if( i < 0 || i >= wordCount )
		{ return -1; } //invalid index
		
		return wordArray[i].getInFileLineNum( );
	}
	
	public long getWordHintClickAtIndex( int i )
	{
		//returns -1 on error
		if( i < 0 || i >= wordCount )
		{ return -1; } //invalid index
		
		return wordArray[i].getHintClick( );
	}
	
	public int wordUpdateHintClickAtIndex( int i, int newHintClick )
	{
		//returns -1 on error
		if( i < 0 || i >= wordCount )
		{ return -1; } //invalid index
		
		wordArray[i].updateHintClick( newHintClick );
		return 0;
	}
	
	public int wordIncrementHintClickAtIndex( int i )
	{
		//returns -1 on error
		if( i < 0 || i >= wordCount )
		{ return -1; } //invalid index
		
		wordArray[i].incrementHintClick( );
		return 0;
	}
	
	public boolean getWordAlreadyUsedInGameAtIndex( int i )
	{
		//returns true on error
		if( i < 0 || i >= wordCount )
		{ return true; } //assume true on error
		
		return wordArray[i].getAlreadyUsedInGame( );
	}
	
	public int setWordUsedInGameAtIndex( int i )
	{
		//returns -1 on error
		if( i < 0 || i >= wordCount )
		{ return -1; } //assume true on error
		
		wordArray[i].setUsedInGame( );
		return 0;
	}
	
	public boolean getWordAllowToDecreaseDifficultyAtIndex( int i )
	{
		//returns false on error
		if( i < 0 || i >= wordCount )
		{ return false; } //assume false on error
		
		return wordArray[i].getAllowToDecreaseDifficulty( );
	}
	
	public int setWordToAllowToDecreaseDifficultyAtIndex( int i )
	{
		//returns -1 on error
		if( i < 0 || i >= wordCount )
		{ return -1; } //assume false on error
		
		wordArray[i].setToAllowToDecreaseDifficulty( );
		return 0;
	}
	
	public int setWordDoNotAllowToDecreaseDifficultyAtIndex( int i )
	{
		//returns -1 on error
		if( i < 0 || i >= wordCount )
		{ return -1; } //assume false on error
		
		wordArray[i].setDoNotAllowToDecreaseDifficulty( );
		return 0;
	}
		
	
	
		/** SELECT FUNCTION **/
		
	public int selectWord( Word[] wordArray, String fileNameSelected, BufferedReader buffRead,
						   int HINT_CLICK_TO_MAX_PROB ) throws IOException
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
		int SINGLE_WORD_BLOCK_UNIT_SZ; //depending on SINGLE_WORD_BLOCK_UNIT_SZ_CONST and HintClick, this will represent units of single word OF 1 HINT-COUNT
		int WORD_INCREASE_UNIT_MULTIP = 10; //multiplier which increases word_unit depending on how many words there are, ie if == 10; then 10 words == 100 total units and 100 words == 1000 total units
		float MAX_PERCENTAGE_OF_TOTAL = 0.1f; //stores a percentage of how much probability a word is allowed to gain; ie if == 0.2 and total_units == 1000 (ie 10 words with 100 units), then the max units a word can have would be 0.2*1000 = 200 units max per block
		int MAX_WORD_UNIT_LIMIT_DEFAULT; //stores the maximum units that a single word can have at any time
		int MAX_WORD_UNIT_LIMIT; //same as MAX_WORD_UNIT_LIMIT_DEFAULT but adjusted for HintClick
		long TOTAL_UNITS = 0; //stores all the units from all the HintCount blocks //needs to be long ( > int)
		int WORD_UNIT_INCREASE_PER_HINT_CLICK; //defines how many units are added per HintClick (when HintClick > 1)
		
		
			/** STATISTICALLY CHOOSE MOST DIFFICULT WORD BASED ON HINT CLICK**/
		
		String line;
		String[] strSplit;
		
		int lineCount = 0; //also represents how many word pairs in file
		int i = 0;
		int total = 0; //stores total number of Click Count
		long totalBk = 0; //back up for "total"
		Range[] rangeArr = new Range[MAX_CSV_ROW]; //array for each word pair, storing the range of "hint clicks"
		
		Log.d("upload", " @ WORD_ARRAY ON START GAME BTN CLICK:");
		
		//set pkg name
		wordArray[wordCount+1] = new Word( fileNameSelected, "", -1, -1 );
		
		while( (line = buffRead.readLine( )) != null ) //loop and get all lines
		{
			if( i == 0 ) //get language from file
			{
				strSplit = line.split( "," );
				wordArray[wordCount] = new Word( strSplit[0], strSplit[1], -1, -1 );
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
		////////////////////////
		
		
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
		
		//create UNIT based Range structure
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
		////////////////////////
		
		
		Log.d( "selectW", "lineCount: " + lineCount );
		Log.d( "selectW", "WORD_UNIT_INCREASE_PER_HINT_CLICK: " + WORD_UNIT_INCREASE_PER_HINT_CLICK );
		Log.d( "selectW", "totalUnitsDefault: " + totalUnitsDefault );
		Log.d( "selectW", "SINGLE_WORD_BLOCK_UNIT_SZ: " + SINGLE_WORD_BLOCK_UNIT_SZ );
		Log.d( "selectW", "MAX_WORD_UNIT_LIMIT_DEFAULT: " + MAX_WORD_UNIT_LIMIT_DEFAULT );
		Log.d( "selectW", "MAX_WORD_UNIT_LIMIT: " + MAX_WORD_UNIT_LIMIT + " (should not exceed " + MAX_WORD_UNIT_LIMIT_DEFAULT + " )" );
		Log.d( "selectW", "TOTAL_UNITS: " + TOTAL_UNITS );
		
		
			/* RANDOMLY CHOOSE 'N' WORDS (based on difficulty) */
		
		Random rand = new Random( );
		int randPos; //random position to choose
		int n = 0; //used to prevent run-on random generator
		boolean breakOut = false;
		int[] wordUsed = new int[lineCount]; //array for all words used to mark if a word was selected for wordArray
		int wordsUsedSoFar = 0; //stores number of words used so far for wordArr[]
		
		for( int k=0; k<wordCount; k++ ) //loop to find all words
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
			
			if( n >= 500000 ) //if exhausted all tries, linearly choose 'n' words
			{
				Log.d( "selectW", "WARNING: exhauster all tries in selectWord" );
				int r = rand.nextInt( ) % lineCount; //get a random position to start
				//loop linearly and choose 'n' words
				int c = 0;
				while( c<lineCount && wordsUsedSoFar < wordCount )
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
				break; //already found 'n' words
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
