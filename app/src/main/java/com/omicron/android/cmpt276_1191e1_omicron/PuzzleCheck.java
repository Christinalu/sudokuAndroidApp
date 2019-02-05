package com.omicron.android.cmpt276_1191e1_omicron;

public class PuzzleCheck {
    private int size;
    public boolean isTrue;

    public PuzzleCheck(int[][] arr) {
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
    private boolean Check(int[][] arr) {
        //returns FALSE on fail, TRUE on pass
        //first pair of for loops check to see if there are duplicate numbers in the rows. If there is, return false, second pair checks for columns
        int[][] temparr = new int[size][size];
        int num;
        zero(temparr);
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