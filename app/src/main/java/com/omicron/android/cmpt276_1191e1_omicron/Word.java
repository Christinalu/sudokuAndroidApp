package com.omicron.android.cmpt276_1191e1_omicron;
import java.io.Serializable;

public class Word implements Serializable
{
	/*
	 * This Object holds a word in native language and its translation
	*/
	
	private String mNative;
	private String mTranslation;
	private int mInFileLineNum; //stores on which line in .csv this word is found
	private long mHintClick; //stores the number of times the user had difficulty with a word

	//create a word
	public Word( String wordNative, String wordTranslation, int inFileLineNum, long hintClick )
	{
		mNative = wordNative;
		mTranslation = wordTranslation;
		mInFileLineNum = inFileLineNum;
		mHintClick = hintClick;
	}

	//get native word
	public String getNative(  ){ return mNative; }

	//get translation word
	public String getTranslation(  ){ return mTranslation; }

	//set native word
	public void setNative(String s) {
		mNative = s;
		return;
	}
	//set translation word
	public void setTranslation(String s) {
		mTranslation = s;
		return;
	}


	public int getInFileLineNum(  ){ return mInFileLineNum; }
	
	public void updateHintClick( int newHintClick ){ mHintClick = newHintClick; }
	
	public void incrementHintClick( ){ mHintClick++; }
	
	public long getHintClick(  ){ return mHintClick; }
}
