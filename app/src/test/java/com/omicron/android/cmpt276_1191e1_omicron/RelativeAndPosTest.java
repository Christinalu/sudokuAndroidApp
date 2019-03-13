package com.omicron.android.cmpt276_1191e1_omicron;

import android.content.Context;
import android.widget.RelativeLayout;

import org.junit.Test;

import static org.junit.Assert.*;

public class RelativeAndPosTest {

    private Context mContext;
    private RelativeAndPos mRelativeAndPos = new RelativeAndPos(mContext);

    @Test
    public void getRelativeLayout() throws Exception{
        assertNull(mContext);
    }

    @Test
    public void setCoordinates() throws Exception{
        mRelativeAndPos.setCoordinates(3,4);
        assertEquals(3, mRelativeAndPos.getSqrT());
        assertEquals(4, mRelativeAndPos.getSqrL());
    }

    @Test
    public void getSqrT() {
        assertEquals(0, mRelativeAndPos.getSqrT());
    }

    @Test
    public void getSqrL() {
        assertEquals(0, mRelativeAndPos.getSqrL());
    }
}