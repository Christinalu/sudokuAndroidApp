package com.omicron.android.cmpt276_1191e1_omicron;

class PackageFile
{
	private String wordPackageName = null; //name the user provided for Package
	private String internalFileName = null; //corresponding internal file name
	private String nativeLang = null; //the corresponding native lang
	private String translateLang = null; //the corresponding lang of translation
	private int packageWordPairCount; //the number of words in a package
	
	public PackageFile( String wordPackageName2, String internalFileName2, String nativeLang2,
						String translateLang2, int packageWordPairCount2 )
	{
		wordPackageName = wordPackageName2;
		internalFileName = internalFileName2;
		nativeLang = nativeLang2;
		translateLang = translateLang2;
		packageWordPairCount = packageWordPairCount2;
	}
	
	public String getWordPackageName( )
	{ return wordPackageName; }
	
	public String getInternalFileName( )
	{ return internalFileName; }
	
	public String getNativeLang( )
	{ return nativeLang; }
	
	public String getTranslateLang( )
	{ return translateLang; }
	
	public int getPackageWordPairCount( )
	{ return packageWordPairCount; }
}
