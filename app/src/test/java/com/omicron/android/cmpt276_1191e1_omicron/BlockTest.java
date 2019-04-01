package com.omicron.android.cmpt276_1191e1_omicron;

import android.graphics.Rect;

import org.junit.Test;

import static org.junit.Assert.*;

public class BlockTest {

    private Block mBlock = new Block(40,30,30,50);

    @Test
    public void isSelected() throws Exception {
        assertNotNull(mBlock);
        assertFalse(mBlock.isSelected());
    }

    @Test
    public void select() throws Exception{
        mBlock.select();
        assertTrue(mBlock.getSelected());
    }

    @Test
    public void deselect() throws Exception{
        mBlock.deselect();
        assertFalse(mBlock.getSelected());
    }

    @Test
    public void getRect() throws Exception{
        Rect expected = new Rect(40, 30, 30, 50);
        assertEquals(expected.left, mBlock.getRect().left);
        assertEquals(expected.top, mBlock.getRect().top);
        assertEquals(expected.right, mBlock.getRect().right);
        assertEquals(expected.bottom, mBlock.getRect().bottom);

        //False case
        assertNotEquals(20, mBlock.getRect().left);
        assertNotEquals(33, mBlock.getRect().top);
        assertNotEquals(23,mBlock.getRect().right);
        assertNotEquals(90,mBlock.getRect().bottom);
    }
}