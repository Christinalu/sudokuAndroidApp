package com.omicron.android.cmpt276_1191e1_omicron.Model;

import android.content.Intent;
import android.os.Bundle;
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
	
	private String[] cardArray; // 1D array holding the strings to display on "card"
	private int[] cardKey;  	//stores index of which word the native/translation is from
								// ie  if cardKey[0] and cardKey[2] are both the same (same word index == 5),
								// it implies the translations match that is, both are from 5th word pair
								// (cardKey[0] --> Four, cardKey[2] --> Quattre; translations of same word)
	private int gridRowCount;
	private int gridColCount;
	private int size;
	
	
	public CardArray( int gridRowCount2 , int gridColCount2 )
	{
		gridRowCount = gridRowCount2;
		gridColCount = gridColCount2;
		size = gridRowCount * gridColCount;
		
		cardArray = new String[gridRowCount*gridColCount];
		cardKey = new int[gridRowCount*gridColCount];
	}
	
	
	public void initializeStringArray(  WordArray wordArray )
	{
		
			/** IMPORT AND CREATE STRING 'CARD' ARRAY FROM WORD-ARRAY **/
		
		int rand;
		Random random = new Random( );

		//use 1D array to linearly shuffle word array to string array
		//use 1D array mask to represent word array
		int wordLeft = size;
		int indexOfLastInitialized = size-1;
		int temp;
		int indexLastMaskKey = 0; //stores int index from wordArrayMask of where current translation has correct key index (in wordArrayMask)
		int indexCurrentMaskKey = 0;
		int[] wordArrayMask = new int[size];
		
		for( int i=0; i<size; i++ ){ //fill mask with numbers where each number will represent a string word from word array
			wordArrayMask[i] = i;	 //ie i==2 means 2nd word native; i==1 first word translation
		}
		
		for( int i=0; i<size-1; i++ ) //loop, choose a random index and move it to the back (initialized)
		{
			rand = random.nextInt( wordLeft ); //index of which int from wordArrayMask to randomize
			
			//swap random with last index
			temp = wordArrayMask[indexOfLastInitialized];
			wordArrayMask[indexOfLastInitialized] = wordArrayMask[rand];
			wordArrayMask[rand] = temp;
			
//			//create key
//			if( rand%2 == 0 ){ //if native word
//				indexLastMaskKey = rand;
//			}
//			else{ //translation word
//				cardKey[rand] = indexLastMaskKey; //first save key of native
//				cardKey[indexLastMaskKey] = rand; //also save current translation index to (previous) native word
//			}
			
			indexOfLastInitialized--;
			wordLeft--;
		}
		
		if( size == 18 ){
			Log.d("cardArray", "word array rand mask: "+wordArrayMask[0]+", "+wordArrayMask[1]+", "+wordArrayMask[2]+", "+wordArrayMask[3]+", "
					+wordArrayMask[4]+", "+wordArrayMask[5]+", "+wordArrayMask[6]+", "+wordArrayMask[7]+", "+wordArrayMask[8]+", "+wordArrayMask[9]+", "
					+wordArrayMask[10]+", "+wordArrayMask[11]+", "+wordArrayMask[12]+", "+wordArrayMask[13]+", "+wordArrayMask[14]+", "+wordArrayMask[15]+", "
					+wordArrayMask[16]+", "+wordArrayMask[17]);
		}
		
		
			/* CONVERT MASK TO STRING (from wordArray to cardArray) */
		
		int k=0; //index to loop in 1D mark
		int index; //which word to get from word array; ie index==2 means 2nd word native; index==1 first word translation
		int wordIndex; //which index pair from wordArray
		for( int i=0; i<size; i++ ) //for each mask, get the actual word array string
		{
			index = wordArrayMask[k]; //for each mask
			wordIndex = index/2; //index of word pair
				
			if( wordIndex*2 == index ){ //if native word
				cardArray[i] = wordArray.getWordNativeAtIndex( wordIndex );
				cardKey[i] = wordIndex; //save which word pair this is
				//Log.d( "cardArray", index + " native" );
			}else{ //remainder 1
				cardArray[i] = wordArray.getWordTranslationAtIndex( wordIndex );
				cardKey[i] = wordIndex; //save which word pair this is
				//Log.d( "cardArray", index + " translation" );
			}
				
			k++;
		}
		
		
		////// debug //////////////////
		if( size == 18 ){
			Log.d("cardArray", "cardArray: "+cardArray[0]+", "+cardArray[1]+", "+cardArray[2]+", "+cardArray[3]+", "
					+cardArray[4]+", "+cardArray[5]+", "+cardArray[6]+", "+cardArray[7]+", "+cardArray[8]+", "+cardArray[9]+", "
					+cardArray[10]+", "+cardArray[11]+", "+cardArray[12]+", "+cardArray[13]+", "+cardArray[14]+", "+cardArray[15]+", "
					+cardArray[16]+", "+cardArray[17]);
		}
		if( size == 18 ){
			Log.d("cardArray", "cardKey: "+cardKey[0]+", "+cardKey[1]+", "+cardKey[2]+", "+cardKey[3]+", "
					+cardKey[4]+", "+cardKey[5]+", "+cardKey[6]+", "+cardKey[7]+", "+cardKey[8]+", "+cardKey[9]+", "
					+cardKey[10]+", "+cardKey[11]+", "+cardKey[12]+", "+cardKey[13]+", "+cardKey[14]+", "+cardKey[15]+", "
					+cardKey[16]+", "+cardKey[17]);
		}
		///////////////////////////////
		
	}
	
	
	public boolean checkIfPairMatch( int p1, int p2 )
	{
		/*
		 * This function checks if the two coordinate pairs both
		 * are from that same word (if both strings are from same word)
		 * Return true if they match
		 * On out of bounds return false
		 */
		
//		int p1row = p1.getRow();
//		int p1col = p1.getColumn();
//		int p2row = p2.getRow();
//		int p2col = p2.getColumn();
//
//		if( 0 > p1row || p1row >= gridRowCount || //out of bounds
//			0 > p1col || p1col >= gridColCount ||
//			0 > p2row || p2row >= gridRowCount ||
//			0 > p2col || p2col >= gridColCount )
//		{ return false; }
		
		if( p1 < 0 || p1 >= size || p2 < 0 || p2 >= size ) //out of bounds
		{ return false; }
		
		if( cardKey[p1] == cardKey[p2] ) //if both strings from same word
		{ return true; }
		else { return false; }
	}
	
	public String getCardStringAtIndex( int i, int j, int colCount )
	{
		/*
		 * Return string associated with that card
		 * from 1D array extract using 2D coordinate
		 */
		return  cardArray[i*colCount+j];
	}
	
	public void saveDataForRotation( Bundle state )
	{
		/*
		 * This function adds all necessary data to intent to be saved when rotating
		 */
		
		state.putSerializable( "cardArray", cardArray );
		state.putSerializable( "cardKey", cardKey );
		state.putInt( "gridRowCount", gridRowCount );
		state.putInt( "gridColCount", gridColCount );
		state.putInt( "size", size );
	}
	
	public void saveDataForResume( Intent intent )
	{
		/*
		 * This function adds all necessary data to intent to be saved for resume
		 */
		
		intent.putExtra( "cardArray", cardArray );
		intent.putExtra( "cardKey", cardKey );
		intent.putExtra( "gridRowCount", gridRowCount );
		intent.putExtra( "gridColCount", gridColCount );
		intent.putExtra( "size", size );
	}
	
	public void restoreFromRotation( Bundle state )
	{
		/*
		 * This function restores data when device rotated
		 */
		
		cardArray = (String[]) state.getSerializable( "cardArray" );
		cardKey = (int[]) state.getSerializable( "cardKey" );
		gridRowCount = state.getInt( "gridRowCount" );
		gridColCount = state.getInt( "gridColCount" );
		size = state.getInt( "size" );
	}
	
	public void restoreFromResume( Intent intent )
	{
		/*
		 * This function restores data when resuming game
		 */
		
		cardArray = (String[]) intent.getSerializableExtra( "cardArray" );
		cardKey = (int[]) intent.getSerializableExtra( "cardKey" );
		gridRowCount = (int) intent.getSerializableExtra( "gridRowCount" );
		gridColCount = (int) intent.getSerializableExtra( "gridColCount" );
		size = (int) intent.getSerializableExtra( "size" );
	}
}



































