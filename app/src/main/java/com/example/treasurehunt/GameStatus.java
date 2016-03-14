package com.example.treasurehunt;

/**
 * Created by library on 2016/03/14.
 */
public class GameStatus {
    private int lives;
    private int score;
    private int level;
    private int traps;

    public GameStatus(int lives, int score, int level) {
        this.lives = lives;
        this.score = score;
        this.level = level;
    }


    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLevel() {
        return level;
    }

    public void nextLevel() {
        this.level++;
    }

    public void resetLevel() {
        this.level = 0;
    }

    public int getTraps() {
        return traps;
    }

    public void setTraps(int traps) {
        this.traps = traps;
    }
}
