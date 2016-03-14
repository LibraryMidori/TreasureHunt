package com.example.treasurehunt.game_parameters;

import com.example.treasurehunt.constant.GameParameterConstant;

/**
 * Created by library on 2016/03/14.
 */
public class SetupParamsNormalModeLevel5 implements SetupGameParameters {
    @Override
    public int getTotalTrap() {
        return GameParameterConstant.getMinTraps() + 5;
    }

    @Override
    public int getPlayTime() {
        return GameParameterConstant.getMaxTime() - 60;
    }
}