package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
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
	private int bitmapSize;
	private Word[] wordArray;
	private float ZOOM_SCALE;


	public drw( Block[][] rectArr2, Paint paint2, Canvas canvas2,
				RelativeLayout rectLayout2, RelativeLayout rectTextLayout2, TextMatrix textMatrix2, SudokuGenerator usrSudokuArr2,
				int[] zoomOn2, int[] drag2, int[] dX2, int[] dY2,
				int[] touchXZ2, int[] touchYZ2, int[] zoomButtonSafe2, int[] zoomClickSafe2,
				int[] zoomButtonDisableUpdate2, int bitmapSize2, Word[] wordArray2, int[] btnClicked2,
				float ZOOM_SCALE2 )
	{
		paint = paint2;
		rectArr = rectArr2;
		canvas = canvas2;
		rectLayout = rectLayout2;
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
		bitmapSize = bitmapSize2;
		wordArray = wordArray2;
		btnClicked = btnClicked2;
		ZOOM_SCALE = ZOOM_SCALE2;
	}

	public void reDraw( int[] touchX, int[] touchY, Pair lastRectColoured,
						Pair currentRectColoured, int usrLangPref )
	{
		if (zoomOn[0] == 0)
		{

				/** DRAW ZOOM OUT MODE **/

			newSqrTouched = false; //reset
			canvas.drawColor( Color.parseColor( "#5cddb1" ) );


			// loop to find selected rect
			// note: this loop must be separate; cannot be combined with colour loop because running
			// 		 these simultaneously wont work in case when user has selected a later square
			//		 because the deselected square wont get updated in time to be decoloured

			if( zoomButtonSafe[0] == 0 && zoomButtonDisableUpdate[0] == 0 ) // do not update on button click (zoomSafe == 0 means only test after going out of "zoom" mode, that is, do not check the 'click coordinate' if it is in a square because those coordinates do not count as a click)
			{																// when zoomButtonDisableUpdate[0] == 0, do not update currectRectColoured (when switching "zoom" modes)
				// TEST IF USER TOUCH INSIDE A SQUARE
				for (int i = 0; i < 9; i++)
				{
					for (int j = 0; j < 9; j++)
					{
						if( rectArr[i][j].getRect( ).contains( touchX[0], touchY[0] )  ) // find if sqr was clicked
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


			// REDRAW ALL SQUARES IN ZOOM MODE WITH CORRESPONDING SHADES
			for (int i = 0; i < 9; i++)
			{
				for (int j = 0; j < 9; j++)
				{
					//set colours; must occur after figuring out is rect contained
					if (rectArr[i][j].isSelected()) //if a selected rect
					{
						//note: if rect selected, choose red and skip rest of colours - so red must be first colour assigned
						paint.setColor(Color.parseColor("#ff0000"));
					} else if (usrSudokuArr.PuzzleOriginal[i][j] != 0) // if a element that cannot be modified
					{
						paint.setColor(Color.parseColor("#a2a2a2")); // set darker colour for fixed numbers
					} else {
						paint.setColor(Color.parseColor("#c2c2c2")); // set lighter colour for fixed numbers
					}

					canvas.drawRect(rectArr[i][j].getRect( ), paint);
				}
			}


			// DRAW TEXT OVERLAY
			if( currentRectColoured.getRow() != -1 && btnClicked[0] == 1 )
			{
				//redraw text of currently selected square
				//note that also having this execute only on "btnClicked[0] == 1" does not reset the sliding animation each time a square is highlighted
				textMatrix.chooseLangAndDraw( currentRectColoured.getRow(), currentRectColoured.getColumn(), wordArray, usrSudokuArr, usrLangPref );
			}

			rectLayout.invalidate( ); //required to update to print to screen
			rectTextLayout.invalidate( );
		}
		else if (zoomOn[0] == 1)
		{

				/** DRAW ZOOM IN MODE **/

			newSqrTouched = false; //reset

			// save and reset canvas
			canvas.save();
			canvas.drawColor( Color.parseColor( "#5cddb1" ) );

			if( drag[0] == 0 )
			{
				//scale to find square where user clicked
				px = (int)( ( touchXZ[0] + touchX[0] ) / ZOOM_SCALE ); //divide to scale
				py = (int)( ( touchYZ[0] + touchY[0] ) / ZOOM_SCALE );
			}

			Log.d( "TAG-2", " touchXZ: " + touchXZ[0] + ", touchX: " + touchX[0] );

			// if drag enabled, then translate matrix
			if (drag[0] == 1) {
				canvas.translate( -touchXZ[0] + dX[0], -touchYZ[0] + dY[0] );
			} else { //else user only clicked
				canvas.translate(-touchXZ[0], -touchYZ[0]);
			}

			canvas.scale(ZOOM_SCALE, ZOOM_SCALE);


			// LOOP TO FIND IF TOUCH IS INSIDE SQUARE
			// note: this loop must be separate; cannot be combined with colour loop (see above for reason)

			if( drag[0] == 0 && zoomButtonDisableUpdate[0] == 0 ) // do not update on button click (zoomSafe == 0 means only test after going in "zoom" mode, that is, do not check the 'click coordinate' until user touches screen again because those coordinates do not count as a click)
			{
				for (int i = 0; i < 9; i++)
				{
					for (int j = 0; j < 9; j++)
					{
						if (rectArr[i][j].getRect().contains(px, py) && zoomClickSafe[0] == 0) // find if sqr was clicked
						{
							// if in "zoom", check to see if clicking outside the zoomed map
							// before when clicked in empty space on BOTTOM side, it highlighted square because click was valid within rectLayout
							if( touchX[0] > bitmapSize || touchY[0] > bitmapSize )
							{
								// NOTE: this works only for hardcoded bitmap size : should change this code when adapting bitmap to screen size
								break; // do not count click (outside bound)
							}

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


			// redraw all squares in zoom/scale mode with corresponding shade
			for (int i = 0; i < 9; i++)
			{
				for (int j = 0; j < 9; j++)
				{
					//set colours; must occur after figuring out is rect contained
					if (rectArr[i][j].isSelected()) //if a selected rect
					{
						//note: if rect selected, choose red and skip rest of colours - so red must be first colour assigned
						paint.setColor(Color.parseColor("#ff0000"));
					} else if (usrSudokuArr.PuzzleOriginal[i][j] != 0) // if a element that cannot be modified
					{
						paint.setColor(Color.parseColor("#a2a2a2")); // set darker colour for fixed numbers
					} else {
						paint.setColor(Color.parseColor("#c2c2c2")); // set lighter colour for fixed numbers
					}

					canvas.drawRect(rectArr[i][j].getRect(), paint);
				}
			}


			// DRAW TEXT OVERLAY + RESTORE


			// update text of currently selected square on button click
			if( currentRectColoured.getRow() != -1 && btnClicked[0] == 1 )
			{
				//note that also having this execute only on "btnClicked[0] == 1" does not reset the sliding animation each time a square is highlighted
				//note: animation will reset each time a button is clicked because
				textMatrix.chooseLangAndDraw( currentRectColoured.getRow(), currentRectColoured.getColumn(), wordArray, usrSudokuArr, usrLangPref );
			}


			textMatrix.reDrawTextZoom(touchXZ, touchYZ, dX, dY);

			canvas.restore( );

			rectLayout.invalidate( );
		}
	}
}
