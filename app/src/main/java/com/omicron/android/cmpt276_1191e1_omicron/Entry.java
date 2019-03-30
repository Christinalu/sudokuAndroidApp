package com.omicron.android.cmpt276_1191e1_omicron;

import android.util.Log;

import java.io.Serializable;

public class Entry implements Serializable
{
	/*
	 *	This class holds coordiante x,y of an object in 2D array
	*/

	private int val; // row index; note: coordinate -1,-1 means user did not touch square
	private Pair coor; // column index

	public Entry(int value, Pair coordinate)
	{
		val = value;
		coor = coordinate;
	}

	public void update( int value, Pair coordinate ) // update with new values
	{
		val = value;
		coor = coordinate;
	}

	public void print() {
		Log.d("TESTI", "Entry is: val("+val+") coor("+coor.getRow()+", "+coor.getColumn()+")");
	}

	public boolean isEqual(Entry e) {
		if (e.getValue() == val && e.getCoordinate().isEqual(coor)) {
			return true;
		}
		return false;
	}

	public int getValue( )
	{
		return val;
	}
	public Pair getCoordinate( )
	{
		return coor;
	}

}
