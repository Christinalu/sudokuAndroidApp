package com.omicron.android.cmpt276_1191e1_omicron;

import android.util.Log;
import android.widget.TextView;

public class FindSqrCoordToZoomInOn
{
	/*
	 * This objects adjusts the coodinates of where to zoom in
	 * Say a user is in "zoom out" mode, then clicks on a square,
	 * then when user switched to "zoom in" mode, this will calculate
	 * coordiantes to zoom in onto that selected square
	 *
	 * If no square selected, it will zoom in to center by default
	 */

	private int[] touchXZ;
	private int[] touchYZ;
	private Pair currentRectColoured;
	private int bitmapSize;
	private int sqrSize;
	private TextMatrix textMatrix;
	private float ZOOM_SCALE;

	public FindSqrCoordToZoomInOn(int[] touchXZ2, int[] touchYZ2, Pair currentRectColoured2, int bitmapSize2,
								  int sqrSize2, TextMatrix textMatrix2, float ZOOM_SCALE2 )
	{
		touchXZ = touchXZ2;
		touchYZ = touchYZ2;
		currentRectColoured = currentRectColoured2;
		bitmapSize = bitmapSize2;
		sqrSize = sqrSize2;
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
			topX = topX - (int)((bitmapSize-sqrSize*ZOOM_SCALE)/2);
			topY = topY - (int)((bitmapSize-sqrSize*ZOOM_SCALE)/2);


			// FIX OUT-OF-BOUNDS
			//ie: if calculated coordinates of selected square is too close to top left corner, set view at (0,0)
			if( topX < 0 ) //if too close to top
			{
				topX = 0;
			}
			else if( topX > bitmapSize*ZOOM_SCALE - bitmapSize ) //if too close to bottom
			{
				topX = (int)(bitmapSize*ZOOM_SCALE - bitmapSize);
			}

			if( topY < 0 ) //if too close to left
			{
				topY = 0;
			}
			else if( topY > bitmapSize*ZOOM_SCALE - bitmapSize ) //if too close to right
			{
				topY = (int)(bitmapSize*ZOOM_SCALE - bitmapSize);
			}
		}
		else
		{
			// default: translate to center of matrix
			topX = (int)(bitmapSize*ZOOM_SCALE/2 - bitmapSize/2);
			topY = (int)(bitmapSize*ZOOM_SCALE/2 - bitmapSize/2);
		}

		touchXZ[0] = topX;
		touchYZ[0] = topY;
	}
}
