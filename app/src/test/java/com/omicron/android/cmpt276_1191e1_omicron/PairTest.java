package com.omicron.android.cmpt276_1191e1_omicron;

import org.junit.Test;
import org.junit.runner.notification.RunListener;

import static org.junit.Assert.*;

public class PairTest {

    private Pair mPair = new Pair(4,7);

    @Test
    public void getColumn() throws Exception{
        assertEquals(7,mPair.getColumn(),0);
    }

    @Test
    public void getRow() {
        assertEquals(4, mPair.getRow(), 0);
    }

    @Test
    public void update() throws Exception{
        mPair.update(6,2);
        assertEquals(6,mPair.getRow(),0);
        assertEquals(2,mPair.getColumn(),0);
    }
}