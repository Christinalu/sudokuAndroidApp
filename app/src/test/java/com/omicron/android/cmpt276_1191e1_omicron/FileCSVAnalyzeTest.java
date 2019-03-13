package com.omicron.android.cmpt276_1191e1_omicron;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;

public class FileCSVAnalyzeTest {


    private FileCSVAnalyze mFileCSVAnalyze = new FileCSVAnalyze(150);
    private File mFile = new File("pkg_1.csv");
    private File mFile2 = new File("pkg_2.csv");
    private File mFile4 = new File("pgk_four.csv");
    private File mFile6 = new File("pkg_six.csv");
    private File[] files = new File[2];

    private BufferedReader mBufferedReader;
//    private String test = "English, French, bedroom, chambre, everyday, tourlesjours, or, ou, television, télé," +
//            "kitchen, cuisine, house, maison, apartment, appartement,you, tu,have, avoir,she, elle, but, mais" ;

    private String mStrings = "English,French\n"+"bedroom,chambre\n"+"everyday,tourlesjours\n" +
            "or,ou\n" + "television, télé\n" + "kitchen,cuisine\n" + "house,maison";

    private Reader inputString = new StringReader(mStrings);

    private String[] expected = {"bedroom,chambre,1\neveryday,tourlesjours,1\nor,ou,1\ntelevision, télé,1\nkitchen,cuisine,1\nhouse,maison,1" + "English,French\n"};

    @Test
    public void analyseSaveFileNameNum() throws Exception{
        files[0]=mFile;
        files[1]=mFile2;
        assertEquals(0, mFileCSVAnalyze.analyseSaveFileNameNum(files));
        files[0]=mFile4;
        files[1]=mFile6;
        assertEquals(-1, mFileCSVAnalyze.analyseSaveFileNameNum(files));
    }

    @Test
    public void analyseReadCSVFile() throws IOException {
        BufferedReader read = new BufferedReader(inputString);
        assertArrayEquals(expected, mFileCSVAnalyze.analyseReadCSVFile(mBufferedReader, 10, 200, 20));
    }
}