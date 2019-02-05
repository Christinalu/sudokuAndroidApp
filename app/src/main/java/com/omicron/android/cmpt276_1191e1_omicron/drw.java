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

	public drw( )
	{

	}

	public void reDraw( Rect[][] rectArr, int x, int y, Paint paint, Canvas canvas,
						RelativeLayout rectLayout, Pair lastRectColoured, Pair currentRectColoured )
	{
		newSqrTouched = false; //reset

		//loop and draw if valid rect clicked
		for( int i=0; i<9; i++ )
		{
			for( int j=0; j<9; j++ )
			{
				if( rectArr[i][j].contains( x, y ) )
				{
					lastRectColoured.update( currentRectColoured.getRow(), currentRectColoured.getColumn() );

					paint.setColor(Color.parseColor("#ff0000"));

					//Toast.makeText(GameActivity.this, "REKT CLICKED", Toast.LENGTH_SHORT).show();

					canvas.drawRect(rectArr[i][j], paint);

					//Log.d("TAG", "left: " + rectArr[i][j].left + " top: " + rectArr[i][j].top);
					Log.d( "TAG", "--current-sqr-coloured: [" + i + "] [" + j + "]" );

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
			paint.setColor(Color.parseColor("#c2c2c2"));
			if( currentRectColoured.getRow() != -1 )
			{
				canvas.drawRect(rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()], paint);
			}
			currentRectColoured.update( -1, -1 ); //set -1 because no new rect was drawn

			Log.d( "TAG", "--current-sqr-coloured: [" + currentRectColoured.getRow() + "] [" + currentRectColoured.getColumn() + "]" );

			rectLayout.invalidate( );

			////////////////////////
			//
			// 	HERE DO NOT FORGET TO DESELECT SQUARE IF CLICKED OUTSIDE MATRIX
			//
			/////////////////
		}

		//if new square coloured, decolour prev
		if( newSqrTouched == true )
		{
			paint.setColor(Color.parseColor("#c2c2c2"));
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
			//currentRectColoured.update( -1, -1 ); //set -1 because no new rect was drawn

			rectLayout.invalidate( );
		}
	}
}
