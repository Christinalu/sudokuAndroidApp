package com.omicron.android.cmpt276_1191e1_omicron;

public class Word
{
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
