package com.omicron.android.cmpt276_1191e1_omicron;

class PackageFile
{
	private String wordPackageName = null; //name the user provided for Package
	private String internalFileName = null; //corresponding internal file name
	
	public PackageFile( String wordPackageName2, String internalFileName2 )
	{
		wordPackageName = wordPackageName2;
		internalFileName = internalFileName2;
	}
	
	public String getWordPackageName( )
	{ return wordPackageName; }
	
	public String getInternalFileName( )
	{ return internalFileName; }
}
