package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import com.omicron.android.cmpt276_1191e1_omicron.Model.CardArray;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class CardArrayTest
{
	@Before
	public void setUp() throws Exception { 	}
	@Test 	public void useAppContext() {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();
		assertEquals("com.omicron.android.cmpt276_1191e1_omicron", appContext.getPackageName());
	}
	
	int gridRowCount = 9;
	int gridColCount = 2;
	CardArray cardArray = new CardArray( gridRowCount, gridColCount );
	Context context = InstrumentationRegistry.getTargetContext();
	int size = gridRowCount*gridColCount;
	
	int usrPuzzleTypePref = 1;
	int MAX_CSV_ROW = 50;
	int HINT_CLICK_TO_MAX_PROB = 15;
	int usrModePref = 0;
	
	WordArray wordArray = new WordArray( usrPuzzleTypePref, MAX_CSV_ROW, HINT_CLICK_TO_MAX_PROB, usrModePref );
	
	@Test
	public void initializeStringArray( )
	{
		try {
			wordArray.initializeWordArray( context, "pkg_0.csv" );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int k=0;
		int mask;
		boolean res = false;
		int wordIndex;
		String cardString;
		String wordArrayString;
		cardArray.initializeStringArray( wordArray );
		
		//assert the strings match those in wordArray
		for( int i=0; i<gridRowCount; i++ )
		{
			for( int j=0; j<gridColCount; j++ ) {
				mask = cardArray.getWordArrayMaskAtIndex( k );
				cardString = cardArray.getCardStringAtIndex( i, j, gridColCount );
				wordIndex = mask/2; //index of word pair
				
				if( wordIndex*2 == mask ){ //if native word
					wordArrayString = wordArray.getWordNativeAtIndex( wordIndex );
				}else{
					wordArrayString = wordArray.getWordTranslationAtIndex( wordIndex );
				}
				
				res = cardString.contentEquals( wordArrayString );
				
				assertEquals( res, true );
				
				k++;
			}
		}
	}
	
	
	@Test
	public void checkIfPairMatch( )
	{
		try {
			wordArray.initializeWordArray( context, "pkg_0.csv" );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int i=0;
		int j=0;
		boolean res = false;
		
		cardArray.initializeStringArray( wordArray );
		
		for( i=0; i<size; i++ )
		{
			for( j=0; j<size; j++ )
			{
				if( cardArray.getCardKeyAtIndex( i ) == cardArray.getCardKeyAtIndex( j ) )
				{
					res = true;
				}else{
					res = false;
				}
				
				assertEquals( res, cardArray.checkIfPairMatch( i, j ) );
			}
		}
	}
	
	
	@Test
	public void getCardStringAtIndex( )
	{
		try {
			wordArray.initializeWordArray( context, "pkg_0.csv" );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int mask;
		int wordIndex;
		String wordArrayString;
		
		cardArray.initializeStringArray( wordArray );
		
		for( int i=0; i<gridRowCount; i++ )
		{
			for( int j=0; j<gridColCount; j++ )
			{
				mask = cardArray.getWordArrayMaskAtIndex( i*gridColCount+j );
				wordIndex = mask/2; //index of word pair
				
				if( wordIndex*2 == mask ){ //if native word
					wordArrayString = wordArray.getWordNativeAtIndex( wordIndex );
				}else{
					wordArrayString = wordArray.getWordTranslationAtIndex( wordIndex );
				}
				
				assertEquals( wordArrayString, cardArray.getCardStringAtIndex( i, j, gridColCount) );
			}
		}
	}
}