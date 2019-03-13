package com.omicron.android.cmpt276_1191e1_omicron;

import org.junit.Test;

import static org.junit.Assert.*;

public class RangeTest {

    private Range mRange = new Range(455, 7888, "kitchen", "cuisine",3);

    @Test
    public void getNumLeft() {
        assertEquals(455, mRange.getNumLeft());
    }

    @Test
    public void getNumRight() {
        assertEquals(7888, mRange.getNumRight());
    }

    @Test
    public void getStrNative() {
        assertEquals("kitchen", mRange.getStrNative());
    }

    @Test
    public void getStrTranslation() {
        assertEquals("cuisine", mRange.getStrTranslation());
    }

    @Test
    public void getHintClick() {
        assertEquals(3, mRange.getHintClick());
    }
}