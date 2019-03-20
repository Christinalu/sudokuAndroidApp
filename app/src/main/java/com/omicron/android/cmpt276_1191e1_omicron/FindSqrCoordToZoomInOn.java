package com.omicron.android.cmpt276_1191e1_omicron;

import android.util.Log;
import android.widget.TextView;

public class FindSqrCoordToZoomInOn
{
	/*
	 * This objects adjusts the coordinates of where to zoom in
	 * Say a user is in "zoom out" mode, then clicks on a square,
	 * then when user switched to "zoom in" mode, this will calculate
	 * coordinates to zoom in onto that selected square
	 *
	 * If no square selected, it will zoom in to center by default
	 */

	private int[] touchXZ;
	private int[] touchYZ;
	private Pair currentRectColoured;
	private int bitmapSizeWidth;
	private int bitmapSizeHeight;
	private int sqrSizeWidth;
	private int sqrSizeHeight;
	private TextMatrix textMatrix;
	private float ZOOM_SCALE;

	public FindSqrCoordToZoomInOn(int[] touchXZ2, int[] touchYZ2, Pair currentRectColoured2, int bitmapSizeWidth2,
								  int bitmapSizeHeight2, int sqrSizeWidth2, int sqrSizeHeight2,
								  TextMatrix textMatrix2, float ZOOM_SCALE2 )
	{
		touchXZ = touchXZ2;
		touchYZ = touchYZ2;
		currentRectColoured = currentRectColoured2;
		bitmapSizeWidth = bitmapSizeWidth2;
		bitmapSizeHeight = bitmapSizeHeight2;
		sqrSizeWidth = sqrSizeWidth2;
		sqrSizeHeight = sqrSizeHeight2;
		textMatrix = textMatrix2;
		ZOOM_SCALE = ZOOM_SCALE2;
	}

	public void findSqrCoordToZoomInOn( )
	{
		int topX; //x-coordinate of top left corner of where the view relative to zoomed canvas
		int topY;

		// TRANSLATE COORDINATE ONLY IF A SQUARE IS SELECTED
		if( currentRectColoured.getRow() != -1 )
		{
			int i = currentRectColoured.getRow( ); //row coordinate of selected square
			int j = currentRectColoured.getColumn( );

			//get coordinates of top left corder of square
			//get position of selected square (then scale)
			topX = (int) (( (textMatrix.getRelativeAndPos())[i][j] ).getSqrL( )*ZOOM_SCALE);
			topY = (int) (( (textMatrix.getRelativeAndPos())[i][j] ).getSqrT( )*ZOOM_SCALE);

			//translate from corner of square to corner of zoom view area
			//note: "zoom view area" is the square field of view, ie the only rectangular area that is visible in zoom mode out of whole puzzle
			topX = topX - (int)( (bitmapSizeWidth-sqrSizeWidth*ZOOM_SCALE) / 2 );
			topY = topY - (int)( (bitmapSizeHeight-sqrSizeHeight*ZOOM_SCALE) / 2 );
			
			// FIX OUT-OF-BOUNDS
			//ie: if calculated coordinates of selected square is too close to top left corner, set view at (0,0)
			if( topX < 0 ) //if too close left
			{
				topX = 0;
			}
			else if( topX > bitmapSizeWidth*ZOOM_SCALE - bitmapSizeWidth ) //if too close to right
			{
				topX = (int)(bitmapSizeWidth*ZOOM_SCALE - bitmapSizeWidth);
			}

			if( topY < 0 ) //if too close to top
			{
				topY = 0;
			}
			else if( topY > bitmapSizeHeight*ZOOM_SCALE - bitmapSizeHeight ) //if too close to bottom
			{
				topY = (int)(bitmapSizeHeight*ZOOM_SCALE - bitmapSizeHeight);
			}
		}
		else
		{
			// default: translate to center of matrix
			topX = (int)(bitmapSizeWidth*ZOOM_SCALE/2 - bitmapSizeWidth/2);
			topY = (int)(bitmapSizeHeight*ZOOM_SCALE/2 - bitmapSizeHeight/2);
		}

		touchXZ[0] = topX;
		touchYZ[0] = topY;
	}
}
