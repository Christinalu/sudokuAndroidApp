package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GameActivityControl
{
	/*
 	* This class is an extension of Game Activity but stores the Control part of MCV
 	*/
	
	private int screenH;
	private int screenW;
	private int bitmapSize; //lowest of screen dimensions - size of square bitmap
	private int sqrSize ; //size of single square in matrix
	
	private int sqrLO; // original left coordinate of where puzzle starts
	private int sqrTO; // original top coordinate of where puzzle starts
	private final static int BOUNDARY_OFFSET = 40; //puzzle will have some offset for aesthetic reasons
	private int PUZZLE_FULL_SIZE; // size of full puzzle from left most column pixel to right most column pixel
	private final static int SQR_INNER_DIVIDER = 5;
	private final static int SQR_OUTER_DIVIDER = 15;
	
	private Bitmap bgMap;
	private Canvas canvas;
	private Paint paint = new Paint( );
	private Block[][] rectArr = new Block[9][9]; // stores all squares in a 2D array
	
	private int sqrL; //duplicate square coordinates
	private int sqrT;
	private int sqrR;
	private int sqrB;
	
	
	
	public GameActivityControl( )
	{
		//void
	}
	
	
	public void initializeView(Display screen, int barH, RelativeLayout rectTextLayout, RelativeLayout rectLayout,
							   ImageView imgView, TextMatrix textMatrix, Word[] wordArray, SudokuGenerator usrSudokuArr,
							   int usrLangPref )
	{
		/*
		 * This function initializes Relative View where the Puzzle will be
		 */
		
		// get display metrics
		int orientation = Configuration.ORIENTATION_UNDEFINED;
		DisplayMetrics displayMetrics = new DisplayMetrics( );
		screen.getMetrics( displayMetrics );
		
		screenH = displayMetrics.heightPixels;
		screenW = displayMetrics.widthPixels;
		
		//set orientation
		if( screenH > screenW )
		{
			orientation = Configuration.ORIENTATION_PORTRAIT;
		}else {
			orientation = Configuration.ORIENTATION_LANDSCAPE;
		}
		
		//find minimum to create square bitmap
		//note: in Canvas, a bitmap starts from coordinate (0,0), so a bitmap must cover the entire screen size
		if( screenH < screenW )
		{ bitmapSize = screenH; }
		else{ bitmapSize = screenW; }
		
		// center bitmap based on orientation
		// original coordinates of where to start to draw square (LO == Left Original)
		// key: sqrLO/TO determines where the drawR class will start drawing from
		if( orientation == Configuration.ORIENTATION_LANDSCAPE ) //if in landscape mode, center bitmap on left side
		{
			sqrLO = BOUNDARY_OFFSET;
			sqrTO = BOUNDARY_OFFSET;
			
			// find matrix single square size based on screen
			// barH needed in landscape mode
			sqrSize = ( bitmapSize - barH - 2*BOUNDARY_OFFSET - 6*SQR_INNER_DIVIDER - 2*SQR_OUTER_DIVIDER ) / 9;
			
			// find entire matrix puzzle size
			PUZZLE_FULL_SIZE = 9*sqrSize + 6*SQR_INNER_DIVIDER + 2*SQR_OUTER_DIVIDER;
			
			bgMap = Bitmap.createBitmap( bitmapSize-barH, bitmapSize-barH, Bitmap.Config.ARGB_8888 );
			canvas = new Canvas( bgMap );
			
			//set rect_txt_layout the same size as the bitmap
			rectTextLayout.getLayoutParams().height = bitmapSize-barH;
			rectTextLayout.getLayoutParams().width = bitmapSize-barH;
			
			rectLayout.getLayoutParams().height = bitmapSize-barH;
			rectLayout.getLayoutParams().width = bitmapSize-barH;
		}
		else //in portrait mode
		{
			// find matrix single square size based on screen
			sqrSize = ( bitmapSize - 2*BOUNDARY_OFFSET - 6*SQR_INNER_DIVIDER - 2*SQR_OUTER_DIVIDER ) / 9;
			
			// find entire matrix puzzle size
			PUZZLE_FULL_SIZE = 9*sqrSize + 6*SQR_INNER_DIVIDER + 2*SQR_OUTER_DIVIDER;
			
			//if in portrait mode, center puzzle top of screen
			sqrLO = BOUNDARY_OFFSET + (screenW - 2 * BOUNDARY_OFFSET) / 2 - PUZZLE_FULL_SIZE / 2; // if math correct, sqrLO should == BOUNDARY_OFFSET
			sqrTO = BOUNDARY_OFFSET; // no boundary offset - note: boundary offset is already included in bitmapSize because width == height, so sqrSize calculated for sqrLO width assures the offset is included in height sqrTO
			
			bgMap = Bitmap.createBitmap( bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888 );
			canvas = new Canvas( bgMap );
			
			//set rect_txt_layout the same size as the bitmap
			rectTextLayout.getLayoutParams().height = bitmapSize;
			rectTextLayout.getLayoutParams().width = bitmapSize;
			
			//note: limiting rectLayout to bitmap size will force a selected square to be deselected only when clicking inside the bitmap
			//		it wont deselect when clicking outside, say near buttons
			rectLayout.getLayoutParams().height = bitmapSize;
			rectLayout.getLayoutParams().width = bitmapSize;
		}
		
		
		
		imgView.setImageBitmap( bgMap );
		paint.setColor(Color.parseColor("#c2c2c2"));
		
		rectLayout.addView( imgView );
		
		
			/** CREATE RECT MATRIX **/
		
		for( int i=0; i<9; i++ ) //row
		{
			for( int j=0; j<9; j++ ) //column
			{
				//increase square dimensions
				sqrL = sqrLO + j*(sqrSize + SQR_INNER_DIVIDER);
				sqrT = sqrTO + i*(sqrSize + SQR_INNER_DIVIDER);
				sqrR = sqrL + sqrSize;
				sqrB = sqrT + sqrSize;
				
				
				//add padding
				if( i>=3 ) //add extra space between rows
				{
					sqrT = sqrT + SQR_OUTER_DIVIDER; //square padding
					sqrB = sqrB + SQR_OUTER_DIVIDER;
				}
				if( i>=6 )
				{
					sqrT = sqrT + SQR_OUTER_DIVIDER; //square padding
					sqrB = sqrB + SQR_OUTER_DIVIDER;
				}
				
				if( j>=3 ) //add extra space between columns
				{
					sqrL = sqrL + SQR_OUTER_DIVIDER; //square padding
					sqrR = sqrR + SQR_OUTER_DIVIDER;
				}
				if( j>=6 )
				{
					sqrL = sqrL + SQR_OUTER_DIVIDER; //square padding
					sqrR = sqrR + SQR_OUTER_DIVIDER;
				}
				
				rectArr[i][j] = new Block( sqrL, sqrT, sqrR, sqrB ); // create the new square
				//note: this loop has to be here and cannot be replaced by drw class
				
				// add text view to relative layout matrix
				textMatrix.newTextView( sqrL, sqrT, sqrSize, i, j, wordArray,
						usrSudokuArr, usrLangPref );
				rectTextLayout.addView( textMatrix.getRelativeTextView( i, j ) );
				
				rectTextLayout.invalidate();
			}
		}
		
		
	}
	
	public Bitmap getBgMap( ){
		return bgMap;
	}
	
	public int getSqrSize( )
	{
		return sqrSize;
	}
	
	public int getBitmapSize() {
		return bitmapSize;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public Block[][] getRectArr() {
		return rectArr;
	}
}
