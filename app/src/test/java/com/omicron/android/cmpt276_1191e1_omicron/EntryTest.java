package com.omicron.android.cmpt276_1191e1_omicron;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntryTest {
    int val = 99;
    int x = 1;
    int y = 1;
    Pair coor = new Pair(x,y);
    Entry e1 = new Entry(val,coor);
    Entry e2 = new Entry(e1);

    @Test
    public void GetValueANDgetCoordinate( ) {
        //checks return value in Entry from getValue() (returns value of Entry) and getCoordinate() (returns coordinate from Entry). If equal to initial values, it fails the test
        int check = 0;
        if (e1.getValue()!=val || e1.getCoordinate()!=coor || e2.getValue()!=val || e2.getCoordinate()!=coor) {
            check++;
        }
        assertEquals(0,check);
    }
    @Test
    public void Update( ) {
        //changes value of Entry
        int check = 0;
        int newVal = 50;
        int newX = 0;
        int newY = 0;
        Pair newCoor = new Pair(newX,newY);
        e1.update(newVal,newCoor);
        if (e1.getValue()!=newVal || e1.getCoordinate()!=newCoor) {
            check++;
        }
        assertEquals(0,check);
    }
}