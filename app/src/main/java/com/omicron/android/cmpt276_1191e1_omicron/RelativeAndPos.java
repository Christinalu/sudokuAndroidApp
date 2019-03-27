package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.widget.RelativeLayout;

public class RelativeAndPos
{
	/*
	 * This object holds a relative layout as well as its onCreate sqrL sqrT coordinates
	 * used to reset the layout and then translate it, in zoom mode
	 *
	 * This Relative Layout also contains a Text View, which will store the word
	 */

	private RelativeLayout relativeLay;
	private int sqrT;
	private int sqrL;

	public RelativeAndPos( Context context )
	{
		relativeLay = new RelativeLayout( context );
		sqrT = 0;
		sqrL = 0;
	}

	public RelativeLayout getRelativeLayout( )
	{ return relativeLay; }

	public void setCoordinates( int sqrT2, int sqrL2 )
	{
		sqrT = sqrT2;
		sqrL = sqrL2;
	}

	public int getSqrT( )
	{ return sqrT; }

	public int getSqrL( )
	{ return sqrL; }
}
