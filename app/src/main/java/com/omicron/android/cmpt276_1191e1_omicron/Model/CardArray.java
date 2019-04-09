package com.omicron.android.cmpt276_1191e1_omicron.Model;

import android.util.Log;

import com.omicron.android.cmpt276_1191e1_omicron.WordArray;

import java.io.Serializable;
import java.util.Random;

public class CardArray implements Serializable
{
	/*
	 * This class holds Model data about the cards such as text
	 * 2D array format, if selected
	 */
	
	private String[][] cardArray; // 2D array holding the strings to display on "card"
	private int gridRowCount;
	private int gridColCount;
	private int size;
	private final int LIMIT = 500000;
	
	public CardArray( int gridRowCount2 , int gridColCount2 )
	{
		gridRowCount = gridRowCount2;
		gridColCount = gridColCount2;
		size = gridRowCount * gridColCount;
		
		cardArray = new String[gridRowCount][gridColCount];
	}
	
	public void initializeStringArray(  WordArray wordArray )
	{
		//return 1 if could not initialize array
		
			/** IMPORT AND CREATE STRING 'CARD' ARRAY FROM WORD-ARRAY **/
		
		int[] cardArrayUsed; //array where 1==cardArray already initialized at this index
		cardArrayUsed = new int[gridRowCount*gridColCount];
		int wordCount = wordArray.getWordCount( );
		String word;
		Random random = new Random( );
		int rand;
		int randRow;
		int randCol;
		int limit = 0;
		int init = 0; //flag if string initialized
//		for( int i=0; i<wordCount; i++ ) //loop through word array and randomly import all words
//		{
//			for( int j=0; j<2; j++ ) //import native and translation word
//			{
//				limit = 0;
//				init = 0;
//				if( j == 0 ){
//					word = wordArray.getWordNativeAtIndex( i );
//				}else{
//					word = wordArray.getWordTranslationAtIndex( i );
//				}
//
//				rand = random.nextInt( ) % gridRowCount;
//				randRow = random.nextInt( ) % gridRowCount;
//				randCol = random.nextInt( ) % gridColCount;
//
//				while( cardArrayUsed[randRow][randCol] == 1 && limit < LIMIT ) //loop until finding an index that was not previously initialized
//				{
//					limit++;
//					randRow = random.nextInt( ) % gridRowCount;
//					randCol = random.nextInt( ) % gridColCount;
//				}
//
//				if( limit >= LIMIT ) //check to see if probably index was not found correctly
//				{ return 1; }
//
//				//available index found, place string
//				cardArrayUsed[randRow][randCol] = 1; //mark index as used
//			}
//		}
		//return 0;

		
		//use 1D array to linearly shuffle word array to string array
		//use 1D array mask to represent word array
		int wordLeft = size;
		int indexOfLastInitialized = size-1;
		int temp;
		int[] wordArrayMask = new int[size];
		for( int i=0; i<size; i++ ){ //fill mask with numbers where each number will represent a string word from word array
			wordArrayMask[i] = i;	 //ie i==2 means 2nd word native; i==1 first word translation
		}
		
		for( int i=0; i<size-1; i++ ) //loop, choose a random index and move it to the back (initialized)
		{
			rand = random.nextInt( wordLeft );
			
			//Log.d( "cardArray", "rand: " + rand );
			
			//swap random with last index
			temp = wordArrayMask[indexOfLastInitialized];
			wordArrayMask[indexOfLastInitialized] = wordArrayMask[rand];
			wordArrayMask[rand] = temp;
			
			indexOfLastInitialized--;
			wordLeft--;
		}
		
		if( size == 18 ){
			Log.d("cardArray", "word array rand mask: "+wordArrayMask[0]+", "+wordArrayMask[1]+", "+wordArrayMask[2]+", "+wordArrayMask[3]+", "
					+wordArrayMask[4]+", "+wordArrayMask[5]+", "+wordArrayMask[6]+", "+wordArrayMask[7]+", "+wordArrayMask[8]+", "+wordArrayMask[9]+", "
					+wordArrayMask[10]+", "+wordArrayMask[11]+", "+wordArrayMask[12]+", "+wordArrayMask[13]+", "+wordArrayMask[14]+", "+wordArrayMask[15]+", "
					+wordArrayMask[16]+", "+wordArrayMask[17]);
		}
		
		// CONVERT MASK TO STRING (from wordArray to cardArray)
		int k=0; //index to loop in 1D mark
		int index; //which word to get from word array; ie index==2 means 2nd word native; index==1 first word translation
		int wordIndex; //which index pair from wordArray
		for( int i=0; i<gridRowCount; i++ ) //for each mask, get the actual word array string
		{
			for( int j=0; j<gridColCount; j++ )
			{
				index = wordArrayMask[k]; //for each mask
				wordIndex = index/2; //index of word pair
				
				if( wordIndex*2 == index ){ //if native word
					cardArray[i][j] = wordArray.getWordNativeAtIndex( wordIndex );
					Log.d( "cardArray", index + " native" );
				}else{ //remainder 1
					cardArray[i][j] = wordArray.getWordTranslationAtIndex( wordIndex );
					Log.d( "cardArray", index + " translation" );
				}
				
				k++;
			}
		}
		
		////// debug //////////////////
		if( size == 18 ){ //debug
			for( int i=0; i<gridRowCount; i++ )
			{
				if( gridColCount == 2 ) {
					Log.d("cardArray", "cardArray: " + cardArray[i][0] + ", " + cardArray[i][1] );
				}
				else if( gridColCount == 3 ){
					Log.d("cardArray", "cardArray: " + cardArray[i][0] + ", " + cardArray[i][1] + ", " + cardArray[i][2] );
				}
			}
			
		}
		///////////////////////////////
		
	}
}



































