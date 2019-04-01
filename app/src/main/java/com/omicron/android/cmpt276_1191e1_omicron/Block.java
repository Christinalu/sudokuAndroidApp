package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import java.io.Serializable;

public class Block implements Serializable
{
	/*
	 * This class holds a single puzzle rectangle and contains certain attributes
	 */

	private Rect sqr; //rectangle
	private boolean selected; //indicates if rect is the one user selected
	private TextView textView;
	private String lastColour = "#000000"; //black
	private boolean conflict = false; //if word has row/col/block conflict; no conflict does not guarantee correct solution

	public Block( int sqrL, int sqrT, int sqrR, int sqrB )
	{
		sqr = new Rect( sqrL, sqrT, sqrR, sqrB );
		selected = false; //default
	}

	public boolean isSelected( )
	{
		return selected;
	}

	public boolean getSelected(){
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
	public void setLastColour( String str ){ lastColour = str; }
	public String getLastColour( ){ return lastColour; }
	public void setConflict(  boolean c ){ conflict = c; }
	public boolean getConflict( ){ return conflict; }
}
