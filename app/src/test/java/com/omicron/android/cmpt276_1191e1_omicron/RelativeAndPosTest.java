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
        int sqrt2 = 3;
        int sqrtl2 = 4;
        mRelativeAndPos.setCoordinates(sqrt2,sqrtl2);
        assertEquals(sqrt2, mRelativeAndPos.getSqrT());
        assertEquals(sqrtl2, mRelativeAndPos.getSqrL());
    }

    @Test
    public void getSqrT() {
        int sqrt2 = 3;
        int sqrtl2 = 4;
        mRelativeAndPos.setCoordinates(sqrt2,sqrtl2);
        assertEquals(sqrt2, mRelativeAndPos.getSqrT());
    }

    @Test
    public void getSqrL() {
        int sqrt2 = 3;
        int sqrtl2 = 4;
        mRelativeAndPos.setCoordinates(sqrt2,sqrtl2);
        assertEquals(sqrtl2, mRelativeAndPos.getSqrL());
    }
}