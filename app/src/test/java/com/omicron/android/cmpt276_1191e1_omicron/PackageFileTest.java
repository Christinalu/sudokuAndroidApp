package com.omicron.android.cmpt276_1191e1_omicron;

import org.junit.Test;

import static org.junit.Assert.*;

public class PackageFileTest {

    private PackageFile mPackageFile = new PackageFile("my name", "file name", "Chinese", "Japanese");

    @Test
    public void getWordPackageName() throws Exception{
        assertEquals("my name", mPackageFile.getWordPackageName());
    }

    @Test
    public void getInternalFileName() throws Exception{
        assertEquals("file name", mPackageFile.getInternalFileName());
    }

    @Test
    public void getNativeLang() throws Exception{
        assertEquals("Chinese", mPackageFile.getNativeLang());
    }

    @Test
    public void getTranslateLang() throws Exception{
        assertEquals("Japanese", mPackageFile.getTranslateLang());
    }
}