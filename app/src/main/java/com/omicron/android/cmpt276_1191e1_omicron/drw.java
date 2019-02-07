package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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


	public drw( Rect[][] rectArr2, Paint paint2, Canvas canvas2,
				RelativeLayout rectLayout2, RedrawText textOverlay2, SudokuGenerator usrSudokuArr2 )
	{
		paint = paint2;
		rectArr = rectArr2;
		canvas = canvas2;
		rectLayout = rectLayout2;
		textOverlay = textOverlay2;
		usrSudokuArr = usrSudokuArr2;
	}

	public void reDraw( int[] x, int[] y, Pair lastRectColoured, Pair currentRectColoured, boolean forcePaint, int usrLangPref )
	{
		newSqrTouched = false; //reset
		prevNewSqrTouched = newSqrTouched;

		//loop and draw if valid rect clicked
		for( int i=0; i<9; i++ )
		{
			for( int j=0; j<9; j++ )
			{
				if( rectArr[i][j].contains( x[0], y[0] ) )
				{
					lastRectColoured.update( currentRectColoured.getRow(), currentRectColoured.getColumn() );

					paint.setColor(Color.parseColor("#ff0000"));

					//Toast.makeText(GameActivity.this, "REKT CLICKED", Toast.LENGTH_SHORT).show();

					canvas.drawRect(rectArr[i][j], paint);

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

			if( currentRectColoured.getRow() != -1 ) // if statement to avoid indexing element -1
			{

				canvas.drawRect(rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()], paint);
				Log.d( "TAG", "--drawn" );
			}

			if( forcePaint == true )
			{
				currentRectColoured.update(-1, -1); //set -1 because no new rect was drawn
			}
			else
			{
				paint.setColor(Color.parseColor("#ff0000")); // here activate when button is used to insert
				canvas.drawRect(rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()], paint);
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
				if( (lastRectColoured.getRow() == currentRectColoured.getRow() )
					&& ( lastRectColoured.getColumn() == currentRectColoured.getColumn() ) )
				{
					//pass
				}
				else
				{
					canvas.drawRect(rectArr[lastRectColoured.getRow()][lastRectColoured.getColumn()], paint);
				}
			}

			// redraw text overlay
			textOverlay.reDrawText( usrLangPref );

			rectLayout.invalidate( );
		}
	}
}
