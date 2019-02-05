package com.omicron.android.cmpt276_1191e1_omicron;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class ColourSqr
{
	public void ColourSqr(  )
	{

	}
	public void colour(Pair p, Pair sqrSelected, Pair sqrLastSelected, Pair sqrLastActive, Pair lastPair, Canvas canvas, Paint paint, float sqrLO, float sqrTO )
	{
		float left;
		float top;
		float right;
		float bottom;

		Log.d( "TAG", "--PASS-Colouring-Square" );
		Log.d( "TAG", "--sqrLO-2: " + sqrLO );
		Log.d( "TAG", "--sqrTO-2: " + sqrTO );

		if( p.getRow() != -1 ) //colour red
		{
			if( p.getRow() == sqrLastActive.getRow() && p.getColumn() == sqrLastActive.getColumn()
					&& lastPair.getRow() != -1 )
			{
				return;
			}
			//if( p.getRow() )
			//Log.d( "TAG", "--PASS-ColourSqr-2" );
			left = sqrLO + (p.getColumn() * (105 + 5));
			top = sqrTO + (p.getRow() * (105 + 5));
			right = left + (105);
			bottom = top + (105);

			//add padding
			if( p.getRow()>=3 ) //add extra space between rows
			{
				top = top + 15;
				bottom = bottom + 15;
			}
			if( p.getRow()>=6 )
			{
				top = top + 15;
				bottom = bottom + 15;
			}

			if( p.getColumn()>=3 ) //add extra space between columns
			{
				left = left + 15;
				right = right + 15;
			}
			if( p.getColumn()>=6 )
			{
				left = left + 15;
				right = right + 15;
			}

			canvas.drawRect(left, top, right, bottom, paint);
		}
		else  //colour back to grey
		{
			//Log.d( "TAG", "--PASS-ColourSqr-3" );
			left = sqrLO + (sqrSelected.getColumn() * (105 + 5));
			//Log.d( "TAG", "--PASS-ColourSqr-4" );
			top = sqrTO + (sqrSelected.getRow() * (105 + 5));
			right = left + (105);
			bottom = top + (105);

			//add padding
			if( sqrSelected.getRow()>=3 ) //add extra space between rows
			{
				top = top + 15;
				bottom = bottom + 15;
			}
			if( sqrSelected.getRow()>=6 )
			{
				top = top + 15;
				bottom = bottom + 15;
			}

			if( sqrSelected.getColumn()>=3 ) //add extra space between columns
			{
				left = left + 15;
				right = right + 15;
			}
			if( sqrSelected.getColumn()>=6 )
			{
				left = left + 15;
				right = right + 15;
			}

			canvas.drawRect(left, top, right, bottom, paint);
		}

		//if last square selected was a valid square - deal with selecting squares one after the other
		if(  p.getRow() != -1 && sqrLastSelected.getRow( ) != -1 && lastPair.getRow() != -1 )
		{
			paint.setColor(Color.parseColor("#c2c2c2"));

			//Log.d( "TAG", "--PASS-ColourSqr-3" );
			left = sqrLO + (sqrLastSelected.getColumn() * (105 + 5));
			//Log.d( "TAG", "--PASS-ColourSqr-4" );
			top = sqrTO + (sqrLastSelected.getRow() * (105 + 5));
			right = left + (105);
			bottom = top + (105);

			//add padding
			if( sqrLastSelected.getRow()>=3 ) //add extra space between rows
			{
				top = top + 15;
				bottom = bottom + 15;
			}
			if( sqrLastSelected.getRow()>=6 )
			{
				top = top + 15;
				bottom = bottom + 15;
			}

			if( sqrLastSelected.getColumn()>=3 ) //add extra space between columns
			{
				left = left + 15;
				right = right + 15;
			}
			if( sqrLastSelected.getColumn()>=6 )
			{
				left = left + 15;
				right = right + 15;
			}

			canvas.drawRect(left, top, right, bottom, paint);
		}

	}
}
