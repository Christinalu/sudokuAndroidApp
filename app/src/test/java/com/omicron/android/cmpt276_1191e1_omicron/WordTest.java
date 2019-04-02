package com.omicron.android.cmpt276_1191e1_omicron;

import com.omicron.android.cmpt276_1191e1_omicron.Model.Word;

import org.junit.Test;

import static org.junit.Assert.*;

public class WordTest {

    private Word mWord = new Word("English", "French", 12, 5);
    private Word mWord2 = new Word("Chinese","Japanese", 30, 8);

    @Test
    public void getNative() {
        assertNotNull(mWord);
        assertNotNull(mWord2);
        assertEquals("English", mWord.getNative());
        assertNotEquals("French", mWord.getNative());
    }

    @Test
    public void getTranslation() {
        assertEquals("French", mWord.getTranslation());
        assertNotEquals("English", mWord2.getTranslation());
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
        mWord2.setTranslation("French");
        assertNotEquals("English", mWord2.getTranslation());
    }

    @Test
    public void getInFileLineNum() {
        assertEquals(12, mWord.getInFileLineNum());
        assertNotEquals(23, mWord.getInFileLineNum());
        assertEquals(30, mWord2.getInFileLineNum());
        assertNotEquals(4, mWord2.getInFileLineNum());
    }

    @Test
    public void getHintClick() {
        assertEquals(5, mWord.getHintClick());
        assertNotEquals(9, mWord.getHintClick());
        assertEquals(8, mWord2.getHintClick());
        assertNotEquals(22, mWord2.getHintClick());
    }

    @Test
    public void incrementHintClick() {
        mWord.incrementHintClick();
        assertEquals(6, mWord.getHintClick());
        assertNotEquals(5, mWord.getHintClick());
        mWord2.incrementHintClick();
        assertEquals(9, mWord2.getHintClick());
        assertNotEquals(8, mWord2.getHintClick());
    }

    @Test
    public void updateHintClick() {
        mWord.updateHintClick(8);
        assertEquals(8, mWord.getHintClick());
        assertNotEquals(6, mWord.getHintClick());
        mWord2.updateHintClick(22);
        assertEquals(22, mWord2.getHintClick());
        assertNotEquals(8, mWord2.getHintClick());
    }

}