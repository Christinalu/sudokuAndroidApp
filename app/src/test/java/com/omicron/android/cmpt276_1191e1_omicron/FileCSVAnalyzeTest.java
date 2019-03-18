package com.omicron.android.cmpt276_1191e1_omicron;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

import static org.junit.Assert.*;

public class FileCSVAnalyzeTest {
    private FileCSVAnalyze mFileCSVAnalyze = new FileCSVAnalyze(150);
    private File mFile = new File("pkg_1.csv");
    private File mFile2 = new File("pkg_2.csv");
    private File mFile3 = new File("jhhs.pdf");
    private File mFile4 = new File("pkg_a3e4rw.dox");
    private File[] files = new File[2];

    private BufferedReader mBufferedReader;
//    private String test = "English, French, bedroom, chambre, everyday, tourlesjours, or, ou, television, télé," +
//            "kitchen, cuisine, house, maison, apartment, appartement,you, tu,have, avoir,she, elle, but, mais" ;

    private String mStrings = "English,French\n"+ "bedroom,chambre\n"+"everyday,tourlesjours\n"+"or,ou\n"+"television, télé\n"+"kitchen,cuisine\n"+"house,maison\n"+"street,rue\n"+
            "one,un\n"+"two,deux\n"+"three,trois";
    private String emptyString = "\n"+"";

    private Reader inputString = new StringReader(emptyString);
    private Reader inputString2 = new StringReader(mStrings);

    private String[] expected = {"English,French\nbedroom,chambre,1\neveryday,tourlesjours,1\nor,ou,1\ntelevision, télé,1\nkitchen,cuisine,1\nhouse,maison,1\nstreet,rue,1\none,un,1\ntwo,deux,1\nthree,trois,1\n", "English,French\n"};
    private String[] expected2 = {"",""};

    @Test
    public void analyseSaveFileNameNum() throws Exception{
        files[0]=mFile;
        files[1]=mFile2;
        assertEquals(0, mFileCSVAnalyze.analyseSaveFileNameNum(files));
        files[0]=mFile3;
        files[1]=mFile4;
        assertEquals(-1, mFileCSVAnalyze.analyseSaveFileNameNum(files));
    }

    @Test
    public void analyseReadCSVFile() throws IOException {
        //empty case
        BufferedReader read = new BufferedReader(inputString);
        assertArrayEquals(expected2, mFileCSVAnalyze.analyseReadCSVFile(read, 10, 200, 20));

        BufferedReader read2 = new BufferedReader(inputString2);
        String[] output = mFileCSVAnalyze.analyseReadCSVFile(read2, 15, 200, 20);
        assertArrayEquals(expected,output);
    }
}