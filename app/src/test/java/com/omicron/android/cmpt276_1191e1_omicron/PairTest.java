package com.omicron.android.cmpt276_1191e1_omicron;

import com.omicron.android.cmpt276_1191e1_omicron.Model.Pair;

import org.junit.Test;

import static org.junit.Assert.*;

public class PairTest {

    private Pair mPair = new Pair(4,7);
    private Pair mPair2 = mPair;

    @Test
    public void getColumn() throws Exception{
        assertNotNull(mPair);
        assertNotNull(mPair2);
        assertEquals(7,mPair.getColumn(),0);
        assertNotEquals(8, mPair2.getColumn(), 0);
    }

    @Test
    public void getRow() {
        assertEquals(4, mPair.getRow(), 0);
        assertNotEquals(1, mPair2.getRow(),0);
    }

    @Test
    public void update() throws Exception{
        mPair.update(6,2);
        assertEquals(6,mPair.getRow(),0);
        assertEquals(2,mPair.getColumn(),0);
        assertNotEquals(8, mPair2.getRow(),0);
        assertNotEquals(6,mPair2.getColumn(),0);
    }
}