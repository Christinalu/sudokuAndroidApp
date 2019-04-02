package com.omicron.android.cmpt276_1191e1_omicron;

import com.omicron.android.cmpt276_1191e1_omicron.Model.Pair;

import org.junit.Test;

import static java.lang.Math.sqrt;
import static org.junit.Assert.*;

public class SudokuGeneratorTest {
    //usrDiffPref ranges from [0,2] and psize ranges from [0,3] where:
    //usrDiffPref: 0 = easy difficulty, 1 = medium difficulty, 2 = hard difficulty
    //psize: 0 = 4x4, 1 = 6x6, 2 = 9x9, 3 = 12x12
    private int usrDiffPref = 1;
    private int psize = 0;
    private SudokuGenerator usrSudokuArr = new SudokuGenerator (usrDiffPref,psize);
    //get index of puzzle to use given usrDiffPref and psize from mainActivity
    private int pindex = psize*3 + usrDiffPref;
    //get length of 1 row (this will be the max number of different entries
    private int len = usrSudokuArr.getPuzzle()[0].length;
    //calculate number of sections on x axis
    private int puzz_numHsec = (int) sqrt(len);
    //calculate number of sections on the y axis
    private int puzz_numVsec = len/puzz_numHsec;
    private int sec_numRows = puzz_numHsec;
    private int sec_numCols = puzz_numVsec;
    private int[][] puzz;
    private int[][] puzzsol;

    @Test
    public void Scramble() {
        /*
        tests scramble. upon creation of a SudokuGenerator, it will call scramble as per the constructor. Thus we can simply test the Puzzle and PuzzleSol to see if it worked
        if the scramble worked, then each row, column, and section should have at most 1 of each entry (each entry will represent a number, 1 through len, with index 0 being 0, and index len being len) in our arr
        scramble also calls: copyarr(), randomizeOrder()
        */
        int duplicates = 0;
        //make arrays that will track which numbers have been used, then zero it
        int[] arr1 = new int[len+1];
        int[] arr2 = new int[len+1];
        zero(arr1);
        zero(arr2);
        puzz = usrSudokuArr.getPuzzle();
        puzzsol = usrSudokuArr.getSolution();
        //check by rows
        for (int i=0; i<len; i++) {
            for (int j=0; j<len; j++) {
                //whichever number is in puzz[i][j] and puzzsol[i][j] will be recorded into their respective arrays
                arr1[puzz[i][j]]++;
                arr2[puzzsol[i][j]]++;
            }
            //if arr[k] > 1 other than arr[0] (we ignore zero entries, as they represent an empty entry)
            for (int k=1; k<len+1; k++) {
                //if ether arrays have more than one increment, we have a duplicate in a row
                if (arr1[k] > 1 || arr2[k] > 1) {
                    duplicates++;
                }
            }
            zero(arr1);
            zero(arr2);
        }
        //check by cols
        for (int i=0; i<len; i++) {
            for (int j=0; j<len; j++) {
                //whichever number is in puzz[j][i] and puzzsol[j][i] will be recorded into their respective arrays
                arr1[puzz[j][i]]++;
                arr2[puzzsol[j][i]]++;
            }
            for (int k=1; k<len+1; k++) {
                //if ether arrays have more than one increment, we have a duplicate in a row
                if (arr1[k] > 1 || arr2[k] > 1) {
                    duplicates++;
                }
            }
            zero(arr1);
            zero(arr2);
        }
        //check sections
        for (int i=0; i<sec_numRows; i++) {
            for (int j=0; j<sec_numCols; j++) {
                //whichever number is in puzz[i][j] and puzzsol[i][j] will be recorded into their respective arrays
                arr1[puzz[i][j]]++;
                arr2[puzzsol[i][j]]++;
            }
            for (int k=1; k<len+1; k++) {
                //if ether arrays have more than one increment, we have a duplicate in a row
                if (arr1[k] > 1 || arr2[k] > 1) {
                    duplicates++;
                }
            }
        }
        //lets make sure scramble scrambled Puzzle and PuzzleSol equally
        for (int i=0; i<len; i++) {
            for (int j = 0; j < len; j++) {
                //make sure all entries in Puzzle[i][j] and PuzzleSol[i][j] are equal (with the exception of Puzzle[i][j] == 0, since these are the empty entries for the user to complete
                if (puzz[i][j] != puzzsol[i][j]) {
                    if (puzz[i][j] != 0) {
                        duplicates++;
                    }
                }
            }
        }
        assertEquals(0, duplicates);
    }
    @Test
    public void GetSolution() {
        //returns PuzzleSol
        int check = 0;
        puzz = usrSudokuArr.getSolution();
        puzzsol = usrSudokuArr.PuzzleSol;
        for (int i=0; i<len; i++) {
            for (int j=0; j<len; j++) {
                if (puzz[i][j] != puzzsol[i][j]) {
                    check++;
                }
            }
        }
        assertEquals(0, check);
    }
    @Test
    public void GetPuzzle() {
        //returns Puzzle
        int check = 0;
        puzz = usrSudokuArr.getPuzzle();
        puzzsol = usrSudokuArr.Puzzle;
        for (int i=0; i<len; i++) {
            for (int j=0; j<len; j++) {
                if (puzz[i][j] != puzzsol[i][j]) {
                    check++;
                }
            }
        }
        assertEquals(0, check);
    }
    @Test
    public void SetPuzzleVal() {
        //sets a value at (x,y) on Puzzle... make sure your (x,y) exists in the puzzle for the test... otherwise you will get out of bounds exception
        int val = 99;
        int x = 3;
        int y = 3;
        usrSudokuArr.setPuzzleVal(val,x,y);
        puzz = usrSudokuArr.getPuzzle();
        assertEquals(val, puzz[x][y]);
    }
    @Test
    public void GetconflictArr() {
        //conflictArr is initialed to zero, so just make sure its all zeros
        int check = 0;
        puzz = usrSudokuArr.getconflictArr();
        for (int i=0; i<len; i++) {
            for (int j=0; j<len; j++) {
                if (puzz[i][j] != 0) {
                    check++;
                }
            }
        }
        assertEquals(0, check);
    }
    @Test
    public void AddHistoryANDhistoryisEmpty() {
        //add a History entry, then check if the List is empty
        //this test tests addHistory() and historyisEmpty() functions
        int check = 0;
        int val = 99;
        int x = 3;
        int y = 3;
        usrSudokuArr.setPuzzleVal(val,x,y);
        usrSudokuArr.addHistroy(x,y);
        if(usrSudokuArr.historyisEmpty()) {
            check++;
        }
        //now remove the history to make sure it was added in the first place (test accuracy of historyisEmpty())
        usrSudokuArr.removeHistory();
        //will segmentation fault if History is empty (it is the user's responsibilty to call historyisEmpty() to check before removing)
        if(!usrSudokuArr.historyisEmpty()) {
            check++;
        }
        assertEquals(0, check);
    }

    @Test
    public void ResetPuzzleandclearHistory() {
        //reset puzzle copies the stored original array to the user-active array, and clears the History list. Lets insert a value into some location and insert a Entry into History and see if it actually resets
        //resetPuzzle() also calls clearHistory() and will only pass if it works
        int check = 0;
        int val = 99;
        int x = 3;
        int y = 3;
        int sqrFilled;
        usrSudokuArr.setPuzzleVal(val,x,y);
        usrSudokuArr.addHistroy(x,y);
        puzz = usrSudokuArr.getPuzzle();
        sqrFilled = usrSudokuArr.getSqrFilled();
        //add +1 to sqrFilled to simulate an entry
        sqrFilled++;
        usrSudokuArr.resetPuzzle();
        puzzsol = usrSudokuArr.getPuzzle();
        //check and make sure History is now empty
        if(!usrSudokuArr.historyisEmpty()) {
            check++;
        }
        //check and make sure puzzle has reset
        for (int i=0; i<len; i++) {
            for (int j=0; j<len; j++) {
                if (puzz[i][j] != puzzsol[i][j]) {
                    check++;
                }
            }
        }
        if(usrSudokuArr.getSqrFilled()==sqrFilled){
            check++;
        }
        assertEquals(0, check);
    }
    @Test
    public void RemoveHistory() {
        //removes a Entry from History
        int check = 0;
        int val = 99;
        int x = 3;
        int y = 3;
        usrSudokuArr.setPuzzleVal(val,x,y);
        usrSudokuArr.addHistroy(x,y);
        usrSudokuArr.removeHistory();
        //check to see if History Entry was removed
        if (!usrSudokuArr.historyisEmpty()) {
            check++;
        }
        assertEquals(0, check);
    }
    @Test
    public void Track() {
        //track takes in a (x,y) coordinate as a result of a user input. It then increments sqrFilled to track if the Puzzle has been completed, whether it is correct or not. If it is filled, it allows the Puzzle to be checked, for efficiency.
        int check = 0;
        int val = 99;
        int x = 3;
        int y = 3;
        int sqrFilled = usrSudokuArr.getSqrFilled();
        puzz = usrSudokuArr.getPuzzle();
        //find an empty square in Puzzle to fill for the test
        for (int i=0; i<len; i++) {
            for (int j=0; j<len; j++) {
                if (puzz[i][j]==0) {
                    usrSudokuArr.setPuzzleVal(val, x, y);
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        Pair xy = new Pair(x,y);
        //track will check to see if the input is in a valid location (does not already contain a value) and increment sqrFilled
        usrSudokuArr.track(xy);
        if (usrSudokuArr.getSqrFilled()==sqrFilled) {
            check++;
        }
        assertEquals(0, check);
    }
    @Test
    public void CanCheck() {
        //function returns enableCheck which allows a function to begin checking (for efficiency) the puzzle to see if it is actually correct
        //is false to begin with
        puzz = usrSudokuArr.getSolution();
        int check = 0;
        int x = 0;
        int y = 0;
        Pair xy = new Pair(x,y);
        int val = puzz[x][y];
        //set puzz[0][0] to 0 to allow it to be tracked
        puzz[x][y] = 0;
        if (usrSudokuArr.canCheck()) {
            check++;
        }
        //
        usrSudokuArr.setPuzzle(puzz);
        usrSudokuArr.setSqrFilled();
        usrSudokuArr.track(xy);
        usrSudokuArr.setPuzzleVal(val,x,y);
        if (!usrSudokuArr.canCheck()) {
            check++;
        }
        assertEquals(0, check);
    }
    @Test
    public void CheckDuplicate() {
        //checkDuplicate() is called after a input is made into the Puzzle at coordinates (x,y). It then checks the row x, col y, and section to find if any duplicate entries can be found
        //we can insert a puzzle with or without duplicates and see if the function can find it BUT it returns false if PuzzleOriginal[x][y] != 0 (a protection to ignore calls on inputs that are not accepted), so we must also set a PuzzleOriginal
        //to test other sizes, you will need to hardcode an array of your pick with the relevant size
        int[][] testpuzz1 = {{1,1,3,4},{2,3,4,1},{3,4,1,2},{4,1,2,3}};
        int[][] testpuzz2 = {{0,1,3,4},{2,3,4,1},{3,4,1,2},{4,1,2,3}};
        usrSudokuArr.setPuzzle(testpuzz1);
        usrSudokuArr.setPuzzleOriginal(testpuzz2);
        int x = 0;
        int y = 0;
        boolean returns = usrSudokuArr.checkDuplicate(x,y);;
        assertEquals(true, returns);
    }
    //use to zero 1D arrays
    public void zero(int[] arr)
    {
        for (int i=0; i<arr.length; i++) arr[i] = 0;
    }

}