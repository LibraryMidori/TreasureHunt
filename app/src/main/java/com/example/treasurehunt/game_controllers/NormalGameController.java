package com.example.treasurehunt.game_controllers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.treasurehunt.BoardSize;
import com.example.treasurehunt.GameStatus;
import com.example.treasurehunt.MapController;
import com.example.treasurehunt.Timer;
import com.example.treasurehunt.game_parameters.SetupGameParameters;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel1;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel10;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel11;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel12;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel13;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel14;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel15;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel2;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel3;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel4;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel5;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel6;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel7;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel8;
import com.example.treasurehunt.game_parameters.SetupParamsNormalModeLevel9;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by library on 2016/03/14.
 */
public class NormalGameController implements Observer {

    private boolean isGameOver;
    private boolean isGameFinish = false;
    private boolean isGameStart;
    private boolean isMapGen;

    // Tracking time
    private int timer;

    private GameStatus gameStatus;
    private MapController mapController;

    public NormalGameController(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        mapController = new MapController();
        initialGame();
    }

    private void initialGame() {
        // Default setting
        isGameOver = false;
        isGameStart = false;
        isMapGen = false;

        // Tracking time
        Timer.getInstance().addObserver(this);

        switch (gameStatus.getLevel()) {
            case 1:
                setupGame(new SetupParamsNormalModeLevel1());
                break;
            case 2:
                setupGame(new SetupParamsNormalModeLevel2());
                break;
            case 3:
                setupGame(new SetupParamsNormalModeLevel3());
                break;
            case 4:
                setupGame(new SetupParamsNormalModeLevel4());
                break;
            case 5:
                setupGame(new SetupParamsNormalModeLevel5());
                break;
            case 6:
                setupGame(new SetupParamsNormalModeLevel6());
                break;
            case 7:
                setupGame(new SetupParamsNormalModeLevel7());
                break;
            case 8:
                setupGame(new SetupParamsNormalModeLevel8());
                break;
            case 9:
                setupGame(new SetupParamsNormalModeLevel9());
                break;
            case 10:
                setupGame(new SetupParamsNormalModeLevel10());
                break;
            case 11:
                setupGame(new SetupParamsNormalModeLevel11());
                break;
            case 12:
                setupGame(new SetupParamsNormalModeLevel12());
                break;
            case 13:
                setupGame(new SetupParamsNormalModeLevel13());
                break;
            case 14:
                setupGame(new SetupParamsNormalModeLevel14());
                break;
            case 15:
                setupGame(new SetupParamsNormalModeLevel15());
                break;
            default:
                break;
        }
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public BoardSize getBoardSize() {
        return mapController.getBoardSize();
    }

    private void setupGame(SetupGameParameters gameParameters) {
        gameStatus.setTraps(gameParameters.getTotalTrap());
        timer = gameParameters.getPlayTime();
        mapController.setTotalTraps(gameParameters.getTotalTrap());
    }

    /*
     * Create new map
     *
     * @author 8C Pham Duy Hung
     */
    public void createMap(Context context) {
        mapController.createMap(context);
    }

    public void backBtnPressed(SharedPreferences gamePrefs) {
        if (gameStatus.getLives() > 0 && !isGameFinish) {
            saveGameState(gameStatus.getLevel(), gameStatus.getScore(), gameStatus.getLives(), gamePrefs);
        }
    }

    /*
     * Save game to continue
     *
     * @author 8A Tran Trong Viet
     *
     * @param _level the current level
     *
     * @param _score the current score
     *
     * @param _lives the current lives
     */
    private void saveGameState(int _level, int _score, int _lives, SharedPreferences gamePrefs) {
        SharedPreferences.Editor gameStateEdit = gamePrefs.edit();
        gameStateEdit.putString("saveGame", _level + " - " + _score + " - " + _lives);
        gameStateEdit.commit();
    }

    /*
     * Start time time
     *
     * @author: 8B Pham Hung Cuong
     */
    public void startTimer() {
        Timer.getInstance().startTimer(timer);
    }

	/*
     * Stop the time
	 *
	 * @author 8B Pham Hung Cuong
	 */

    public void stopTimer() {
        Timer.getInstance().stopTimer();
    }

    @Override
    public void update(Observable observable, Object data) {

    }
}
