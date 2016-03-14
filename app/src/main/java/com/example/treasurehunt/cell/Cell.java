package com.example.treasurehunt.cell;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

import com.example.treasurehunt.R;

import java.util.Random;

/*
 * The instance of this class is the cell in game
 * @author group 8
 */

public class Cell extends Button {
    /*
     * Properties
     */
    private CellStatus status;
    private CellContent content;
    private boolean isClickable;            // can cell accept click events
    private int numberOfTrapInSurrounding;    // number of traps in nearby cells

    /*
     * Constructor
     *
     * @author 8B Pham Hung Cuong
     *
     * @param context context
     */
    public Cell(Context context) {
        super(context);
    }

    /*
     * Constructor
     *
     * @author 8A Tran Trong Viet
     */
    public Cell(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * Constructor
     *
     * @author 8A Tran Trong Viet
     */
    public Cell(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*
     * Set default properties for the Cell
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public void setDefaults() {
        status = CellStatus.COVER;
        isClickable = true;
        numberOfTrapInSurrounding = 0;

        Random r = new Random();
        switch (r.nextInt() % 2) {
            case 0:
                this.setBackgroundResource(R.drawable.cell1);
                break;
            case 1:
                this.setBackgroundResource(R.drawable.cell2);
                break;
            default:
                this.setBackgroundResource(R.drawable.cell1);
                break;
        }
    }

    /*
     * Is cell covered?
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public boolean isCovered() {
        return status == CellStatus.COVER;
    }

    /*
     * Is cell a treasure
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public boolean hasTreasure() {
        return content == CellContent.TREASURE;
    }

    /*
     * Is cell a trap
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public boolean hasTrap() {
        return content == CellContent.TRAPPED;
    }

    /*
     * Is cell flagged
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public boolean isFlagged() {
        return status == CellStatus.FLAGGED;
    }

    /*
     * Is cell marked as doubt
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public boolean isDoubted() {
        return status == CellStatus.DOUBT;
    }

    /*
     * Can cell receive click event
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public boolean isClickable() {
        return isClickable;
    }

    /*
     * Mark cell as flagged
     *
     * @author 8A Tran Trong Viet
     *
     */
    public void setFlag() {
        status = CellStatus.FLAGGED;
        isClickable = true;
    }

    /*
     * Set question mark for the cell
     *
     * @author 8A Tran Trong Viet
     *
     */
    public void setDoubt() {
        status = CellStatus.DOUBT;
        isClickable = true;
    }

    /*
     * Mark the cell as disabled/opened and update the number of nearby traps
     *
     * @author 8A Tran Trong Viet
     *
     * @param number number of traps surrounded this cell
     */
    public void setNumberOfSurroundingTraps(int number) {
        this.setBackgroundResource(R.drawable.empty);
        updateNumber(number);
    }

    /*
     * Set trap icon for cell and set cell as disabled/opened if false is passed
     *
     * @author 8A Tran Trong Viet
     *
     * @param enabled boolean variable
     */
    public void setTrapIcon(boolean enabled) {
        if (!enabled) {
            this.setBackgroundResource(R.drawable.trap);
            this.setTextColor(Color.RED);
        } else {
            this.setTextColor(Color.BLACK);
        }
    }

    /*
     * Set trap as flagged and set cell as disabled/opened if false is passed
     *
     * @author 8A Tran Trong Viet
     *
     * @param enabled boolean variable
     */
    public void setFlagIcon() {
        this.setBackgroundResource(R.drawable.flag);
    }

    /*
     * Set trap as doubt and set cell as disabled/opened if false is passed
     *
     * @author 8A Tran Trong Viet
     *
     * @param enabled boolean variable
     */
    public void setDoubtIcon(boolean enabled) {
        this.setBackgroundResource(R.drawable.doubt);
        this.setTextColor(enabled ? Color.BLACK : Color.RED);
    }

    /*
     * Clear all icons/text
     *
     * @author 8A Tran Trong Viet
     */
    public void clearAllIcons() {
        this.setBackgroundResource(R.drawable.cell1);
    }

    /*
     * Disable this cell
     *
     * @author 8C Pham Duy Hung
     */
    public void disable() {
        isClickable = false;
    }

    /*
     * Set cell as a treasure
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public void setTreasure() {
        content = CellContent.TREASURE;
        this.setBackgroundResource(R.drawable.treasure);
    }

    /*
     * Set cell as a trap underneath
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public void setTrap() {
        content = CellContent.TRAPPED;
    }

    /*
     * Uncover this cell
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public void OpenCell() {
        // cannot uncover a trap which is not covered
        if (!(status == CellStatus.COVER))
            return;

        status = CellStatus.OPEN;

        if (this.numberOfTrapInSurrounding == 0) {
            isClickable = false;
        }

        if (hasTrap()) {
            // check if it has trap
            setTrapIcon(false);
        } else {
            // update with the nearby trap count
            setNumberOfSurroundingTraps(numberOfTrapInSurrounding);
        }
    }

    /*
     * Set text as nearby trap count
     *
     * @author 8A Tran Trong Viet
     *
     * @param text number of trap surrounding
     */
    public void updateNumber(int text) {
        if (text != 0) {
            // select different color for each number
            // we have 1 - 8 trap count
            switch (text) {
                case 1:
                    this.setBackgroundResource(R.drawable.c1);
                    break;
                case 2:
                    this.setBackgroundResource(R.drawable.c2);
                    break;
                case 3:
                    this.setBackgroundResource(R.drawable.c3);
                    break;
                case 4:
                    this.setBackgroundResource(R.drawable.c4);
                    break;
                case 5:
                    this.setBackgroundResource(R.drawable.c5);
                    break;
                case 6:
                    this.setBackgroundResource(R.drawable.c6);
                    break;
                case 7:
                    this.setBackgroundResource(R.drawable.c7);
                    break;
                case 8:
                    this.setBackgroundResource(R.drawable.c8);
                    break;
                case 9:
                    // this.setBackgroundResource(R.drawable.empty);
                    break;
            }
        }
    }

    /*
     * Change the cell icon and color of opened cell
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public void triggerTrap() {
        // TODO: add more effect
        setTrapIcon(true);
    }

    /*
     * Get number of nearby traps
     *
     * @author 8A Tran Trong Viet
     *
     * @param
     */
    public int getNumberOfTrapsInSurrounding() {
        return numberOfTrapInSurrounding;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View#toString()
     */
    @Override
    public String toString() {
        if (hasTrap()) {
            return "This is a trap " + numberOfTrapInSurrounding;
        } else {
            return "This is not a trap " + numberOfTrapInSurrounding;
        }
    }

    /*
     * Set the numberOfTrapInSurrounding
     *
     * @author 8C Pham Duy Hung
     */
    public void setNumberOfTrapsInSurrounding(int number) {
        numberOfTrapInSurrounding = number;
    }
}
