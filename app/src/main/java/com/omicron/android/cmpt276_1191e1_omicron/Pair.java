package com.omicron.android.cmpt276_1191e1_omicron;

public class Pair
{
	private int x; // row index; note: coordinate -1,-1 means user did not touch square
	private int y; // column index

	public Pair( int row, int col)
	{
		x = row;
		y = col;
	}

	public void update( int row, int col )
	{
		x = row;
		y = col;
	}

	public int getColumn( )
	{
		return y;
	}
	public int getRow( )
	{
		return x;
	}

}
