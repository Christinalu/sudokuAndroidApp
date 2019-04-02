package com.omicron.android.cmpt276_1191e1_omicron.View;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.omicron.android.cmpt276_1191e1_omicron.Model.RelativeAndPos;
import com.omicron.android.cmpt276_1191e1_omicron.SudokuGenerator;
import com.omicron.android.cmpt276_1191e1_omicron.WordArray;

public class TextMatrix
{
	private float TXT_SIZE_NORMAL;
	private float TXT_SIZE_ZOOM;
	
	private float TXT_SIZE_RATIO_4x4 = 0.16f; //ratio to determine size of text inside square
	private float TXT_SIZE_RATIO_6x6 = 0.25f;
	private float TXT_SIZE_RATIO_9x9 = 0.3846f;
	private float TXT_SIZE_RATIO_12x12 = 0.45f;
	
	private float[] ZOOM_SCALE; //puzzle zoom
	private float ZOOM_SCALE_TXT; //text zoom
	private float ZOOM_SCALE_RATIO_4x4;
	private float ZOOM_SCALE_RATIO_6x6;
	private float ZOOM_SCALE_RATIO_9x9;
	private float ZOOM_SCALE_RATIO_12x12;

	private RelativeAndPos[][] textViewArr; //holds all text views
	private Context gameActivity;
	private int sqrSizeWidth;
	private int sqrSizeHeight;
	private int puzzleTypeSize; //stores number or row/col a puzzle has

	public TextMatrix( Context context, int sqrSizeWidth2, int sqrSizeHeight2, float[] ZOOM_SCALE2,
					   int puzzleTypeSize2 )
	{
		/*
		 * NOTE: individual LinearLayout are required for each TextView to fix issue where
		 *       changing single setText() resulted in resetting animation for all TextView
		 *       (because when calling setText() it re-wraps all texts in single Layout, so have to create multiple Layouts)
		 */
		
		puzzleTypeSize = puzzleTypeSize2;
		textViewArr = new RelativeAndPos[puzzleTypeSize][puzzleTypeSize];

		for( int i=0; i<puzzleTypeSize; i++ )
		{
			for( int j=0; j<puzzleTypeSize; j++ )
			{
				textViewArr[i][j] = new RelativeAndPos( context );
				textViewArr[i][j].getRelativeLayout().addView( new TextView( context ) );

			}
		}

		gameActivity = context;
		sqrSizeWidth = sqrSizeWidth2;
		sqrSizeHeight = sqrSizeHeight2;
		ZOOM_SCALE = ZOOM_SCALE2;
		
		Log.d( "screen", "sqrSizeWidth: " + sqrSizeWidth + " sqrSizeHeight: " +sqrSizeHeight );
		
		// NOTE: ZOOM_SCALE should be >=1
		if( puzzleTypeSize == 4 ){ // if 4x4 puzzle type
			TXT_SIZE_NORMAL = 14;//sqrSizeHeight * TXT_SIZE_RATIO_4x4;
			ZOOM_SCALE_TXT = (ZOOM_SCALE[0] - 1f)/2f + 1; //when zooming in, increase text font size but at a slower rate than square size, so in zoom mode, it will fit more of a word
			//TXT_SIZE_ZOOM = TXT_SIZE_NORMAL * (ZOOM_SCALE_TXT); //use this when scaling text as well
			TXT_SIZE_ZOOM = TXT_SIZE_NORMAL; //keep the same text size when zooming
		}
		else if( puzzleTypeSize == 6 ){
			TXT_SIZE_NORMAL = 13;//sqrSizeHeight * TXT_SIZE_RATIO_6x6;
			ZOOM_SCALE_TXT = (ZOOM_SCALE[0] - 1f)/2f + 1;
			//TXT_SIZE_ZOOM = TXT_SIZE_NORMAL * (ZOOM_SCALE_TXT);
			TXT_SIZE_ZOOM = TXT_SIZE_NORMAL;
		}
		else if( puzzleTypeSize == 13 ){
			TXT_SIZE_NORMAL = 12;//sqrSizeHeight * TXT_SIZE_RATIO_12x12;
			ZOOM_SCALE_TXT = (ZOOM_SCALE[0] - 1f)/2f + 1;
			//TXT_SIZE_ZOOM = TXT_SIZE_NORMAL * (ZOOM_SCALE_TXT);
			TXT_SIZE_ZOOM = TXT_SIZE_NORMAL;
		}
		else {
			TXT_SIZE_NORMAL = 13;//sqrSizeHeight * TXT_SIZE_RATIO_9x9;
			ZOOM_SCALE_TXT = (ZOOM_SCALE[0] - 1f)/2f + 1;
			//TXT_SIZE_ZOOM = TXT_SIZE_NORMAL * (ZOOM_SCALE_TXT);
			TXT_SIZE_ZOOM = TXT_SIZE_NORMAL;
		}
	}


	public void scaleTextZoom( float ZOOM_SCALE )
	{
		RelativeLayout.LayoutParams parameter = new RelativeLayout.LayoutParams( (int)( sqrSizeWidth*ZOOM_SCALE),
													(int)( sqrSizeHeight*ZOOM_SCALE ) );//height and width are in pixel
		
		//SCALE TEXT IN ZOOM MODE
		//SCALING ADAPTS TO ZOOM: as zoom increases, so does text size, but at a slower rate
		//	so that more of the word can be shown
		if( ZOOM_SCALE == 1 ){ //do not do anything in default zoom
			TXT_SIZE_ZOOM = TXT_SIZE_NORMAL;
		}
		else if( puzzleTypeSize == 4  ){
			TXT_SIZE_ZOOM = TXT_SIZE_NORMAL * ((ZOOM_SCALE - 1f)/5f + 1);
			
		}
		else if( puzzleTypeSize == 6  ){
			TXT_SIZE_ZOOM = TXT_SIZE_NORMAL * ((ZOOM_SCALE - 1f)/4f + 1);
			
		}
		else if( puzzleTypeSize == 9  ){
			TXT_SIZE_ZOOM = TXT_SIZE_NORMAL * ((ZOOM_SCALE - 1f)/5.5f + 1);
			
		}
		else if( puzzleTypeSize == 12 ){
			TXT_SIZE_ZOOM = TXT_SIZE_NORMAL * ((ZOOM_SCALE - 1f)/11f + 1);
		}
		
		Log.d( "zoom-1", "TXT_SIZE_ZOOM: " + TXT_SIZE_ZOOM );
		
		//this method scales all of the text in "zoom in" mode
		for( int i=0; i<puzzleTypeSize; i++ )
		{
			for( int j=0; j<puzzleTypeSize; j++ )
			{
				textViewArr[i][j].getRelativeLayout().setX( textViewArr[i][j].getRelativeLayout().getX( ) * ZOOM_SCALE ); //set x,y coordinate to multiple of zoom
				textViewArr[i][j].getRelativeLayout().setY( textViewArr[i][j].getRelativeLayout().getY( ) * ZOOM_SCALE );

				textViewArr[i][j].getRelativeLayout().setLayoutParams( parameter );


				//upscale text size
				( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setTextSize( TXT_SIZE_ZOOM );
			}
		}
	}


	/*public void scaleTextZoomOut( float ZOOM_SCALE )
	{
		RelativeLayout.LayoutParams parameter = new RelativeLayout.LayoutParams( (int)(sqrSizeWidth), (int)(sqrSizeHeight) );//height and width are in pixel

		//this method scales all of the text in zoom mode
		for( int i=0; i<puzzleTypeSize; i++ )
		{
			for( int j=0; j<puzzleTypeSize; j++ )
			{
				textViewArr[i][j].getRelativeLayout().setX( textViewArr[i][j].getSqrL() ); //reset x,y coordinate to original
				textViewArr[i][j].getRelativeLayout().setY( textViewArr[i][j].getSqrT() );

				textViewArr[i][j].getRelativeLayout().setLayoutParams( parameter );

				//downscale text size
				( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setTextSize( TypedValue.COMPLEX_UNIT_PX, TXT_SIZE_NORMAL );
			}
		}
	}*/


	public void reDrawTextZoom( int[] touchXZ, int[] touchYZ, int[] dX, int[] dY )
	{
		/*
		 * Draw the text according to 'drag' coordinates in zoom mode
		 */

		for( int i=0; i<puzzleTypeSize; i++ )
		{
			for( int j=0; j<puzzleTypeSize; j++ )
			{
				textViewArr[i][j].getRelativeLayout().setX( textViewArr[i][j].getSqrL() * ZOOM_SCALE[0] + ( -touchXZ[0] + dX[0] ) ); //set x,y coordinate
				textViewArr[i][j].getRelativeLayout().setY( textViewArr[i][j].getSqrT() * ZOOM_SCALE[0] + ( -touchYZ[0] + dY[0] ) );
			}
		}
	}


	public void chooseLangAndDraw(int i, int j, WordArray wordArray, SudokuGenerator usrSudokuArr,
                                  int usrLangPref )
	{
		// CHOOSE WHAT LANGUAGE TO DRAW IN  TEXT-VIEW MARQUEE
		// note: usrLangPref refers to text that the user is typing in the puzzle (ie if usrLangPref = 0 = native, then the (unchangeable) text inside puzzle is translation)
		// matrix text: text inside matrix that cannot be modified
		// user text: the text that the user inputs

		if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]!=0 && usrLangPref == 0 ) // draw only if the puzzle contains a number; and draw the native translated word
		{
			// matrix text - draw translation (because user chose = 0 = native, the matrix is =1=translation)
			( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setText( wordArray.getWordTranslationAtIndex( usrSudokuArr.Puzzle[i][j] - 1 ) );
		}
		else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]!=0 && usrLangPref == 1)
		{
			// matrix text - draw native (because user chose = 1 = translation, the matrix is =0=native)
			( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setText( wordArray.getWordNativeAtIndex( usrSudokuArr.Puzzle[i][j] - 1 ) );
		}
		else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]==0 && usrLangPref == 0)
		{
			// user text - draw native (because user chose = 0 = native, draw user square native)
			( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setText( wordArray.getWordNativeAtIndex( usrSudokuArr.Puzzle[i][j] - 1 ) );
		}
		else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]==0 && usrLangPref == 1)
		{
			// user text - draw translation (because user chose = 1 = translation, draw user square translation)
			( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setText( wordArray.getWordTranslationAtIndex( usrSudokuArr.Puzzle[i][j] - 1 ) );
		}
		else
		{
			//set empty text
			//note: not adding "empty string" causes error
			( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setText( " " );
		}
	}


	public void newTextView( int sqrL, int sqrT, int sqrSizeWidth, int sqrSizeHeight, int i, int j, WordArray wordArray,
							SudokuGenerator usrSudokuArr, int usrLangPref )
	{
		/*
		 * This method sets multiple text view and creates a marquee, which wraps the text
		 * inside squares if the text is too long. Each cell has its individual marquee
		 * which resets depending on text length.
		 * Note that synchronizing all marques to slide at once is not the best because a single
		 * long word will take too long to loop, causing shorter words to not loop frequently, so
		 * user will have to wait a long time to see short words. Hence having each marquee loop
		 * depending on individual word length is better
		 */

		RelativeLayout.LayoutParams parameter = new RelativeLayout.LayoutParams(sqrSizeWidth, sqrSizeHeight);//height and width are inpixel

		textViewArr[i][j].getRelativeLayout().getChildAt(0).setSelected( true ); //set focus

		textViewArr[i][j].getRelativeLayout().setX( (float) sqrL ); //set x,y coordinate
		textViewArr[i][j].getRelativeLayout().setY( (float) sqrT ); //temporary 10px offset

		textViewArr[i][j].getRelativeLayout().setLayoutParams( parameter );

		textViewArr[i][j].getRelativeLayout().getChildAt(0).setLayoutParams( new RelativeLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT) );
		((TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setGravity( Gravity.CENTER ); //center text vertically
		( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setEllipsize( TextUtils.TruncateAt.MARQUEE ); //set marquee animation
		( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setSingleLine( true ); //limit to single line
		( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setMarqueeRepeatLimit( -1 ); //makes marquee loop forever
		textViewArr[i][j].getRelativeLayout().setPadding( 10, 10, 10,10 ); //add padding so text is not shown right at the edge
		( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setTextSize( TXT_SIZE_NORMAL ); //set text size

		//add the original sqrT/L coordinates
		textViewArr[i][j].setCoordinates( sqrT, sqrL );

		// set the text according to user language preference
		chooseLangAndDraw( i, j, wordArray, usrSudokuArr, usrLangPref );
	}


	public RelativeLayout getRelativeTextView( int i, int j )
	{
		return textViewArr[i][j].getRelativeLayout( );
	}

	public RelativeAndPos[][] getRelativeAndPos( )
	{
		return textViewArr;
	}

	public void resetAllText( SudokuGenerator usrSudokuArr )
	{
		/*
		 * Reset all cells the user has modified to no text
		 */

		for( int i=0; i<puzzleTypeSize; i++ )
		{
			for( int j=0; j<puzzleTypeSize; j++ )
			{
				if( usrSudokuArr.PuzzleOriginal[i][j] == 0 ) //if a cell that the user can modify
				{
					( (TextView) (textViewArr[i][j].getRelativeLayout().getChildAt(0)) ).setText( "" );
				}
			}
		}
	}
}
