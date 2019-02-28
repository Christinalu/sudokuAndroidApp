package com.omicron.android.cmpt276_1191e1_omicron;

public class WordPackageFileIndex
{
	/*
	 * This class stores the Word Packages the user has saved so far, along with the internal file name
	 */
	
	private int MAX_WORD_PKG;
	private int[] CURRENT_WORD_PKG_COUNT;
	private PackageFile[] packageFileArr; //stores pairs of Word Package name and internal file name
	
	public WordPackageFileIndex( int MAX_WORD_PKG2, int[] CURRENT_WORD_PKG_COUNT2 )
	{
		String pkgName; //used to store wordPackageName
		String fileName; //used to store internalFileName
		
		MAX_WORD_PKG = MAX_WORD_PKG2;
		CURRENT_WORD_PKG_COUNT = CURRENT_WORD_PKG_COUNT2;

		packageFileArr = new PackageFile[CURRENT_WORD_PKG_COUNT[0]+1]; //as many as user has now +1 for new
		
		//warining: test if PackageFile line above should have size MAX_PKG_COUNT instead if activity nor re-craeted
		
		for( int i=0; i<CURRENT_WORD_PKG_COUNT[0]+1; i++ )
		{
			/////////////
			//
			// here get data from SQL database
			//
			//////////////
			
			//packageFileArr[i] = new PackageFile( pkgName, fileName ); //add pair of WordPackageName and internal_storage_file_name
		}
	}
	
	public void increateCurrentWordCountFile( )
	{
		/*
		 * This method increases the count of current_word_count_file in RAW
		 */
		
		// OVERWRITE TO FILE
		
		
	}
	
	
	// TODO: when adding new file, dont forget to update CURRENT_WORD_PKG_COUNT
}
