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
	private Rect[][] rectArr;
	private Canvas canvas;
	private RelativeLayout rectLayout;
	private RedrawText textOverlay;
	private boolean prevNewSqrTouched;
	private SudokuGenerator usrSudokuArr;
	private int[] zoomOn;
	private int[] zoomX;
	private int[] zoomY;
	private int px;
	private int py;
	private int[] drag;
	private int[] dX;
	private int[] dY;
	private int[] touchXZ;
	private int[] touchYZ;
	//private int[] zoomOn;


	public drw( Rect[][] rectArr2, Paint paint2, Canvas canvas2,
				RelativeLayout rectLayout2, RedrawText textOverlay2, SudokuGenerator usrSudokuArr2,
				int [] zoomOn2, int[] zoomX2, int[] zoomY2, int[] drag2, int[] dX2, int[] dY2,
				int[] touchXZ2, int[] touchYZ2 )
	{
		paint = paint2;
		rectArr = rectArr2;
		canvas = canvas2;
		rectLayout = rectLayout2;
		textOverlay = textOverlay2;
		usrSudokuArr = usrSudokuArr2;
		zoomOn = zoomOn2;
		zoomX = zoomX2;
		zoomY = zoomY2;
		drag = drag2;
		dX = dX2;
		dY = dY2;
		touchXZ = touchXZ2;
		touchYZ = touchYZ2;
		//zoomOn = zoomOn2;
	}

	public void reDraw( int[] x, int[] y, Pair lastRectColoured, Pair currentRectColoured,
						boolean forcePaint, int usrLangPref )
	{
		newSqrTouched = false; //reset
		prevNewSqrTouched = newSqrTouched;

		//loop and draw if valid rect clicked
		for( int i=0; i<9; i++ )
		{
			for( int j=0; j<9; j++ )
			{
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

					paint.setColor(Color.parseColor("#ff0000"));

					//Toast.makeText(GameActivity.this, "REKT CLICKED", Toast.LENGTH_SHORT).show();

					//////////////
					//
					//	if using reDrawZoom for zoom, then remove all zoom stuff from here
					//
					////////////

					if( zoomOn[0] == 1 )//colour on zoom mode
					{
						canvas.save( );
						canvas.scale( 2.0f, 2.0f );
						canvas.translate( (float)(touchXZ[0]), (float)(touchYZ[0]) );
						canvas.drawRect(rectArr[i][j], paint);
						canvas.restore( );
					}
					else{
						canvas.drawRect(rectArr[i][j], paint);
					}


					//Log.d("TAG", "left: " + rectArr[i][j].left + " top: " + rectArr[i][j].top);
					Log.d( "TAG", "--current-sqr-coloured-1: [" + i + "] [" + j + "]" );

					rectLayout.invalidate( );

					newSqrTouched = true;

					currentRectColoured.update( i, j ); //update this
				}
			}
		}

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
					canvas.translate( (float)(-touchXZ[0]), (float)(-touchYZ[0]) );
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
					canvas.translate( (float)(-touchXZ[0]), (float)(-touchYZ[0]) );
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
						canvas.translate( (float)(-touchXZ[0]), (float)(-touchYZ[0]) );
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
		}
	}

	//function similar to reDraw( ) but works for "zoom and drag"
	public void reDrawZoom( int[] touchX, int[] touchY, Pair lastRectColoured, Pair currentRectColoured,
						boolean forcePaint, int usrLangPref )
	{
		//newSqrTouched = false; //reset
		//prevNewSqrTouched = newSqrTouched;

		if( drag[0] == 0 ) //if only click, no drag - update matrix entry
		{






		}
		else if( drag[0] == 1 ) // if drag enabled, then translate matrix
		{
			//loop and draw if valid rect clicked
			canvas.save( );
			canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //clear screen

			canvas.translate( -touchXZ[0] + dX[0], -touchYZ[0] + dY[0] );
			canvas.scale( 2.0f, 2.0f );


			for( int i=0; i<9; i++ )
			{
				for( int j=0; j<9; j++ )
				{
					//set colour
					if (usrSudokuArr.PuzzleOriginal[i][j] != 0) // if a element that cannot be modified
					{
						paint.setColor(Color.parseColor("#a2a2a2")); // set darker colour for fixed numbers
					} else {
						paint.setColor(Color.parseColor("#c2c2c2")); // set lighter colour for fixed numbers
					}
					canvas.drawRect(rectArr[i][j], paint);
				}
			}




			canvas.restore( );
			rectLayout.invalidate( );
		}





		////////////////
		//
		//	TODO: !!! !!! !!! !!! !!! !!! !!! !!! !!! !!! !!!    !!! !!!    !!! !!!
		//
		//	TODO: only use reDrawZoom for zoom, since have to draw entire array
		//	TODO: change rectArr from only rect to objects that hold rect, + text + colour + shade + if-last-selected( red )
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
	}



	/*
	//allow the canvas to be translated to mimic 'drag'
						if( drag[0] == 1 ) //if drag enabled on touch
						{
							canvas.translate( dX[0], dY[0] );
						}
	 */
}
