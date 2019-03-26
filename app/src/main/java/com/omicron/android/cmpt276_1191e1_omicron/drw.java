package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class drw
{
	private Paint paint;
	private boolean newSqrTouched;
	private Block[][] rectArr;
	private Canvas canvas;
	private RelativeLayout rectLayout;
	private RelativeLayout rectTextLayout;
	private TextMatrix textMatrix;
	private SudokuGenerator usrSudokuArr;

	private int px;
	private int py;
	private int[] zoomOn;
	private int[] drag;
	private int[] dX;
	private int[] dY;
	private int[] touchXZ;
	private int[] touchYZ;
	private int[] zoomButtonSafe;
	private int[] zoomClickSafe;
	private int[] zoomButtonDisableUpdate;
	private int[] btnClicked;
	private int bitmapSizeWidth;
	private int bitmapSizeHeight;
	private int barH;
	private ImageView imgView;
	private Bitmap bitMap;
	//private Canvas canvas;
	private WordArray wordArray;
	private float[] ZOOM_SCALE;
	private int WORD_COUNT; //stores the number of words in wordArray
	private int COL_PER_BLOCK; //stores how many columns will be inside a block; in 9x9 this would be 3
	private int ROW_PER_BLOCK;
	private int VERTICAL_BLOCK; //stores how many (vertical) blocks are in a puzzle; in 9x9 this would be 3 blocks
	private int HORIZONTAL_BLOCK;
	private Context context; //context of GameActivity
	private String lastColour;
	
	
	public drw( Context context2, RelativeLayout rectLayout2 )
	{
		//only declare at this point
		//no initialization because not enough parameters were calculated
		//but this class was needed to store View objects Bitmap, ImageView and Canvas
		context = context2;
		rectLayout = rectLayout2;
	}
	
	
	public void drwInitialize( Block[][] rectArr2, Paint paint2, //Canvas canvas2,
				RelativeLayout rectTextLayout2, TextMatrix textMatrix2, SudokuGenerator usrSudokuArr2,
				int[] zoomOn2, int[] drag2, int[] dX2, int[] dY2,
				int[] touchXZ2, int[] touchYZ2, int[] zoomButtonSafe2, int[] zoomClickSafe2,
				int[] zoomButtonDisableUpdate2, int bitmapSizeWidth2, int bitmapSizeHeight2, WordArray wordArray2, int[] btnClicked2,
				float[] ZOOM_SCALE2, int COL_PER_BLOCK2, int ROW_PER_BLOCK2, int VERTICAL_BLOCK2, int HORIZONTAL_BLOCK2,
				int WORD_COUNT2, int barH2 )
	{
		paint = paint2;
		rectArr = rectArr2;
		//canvas = canvas2;
		
		rectTextLayout = rectTextLayout2;
		textMatrix = textMatrix2;
		usrSudokuArr = usrSudokuArr2;
		zoomOn = zoomOn2;
		drag = drag2;
		dX = dX2;
		dY = dY2;
		touchXZ = touchXZ2;
		touchYZ = touchYZ2;
		zoomButtonSafe = zoomButtonSafe2;
		zoomClickSafe = zoomClickSafe2;
		zoomButtonDisableUpdate = zoomButtonDisableUpdate2;
		bitmapSizeWidth = bitmapSizeWidth2;
		bitmapSizeHeight = bitmapSizeHeight2;
		wordArray = wordArray2;
		btnClicked = btnClicked2;
		ZOOM_SCALE = ZOOM_SCALE2;
		COL_PER_BLOCK = COL_PER_BLOCK2;
		ROW_PER_BLOCK = ROW_PER_BLOCK2;
		VERTICAL_BLOCK = VERTICAL_BLOCK2;
		HORIZONTAL_BLOCK = HORIZONTAL_BLOCK2;
		WORD_COUNT = WORD_COUNT2;
		barH = barH2;
		
	}

	
	public void createBitmap( int bitmapSizeWidth, int bitmapSizeHeight, int barH )
	{
			// INITIALIZE VIEW
		bitMap = Bitmap.createBitmap( bitmapSizeWidth, bitmapSizeHeight-barH, Bitmap.Config.ARGB_8888 );
		
		imgView = new ImageView(context);
		canvas = new Canvas( bitMap );
		imgView.setImageBitmap( bitMap );
		rectLayout.addView( imgView );
		
	}
	
	
	public void setDrawParameters( int[] touchX, int[] touchY, Pair lastRectColoured,
						Pair currentRectColoured )
	{
		if( zoomOn[0] == 0 )
		{

				/** DRAW ZOOM OUT MODE **/

			newSqrTouched = false; //reset
			//canvas.drawColor( Color.parseColor( "#5cddb1" ) );


			// loop to find selected rect
			// note: this loop must be separate; cannot be combined with colour loop because running
			// 		 these simultaneously wont work in case when user has selected a later square
			//		 because the deselected square wont get updated in time to be decoloured

			if( zoomButtonSafe[0] == 0 && zoomButtonDisableUpdate[0] == 0 ) // do not update on button click (zoomSafe == 0 means only test after going out of "zoom" mode, that is, do not check the 'click coordinate' if it is in a square because those coordinates do not count as a click)
			{																// when zoomButtonDisableUpdate[0] == 0, do not update currectRectColoured (when switching "zoom" modes)
				// TEST IF USER-TOUCH INSIDE A SQUARE
				for( int i=0; i<WORD_COUNT; i++ )
				{
					for( int j=0; j<WORD_COUNT; j++ )
					{
						if( rectArr[i][j].getRect( ).contains( touchX[0], touchY[0] )  ) // find if sqr was clicked
						{
							selectDeselectRectArrAtIndex( lastRectColoured, currentRectColoured, i, j );
							
							newSqrTouched = true;
							Log.d("TAG", "--touched-1: [" + i + "] [" + j + "]");
						}
					}
				}

				if( newSqrTouched == false && zoomClickSafe[0] == 0 ) // if user touched but did not touch a square -deselect
				{
					if( currentRectColoured.getRow() != -1 ) {
						rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()].deselect();
					}
					Log.d( "TAG-2", " --outside click" );
					currentRectColoured.update(-1, -1);
				}
			}

			
			

			//rectLayout.invalidate( ); //required to update to print to screen
		}
		else if( zoomOn[0] == 1 )
		{

				/** DRAW ZOOM IN MODE **/

			newSqrTouched = false; //reset

			// save and reset canvas
			//canvas.save();
			//canvas.drawColor( Color.parseColor( "#5cddb1" ) );

			if( drag[0] == 0 )
			{
				//scale to find square where user clicked
				px = (int)( ( touchXZ[0] + touchX[0] ) / ZOOM_SCALE[0] ); //divide to scale
				py = (int)( ( touchYZ[0] + touchY[0] ) / ZOOM_SCALE[0] );
			}

			Log.d( "TAG-2", " touchXZ: " + touchXZ[0] + ", touchX: " + touchX[0] );

			// if drag enabled, then translate matrix
//			if (drag[0] == 1) {
//				canvas.translate( -touchXZ[0] + dX[0], -touchYZ[0] + dY[0] );
//			} else { //else user only clicked
//				canvas.translate(-touchXZ[0], -touchYZ[0]);
//			}
//
//			canvas.scale( ZOOM_SCALE, ZOOM_SCALE );


				/* LOOP TO FIND IF TOUCH IS INSIDE SQUARE */
			// note: this loop must be separate; cannot be combined with colour loop (see above for reason)

			if( drag[0] == 0 && zoomButtonDisableUpdate[0] == 0 ) // do not update on button click (zoomSafe == 0 means only test after going in "zoom" mode, that is, do not check the 'click coordinate' until user touches screen again because those coordinates do not count as a click)
			{
				for( int i=0; i<WORD_COUNT; i++ )
				{
					for( int j=0; j<WORD_COUNT; j++ )
					{
						if (rectArr[i][j].getRect().contains(px, py) && zoomClickSafe[0] == 0) // find if sqr was clicked
						{
							// if in "zoom", check to see if clicking outside the zoomed map
							// before when clicked in empty space on BOTTOM side, it highlighted square because click was valid within rectLayout
							if( touchX[0] > bitmapSizeWidth || touchY[0] > bitmapSizeHeight )
							{
								// NOTE: this works only for hardcoded bitmap size : should change this code when adapting bitmap to screen size
								break; // do not count click (outside bound)
							}
							
							selectDeselectRectArrAtIndex( lastRectColoured, currentRectColoured, i, j );

							newSqrTouched = true;
							Log.d("TAG", "--touched-2: [" + i + "] [" + j + "]");
						}
					}
				}

				if( newSqrTouched == false && zoomClickSafe[0] == 0 ) // if user touched but did not touch a square : deselect
				{													  // zoomClickSafe is supposed to block deselection after user switched mode AND clicked button right away
					if( currentRectColoured.getRow() != -1 ) {
						rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()].deselect( );
					}
					currentRectColoured.update(-1, -1);
				}
			}
			
			//canvas.restore( );

			//rectLayout.invalidate( );
		}
	}
	
	
	private void selectDeselectRectArrAtIndex( Pair lastRectColoured, Pair currentRectColoured, int i, int j )
	{
		lastRectColoured.update(currentRectColoured.getRow(), currentRectColoured.getColumn());
		if (lastRectColoured.getRow() != -1) //avoid indexing out of array since -1 means nothing was previously selected
		{
			// this "if statement" must occur after setting lastRect.update() to currentRect
			rectArr[lastRectColoured.getRow()][lastRectColoured.getColumn()].deselect(); //deselect prev selected
		}
		
		currentRectColoured.update(i, j); // update this (must occur before rectArr.select()
		if (currentRectColoured.getRow() != -1) //bound check
		{
			rectArr[i][j].select(); //update array as well
		}
	}
	
	
	public void reDraw( Pair currentRectColoured, int usrLangPref, int currentSelectedIsCorrect )
	{
		/*
		 * After all coordinates and parameters were set, draw the rectangles
		 */
		
		if( zoomOn[0] == 1 ){
			canvas.save();
			
			//translate the canvas when zoomed
			if (drag[0] == 1) { //translate as the user drags the canvas
				canvas.translate( -touchXZ[0] + dX[0], -touchYZ[0] + dY[0] );
			} else { //else user only clicked
				canvas.translate(-touchXZ[0], -touchYZ[0]);
			}
			
			canvas.scale( ZOOM_SCALE[0], ZOOM_SCALE[0] );
		}
		
		canvas.drawColor( Color.parseColor( "#5cddb1" ) ); //reset all canvas to colour
		
		boolean highlight = true;
		String selectPredefionedWord = "#727272";
		String selectEmptySquare = "#ffd700";
		String selectCorrect = "#228b22";
		String selectIncorrect = "#ff7f50";
		String fixedSquareDark = "#a2a2a2";
		String fixedSquareLight = "#c2c2c2";
		
			/**REDRAW ALL SQUARES IN ZOOM WITH CORRESPONDING COLOUR **/
			
		for( int i=0; i<WORD_COUNT; i++ )
		{
			for( int j=0; j<WORD_COUNT; j++ )
			{
				//set colours; must occur after figuring out is rect contained
				if (rectArr[i][j].isSelected()) //if a selected rect
				{
					Log.d( "highlight", "selected" );
					if( usrSudokuArr.PuzzleOriginal[i][j] != 0 ) //if an element that cannot be modified
					{
						paint.setColor(Color.parseColor( selectPredefionedWord )); //colour if selected a predefined word
						canvas.drawRect(rectArr[i][j].getRect(), paint);
						highlight = false;
						continue;
					}
					else if( usrSudokuArr.Puzzle[i][j] == 0 ) //if square selected is empty (no input so far)
					{
						paint.setColor(Color.parseColor( selectEmptySquare )); //colour if selected a empty sqr
						canvas.drawRect(rectArr[i][j].getRect(), paint);
						highlight = false;
						continue;
					}
					
					if( currentSelectedIsCorrect == 1 ) //selected is correct
					{
						paint.setColor(Color.parseColor( selectCorrect ));
						rectArr[i][j].setLastColour( selectCorrect );
					}
					else if( currentSelectedIsCorrect == 2 ) //selected is incorrect
					{
						paint.setColor(Color.parseColor( selectIncorrect ));
						rectArr[i][j].setLastColour( selectIncorrect );
					}
					else
					{
						//paint.setColor(Color.parseColor("#000000"));
						paint.setColor( Color.parseColor( rectArr[i][j].getLastColour( ) ) ); //on drag, use previous colour of selected rect
					}

				} else if (usrSudokuArr.PuzzleOriginal[i][j] != 0) // if a element that cannot be modified
				{
					paint.setColor(Color.parseColor( fixedSquareDark )); // set darker colour for fixed numbers
				} else {
					paint.setColor(Color.parseColor( fixedSquareLight )); // set lighter colour for fixed numbers
				}
				
				canvas.drawRect(rectArr[i][j].getRect(), paint);
			}
		}
		
			/** HIGHLIGHT ROW AND COL **/
		/*
		//loop and highlight row and column if correct or wrong
		if( currentRectColoured.getRow() != -1 && highlight == true && usrSudokuArr.Puzzle[currentRectColoured.getRow()][currentRectColoured.getColumn()] != 0 && zoomClickSafe[0] == 0 && drag[0] == 0 )
		{
			int i = currentRectColoured.getRow();
			//loop column in constant row
			for (int j = 0; j < WORD_COUNT; j++)
			{
				
				if( currentRectColoured.getColumn() == j ) { // if colouring the selected square
					//void - keep same colour so far
					continue;
				}
				
				if (usrSudokuArr.PuzzleOriginal[i][j] != 0) //if a predefined word
				{
					if( currentSelectedIsCorrect == 1 ) //selected is correct
					{
						paint.setColor(Color.parseColor("#00cc00")); //colour darker
					}
					else if( currentSelectedIsCorrect == 2 ) //selected is incorrect
					{
						paint.setColor(Color.parseColor("#cc0000")); //colour darker
					}
					
				}
				else
				{
					if( currentSelectedIsCorrect == 1 ) //selected is correct
					{
						paint.setColor(Color.parseColor("#22ff22")); //colour lighter
					}
					else if( currentSelectedIsCorrect == 2 ) //selected is incorrect
					{
						paint.setColor(Color.parseColor("#ff0000")); //colour darker
					}
				}
				canvas.drawRect(rectArr[i][j].getRect(), paint);
			}
			
			int j = currentRectColoured.getColumn();
			//loop column in constant row
			for (int k = 0; k < WORD_COUNT; k++)
			{
				
				if( currentRectColoured.getRow() == k ) { // if colouring the selected square
					//void - keep same colour so far
					continue;
				}
				
				if (usrSudokuArr.PuzzleOriginal[k][j] != 0) //if a predefined word
				{
					if( currentSelectedIsCorrect == 1 ) //selected is correct
					{
						paint.setColor(Color.parseColor("#00cc00")); //colour darker
					}
					else if( currentSelectedIsCorrect == 2 ) //selected is incorrect
					{
						paint.setColor(Color.parseColor("#cc0000")); //colour darker
					}
				}
				else
				{
					if( currentSelectedIsCorrect == 1 ) //selected is correct
					{
						paint.setColor(Color.parseColor("#22ff22")); //colour lighter
					}
					else if( currentSelectedIsCorrect == 2 ) //selected is incorrect
					{
						paint.setColor(Color.parseColor("#ff0000")); //colour darker
					}
				}
				canvas.drawRect(rectArr[k][j].getRect(), paint);
			}
		}
		*/
		
		
		// DRAW TEXT OVERLAY + RESTORE
		// update text of currently selected square on button click
		if( currentRectColoured.getRow() != -1 && btnClicked[0] == 1 )
		{
			//note that also having this execute only on "btnClicked[0] == 1" does not reset the sliding animation each time a square is highlighted
			//note: animation will reset each time a button is clicked because
			textMatrix.chooseLangAndDraw( currentRectColoured.getRow(), currentRectColoured.getColumn(),
					  					  wordArray, usrSudokuArr, usrLangPref );
		}
		
		if( zoomOn[0] == 1 )
		{
			textMatrix.reDrawTextZoom(touchXZ, touchYZ, dX, dY);
			canvas.restore( ); //return canvas back to original before zoom
		}
		
		rectLayout.invalidate( );
	}
}
