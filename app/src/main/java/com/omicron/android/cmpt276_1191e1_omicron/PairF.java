package com.omicron.android.cmpt276_1191e1_omicron;

public class PairF
{
    private float x; // row index; note: coordinate -1,-1 means user did not touch square
    private float y; // column index

    public PairF( float row, float col)
    {
        x = row;
        y = col;
    }

    public void update( float row, float col )
    {
        x = row;
        y = col;
    }

    public float getColumn( )
    {
        return y;
    }
    public float getRow( )
    {
        return x;
    }

}