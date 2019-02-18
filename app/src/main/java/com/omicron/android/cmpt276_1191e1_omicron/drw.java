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
	private RedrawText textOverlay;
	private boolean prevNewSqrTouched;
	private SudokuGenerator usrSudokuArr;
	private int[] zoomOn;
	private int px;
	private int py;
	private int[] drag;
	private int[] dX;
	private int[] dY;
	private int[] touchXZ;
	private int[] touchYZ;
	private int[] zoomButtonSafe;
	private int[] zoomClickSafe;
	private int[] zoomButtonDisableUpdate;

	private final static int BIT_MAP_W = 1052; //NOTE: !! this constant is also currently in .xml file - bitmap width (see later in code how to get this number)
	private final static int BIT_MAP_H = 1055; //bitmap height
	//private int[] zoomOn;


	public drw( Block[][] rectArr2, Paint paint2, Canvas canvas2,
			    RelativeLayout rectLayout2, RedrawText textOverlay2, SudokuGenerator usrSudokuArr2,
			    int[] zoomOn2, int[] zoomX2, int[] zoomY2, int[] drag2, int[] dX2, int[] dY2,
			    int[] touchXZ2, int[] touchYZ2, int[] zoomButtonSafe2, int[] zoomClickSafe2,
				int[] zoomButtonDisableUpdate2 )
	{
		paint = paint2;
		rectArr = rectArr2;
		canvas = canvas2;
		rectLayout = rectLayout2;
		textOverlay = textOverlay2;
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
	}

	public void reDraw(int[] touchX, int[] touchY, Pair lastRectColoured, Pair currentRectColoured,
					   boolean forcePaint, int usrLangPref)
	{
		// NOTE: reDraw only redraws squares that need to be changed, and leaves the rest alone (for efficiency), thats why this function is long
		//		 reDrawZoom cannot do this because of 'drag' so it has to redraw the entire puzzle


		/* DRAW IN NORMAL NON-ZOOM MODE */

		if (zoomOn[0] == 0)
		{
//			newSqrTouched = false; //reset
//			prevNewSqrTouched = newSqrTouched;
//
//			//loop and draw if valid rect clicked
//			for (int i = 0; i < 9; i++)
//			{
//				for (int j = 0; j < 9; j++)
//				{
//					// important: scale coordinates if called from button listener (in "zoom mode")
//					// needed because when called from listener, it is not called with zoom coordiantes
//					// because listeners set with 'final' regular variable, so have to transform to 'zoom'
//
//					px = touchX[0];
//					py = touchY[0];
//
//					if (rectArr[i][j].getRect().contains(px, py))
//					{
//						if (lastRectColoured.getRow() != -1) //avoid indexing out of array since -1 means nothing was previously selected
//						{
//							// this "if" statement must occur before setting lastRect.update() to currentRect
//							rectArr[lastRectColoured.getRow()][lastRectColoured.getColumn()].deselect(); //deselect prev selected
//						}
//						lastRectColoured.update(currentRectColoured.getRow(), currentRectColoured.getColumn());
//
//						paint.setColor(Color.parseColor("#ff0000"));
//
//						//Toast.makeText(GameActivity.this, "REKT CLICKED", Toast.LENGTH_SHORT).show();
//
//						//////////////
//						//
//						//	if using reDrawZoom for zoom, then remove all zoom stuff from here
//						//
//						////////////
//
//					/*if( zoomOn[0] == 1 )//colour on zoom mode
//					{
//						canvas.save( );
//						canvas.scale( 2.0f, 2.0f );
//						canvas.translate( (float)(touchXZ[0]), (float)(touchYZ[0]) );
//						canvas.drawRect(rectArr[i][j].getRect( ), paint);
//						canvas.restore( );
//					}
//					else{*/
//						canvas.drawRect(rectArr[i][j].getRect(), paint);
//						//}
//
//
//						//Log.d("TAG", "left: " + rectArr[i][j].left + " top: " + rectArr[i][j].top);
//						Log.d("TAG", "--current-sqr-coloured-1: [" + i + "] [" + j + "]");
//
//						rectLayout.invalidate();
//
//						newSqrTouched = true;
//
//						currentRectColoured.update(i, j); //update this
//						rectArr[i][j].select(); //mark this rect as selected in array
//
//					}
//				}
//			}
//
//			//if user touched screen but did not touch valid square
//			if (newSqrTouched == false)
//			{
//				//draw current so far back to grey
//
//				// set proper colour for changeable vs fixed squares
//				if (currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()] != 0) {
//					paint.setColor(Color.parseColor("#a2a2a2")); // set darker colour for fixed numbers
//				} else {
//					paint.setColor(Color.parseColor("#c2c2c2")); // set lighter colour for fixed numbers
//				}
//
//				if (currentRectColoured.getRow() != -1) // 'if statement' to avoid indexing element -1
//				{
//					if (zoomOn[0] == 1)//colour on zoom mode
//					{
//						canvas.save();
//						canvas.scale(2.0f, 2.0f);
//						canvas.translate((float) (-touchXZ[0]), (float) (-touchYZ[0]));
//						canvas.drawRect(rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()].getRect(), paint);
//						canvas.restore();
//					} else {
//						canvas.drawRect(rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()].getRect(), paint);
//					}
//					//Log.d("TAG", "--drawn");
//				}
//
//				if (forcePaint == true) // when forcePaint set to 'true', when user touched outside valid square, it will deselect the square
//				// when set to 'false' it means a button was clicked, and should keep the square selected
//				{
//					if (currentRectColoured.getRow() != -1) //index check
//					{
//						rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()].deselect(); //deselect from array as well
//					}
//					currentRectColoured.update(-1, -1); //set (pair to) -1 because no new rect was drawn
//				} else {
//					paint.setColor(Color.parseColor("#ff0000")); // here activate when button is used to insert
//					if (zoomOn[0] == 1)//colour on zoom mode
//					{
//						canvas.save();
//						canvas.scale(2.0f, 2.0f);
//						canvas.translate((float) (-touchXZ[0]), (float) (-touchYZ[0]));
//						canvas.drawRect(rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()].getRect(), paint);
//						canvas.restore();
//					} else {
//						canvas.drawRect(rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()].getRect(), paint);
//					}
//				}
//
//				Log.d("TAG", "--current-sqr-coloured-2: [" + currentRectColoured.getRow() + "] [" + currentRectColoured.getColumn() + "]");
//				Log.d("TAG", "--last-sqr-coloured-2: [" + lastRectColoured.getRow() + "] [" + lastRectColoured.getColumn() + "]");
//
//				// redraw text overlay
//				textOverlay.reDrawText(usrLangPref);
//
//				rectLayout.invalidate();
//			}
//
//			//if new square coloured, de-colour prev
//			if (newSqrTouched == true)
//			{
//				// set proper colour for changeable vs fixed squares
//				if (lastRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[lastRectColoured.getRow()][lastRectColoured.getColumn()] != 0) {
//					paint.setColor(Color.parseColor("#a2a2a2")); // set darker colour for fixed numbers
//				} else {
//					paint.setColor(Color.parseColor("#c2c2c2")); // set lighter colour for fixed numbers
//				}
//
//				Log.d("TAG", "--current-sqr-coloured-3: [" + currentRectColoured.getRow() + "] [" + currentRectColoured.getColumn() + "]");
//
//				if ((lastRectColoured.getRow() != -1))
//				{
//					Log.d("TAG", "--pass lastRow: " + lastRectColoured.getRow() + ", currentRow: " + currentRectColoured.getRow());
//					if ((lastRectColoured.getRow() == currentRectColoured.getRow()) // skip redrawing if the same square clicked twice in a row?
//							&& (lastRectColoured.getColumn() == currentRectColoured.getColumn())) {
//						//pass
//					} else {
//						if (zoomOn[0] == 1)//colour on zoom mode
//						{
//							canvas.save();
//							canvas.scale(2.0f, 2.0f);
//							canvas.translate((float) (-touchXZ[0]), (float) (-touchYZ[0]));
//							canvas.drawRect(rectArr[lastRectColoured.getRow()][lastRectColoured.getColumn()].getRect(), paint);
//							canvas.restore();
//						} else {
//							canvas.drawRect(rectArr[lastRectColoured.getRow()][lastRectColoured.getColumn()].getRect(), paint);
//						}
//					}
//				}
//
//				// redraw text overlay
//				textOverlay.reDrawText(usrLangPref);
//
//				rectLayout.invalidate();
//			}

				/** DRAW ZOOM OUT MODE **/


			newSqrTouched = false; //reset
			//prevNewSqrTouched = newSqrTouched;


			// save and reset canvas
			//canvas.save();
			canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //clear screen

			//if drag[0] == 0 then user clicked
			/*if (drag[0] == 0) {
				//scale to find square where user clicked
				px = ( touchXZ[0] + touchX[0] ) / 2; //divide by 2 to scale
				py = ( touchYZ[0] + touchY[0] ) / 2;
			}

			Log.d( "TAG-2", " touchXZ: " + touchXZ[0] + ", touchX: " + touchX[0] );

			// if drag enabled, then translate matrix
			if (drag[0] == 1) {
				canvas.translate(-touchXZ[0] + dX[0], -touchYZ[0] + dY[0]);
			} else { //else user only clicked
				Log.d( "TAG-2", "--translate on drag == 0" );
				canvas.translate(-touchXZ[0], -touchYZ[0]);
			}

			canvas.scale(2.0f, 2.0f);
			*/

			// loop to find selected rect
			// note: this loop must be separate; cannot be combined with colour loop because running
			// 		 these simultaneously wont work in case when user has selected a later square
			//		 because the deselected square wont get updated in time to be decoloured

			if( true ) // here it means user switched  zoom mode so scale do not update touchX
			{
				//
			}

			if( zoomButtonSafe[0] == 0 && zoomButtonDisableUpdate[0] == 0 ) // do not update on button click (zoomSafe == 0 means only test after going out of "zoom" mode, that is, do not check the 'click coordinate' if it is in a square because those coordinates do not count as a click)
			{																// when zoomButtonDisableUpdate[0] == 0, do not update currectRectColoured (when switching "zoom" modes)
				for (int i = 0; i < 9; i++)
				{
					for (int j = 0; j < 9; j++)
					{
						if( rectArr[i][j].getRect( ).contains( touchX[0], touchY[0] )  ) // find if sqr was clicked
						{
							/*
							// if in "zoom", check to see if clicking outside the zoomed map
							// before when clicked in empty space on BOTTOM side, it highlighted square because click was valid within rectLayout
							if( touchX[0] > BIT_MAP_W || touchY[0] > BIT_MAP_H )
							{
								// NOTE: this works only for hardcoded bitmap size : should change this code when adapting bitmap to screen size
								break; // do not count click (outside bound)
							}*/

							lastRectColoured.update(currentRectColoured.getRow(), currentRectColoured.getColumn());
							if (lastRectColoured.getRow() != -1) //avoid indexing out of array since -1 means nothing was previously selected
							{
								// this "if statement" must occur after setting lastRect.update() to currentRect
								rectArr[lastRectColoured.getRow()][lastRectColoured.getColumn()].deselect(); //deselect prev selected
							}


							newSqrTouched = true;

							currentRectColoured.update(i, j); // update this (must occur before rectArr.select()
							if (currentRectColoured.getRow() != -1) //bound check
							{
								rectArr[i][j].select(); //update array as well
							}



							//Log.d("TAG", "left: " + rectArr[i][j].left + " top: " + rectArr[i][j].top);
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


			// draw text overlay
			textOverlay.reDrawText(usrLangPref);


			//canvas.restore();
			rectLayout.invalidate();

		}
		else if (zoomOn[0] == 1)
		{

				/** DRAW ZOOM IN MODE **/

			newSqrTouched = false; //reset
			//prevNewSqrTouched = newSqrTouched;


			// save and reset canvas
			canvas.save();
			canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //clear screen

			//if drag[0] == 0 then user clicked
			if (drag[0] == 0 ) {
				//scale to find square where user clicked
				px = ( touchXZ[0] + touchX[0] ) / 2; //divide by 2 to scale
				py = ( touchYZ[0] + touchY[0] ) / 2;
			}

			Log.d( "TAG-2", " touchXZ: " + touchXZ[0] + ", touchX: " + touchX[0] );

			// if drag enabled, then translate matrix
			if (drag[0] == 1) {
				canvas.translate(-touchXZ[0] + dX[0], -touchYZ[0] + dY[0]);
			} else { //else user only clicked
				Log.d( "TAG-2", "--translate on drag == 0" );
				canvas.translate(-touchXZ[0], -touchYZ[0]);
			}

			canvas.scale(2.0f, 2.0f);


			// loop to find selected rect
			// note: this loop must be separate; cannot be combined with colour loop because running
			// 		 these simultaneously wont work in case when user has selected a later square
			//		 because the deselected square wont get updated in time to be decoloured

			if( drag[0] == 0 && zoomButtonDisableUpdate[0] == 0 ) // do not update on button click (zoomSafe == 0 means only test after going in "zoom" mode, that is, do not check the 'click coordinate' if it is in a square because those coordinates do not count as a click)
			{
				for (int i = 0; i < 9; i++)
				{
					for (int j = 0; j < 9; j++)
					{
						if (rectArr[i][j].getRect().contains(px, py) && zoomClickSafe[0] == 0) // find if sqr was clicked
						{
							// if in "zoom", check to see if clicking outside the zoomed map
							// before when clicked in empty space on BOTTOM side, it highlighted square because click was valid within rectLayout
							if( touchX[0] > BIT_MAP_W || touchY[0] > BIT_MAP_H )
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


							newSqrTouched = true;

							currentRectColoured.update(i, j); // update this (must occur before rectArr.select()
							if (currentRectColoured.getRow() != -1) //bound check
							{
								rectArr[i][j].select(); //update array as well
							}



							//Log.d("TAG", "left: " + rectArr[i][j].left + " top: " + rectArr[i][j].top);
							Log.d("TAG", "--touched-2: [" + i + "] [" + j + "]");
						}
					}
				}

				if( newSqrTouched == false && zoomClickSafe[0] == 0 ) // if user touched but did not touch a square -deselect
				{													  // zoomClickSafe is supposed to block deselection after user switched mode AND clicked button right away
					if( currentRectColoured.getRow() != -1 ) {
						rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()].deselect();
					}
					Log.d( "TAG-2", " --outside click" );
					currentRectColoured.update(-1, -1);
				}
			}

			////////////////////
			//
			//	fix in zoom mode when clicking outside, it should deselect square -- finish implementing code from reDraw to reDrawZoom
			//
			//	fix when on first click it does not select sqr
			//
			////////////////////


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


			// draw text overlay
			textOverlay.reDrawText(usrLangPref);


			canvas.restore();
			rectLayout.invalidate();


			////////////////
			//
			//	TODO: !!! !!! !!! !!! !!! !!! !!! !!! !!! !!! !!!    !!! !!!    !!! !!!
			//
			//	TODO: only use reDrawZoom for zoom, since have to draw entire array
			//	TODO: change rectArr from only rect to objects that hold rect, + text + colour + shade + if-last-selected( red )
			//	TODO: CONSIDER DRAWING ALL SQUARES FIRST IN ORIGINAL GREY SHADES, THEN IF RED OR DESELECT, DEAL WITH THOSE CASES AFTER
			//
			// 	TODO: !!! !!! !!! !!! !!! !!! !!! !!! !!! !!! !!!    !!! !!!    !!! !!!
			//
			////////////////////
		}
	}






/*
	//function similar to reDraw( ) but works for "zoom and drag"
	public void reDrawZoom( int[] touchX, int[] touchY, Pair lastRectColoured, Pair currentRectColoured,
						boolean forcePaint, int usrLangPref )
	{
		newSqrTouched = false; //reset
		//prevNewSqrTouched = newSqrTouched;

		Log.d( "TAG-2", " --CLICK" );


		// save and reset canvas
		canvas.save( );
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //clear screen

		//if drag[0] == 0 then user clicked
		if( drag[0] == 0 )
		{
			//scale to find square where user clicked
			px = touchXZ[0] + touchX[0]/2;
			py = touchYZ[0] + touchY[0]/2;
		}


		// if drag enabled, then translate matrix
		if( drag[0] == 1 )
		{
			canvas.translate( -touchXZ[0] + dX[0], -touchYZ[0] + dY[0] );
		} else { //else user only clicked
			canvas.translate( -touchXZ[0], -touchYZ[0] );
		}

		canvas.scale( 2.0f, 2.0f );


		// loop to find selected rect
		// note: this loop must be separate; cannot be combined with colour loop because running
		// 		 these simultaneously wont work in case when user has selected a later square
		//		 because the deselected square wont get updated in time to be decoloured
		if( drag[0] == 0 ) //update only on click
		{
			for (int i = 0; i < 9; i++)
			{
				for (int j = 0; j < 9; j++)
				{
					if (rectArr[i][j].getRect().contains(px, py)) // find if sqr was clicked
					{
						lastRectColoured.update(currentRectColoured.getRow(), currentRectColoured.getColumn());
						if (lastRectColoured.getRow() != -1) //avoid indexing out of array since -1 means nothing was previously selected
						{
							// this "if statement" must occur after setting lastRect.update() to currentRect
							rectArr[lastRectColoured.getRow()][lastRectColoured.getColumn()].deselect(); //deselect prev selected
						}


						newSqrTouched = true;

						if (currentRectColoured.getRow() != -1) //bound check
						{
							rectArr[i][j].select(); //update array as well
						}
						currentRectColoured.update(i, j); //update this


						//Log.d("TAG", "left: " + rectArr[i][j].left + " top: " + rectArr[i][j].top);
						Log.d("TAG", "--touched: [" + i + "] [" + j + "]");
					}
				}
			}

			if( newSqrTouched == false ) // if user touched but did not touch a square -deselect
			{
				if( currentRectColoured.getRow() != -1 ) {
					rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()].deselect();
				}
				Log.d( "TAG-2", " --outside click" );
				currentRectColoured.update(-1, -1);
			}

		}




		////////////////////
		//
		//	fix in zoom mode when clicking outside, it should deselect square -- finish implementing code from reDraw to reDrawZoom
		//
		////////////////////


		// redraw all squares in zoom/scale mode with corresponding shade
		for( int i=0; i<9; i++ )
		{
			for( int j=0; j<9; j++ )
			{
				//set colours; must occur after figuring out is rect contained
				if( rectArr[i][j].isSelected( ) ) //if a selected rect
				{
					//note: if rect selected, choose red and skip rest of colours - so red must be first colour assigned
					paint.setColor(Color.parseColor("#ff0000"));
				}
				else if (usrSudokuArr.PuzzleOriginal[i][j] != 0) // if a element that cannot be modified
				{
					paint.setColor(Color.parseColor("#a2a2a2")); // set darker colour for fixed numbers
				} else {
					paint.setColor(Color.parseColor("#c2c2c2")); // set lighter colour for fixed numbers
				}



				canvas.drawRect(rectArr[i][j].getRect( ), paint);
			}
		}




			canvas.restore( );
			rectLayout.invalidate( );


		*/



		////////////////
		//
		//	TODO: !!! !!! !!! !!! !!! !!! !!! !!! !!! !!! !!!    !!! !!!    !!! !!!
		//
		//	TODO: only use reDrawZoom for zoom, since have to draw entire array
		//	TODO: change rectArr from only rect to objects that hold rect, + text + colour + shade + if-last-selected( red )
		//	TODO: CONSIDER DRAWING ALL SQUARES FIRST IN ORIGINAL GREY SHADES, THEN IF RED OR DESELECT, DEAL WITH THOSE CASES AFTER
		//
		// 	TODO: !!! !!! !!! !!! !!! !!! !!! !!! !!! !!! !!!    !!! !!!    !!! !!!
		//
		////////////////////



		/*for( int i=0; i<9; i++ )
		{
			for( int j=0; j<9; j++ )
			{
				////////////  -----------
				// important: scale coordinates if called from button listener (in "zoom mode")
				// needed because when called from listener, it is not called with zoom coordiantes
				// because listeners set with 'final' regular variable, so have to transform to 'zoom'
				if( zoomOn[0] == 1 )
				{ px = zoomX[0]; py = zoomY[0]; }
				else{ px = x[0]; py = y[0]; }

				if( (zoomOn[0] == 1 && py > 525) || (zoomOn[0] == 1 && px > 527) ) //if in 'zoom mode' and if outside (right or lower) bound, mark as invalid
				{ break; }

				if( rectArr[i][j].contains( px, py ) )
				{
					lastRectColoured.update( currentRectColoured.getRow(), currentRectColoured.getColumn() );



					newSqrTouched = true;

					currentRectColoured.update( i, j ); //update this
				}
				/////////////  -----------

				paint.setColor(Color.parseColor("#ff0000"));

				//Toast.makeText(GameActivity.this, "REKT CLICKED", Toast.LENGTH_SHORT).show();

				if( zoomOn[0] == 1 )//colour on zoom mode
				{

					//canvas.scale( 2.0f, 2.0f );

					//allow the canvas to be translated to mimic 'drag'
					if( drag[0] == 1 ) //if drag enabled on touch
					{



					}

					canvas.drawRect(rectArr[i][j], paint);

				}
				else{
					canvas.drawRect(rectArr[i][j], paint);
				}


				//Log.d("TAG", "left: " + rectArr[i][j].left + " top: " + rectArr[i][j].top);
				Log.d( "TAG", "--current-sqr-coloured-1: [" + i + "] [" + j + "]" );


			}
		}*/



		/*
		//if user touched screen but did not touch valid square
		if( newSqrTouched == false )
		{
			//draw current so far back to grey

			// set proper colour for changeable vs fixed squares
			if( currentRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[currentRectColoured.getRow()][currentRectColoured.getColumn()] != 0 )
			{
				paint.setColor(Color.parseColor("#a2a2a2")); // set darker colour for fixed numbers
			}
			else
			{
				paint.setColor(Color.parseColor("#c2c2c2")); // set lighter colour for fixed numbers
			}

			if( currentRectColoured.getRow() != -1 ) // 'if statement' to avoid indexing element -1
			{
				if( zoomOn[0] == 1 )//colour on zoom mode
				{
					canvas.save( );
					canvas.scale( 2.0f, 2.0f );
					canvas.drawRect(rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()], paint);
					canvas.restore( );
				}
				else {
					canvas.drawRect(rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()], paint);
				}
				Log.d( "TAG", "--drawn" );
			}

			if( forcePaint == true ) // when forcePaint set to 'true', when user touched outside valid square, it will deselect the square
			// when set to 'false' it means a button was clicked, and should keep the square selected
			{
				currentRectColoured.update(-1, -1); //set -1 because no new rect was drawn
			}
			else
			{
				paint.setColor(Color.parseColor("#ff0000")); // here activate when button is used to insert
				if( zoomOn[0] == 1 )//colour on zoom mode
				{
					canvas.save( );
					canvas.scale( 2.0f, 2.0f );
					canvas.drawRect(rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()], paint);
					canvas.restore( );
				}
				else {
					canvas.drawRect(rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()], paint);
				}
			}

			Log.d( "TAG", "--current-sqr-coloured-2: [" + currentRectColoured.getRow() + "] [" + currentRectColoured.getColumn() + "]" );
			Log.d( "TAG", "--last-sqr-coloured-2: [" + lastRectColoured.getRow() + "] [" + lastRectColoured.getColumn() + "]" );

			// redraw text overlay
			textOverlay.reDrawText( usrLangPref );

			rectLayout.invalidate( );
		}

		//if new square coloured, de-colour prev
		if( newSqrTouched == true )
		{
			// set proper colour for changeable vs fixed squares
			if( lastRectColoured.getRow() != -1 && usrSudokuArr.PuzzleOriginal[lastRectColoured.getRow()][lastRectColoured.getColumn()] != 0 )
			{
				paint.setColor(Color.parseColor("#a2a2a2")); // set darker colour for fixed numbers
			}
			else
			{
				paint.setColor(Color.parseColor("#c2c2c2")); // set lighter colour for fixed numbers
			}

			Log.d( "TAG", "--current-sqr-coloured-3: [" + currentRectColoured.getRow() + "] [" + currentRectColoured.getColumn() + "]" );

			if( (lastRectColoured.getRow() != -1) )
			{
				Log.d( "TAG", "--pass lastRow: " + lastRectColoured.getRow() + ", currentRow: " + currentRectColoured.getRow() );
				if( (lastRectColoured.getRow() == currentRectColoured.getRow() ) // skip redrawing if the same square clicked twice in a row?
						&& ( lastRectColoured.getColumn() == currentRectColoured.getColumn() ) )
				{
					//pass
				}
				else
				{
					if( zoomOn[0] == 1 )//colour on zoom mode
					{
						canvas.save( );
						canvas.scale( 2.0f, 2.0f );
						canvas.drawRect(rectArr[lastRectColoured.getRow()][lastRectColoured.getColumn()], paint);
						canvas.restore( );
					}
					else {
						canvas.drawRect(rectArr[lastRectColoured.getRow()][lastRectColoured.getColumn()], paint);
					}
				}
			}

			// redraw text overlay
			textOverlay.reDrawText( usrLangPref );

			rectLayout.invalidate( );
		}*/
	//}



	/*
	//allow the canvas to be translated to mimic 'drag'
						if( drag[0] == 1 ) //if drag enabled on touch
						{
							canvas.translate( dX[0], dY[0] );
						}
	 */
}
