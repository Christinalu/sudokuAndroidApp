package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

public class Block
{
	/*
	 * This class holds a single puzzle rectangle and contains certain attributes
	 */

	private Rect sqr; //rectangle
	private boolean selected; //indicates if rect is the one user selected
	private TextView textView;

	public Block( int sqrL, int sqrT, int sqrR, int sqrB )
	{
		sqr = new Rect( sqrL, sqrT, sqrR, sqrB );
		selected = false; //default
	}

	public boolean isSelected( )
	{
		return selected;
	}

	public void select( )
	{
		selected = true;
	}

	public void deselect( )
	{
		selected = false;
	}
	public Rect getRect( )
	{
		return sqr;
	}

}
