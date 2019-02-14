package com.omicron.android.cmpt276_1191e1_omicron;

public class PuzzleCheck {

    /*
     * This Class checks if the user has correctly completed the puzzle
    */

    private int size;
    public boolean isTrue;

    public PuzzleCheck(int[][] arr) {
        //void
    }

	public void PuzzleCheckStart(int[][] arr) {
		size = arr.length;
		isTrue = Check(arr);
	}

    private void zero(int[][] Puzzle) {
        for (int i=0; i<Puzzle.length; i++) {
            for(int j=0; j<Puzzle[0].length; j++) {
                Puzzle[i][j]=0;
            }
        }
    }
    public void copyArr(int[][] arr1, int[][] arr2) {
        //copy arr1 to arr2
        int colrow = size;
        for (int i=0; i<colrow; i++) {
            for (int j=0; j<colrow; j++) {
                arr2[i][j] = arr1[i][j];
            }
        }
    }
    private boolean Check(int[][] arr) {
        //returns FALSE on fail, TRUE on pass
        //first pair of for loops check to see if there are duplicate numbers in the rows. If there is, return false, second pair checks for columns
        int[][] temparr = new int[size][size];
        int num;

        zero(temparr);
        // temp array here is not needed - it uses too much space - only 1 row needed as reference
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (arr[i][j]!=0) {
                    num = arr[i][j] - 1;
                    temparr[i][num] = 1;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (temparr[i][j] == 0)
                    return false;
            }
        }

        // loops up to here check if the array was been filled

        zero(temparr);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (arr[j][i]!=0) {
                    num = arr[j][i] - 1;
                    temparr[num][i] = 1;
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (temparr[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}