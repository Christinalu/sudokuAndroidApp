package com.omicron.android.cmpt276_1191e1_omicron;

import org.junit.Test;

import static org.junit.Assert.*;

public class RangeTest {
    private long left = 455; // left range index
    private long right = 7888; // right range index
    private String strNative = "kitchen";
    private String strTranslation = "cuisine";
    private int hintClick = 3;

    private Range mRange = new Range(left, right, strNative, strTranslation,hintClick);

    @Test
    public void getNumLeft() {
        assertEquals(left, mRange.getNumLeft());
    }

    @Test
    public void getNumRight() {
        assertEquals(right, mRange.getNumRight());
    }

    @Test
    public void getStrNative() {
        assertEquals(strNative, mRange.getStrNative());
    }

    @Test
    public void getStrTranslation() {
        assertEquals(strTranslation, mRange.getStrTranslation());
    }

    @Test
    public void getHintClick() {
        assertEquals(hintClick, mRange.getHintClick());
    }

    @Test
    public void setNumLeftANDsetNumRight() {
        int check = 0;
        int LEFT = 10;
        int RIGHT = 20;
        mRange.setNumLeft(LEFT);
        mRange.setNumRight(RIGHT);
        if (mRange.getNumLeft()!=LEFT || mRange.getNumRight()!=RIGHT) {
            check++;
        }
        assertEquals(0,check);
    }
}