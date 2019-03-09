package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FindSqrCoordToZoomInOnTest {
	
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void useAppContext() {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();
		assertEquals("com.omicron.android.cmpt276_1191e1_omicron", appContext.getPackageName());
	}
	
	@Test
	public void findSqrCoordToZoomInOn()
	{
			/** SET UP **/
			
		int[] touchXZ = { -1 };
		int[] touchYZ = { -1 } ;
		Pair currentRectColoured = new Pair( 0, 0 );
		int bitmapSize = 1080;
		int BOUNDARY_OFFSET = 40;
		int SQR_INNER_DIVIDER = 5;
		int SQR_OUTER_DIVIDER = 15;
		//note: with these settings, sqrSize = 95;
		int sqrSize = ( bitmapSize - 2*BOUNDARY_OFFSET - 6*SQR_INNER_DIVIDER - 2*SQR_OUTER_DIVIDER ) / 9;
		Context context = InstrumentationRegistry.getTargetContext();
		float ZOOM_SCALE = 1.5f;
		TextMatrix textMatrix = new TextMatrix( context, sqrSize, ZOOM_SCALE );
		int screenW = bitmapSize;
		int PUZZLE_FULL_SIZE = 9*sqrSize + 6*SQR_INNER_DIVIDER + 2*SQR_OUTER_DIVIDER;
		int sqrLO = BOUNDARY_OFFSET + (screenW - 2 * BOUNDARY_OFFSET) / 2 - PUZZLE_FULL_SIZE / 2; // if math correct, sqrLO should == BOUNDARY_OFFSET
		int sqrTO = BOUNDARY_OFFSET; // no boundary offset - note: boundary offset is already included in bitmapSize because width == height, so sqrSize calculated for sqrLO width assures the offset is included in height sqrTO
		
		Word[] wordArray =new Word[]
				{
						new Word( "Un", "Un", 1, 1 ),
						new Word( "Two", "Deux", 2, 1 ),
						new Word( "Three", "Trois", 3, 1 ),
						new Word( "Four", "Quatre", 4, 1 ),
						new Word( "Five", "Cinq", 5, 1 ),
						new Word( "Six", "Six", 6, 1 ),
						new Word( "Seven", "Sept", 7, 1 ),
						new Word( "Eight", "Huit", 8, 1 ),
						new Word( "Nine", "Neuf", 9, 1 ),
						new Word( "en-US", "fr-FR", -1, -1 ), //lang
						new Word( "pkg_n.csv", "", -1, -1 ) //pkg name
				};
		
		SudokuGenerator usrSudokuArr = new SudokuGenerator( 0 );
		int usrLangPref = 0;
		
		// SETUP WORD OVERLAY VIEW //
		for( int i=0; i<9; i++ ) //row
		{
			for( int j=0; j<9; j++ ) //column
			{
				//increase square dimensions
				int sqrL = sqrLO + j*(sqrSize + SQR_INNER_DIVIDER);
				int sqrT = sqrTO + i*(sqrSize + SQR_INNER_DIVIDER);
				int sqrR = sqrL + sqrSize;
				int sqrB = sqrT + sqrSize;
				
				
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
				
				//rectArr[i][j] = new Block( sqrL, sqrT, sqrR, sqrB ); // create the new square
				//note: this loop has to be here and cannot be replaced by drw class
				
				// add text view to relative layout matrix
				textMatrix.newTextView( sqrL, sqrT, sqrSize, i, j, wordArray, usrSudokuArr, usrLangPref );
				//rectTextLayout.addView( textMatrix.getRelativeTextView( i, j ) );
			}
		}
		
		FindSqrCoordToZoomInOn findSqrCoodToZoom = new FindSqrCoordToZoomInOn( touchXZ, touchYZ, currentRectColoured, bitmapSize,
																			   sqrSize, textMatrix, ZOOM_SCALE );
		
		textMatrix.scaleTextZoomIn( );
		//textMatrix.reDrawTextZoom( touchXZ, touchYZ, dX, dY ); // call this because .scaleTextZoom() only scales Layouts, so call this to place them in correct (drag) position
		
		
		
		/** TEST CASES **/
		
			/* TEST TOP LEFT SQUARE, SHOULD ZOOM PUZZLE-VIEW TO TOP LEFT */
		
		Log.d( "testunit", "testing findSqrCoodToZoom.findSqrCoordToZoomInOn( );" );
		findSqrCoodToZoom.findSqrCoordToZoomInOn( );
		
		assertEquals( touchXZ[0], 0 );
		assertEquals( touchYZ[0], 0 );
		
		
			/* TEST BOTTOM RIGHT SQUARE, SHOULD ZOOM PUZZLE-VIEW TO BOTTOM RIGHT */
		
		currentRectColoured = new Pair( 8, 8 );
		findSqrCoodToZoom = new FindSqrCoordToZoomInOn( touchXZ, touchYZ, currentRectColoured, bitmapSize,
														sqrSize, textMatrix, ZOOM_SCALE );
		
		//calculate where zoomXY should be according to screen size equation
		//int coordX = (int) (( (textMatrix.getRelativeAndPos())[currentRectColoured.getRow()][currentRectColoured.getColumn()] ).getSqrL( )*ZOOM_SCALE);
		//int coordY = (int) (( (textMatrix.getRelativeAndPos())[currentRectColoured.getRow()][currentRectColoured.getColumn()] ).getSqrT( )*ZOOM_SCALE);
		
		//coordinates for right most side
		int coordX =  (int)(bitmapSize * ZOOM_SCALE - bitmapSize);
		int coordY =  (int)(bitmapSize * ZOOM_SCALE - bitmapSize);
		Log.d( "testunit", "testing findSqrCoodToZoom.findSqrCoordToZoomInOn-2( );" );
		Log.d( "testunit", "coordX: " + coordX + " coordY: " + coordY );
		
		findSqrCoodToZoom.findSqrCoordToZoomInOn( );
		
		
		Log.d( "testunit", "touchXZ[0] = " + touchXZ[0] );
		
		assertEquals( touchXZ[0], coordX );
		assertEquals( touchYZ[0], coordY );
		
		
			/* TEST DEFAULT, SHOULD ZOOM PUZZLE-VIEW TO CENTER OF PUZZLE */
		
		currentRectColoured = new Pair( -1, -1 );
		findSqrCoodToZoom = new FindSqrCoordToZoomInOn( touchXZ, touchYZ, currentRectColoured, bitmapSize,
				sqrSize, textMatrix, ZOOM_SCALE );
		
		coordX = 270;
		coordY = 270;
		
		Log.d( "testunit", "testing findSqrCoodToZoom.findSqrCoordToZoomInOn-3( );" );
		Log.d( "testunit", "coordX: " + coordX + " coordY: " + coordY );
		findSqrCoodToZoom.findSqrCoordToZoomInOn( );
		
		assertEquals( touchXZ[0], coordX );
		assertEquals( touchYZ[0], coordY );
	}
}