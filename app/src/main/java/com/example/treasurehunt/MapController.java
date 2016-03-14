package com.example.treasurehunt;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.example.treasurehunt.cell.Cell;

import java.util.Random;

/**
 * Created by library on 2016/03/14.
 */
public class MapController {
    private Cell cells[][];
    private BoardSize boardSize;
    private int totalTraps;

    public MapController() {
        boardSize = new BoardSize(16, 30);
    }

    private void initialMap(Context context) {
        int numberOfRows = boardSize.getRows();
        int numberOfColumns = boardSize.getCols();

        // We make more 2 row and column, the 0 row/column and the last one are
        // not showed
        cells = new Cell[numberOfRows + 2][numberOfColumns + 2];

        for (int row = 0; row < numberOfRows + 2; row++) {
            for (int column = 0; column < numberOfColumns + 2; column++) {
                cells[row][column] = new Cell(context);
                cells[row][column].setDefaults();

                final int currentRow = row;
                final int currentColumn = column;

                cells[row][column].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
//                        gameController.onClickOnCellHandle(currentRow, currentColumn);
                    }
                });

                // add Long Click listener
                // this is treated as right mouse click listener
                cells[row][column]
                        .setOnLongClickListener(new View.OnLongClickListener() {
                            public boolean onLongClick(View view) {
                                // simulate a left-right (middle) click
                                // if it is a long click on an opened trap then
                                // open all surrounding blocks
                                return onLongClickOnCellHandle(currentRow,
                                        currentColumn);
                            }
                        });
            }
        }
    }

    public void createMap(Context context) {
        initialMap(context);
    }


    /*
     * Generate the map
     *
     * @author 8A Tran Trong Viet
     *
     * @param rowClicked the position of clicked cell
     *
     * @param columnClicked the position of clicked cell
     */
    public void genMap(int rowClicked, int columnClicked) {
        genTreasure(rowClicked, columnClicked);
        genTraps(rowClicked, columnClicked);
        setTheNumberOfSurroundingTrap();
    }

    /*
     * Generate the treasure position in map
     *
     * @author 8C Pham Duy Hung
     *
     * @param rowClicked the position of the first-clicked-cell
     *
     * @param columnClicked the position of the first-clicked-cell
     */
    private void genTreasure(int rowClicked, int columnClicked) {
        Random rand = new Random();
        int treasureRow, treasureCol;
        int numberOfRows = boardSize.getRows();
        int numberOfColumns = boardSize.getCols();

        // Set the treasure position
        treasureRow = rand.nextInt(numberOfRows - 1) + 2;
        if (treasureRow >= numberOfRows) {
            treasureRow = numberOfRows - 2;
        }

        treasureCol = rand.nextInt(numberOfColumns - 1) + 2;
        if (treasureCol >= numberOfColumns) {
            treasureCol = numberOfColumns - 2;
        }

        // For debugging
//        Log.e("8A >>>>", "Treasure: " + treasureRow + " " + treasureCol);

        // Make sure the treasure is not near the clicked cell
        if (!isTreasureNear(treasureRow, treasureCol, rowClicked, columnClicked)) {
            if (treasureRow < numberOfRows / 2) {
                treasureRow = (numberOfRows - treasureRow) / 2;
                treasureCol = (numberOfColumns - treasureCol) / 2;
            } else {
                treasureRow = (treasureRow) / 2;
                treasureCol = (treasureCol) / 2;
            }
        }

        for (int previousRow = -1; previousRow < 2; previousRow++) {
            for (int previousColumn = -1; previousColumn < 2; previousColumn++) {
                if ((previousRow != 0) || (previousColumn != 0)) {
                    cells[treasureRow + previousRow][treasureCol
                            + previousColumn].setTrap();
                } else {
                    cells[treasureRow + previousRow][treasureCol
                            + previousColumn].setTreasure();
                }
            }
        }
    }

    /*
     * Generate the traps position in map
     *
     * @author 8C Pham Duy Hung
     *
     * @param rowClicked the position of the first-clicked-cell
     *
     * @param columnClicked the position of the first-clicked-cell
     */
    private void genTraps(int rowClicked, int columnClicked) {
        Random rand = new Random();
        int trapRow, trapColumn;
        int numberOfColumns = boardSize.getCols();
        int numberOfRows = boardSize.getRows();

        // set traps excluding the location where user clicked
        for (int row = 0; row < totalTraps - 8; row++) {
            trapRow = rand.nextInt(numberOfColumns);
            trapColumn = rand.nextInt(numberOfRows);

            // set the 8 surrounded cells of the clicked cell have no trap
            if (isNearTheClickedCell(trapRow, trapColumn, rowClicked,
                    columnClicked)) {
                if (cells[trapColumn + 1][trapRow + 1].hasTrap()
                        || cells[trapColumn + 1][trapRow + 1].hasTreasure()) {
                    row--; // trap is already there, don't repeat for same block
                }
                // plant trap at this location
                cells[trapColumn + 1][trapRow + 1].setTrap();
            }
            // exclude the user clicked location
            else {
                row--;
            }
        }
    }

    /*
     * Check if this cell is near the clicked cell
     *
     * @author 8C Pham Duy Hung
     *
     * @param rowCheck the row which want to check
     *
     * @param columnCheck the column which want to check
     *
     * @param rowClicked the row of the clicked cell
     *
     * @param columnClick the column of the clicked cell
     */
    private boolean isNearTheClickedCell(int rowCheck, int columnCheck,
                                         int rowClicked, int columnClicked) {
        return ((rowCheck != columnClicked) || (columnCheck != rowClicked))
                && ((rowCheck != columnClicked) || (columnCheck + 1 != rowClicked))
                && ((rowCheck != columnClicked) || (columnCheck + 2 != rowClicked))
                && ((rowCheck + 1 != columnClicked) || (columnCheck != rowClicked))
                && ((rowCheck + 1 != columnClicked) || (columnCheck + 1 != rowClicked))
                && ((rowCheck + 1 != columnClicked) || (columnCheck + 2 != rowClicked))
                && ((rowCheck + 2 != columnClicked) || (columnCheck != rowClicked))
                && ((rowCheck + 2 != columnClicked) || (columnCheck + 1 != rowClicked))
                && ((rowCheck + 2 != columnClicked) || (columnCheck + 2 != rowClicked));
    }


    /*
     * Check if the treasure is near the clicked cell
     *
     * @author 8A Tran Trong Viet
     *
     * @param rowCheck the row which want to check
     *
     * @param columnCheck the column which want to check
     *
     * @param rowClicked the row of the clicked cell
     *
     * @param columnClick the column of the clicked cell
     */
    private boolean isTreasureNear(int rowCheck, int columnCheck,
                                   int rowClicked, int columnClicked) {
        return (((rowCheck != columnClicked) || (columnCheck - 1 != rowClicked))
                && ((rowCheck != columnClicked) || (columnCheck != rowClicked))
                && ((rowCheck != columnClicked) || (columnCheck + 1 != rowClicked))
                && ((rowCheck != columnClicked) || (columnCheck + 2 != rowClicked))
                && ((rowCheck != columnClicked) || (columnCheck + 3 != rowClicked))
                && ((rowCheck + 1 != columnClicked) || (columnCheck - 1 != rowClicked))
                && ((rowCheck + 1 != columnClicked) || (columnCheck != rowClicked))
                && ((rowCheck + 1 != columnClicked) || (columnCheck + 1 != rowClicked))
                && ((rowCheck + 1 != columnClicked) || (columnCheck + 2 != rowClicked))
                && ((rowCheck + 1 != columnClicked) || (columnCheck + 3 != rowClicked))
                && ((rowCheck + 2 != columnClicked) || (columnCheck - 1 != rowClicked))
                && ((rowCheck + 2 != columnClicked) || (columnCheck != rowClicked))
                && ((rowCheck + 2 != columnClicked) || (columnCheck + 1 != rowClicked))
                && ((rowCheck + 2 != columnClicked) || (columnCheck + 2 != rowClicked))
                && ((rowCheck + 2 != columnClicked) || (columnCheck + 3 != rowClicked)));
    }

    /*
     * Set the number of surrounding trap
     *
     * @author 8C Pham Duy Hung
     */
    private void setTheNumberOfSurroundingTrap() {
        int nearByTrapCount;
        int numberOfRows = boardSize.getRows();
        int numberOfColumns = boardSize.getCols();

        // count number of traps in surrounding blocks
        for (int row = 0; row < numberOfRows + 2; row++) {
            for (int column = 0; column < numberOfColumns + 2; column++) {
                // for each block find nearby trap count
                nearByTrapCount = 0;
                if ((row != 0) && (row != (numberOfRows + 1)) && (column != 0)
                        && (column != (numberOfColumns + 1))) {
                    // check in all nearby blocks
                    for (int previousRow = -1; previousRow < 2; previousRow++) {
                        for (int previousColumn = -1; previousColumn < 2; previousColumn++) {
                            if (cells[row + previousRow][column
                                    + previousColumn].hasTrap()) {
                                // a trap was found so increment the counter
                                nearByTrapCount++;
                            }
                        }
                    }
                    cells[row][column]
                            .setNumberOfTrapsInSurrounding(nearByTrapCount);
                }
                // for side rows (0th and last row/column)
                // set count as 9 and mark it as opened
                else {
                    cells[row][column].setNumberOfTrapsInSurrounding(9);
                    cells[row][column].OpenCell();
                }

            }
        }
    }

    /*
    * This method handles the on click event
    *
    * @author 8C Pham Duy Hung
    *
    * @param currentRow the position of the clicked cell
    *
    * @param currentCol the position of the clicked cell
    */
    private void onClickOnCellHandle(int currentRow, int currentColumn) {

        if (!isGameStart) {
            startTimer();
            isGameStart = true;
        }

        if (!isMapGen) {
            mapController.genMap(currentRow, currentColumn);
            isMapGen = true;
        }

        if (!cells[currentRow][currentColumn].isFlagged()) {
            rippleUncover(currentRow, currentColumn);

            if (cells[currentRow][currentColumn].hasTrap()) {
                lives--;
                trapsRemain--;
                livesText.setText(lives);
                trapText.setText(trapsRemain);
                cells[currentRow][currentColumn].OpenCell();
                cells[currentRow][currentColumn].setFlag();
                if (lives <= 0) {
                    finishGame(currentRow, currentColumn);
                    livesText.setText(0);
                }
            }

            if (checkGameWin(cells[currentRow][currentColumn])) {
                winGame();
            }
        }
    }


    /*
     * Open the cells which surrounded the no-trap-surrounded cell continuously
     *
     * @author 8-B Pham Hung Cuong
     *
     * @param rowClicked the row of the clicked position
     *
     * @param columnClicked the column of the clicked position
     */
    private void rippleUncover(int rowClicked, int columnClicked) {
        if (cells[rowClicked][columnClicked].hasTrap()
                || cells[rowClicked][columnClicked].isFlagged()
                || cells[rowClicked][columnClicked].isDoubted()) {
            return;
        }

        if (!cells[rowClicked][columnClicked].isClickable()) {
            return;
        }

        cells[rowClicked][columnClicked].OpenCell();
        if (cells[rowClicked][columnClicked].getNumberOfTrapsInSurrounding() != 0) {
            return;
        }

        int numberOfRows = boardSize.getRows();
        int numberOfColumns = boardSize.getCols();

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                // check all the above checked conditions
                // if met then open subsequent blocks
                if (cells[rowClicked + row - 1][columnClicked + column - 1]
                        .isCovered()
                        && (rowClicked + row - 1 > 0)
                        && (columnClicked + column - 1 > 0)
                        && (rowClicked + row - 1 < numberOfRows + 1)
                        && (columnClicked + column - 1 < numberOfColumns + 1)) {
                    rippleUncover(rowClicked + row - 1, columnClicked + column
                            - 1);
                }
            }
        }
    }

    /*
     * Check the game wins or not (should do this for changing rules)
     *
     * @author 8C Pham Duy Hung
     *
     * @param cell the cell which is clicked
     */
    private boolean checkGameWin(Cell cell) {
        return cell.hasTreasure();
    }

    private void winGame() {
        // reset all stuffs
        stopTimer();
        isGameFinish = true;
        isGameStart = false;
        trapsRemain = 0;
        totalScore += 1000;
        scoreText.setText(totalScore);

        int numberOfRows = boardSize.getRows();
        int numberOfColumns = boardSize.getCols();

        // disable all buttons
        // set flagged all un-flagged blocks
        for (int row = 1; row < numberOfRows + 1; row++) {
            for (int column = 1; column < numberOfColumns + 1; column++) {
                cells[row][column].setClickable(false);
                if (cells[row][column].hasTrap()) {
                    cells[row][column].setTrapIcon(true);
                }
                if (cells[row][column].hasTreasure())
                    cells[row][column].setTreasure();
            }
        }

        popupHandler = new Handler();
        popupHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                mImgViewResult.setBackgroundResource(R.drawable.congrat);
                mImgViewResult.setVisibility(View.VISIBLE);
                mImgViewResult.bringToFront();
                mImgViewResult.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mImgViewResult.setVisibility(View.GONE);
                        showWinPopUp();
                        popupHandler.removeCallbacks(this);
                    }
                }, 2000);
            }
        }, 500);

    }

    /*
     * This method handles the on long click event
     *
     * @author 8C Pham Duy Hung
     *
     * @param currentRow the position of the clicked cell
     *
     * @param currentCol the position of the clicked cell
     */
    private boolean onLongClickOnCellHandle(int currentRow, int currentColumn) {

        if (!cells[currentRow][currentColumn].isCovered()
                && (cells[currentRow][currentColumn]
                .getNumberOfTrapsInSurrounding() > 0) && !isGameOver) {
            int nearbyFlaggedBlocks = 0;
            for (int previousRow = -1; previousRow < 2; previousRow++) {
                for (int previousColumn = -1; previousColumn < 2; previousColumn++) {
                    if (cells[currentRow + previousRow][currentColumn
                            + previousColumn].isFlagged()) {
                        nearbyFlaggedBlocks++;
                    }
                }
            }

            // if flagged block count is equal to nearby trap count then open
            // nearby blocks
            if (nearbyFlaggedBlocks == cells[currentRow][currentColumn]
                    .getNumberOfTrapsInSurrounding()) {
                for (int previousRow = -1; previousRow < 2; previousRow++) {
                    for (int previousColumn = -1; previousColumn < 2; previousColumn++) {
                        // don't open flagged blocks
                        if (!cells[currentRow + previousRow][currentColumn
                                + previousColumn].isFlagged()) {
                            // open blocks till we get
                            // numbered block
                            rippleUncover(currentRow + previousRow,
                                    currentColumn + previousColumn);

                            // did we clicked a trap
                            if (cells[currentRow + previousRow][currentColumn
                                    + previousColumn].hasTrap()) {

                                cells[currentRow + previousRow][currentColumn
                                        + previousColumn].OpenCell();
                                lives--;
                                livesText.setText(lives);
                                trapsRemain--;
                                trapText.setText(trapsRemain);
                                if (lives <= 0) {
                                    livesText.setText("0");
                                    finishGame(0, 0);
                                }

                            }
                        }
                    }
                }
            }
            return true;
        }

        // if clicked cells is enabled, clickable or flagged

        flagAndDoubtHandle(currentRow, currentColumn);
        return true;
    }

    /*
       * This method handles the flag and doubt situations
       *
       * @author 8C Pham Duy Hung
       *
       * @param currentRow the position of the clicked cell
       *
       * @param currentCol the position of the clicked cell
       */
    private void flagAndDoubtHandle(int currentRow, int currentColumn) {
        // we got 3 situations
        // 1. empty blocks to flagged
        // 2. flagged to question mark
        // 3. question mark to blank

        if (cells[currentRow][currentColumn].isClickable()
                && (cells[currentRow][currentColumn].isEnabled() || cells[currentRow][currentColumn]
                .isFlagged())) {
            mp.start();
            // case 1. set blank block to flagged
            if (!cells[currentRow][currentColumn].isFlagged()
                    && !cells[currentRow][currentColumn].isDoubted()) {
                cells[currentRow][currentColumn].setFlagIcon();
                cells[currentRow][currentColumn].setFlag();
                trapsRemain--; // reduce trap count
                trapText.setText(trapsRemain);
            }
            // case 2. set flagged to question mark
            else if (!cells[currentRow][currentColumn].isDoubted()) {
                cells[currentRow][currentColumn].setDoubt();
                cells[currentRow][currentColumn].setFlagIcon();
                cells[currentRow][currentColumn].setDoubtIcon(true);
                trapsRemain++; // increase trap count
                trapText.setText(trapsRemain);
            }
            // case 3. change to blank square
            else {
                cells[currentRow][currentColumn].clearAllIcons();
                // if it is flagged then increment trap count
                if (cells[currentRow][currentColumn].isFlagged()) {
                    trapsRemain++; // increase trap count
                    trapText.setText(trapsRemain);
                }
            }

        }
    }

    public void setTotalTraps(int totalTraps) {
        this.totalTraps = totalTraps;
    }

    public BoardSize getBoardSize() {
        return boardSize;
    }
}
