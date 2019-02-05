package com.omicron.android.cmpt276_1191e1_omicron;

public class CheckTouch
{
	private int x;
	private int y;
	private int m; //coordinate of square in 2D array - row index
	private int n; // column index
	private float sqrL;
	private float sqrT;
	private float sqrR;
	private float sqrB;
	private float sqrLO;
	private float sqrTO;
	private boolean touch;
	private Pair p;
	private Pair sqrSelected;

	public CheckTouch( float sqrLOrig, float sqrTOrig )
	{
		p = new Pair( -1, -1 ); //note: coordinate -1,-1 means user did not touch square
		touch = false;
		sqrLO = sqrLOrig;
		sqrTO = sqrTOrig;
	}

	public boolean touch( )
	{
		return touch; //returns if a square was touched
	}

	public Pair check( int xp, int yp )
	{
		//update
		x = xp;
		y = yp;
		touch = false;

		for( int i=0; i<9; i++ ) //row
		{
			for( int j=0; j<9; j++ ) //column
			{
				//increase square dimensions
				sqrL = sqrLO + j*(105+5);
				sqrT = sqrTO + i*(105+5);
				sqrR = sqrL + 105;
				sqrB = sqrT + 105;

				//add padding
				if( i>=3 ) //add extra space between rows
				{
					sqrT = sqrT + 15;
					sqrB = sqrB + 15;
				}
				if( i>=6 )
				{
					sqrT = sqrT + 15;
					sqrB = sqrB + 15;
				}

				if( j>=3 ) //add extra space between columns
				{
					sqrL = sqrL + 15;
					sqrR = sqrR + 15;
				}
				if( j>=6 )
				{
					sqrL = sqrL + 15;
					sqrR = sqrR + 15;
				}

				//validate touch coordinate
				if( (xp>=sqrL && yp>=sqrT) && (xp<=sqrR && yp<=sqrB) )
				{
					touch = true;
					m = i;
					n = j;

				}
			}
		}

		if( touch == true )
		{
			p.update( m, n );

		}
		else //no valid touch
		{
			p.update( -1, -1 );
		}
		return p;
	}
}
