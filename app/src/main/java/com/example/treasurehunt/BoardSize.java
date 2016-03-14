package com.example.treasurehunt;

/**
 * Created by library on 2016/03/14.
 */
public class BoardSize {
    private int rows;
    private int cols;

    public BoardSize(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }
}
