package com.omicron.android.cmpt276_1191e1_omicron;

public class Range
{
	private long left; // left range index
	private long right; // right range index
	private String strNative;
	private String strTranslation;
	private int hintClick;
	
	public Range( long numLeft, long numRight , String strNative2, String strTranslation2, int hintClick2 )
	{
		left = numLeft;
		right = numRight;
		strNative = strNative2;
		strTranslation = strTranslation2;
		hintClick = hintClick2;
	}
	
	public long getNumLeft( )
	{
		return left;
	}
	public long getNumRight( )
	{
		return right;
	}
	public String getStrNative( ){ return strNative; }
	public String getStrTranslation( ){ return strTranslation; }
	public int getHintClick( ){ return hintClick; }
}