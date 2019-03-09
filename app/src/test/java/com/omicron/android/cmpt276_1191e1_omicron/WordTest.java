package com.omicron.android.cmpt276_1191e1_omicron;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordTest {

    private Word mWord = new Word("English", "French", 12, 5);

    @Test
    public void getNative() {
        assertEquals("English", mWord.getNative());
    }

    @Test
    public void getTranslation() {
        assertEquals("French", mWord.getTranslation());
    }

    @Test
    public void setNative() {
        mWord.setNative("Japanese");
        assertEquals("Japanese", mWord.getNative());
    }

    @Test
    public void setTranslation() {
        mWord.setTranslation("Chinese");
        assertEquals("Chinese", mWord.getTranslation());
    }

    @Test
    public void getInFileLineNum() {
        assertEquals(12, mWord.getInFileLineNum());
    }

    @Test
    public void getHintClick() {
        assertEquals(5, mWord.getHintClick());
    }

    @Test
    public void incrementHintClick() {
        mWord.incrementHintClick();
        assertEquals(6, mWord.getHintClick());
    }

    @Test
    public void updateHintClick() {
        mWord.updateHintClick(8);
        assertEquals(8, mWord.getHintClick());
    }

}