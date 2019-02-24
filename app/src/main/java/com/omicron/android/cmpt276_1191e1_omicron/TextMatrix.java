package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextMatrix
{
	private static final float TXT_SIZE_NORMAL = 15;
	private static final float TXT_SIZE_ZOOM = 21;
	private float ZOOM_SCALE;

	private RelativeAndPos[][] textViewArr; //holds all text views
	private Context gameActivity;
	private int sqrSize;

	public TextMatrix( Context context, int sqrSize2, float ZOOM_SCALE2 )
	{
		/*
		 * NOTE: individual LinearLayout are required for each TextView to fix issue where
		 *       changing single setText() resulted in resetting animation for all TextView
		 *       (because when calling setText() it re-wraps all texts in single Layout, so hat to create multiple Layouts)
		 */

		textViewArr = new RelativeAndPos[9][9];

		for( int i=0; i<9; i++ )
		{
			for( int j=0; j<9; j++ )
			{
				textViewArr[i][j] = new RelativeAndPos( context );
				textViewArr[i][j].getRelativeLayout().addView( new TextView( context ) );
			}
		}

		gameActivity = context;
		sqrSize = sqrSize2;
		ZOOM_SCALE = ZOOM_SCALE2;
	}


	public void scaleTextZoomIn(  )
	{
		RelativeLayout.LayoutParams parameter = new RelativeLayout.LayoutParams( (int)(sqrSize*ZOOM_SCALE), (int)(sqrSize*ZOOM_SCALE) );//height and width are inpixel

		//this method scales all of the text in zoom mode
		for( int i=0; i<9; i++ )
		{
			for( int j=0; j<9; j++ )
			{
				textViewArr[i][j].getRelativeLayout().setX( textViewArr[i][j].getRelativeLayout().getX( ) * ZOOM_SCALE ); //set x,y coordinate to multiple of 2
				textViewArr[i][j].getRelativeLayout().setY( textViewArr[i][j].getRelativeLayout().getY( ) * ZOOM_SCALE );

				textViewArr[i][j].getRelativeLayout().setLayoutParams( parameter );

				//upscale text size
				( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setTextSize( TXT_SIZE_ZOOM );
			}
		}
	}


	public void scaleTextZoomOut(  )
	{
		RelativeLayout.LayoutParams parameter = new RelativeLayout.LayoutParams( (int)(sqrSize), (int)(sqrSize) );//height and width are inpixel

		//this method scales all of the text in zoom mode
		for( int i=0; i<9; i++ )
		{
			for( int j=0; j<9; j++ )
			{
				textViewArr[i][j].getRelativeLayout().setX( textViewArr[i][j].getSqrL() ); //reset x,y coordinate to original
				textViewArr[i][j].getRelativeLayout().setY( textViewArr[i][j].getSqrT() );

				textViewArr[i][j].getRelativeLayout().setLayoutParams( parameter );

				//downscale text size
				( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setTextSize( TXT_SIZE_NORMAL );
			}
		}
	}


	public void reDrawTextZoom( int[] touchXZ, int[] touchYZ, int[] dX, int[] dY )
	{
		/*
		 * Draw the text according to 'drag' coordinates in zoom mode
		 */

		for( int i=0; i<9; i++ )
		{
			for( int j = 0; j < 9; j++ )
			{
				textViewArr[i][j].getRelativeLayout().setX( textViewArr[i][j].getSqrL() * ZOOM_SCALE + ( -touchXZ[0] + dX[0] ) ); //set x,y coordinate
				textViewArr[i][j].getRelativeLayout().setY( textViewArr[i][j].getSqrT() * ZOOM_SCALE + ( -touchYZ[0] + dY[0] ) );
			}
		}
	}


	public void chooseLangAndDraw( int i, int j, Word[] wordArray, SudokuGenerator usrSudokuArr,
								   int usrLangPref )
	{
		// CHOOSE WHAT LANGUAGE TO DRAW IN  TEXT-VIEW MARQUEE
		// note: usrLangPref refers to text that the user is typing in the puzzle (ie if usrLangPref = 0 = native, then the (unchangeable) text inside puzzle is translation)
		// matrix text: text inside matrix that cannot be modified
		// user text: the text that the user inputs

		if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]!=0 && usrLangPref == 0 ) // draw only if the puzzle contains a number; and draw the native translated word
		{
			// matrix text - draw translation (because user chose = 0 = native, the matrix is =1=translation)
			( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setText( wordArray[usrSudokuArr.Puzzle[i][j] - 1].getTranslation() );
		}
		else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]!=0 && usrLangPref == 1)
		{
			// matrix text - draw native (because user chose = 1 = translation, the matrix is =0=native)
			( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setText( wordArray[usrSudokuArr.Puzzle[i][j] - 1].getNative() );
		}
		else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]==0 && usrLangPref == 0)
		{
			// user text - draw native (because user chose = 0 = native, draw user square native)
			( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setText( wordArray[usrSudokuArr.Puzzle[i][j] - 1].getNative() );
		}
		else if( usrSudokuArr.Puzzle[i][j]!=0 && usrSudokuArr.PuzzleOriginal[i][j]==0 && usrLangPref == 1)
		{
			// user text - draw translation (because user chose = 1 = translation, draw user square translation)
			( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setText( wordArray[usrSudokuArr.Puzzle[i][j] - 1].getTranslation() );
		}
		else
		{
			//set empty text
			//note: not adding "empty string" causes error
			( (TextView) textViewArr[i][j].getRelativeLayout().getChildAt(0)).setText( " " );
		}
	}


	public void newTextView( int sqrL, int sqrT, int sqrSize, int i, int j, Word[] wordArray,
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

		RelativeLayout.LayoutParams parameter = new RelativeLayout.LayoutParams(sqrSize, sqrSize);//height and width are inpixel

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
}
