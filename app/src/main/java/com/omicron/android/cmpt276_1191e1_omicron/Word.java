package com.omicron.android.cmpt276_1191e1_omicron;
import java.io.Serializable;

public class Word implements Serializable
{
	/*
	 * This Object holds a word in native language and its translation
	*/
	private String mNative;
	private String mTranslation;

	//create a word
	public Word( String wordNative, String wordTranslation )
	{
		mNative = wordNative;
		mTranslation = wordTranslation;
	}

	//get native word
	public String getNative(  ){ return mNative; };

	//get translation word
	public String getTranslation(  ){ return mTranslation; };
}
